package de.fxnn.brainfuckongenetics;

import java.util.Arrays;

import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.StringProgram;
import de.fxnn.brainfuck.program.TreeProgram;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

public class ProgramCopierTest {

  ProgramCopier sut = ProgramCopier.INSTANCE;

  @Test
  public void testCopy() {

    TreeProgram original = treeProgram(stringProgram("+"), treeProgram(stringProgram("<"), treeProgram(stringProgram(".")), stringProgram(">")), stringProgram("-"));

    TreeProgram copy = sut.copyProgram(original);

    Assert.assertEquals(original, copy);

  }

  @Test
  public void testSimplification() {

    TreeProgram original = treeProgram(stringProgram("+"), stringProgram("+"), treeProgram(), treeProgram(stringProgram("-")), stringProgram("<"));

    TreeProgram copy = sut.copyProgram(original);

    Assert.assertThat(copy.getChildPrograms(), Matchers.contains(stringProgram("++"), treeProgram(stringProgram("-")), stringProgram("<")));

  }

  protected static StringProgram stringProgram(String program) {
    return new StringProgram(program);
  }

  protected static TreeProgram treeProgram(Program... childPrograms) {
    return new TreeProgram(Arrays.asList(childPrograms));
  }

}