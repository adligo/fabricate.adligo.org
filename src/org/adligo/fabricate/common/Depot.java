package org.adligo.fabricate.common;

import org.adligo.fabricate.external.files.FileUtils;
import org.adligo.fabricate.xml.io.depot.DepotType;
import org.adligo.fabricate.xml_io.DepotIO;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Paths;

/**
 * This class manages the depot directory 
 * which consists of a depot.xml file
 * 
 * @author scott
 *
 */
public class Depot implements I_Depot {
  private static PrintStream OUT = System.out;
  private String dir_;
  private I_FabContext ctx_;
  private DepotType depot_;
  
  public Depot(String dir, I_FabContext ctx) {
    dir_ = dir;
    ctx_ = ctx;
    if (ctx_.isLogEnabled(Depot.class)) {
      OUT.println("New Depot for " + dir);
    }
    File depot = new File(dir_ + File.separator + "depot.xml");
    if (depot.exists()) {
      try {
        depot_ = DepotIO.parse(depot);
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
  
  public synchronized void create() {
    File file = new File(dir_);
    file.mkdirs();
    File depot = new File(dir_ + File.separator + "depot.xml");
    depot_ = new DepotType();
    try {
      DepotIO.write(depot, depot_);
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }
  
  @Override
  public synchronized void clean()  {
    File dir = new File(dir_);
    
    if (dir.exists()) { 
    
      File [] files = dir.listFiles();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          File f = files[i];
          if ( !f.isDirectory()) {
            if ( !"depot.xml".equals(f.getName())) {
              throw new RuntimeException("The depot appears to have a "
                  + "non depot file structure, aborting.");
            }
          }
        }
      }
      try {
        FileUtils fus = new FileUtils(ctx_);
        fus.removeRecursive(Paths.get(dir.toURI()));
      } catch (IOException x) {
        throw new RuntimeException(x);
      }
    }
    create();
  }

  @Override
  public synchronized void add(I_DepotInput input) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public synchronized void remove(I_DepotInput input) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean exists() {
    return new File(dir_).exists();
  }

}
