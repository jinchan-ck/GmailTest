package com.google.android.gm;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class MultiListPreference extends ListPreference
{
  private boolean[] mNewValues;
  private boolean[] mValues;

  public MultiListPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    int i = getEntries().length;
    this.mValues = new boolean[i];
    this.mNewValues = new boolean[i];
  }

  public String getValue()
  {
    CharSequence[] arrayOfCharSequence = getEntryValues();
    StringBuilder localStringBuilder = new StringBuilder();
    for (int i = 0; i < this.mNewValues.length; i++)
      if (this.mNewValues[i] != 0)
      {
        if (localStringBuilder.length() > 0)
          localStringBuilder.append(",");
        localStringBuilder.append(arrayOfCharSequence[i]);
      }
    if (localStringBuilder.length() > 0)
      return localStringBuilder.toString();
    return "none";
  }

  protected void onDialogClosed(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      String str = getValue();
      if (callChangeListener(str))
        setValue(str);
    }
    while (true)
    {
      return;
      for (int i = 0; i < this.mValues.length; i++)
        this.mNewValues[i] = this.mValues[i];
    }
  }

  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder)
  {
    CharSequence[] arrayOfCharSequence1 = getEntries();
    CharSequence[] arrayOfCharSequence2 = getEntryValues();
    if ((arrayOfCharSequence1 == null) || (arrayOfCharSequence2 == null))
      throw new IllegalStateException("ListPreference requires an entries array and an entryValues array.");
    paramBuilder.setMultiChoiceItems(arrayOfCharSequence1, this.mNewValues, new DialogInterface.OnMultiChoiceClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt, boolean paramAnonymousBoolean)
      {
        MultiListPreference.this.mNewValues[paramAnonymousInt] = paramAnonymousBoolean;
      }
    });
  }

  public void setValue(String paramString)
  {
    CharSequence[] arrayOfCharSequence = getEntryValues();
    for (int i = 0; i < this.mValues.length; i++)
    {
      boolean bool = paramString.contains(arrayOfCharSequence[i]);
      this.mValues[i] = bool;
      this.mNewValues[i] = bool;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.MultiListPreference
 * JD-Core Version:    0.6.2
 */