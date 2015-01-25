package org.adligo.fabricate.routines;
/**
 * This is a marker interface which Fabricate uses
 * to detect if a Routine needs to know about the
 * SCM (Source Control Management) which is used by
 * this fabrication.  
 * 
 * @author scott
 *
 * @param <T>
 */
public interface I_ScmAware <T> {
  public T getScm();
  public void setScm(T scm);
}
