package de.fxnn.brainfuckongenetics;

import java.util.SortedMap;
import java.util.TreeMap;

import com.google.common.base.Joiner;
import com.google.common.collect.Multimap;
import de.fxnn.brainfuck.ProgramOptimizer;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.genetics.generation.Generation;
import de.fxnn.genetics.generation.GenerationWithConcurrentFitnessFunction;

public class BrainfuckGenerationDescriber {

  private final ProgramOptimizer programOptimizer = new ProgramOptimizer();

  public String describe(Generation<Program> generation) {

    SortedMap<Double, String> outputs = new TreeMap<>();

    for (Program program : generation.getSolutions()) {
      double fitness = generation.getFitness(program);
      if (fitness > Double.NEGATIVE_INFINITY) {
        outputs.put(fitness,
            String.format("%1s [%10f] %s", describeSelected(generation, program), fitness, describeProgram(program)));
      }
    }

    return Joiner.on(System.lineSeparator()).join(outputs.values());

  }

  protected String describeSelected(Generation<Program> generation, Program program) {
    if (generation.getSelectedSolutions().contains(program)) {
      return "X";
    }

    return "";
  }

  protected String describeProgram(Program program) {
    return programOptimizer.optimizeProgram(program).getProgram();
  }

  public String describeThrowables(GenerationWithConcurrentFitnessFunction<?> generation) {

    StringBuilder resultBuilder = new StringBuilder();

    resultBuilder.append("Exceptions:").append(System.lineSeparator());

    Multimap<Class<? extends Throwable>, Throwable> throwables = generation.getThrowables();
    for (Class<? extends Throwable> throwableClass : throwables.keySet()) {
      resultBuilder.append(String.format("[%d] %s", throwables.get(throwableClass).size(), throwableClass.getName()));
      resultBuilder.append(System.lineSeparator());
    }

    return resultBuilder.toString();

  }

}
