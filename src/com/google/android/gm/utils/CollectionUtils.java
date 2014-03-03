package com.google.android.gm.utils;

import java.text.Collator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;

public class CollectionUtils
{
  public static <E> ArrayList<E> newArrayList()
  {
    return new ArrayList();
  }

  public static <K, V> HashMap<K, V> newHashMap()
  {
    return new HashMap();
  }

  public static <K> HashSet<K> newHashSet()
  {
    return new HashSet();
  }

  public static <K> SortedSet<K> newSortedSet()
  {
    return new TreeSet(Collator.getInstance());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.utils.CollectionUtils
 * JD-Core Version:    0.6.2
 */