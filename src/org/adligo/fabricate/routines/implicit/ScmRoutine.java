package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.system.I_ExecutionResult;
import org.adligo.fabricate.common.system.I_Executor;
import org.adligo.fabricate.common.util.StringUtils;
import org.adligo.fabricate.models.common.FabricationMemoryConstants;
import org.adligo.fabricate.models.common.FabricationRoutineCreationException;
import org.adligo.fabricate.models.common.I_ExecutionEnvironmentMutant;
import org.adligo.fabricate.models.common.I_FabricationMemory;
import org.adligo.fabricate.models.common.I_FabricationMemoryMutant;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.models.common.I_RoutineBrief;
import org.adligo.fabricate.models.common.I_RoutineMemory;
import org.adligo.fabricate.models.common.I_RoutineMemoryMutant;
import org.adligo.fabricate.models.common.MemoryLock;
import org.adligo.fabricate.routines.I_FabricateAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;
import org.adligo.fabricate.routines.ProjectBriefQueueRoutine;
import org.adligo.fabricate.routines.SshAgentHelper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ScmRoutine extends ProjectBriefQueueRoutine {
  /**
   * key to routine memory 
   */
  private static final String SCM_CONTEXT = "scmContext";
  /**
   * This was intended to be a 
   * key to a optional command line argument which 
   * may contain a encrypted password for continuous integration builds.
   * 
   * However piping a password into ssh-add is NOT working,
   * and this feature will NOT work until that is also working.
   */
  public static final String GIT_KEYSTORE_PASSWORD = "gitKeystorePassword";
  /**
   * This was intended to be a 
   * key to the fabricate.xml parameter for this trait
   * for the open ssl keystorePassword
   * which must be encrypted in the file 
   * using the encrypt, decrypt trait set in fabricate.xml 
   * (or the implicit one).
   * 
   * However piping a password into ssh-add is NOT working,
   * and this feature will NOT work until that is also working.
   */
  public static final String KEYSTORE_PASSWORD = "keystorePassword";
  
  /**
   * key to the fabricate.xml parameter the location
   * of the private key defaults to ${user.home}/.ssh/id_rsa
   */
  public static final String PRIVATE_KEY_LOCATION = "privateKeyLocation";
  
  private ScmContext context_;
  
  @SuppressWarnings("boxing")
  @Override
  public boolean setup(I_FabricationMemoryMutant<Object> memory, I_RoutineMemoryMutant<Object> routineMemory)
      throws FabricationRoutineCreationException {
    
    I_RoutineBrief scm = fabricate_.getScm();
    context_ = new ScmContext(scm);
    if ("ssh".equals(context_.getProtocol())) {
      if (!system_.hasArg(cmdConstants_.getNoSshKeystorePassPhrase(true))) {
        Boolean setup = (Boolean) memory.get(FabricationMemoryConstants.SETUP_SSH_AGENT);
        if (setup == null) {
          manageSshKeystorePassword(memory, scm);
          memory.put(FabricationMemoryConstants.SETUP_SSH_AGENT, true);
          memory.addLock(new MemoryLock(FabricationMemoryConstants.SETUP_SSH_AGENT, 
              Collections.singleton(ScmRoutine.class.getName())));
        }
      }
    }
    routineMemory.put(SCM_CONTEXT, context_);
    boolean toRet = super.setup(memory, routineMemory);
    return toRet;
  }

  public void manageSshKeystorePassword(I_FabricationMemoryMutant<Object> memory, I_RoutineBrief scm)
      throws FabricationRoutineCreationException {
    
    // TODO figure out how to pipe a password to ssh-add, to make the following work
    //getKeystorePasswordFromFabricate(memory, scm)
    try{
      I_Executor exe = system_.getExecutor();
      I_ExecutionResult result =  exe.executeProcess(FabricationMemoryConstants.EMPTY_ENV,
          ".", "ssh-agent");
      if (result.getExitCode() != 0) {
        String message = sysMessages_.getTheFollowingCommandLineProgramExitedAbnormallyWithExitCodeX();
        message = message.replace("<X/>", "" + result.getExitCode());
        throw new IllegalStateException(message + System.lineSeparator() +
            "ssh-agent");
      }
      String out = result.getOutput();
      SshAgentHelper sshAgentHelper = new SshAgentHelper(out, sysMessages_);
      I_ExecutionEnvironmentMutant env = (I_ExecutionEnvironmentMutant) 
            memory.get(FabricationMemoryConstants.ENV);
      env.put(SshAgentHelper.SSH_AUTH_SOCK, sshAgentHelper.getSock());
      env.put(SshAgentHelper.SSH_AGENT_PID, sshAgentHelper.getPid());
      
      //note I tried to do my own dialog here and pipe the password to ssh-add
      //but it never really worked
      String loc = getPrivateKeyLocation();
      I_ExecutionResult addResult = exe.executeProcess(env, ".", "ssh-add", loc);
      if (addResult.getExitCode() != 0) {
        String message = sysMessages_.getTheFollowingCommandLineProgramExitedAbnormallyWithExitCodeX();
        message = message.replace("<X/>", "" + addResult.getExitCode());
        throw new IllegalStateException(message + system_.lineSeperator() +
            "ssh-add");
      }
    } catch (IOException e) {
      //pass to run monitor
      throw new RuntimeException(e);
    }
  }

  /**
   * This commented out method will get a encrypted password 
   * from the command line or fabricate.xml, however
   * I couldn't figure out how to pipe the password 
   * into ssh-add from java. It looks like
   * the only way to pass a password from 
   * Fabricate to ssh-agent would be to re-write
   * ssh-add in java and then call that.
   * 
   * 
   * 
  public String getKeystorePasswordFromFabricate(I_FabricationMemoryMutant memory, I_RoutineBrief scm)
      throws FabricationRoutineCreationException {
    String password = (String) memory.get(FabricationMemoryConstants.GIT_KEYSTORE_PASSWORD);
    if (password == null) {
      List<String> keystorePassEncrypted = scm.getParameters(KEYSTORE_PASSWORD);
      if (keystorePassEncrypted == null || keystorePassEncrypted.size() == 0) {
        //check command line args
        String value = system_.getArgValue(GIT_KEYSTORE_PASSWORD);
        if (value != null) {
          password = decryptPassword(memory, value);
        } 
      } else {
        String keystorePassEncryptedValue = keystorePassEncrypted.get(0);
        password = decryptPassword(memory, keystorePassEncryptedValue);
      }
    }
    return password;
  }
   */
  
  public String getPrivateKeyLocation() {
    String keyLocFromFile = brief_.getParameter(PRIVATE_KEY_LOCATION);
    String loc = "";
    String userHome = system_.getProperty("user.home", "");
    if (StringUtils.isEmpty(userHome)) {
      throw new IllegalStateException("no user home");
    }
    if (StringUtils.isEmpty(keyLocFromFile)) {
      loc = userHome + files_.getNameSeparator() + ".ssh" + files_.getNameSeparator() +
          "id_rsa";
    } else {
      if (keyLocFromFile.indexOf("${user.home}") == 0) {
        loc = userHome + keyLocFromFile.substring(12, keyLocFromFile.length());
      }
      StringBuilder sb = new StringBuilder();
      char [] chars = loc.toCharArray();
      for (int i = 0; i < chars.length; i++) {
        char c = chars[i];
        if (c == '/') {
          sb.append(files_.getNameSeparator());
        } else {
          sb.append(c);
        }
      }
      loc = sb.toString();
    }
    return loc;
  }

  @SuppressWarnings("unchecked")
  public String decryptPassword(I_FabricationMemoryMutant<Object> memory, 
      String keystorePassEncryptedValue)
      throws FabricationRoutineCreationException {
    String password;
    I_FabricationRoutine routine = traitFactory_.createRoutine(EncryptTrait.NAME, EncryptTrait.IMPLEMENTED_INTERFACES);
    ((I_InputAware<String>) routine).setInput(keystorePassEncryptedValue);
    routine.run();
    password = ((I_OutputProducer<String>) routine).getOutput();
    memory.put(FabricationMemoryConstants.GIT_KEYSTORE_PASSWORD, password);
    return password;
  }

  @Override
  public void setup(I_FabricationMemory<Object> memory, I_RoutineMemory<Object> routineMemory)
      throws FabricationRoutineCreationException {
    context_ = (ScmContext) routineMemory.get(SCM_CONTEXT);
    super.setup(memory, routineMemory);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public void setupTask(I_FabricationRoutine taskRoutine) {
    if (I_InputAware.class.isAssignableFrom(taskRoutine.getClass())) {
      List<Class<?>> genericTypes = ((I_InputAware) taskRoutine).getClassType(I_InputAware.class);
      if (genericTypes != null && genericTypes.size() == 1) {
        if (ScmContext.class.equals(genericTypes.get(0))) {
          ((I_InputAware<ScmContext>) taskRoutine).setInput(context_);
        }
      }
    }
    if (I_FabricateAware.class.isAssignableFrom(taskRoutine.getClass())) {
      ((I_FabricateAware) taskRoutine).setFabricate(fabricate_);
    }
    super.setupTask(taskRoutine);
  }
}
