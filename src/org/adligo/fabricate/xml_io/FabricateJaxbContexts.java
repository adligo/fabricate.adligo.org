package org.adligo.fabricate.xml_io;

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
    
    private static final JAXBContext getDepotContext() {
      try {
        return JAXBContext.newInstance("org.adligo.fabricate.xml.io.depot.v1_0");
      } catch (JAXBException e) {
        throw new RuntimeException(e);
      }
    }
}
