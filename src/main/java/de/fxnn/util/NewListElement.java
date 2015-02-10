package de.fxnn.util;

import java.util.List;
import java.util.NoSuchElementException;

import lombok.Data;

@Data
public class NewListElement<T> implements ListElement<T> {

  private final List<T> backingList;

  private final int listIndex;

  private ExistingListElement<T> insertedElement = null;

  @Override
  public T get() {
    if (insertedElement == null) {
      throw new NoSuchElementException("Element does not exist yet: " + toString());
    }

    return insertedElement.get();
  }

  @Override
  public void set(T value) {
    if (insertedElement != null) {
      insertedElement.set(value);
    } else {
      backingList.add(listIndex, value);
      insertedElement = new ExistingListElement<>(backingList, listIndex);
    }
  }

  @Override
  public boolean isExisting() {
    return insertedElement != null && insertedElement.isExisting();
  }
}
