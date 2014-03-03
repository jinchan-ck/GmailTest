package android.support.v4.view;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.TextView;
import java.util.Locale;

class PagerTitleStripIcs
{
  public static void setSingleLineAllCaps(TextView paramTextView)
  {
    paramTextView.setTransformationMethod(new SingleLineAllCapsTransform(paramTextView.getContext()));
  }

  private static class SingleLineAllCapsTransform extends SingleLineTransformationMethod
  {
    private Locale mLocale;

    public SingleLineAllCapsTransform(Context paramContext)
    {
      this.mLocale = paramContext.getResources().getConfiguration().locale;
    }

    public CharSequence getTransformation(CharSequence paramCharSequence, View paramView)
    {
      CharSequence localCharSequence = super.getTransformation(paramCharSequence, paramView);
      if (localCharSequence != null)
        return localCharSequence.toString().toUpperCase(this.mLocale);
      return null;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.PagerTitleStripIcs
 * JD-Core Version:    0.6.2
 */