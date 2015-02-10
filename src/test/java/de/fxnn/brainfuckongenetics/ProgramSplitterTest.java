package de.fxnn.brainfuckongenetics;

import com.google.common.collect.Lists;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.program.TreeProgram;
import org.junit.Test;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class ProgramSplitterTest {

  @Test
  public void testSingleStringProgram_insertAtEmptyList() {

    TreeProgram givenProgram = treeProgram();

    new ProgramSplitter().split(givenProgram, 0).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains(stringProgram("X")));

  }

  @Test
  public void testSingleStringProgram_insertAtBeginning() {

    TreeProgram givenProgram = treeProgram(stringProgram("0123"));

    new ProgramSplitter().split(givenProgram, 0).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains(stringProgram(""), stringProgram("X"), stringProgram("0123")));

  }

  @Test
  public void testSingleStringProgram_insertAtEnd() {

    TreeProgram givenProgram = treeProgram(stringProgram("0123"));

    new ProgramSplitter().split(givenProgram, 4).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains(stringProgram("0123"), stringProgram("X"), stringProgram("")));

  }

  @Test
  public void testSingleStringProgram_insertInTheMiddle() {

    TreeProgram givenProgram = treeProgram(stringProgram("0123"));

    new ProgramSplitter().split(givenProgram, 2).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains(stringProgram("01"), stringProgram("X"), stringProgram("23")));

  }

  @Test
  public void testNestedStringPrograms_insertAtTheBeforeATreeProgram() {

    TreeProgram givenProgram = treeProgram( //
        treeProgram( //
            stringProgram("01"), //
            treeProgram(stringProgram("23")) //
        ), //
        stringProgram("45") //
    );

    new ProgramSplitter().split(givenProgram, 2).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains( //
        treeProgram( //
            stringProgram("01"), stringProgram("X"), stringProgram(""), //
            treeProgram(stringProgram("23")) //
        ), //
        stringProgram("45") //
    ));

  }

  @Test
  public void testNestedStringPrograms_insertInTheMiddleOfANestedStringProgram() {

    TreeProgram givenProgram = treeProgram( //
        treeProgram( //
            stringProgram("01"), //
            treeProgram(stringProgram("23")) //
        ), //
        stringProgram("45") //
    );

    new ProgramSplitter().split(givenProgram, 3).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains( //
        treeProgram( //
            stringProgram("01"), //
            treeProgram(stringProgram("2"), stringProgram("X"), stringProgram("3")) //
        ), //
        stringProgram("45") //
    ));

  }

  @Test
  public void testNestedStringPrograms_insertAtTheEndOfATreeProgram() {

    TreeProgram givenProgram = treeProgram( //
        treeProgram( //
            stringProgram("01"), //
            treeProgram(stringProgram("23")) //
        ), //
        stringProgram("45") //
    );

    new ProgramSplitter().split(givenProgram, 4).set(stringProgram("X"));

    assertThat(givenProgram.getChildPrograms(), contains( //
        treeProgram( //
            stringProgram("01"), //
            treeProgram(stringProgram("23"), stringProgram("X"), stringProgram("")) //
        ), //
        stringProgram("45") //
    ));

  }

  protected static StringProgram stringProgram(String program) {
    return new StringProgram(program);
  }

  protected static TreeProgram treeProgram(Program... childPrograms) {
    return new TreeProgram(Lists.newArrayList(childPrograms));
  }

}