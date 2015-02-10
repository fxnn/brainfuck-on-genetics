package de.fxnn.genetics.breeding;

import de.fxnn.genetics.generation.Generation;

public interface GenerationBreeder<Solution> {

  Generation<Solution> breedNewGeneration(Generation<Solution> generation);

}
