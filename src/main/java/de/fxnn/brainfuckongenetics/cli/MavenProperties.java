package de.fxnn.brainfuckongenetics.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MavenProperties {

  private static final String VERSION_KEY = "version";

  private static final String GROUP_ID_KEY = "groupId";

  private static final String ARTIFACT_ID_KEY = "artifactId";

  private final Properties properties;

  public MavenProperties(String groupId, String artifactId) {
    this.properties = readMavenProperties(groupId, artifactId);
  }

  public String getVersion() {
    return properties.getProperty(VERSION_KEY);
  }

  public String getGroupId() {
    return properties.getProperty(GROUP_ID_KEY);
  }

  public String getArtifactId() {
    return properties.getProperty(ARTIFACT_ID_KEY);
  }

  private Properties readMavenProperties(String groupId, String artifactId) {
    Properties result = new Properties();
    String pomPropertiesPath = "/META-INF/maven/" + groupId + "/" + artifactId + "/pom.properties";

    try {
      ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
      InputStream pomPropertiesStream = contextClassLoader.getResourceAsStream(pomPropertiesPath);
      if (pomPropertiesStream != null) {
        result.load(pomPropertiesStream);
      } else {
        result.put(GROUP_ID_KEY, groupId);
        result.put(ARTIFACT_ID_KEY, artifactId);
        result.put(VERSION_KEY, "UNKNOWN");
      }
    } catch (IOException e) {
      throw new RuntimeException("Could not read properties from " + pomPropertiesPath, e);
    }

    return result;
  }

}
