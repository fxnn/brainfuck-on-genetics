package de.fxnn.brainfuckongenetics.cli;

import java.util.concurrent.TimeUnit;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import asg.cliche.ShellManageable;
import com.google.common.base.Joiner;
import com.google.common.base.Throwables;
import de.fxnn.brainfuckongenetics.BrainfuckGenerationDescriber;
import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsConfiguration;
import de.fxnn.brainfuckongenetics.job.BrainfuckOnGeneticsJob;
import de.fxnn.brainfuckongenetics.job.JobCurrentlyRunningException;
import de.fxnn.brainfuckongenetics.job.JobExecutionException;
import de.fxnn.brainfuckongenetics.job.NoJobActiveException;
import de.fxnn.genetics.generation.GenerationWithConcurrentFitnessFunction;

public class BrainfuckOnGeneticsShell implements ShellManageable {

  private final MavenProperties mavenProperties;

  private final BrainfuckGenerationDescriber brainfuckGenerationDescriber;

  private BrainfuckOnGeneticsConfiguration configuration;

  private BrainfuckOnGeneticsJob job;

  public BrainfuckOnGeneticsShell(MavenProperties mavenProperties,
      BrainfuckOnGeneticsConfiguration initialConfiguration) {
    this.mavenProperties = mavenProperties;
    this.configuration = initialConfiguration;
    this.brainfuckGenerationDescriber = new BrainfuckGenerationDescriber();
    this.job = new BrainfuckOnGeneticsJob();
  }

  public Shell createConsoleShell() {
    return ShellFactory.createConsoleShell(mavenProperties.getArtifactId(), getAppName(), this);
  }

  private String getAppName() {
    return String.format("%s %s", mavenProperties.getArtifactId(), mavenProperties.getVersion());
  }

  @Command
  public int populationSize() {
    return configuration.getPopulationSize();
  }

  @Command
  public void populationSize(@Param(name="value") int value) {
    configuration.setPopulationSize(value);
  }

  @Command
  public int numberOfGenerationRounds() {
    return configuration.getNumberOfGenerationRounds();
  }

  @Command
  public void numberOfGenerationRounds(@Param(name="value") int value) {
    configuration.setNumberOfGenerationRounds(value);
  }

  @Command
  public long randomSeed() {
    return configuration.getRandomSeed();
  }

  @Command
  public void randomSeed(@Param(name="value") long value) {
    configuration.setRandomSeed(value);
  }

  @Command
  public double selectionRatio() {
    return configuration.getSelectionRatio();
  }

  @Command
  public void selectionRatio(@Param(name="value") double value) {
    configuration.setSelectionRatio(value);
  }

  @Command
  public String fitnessFunction() {
    return configuration.getFitnessFunction().toString();
  }

  @Command
  public String fitnessFunctionTimeout() {
    return String.format("%d %s", configuration.getFitnessFunctionTimeoutDuration(),
        configuration.getFitnessFunctionTimeoutUnit().name());
  }

  @Command
  public void fitnessFunctionTimeout(@Param(name="seconds") long seconds) {
    configuration.setFitnessFunctionTimeoutDuration(seconds);
    configuration.setFitnessFunctionTimeoutUnit(TimeUnit.SECONDS);
  }

  @Command
  public String start() {
    try {

      job.start(configuration);
      return "Job started.";

    } catch (JobCurrentlyRunningException e) {
      return "Job already running since " + formatSeconds(e.getRunDurationInMillis());
    }
  }

  @Command
  public String cancel() {
    try {

      job.cancel();
      return "Job cancelled after " + formatSeconds(job.getJobRunDurationInMilliseconds());

    } catch (NoJobActiveException e) {
      return "No job started yet.";
    }
  }

  @Command
  public String result() {
    try {

      return lines( //
          "Job took " + formatSeconds(job.getJobRunDurationInMilliseconds()), //
          brainfuckGenerationDescriber.describe(job.getLastResult()) //
      );

    } catch (NoJobActiveException e) {
      return "No job started yet.";

    } catch (JobCurrentlyRunningException e) {
      return "Job still running since " + formatSeconds(e.getRunDurationInMillis());

    } catch (JobExecutionException e) {
      return lines("ERROR: " + e.getMessage(), Throwables.getStackTraceAsString(e.getCause()));
    }
  }

  @Command
  public String errors() {
    try {

      return brainfuckGenerationDescriber
          .describeThrowables((GenerationWithConcurrentFitnessFunction<?>) job.getLastResult());

    } catch (NoJobActiveException e) {
      return "No job started yet.";

    } catch (JobCurrentlyRunningException e) {
      return "Job still running since " + formatSeconds(e.getRunDurationInMillis());

    } catch (JobExecutionException e) {
      return lines("ERROR: " + e.getMessage(), Throwables.getStackTraceAsString(e.getCause()));
    }
  }

  private String formatSeconds(long millis) {
    return String.format("%.3f s", millis / 1000.0);
  }

  private String lines(String ... lines) {
    return Joiner.on(System.lineSeparator()).join(lines);
  }

  @Override
  public void cliEnterLoop() {
    // nothing to do here
  }

  @Override
  public void cliLeaveLoop() {

    configuration.getGeneticAlgorithmExecutorService().shutdownNow();
    configuration.getFitnessFunctionExecutorService().shutdownNow();
    configuration.getWatchdogExecutorService().shutdownNow();

  }
}
