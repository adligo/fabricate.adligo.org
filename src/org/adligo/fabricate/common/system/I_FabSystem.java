package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLogSystem;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;


public interface I_FabSystem extends I_FabLogSystem {
  public I_FabFileIO getFileIO();
  public I_FabXmlFileIO getXmlFileIO();
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
  public boolean hasArg(String arg);
  
  /**
   * backed by the CommandLineArgs map.
   * @param arg
   * @return
   */
  public String getArgValue(String key);
  
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
