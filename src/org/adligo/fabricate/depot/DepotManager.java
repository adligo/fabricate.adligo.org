package org.adligo.fabricate.depot;

import org.adligo.fabricate.common.files.FabFileIO;
import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.files.xml_io.FabXmlFileIO;
import org.adligo.fabricate.common.files.xml_io.I_FabXmlFileIO;
import org.adligo.fabricate.common.log.I_FabLog;
import org.adligo.fabricate.common.system.I_FabSystem;
import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class DepotManager {
  private final I_FabLog log_;
  private final DepotContext ctx_;
  private final I_FabFileIO files_;
  private final I_FabXmlFileIO xmlFiles_;
  
  private DepotType depotType_;
  private String dir_;
  private String depotXml_;
  private String depotRunningXml_;
  private I_Depot depot_;
  private boolean cleaning_;
  
  public DepotManager(I_FabSystem sys) {
    log_ = sys.getLog();
    ctx_ = new DepotContext(sys);
    files_ = sys.getFileIO();
    xmlFiles_ = sys.getXmlFileIO();
  }
  
  public DepotManager(DepotContext ctx) {
    ctx_ = ctx;
    log_ = ctx.getLog();
    files_ = ctx.getFiles();
    xmlFiles_ = ctx.getXmlFiles();
  }

  public void setup( String dir) {
 
    
    dir_ = dir;
    depotXml_ = dir_ + File.separator + "depot.xml";
    depotRunningXml_ = dir_ + File.separator + ".running";
    if (!exists()) {
      create();
    }
    if (log_.isLogEnabled(Depot.class)) {
      log_.println(depotRunningXml_);
    }
    File fabricate = new File(dir_ + File.separator + ".running");
    if (fabricate.exists()) {
      throw new IllegalStateException("Another fabricate process is using the depot " + 
            System.lineSeparator() + "\t" + dir);
    }
    try {
      if (!fabricate.createNewFile()) {
        throw new IllegalStateException("There was a problem creating the file " + 
            System.lineSeparator() + "\t" + dir + File.separator + ".running");
      }
      fabricate.deleteOnExit();
    } catch (IOException x) {
      throw new IllegalStateException(x);
    }
    
    try {
      depotType_ = xmlFiles_.parseDepot_v1_0(depotXml_);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  
  private boolean exists() {
    if  (!new File(dir_).exists()) {
      return false;
    }
    return new File(depotXml_).exists();
  }
  
  private void create() {
    if (!cleaning_) {
      if (log_.isLogEnabled(DepotManager.class)) {
        log_.println("DepotManager creating depot " + dir_);
      }
    }
    if (!files_.exists(dir_)) { 
      if (!files_.mkdirs(dir_)) {
        throw new IllegalStateException("Problem creating " + dir_);
      }
    }
    if (!files_.exists(depotXml_)) {
      try {
        depotType_ = new DepotType();
        xmlFiles_.writeDepot_v1_0(depotXml_, depotType_);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  public void clean()  {
    cleaning_ = true;
    if (log_.isLogEnabled(DepotManager.class)) {
      log_.println("DepotManager cleaning depot " + dir_);
    }
    File dir = new File(dir_);
    
    File [] files = dir.listFiles();
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        File f = files[i];
        if ( !f.isDirectory()) {
          if ( !"depot.xml".equals(f.getName()) && !".running".equals(f.getName())) {
            throw new RuntimeException("The following depot appears to have a "
                + "non depot file structure, aborting." + System.lineSeparator() +
                dir_);
          }
        }
      }
    }
    if (files != null) {
      for (int i = 0; i < files.length; i++) {
        File f = files[i];
        if (!".running".equals(f.getName())) {
          if (f.isFile()) {
            if (log_.isLogEnabled(DepotManager.class)) {
              log_.println("DepotManager cleaning depot deleting " + f.getAbsolutePath());
            }
            try {
              Files.delete(f.toPath());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          } else {
            try {
              files_.deleteRecursive(f.getAbsolutePath());
            } catch (IOException x) {
              throw new RuntimeException(x);
            }
          }
        }
      }
    }
    create();
    cleaning_ = false;
  }

  public I_Depot getDepot() {
    if (depot_ == null) {
      Depot depot = new Depot(dir_, ctx_, depotType_);
      depot_ = depot;
    }
    return depot_;
  }
  
  public boolean remove(I_DepotEntry entry) {
    // TODO Auto-generated method stub
    return false;
  }
}
