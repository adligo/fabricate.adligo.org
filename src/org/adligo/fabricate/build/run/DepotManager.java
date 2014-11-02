package org.adligo.fabricate.build.run;

import org.adligo.fabricate.common.Depot;
import org.adligo.fabricate.common.I_Depot;
import org.adligo.fabricate.common.I_DepotEntry;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.ThreadLocalPrintStream;
import org.adligo.fabricate.external.files.FileUtils;
import org.adligo.fabricate.xml.io.depot.DepotType;
import org.adligo.fabricate.xml_io.DepotIO;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DepotManager {
  private I_FabContext ctx_;
  private DepotType depotType_;
  private String dir_;
  private String depotXml_;
  private String depotRunningXml_;
  private I_Depot depot_;
  private boolean cleaning_;
  
  public DepotManager(I_FabContext ctx, String dir) {
    ctx_ = ctx;
    dir_ = dir;
    depotXml_ = dir_ + File.separator + "depot.xml";
    depotRunningXml_ = dir_ + File.separator + ".running";
    if (!exists()) {
      create();
    }
    if (ctx_.isLogEnabled(Depot.class)) {
      ThreadLocalPrintStream.println(depotRunningXml_);
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
    
    File depot = new File(depotXml_);
    try {
      depotType_ = DepotIO.parse(depot);
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
      if (ctx_.isLogEnabled(DepotManager.class)) {
        ThreadLocalPrintStream.println("DepotManager creating depot " + dir_);
      }
    }
    File dir = new File(dir_);
    
    if (!dir.exists()) { 
      if (!dir.mkdirs()) {
        throw new IllegalStateException("Problem creating " + dir_);
      }
    }
    File depot = new File(depotXml_);
    if (!depot.exists())
    try {
      depotType_ = new DepotType();
      DepotIO.write(depot, depotType_);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
  public void clean()  {
    cleaning_ = true;
    if (ctx_.isLogEnabled(DepotManager.class)) {
      ThreadLocalPrintStream.println("DepotManager cleaning depot " + dir_);
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
            if (ctx_.isLogEnabled(DepotManager.class)) {
              ThreadLocalPrintStream.println("DepotManager cleaning depot deleting " + f.getAbsolutePath());
            }
            try {
              Files.delete(f.toPath());
            } catch (IOException e) {
              throw new RuntimeException(e);
            }
          } else {
            try {
              FileUtils fus = new FileUtils(ctx_);
              fus.removeRecursive(Paths.get(f.toURI()));
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
      depot_ = new Depot(dir_, ctx_, depotType_);
    }
    return depot_;
  }
  
  public boolean remove(I_DepotEntry entry) {
    // TODO Auto-generated method stub
    return false;
  }
}
