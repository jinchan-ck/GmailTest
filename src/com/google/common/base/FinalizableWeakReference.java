package com.google.common.base;

import java.lang.ref.WeakReference;

public abstract class FinalizableWeakReference<T> extends WeakReference<T>
  implements FinalizableReference
{
  protected FinalizableWeakReference(T paramT, FinalizableReferenceQueue paramFinalizableReferenceQueue)
  {
    super(paramT, paramFinalizableReferenceQueue.queue);
    paramFinalizableReferenceQueue.cleanUp();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.base.FinalizableWeakReference
 * JD-Core Version:    0.6.2
 */