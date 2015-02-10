package de.fxnn.util;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * A view on a special element of a list. Changes that list, gets changed by that list.
 */
public interface ListElement<T> {

  T get();

  void set(T value);

  boolean isExisting();

  public static <T> ListElement<T> lastOrNewElement(List<T> backingList) {
    if (backingList == null) {
      throw new IllegalArgumentException("Cannot append new element to NULL list");
    }

    if (backingList.isEmpty()) {
      return appendNewElement(backingList);
    }

    return lastElement(backingList);
  }

  public static <T> ListElement<T> appendNewElement(List<T> backingList) {
    if (backingList == null) {
      throw new IllegalArgumentException("Cannot append new element to NULL list");
    }
    return new NewListElement<>(backingList, backingList.size());
  }

  public static <T> ListElement<T> lastElement(List<T> backingList) {
    if (backingList == null || backingList.isEmpty()) {
      throw new NoSuchElementException("Cannot access this lists last element, as it's empty: " + backingList);
    }
    return new ExistingListElement<>(backingList, backingList.size() - 1);
  }

  public static <T> ListElement<T> firstElement(List<T> backingList) {
    if (backingList == null || backingList.isEmpty()) {
      throw new NoSuchElementException("Cannot access this lists first element, as it's empty: " + backingList);
    }
    return new ExistingListElement<>(backingList, 0);
  }

  public static <T> ListElement<T> elementAt(List<T> backingList, int listIndex) {
    if (backingList == null || listIndex < 0 || listIndex >= backingList.size()) {
      throw new NoSuchElementException("This list has no element with index " + listIndex + ": " + backingList);
    }
    return new ExistingListElement<>(backingList, listIndex);
  }

  public static <T> ListElement<T> insertElementAt(List<T> backingList, int listIndex) {
    if (backingList == null || listIndex < 0 || listIndex > backingList.size()) {
      throw new NoSuchElementException("Cannot insert list element with index " + listIndex + " in list " + backingList);
    }
    return new NewListElement<>(backingList, listIndex);
  }
}
