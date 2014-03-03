package com.google.common.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.METHOD})
@GwtCompatible
public @interface GwtCompatible
{
  public abstract boolean emulated();

  public abstract boolean serializable();
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.common.annotations.GwtCompatible
 * JD-Core Version:    0.6.2
 */