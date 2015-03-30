package org.adligo.fabricate.models.project;

import java.util.List;

public interface I_ProjectModifications {

  public abstract List<String> getAdditions();

  public abstract List<String> getDeletions();

  public abstract List<String> getModifications();

}