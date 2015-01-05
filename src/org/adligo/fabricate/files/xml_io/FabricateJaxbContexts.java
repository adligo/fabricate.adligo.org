package org.adligo.fabricate.files.xml_io;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * from this suggestion
 * https://jaxb.java.net/guide/Performance_and_thread_safety.html
 * @author scott
 *
 */
public class FabricateJaxbContexts {
    public static final JAXBContext DEPOT_CONTEXT = getDepotContext();
    public static final JAXBContext DEV_CONTEXT = getDevContext();
    public static final JAXBContext FAB_CONTEXT = getFabContext();
    public static final JAXBContext LIB_CONTEXT = getLibContext();
    public static final JAXBContext PROJECT_CONTEXT = getProjectContext();
    public static final JAXBContext RESULT_CONTEXT = getResultContext();
    
    private static final JAXBContext getDepotContext() {
      try {
        return JAXBContext.newInstance(SchemaLoader.JAVA_DEPOT_NS_V1_0);
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    
    private static final JAXBContext getDevContext() {
      try {
        return JAXBContext.newInstance(SchemaLoader.JAVA_DEV_NS_V1_0);
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    
    private static final JAXBContext getFabContext() {
      try {
        return JAXBContext.newInstance(SchemaLoader.JAVA_FAB_NS_V1_0);
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    
    private static final JAXBContext getLibContext() {
      try {
        return JAXBContext.newInstance(SchemaLoader.JAVA_LIB_NS_V1_0);
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    
    
    private static final JAXBContext getProjectContext() {
      try {
        return JAXBContext.newInstance(SchemaLoader.JAVA_PROJECT_NS_V1_0);
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
    
    private static final JAXBContext getResultContext() {
      try {
        return JAXBContext.newInstance(SchemaLoader.JAVA_RESULT_NS_V1_0);
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
}
