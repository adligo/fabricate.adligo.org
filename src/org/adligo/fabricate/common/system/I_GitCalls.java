package org.adligo.fabricate.common.system;

import org.adligo.fabricate.models.common.I_ExecutionEnvironment;

import java.io.IOException;

public interface I_GitCalls {

  /** 
   * git clone
   * @param env the environment (environment variables) for the git process
   * @param project the name of the project
   * @param projectsDir the directory where the projects are stored
   */
  public I_ExecutingProcess clone(I_ExecutionEnvironment env, String project, String projectsDir)  throws IOException;

  /**
   * git checkout
   * @param project
   * @param localProjectDir
   * @param version
   * @return
   * @throws IOException
   */
  public boolean checkout(String project, String projectsDir, String version)
      throws IOException;
  
  /**
   * makes sure git is installed
   * @param exe
   * @return
   * @throws IOException
   */
  public boolean check(I_Executor exe) throws IOException;

  /**
   * git describe in the current running directory
   * @return
   * @throws IOException
   */
  public String describe() throws IOException;
  public String describe(String where) throws IOException;

  public String getHostname();
  public int getPort();

  
  
  public String getProtocol();
  
  public String getRemotePath();
  

  public String getUser();

  /** 
   * git pull
   * @param env the environment (environment variables) for the git process
   * @param project the name of the project
   * @param projectsDir the directory where the projects are stored
   */
  public boolean pull(I_ExecutionEnvironment env, String project, String projectsDir) throws IOException;

  /**
   * if the output of a git call is successful
   * or not, determined by looking for 'failue:' or 'error:' in the output.
   * @param result
   * @return
   */
  public boolean isSuccess(String result);


  public void setHostname(String hostname);

  public void setProtocol(String protocol);
  public void setPort(int port);
  public void setRemotePath(String remotePath);
  
  public void setUser(String user);


  

}