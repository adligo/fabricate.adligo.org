package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.I_FabFilesSystem;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.I_FabFileLog;
import org.adligo.fabricate.common.log.I_Print;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;


public interface I_FabSystem extends I_FabFilesSystem {
  
  /**
   * This method provides the Thread.currentThread(),
   * and adds a way to inject a mock for testing.
   * @return
   */
  public Thread currentThread();
  public String currentThreadName();
  /**
   * This dialogs the user on the command line, 
   * it should only be used inside the method 'doDialogs'
   * as a implementation of I_PresentationAware.
   * 
   * @param question
   * @return
   */
  public String doDialog(String question, boolean readPassword);
  
  /**
   * does a System.exit
   * @param exitStatus
   */
  public void exit(int exitStatus);
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
   * Runtime.getRuntime().getAvailableProcessors();
   * @return
   */
  public int getAvailableProcessors();
  /**
   * @param os
   * @return  a array of two strings;
   * 1) the cpu name
   * 2) the cpu speed
   */
  public String[] getCpuInfo(String os);
  /**
   * @see I_Executor, this method also provides a
   * way to pass in a mock for testing.
   * @return
   */
  public I_Executor getExecutor();
  
  public String getOperatingSystem();
  public String getOperatingSystemVersion(String os);
  /**
   * This returns the system dependent separator for class path entries.
   * an alias to File.pathSeparator(), for stubbing.
   * @return
   */
  public String getPathSeparator();
  
  /**
   * An alias to System.getProperty for stubbing.
   * @param key
   * @param defaultValue
   * @return
   */
  public String getProperty(String key, String defaultValue);
  
  public I_FabFileIO getFileIO();
  public String getHostname();
  public String getInetAddressHostname() throws UnknownHostException;
  public String getJavaVersion();
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
   * the command line arguments that came
   * in as a normalized string
   * @return
   */
  public String toScriptArgs();

  /**
   * return a new ConcurrentLinkedQueue.
   * @param type
   * @return
   */
  public <T,V> ConcurrentHashMap<T,V> newConcurrentHashMap(Class<T> keyType, Class<V> valType);
  
  /**
   * return a new ConcurrentLinkedQueue.
   * @param type
   * @return
   */
  public <T> ConcurrentLinkedQueue<T> newConcurrentLinkedQueue(Class<T> type);
  
  /**
   * creates a new ArrayBlockingQueue for stubbing.
   * @param type
   * @param size
   * @return
   */
  public <E> ArrayBlockingQueue<E> newArrayBlockingQueue(Class<E> type, int size);
  
  /**
   * create a new DatatypeFactory
   * delegates to DatatypeFactory.newInstance().
   * added for testing mocks.
   * @return
   */
  public DatatypeFactory newDatatypeFactory() throws DatatypeConfigurationException;
  
  /**
   * returns a new ExecutingProcess
   * @param proc
   * @return
   */
  public I_ExecutingProcess newExecutingProcess(Process proc);
  
  /**
   * Create a logger that writes out to a file.
   * @param fileName
   * @return
   */
  public I_FabFileLog newFabFileLog(String fileName) throws IOException;
  
  /**
   * create the constants using languageCode and countryCode
   * @param languageCode
   * @param countryCode
   * @return
   */
  public I_FabricateConstants newFabConstantsDiscovery(String languageCode,String countryCode);
  
  /**
   * This method provides a
   * way to pass in a mock for testing.
   * @param args
   * @return
   */
  public I_ProcessBuilderWrapper newProcessBuilder(String []  args);
  
  /**
   * returns a new ProcessRunnable instance
   * @param proc
   * @return
   */
  public I_ProcessRunnable newProcessRunnable(Process proc);
  
  /**
   * delegates to Executors.newFixedThreadPool(int size)
   * in order to pass a mock for testing.
   * @param size
   * @return for size <= 1
   *  Executors.newSingleThreadExecutor()
   *  otherwise
   *  Executors.newFixedThreadPool()
   */
  public ExecutorService newFixedThreadPool(int size);
  
  /**
   * create a new git calls instance.
   * @return
   */
  public I_GitCalls newGitCalls();
  
  /**
   * Create a new run monitor to wrap a Runnable,
   * in a way that can be monitored.
   * 
   * @param delegate
   * @param counter
   * @return
   */
  public I_RunMonitor newRunMonitor(I_LocatableRunnable delegate, int counter);
  
  /**
   * This method provides a new ByteArrayOutputStream()
   * and adds a way to inject a mock for testing.
   * @return
   */
  public ByteArrayOutputStream newByteArrayOutputStream();
  
  public BufferedInputStream newBufferedInputStream(InputStream in);
  
  public void setLogFile(I_Print ps);
}
