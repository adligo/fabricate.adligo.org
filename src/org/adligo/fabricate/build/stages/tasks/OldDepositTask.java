package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_FabTask;
import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.I_StageContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.depot.DepotEntryMutant;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.I_KeyValue;
import org.adligo.fabricate.models.project.I_Project;

import java.io.IOException;
import java.util.Map;

/**
 * This puts files from the build, 
 * into the depot based on some parameters.
 * 
 * @author scott
 *
 */
public class OldDepositTask extends OldBaseTask implements I_FabTask {
  private I_Depot depot_;
  
  @Override
  public void setup(I_RunContext ctx, NamedProject project, Map<String, String> params) {
    super.setup(ctx, project, params);
    depot_ = ctx.getDepot();
  }

  public void execute(String inputFile, String artifactType) {
    
    DepotEntryMutant dem = new DepotEntryMutant();
    dem.setArtifactType(artifactType);
    dem.setProjectName(projectName_); 
    depot_.add(inputFile, dem);
  }
  @Override
  public void setup(I_RunContext ctx, I_StageContext stageCtx, I_Project project) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void execute(I_KeyValue taskParams) throws IOException {
    // TODO Auto-generated method stub
    
  }
}
