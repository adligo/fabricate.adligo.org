package org.adligo.fabricate.models;


import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class DocumentModel {
  XPathFactory xFactory = XPathFactory.newInstance();
  Document doc_;
  
  public DocumentModel(Document doc) {
    doc_ = doc;
  }
  
  
  public Node find(String xpath) throws XPathExpressionException {
    XPath xp = xFactory.newXPath();

    // compile the XPath expression
    XPathExpression expr = xp.compile(xpath);
    // run the query and get a nodeset
    return (Node) expr.evaluate(doc_, XPathConstants.NODE);
  }
  
  public String getAttributeValue(String xpath, String defaultValue) {
    Node node = null;
    try {
      node = find(xpath);
    } catch (XPathExpressionException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    if (node != null) {
      return node.getNodeValue();
    }
    return defaultValue;
  }
}
