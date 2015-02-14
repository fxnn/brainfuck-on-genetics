package de.fxnn.brainfuckongenetics.cli;

import java.io.IOException;

import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsConfiguration;

public class BrainfuckOnGeneticsApplication implements Runnable {

  public static final String GROUP_ID = "de.fxnn";

  public static final String ARTIFACT_ID = "brainfuck-on-genetics";

  public static void main(String[] args) {
    new BrainfuckOnGeneticsApplication().run();
  }

  private final MavenProperties mavenProperties = new MavenProperties(GROUP_ID, ARTIFACT_ID);

  @Override
  public void run() {
    try {

      BrainfuckOnGeneticsConfiguration configuration = createDefaultConfiguration();

      BrainfuckOnGeneticsShell shell = new BrainfuckOnGeneticsShell(mavenProperties, configuration);

      shell.createConsoleShell().commandLoop();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected BrainfuckOnGeneticsConfiguration createDefaultConfiguration() {
    return new BrainfuckOnGeneticsConfigurationFactory().fromDefaults();
  }
}
