package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.FabFileIO;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.DeferredLog;
import org.adligo.fabricate.common.log.FabLog;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.log.I_Print;
import org.adligo.fabricate.common.log.ThreadLocalPrintStream;
import org.adligo.fabricate.common.util.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.Console;
import java.io.File;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

public class FabSystem implements I_FabSystem {
  
  private final DeferredLog log_ = new DeferredLog();
  private final I_FabFileIO fileIO_;
  private final I_FabXmlFileIO xmlFileIO_ = new FabXmlFileIO();
  private final ConcurrentHashMap<String, List<String>> argListVals_ = new ConcurrentHashMap<String, List<String>>();
  private final Map<String,String> argMap = new TreeMap<String,String>();
  private I_FabricateConstants constants_ = FabricateEnConstants.INSTANCE;
  private Executor executor_;
  private I_Print fileLog_;
  
  public FabSystem() {
    fileIO_ = new FabFileIO(this);
    Map<String, Boolean> em = Collections.emptyMap();
    FabLog log = new FabLog( em, true);
    log_.setDelegate(log);
  }
  
  @Override
  public Thread currentThread() {
    return Thread.currentThread();
  }
  
  @Override
  public String doDialog(String question, boolean readPassword) {
    Console console = System.console();
    
    question = question + lineSeperator();
    if (fileLog_ != null) {
      fileLog_.println(question);
    }
    if (readPassword) {
      return new String(console.readPassword(question, new Object[]{}));
    } else {
      return console.readLine(question, new Object[]{});
    }
  }
  
  @Override
  public String getArgValue(String key) {
    return argMap.get(key);
  }
  
  @Override
  public List<String> getArgValues(String key) {
    List<String> toRet = argListVals_.get(key);
    if (toRet == null) {
      String valsDelimited  = argMap.get(key);
      if ( !StringUtils.isEmpty(valsDelimited)) {
        List<String> newVals = new ArrayList<String>();
        StringTokenizer st = new StringTokenizer(valsDelimited,",");
        boolean hadVals = false;
        while (st.hasMoreTokens()) {
          String token = st.nextToken();
          newVals.add(token);
          hadVals = true;
        }
        if (!hadVals) {
          if (valsDelimited.length() >= 1) {
            newVals.add(valsDelimited);
            hadVals = true;
          }
        }
        if (hadVals ) {
          toRet = Collections.unmodifiableList(newVals);
          argListVals_.putIfAbsent(key, toRet);
        }
      }
    }
    return toRet;
  }
  
  @Override
  public int getAvailableProcessors() {
    return Runtime.getRuntime().availableProcessors();
  }
  
  public I_FabricateConstants getConstants() {
    return constants_;
  }

  @Override
  public String[] getCpuInfo(String os) {
    return ComputerInfoDiscovery.getCpuInfo(this, os);
  }
  
  @Override
  public long getCurrentTime() {
    return System.currentTimeMillis();
  }
  
  @Override
  public I_FabLog getLog() {
    return log_;
  }

  public String getOperatingSystem() {
    return ComputerInfoDiscovery.getOperatingSystem(this);
  }
  
  @Override
  public String getOperatingSystemVersion(String os) {
    return ComputerInfoDiscovery.getOperatingSystemVersion(this, os);
  }
  
  @Override
  public String getProperty(String key, String defaultValue) {
    return System.getProperty(key, defaultValue);
  }  
  
  @Override
  public String getPathSeparator() {
    return File.pathSeparator;
  }



  @Override
  public String getDefaultLanguage() {
    return Locale.getDefault().getLanguage();
  }

  @Override
  public String getDefaultCountry() {
    return Locale.getDefault().getCountry();
  }


  public void setConstants(I_FabricateConstants constants) {
    this.constants_ = constants;
  }

  @Override
  public I_FabFileIO getFileIO() {
    return fileIO_;
  }

  @Override
  public I_FabXmlFileIO getXmlFileIO() {
    return xmlFileIO_;
  }


  @Override
  public String getenv(String key) {
    return System.getenv(key);
  }

  @Override
  public String lineSeperator() {
    return System.lineSeparator();
  }

  @Override
  public boolean hasArg(String arg) {
    return argMap.containsKey(arg);
  }

   public void join() {
     try {
       Thread.currentThread().join();
     } catch (InterruptedException x) {
       Thread.currentThread().interrupt();
     }
   }
  
  public void setArgs(Map<String,String> argsIn) {
    argMap.clear();
    if (argsIn != null) {
      argMap.putAll(argsIn);
    }
  }

  @Override
  public synchronized I_Executor getExecutor() {
    if (executor_ == null) {
      executor_ = new Executor(this);
    }
    return executor_;
  }

  @Override
  public ByteArrayOutputStream newByteArrayOutputStream() {
    return new ByteArrayOutputStream();
  }

  @Override
  public BufferedInputStream newBufferedInputStream(InputStream in) {
    return new BufferedInputStream(in);
  }

  @Override
  public CloseableHttpClient newHttpClient() {
    return HttpClients.createDefault();
  }

  @Override
  public ProcessBuilderWrapper newProcessBuilder(String[] args) {
    return new ProcessBuilderWrapper(new ProcessBuilder(args));
  }
  
  @Override
  public I_RunMonitor newRunMonitor(Runnable delegate, int counter) {
    return new RunMonitor(this, delegate, counter);
  }

  public void setLog(I_FabLog log) {
    log_.setDelegate(log);
  }
  
  @Override
  public String toScriptArgs() {
    StringBuilder sb = new StringBuilder();
    CommandLineArgs.appendArgs(sb, argMap);
    return sb.toString();
  }

  @Override
  public <E> ArrayBlockingQueue<E> newArrayBlockingQueue(Class<E> type, int size) {
    return new ArrayBlockingQueue<E>(size);
  }

  @Override
  public void setLogFileOutputStream(I_Print ps) {
    fileLog_ = ps;
    ThreadLocalPrintStream.setLOG_FILE_OUTPUT(ps);
  }

  @Override
  public String getHostname() {
    return ComputerInfoDiscovery.getHostname(this);
  }

  @Override
  public String getInetAddressHostname() throws UnknownHostException {
    return InetAddress.getLocalHost().getHostName();
  }

  @Override
  public String getJavaVersion() {
    return ComputerInfoDiscovery.getJavaVersion(this);
  }



}
