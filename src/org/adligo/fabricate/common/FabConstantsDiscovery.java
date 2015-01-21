package org.adligo.fabricate.common;

import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_FileMessages;
import org.adligo.fabricate.common.i18n.I_GitMessages;
import org.adligo.fabricate.common.i18n.I_ProjectMessages;
import org.adligo.fabricate.common.i18n.I_SystemMessages;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

public class FabConstantsDiscovery implements I_FabricateConstants {
  private Properties labels_;
  private boolean leftToRight_ = true;
  private String language_;
  private String country_;
  
  public FabConstantsDiscovery(String language, String country) throws IOException {
    language_ = language;
    country_ = country;
    labels_ = new Properties();
    InputStream in = FabConstantsDiscovery.class.getResourceAsStream(
        "/org/adligo/fabricate/common/i18n/Constants_" + language_ + "_" + country + ".properties");
    
    if (in == null) {
      throw new IOException();
    }
    labels_.load(in);
    String ltr = labels_.getProperty("ltr");
    if ("false".equalsIgnoreCase(ltr)) {
      leftToRight_ = false;
    }
  }

  @Override
  public boolean isLeftToRight() {
    return leftToRight_;
  }

  @Override
  public I_FileMessages getFileMessages() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public I_GitMessages getGitMessages() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getLineSeperator() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public I_ProjectMessages getProjectMessages() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public I_SystemMessages getSystemMessages() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getLanguage() {
    return language_;
  }

  @Override
  public String getCountry() {
    return country_;
  }

}
