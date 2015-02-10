package de.fxnn.genetics;

import de.fxnn.genetics.generation.Generation;

public interface TerminationCondition<Solution> {

  boolean isTerminationConditionReached(Generation<Solution> generation);

}
