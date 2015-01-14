package org.adligo.fabricate.common.log;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;


/**
 * Yet another logging Api for fabricate,
 * so that it doesn't have a logging dependency.
 * @author scott
 *
 */
public interface I_FabLog {
  public I_FabricateConstants getConstants();
  public boolean isLogEnabled(Class<?> clazz);
  public void println(String p);
  public void printTrace(Throwable t);
}
