package org.adligo.fabricate.routines;

/**
 * @author scott
 *
 */
public interface I_ViewAware {
  public I_ViewFactory getViewFactory();
  public void setViewFactory(I_ViewFactory factory);
}
