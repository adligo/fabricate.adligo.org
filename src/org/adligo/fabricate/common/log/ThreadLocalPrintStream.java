package org.adligo.fabricate.common.log;

import org.adligo.fabricate.common.util.MethodBlocker;

import java.io.PrintStream;
import java.nio.charset.Charset;
import java.util.Collections;

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
  private static final MethodBlocker SET_PROTECTED = new MethodBlocker(
      ThreadLocalPrintStream.class, "setProtected", Collections.singletonList(
          "org.adligo.fabricate_tests.common.log.ThreadLocalPrintStreamMock"));
  private static final MethodBlocker REVERT_PROTECTED = new MethodBlocker(
      ThreadLocalPrintStream.class, "revertProtected", Collections.singletonList(
          "org.adligo.fabricate_tests.common.log.ThreadLocalPrintStreamMock"));
  private static final MethodBlocker SET_LOG_FILE_OUTPUT_BLOCKER = new MethodBlocker(
      ThreadLocalPrintStream.class, "setLOG_FILE_OUTPUT", Collections.singletonList(
          "org.adligo.fabricate.common.system.FabSystem"));
  private static volatile I_Print LOG_FILE_OUTPUT;
  /**
   * This method should only be used if there 
   * is no I_FabLog available.
   * @param p
   */
  protected static final void println(String p) {
    PrintStream out = getProtected();
    String line = new String(p.getBytes(), Charset.forName("UTF-8"));
    out.println(line);
    if (LOG_FILE_OUTPUT != null) {
      LOG_FILE_OUTPUT.println(line);
    }
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
    if (LOG_FILE_OUTPUT != null) {
      LOG_FILE_OUTPUT.printTrace(t);
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
    SET_PROTECTED.checkAllowed();
    OUT.set(threadPrintStream);
  }
  
  /**
   * for tests only
   */
  protected static void revertProtected() {
    REVERT_PROTECTED.checkAllowed();
    OUT.set(ORIGINAL);
  }
  public static I_Print getLOG_FILE_OUTPUT() {
    return LOG_FILE_OUTPUT;
  }
  /**
   * This method should be called through the I_FabSystem 
   * method so it can be checked for getting called.
   * @param lOG_FILE_OUTPUT
   */
  public static void setLOG_FILE_OUTPUT(I_Print lOG_FILE_OUTPUT) {
    SET_LOG_FILE_OUTPUT_BLOCKER.checkAllowed();
    LOG_FILE_OUTPUT = lOG_FILE_OUTPUT;
  }
}
