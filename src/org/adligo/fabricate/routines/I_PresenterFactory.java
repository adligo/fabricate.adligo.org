package org.adligo.fabricate.routines;

public interface I_PresenterFactory {
  public <T> T newPresenter(Class<T> clazz);
}
