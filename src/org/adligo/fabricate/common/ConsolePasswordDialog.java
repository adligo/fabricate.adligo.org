package org.adligo.fabricate.common;

import java.io.Console;

public class ConsolePasswordDialog {
  private static final Console CONSOLE = System.console();
  private final Console console_;
  
  public ConsolePasswordDialog() {
    console_ = CONSOLE;
  }
  public ConsolePasswordDialog(Console console) {
    console_ = console;
  }
  
  public String dialogPassword(String message) {
    return new String(console_.readPassword(message));
  }
}
