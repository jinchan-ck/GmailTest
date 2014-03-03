package com.google.android.gm;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.preference.ListPreference;
import com.google.android.gm.provider.Gmail.LabelMap;
import java.util.Set;

public class LabelPreference extends ListPreference
{
  private static final String SYNC_ALL = String.valueOf(2131296437);
  private static final String SYNC_NONE = String.valueOf(2131296439);
  private static final String SYNC_RECENT = String.valueOf(2131296438);
  private LabelsSynchronizationSettings mCaller;
  private int mConversationAgeDays;
  private CharSequence mHumanLabelName;
  private Drawable mStarDrawable;
  private CharSequence mSyncSet;
  private CharSequence mSystemLabelName;

  public LabelPreference(LabelsSynchronizationSettings paramLabelsSynchronizationSettings, int paramInt1, CharSequence paramCharSequence1, CharSequence paramCharSequence2, CharSequence paramCharSequence3, int paramInt2)
  {
    super(paramLabelsSynchronizationSettings, null);
    this.mCaller = paramLabelsSynchronizationSettings;
    this.mConversationAgeDays = paramInt1;
    this.mHumanLabelName = paramCharSequence1;
    this.mSystemLabelName = paramCharSequence2;
    this.mSyncSet = paramCharSequence3;
    this.mStarDrawable = paramLabelsSynchronizationSettings.getResources().getDrawable(17301516);
    setTitle(this.mHumanLabelName);
    setSummary(this.mSyncSet);
    setDialogTitle(this.mHumanLabelName);
    if (Gmail.LabelMap.getForcedIncludedOrPartialLabels().contains(paramCharSequence2))
    {
      CharSequence[] arrayOfCharSequence3 = new CharSequence[2];
      Object[] arrayOfObject2 = new Object[1];
      arrayOfObject2[0] = Integer.valueOf(this.mConversationAgeDays);
      arrayOfCharSequence3[0] = paramLabelsSynchronizationSettings.getString(2131296438, arrayOfObject2);
      arrayOfCharSequence3[1] = paramLabelsSynchronizationSettings.getString(2131296437);
      setEntries(arrayOfCharSequence3);
      CharSequence[] arrayOfCharSequence4 = new CharSequence[2];
      arrayOfCharSequence4[0] = SYNC_RECENT;
      arrayOfCharSequence4[1] = SYNC_ALL;
      setEntryValues(arrayOfCharSequence4);
    }
    while (true)
      switch (paramInt2)
      {
      default:
        return;
        CharSequence[] arrayOfCharSequence1 = new CharSequence[3];
        arrayOfCharSequence1[0] = paramLabelsSynchronizationSettings.getString(2131296439);
        Object[] arrayOfObject1 = new Object[1];
        arrayOfObject1[0] = Integer.valueOf(this.mConversationAgeDays);
        arrayOfCharSequence1[1] = paramLabelsSynchronizationSettings.getString(2131296438, arrayOfObject1);
        arrayOfCharSequence1[2] = paramLabelsSynchronizationSettings.getString(2131296437);
        setEntries(arrayOfCharSequence1);
        CharSequence[] arrayOfCharSequence2 = new CharSequence[3];
        arrayOfCharSequence2[0] = SYNC_NONE;
        arrayOfCharSequence2[1] = SYNC_RECENT;
        arrayOfCharSequence2[2] = SYNC_ALL;
        setEntryValues(arrayOfCharSequence2);
      case 2131361960:
      case 2131361959:
      case 2131361958:
      }
    setValue(SYNC_NONE);
    return;
    setValue(SYNC_RECENT);
    return;
    setValue(SYNC_ALL);
  }

  protected void onDialogClosed(boolean paramBoolean)
  {
    super.onDialogClosed(paramBoolean);
    if (paramBoolean)
      this.mCaller.onSyncSettingChanged(this.mSystemLabelName, Integer.parseInt(getValue()));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.LabelPreference
 * JD-Core Version:    0.6.2
 */