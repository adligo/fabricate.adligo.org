package org.adligo.fabricate.common.log;

import org.adligo.fabricate.common.util.MethodBlocker;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class FabLog implements I_FabLog {
  /**
   * This method orders lines for left to right (English) <br/>
   * and right to left (Arabic) languages, when a <br/>
   * line header or tab is required (i.e); <br/>
   *  <br/>
   * Left to right (spaces simulate tabs) <br/>
   * Tests4j: setup <br/>
   *    org.adligo.tests4j.Foo 12.0% <br/>
   *    org.adligo.tests4j.Boo 13.0% <br/>
   *     <br/>
   * Right to left (spaces simulate tabs at right only)<br/>
   *                   setup :Tests4j<br/>
   *  12.0% org.adligo.tests4j.Foo   <br/>
   *  13.0% org.adligo.tests4j.Boo   <br/>
   * Also note I don't speak any or know much about any
   * right to left languages so this will take some time to get right.
   *  I assumed that java class names would stay left to right,
   *  and that the percent sign would still be to the right of the 
   *  number (after all it is the Arabic numeral system).
   *  The name of the Tests4J product would not change, as 
   *    translating it into other languages would be confusing.
   *    
   * @param p
   * @return
   */
  public static String orderLine(boolean leftToRight, String ... p) {
    if (p.length == 1) {
      return p[0];
    }
    StringBuilder sb = new StringBuilder();
    if (leftToRight) {
      for (int i = 0; i < p.length; i++) {
        sb.append(p[i]);
      }
    } else {
      for (int i = p.length - 1; i >= 0; i--) {
        sb.append(p[i]);
      }
    }
    return sb.toString();
  }

  private Map<String,Boolean> logSettings_ = new HashMap<String,Boolean>();
  private boolean allOn_ = false;
  private AtomicBoolean derailed_ = new AtomicBoolean(false);
  
  public FabLog(Map<String,Boolean> logSettings, boolean allOn) {
    if (allOn) {
      allOn_ = allOn;
    } else {
      if (logSettings != null) {
        logSettings_.putAll(logSettings);
      }
    }
  }
  @SuppressWarnings("boxing")
  @Override
  public boolean isLogEnabled(Class<?> clazz) {
    if (allOn_) {
      return true;
    }
    String className = clazz.getName();
    Boolean toRet = logSettings_.get(className);
    if (toRet != null) {
      return toRet;
    }
    return false;
  }
  
  @SuppressWarnings("deprecation")
  @Override
  public void println(String p) {
    if (!derailed_.get()) {
      ThreadLocalPrintStream.println(p);
    }
  }

  @Override
  public void printTrace(Throwable t) {
    if (!derailed_.get()) {
      if (isLogEnabled(FabLog.class)) {
        ThreadLocalPrintStream.printTrace(new RuntimeException("logging trace from;"));
      }
      ThreadLocalPrintStream.printTrace(t);
    }
  }
  @Override
  public boolean hasAllLogsEnabled() {
    return allOn_;
  }
  @Override
  public void derail() {
    MethodBlocker mb = new MethodBlocker(FabLog.class, "derail", Collections.singleton("org.adligo.fabricate.FabricateController"));
    mb.checkAllowed();
    derailed_.set(true);
  }
}
