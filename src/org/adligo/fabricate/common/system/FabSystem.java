package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.en.FabricateEnConstants;
import org.adligo.fabricate.common.files.FabFileIO;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.log.DeferredLog;
import org.adligo.fabricate.common.log.I_FabLog;

import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

public class FabSystem implements I_FabSystem {
  private I_FabricateConstants constants_ = FabricateEnConstants.INSTANCE;
  private DeferredLog log_ = new DeferredLog();
  private I_FabFileIO fileIO_ = new FabFileIO(log_);
  private I_FabXmlFileIO xmlFileIO_ = new FabXmlFileIO();
  private Map<String,String> argMap = new TreeMap<String,String>();
  
  @Override
  public I_FabLog getLog() {
    return log_;
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
}
