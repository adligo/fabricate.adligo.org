package org.adligo.fabricate.build.stages.tasks;

import org.adligo.fabricate.common.I_RunContext;
import org.adligo.fabricate.common.I_StageContext;
import org.adligo.fabricate.common.NamedProject;
import org.adligo.fabricate.depot.I_Depot;
import org.adligo.fabricate.models.common.I_Parameter;
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
public class OldDepositTask extends OldBaseTask {
  private I_Depot depot_;
  
  @Override
  public void setup(I_RunContext ctx, NamedProject project, Map<String, String> params) {
    super.setup(ctx, project, params);
    depot_ = ctx.getDepot();
  }

  public void execute(String inputFile, String artifactType) {
    /*
    ArtifactMutant dem = new ArtifactMutant();
    dem.setArtifactType(artifactType);
    dem.setProjectName(projectName_); 
    depot_.add(inputFile, dem);
    */
  }
  @Override
  public void setup(I_RunContext ctx, I_StageContext stageCtx, I_Project project) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void execute(I_Parameter taskParams) throws IOException {
    // TODO Auto-generated method stub
    
  }
}
