package org.adligo.fabricate.files;

import org.adligo.fabricate.files.xml_io.FabXmlFiles;
import org.adligo.fabricate.files.xml_io.I_FabXmlFiles;
import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.File;
import java.io.IOException;

public class FabFiles implements I_FabFiles {
  public static final FabFiles INSTANCE = new FabFiles();
  private static I_FabXmlFiles XML_FILES = FabXmlFiles.INSTANCE;
  
  private FabFiles() {}
  
  @Override
  public boolean exists(String filePath) {
    return new File(filePath).exists();
  }

  @Override
  public DepotType parseDepot_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseDepot_v1_0(xmlFilePath);
  }

  
  @Override
  public FabricateDevType parseDev_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseDev_v1_0(xmlFilePath);
  }

  @Override
  public FabricateType parseFabricate_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseFabricate_v1_0(xmlFilePath);
  }

  @Override
  public FabricateProjectType parseProject_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseProject_v1_0(xmlFilePath);
  }

  @Override
  public LibraryType parseLibrary_v1_0(String xmlFilePath) throws IOException {
    return XML_FILES.parseLibrary_v1_0(xmlFilePath);
  }

  @Override
  public void writeDev_v1_0(String filePath, FabricateDevType dev) throws IOException {
    XML_FILES.writeDev_v1_0(filePath, dev);
  }

  @Override
  public void writeDepot_v1_0(String filePath, DepotType depot) throws IOException {
    XML_FILES.writeDepot_v1_0(filePath, depot);
  }

  @Override
  public boolean mkdirs(String dirsPath) {
    return new File(dirsPath).mkdirs();
  }

  @Override
  public String getAbsolutePath(String filePath) {
    return new File(filePath).getAbsolutePath();
  }



}
