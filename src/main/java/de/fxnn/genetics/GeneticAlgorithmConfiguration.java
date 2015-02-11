package de.fxnn.genetics;

import de.fxnn.genetics.initialization.GenerationFactoryConfiguration;
import de.fxnn.genetics.selection.GenerationSelectorConfiguration;

public interface GeneticAlgorithmConfiguration<Solution>
    extends GenerationFactoryConfiguration<Solution>, GenerationSelectorConfiguration
{

}
