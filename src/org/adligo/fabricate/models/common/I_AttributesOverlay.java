package org.adligo.fabricate.models.common;

import java.util.List;

/**
 * This class represents attributes from a fabricate.xml file
 * a project.xml.  The attributes are accessible in two manors,
 * the merged manor where a attribute key from a project.xml
 * overrides the attribute key from fabricate.xml.  The merged
 * results are obtained by the I_AttributesContainer.  The
 * second manor is to treat all the parameters from fabricate.xml
 * and project.xml as a super set.  The methods getAll* in this
 * interface provide access for that style of probing attributes.
 *    Access to the project or fabricate specific attributes
 * is not provided, since it is assumed that the I_Fabricate
 * or I_Project instance will be available to provide attributes
 * for routines that want the specific attributes from a project.xml
 * or a fabricate.xml file.
 * @author scott
 *
 */
public interface I_AttributesOverlay extends I_AttributesContainer {

  /**
   * Get the attributes that came from the respective project.xml
   * combined with the fabricate.xml attributes.
   * @return
   */
  public abstract List<I_Parameter> getAllAttributes();

  /**
   * Get only the first value that came that came from the respective project.xml
   * combined with the fabricate.xml attributes.
   * @return
   */
  public abstract I_Parameter getAnyAttribute(String key);

  /**
   * Get only the first value that came that came from the respective project.xml
   * combined with the fabricate.xml attributes.
   * @return
   */
  public abstract String getAnyAttributeValue(String key);
  
  /**
   * Get the attributes that came from the respective project.xml
   * combined with the fabricate.xml attributes.
   * @return
   */
  public abstract List<I_Parameter> getAllAttributes(String key);

  /**
   * Get the attributes that came from the respective project.xml
   * combined with the fabricate.xml attributes.
   * @return
   */
  public abstract List<I_Parameter> getAllAttributes(String key, String value);
  
  /**
   * Get the attributes values that came from the respective fabricate.xml,
   * project.xml file or overlay (project.xml overriding fabricate.xml attributes).
   * @return
   */
  public abstract List<String> getAllAttributeValues(String key);
}
