package org.adligo.fabricate.common.files;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class IncludesExcludesFileMatcher implements I_FileMatcher {
  private List<I_FileMatcher> excludes_ = new ArrayList<I_FileMatcher>();
  private List<I_FileMatcher> includes_ = new ArrayList<I_FileMatcher>();


  public IncludesExcludesFileMatcher(Collection<? extends I_FileMatcher> includes, 
      Collection<? extends I_FileMatcher> excludes) {
    if (includes != null) {
      includes_.addAll(includes);
    }
    if (excludes != null) {
      excludes_.addAll(excludes);
    }
  }
  
  @Override
  public boolean isMatch(String path) {
    
    for (I_FileMatcher i: includes_) {
      if (i.isMatch(path)) {
        boolean excluded = false;
        for (I_FileMatcher e: excludes_) {
          if (e.isMatch(path)) {
            excluded = true;
            break;
          }
        }
        return !excluded;
      }
    }
    return false;
  }
  
}
