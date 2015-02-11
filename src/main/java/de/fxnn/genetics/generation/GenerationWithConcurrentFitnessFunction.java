package de.fxnn.genetics.generation;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.stream.DoubleStream;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import de.fxnn.genetics.fitness.ConcurrentFitnessFunction;
import lombok.Getter;

public class GenerationWithConcurrentFitnessFunction<Solution> implements Generation<Solution> {

  private final Set<Solution> selectedSolutions;

  private final Map<Solution, Future<Double>> fitnessPerSolution;

  private final ConcurrentFitnessFunction<Solution> fitnessFunction;

  @Getter
  private final Multimap<Class<? extends Throwable>, Throwable> throwables;

  public GenerationWithConcurrentFitnessFunction(int maxPopulationSize, ConcurrentFitnessFunction<Solution> fitnessFunction) {
    this.fitnessPerSolution = Maps.newHashMapWithExpectedSize(maxPopulationSize);
    this.selectedSolutions = Sets.newHashSetWithExpectedSize(maxPopulationSize);
    this.fitnessFunction = fitnessFunction;
    this.throwables = HashMultimap.create();
  }

  @Override
  public Collection<Solution> getSolutions() {
    return fitnessPerSolution.keySet();
  }

  @Override
  public Collection<Solution> getSelectedSolutions() {
    return selectedSolutions;
  }

  @Override
  public DoubleStream streamFitnessValues() {
    return fitnessPerSolution.keySet().stream().mapToDouble(this::getFitness);
  }

  @Override
  public double getFitness(Solution solution) {
    try {
      return fitnessPerSolution.get(solution).get();
    } catch (Exception ex) {
      throwables.put(ex.getClass(), ex);
      return Double.NEGATIVE_INFINITY;
    }
  }

  @Override
  public void addSolution(Solution solution) {
    fitnessPerSolution.put(solution, fitnessFunction.computeFitness(solution));
  }

  @Override
  public void selectSolution(Solution solution) {
    selectedSolutions.add(solution);
  }
}
