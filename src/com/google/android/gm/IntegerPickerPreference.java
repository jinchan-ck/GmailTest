package com.google.android.gm;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class IntegerPickerPreference extends DialogPreference
{
  private LabelsSynchronizationSettings mCaller;
  private int mConversationAgeDays;
  private TextView mNumberPickerDaysView;
  private NumberPicker mNumberPickerView;

  public IntegerPickerPreference(LabelsSynchronizationSettings paramLabelsSynchronizationSettings, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramLabelsSynchronizationSettings, paramAttributeSet);
    this.mCaller = paramLabelsSynchronizationSettings;
    this.mConversationAgeDays = paramInt;
    Resources localResources = paramLabelsSynchronizationSettings.getResources();
    setDialogLayoutResource(2130903055);
    setTitle(2131296417);
    String str = localResources.getQuantityText(2131427346, this.mConversationAgeDays).toString();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(this.mConversationAgeDays);
    setSummary(String.format(str, arrayOfObject));
  }

  private void updateDays()
  {
    this.mNumberPickerDaysView.setText(Utils.formatPlural(this.mCaller, 2131427347, this.mNumberPickerView.getCurrent()));
  }

  protected void onBindDialogView(View paramView)
  {
    super.onBindDialogView(paramView);
    this.mNumberPickerDaysView = ((TextView)paramView.findViewById(2131361847));
    this.mNumberPickerView = ((NumberPicker)paramView.findViewById(2131361846));
    this.mNumberPickerView.setRange(1, 999);
    this.mNumberPickerView.setCurrent(this.mConversationAgeDays);
    updateDays();
    this.mNumberPickerView.setOnChangeListener(new NumberPicker.OnChangedListener()
    {
      public void onChanged(NumberPicker paramAnonymousNumberPicker, int paramAnonymousInt1, int paramAnonymousInt2)
      {
        IntegerPickerPreference.this.updateDays();
      }
    });
  }

  protected void onDialogClosed(boolean paramBoolean)
  {
    if (paramBoolean)
    {
      this.mNumberPickerView.clearFocus();
      this.mCaller.onConversationAgeDaysChanged(this.mNumberPickerView.getCurrent());
    }
  }

  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder)
  {
    super.onPrepareDialogBuilder(paramBuilder);
    paramBuilder.setTitle(getContext().getString(2131296481)).setCancelable(true);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.IntegerPickerPreference
 * JD-Core Version:    0.6.2
 */