package org.adligo.fabricate.xml_io;

import org.adligo.fabricate.common.FabricateXmlDiscovery;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

public class SchemaLoader implements LSResourceResolver {
  public static final SchemaLoader INSTANCE = new SchemaLoader();
  public static final String XML_PACKAGE = "/org/adligo/fabricate/xml/";
  public static final String DEV_SCHEMA = XML_PACKAGE + "dev.xsd";
  public static final String FAB_SCHEMA = XML_PACKAGE + "fabricate.xsd";
  public static final String LIB_SCHEMA = XML_PACKAGE + "library.xsd";
  public static final String PROJECT_SCHEMA = XML_PACKAGE + "project.xsd";
  public static final String RESULT_SCHEMA = XML_PACKAGE + "result.xsd";
  public static final String TASKS_SCHEMA = XML_PACKAGE + "tasks.xsd";
  
  private DOMImplementationRegistry registry;
  private DOMImplementationLS domImplementationLS;
  private SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
  private Map<String,String> namespaceToSchema = new HashMap<String,String>();
  private Map<String,LSInput> namespaceToLSInput = new HashMap<String,LSInput>();
  private Schema schema;
  
  private SchemaLoader() {
    try {

      registry = DOMImplementationRegistry.newInstance();
      domImplementationLS = (DOMImplementationLS) registry
              .getDOMImplementation("LS 3.0");

    } catch (ClassCastException | ClassNotFoundException | InstantiationException | 
        IllegalAccessException e) {
        e.printStackTrace();
    }
    
    namespaceToSchema.put("http://www.adligo.org/fabricate/xml/io/dev", DEV_SCHEMA);
    namespaceToSchema.put("http://www.adligo.org/fabricate/xml/io", FAB_SCHEMA);
    namespaceToSchema.put("http://www.adligo.org/fabricate/xml/io/library", LIB_SCHEMA);
    namespaceToSchema.put("http://www.adligo.org/fabricate/xml/io/project", PROJECT_SCHEMA);
    namespaceToSchema.put("http://www.adligo.org/fabricate/xml/io/result", RESULT_SCHEMA);
    namespaceToSchema.put("http://www.adligo.org/fabricate/xml/io/tasks", TASKS_SCHEMA);
    factory.setResourceResolver(this);  
    
    try {
      load(new Source[] {
          new StreamSource(FabricateXmlDiscovery.class.getResourceAsStream(DEV_SCHEMA)),
          new StreamSource(FabricateXmlDiscovery.class.getResourceAsStream(LIB_SCHEMA)),
          new StreamSource(FabricateXmlDiscovery.class.getResourceAsStream(TASKS_SCHEMA)),
          new StreamSource(FabricateXmlDiscovery.class.getResourceAsStream(FAB_SCHEMA)),
          new StreamSource(FabricateXmlDiscovery.class.getResourceAsStream(PROJECT_SCHEMA)),
          new StreamSource(FabricateXmlDiscovery.class.getResourceAsStream(RESULT_SCHEMA))
      });
    } catch (IOException x) {
      x.printStackTrace();
    }
    
  }
  private void load(Source [] sources) throws IOException {
   InputStream in  = null;
    try {
       Schema schema = factory.newSchema(sources);
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
    return schema;
  }
  
  @Override
  public LSInput resolveResource(String type, String namespaceURI,
          String publicId, String systemId, String baseURI) {
    
      LSInput toRet = namespaceToLSInput.get(namespaceURI);
      if (toRet != null) {
        return toRet;
      }
      
      LSInput ret = domImplementationLS.createLSInput();
      String schemaResource = namespaceToSchema.get(namespaceURI);
      if (schemaResource != null) {
        InputStream in = SchemaLoader.class.getResourceAsStream(schemaResource);
        ret.setByteStream(in);
        //ret.setSystemId(systemId);
        namespaceToLSInput.put(namespaceURI, ret);
        return ret;
      }

      return null;
  }
}
