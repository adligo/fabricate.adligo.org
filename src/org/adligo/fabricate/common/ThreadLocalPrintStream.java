package org.adligo.fabricate.common;

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
  
  public static final void println(String p) {
    PrintStream out = get();
    out.println(p);
  }
  
  public static final void printTrace(Throwable t) {
    PrintStream out = get();
    t.printStackTrace(out);
    Throwable cause = t.getCause();
    while (cause != null) {
      cause.printStackTrace(out);
      cause = cause.getCause();
    }
  }
  
  private static PrintStream get() {
    PrintStream toRet = OUT.get();
    if (toRet == null) {
      toRet = ORIGINAL;
      OUT.set(ORIGINAL);
    }
    return toRet;
  }
  
  protected static void set(PrintStream threadPrintStream) {
    OUT.set(threadPrintStream);
  }
  
  protected static void revert() {
    OUT.set(ORIGINAL);
  }
}
