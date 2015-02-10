package de.fxnn.brainfuckongenetics.operators.mutation;

import java.util.Random;

import de.fxnn.brainfuck.program.TreeProgram;

public interface BrainfuckMutationOperator {

  void mutate(TreeProgram program, Random random);

}
