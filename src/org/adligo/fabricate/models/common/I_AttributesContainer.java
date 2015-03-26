package org.adligo.fabricate.models.common;

import java.util.List;

/**
 * This interface is used for models that contain attributes, namely
 * the Fabricate*, Project* and AttributeOverlay* classes.
 * 
 * @author scott
 *
 */
public interface I_AttributesContainer {

  /**
   * Get the attributes that came from the the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract List<I_Parameter> getAttributes();

  /**
   * Get only the first attribute that came from the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract I_Parameter getAttribute(String key);

  /**
   * Get only the first value that came from the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract String getAttributeValue(String key);
  
  /**
   * Get the attributes that came from the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract List<I_Parameter> getAttributes(String key);

  /**
   * Get the attributes that came from the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract List<I_Parameter> getAttributes(String key, String value);
  
  /**
   * Get the attributes values that came from the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract List<String> getAttributeValues(String key);
}