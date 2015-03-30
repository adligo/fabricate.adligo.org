package org.adligo.fabricate.routines;

public interface I_ViewFactory {
  public <T> T newPresenter(Class<T> clazz);
}
