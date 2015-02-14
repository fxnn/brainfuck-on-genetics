package de.fxnn.brainfuckongenetics.fitness;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.fxnn.brainfuck.ProgramBuilder;
import de.fxnn.brainfuck.ProgramOptimizer;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.tape.InfiniteSignedIntegerTape;
import de.fxnn.brainfuck.tape.TapeEofBehaviour;
import de.fxnn.genetics.fitness.FitnessFunction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BrainfuckProgramFitnessFunction implements FitnessFunction<Program> {

  private final ProgramOptimizer programOptimizer = new ProgramOptimizer();

  private final FitnessFunction<DataInput> byteArrayFitnessFunction;

  @Override
  public double computeFitness(Program program) {
    Program optimizedProgram = programOptimizer.optimizeProgram(program);
    ByteArrayDataOutput output = ByteStreams.newDataOutput();

    ProgramBuilder programBuilder = new ProgramBuilder();
    programBuilder.withInput(dataInputFrom(new byte[0]));
    programBuilder.withOutput(output);
    programBuilder.withTape(new InfiniteSignedIntegerTape(TapeEofBehaviour.READS_ZERO));
    programBuilder.withProgram(optimizedProgram);

    programBuilder.buildProgramExecutor().run();

    return byteArrayFitnessFunction.computeFitness(dataInputFrom(output.toByteArray()));
  }

  private static DataInput dataInputFrom(byte[] bytes) {
    return new DataInputStream(new ByteArrayInputStream(bytes));
  }
}
