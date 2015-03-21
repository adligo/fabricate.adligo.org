package org.adligo.fabricate.depot;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;

public class DepotContext {
  private final I_FabSystem system_;
  private final I_FabLog log_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  
  public DepotContext(I_FabSystem sys) {
    system_ = sys;
    log_ = sys.getLog();
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
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

  public I_FabSystem getSystem() {
    return system_;
  }
}
