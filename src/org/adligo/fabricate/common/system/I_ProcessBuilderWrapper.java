package org.adligo.fabricate.common.system;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface I_ProcessBuilderWrapper {

  public abstract I_ProcessBuilderWrapper directory(File directory);

  public abstract Map<String, String> environment();

  public abstract ProcessBuilder getDelegate();

  public abstract I_ProcessBuilderWrapper redirectErrorStream(boolean redirectErrorStream);

  public abstract Process start() throws IOException;

}