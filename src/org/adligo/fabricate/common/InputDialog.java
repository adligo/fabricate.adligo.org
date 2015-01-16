package org.adligo.fabricate.common;

import com.sun.jndi.url.corbaname.corbanameURLContextFactory;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

public class InputDialog {
  private static final Console CONSOLE = System.console();
  
 private final Console console_;
  
  public InputDialog() {
    console_ = CONSOLE;
  }
  public InputDialog(Console console) {
    console_ = console;
  }
  
  public String dialogMultipleLine(String message) {
    StringBuilder sb = new StringBuilder();
    CONSOLE.format(message);
    boolean lastLineEmpty = false;
    while (!lastLineEmpty) {
      String line = CONSOLE.readLine();
      if (line.isEmpty()) {
        lastLineEmpty = true;
      } else {
        sb.append(line);
        sb.append("\n");
      }
    }
    return sb.toString();
  }
}
