package com.google.android.gm.preference;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.res.Resources;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import com.google.android.gm.Utils;

public class IntegerPickerPreference extends DialogPreference
{
  private Callbacks mCallbacks;
  private final Context mContext;
  private int mConversationAgeDays;
  private TextView mNumberPickerDaysView;
  private NumberPicker mNumberPickerView;

  public IntegerPickerPreference(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mContext = paramContext;
  }

  private void updateDays()
  {
    this.mNumberPickerDaysView.setText(Utils.formatPlural(this.mContext, 2131755041, this.mNumberPickerView.getValue()));
  }

  public void bind(Callbacks paramCallbacks, int paramInt)
  {
    this.mCallbacks = paramCallbacks;
    this.mConversationAgeDays = paramInt;
    Resources localResources = this.mContext.getResources();
    setDialogLayoutResource(2130968627);
    setTitle(2131427751);
    String str = localResources.getQuantityText(2131755040, this.mConversationAgeDays).toString();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(this.mConversationAgeDays);
    setSummary(String.format(str, arrayOfObject));
  }

  protected void onBindDialogView(View paramView)
  {
    super.onBindDialogView(paramView);
    this.mNumberPickerDaysView = ((TextView)paramView.findViewById(2131689642));
    this.mNumberPickerView = ((NumberPicker)paramView.findViewById(2131689641));
    this.mNumberPickerView.setMinValue(1);
    this.mNumberPickerView.setMaxValue(999);
    this.mNumberPickerView.setValue(this.mConversationAgeDays);
    updateDays();
    this.mNumberPickerView.setOnValueChangedListener(new NumberPicker.OnValueChangeListener()
    {
      public void onValueChange(NumberPicker paramAnonymousNumberPicker, int paramAnonymousInt1, int paramAnonymousInt2)
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
      this.mCallbacks.onNumberChanged(this.mNumberPickerView.getValue());
    }
  }

  protected void onPrepareDialogBuilder(AlertDialog.Builder paramBuilder)
  {
    super.onPrepareDialogBuilder(paramBuilder);
    paramBuilder.setTitle(getContext().getString(2131427751)).setCancelable(true);
  }

  public static abstract interface Callbacks
  {
    public abstract void onNumberChanged(int paramInt);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.IntegerPickerPreference
 * JD-Core Version:    0.6.2
 */