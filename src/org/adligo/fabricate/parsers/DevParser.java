package org.adligo.fabricate.parsers;

import org.adligo.fabricate.common.I_FabLog;

import java.io.IOException;

import javax.xml.validation.Schema;

public class DevParser {
  private I_FabLog log_;
  private Schema schema;
  
  public DevParser(I_FabLog log) throws IOException {
    log_ = log;
    schema = SchemaLoader.INSTANCE.get();
  }
  
}
