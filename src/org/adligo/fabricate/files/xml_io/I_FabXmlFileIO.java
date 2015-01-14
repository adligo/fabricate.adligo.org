package org.adligo.fabricate.files.xml_io;

import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;

import java.io.IOException;

public interface I_FabXmlFileIO {
  public DepotType parseDepot_v1_0(String xmlFilePath) throws IOException;
  public FabricateDevType parseDev_v1_0(String xmlFilePath) throws IOException;
  public FabricateType parseFabricate_v1_0(String xmlFilePath) throws IOException;
  public FabricateProjectType parseProject_v1_0(String xmlFilePath) throws IOException;
  public LibraryType parseLibrary_v1_0(String xmlFilePath) throws IOException;
  
  public void writeDepot_v1_0(String filePath, DepotType depot) throws IOException;
  public void writeDev_v1_0(String filePath, FabricateDevType dev) throws IOException;
}
