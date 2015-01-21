package org.adligo.fabricate.common.log;



/**
 * Yet another logging Api for fabricate,
 * so that it doesn't have a logging dependency.
 * @author scott
 *
 */
public interface I_FabLog {
  public boolean isLogEnabled(Class<?> clazz);
  public void println(String p);
  public void printTrace(Throwable t);
}
