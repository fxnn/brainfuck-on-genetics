package de.fxnn.brainfuckongenetics.fitness;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.google.common.io.ByteStreams;
import de.fxnn.brainfuck.ProgramBuilder;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.tape.InfiniteSignedIntegerTape;
import de.fxnn.brainfuckongenetics.ProgramOptimizer;
import de.fxnn.genetics.fitness.FitnessFunction;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BrainfuckProgramFitnessFunction implements FitnessFunction<Program> {

  private final ProgramOptimizer programOptimizer = new ProgramOptimizer();

  private final FitnessFunction<DataInput> byteArrayFitnessFunction;

  @Override
  public double computeFitness(Program program) {
    Program optimizedProgram = programOptimizer.optimizeProgram(program);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ProgramBuilder programBuilder = new ProgramBuilder();
    programBuilder.withInputReader(new BufferedReader(new InputStreamReader(new ByteArrayInputStream(new byte[0]))));
    programBuilder.withOutputWriter(new BufferedWriter(new OutputStreamWriter(outputStream)));
    programBuilder.withTape(new InfiniteSignedIntegerTape());
    programBuilder.withProgram(optimizedProgram);

    programBuilder.buildProgramExecutor().run();

    return byteArrayFitnessFunction.computeFitness(ByteStreams.newDataInput(outputStream.toByteArray()));
  }
}
