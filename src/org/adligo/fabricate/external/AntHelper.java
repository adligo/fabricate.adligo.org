package org.adligo.fabricate.external;

import org.adligo.fabricate.external.GitCalls;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * This class helps the ant/build.xml
 * determine the tag from git for 
 * this project's build.
 * 
 * @author scott
 *
 */
public class AntHelper {

	public static void main(String [] args) {
	  if (args.length == 0) {
	    System.out.println("Helper requires a dir argument.");
	  }
	  String dir = args[0];
		try {
      if (!GitCalls.check()) {
        System.out.println("Git does NOT appear to be installed, please install it.");
        return;
      }
      String desc = GitCalls.describe();
      File file = new File(dir + File.separator + "version.properties");
      System.out.println("writing " + file.getAbsolutePath());
      FileOutputStream fos = new FileOutputStream(file);
      fos.write(new String("fabricate_name=fabricate_" + desc + 
          System.lineSeparator()).getBytes("UTF-8"));
      fos.write(new String("fabricate_version=" + desc + 
          System.lineSeparator()).getBytes("UTF-8"));
      fos.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    
	}
	
}
