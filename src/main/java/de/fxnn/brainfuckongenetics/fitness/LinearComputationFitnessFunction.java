package de.fxnn.brainfuckongenetics.fitness;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;

import de.fxnn.genetics.fitness.FitnessFunction;

public class LinearComputationFitnessFunction implements FitnessFunction<DataInput> {

  public static final int EXPECTED_DIFFERENCE = 1;

  @Override
  public double computeFitness(DataInput dataInput) {
    double result = 0.0;

    Integer previous = null;
    boolean eofReached = false;
    while (!eofReached) {
      try {

        int current = dataInput.readInt();
        if (previous != null) {
          double normalization = (previous == 0) ? 1.0 : previous;
          result = result - Math.abs(((double) (current - previous - EXPECTED_DIFFERENCE)) / normalization);
        }

        previous = current;

      } catch (EOFException e) {
        eofReached = true;
      } catch (IOException e) {
        throw new RuntimeException("Could not compute fitness, as an i/o error occured when reading the solution", e);
      }
    }

    return result;
  }
}
