package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.DepotEntryMutant;
import org.adligo.fabricate.common.I_Depot;
import org.adligo.fabricate.common.I_FabContext;
import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.NamedProject;

import java.util.Map;

/**
 * This puts files from the build, 
 * into the depot based on some parameters.
 * 
 * @author scott
 *
 */
public class DepositTask extends BaseTask implements I_FabTask {
  private I_Depot depot_;
  
  @Override
  public void setup(I_FabContext ctx, NamedProject project, Map<String, String> params) {
    super.setup(ctx, project, params);
    depot_ = ctx.getDepot();
  }

  public void execute(String inputFile, String artifactType) {
    
    DepotEntryMutant dem = new DepotEntryMutant();
    dem.setArtifactType(artifactType);
    dem.setProjectName(projectName_); 
    depot_.add(inputFile, dem);
  }

}
