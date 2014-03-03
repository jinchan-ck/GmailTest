package com.android.mail;

import android.content.Context;
import android.text.format.DateUtils;
import java.util.Formatter;

public class FormattedDateBuilder
{
  private Formatter dateFormatter;
  private Context mContext;
  private StringBuilder sb;

  public FormattedDateBuilder(Context paramContext)
  {
    this.mContext = paramContext;
    this.sb = new StringBuilder();
    this.dateFormatter = new Formatter(this.sb);
  }

  public CharSequence formatLongDateTime(long paramLong)
  {
    this.sb.setLength(0);
    DateUtils.formatDateRange(this.mContext, this.dateFormatter, paramLong, paramLong, 524310);
    this.sb.append(" ");
    DateUtils.formatDateRange(this.mContext, this.dateFormatter, paramLong, paramLong, 1);
    return this.sb.toString();
  }

  public CharSequence formatShortDate(long paramLong)
  {
    return DateUtils.getRelativeTimeSpanString(this.mContext, paramLong);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.FormattedDateBuilder
 * JD-Core Version:    0.6.2
 */