package org.adligo.fabricate.common.files;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.log.I_FabLogSystem;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class FabFileIO implements I_FabFileIO {
  /**
   * This should be thread safe since the following class has
   * the annotation @ThreadSafe; <br/>
   * @see org.apache.http.impl.client.CloseableHttpClient
   */
  private final CloseableHttpClient httpClient_;
  private final I_FabLogSystem sys_;
  private final I_FabLog log_;
  private final I_FabricateConstants constants_;
  
  public FabFileIO(I_FabLogSystem sys) {
    httpClient_ = HttpClients.createDefault();
    sys_ = sys;
    constants_ = sys.getConstants();
    log_ = sys.getLog();
  }

  public FabFileIO(I_FabLogSystem sys, CloseableHttpClient httpClient) {
    httpClient_ = httpClient;
    sys_ = sys;
    constants_ = sys.getConstants();
    log_ = sys.getLog();
  }
  
  @Override
  public File create(String filePath) throws IOException {
    File file = new File(filePath);
    if (file.createNewFile()) {
      return file;
    } else {
      I_FileMessages messages = constants_.getFileMessages();
      String message = messages.getThereWasAProblemCreatingTheFollowingFile();
      throw new IOException(message + sys_.lineSeperator() +
          filePath);
    }
  }
  
  @Override
  public void deleteOnExit(String path) {
    new File(path).deleteOnExit();
  }

  @Override
  public void downloadFile(String url, String file) throws IOException {
    HttpEntity entity = null;
    try {
      CloseableHttpResponse resp = httpClient_.execute(new HttpGet(url));
      entity = resp.getEntity();
      StatusLine status = resp.getStatusLine();
      int statusCode = status.getStatusCode();
      if (statusCode >= 300) {
        I_FileMessages messages = constants_.getFileMessages();
        String message = messages.getSubmittingAHttpGetToTheFollowingUrlReturnedAnInvalidStatusCodeX();
        message = message.replace("<X/>", "" + statusCode);
        throw new IOException(message + sys_.lineSeperator() +
            url);
      }
    } catch (ClientProtocolException x) {
      throw new IOException(x);
    }
    InputStream in = entity.getContent();
    
    try {
      writeFile(in, newFileOutputStream(file), 16 * 1024);
    } catch (FileNotFoundException x) {
      throw new IOException(x);
    }
  }

  public FileOutputStream newFileOutputStream(String file) throws IOException, FileNotFoundException {
    return new FileOutputStream(file);
  }
  
  /**
   * https://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/
   * @param in
   * @param fos
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos, int bufferSize) throws IOException {
    try {
      ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
      ReadableByteChannel inputChannel = Channels.newChannel(in);
      WritableByteChannel outputChannel = fos.getChannel();
      
      writeFile(buffer, inputChannel, outputChannel);
    } catch (IOException x) {
      throw x;
    } finally {
      close(fos);
      close(in);
    }
  }

  public void writeFile(ByteBuffer buffer, ReadableByteChannel inputChannel,
      WritableByteChannel outputChannel) throws IOException {
    try {
      while (inputChannel.read(buffer) != -1) {
        // prepare the buffer to be drained
        buffer.flip();
        // write to the channel, may block
        outputChannel.write(buffer);
        // If partial transfer, shift remainder down
        // If buffer is empty, same as doing clear()
        buffer.compact();
      }
      // EOF will leave buffer in fill state
      buffer.flip();
      // make sure the buffer is fully drained.
      while (buffer.hasRemaining()) {
        outputChannel.write(buffer);
      }
    } catch (IOException x) {
      throw x;
    } finally {
      close(inputChannel);
      close(outputChannel);
    }
  }

  public void close(Closeable inputChannel) {
    if (inputChannel != null) {
      try {
        inputChannel.close();
      } catch (IOException x) {
        //do nothing
      }
    }
  }
  
  @Override
  public boolean exists(String filePath) {
    return new File(filePath).exists();
  }

  @Override
  public boolean mkdirs(String dirsPath) {
    return new File(dirsPath).mkdirs();
  }

  @Override
  public String getAbsolutePath(String filePath) {
    return new File(filePath).getAbsolutePath();
  }

  public void removeRecursive(String path) throws IOException
  {
      File dir = new File(path);
      Path dirPath = dir.toPath();
      log("Deleting recursive " + dir.getAbsolutePath());
      Files.walkFileTree(dirPath, new SimpleFileVisitor<Path>()
      {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                  throws IOException
          {
              Files.delete(file);
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
          {
              // try to delete the file anyway, even if its attributes
              // could not be read, since delete-only access is
              // theoretically possible
              Files.delete(file);
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
          {
              if (exc == null)
              {
                  Files.delete(dir);
                  return FileVisitResult.CONTINUE;
              }
              else
              {
                  // directory iteration failed; propagate exception
                  throw exc;
              }
          }
      });
  }
  public String readFile(String path) throws IOException {
    return readFile(newBufferedReader(path));
  }
  public String readFile(BufferedReader reader) throws IOException {
    StringBuilder sb = new StringBuilder();
    try {
      boolean lastLine = false;
      for (String line; (line = reader.readLine()) != null;) {
        if (lastLine) {
          sb.append(sys_.lineSeperator());
        } else {
          lastLine = true;
        }
        sb.append(line);
      }
      sb.append(sys_.lineSeperator());
    } catch (IOException x) {
      throw x;
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (IOException x) {
          //do nothing
        }
      }
    }
    return sb.toString();
  }

  public BufferedReader newBufferedReader(String path) throws IOException {
    BufferedReader rdr = Files.newBufferedReader(Paths.get(path));
    return rdr;
  }
  /**
   * 
   * @param path
   * @param matcher
   * @param ctx
   * @return a list of relative absolute paths to the path parameter
   * which match in the matcher parameter.
   * 
   * @throws IOException
   */
  public List<String> list(final String path, final I_FileMatcher matcher) throws IOException
  {
      Path dir = new File(path).toPath();
      final List<String> list = new ArrayList<String>();
      File pathFile = dir.toFile();
      final String absPath = pathFile.getAbsolutePath();
     
      final int start = absPath.indexOf(path) + 1;
      final int length = path.length();
      
      Files.walkFileTree(dir, new SimpleFileVisitor<Path>()
      {
          @Override
          public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                  throws IOException
          {
              File f = file.toFile();
              String fpath = f.getAbsolutePath();
              String relPath = fpath.substring(start + length, fpath.length());
              if (matcher.isMatch(relPath)) {
                list.add(fpath);
              }
              return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException
          {
            // this should never occur, unless some permission issue causes it
            if (exc != null) {
              throw exc;
            }
            return FileVisitResult.CONTINUE;
          }

          @Override
          public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException
          {
            // this should never occur, unless some permission issue causes it
            if (exc != null) {
              throw exc;
            }
            return FileVisitResult.CONTINUE;
          }
      });
      return list;
  }
  
  private void log(String message) {
    if (log_.isLogEnabled(FabFileIO.class)) {
      log_.println(message);
    }
  }


  @Override
  public String getNameSeparator() {
    return File.separator;
  }

  @Override
  public String getSlashPath(String absolutePath) {
    char [] chars = absolutePath.toCharArray();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < chars.length; i++) {
      char c = chars[i];
      if (c == '\\') {
          sb.append("/");
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  @Override
  public File instance(String filePath) {
    return new File(filePath);
  }

  
}
