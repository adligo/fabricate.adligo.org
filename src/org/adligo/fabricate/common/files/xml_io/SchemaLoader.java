package org.adligo.fabricate.common.files.xml_io;

import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class SchemaLoader implements LSResourceResolver {
  public static final String COMMON_V1_0 = "common_v1_0.xsd";
  public static final String DEPOT_V1_0 = "depot_v1_0.xsd";
  public static final String DEV_V1_0 = "dev_v1_0.xsd";
  public static final String FABRICATE_V1_0 = "fabricate_v1_0.xsd";
  public static final String LIBRARY_V1_0 = "library_v1_0.xsd";
  public static final String PROJECT_V1_0 = "project_v1_0.xsd";
  public static final String RESULT_V1_0 = "result_v1_0.xsd";
  
  public static final String BASE_JAVA_NS_V1 = "org.adligo.fabricate.xml.io_v1";
  private static final String BASE_JAVA_NS_V1_WD = BASE_JAVA_NS_V1 + ".";
  public static final String JAVA_COMMON_NS_V1_0 = BASE_JAVA_NS_V1_WD +"common_v1_0";
  public static final String JAVA_DEPOT_NS_V1_0 = BASE_JAVA_NS_V1_WD +"depot_v1_0";
  public static final String JAVA_DEV_NS_V1_0 = BASE_JAVA_NS_V1_WD +"dev_v1_0";
  public static final String JAVA_FAB_NS_V1_0 = BASE_JAVA_NS_V1_WD +"fabricate_v1_0";
  public static final String JAVA_LIB_NS_V1_0 = BASE_JAVA_NS_V1_WD +"library_v1_0";
  public static final String JAVA_PROJECT_NS_V1_0 = BASE_JAVA_NS_V1_WD +"project_v1_0";
  public static final String JAVA_RESULT_NS_V1_0 = BASE_JAVA_NS_V1_WD +"result_v1_0";
  
  public static final String BASE_NS_V1 = "http://www.adligo.org/fabricate/xml/io_v1/";
  public static final String COMMON_NS_V1_0 = BASE_NS_V1 + COMMON_V1_0;
  public static final String DEPOT_NS_V1_0 = BASE_NS_V1 + DEPOT_V1_0;
  public static final String DEV_NS_V1_0 = BASE_NS_V1 + DEV_V1_0;
  public static final String FAB_NS_V1_0 = BASE_NS_V1 + FABRICATE_V1_0;
  public static final String LIB_NS_V1_0 = BASE_NS_V1 + LIBRARY_V1_0;
  public static final String PROJECT_NS_V1_0 = BASE_NS_V1 + PROJECT_V1_0;
  public static final String RESULT_NS_V1_0 = BASE_NS_V1 + RESULT_V1_0;
  
  
  public static final String UNKNOWN_NAMESPACE_URI = "Unknown namespaceURI ";
  public static final SchemaLoader INSTANCE = new SchemaLoader();
  public static final String XML_PACKAGE = "/org/adligo/fabricate/xml/";
  public static final String COMMON_SCHEMA_V1_0 = XML_PACKAGE + "common_v1_0.xsd";
  public static final String DEPOT_SCHEMA_V1_0 = XML_PACKAGE + "depot_v1_0.xsd";
  public static final String DEV_SCHEMA_V1_0 = XML_PACKAGE + "dev_v1_0.xsd";
  public static final String FAB_SCHEMA_V1_0 = XML_PACKAGE + "fabricate_v1_0.xsd";
  public static final String LIB_SCHEMA_V1_0 = XML_PACKAGE + "library_v1_0.xsd";
  public static final String PROJECT_SCHEMA_V1_0 = XML_PACKAGE + "project_v1_0.xsd";
  public static final String RESULT_SCHEMA_V1_0 = XML_PACKAGE + "result_v1_0.xsd";

  
  private DOMImplementationRegistry registry_;
  private DOMImplementationLS domImplementationLS_;
  private SchemaFactory factory_ = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
  private Map<String,String> namespaceToSchema_ = new ConcurrentHashMap<String,String>();
  private Map<String,LSInput> namespaceToLSInput_ = new ConcurrentHashMap<String,LSInput>();
  private Schema schema_;
  
  private SchemaLoader() {
    try {

      registry_ = DOMImplementationRegistry.newInstance();
      domImplementationLS_ = (DOMImplementationLS) registry_
              .getDOMImplementation("LS 3.0");

    } catch (ClassCastException | ClassNotFoundException | InstantiationException | 
        IllegalAccessException e) {
        e.printStackTrace();
    }
    
    namespaceToSchema_.put(COMMON_NS_V1_0, COMMON_SCHEMA_V1_0);
    namespaceToSchema_.put(DEPOT_NS_V1_0, DEPOT_SCHEMA_V1_0);
    namespaceToSchema_.put(DEV_NS_V1_0, DEV_SCHEMA_V1_0);
    namespaceToSchema_.put(FAB_NS_V1_0, FAB_SCHEMA_V1_0);
    namespaceToSchema_.put(LIB_NS_V1_0, LIB_SCHEMA_V1_0);
    namespaceToSchema_.put(PROJECT_NS_V1_0, PROJECT_SCHEMA_V1_0);
    namespaceToSchema_.put(RESULT_NS_V1_0, RESULT_SCHEMA_V1_0);
    
    factory_.setResourceResolver(this);  
    
    try {
      load(new Source[] {
          //order may be important here
          new StreamSource(getInputStream(COMMON_SCHEMA_V1_0)),
          new StreamSource(getInputStream(DEPOT_SCHEMA_V1_0)),
          new StreamSource(getInputStream(DEV_SCHEMA_V1_0)),
          new StreamSource(getInputStream(LIB_SCHEMA_V1_0)),
          new StreamSource(getInputStream(FAB_SCHEMA_V1_0)),
          new StreamSource(getInputStream(PROJECT_SCHEMA_V1_0)),
          new StreamSource(getInputStream(RESULT_SCHEMA_V1_0))
      });
    } catch (IOException x) {
      x.printStackTrace();
    }
    
  }
  /**
   * @param path
   * @return
   * @throws IOException
   */
  private InputStream getInputStream(String path) throws IOException {
    InputStream in = SchemaLoader.class.getResourceAsStream(path);
    if (in == null) {
      // I actually consider this a bug implementation of Class.getResourceAsStream, 
      // which could easily do this dumb double check for us.
      in = SchemaLoader.class.getClassLoader().getResourceAsStream(path);
      if (in == null) {
        throw new IOException(path);
      }
    }
    return in;
  }
  private void load(Source [] sources) throws IOException {
   InputStream in  = null;
    try {
       schema_ = factory_.newSchema(sources);
    } catch (SAXException x) {
      throw new IOException(x);
    } finally {
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
          //do nothing
        }
      }
    }
  }
  
  public Schema get() {
    return schema_;
  }
  
  @Override
  public LSInput resolveResource(String type, String namespaceURI,
          String publicId, String systemId, String baseURI) {
    
      LSInput toRet = namespaceToLSInput_.get(namespaceURI);
      if (toRet != null) {
        return toRet;
      }
      
      LSInput ret = domImplementationLS_.createLSInput();
      String schemaResource = namespaceToSchema_.get(namespaceURI);
      if (schemaResource != null) {
        InputStream in = SchemaLoader.class.getResourceAsStream(schemaResource);
        ret.setByteStream(in);
        //ret.setSystemId(systemId);
        ret.setPublicId(namespaceURI);
        namespaceToLSInput_.put(namespaceURI, ret);
        return ret;
      }
      throw new IllegalArgumentException(UNKNOWN_NAMESPACE_URI + namespaceURI);
  }
}
