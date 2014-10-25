package org.adligo.fabricate.models;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import javax.xml.xpath.XPathExpressionException;

public class Fabricate extends DocumentModel {
  public static final String JAVA_XMX_DEFAULT = "64m";
  public static final String JAVA_XMS_DEFAULT = "16m";
  
  public Fabricate(Document doc) {
    super(doc);
  }
  
  public String getJavaXmx() {
    return super.getAttributeValue("/fabricate/java@Xmx", JAVA_XMX_DEFAULT);
  }
  
  public String getJavaXms() {
    return super.getAttributeValue("/fabricate/java@Xms", JAVA_XMS_DEFAULT);
  }
}
