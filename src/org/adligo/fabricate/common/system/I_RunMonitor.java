package org.adligo.fabricate.common.system;

public interface I_RunMonitor extends Runnable {

  public abstract boolean isFinished();

  public abstract void waitUntilFinished(long millis) throws InterruptedException;

  public abstract Throwable getCaught();

  public abstract int getSequence();

}