package org.adligo.fabricate.common;


/**
 * This is the plugin interface for 
 * fabricate commands i.e.;
 * fab cmd="classpath2eclipse"
 * 
 * and may be used to plug into
 * stage tasks, using the param tree;
 * i.e.
 * fabricate.xml
 *    <stage name="foo">
 *      <param key="obsfucationPlugin" value="org.example.someObsfucationProject">
 *        <param key="inputFile" val="obsfucation.txt"\>
 *      </param>
 *    </stage>
 * 
 * @author scott
 *
 */
public interface I_Plugin {
  public void run(I_ProjectContext ctx, I_ParamsTree params);
}
