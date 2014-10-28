package org.adligo.fabricate.external;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.xml.io.library.DependencyType;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.File;
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
public class RepositoryDownloader {
  private static PrintStream OUT = System.out;
  
  private List<String> repositories_ = new ArrayList<String>();
  
  
  private I_LocalRepositoryPathBuilder pathBuilder_;
  private I_FabContext ctx_;
  private String localRepo_;
  
  @SuppressWarnings("boxing")
  public RepositoryDownloader(List<String> repositories, 
      I_LocalRepositoryPathBuilder pathBuilder, I_FabContext ctx) {
    ctx_ = ctx;
    localRepo_ = ctx.getLocalRepositoryPath();
    
    pathBuilder_ = pathBuilder;
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
        if (status == 200) {
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
      throw new RuntimeException("All of the remote repositories are down.");
    }
  }
  
  public boolean find(DependencyType dep) {
    String filePath = pathBuilder_.getPath(dep);
    return new File(filePath).exists();
   }
  /**
   * This method can be called by multiple thread 
   * so it must be threadsafe
   * @return
   * @throws ExecutionException for a not standard purpose
   * of the download and sha check sum attempts failed
   */
  public void findOrDownloadAndSha1(DependencyType dep) throws IOException {
    if (find(dep)) {
      return;
    }
    
    CloseableHttpClient httpClient = HttpClients.createDefault();
    //don't use the share repositories_ on this thread
    List<String> threadRemoteRepos = new ArrayList<String>(repositories_);
    boolean successfulDownloads = false;
    for (String repo: threadRemoteRepos) {
      DefaultLocalRepositoryPathBuilder remoteBuilder = 
          new DefaultLocalRepositoryPathBuilder(repo, "/");
      String url = remoteBuilder.getPath(dep);
      String filePath = pathBuilder_.getPath(dep);
      String folderPath = pathBuilder_.getFolderPath(dep);
      new File(folderPath).mkdirs();
      
      boolean downloadedFile = false;
      if (new File(filePath).exists()) {
        downloadedFile = true;
      } else {
        if (ctx_.isLogEnabled(RepositoryDownloader.class)) {
          OUT.println("Trying to download " + url + System.lineSeparator() +
              "\t to " + filePath);
        }
        try {
          
          downloadFile(httpClient, url, filePath);
          if (ctx_.isLogEnabled(RepositoryDownloader.class)) {
            OUT.println("Downloaded " + url + System.lineSeparator() +
                "\t to " + filePath);
          }
          downloadedFile = true;
        } catch (IOException e) {
          if (ctx_.isLogEnabled(RepositoryDownloader.class)) {
            OUT.println("problem downloading " + url);
            e.printStackTrace();
          }
        }
      }
      
      if (downloadedFile) {
        
        String shaFile = filePath + ".sha1";
        boolean downloadedSha = false;
        if (new File(shaFile).exists()) {
          downloadedSha = true;
        } else {
          String shaUrl = url + ".sha1";
          if (ctx_.isLogEnabled(RepositoryDownloader.class)) {
            OUT.println("Trying to download " + shaUrl + System.lineSeparator() +
                "\t to " + shaFile);
          }
          try {
            downloadFile(httpClient, shaUrl, shaFile);
            if (ctx_.isLogEnabled(RepositoryDownloader.class)) {
              OUT.println("Download " + shaUrl + System.lineSeparator() +
                  "\t to " + shaFile);
            }
            downloadedSha = true;
          } catch (IOException e) {
            if (ctx_.isLogEnabled(RepositoryDownloader.class)) {
              OUT.println("problem downloading " + url);
              e.printStackTrace();
            }
          }
        }
        if (downloadedSha) {
          
          byte[] b;
          
          try {
            b = Files.readAllBytes(Paths.get(new File(filePath).toURI()));
            byte[] hash = MessageDigest.getInstance("MD5").digest(b);
            String actual = DatatypeConverter.printHexBinary(hash);
            
            List<String> lines = Files.readAllLines(Paths.get(new File(shaFile).toURI()));
            if (lines != null) {
              if (lines.size() >= 1) {
                String line = lines.get(0);
                if (actual.equals(line)) {
                  successfulDownloads = true;
                  break;
                }
              }
            }
          } catch (IOException e) {
            //guess it was a bad download?
          } catch (NoSuchAlgorithmException e) {
            e.printStackTrace(OUT);
          }
          if (!successfulDownloads) {
            new File(filePath).delete();
            new File(shaFile).delete();
          }
        }
      }
    }
    if (!successfulDownloads) {
      DefaultLocalRepositoryPathBuilder builder = 
            new DefaultLocalRepositoryPathBuilder("any remote repository ", "/");
      throw new IOException("Unable to download from " + 
          builder.getPath(dep));
    }
  }

  private void downloadFile(CloseableHttpClient httpClient, String url, String file) throws IOException {
    HttpEntity entity = null;
    try {
      CloseableHttpResponse resp = httpClient.execute(new HttpGet(url));
      entity = resp.getEntity();
      
    } catch (ClientProtocolException x) {
      throw new IOException(x);
    }
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(new File(file));
    } catch (FileNotFoundException x) {
      throw new IOException(x);
    }
    InputStream in = null;
    try {
      in = entity.getContent();
      
      byte [] bytes = new byte[1];
      while (in.read(bytes) != -1) {
        fos.write(bytes);
      }
    } catch (IOException x) {
      throw x;
    } finally {
      try {
        fos.close();
      } catch (IOException x) {
        //do nothing
      }
      try {
        in.close();
      } catch (IOException x) {
        //do nothing
      }
    }
  }
}
