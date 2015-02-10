package de.fxnn.genetics.selection;

import de.fxnn.genetics.generation.Generation;

public interface GenerationSelector<Solution> {

  void selectSolutions(Generation<Solution> generation);

}
