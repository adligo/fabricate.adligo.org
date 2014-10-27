package org.adligo.fabricate.common;

public enum FabRunType {
  DEFAULT(0),PROJECT(1),DEVELOPMENT(2), AGGERGRATE(3);
  private int id_;
  
  private FabRunType(int id) {
    id_ = id;
  }
}
