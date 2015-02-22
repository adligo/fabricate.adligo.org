package org.adligo.fabricate.common.log;



/**
 * Yet another logging Api for fabricate,
 * so that it doesn't have a logging dependency.
 * @author scott
 *
 */
public interface I_FabLog extends I_Print {
  public boolean hasAllLogsEnabled();
  public boolean isLogEnabled(Class<?> clazz);
}
