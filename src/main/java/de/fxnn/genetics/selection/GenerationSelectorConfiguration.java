package de.fxnn.genetics.selection;

public interface GenerationSelectorConfiguration {

  long getRandomSeed();

  /** Share of whole generation size to be selected for next iteration. */
  double getSelectionRatio();

}
