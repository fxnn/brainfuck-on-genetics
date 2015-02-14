package de.fxnn.brainfuck;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.program.TreeProgram;
import de.fxnn.util.ListElement;

public class ProgramCopier {

  public static final ProgramCopier INSTANCE = new ProgramCopier();

  public TreeProgram copyProgram(TreeProgram treeProgram) {

    List<Program> childPrograms = new ArrayList<>(treeProgram.getChildPrograms().size());

    for (Program program : treeProgram.getChildPrograms()) {
      Optional<Program> newProgram;

      if (program instanceof TreeProgram) {
        newProgram = copyTreeProgram(ListElement.lastOrNewElement(childPrograms), (TreeProgram) program);
      } else if (program instanceof StringProgram) {
        newProgram = copyStringProgram(ListElement.lastOrNewElement(childPrograms),
            (StringProgram) program);
      } else {
        throw new IllegalArgumentException("Cannot copy program " + program);
      }

      if (newProgram.isPresent()) {
        childPrograms.add(newProgram.get());
      }
    }

    return new TreeProgram(childPrograms);

  }

  protected Optional<Program> copyTreeProgram(ListElement<Program> previousSibling, TreeProgram sourceProgram) {
    if (!sourceProgram.getChildPrograms().isEmpty()) {
      return Optional.of(copyProgram(sourceProgram));
    }

    return Optional.empty();
  }

  protected Optional<Program> copyStringProgram(ListElement<Program> previousSibling, StringProgram sourceProgram) {
    if (!sourceProgram.getProgram().isEmpty()) {
      if (!isStringProgram(previousSibling)) {
        return Optional.of(copyProgram(sourceProgram));
      }

      previousSibling.set(union(((StringProgram) previousSibling.get()), sourceProgram));
    }

    return Optional.empty();
  }

  protected StringProgram union(StringProgram program1, StringProgram program2) {
    return new StringProgram(program1.getProgram() + program2.getProgram());
  }

  protected boolean isStringProgram(ListElement<Program> programInList) {
    if (programInList.isExisting()) {
      return programInList.get() instanceof StringProgram;
    }

    return false;
  }

  public StringProgram copyProgram(StringProgram stringProgram) {
    return new StringProgram(stringProgram.getProgram());
  }

}
