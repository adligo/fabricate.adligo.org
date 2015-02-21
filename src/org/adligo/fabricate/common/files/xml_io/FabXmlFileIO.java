package org.adligo.fabricate.common.files.xml_io;

import org.adligo.fabricate.xml.io_v1.depot_v1_0.DepotType;
import org.adligo.fabricate.xml.io_v1.dev_v1_0.FabricateDevType;
import org.adligo.fabricate.xml.io_v1.fabricate_v1_0.FabricateType;
import org.adligo.fabricate.xml.io_v1.library_v1_0.LibraryType;
import org.adligo.fabricate.xml.io_v1.project_v1_0.FabricateProjectType;
import org.adligo.fabricate.xml.io_v1.result_v1_0.ResultType;

import java.io.File;
import java.io.IOException;

import javax.xml.validation.Schema;

public class FabXmlFileIO implements I_FabXmlFileIO {
  /**
   * Please don't use directly use I_FabFiles(FabFiles) for stubbing
   * (and by stubbing I mean putting something in the program
   * to get called in a way that a test can mock out the stub later
   * using something like mockito).
   */
  public static Schema SCHEMA = SchemaLoader.INSTANCE.get();
  
  public FabXmlFileIO() {}

  @Override
  public DepotType parseDepot_v1_0(String xmlFilePath) throws IOException {
    return DepotIO.parse_v1_0(SCHEMA, new File(xmlFilePath));
  }
  
  @Override
  public FabricateDevType parseDev_v1_0(String xmlFilePath) throws IOException {
    return DevIO.parse_v1_0(SCHEMA, new File(xmlFilePath));
  }


  @Override
  public FabricateType parseFabricate_v1_0(String xmlFilePath) throws IOException {
    return FabricateIO.parse_v1_0(SCHEMA, new File(xmlFilePath));
  }


  @Override
  public FabricateProjectType parseProject_v1_0(String xmlFilePath) throws IOException {
    return ProjectIO.parse_v1_0(SCHEMA, new File(xmlFilePath));
  }

  @Override
  public ResultType parseResult_v1_0(String xmlFilePath) throws IOException {
    return ResultIO.parse_v1_0(SCHEMA, new File(xmlFilePath));
  }

  @Override
  public LibraryType parseLibrary_v1_0(String xmlFilePath) throws IOException {
    return LibraryIO.parse_v1_0(SCHEMA, new File(xmlFilePath));
  }

  @Override
  public void writeDepot_v1_0(String filePath, DepotType depot) throws IOException {
    DepotIO.write_v1_0(new File(filePath), depot);
  }

  @Override
  public void writeDev_v1_0(String filePath, FabricateDevType dev) throws IOException {
    DevIO.write_v1_0(filePath, dev);
  }

  @Override
  public void writeResult_v1_0(String filePath, ResultType dev) throws IOException {
    ResultIO.write_v1_0(filePath, dev);
  }
  
}
