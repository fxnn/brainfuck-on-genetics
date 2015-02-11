package de.fxnn.genetics;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.DoubleStream;

import de.fxnn.genetics.breeding.GenerationBreeder;
import de.fxnn.genetics.generation.Generation;
import de.fxnn.genetics.initialization.GenerationFactory;
import de.fxnn.genetics.selection.GenerationSelector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class GeneticAlgorithmTest {

  @InjectMocks
  GeneticAlgorithm<Boolean> sut;

  @Mock
  GeneticAlgorithmConfiguration<Boolean> geneticAlgorithmConfiguration;

  @Mock
  GenerationFactory<Boolean> generationFactory;

  @Mock
  GenerationSelector generationSelector;

  @Mock
  GenerationBreeder<Boolean> generationBreeder;

  @Mock
  TerminationCondition<Boolean> terminationCondition;

  private Generation<Boolean> result;

  @Before
  public void setUpDefaultBehaviour() {

    Mockito.when(generationFactory.initializeGeneration(geneticAlgorithmConfiguration)).thenReturn(createEmptyGeneration());

  }

  @Test
  public void testReturnInitialConditionWhenTerminationConditionReachedInitially() {

    Mockito.when(terminationCondition.isTerminationConditionReached(anyGeneration())).thenReturn(true);

    whenSutInvoked();

    Mockito.verify(terminationCondition).isTerminationConditionReached(anyGeneration());
    Mockito.verifyZeroInteractions(generationSelector, generationBreeder);
    Assert.assertNotNull(result);

  }

  @Test
  public void testSingleLoopExecution() {

    Generation<Boolean> firstGeneration = createGeneration(false);
    Generation<Boolean> secondGeneration = createGeneration(true);

    Mockito.when(generationFactory.initializeGeneration(geneticAlgorithmConfiguration)).thenReturn(firstGeneration);
    Mockito.when(generationBreeder.breedNewGeneration(firstGeneration)).thenReturn(secondGeneration);

    Mockito.when(terminationCondition.isTerminationConditionReached(firstGeneration)).thenReturn(false);
    Mockito.when(terminationCondition.isTerminationConditionReached(secondGeneration)).thenReturn(true);

    whenSutInvoked();

    Mockito.verify(generationFactory).initializeGeneration(geneticAlgorithmConfiguration);
    Mockito.verify(terminationCondition, Mockito.times(2)).isTerminationConditionReached(anyGeneration());
    Mockito.verify(generationSelector).selectSolutions(firstGeneration, geneticAlgorithmConfiguration);
    Mockito.verify(generationBreeder).breedNewGeneration(firstGeneration);

    Assert.assertEquals(secondGeneration, result);

  }

  protected void whenSutInvoked() {
    result = sut.call();
  }

  private static Generation<Boolean> anyGeneration() {
    return Mockito.any(Generation.class);
  }

  protected static Generation<Boolean> createEmptyGeneration() {
    return createGeneration();
  }

  protected static Generation<Boolean> createGeneration(Boolean ... solutions) {
    return new Generation<Boolean>() {

      private Boolean selectedSolution;

      @Override
      public Collection<Boolean> getSolutions() {
        return Arrays.asList(solutions);
      }

      @Override
      public Collection<Boolean> getSelectedSolutions() {
        if (selectedSolution == null) {
          return Collections.emptySet();
        }

        return Collections.singleton(selectedSolution);
      }

      @Override
      public double getFitness(Boolean aBoolean) {
        if (aBoolean == Boolean.TRUE) {
          return 1.0;
        }

        return 0.0;
      }

      @Override
      public DoubleStream streamFitnessValues() {
        throw new UnsupportedOperationException();
      }

      @Override
      public void addSolution(Boolean aBoolean) {
        throw new UnsupportedOperationException();
      }

      @Override
      public void selectSolution(Boolean aBoolean) {
        this.selectedSolution = aBoolean;
      }
    };
  }

}