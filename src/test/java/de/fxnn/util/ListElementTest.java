package de.fxnn.util;

import java.util.List;

import com.google.common.collect.Lists;
import org.junit.Test;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class ListElementTest {

  @Test
  public void testGetter() {

    List<Integer> givenList = Lists.newArrayList(1, 2, 3);

    assertThat(ListElement.firstElement(givenList).get(), is(1));
    assertThat(ListElement.elementAt(givenList, 1).get(), is(2));
    assertThat(ListElement.lastElement(givenList).get(), is(3));

  }

  @Test
  public void testSetFirstElement() {

    List<Integer> givenList = Lists.newArrayList(1, 2, 3);

    ListElement.firstElement(givenList).set(42);
    assertThat(givenList.get(0), is(42));

  }

  @Test
  public void testSetElementByIndex() {

    List<Integer> givenList = Lists.newArrayList(1, 2, 3);

    ListElement.elementAt(givenList, 1).set(42);
    assertThat(givenList.get(1), is(42));

  }

  @Test
  public void testSetLastElement() {

    List<Integer> givenList = Lists.newArrayList(1, 2, 3);

    ListElement.lastElement(givenList).set(42);
    assertThat(givenList.get(2), is(42));

  }

  @Test
  public void testAppendElement() {

    List<Integer> givenList = Lists.newArrayList(1, 2, 3);

    ListElement.appendNewElement(givenList).set(42);
    assertThat(givenList, hasSize(4));
    assertThat(givenList.get(3), is(42));

  }

  @Test
  public void testEquals() {

    List<Integer> oneList = Lists.newArrayList(1, 2, 3);
    List<Integer> sameList = Lists.newArrayList(1, 2, 3);
    List<Integer> differentList = Lists.newArrayList(42);

    ListElement<Integer> firstElement = ListElement.firstElement(oneList);
    ListElement<Integer> lastElement = ListElement.lastElement(oneList);
    ListElement<Integer> newElement = ListElement.appendNewElement(oneList);
    ListElement<Integer> sameListsFirstElement = ListElement.firstElement(sameList);
    ListElement<Integer> differentListsFirstElement = ListElement.firstElement(differentList);

    assertThat(firstElement, not(equalTo(lastElement)));
    assertThat(firstElement, not(equalTo(newElement)));
    assertThat(firstElement, not(equalTo(differentListsFirstElement)));
    assertThat(firstElement, equalTo(firstElement));
    assertThat(firstElement, equalTo(sameListsFirstElement));

    assertThat(lastElement, not(equalTo(firstElement)));
    assertThat(lastElement, not(equalTo(newElement)));
    assertThat(lastElement, not(equalTo(differentListsFirstElement)));
    assertThat(lastElement, equalTo(lastElement));

    assertThat(newElement, equalTo(newElement));

  }

  @Test
  public void testHashCode() {

    List<Integer> oneList = Lists.newArrayList(1, 2, 3);
    List<Integer> sameList = Lists.newArrayList(1, 2, 3);
    List<Integer> differentList = Lists.newArrayList(42);

    ListElement<Integer> firstElement = ListElement.firstElement(oneList);
    ListElement<Integer> lastElement = ListElement.lastElement(oneList);
    ListElement<Integer> newElement = ListElement.appendNewElement(oneList);
    ListElement<Integer> sameListsFirstElement = ListElement.firstElement(sameList);
    ListElement<Integer> differentListsFirstElement = ListElement.firstElement(differentList);

    assertThat(firstElement.hashCode(), not(equalTo(lastElement.hashCode())));
    assertThat(firstElement.hashCode(), not(equalTo(newElement.hashCode())));
    assertThat(firstElement.hashCode(), not(equalTo(differentListsFirstElement.hashCode())));
    assertThat(firstElement.hashCode(), equalTo(firstElement.hashCode()));
    assertThat(firstElement.hashCode(), equalTo(sameListsFirstElement.hashCode()));

    assertThat(lastElement.hashCode(), not(equalTo(firstElement.hashCode())));
    assertThat(lastElement.hashCode(), not(equalTo(newElement.hashCode())));
    assertThat(lastElement.hashCode(), not(equalTo(differentListsFirstElement.hashCode())));
    assertThat(lastElement.hashCode(), equalTo(lastElement.hashCode()));

    assertThat(newElement.hashCode(), equalTo(newElement.hashCode()));

  }

}