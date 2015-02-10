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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class FabSystem implements I_FabSystem {
  
  private final DeferredLog log_ = new DeferredLog();
  private final I_FabFileIO fileIO_;
  private final I_FabXmlFileIO xmlFileIO_ = new FabXmlFileIO();
  private final Map<String,String> argMap = new TreeMap<String,String>();
  private I_FabricateConstants constants_ = FabricateEnConstants.INSTANCE;
  private Executor executor_;
  
  public FabSystem() {
    fileIO_ = new FabFileIO(this);
    FabLog log = new FabLog(Collections.emptyMap(), true);
    log_.setDelegate(log);
  }
  
  @Override
  public I_FabLog getLog() {
    return log_;
  }

  @Override
  public String getPathSeparator() {
    return File.pathSeparator;
  }
  
  public void setLog(I_FabLog log) {
    log_.setDelegate(log);;
  }

  @Override
  public long getCurrentTime() {
    return System.currentTimeMillis();
  }

  @Override
  public String getDefaultLanguage() {
    return Locale.getDefault().getLanguage();
  }

  @Override
  public String getDefaultCountry() {
    return Locale.getDefault().getCountry();
  }

  public I_FabricateConstants getConstants() {
    return constants_;
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
    
  @Override
  public String getArgValue(String key) {
    return argMap.get(key);
  }

  @Override
  public String toScriptArgs() {
    StringBuilder sb = new StringBuilder();
    CommandLineArgs.appendArgs(sb, argMap);
    return sb.toString();
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
  public ProcessBuilderWrapper newProcessBuilder(String[] args) {
    return new ProcessBuilderWrapper(new ProcessBuilder(args));
  }

  @Override
  public Thread currentThread() {
    return Thread.currentThread();
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
}
