package org.adligo.fabricate.common.system;

import org.adligo.fabricate.common.files.I_FabFileIO;
import org.adligo.fabricate.common.i18n.I_FabricateConstants;
import org.adligo.fabricate.common.i18n.I_SystemMessages;
import org.adligo.fabricate.common.log.I_FabLog;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This class helps the fabricate.adligo.org/build.xml
 * determine the tag from git for 
 * this project's build.  This is used when
 * fabricate is developed so that
 * 
 * @author scott
 *
 */
public class AntHelper {

	@SuppressWarnings("unused")
  public static void main(String [] args) {
	  new AntHelper(new FabSystem(), args);
	}
	
	private final I_FabFileIO files_;
	private final I_FabLog log_;
	private final I_SystemMessages sysMessages_;
	
	public AntHelper(I_FabSystem sys, String [] args) {
	  files_ = sys.getFileIO();
	  log_ = sys.getLog();
	  I_FabricateConstants constants_ = sys.getConstants();
	  sysMessages_ = constants_.getSystemMessages();
	      
	  if (args == null || args.length == 0) {
      log_.println(sysMessages_.getAntHelperRequiresADirectoryArgument());
      return;
    }
    String dir = args[0];
    OutputStream fos = null;
    I_GitCalls calls = sys.newGitCalls();
    try {
      calls.check(sys.getExecutor());
    } catch (IOException x) {
      log_.println(sysMessages_.getGitDoesNotAppearToBeInstalledPleaseInstallIt());
      log_.printTrace(x);
      return;
    }
    try {
      
      String desc = calls.describe();
      fos = files_.newFileOutputStream(dir + files_.getNameSeparator() + "version.properties");
      fos.write(new String("fabricate_name=fabricate_" + desc + 
          sys.lineSeparator()).getBytes("UTF-8"));
      fos.write(new String("fabricate_version=" + desc + 
          sys.lineSeparator()).getBytes("UTF-8"));
      
    } catch (IOException e) {
      log_.printTrace(e);
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException e) {
          //do nothing
        }
      }
    }
	}
	
}
