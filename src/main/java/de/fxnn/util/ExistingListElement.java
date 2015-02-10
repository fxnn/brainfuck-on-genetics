package de.fxnn.util;

import java.util.List;

import lombok.Value;

@Value
public class ExistingListElement<T> implements ListElement<T> {

  private final List<T> backingList;

  private final int listIndex;

  @Override
  public T get() {
    return backingList.get(listIndex);
  }

  @Override
  public void set(T value) {
    backingList.set(listIndex, value);
  }

  @Override
  public boolean isExisting() {
    return true;
  }

}
