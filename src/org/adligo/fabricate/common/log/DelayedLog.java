package org.adligo.fabricate.common.log;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DelayedLog extends FabLog {
  private Queue<Object> prints = new ConcurrentLinkedQueue<Object>();
  
  public DelayedLog(Map<String,Boolean> logSettings, boolean allOn) {
    super(logSettings, allOn);
  }

  public void printlnNow(String p) {
    super.println(p);
  }
  
  public void printTraceNow(Throwable t) {
    super.printTrace(t);
  }
  
  @Override
  public void println(String p) {
    prints.add(p);
  }

  @Override
  public void printTrace(Throwable t) {
    prints.add(t);
  }

  public void render() {
    Object obj = prints.poll();
    while (obj != null) {
      if (obj instanceof String) {
        super.println((String) obj);
      } else if (obj instanceof Throwable) {
        super.printTrace((Throwable) obj);
      }
      obj = prints.poll();
    }
  }
}
