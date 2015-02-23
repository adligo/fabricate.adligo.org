package org.adligo.fabricate.common.files;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.log.I_FabLog;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.DatatypeConverter;

public class FabFileIO implements I_FabFileIO {
  public static final String MD5 = "MD5";
  private final I_FabFilesSystem sys_;
  private final I_FabLog log_;
  private final I_FabricateConstants constants_;
  
  public FabFileIO(I_FabFilesSystem sys) {
    sys_ = sys;
    constants_ = sys.getConstants();
    log_ = sys.getLog();
  }

  public String calculateMd5(String file) throws IOException {
    return decode(file, MD5);
  }
  
  @Override
  public int check(String url) throws IOException {
    CloseableHttpClient httpClient = sys_.newHttpClient();
    CloseableHttpResponse resp = null;
    try {
      resp = httpClient.execute(new HttpGet(url));
      StatusLine status = resp.getStatusLine();
      return status.getStatusCode();
    } catch (ClientProtocolException x) {
      throw new IOException(x);
    } finally {
       close(resp);
       close(httpClient);
    }
  }
  /**
   * This method closes the Closeable
   * if it is NOT null, and then 
   * returns the IOException if it was caught.
   * @param inputChannel
   * @return a null IOException when everything 
   * runs smoothly, or the IOException that was caught.
   * NOTE, this IOExcepion is almost always ignored by the caller,
   * since there isn't a good way to recover from a IOException
   * thrown from a close method.  The main reason for 
   * returning it is testing of this method.
   */
  public IOException close(Closeable closeable) {
    if (closeable != null) {
      try {
        closeable.close();
      } catch (IOException x) {
        return x;
      }
    }
    return null;
  }
  

  public void closeIOPair(Closeable in, Closeable out, I_IOCloseTracker tracker) {
    IOException inEx = close(in);
    if (inEx != null) {
      tracker.onCloseException(inEx);
    }
    IOException outEx = close(out);
    if (outEx != null) {
      tracker.onCloseException(outEx);
    }
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
  
  public String decode(String file, String algorithm) throws IOException {
    byte[] b = readAllBytes(file);
    try {
      MessageDigest md = MessageDigest.getInstance(algorithm);
      md.reset();
      md.update(b);
      byte[] hash = md.digest();
      String actual = DatatypeConverter.printHexBinary(hash);
      return actual.toLowerCase();
    } catch (NoSuchAlgorithmException x) {
      throw new IOException(x);
    }
  }
  
  @Override
  public void deleteOnExit(String path) {
    new File(path).deleteOnExit();
  }

  @Override
  public void delete(String path) throws IOException {
    if (!new File(path).delete()) {
      I_FileMessages messages = constants_.getFileMessages();
      throw new IOException(messages.getThereWasAProblemDeletingTheFollowingFile() + 
          sys_.lineSeperator() + path);
    }
  }
  
  @Override
  public void downloadFile(String url, String file) throws IOException {
    if (log_.isLogEnabled(FabFileIO.class)) {
      I_FileMessages messages = constants_.getFileMessages();
      log_.println(messages.getStartingDownloadFromX().replaceAll("<X/>", url) + sys_.lineSeperator());
    }
    
    CloseableHttpClient httpClient = sys_.newHttpClient();
    HttpEntity entity = null;
    CloseableHttpResponse resp = null;
    boolean exiting = true;
    long length = -1;
    try {
      resp = httpClient.execute(new HttpGet(url));
      entity = resp.getEntity();
      length = entity.getContentLength();
      StatusLine status = resp.getStatusLine();
      int statusCode = status.getStatusCode();
      if (statusCode >= 300) {
        I_FileMessages messages = constants_.getFileMessages();
        String message = messages.getSubmittingAHttpGetToTheFollowingUrlReturnedAnInvalidStatusCodeX();
        message = message.replace("<X/>", "" + statusCode);
        throw new IOException(message + sys_.lineSeperator() +
            url);
      }
      exiting = false;
    } catch (ClientProtocolException x) {
      throw new IOException(x);
    } finally {
      if (exiting) {
        close(resp);
        close(httpClient);
      }
    }
    InputStream in = entity.getContent();
    
    try {
      writeFile(in, newFileOutputStream(file), length, url);
    } catch (FileNotFoundException x) {
      throw new IOException(x);
    } finally {
      close(resp);
      close(httpClient);
    }
    if (log_.isLogEnabled(FabFileIO.class)) {
      I_FileMessages messages = constants_.getFileMessages();
      log_.println(messages.getFinisedDownloadFromX().replaceAll("<X/>", url) + sys_.lineSeperator());
    }
  }

  public FileOutputStream newFileOutputStream(String file) throws IOException, FileNotFoundException {
    return new FileOutputStream(file);
  }
  @Override
  public ZipFile newZipFile(String file) throws IOException {
    return new ZipFile(file);
  }
  
  @Override
  public void unzip(String file, String toDir) throws IOException {
    if (toDir.lastIndexOf(getNameSeparator()) != toDir.length() - 1) {
      toDir = toDir + getNameSeparator();
    }
    File dir = new File(toDir);
    Path dirPath = dir.toPath();
    if (Files.notExists(dirPath)) {
      mkdirs(toDir);
    }
    ZipFile zip = new ZipFile(file);
    String dirAbs = dir.getAbsolutePath();
    extractZipFile(dirAbs, zip, DefaultIOCloseTracker.INSTANCE);
  }

  /**
   * @param dir a absolute path
   * @param zip a file which is going to be extracted,
   * by inspecting the entries.
   * @throws FileNotFoundException
   * @throws IOException
   */
  public void extractZipFile(String dir, ZipFile zip, I_IOCloseTracker tracker) throws FileNotFoundException, IOException {
    Enumeration<? extends ZipEntry> zipEntries = zip.entries();
    
    try {
      while(zipEntries.hasMoreElements()){
        ZipEntry ze = zipEntries.nextElement();
        String name = ze.getName();
        
        if (name.indexOf("/") == 0) {
          name = name.substring(1, name.length());
        }
        name = dir + getNameSeparator() + name.replaceAll("/", getNameSeparator());
        if (ze.isDirectory()) {
          mkdirs(name);
        } else {
          InputStream is =  zip.getInputStream(ze);
          FileOutputStream fos = new FileOutputStream(name);
          writeFile(is, fos, 1024);
        }
      }
    } catch (IOException x) {
      throw x;
    } finally {
      IOException ce = close(zip);
      if (ce != null) {
        tracker.onCloseException(ce);
      }
    }
  }
  
  /**
   * @param dir
   * @param zip
   * @return false when one of the zip file entries
   * is not on the disk under the dir.
   */
  public boolean verifyZipFileExtract(String dir, ZipFile zip) {
    if (!exists(dir)) {
      return false;
    }
    Enumeration<? extends ZipEntry> zipEntries = zip.entries();
    
    while(zipEntries.hasMoreElements()){
      ZipEntry ze = zipEntries.nextElement();
      String name = ze.getName();
      
      if (name.indexOf("/") == 0) {
        name = name.substring(1, name.length());
      }
      name = dir + getNameSeparator() + name.replaceAll("/", getNameSeparator());
      if (!exists(name)) {
        return false;
      }
    }
    return true;
  }
  /**
   * This method writes a file out to disk from a input stream, using NIO 
   * (ByteBuffer, ReadableByteChannel, FileChannel).  It uses
   * a default buffer size of 16Kb.
   * @param in
   * @param fos
   * @param length the known length of the file;
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos) throws IOException {
    writeFile(in, fos, 16 * 1024, -1, "");
  }
  /**
   * This method writes a file out to disk from a input stream, using NIO 
   * (ByteBuffer, ReadableByteChannel, FileChannel).  It uses
   * a default buffer size of 16Kb.
   * @param in
   * @param fos
   * @param length the known length of the file;
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos, long length, String whichFile) throws IOException {
    writeFile(in, fos, 16 * 1024, length, whichFile);
  }
  /**
   * This method writes a file out to disk from a input stream, using NIO 
   * (ByteBuffer, ReadableByteChannel, FileChannel).
   * Thanks for this;
   * https://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/
   * @param in
   * @param fos
   * @param length the known length of the file
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos, int bufferSize) throws IOException {
    writeFileWithCloseTracker(in,fos, bufferSize, DefaultIOCloseTracker.INSTANCE, -1, "");
  }
  /**
   * This method writes a file out to disk from a input stream, using NIO 
   * (ByteBuffer, ReadableByteChannel, FileChannel).
   * Thanks for this;
   * https://thomaswabner.wordpress.com/2007/10/09/fast-stream-copy-using-javanio-channels/
   * @param in
   * @param fos
   * @param length the known length of the file
   * @throws IOException
   */
  public void writeFile(InputStream in, FileOutputStream fos, int bufferSize, long length, String whichFile) throws IOException {
    writeFileWithCloseTracker(in,fos, bufferSize, DefaultIOCloseTracker.INSTANCE, length, whichFile);
  }

  public void writeFileWithCloseTracker(InputStream in, FileOutputStream fos, int bufferSize,
      I_IOCloseTracker tracker, long length, String whichFile) throws IOException {
    try {
      ByteBuffer buffer = ByteBuffer.allocateDirect(bufferSize);
      ReadableByteChannel inputChannel = Channels.newChannel(in);
      WritableByteChannel outputChannel = fos.getChannel();
      
      writeFileWithBuffers(buffer, inputChannel, outputChannel, tracker, length, whichFile);
    } catch (IOException x) {
      throw x;
    } finally {
      closeIOPair(in, fos,tracker);
    }
  }

  public void writeFileWithBuffers(ByteBuffer buffer, 
      ReadableByteChannel inputChannel, WritableByteChannel outputChannel, 
      I_IOCloseTracker tracker) throws IOException {
    writeFileWithBuffers(buffer, inputChannel, outputChannel, tracker, -1, "");
  }
  @SuppressWarnings("boxing")
  public void writeFileWithBuffers(ByteBuffer buffer, 
      ReadableByteChannel inputChannel, WritableByteChannel outputChannel, 
      I_IOCloseTracker tracker, long length, String whichFile) throws IOException {
    
    
    LinkedList<Double> pctsToLog = null;
    if (length != -1) {
      pctsToLog = new LinkedList<Double>();
      if (length <= 100000000 && length >= 10000000) {
        //10-100mb
        pctsToLog.add(25.0);
        pctsToLog.add(50.0);
        pctsToLog.add(75.0);
      } else if (length <= Integer.MAX_VALUE && length > 1000000000) {
        for (int i = 1; i < 11; i++) {
          pctsToLog.add(10.0 * i);
        }
      } else if (length > Integer.MAX_VALUE) {
        for (int i = 1; i < 34; i++) {
          pctsToLog.add(3.0 * i);
        }
      }
    }
    
    BigDecimal lengthD = new BigDecimal(length);
    long written = 0;
    try {
      while (inputChannel.read(buffer) != -1) {
        // prepare the buffer to be drained
        buffer.flip();
        // write to the channel, may block
        outputChannel.write(buffer);
        // If partial transfer, shift remainder down
        // If buffer is empty, same as doing clear()
        buffer.compact();
        written = written + buffer.limit();
        
        if (length != -1) {
          BigDecimal writtenD = new BigDecimal(written);
          BigDecimal pct = writtenD.divide(lengthD, 2, RoundingMode.HALF_UP);
          if (pctsToLog != null) {
            Double d = pctsToLog.peek();
            if (d != null) {
              double pctP = pct.doubleValue() * 100.0;
              
              if (pctP >= d.doubleValue()) {
                I_FileMessages messages = constants_.getFileMessages();
                String message = messages.getTheFollowingDownloadIsXPercentComplete().replaceAll(
                    "<X/>", "" + new Double(pctP).intValue());
                log_.println(message + sys_.lineSeperator() + 
                    whichFile + sys_.lineSeperator());
                pctsToLog.pop();
              }
            }
          }
        }
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
      closeIOPair(inputChannel, outputChannel,tracker);
    }
  }


  public byte[] readAllBytes(String file) throws IOException {
    return Files.readAllBytes(Paths.get(new File(file).toURI()));
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

  public void deleteRecursive(String path) throws IOException
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
  @Override
  public String readFile(String path) throws IOException {
    return readFile(newBufferedReader(path, Charset.forName("UTF-8")));
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

  public BufferedReader newBufferedReader(String path, Charset charSet) throws IOException {
    BufferedReader rdr = Files.newBufferedReader(Paths.get(path), charSet);
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
