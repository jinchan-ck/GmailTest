package com.android.ex.chips;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.style.ImageSpan;

class RecipientChip extends ImageSpan
{
  private final long mContactId;
  private final long mDataId;
  private final CharSequence mDisplay;
  private RecipientEntry mEntry;
  private CharSequence mOriginalText;
  private boolean mSelected = false;
  private final CharSequence mValue;

  public RecipientChip(Drawable paramDrawable, RecipientEntry paramRecipientEntry, int paramInt)
  {
    super(paramDrawable, 0);
    this.mDisplay = paramRecipientEntry.getDisplayName();
    this.mValue = paramRecipientEntry.getDestination().trim();
    this.mContactId = paramRecipientEntry.getContactId();
    this.mDataId = paramRecipientEntry.getDataId();
    this.mEntry = paramRecipientEntry;
  }

  public long getContactId()
  {
    return this.mContactId;
  }

  public long getDataId()
  {
    return this.mDataId;
  }

  public RecipientEntry getEntry()
  {
    return this.mEntry;
  }

  public CharSequence getOriginalText()
  {
    if (!TextUtils.isEmpty(this.mOriginalText))
      return this.mOriginalText;
    return this.mEntry.getDestination();
  }

  public CharSequence getValue()
  {
    return this.mValue;
  }

  public boolean isSelected()
  {
    return this.mSelected;
  }

  public void setOriginalText(String paramString)
  {
    if (!TextUtils.isEmpty(paramString))
      paramString = paramString.trim();
    this.mOriginalText = paramString;
  }

  public void setSelected(boolean paramBoolean)
  {
    this.mSelected = paramBoolean;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.chips.RecipientChip
 * JD-Core Version:    0.6.2
 */