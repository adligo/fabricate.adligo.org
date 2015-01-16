package org.adligo.fabricate.depot;

import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.files.FabFileIO;
import org.adligo.fabricate.files.I_FabFileIO;
import org.adligo.fabricate.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.files.xml_io.I_FabXmlFileIO;

public class DepotContext {
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  
  public DepotContext(I_FabLog log) {
    log_ = log;
    files_ = FabFileIO.INSTANCE;
    xmlFiles_ = FabXmlFileIO.INSTANCE;
  }
  
  public I_FabLog getLog() {
    return log_;
  }
  
  public I_FabFileIO getFiles() {
    return files_;
  }
  
  public I_FabXmlFileIO getXmlFiles() {
    return xmlFiles_;
  }
}
