package com.google.wireless.gdata2;

import com.google.wireless.gdata2.data.Entry;

public class ConflictDetectedException extends GDataException
{
  private final Entry conflictingEntry;

  public ConflictDetectedException(Entry paramEntry)
  {
    this.conflictingEntry = paramEntry;
  }

  public Entry getConflictingEntry()
  {
    return this.conflictingEntry;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.wireless.gdata2.ConflictDetectedException
 * JD-Core Version:    0.6.2
 */