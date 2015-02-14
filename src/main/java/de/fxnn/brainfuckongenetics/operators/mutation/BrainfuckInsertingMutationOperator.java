package de.fxnn.brainfuckongenetics.operators.mutation;

import java.util.Random;

import com.google.common.collect.Lists;
import de.fxnn.brainfuck.ProgramSplitter;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.program.TreeProgram;
import de.fxnn.brainfuck.program.TreePrograms;

public enum BrainfuckInsertingMutationOperator implements BrainfuckMutationOperator {

  ADD_VALUE_INSTRUCTION {
    @Override
    protected Program generateRandomProgram(Random random) {
      if (random.nextBoolean()) {
        return new StringProgram("+");
      }

      return new StringProgram("-");
    }
  },

  ADD_MOVEMENT_INSTRUCTION {
    @Override
    protected Program generateRandomProgram(Random random) {
      if (random.nextBoolean()) {
        return new StringProgram(">");
      }

      return new StringProgram("<");
    }
  },

  ADD_IO_INSTRUCTION {
    @Override
    protected Program generateRandomProgram(Random random) {
      if (random.nextBoolean()) {
        return new StringProgram(".");
      }

      return new StringProgram(",");
    }
  },

  ADD_LOOP {
    @Override
    protected Program generateRandomProgram(Random random) {
      return new TreeProgram(Lists.newArrayList(new StringProgram("["), new StringProgram("]")));
    }
  };

  @Override
  public void mutate(TreeProgram program, Random random) {
    int totalStringProgramLength = TreePrograms.getTotalStringProgramLength(program);
    int insertionIndex = totalStringProgramLength == 0 ? 0 : random.nextInt(totalStringProgramLength);

    Program programToInsert = generateRandomProgram(random);

    new ProgramSplitter().split(program, insertionIndex).set(programToInsert);
  }

  protected abstract Program generateRandomProgram(Random random);

}
