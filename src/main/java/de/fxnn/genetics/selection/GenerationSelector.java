package de.fxnn.genetics.selection;

import de.fxnn.genetics.generation.Generation;

public interface GenerationSelector {

  <S> void selectSolutions(Generation<S> generation, GenerationSelectorConfiguration generationSelectorConfiguration);

}
