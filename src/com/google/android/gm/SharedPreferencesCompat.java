package com.google.android.gm;

import android.content.SharedPreferences.Editor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class SharedPreferencesCompat
{
  private static final Method sApplyMethod = findApplyMethod();

  public static void apply(SharedPreferences.Editor paramEditor)
  {
    if (sApplyMethod != null);
    try
    {
      sApplyMethod.invoke(paramEditor, new Object[0]);
      return;
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      paramEditor.commit();
      return;
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      label20: break label20;
    }
  }

  private static Method findApplyMethod()
  {
    try
    {
      Method localMethod = SharedPreferences.Editor.class.getMethod("apply", new Class[0]);
      return localMethod;
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
    }
    return null;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.SharedPreferencesCompat
 * JD-Core Version:    0.6.2
 */