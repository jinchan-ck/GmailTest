package com.google.android.gm;

import android.content.ComponentName;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.Calendar;

public class AboutActivity extends GmailBaseActivity
{
  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903040);
    if (1 == getPackageManager().getComponentEnabledSetting(new ComponentName("com.google.android.gm", "com.google.android.gm.ConversationListActivityGoogleMail")))
      ((ImageView)findViewById(2131361795)).setImageDrawable(getResources().getDrawable(2130837603));
    TextView localTextView1 = (TextView)findViewById(2131361797);
    try
    {
      String str3 = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
      localTextView1.setText(String.format(getString(2131296272), new Object[] { str3 }));
      label103: TextView localTextView2 = (TextView)findViewById(2131361798);
      String str1 = getString(2131296271);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(Calendar.getInstance().get(1));
      localTextView2.setText(String.format(str1, arrayOfObject));
      TextView localTextView3 = (TextView)findViewById(2131361799);
      localTextView3.setMovementMethod(LinkMovementMethod.getInstance());
      URLSpan local1 = new URLSpan("")
      {
        public void onClick(View paramAnonymousView)
        {
          Utils.showFeedback(AboutActivity.this);
        }
      };
      String str2 = getString(2131296269);
      SpannableString localSpannableString = new SpannableString(str2);
      localSpannableString.setSpan(local1, 0, str2.length(), 34);
      localTextView3.setText(localSpannableString);
      return;
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      break label103;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AboutActivity
 * JD-Core Version:    0.6.2
 */