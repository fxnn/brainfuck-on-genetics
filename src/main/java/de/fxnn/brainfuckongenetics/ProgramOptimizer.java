package de.fxnn.brainfuckongenetics;

import java.util.regex.Pattern;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.program.TreePrograms;

public class ProgramOptimizer {

  private static final Pattern ENDLESS_LOOP_PATTERN = Pattern.compile("\\[\\]");

  private static final Pattern INCREMENT_DECREMENT_PATTERN = Pattern.compile("\\+-");

  private static final Pattern DECREMENT_INCREMENT_PATTERN = Pattern.compile("-\\+");

  private static final Function<String, String> OPTIMIZATION_FUNCTION = Functions
      .compose(ProgramOptimizer::removeEndlessLoops,
          Functions.compose(ProgramOptimizer::removeDecrementsIncrement, ProgramOptimizer::removeIncrementDecrements));

  public StringProgram optimizeProgram(Program program) {

    String programSource = TreePrograms.toString(program);

    String optimizedProgramSource = OPTIMIZATION_FUNCTION.apply(programSource);

    return new StringProgram(optimizedProgramSource);

  }

  protected static String removeEndlessLoops(String input) {
    return ENDLESS_LOOP_PATTERN.matcher(input).replaceAll("");
  }

  protected static String removeIncrementDecrements(String input) {
    return INCREMENT_DECREMENT_PATTERN.matcher(input).replaceAll("");
  }

  protected static String removeDecrementsIncrement(String input) {
    return DECREMENT_INCREMENT_PATTERN.matcher(input).replaceAll("");
  }

}
