package org.adligo.fabricate.common.log;

import org.adligo.fabricate.common.util.MethodBlocker;

import java.util.ArrayList;
import java.util.List;

public class DeferredLog implements I_FabLog {
  private final MethodBlocker setDelegateBlocker_;
  
  private I_FabLog delegate_;

  public DeferredLog() {
    List<String> allowedCallers = new ArrayList<String>();
    allowedCallers.add("org.adligo.fabricate.common.system.FabSystem");
    setDelegateBlocker_ = new MethodBlocker(DeferredLog.class, "setDelegate", 
        allowedCallers);
    
  }
  public I_FabLog getDelegate(){
    return delegate_;
  }
  
  public void setDelegate(I_FabLog delegate){
    setDelegateBlocker_.checkAllowed();
    delegate_ = delegate;
  }
  
  
  @Override
  public boolean isLogEnabled(Class<?> clazz) {
    if (delegate_ == null) {
      return false;
    } else {
      return delegate_.isLogEnabled(clazz);
    }
  }

  @Override
  public void println(String p) {
    if (delegate_ == null) {
      ThreadLocalPrintStream.println(p);
    } else {
      delegate_.println(p);
    }
  }

  @Override
  public void printTrace(Throwable t) {
    if (delegate_ == null) {
      ThreadLocalPrintStream.printTrace(t);
    } else {
      delegate_.printTrace(t);
    }
  }

}
