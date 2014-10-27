package org.adligo.fabricate.common;

public enum FabRunType {
  DEFAULT(0),DEVELOPMENT(1), AGGERGRATE(2);
  private int id_;
  
  private FabRunType(int id) {
    id_ = id;
  }
}
