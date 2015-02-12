package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.I_FabFilesSystem;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;


public interface I_FabSystem extends I_FabFilesSystem {
  /**
   * Stub for System.getenv(String key);
   * @param key
   * @return
   */
  public String getenv(String key);

  /**
   * backed by the CommandLineArgs map.
   * @param arg
   * @return
   */
  public String getArgValue(String key);
  /**
   * parses and keeps a cache of comma delimited 
   * arguments from the command line.
   * @param key
   * @return
   */
  public List<String> getArgValues(String key);
  /**
   * the command line arguments that came
   * in as a normalized string
   * @return
   */
  public String toScriptArgs();
  
  /**
   * @see I_Executor, this method also provides a
   * way to pass in a mock for testing.
   * @return
   */
  public I_Executor getExecutor();
  
  /**
   * This returns the system dependent separator for class path entries.
   * an alias to File.pathSeparator(), for stubbing.
   * @return
   */
  public String getPathSeparator();
  
  public I_FabFileIO getFileIO();
  public I_FabXmlFileIO getXmlFileIO();
  /**
   * backed by the CommandLineArgs map.
   * @param arg
   * @return
   */
  public boolean hasArg(String arg);
  
  /**
   * does this;<br/>
   * <pre>
   * <code>
   *  try {
   *    Thread.currentThread().join();
   *  } catch (InterruptedException x) {
   *    Thread.currentThread().interrupt();
   *  }
   * </code>
   * </pre>
   * Provides a stub to make sure it was called.
   */
  public void join();
  
  /**
   * This method provides a
   * way to pass in a mock for testing.
   * @param args
   * @return
   */
  public ProcessBuilderWrapper newProcessBuilder(String []  args);
  
  /**
   * This method provides the Thread.currentThread(),
   * and adds a way to inject a mock for testing.
   * @return
   */
  public Thread currentThread();
  
  /**
   * This method provides a new ByteArrayOutputStream()
   * and adds a way to inject a mock for testing.
   * @return
   */
  public ByteArrayOutputStream newByteArrayOutputStream();
  
  public BufferedInputStream newBufferedInputStream(InputStream in);
}
