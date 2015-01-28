package org.adligo.fabricate.common.log;

import java.io.PrintStream;

/**
 * This class should be used for all
 * console logging in this system
 * so it can be replaced as a thread local
 * for tests;
 * @author scott
 *
 */
public class ThreadLocalPrintStream {
  private static final PrintStream ORIGINAL = System.out;
  private static final ThreadLocal<PrintStream> OUT = new ThreadLocal<PrintStream>();
  
  /**
   * This method should only be used if there 
   * is no I_FabLog available.
   * @param p
   */
  protected static final void println(String p) {
    PrintStream out = getProtected();
    out.println(p);
  }
  /**
   * This method should only be used if there 
   * is no I_FabLog available.
   * @param p
   */
  protected static final void printTrace(Throwable t) {
    PrintStream out = getProtected();
    t.printStackTrace(out);
    Throwable cause = t.getCause();
    while (cause != null) {
      cause.printStackTrace(out);
      cause = cause.getCause();
    }
  }
  
  /**
   * protected for test code only
   * @return
   */
  protected static PrintStream getProtected() {
    PrintStream toRet = OUT.get();
    if (toRet == null) {
      toRet = ORIGINAL;
      OUT.set(ORIGINAL);
    }
    return toRet;
  }
  
  /**
   * for tests only
   * @param threadPrintStream
   */
  protected static void setProtected(PrintStream threadPrintStream) {
    OUT.set(threadPrintStream);
  }
  
  /**
   * for tests only
   */
  protected static void revertProtected() {
    OUT.set(ORIGINAL);
  }
}
