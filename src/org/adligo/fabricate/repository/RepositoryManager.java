package org.adligo.fabricate.repository;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.models.dependencies.I_Dependency;
import org.adligo.fabricate.xml.io_v1.library_v1_0.DependencyType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.xml.bind.DatatypeConverter;

/**
 * This is a thread safe class that downloads
 * files from maven remote_repositories and puts them 
 * in the local_repository
 * @author scott
 *
 */
public class RepositoryManager {
  private static PrintStream OUT = System.out;
  private List<String> repositories_ = new ArrayList<String>();

  private final I_FabLog log_;
  private final I_FabFileIO fileIO_;
  private final String localRepo_;
  
  private I_RepositoryPathBuilder pathBuilder_;
  private int threads;

  
  @SuppressWarnings("boxing")
  public RepositoryManager(I_FabSystem sys, String repositoryPath) {
    log_ = sys.getLog();
    fileIO_ = sys.getFileIO();
    localRepo_ = repositoryPath;
  }
  
 
  
  public boolean find(I_Dependency dep) {
    String filePath = pathBuilder_.getArtifactPath(dep);
    return new File(filePath).exists();
   }
  /**
   * This method can be called by multiple thread 
   * so it must be threadsafe
   * @return
   * @throws ExecutionException for a not standard purpose
   * of the download and sha check sum attempts failed
   */
  public void findOrDownloadAndVerify(I_Dependency dep) throws IOException {
    if (find(dep)) {
      return;
    }
    
    CloseableHttpClient httpClient = HttpClients.createDefault();
    //don't use the share repositories_ on this thread
    List<String> threadRemoteRepos = new ArrayList<String>(repositories_);
    boolean successfulDownloads = false;
    for (String repo: threadRemoteRepos) {
      DefaultRepositoryPathBuilder remoteBuilder = 
          new DefaultRepositoryPathBuilder(repo, "/");
      String url = remoteBuilder.getArtifactUrl(dep);
      String filePath = pathBuilder_.getArtifactPath(dep);
      String folderPath = pathBuilder_.getFolderPath(dep);
      new File(folderPath).mkdirs();
      
      boolean downloadedFile = false;
      if (new File(filePath).exists()) {
        downloadedFile = true;
      } else {
        if (log_.isLogEnabled(RepositoryManager.class)) {
          log_.println("Trying to download "  + System.lineSeparator() +
              "\t" + repo + System.lineSeparator() +
              "\t" + url.substring(repo.length(), url.length()) + System.lineSeparator() +
              "\tto" + System.lineSeparator() + 
              "\t" + filePath);
        }
        try {
          fileIO_.downloadFile(url, filePath);
          if (log_.isLogEnabled(RepositoryManager.class)) {
            log_.println("Successful downloaded to " + System.lineSeparator() +
                "\t" + filePath);
          }
          downloadedFile = true;
        } catch (IOException e) {
          if (log_.isLogEnabled(RepositoryManager.class)) {
            log_.println("problem downloading " + url);
            e.printStackTrace();
          }
        }
      }
      
      if (downloadedFile) {
        
        String md5File = filePath + ".md5";
        boolean downloadedSha = false;
        if (new File(md5File).exists()) {
          downloadedSha = true;
        } else {
          String md5Url = url + ".md5";
          if (log_.isLogEnabled(RepositoryManager.class)) {
            log_.println("Trying to download "  + System.lineSeparator() +
                "\t" + repo + System.lineSeparator() +
                "\t" + md5Url.substring(repo.length(), md5Url.length()) + System.lineSeparator() +
                "\tto" + System.lineSeparator() + 
                "\t" + md5File);
          }
          try {
            fileIO_.downloadFile(url, md5File);
            if (log_.isLogEnabled(RepositoryManager.class)) {
              log_.println("Successful downloaded to " + System.lineSeparator() +
                  "\t" + md5File);
            }
            downloadedSha = true;
          } catch (IOException e) {
            if (log_.isLogEnabled(RepositoryManager.class)) {
              log_.println("problem downloading " + url);
              e.printStackTrace();
            }
          }
        }
        if (downloadedSha) {
          
          byte[] b;
          
          FileInputStream fis = new FileInputStream(new File(md5File));
          try {
            b = Files.readAllBytes(Paths.get(new File(filePath).toURI()));
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.reset();
            md.update(b);
            byte[] hash = md.digest();
            String actual = DatatypeConverter.printHexBinary(hash);
            actual = actual.toLowerCase();
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            byte [] bytes = new byte[1];
            int counter = 0;
            while (fis.read(bytes) != -1) {
              baos.write(bytes);
              counter++;
              if (counter >= 100) {
                break;
              }
            }
            
            if (bytes != null) {
              String line = new String(baos.toByteArray(),"UTF-8");
              if (actual.equals(line)) {
                successfulDownloads = true;
                break;
              } else {
                if (log_.isLogEnabled(RepositoryManager.class)) {
                  log_.println("The file md5 " + System.lineSeparator() +
                      actual + System.lineSeparator() +
                      "did not match the .md5 " + System.lineSeparator() +
                      md5File + System.lineSeparator() + 
                      line);
                  
                }
              }
            }
          } catch (IOException e) {
            //guess it was a bad download?
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(OUT);
          } finally {
            try {
              fis.close();
            } catch (IOException x) {
              
            }
          }
          if (!successfulDownloads) {
            new File(filePath).delete();
            new File(md5File).delete();
          }
        }
      }
    }

    if (log_.isLogEnabled(RepositoryManager.class)) {
      DefaultRepositoryPathBuilder builder = 
          new DefaultRepositoryPathBuilder("", "/");
      log_.println(builder.getArtifactUrl(dep) +
          " was sucessful? " + successfulDownloads);
    }
    if (!successfulDownloads) {
      DefaultRepositoryPathBuilder builder = 
            new DefaultRepositoryPathBuilder("any remote repository ", "/");
      throw new IllegalStateException("Unable to download from " + 
          builder.getArtifactPath(dep));
    }
  }

  public void setPathBuilder(I_RepositoryPathBuilder pathBuilder) {
    pathBuilder_ = pathBuilder;
  }

  public void setRepositories(List<String> repositories ) {
    CloseableHttpClient httpClient = HttpClients.createDefault();
    for (String repo: repositories) {
      final String repoUrl = repo;
      HttpGet get = new HttpGet(repo);
      try {
        int status = httpClient.execute(get, new ResponseHandler<Integer>() {

                  public Integer handleResponse(
                          final HttpResponse response) throws ClientProtocolException, IOException {
                    return response.getStatusLine().getStatusCode();
                  }

              });
        if (status < 300) {
          repositories_.add(repoUrl);
        }
      } catch (IOException e) {
        //do nothing some server is down.
      }
      
    }
    try {
      httpClient.close();
    } catch (IOException e) {
      //do nothing, what could you do close it again
    }
    if (repositories_.size() == 0) {
      throw new RuntimeException("Unable to reach any remote repositories.");
    }
  }
}
