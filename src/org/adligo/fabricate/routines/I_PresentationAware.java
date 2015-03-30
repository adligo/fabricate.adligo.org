package org.adligo.fabricate.routines;

/**
 * @author scott
 *
 */
public interface I_PresentationAware {
  public I_PresenterFactory getViewFactory();
  public void setViewFactory(I_PresenterFactory factory);
}
