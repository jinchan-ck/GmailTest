package com.google.android.gm;

import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.NumberKeyListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Formatter;

public class NumberPicker extends LinearLayout
{
  private static final char[] DIGIT_CHARACTERS = { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
  public static final Formatter TWO_DIGIT_FORMATTER = new Formatter()
  {
    final Object[] mArgs = new Object[1];
    final StringBuilder mBuilder = new StringBuilder();
    final Formatter mFmt = new Formatter(this.mBuilder);

    public String toString(int paramAnonymousInt)
    {
      this.mArgs[0] = Integer.valueOf(paramAnonymousInt);
      this.mBuilder.delete(0, this.mBuilder.length());
      this.mFmt.format("%02d", this.mArgs);
      return this.mFmt.toString();
    }
  };
  private int mCurrent;
  private boolean mDecrement;
  private NumberPickerButton mDecrementButton;
  private String[] mDisplayedValues;
  private int mEnd;
  private Formatter mFormatter;
  private final Handler mHandler;
  private boolean mIncrement;
  private NumberPickerButton mIncrementButton;
  private OnChangedListener mListener;
  private final InputFilter mNumberInputFilter;
  private int mPrevious;
  private final Runnable mRunnable = new Runnable()
  {
    public void run()
    {
      if (NumberPicker.this.mIncrement)
      {
        NumberPicker.this.changeCurrent(1 + NumberPicker.this.mCurrent);
        NumberPicker.this.mHandler.postDelayed(this, NumberPicker.this.mSpeed);
      }
      while (!NumberPicker.this.mDecrement)
        return;
      NumberPicker.this.changeCurrent(NumberPicker.this.mCurrent - 1);
      NumberPicker.this.mHandler.postDelayed(this, NumberPicker.this.mSpeed);
    }
  };
  private long mSpeed = 300L;
  private int mStart;
  private final EditText mText;

  public NumberPicker(Context paramContext)
  {
    this(paramContext, null);
  }

  public NumberPicker(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setOrientation(1);
    ((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2130903069, this, true);
    this.mHandler = new Handler();
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        NumberPicker.this.validateInput(NumberPicker.this.mText);
        if (!NumberPicker.this.mText.hasFocus())
          NumberPicker.this.mText.requestFocus();
        if (2131361895 == paramAnonymousView.getId())
          NumberPicker.this.changeCurrent(1 + NumberPicker.this.mCurrent);
        while (2131361897 != paramAnonymousView.getId())
          return;
        NumberPicker.this.changeCurrent(NumberPicker.this.mCurrent - 1);
      }
    };
    View.OnFocusChangeListener local4 = new View.OnFocusChangeListener()
    {
      public void onFocusChange(View paramAnonymousView, boolean paramAnonymousBoolean)
      {
        if (!paramAnonymousBoolean)
          NumberPicker.this.validateInput(paramAnonymousView);
      }
    };
    View.OnLongClickListener local5 = new View.OnLongClickListener()
    {
      public boolean onLongClick(View paramAnonymousView)
      {
        NumberPicker.this.mText.clearFocus();
        if (2131361895 == paramAnonymousView.getId())
        {
          NumberPicker.access$002(NumberPicker.this, true);
          NumberPicker.this.mHandler.post(NumberPicker.this.mRunnable);
        }
        while (2131361897 != paramAnonymousView.getId())
          return true;
        NumberPicker.access$402(NumberPicker.this, true);
        NumberPicker.this.mHandler.post(NumberPicker.this.mRunnable);
        return true;
      }
    };
    NumberPickerInputFilter localNumberPickerInputFilter = new NumberPickerInputFilter(null);
    this.mNumberInputFilter = new NumberRangeKeyListener(null);
    this.mIncrementButton = ((NumberPickerButton)findViewById(2131361895));
    this.mIncrementButton.setOnClickListener(local3);
    this.mIncrementButton.setOnLongClickListener(local5);
    this.mIncrementButton.setNumberPicker(this);
    this.mDecrementButton = ((NumberPickerButton)findViewById(2131361897));
    this.mDecrementButton.setOnClickListener(local3);
    this.mDecrementButton.setOnLongClickListener(local5);
    this.mDecrementButton.setNumberPicker(this);
    this.mText = ((EditText)findViewById(2131361896));
    this.mText.setOnFocusChangeListener(local4);
    this.mText.setFilters(new InputFilter[] { localNumberPickerInputFilter });
    this.mText.setRawInputType(2);
    if (!isEnabled())
      setEnabled(false);
  }

  private String formatNumber(int paramInt)
  {
    if (this.mFormatter != null)
      return this.mFormatter.toString(paramInt);
    return String.valueOf(paramInt);
  }

  private int getSelectedPos(String paramString)
  {
    if (this.mDisplayedValues == null)
      return Integer.parseInt(paramString);
    for (int i = 0; i < this.mDisplayedValues.length; i++)
    {
      paramString = paramString.toLowerCase();
      if (this.mDisplayedValues[i].toLowerCase().startsWith(paramString))
        return i + this.mStart;
    }
    try
    {
      int j = Integer.parseInt(paramString);
      return j;
    }
    catch (NumberFormatException localNumberFormatException)
    {
    }
    return this.mStart;
  }

  private void notifyChange()
  {
    if (this.mListener != null)
      this.mListener.onChanged(this, this.mPrevious, this.mCurrent);
  }

  private void updateView()
  {
    if (this.mDisplayedValues == null)
      this.mText.setText(formatNumber(this.mCurrent));
    while (true)
    {
      this.mText.setSelection(this.mText.getText().length());
      return;
      this.mText.setText(this.mDisplayedValues[(this.mCurrent - this.mStart)]);
    }
  }

  private void validateCurrentView(CharSequence paramCharSequence)
  {
    int i = getSelectedPos(paramCharSequence.toString());
    if ((i >= this.mStart) && (i <= this.mEnd) && (this.mCurrent != i))
    {
      this.mPrevious = this.mCurrent;
      this.mCurrent = i;
      notifyChange();
    }
    updateView();
  }

  private void validateInput(View paramView)
  {
    String str = String.valueOf(((TextView)paramView).getText());
    if ("".equals(str))
    {
      updateView();
      return;
    }
    validateCurrentView(str);
  }

  public void cancelDecrement()
  {
    this.mDecrement = false;
  }

  public void cancelIncrement()
  {
    this.mIncrement = false;
  }

  protected void changeCurrent(int paramInt)
  {
    if (paramInt > this.mEnd)
      paramInt = this.mStart;
    while (true)
    {
      this.mPrevious = this.mCurrent;
      this.mCurrent = paramInt;
      notifyChange();
      updateView();
      return;
      if (paramInt < this.mStart)
        paramInt = this.mEnd;
    }
  }

  protected int getBeginRange()
  {
    return this.mStart;
  }

  public int getCurrent()
  {
    return this.mCurrent;
  }

  protected int getEndRange()
  {
    return this.mEnd;
  }

  public void setCurrent(int paramInt)
  {
    if ((paramInt < this.mStart) || (paramInt > this.mEnd))
      throw new IllegalArgumentException("current should be >= start and <= end");
    this.mCurrent = paramInt;
    updateView();
  }

  public void setEnabled(boolean paramBoolean)
  {
    super.setEnabled(paramBoolean);
    this.mIncrementButton.setEnabled(paramBoolean);
    this.mDecrementButton.setEnabled(paramBoolean);
    this.mText.setEnabled(paramBoolean);
  }

  public void setFormatter(Formatter paramFormatter)
  {
    this.mFormatter = paramFormatter;
  }

  public void setOnChangeListener(OnChangedListener paramOnChangedListener)
  {
    this.mListener = paramOnChangedListener;
  }

  public void setRange(int paramInt1, int paramInt2)
  {
    setRange(paramInt1, paramInt2, null);
  }

  public void setRange(int paramInt1, int paramInt2, String[] paramArrayOfString)
  {
    this.mDisplayedValues = paramArrayOfString;
    this.mStart = paramInt1;
    this.mEnd = paramInt2;
    this.mCurrent = paramInt1;
    updateView();
  }

  public void setSpeed(long paramLong)
  {
    this.mSpeed = paramLong;
  }

  public static abstract interface Formatter
  {
    public abstract String toString(int paramInt);
  }

  private class NumberPickerInputFilter
    implements InputFilter
  {
    private NumberPickerInputFilter()
    {
    }

    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      if (NumberPicker.this.mDisplayedValues == null)
        return NumberPicker.this.mNumberInputFilter.filter(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4);
      String str1 = String.valueOf(paramCharSequence.subSequence(paramInt1, paramInt2));
      String str2 = String.valueOf(String.valueOf(paramSpanned.subSequence(0, paramInt3)) + str1 + paramSpanned.subSequence(paramInt4, paramSpanned.length())).toLowerCase();
      String[] arrayOfString = NumberPicker.this.mDisplayedValues;
      int i = arrayOfString.length;
      for (int j = 0; j < i; j++)
        if (arrayOfString[j].toLowerCase().startsWith(str2))
          return str1;
      return "";
    }
  }

  private class NumberRangeKeyListener extends NumberKeyListener
  {
    private NumberRangeKeyListener()
    {
    }

    public CharSequence filter(CharSequence paramCharSequence, int paramInt1, int paramInt2, Spanned paramSpanned, int paramInt3, int paramInt4)
    {
      CharSequence localCharSequence = super.filter(paramCharSequence, paramInt1, paramInt2, paramSpanned, paramInt3, paramInt4);
      if (localCharSequence == null)
        localCharSequence = paramCharSequence.subSequence(paramInt1, paramInt2);
      String str = String.valueOf(paramSpanned.subSequence(0, paramInt3)) + localCharSequence + paramSpanned.subSequence(paramInt4, paramSpanned.length());
      if ("".equals(str))
        return str;
      if (NumberPicker.this.getSelectedPos(str) > NumberPicker.this.mEnd)
        return "";
      return localCharSequence;
    }

    protected char[] getAcceptedChars()
    {
      return NumberPicker.DIGIT_CHARACTERS;
    }

    public int getInputType()
    {
      return 2;
    }
  }

  public static abstract interface OnChangedListener
  {
    public abstract void onChanged(NumberPicker paramNumberPicker, int paramInt1, int paramInt2);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.NumberPicker
 * JD-Core Version:    0.6.2
 */