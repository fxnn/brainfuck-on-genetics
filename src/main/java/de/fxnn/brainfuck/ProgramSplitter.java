package de.fxnn.brainfuck;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.program.TreeProgram;
import de.fxnn.util.ListElement;

/**
 * Allows inserting arbitrary programs at a specified index
 */
public class ProgramSplitter {

  private int stringProgramCountdown;

  private Iterator<Program> iterator;

  public ListElement<Program> split(TreeProgram treeProgram, int splitAtStringProgramIndex) {

    this.stringProgramCountdown = splitAtStringProgramIndex;
    this.iterator = treeProgram.iterator();

    return visitTreeProgram(treeProgram.getChildPrograms(), 0).get();

  }

  protected Optional<ListElement<Program>> visitTreeProgram(List<Program> currentChildProgramList,
      int currentChildProgramListIndex) {
    if (currentChildProgramListIndex == currentChildProgramList.size()) {
      if (stringProgramCountdown == 0) {
        return Optional.of(ListElement.appendNewElement(currentChildProgramList));
      }

      return Optional.empty(); // nothing in here
    }

    Program next = iterator.next();

    if (next instanceof StringProgram) {
      int stringProgramLength = ((StringProgram) next).getProgram().length();
      if (stringProgramLength >= stringProgramCountdown) {
        StringProgram firstHalf = new StringProgram(
            ((StringProgram) next).getProgram().substring(0, stringProgramCountdown));
        StringProgram secondHalf = new StringProgram(
            ((StringProgram) next).getProgram().substring(stringProgramCountdown, stringProgramLength));

        currentChildProgramList.set(currentChildProgramListIndex, firstHalf);
        currentChildProgramList.add(currentChildProgramListIndex + 1, secondHalf);

        return Optional.of(ListElement.insertElementAt(currentChildProgramList, currentChildProgramListIndex + 1));
      }

      stringProgramCountdown -= stringProgramLength;
      return visitTreeProgram(currentChildProgramList, currentChildProgramListIndex + 1);
    }

    if (next instanceof TreeProgram) {
      List<Program> newChildPrograms = ((TreeProgram) next).getChildPrograms();

      // NOTE, that we're depth first!
      Optional<ListElement<Program>> result = visitTreeProgram(newChildPrograms, 0);
      if (result.isPresent()) {
        return result;
      }

      return visitTreeProgram(currentChildProgramList, currentChildProgramListIndex + 1);
    }

    throw new IllegalArgumentException("Unsupported program: " + next);
  }

}
