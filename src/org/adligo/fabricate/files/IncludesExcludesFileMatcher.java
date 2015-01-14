package org.adligo.fabricate.files;

import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.log.I_FabLog;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class IncludesExcludesFileMatcher implements I_FileMatcher {
  private final I_FabLog log_;
  private List<I_FileMatcher> exclueds_ = new ArrayList<I_FileMatcher>();
  private List<I_FileMatcher> includes_ = new ArrayList<I_FileMatcher>();
  
  /**
   * Create a complex IncludesExcludesFileMatcher
   * with multiple pattern matchers
   * @param includes if null use the includesDefault
   * @param includesDefault if used and null, nothing is excluded
   * @param excludes if null use the excludesDefault
   * @param excludesDefault if used and null, nothing is excluded
   */
  public IncludesExcludesFileMatcher(I_FabLog log, String includes, 
      String includesDefault, String excludes, String excludesDefault) {
      log_ = log;
      if (includes != null) {
        addIncludes(includes);
      } else if (includesDefault != null) {
        addIncludes(includesDefault);
      }
      if (excludes != null) {
        addExcludes(excludes);
      } else if (excludesDefault != null) {
        addExcludes(excludesDefault);
      }
  }

  
  
  public IncludesExcludesFileMatcher(I_FabLog log, List<I_FileMatcher> includes, List<I_FileMatcher> excludes_ ) {
    log_ = log;
    includes_.addAll(includes);
    excludes_.addAll(excludes_);
  }

  @Override
  public boolean isMatch(String path) {
    for (I_FileMatcher e: exclueds_) {
      if (e.isMatch(path)) {
        return false;
      }
    }
    for (I_FileMatcher i: includes_) {
      if (i.isMatch(path)) {
        return true;
      }
    }
    return false;
  }
  
  private void addIncludes(String includes) {
    StringTokenizer tokens = new StringTokenizer(includes, ",");
    while (tokens.hasMoreTokens()) {
      String pattern = tokens.nextToken();
      includes_.add(new PatternFileMatcher(log_, pattern, true));
    }
  }
  
  private void addExcludes(String excludes) {
    StringTokenizer tokens = new StringTokenizer(excludes, ",");
    while (tokens.hasMoreTokens()) {
      String pattern = tokens.nextToken();
      exclueds_.add(new PatternFileMatcher(log_, pattern, false));
    }
  }
}
