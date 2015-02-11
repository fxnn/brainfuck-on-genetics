package de.fxnn.brainfuckongenetics.fitness;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class LinearComputationFitnessFunctionTest {

  public static final double DELTA = 0.001;

  @Test
  public void testFitnessOfEmptyInput() {

    assertFitnessIs(0.0, emptyInput());

  }

  @Test
  public void testFitnessOfLinearInputWithCoefficientOneWithFixedLength() {

    double fitnessWithCoefficientOneAndLength10 = computeFitness(input(1, 2, 3, 4, 5, 6, 7, 8, 9, 10));

    assertFitnessIs(fitnessWithCoefficientOneAndLength10, input(91, 92, 93, 94, 95, 96, 97, 98, 99, 100));
    assertFitnessIs(fitnessWithCoefficientOneAndLength10, input(-10, -9, -8, -7, -6, -5, -4, -3, -2, -1));

  }

  @Test
  public void testFitnessOfLongerInputIsHigher() {

    double fitnessWithLengthNine = computeFitness(input(1, 2, 3, 4, 5, 6, 7, 8, 9));

    assertFitnessIsSmallerThan(fitnessWithLengthNine, input(1, 2, 3, 4));
    assertFitnessIsSmallerThan(fitnessWithLengthNine, input(1, 2));
    assertFitnessIsSmallerThan(fitnessWithLengthNine, input(1));
    assertFitnessIsSmallerThan(fitnessWithLengthNine, input());

  }

  @Test
  public void testFitnessOfLinearInputWithGreaterCoefficientIsSmaller() {

    double fitnessWithCoefficientOne = computeFitness(input(1, 2, 3, 4));

    assertFitnessIsSmallerThan(fitnessWithCoefficientOne, input(1, 3, 5, 7, 9, 11));
    assertFitnessIsSmallerThan(fitnessWithCoefficientOne, input(-100, -98, -96, -94));
    assertFitnessIsSmallerThan(fitnessWithCoefficientOne, input(1, 11, 111, 1111, 11111));

  }

  @Test
  public void testFitnessOfLinearInputWithSmallerCoefficientIsSmaller() {

    double fitnessWithCoefficientOne = computeFitness(input(1, 2, 3, 4));

    assertFitnessIsSmallerThan(fitnessWithCoefficientOne, input(99, 98, 97, 96, 95, 94));
    assertFitnessIsSmallerThan(fitnessWithCoefficientOne, input(9999, 999, 99, 9));
    assertFitnessIsSmallerThan(fitnessWithCoefficientOne, input(-10, -11, -12, -13));

  }

  @Test
  public void testFitnessOfLinearInputIsGreaterThanFitnessOfExponentialInput() {

    double fitnessOfLinearInput = computeFitness(input(1, 3, 5, 7, 9));

    assertFitnessIsSmallerThan(fitnessOfLinearInput, input(1, 2, 3, 5, 8, 12, 18, 27));
    assertFitnessIsSmallerThan(fitnessOfLinearInput, input(1, 2, 4, 8, 16, 32, 64));

  }

  @Test
  public void testFitnessOfLinearInputIsGreaterThanFitnessOfConstantInput() {

    double fitnessOfLinearInput = computeFitness(input(1, 2, 3, 4, 5));

    assertFitnessIsSmallerThan(fitnessOfLinearInput, input(0, 0, 0, 0, 0));
    assertFitnessIsSmallerThan(fitnessOfLinearInput, input(9999, 9999, 9999, 9999));

  }

  private void assertFitnessIsSmallerThan(double referenceFitness, DataInput dataInput) {
    Assert.assertThat(computeFitness(dataInput), Matchers.lessThan(referenceFitness));
  }

  private void assertFitnessIs(double expectedFitness, DataInput dataInput) {
    Assert.assertEquals(expectedFitness, computeFitness(dataInput), DELTA);
  }

  private double computeFitness(DataInput dataInput) {
    return new LinearComputationFitnessFunction().computeFitness(dataInput);
  }

  private static DataInput emptyInput() {
    return input();
  }

  private static DataInput input(int ... ints) {
    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
    for (int i : ints) {
      byteArrayDataOutput.writeInt(i);
    }

    return new DataInputStream(new ByteArrayInputStream(byteArrayDataOutput.toByteArray()));
  }

}