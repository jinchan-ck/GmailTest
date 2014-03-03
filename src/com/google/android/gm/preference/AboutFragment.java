package com.google.android.gm.preference;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.mail.ui.WhatsNewDialogFragment.WhatsNewDialogLauncher;
import com.google.android.gm.Utils;
import com.google.android.gsf.Gservices;
import java.util.Calendar;

public final class AboutFragment extends Fragment
{
  private WhatsNewDialogFragment.WhatsNewDialogLauncher mCallback;

  public void onAttach(Activity paramActivity)
  {
    super.onAttach(paramActivity);
    try
    {
      this.mCallback = ((WhatsNewDialogFragment.WhatsNewDialogLauncher)paramActivity);
      return;
    }
    catch (ClassCastException localClassCastException)
    {
    }
    throw new ClassCastException(paramActivity.getClass().getSimpleName() + " must implement WhatsNewDialogLauncher");
  }

  public View onCreateView(LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup, Bundle paramBundle)
  {
    View localView = paramLayoutInflater.inflate(2130968576, null);
    TextView localTextView1 = (TextView)localView.findViewById(2131689478);
    try
    {
      localTextView1.setText(getString(2131427648, new Object[] { getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName }));
      label60: TextView localTextView2 = (TextView)localView.findViewById(2131689479);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Integer.valueOf(Calendar.getInstance().get(1));
      localTextView2.setText(getString(2131427647, arrayOfObject));
      TextView localTextView3 = (TextView)localView.findViewById(2131689480);
      MovementMethod localMovementMethod = LinkMovementMethod.getInstance();
      localTextView3.setMovementMethod(localMovementMethod);
      URLSpan local1 = new URLSpan("")
      {
        public void onClick(View paramAnonymousView)
        {
          if (AboutFragment.this.mCallback != null)
            AboutFragment.this.mCallback.showWhatsNewDialog();
        }
      };
      String str1 = getString(2131427819);
      SpannableString localSpannableString1 = new SpannableString(str1);
      localSpannableString1.setSpan(local1, 0, str1.length(), 34);
      localTextView3.setText(localSpannableString1);
      TextView localTextView4 = (TextView)localView.findViewById(2131689481);
      localTextView4.setMovementMethod(localMovementMethod);
      URLSpan local2 = new URLSpan("")
      {
        public void onClick(View paramAnonymousView)
        {
          Utils.showFeedbackSurvey(paramAnonymousView.getContext());
        }
      };
      String str2 = getString(2131427424);
      SpannableString localSpannableString2 = new SpannableString(str2);
      localSpannableString2.setSpan(local2, 0, str2.length(), 34);
      localTextView4.setText(localSpannableString2);
      TextView localTextView5 = (TextView)localView.findViewById(2131689482);
      if (Utils.isGoogleFeedbackInstalled(getActivity()))
      {
        localTextView5.setMovementMethod(localMovementMethod);
        URLSpan local3 = new URLSpan("")
        {
          public void onClick(View paramAnonymousView)
          {
            Utils.launchGoogleFeedback(paramAnonymousView.getContext());
          }
        };
        String str6 = getString(2131427642);
        SpannableString localSpannableString5 = new SpannableString(str6);
        localSpannableString5.setSpan(local3, 0, str6.length(), 34);
        localTextView5.setText(localSpannableString5);
      }
      while (true)
      {
        TextView localTextView6 = (TextView)localView.findViewById(2131689485);
        localTextView6.setMovementMethod(localMovementMethod);
        URLSpan local4 = new URLSpan("")
        {
          public void onClick(View paramAnonymousView)
          {
            Utils.showOpenSourceLicenses(paramAnonymousView.getContext());
          }
        };
        String str3 = getString(2131427645);
        SpannableString localSpannableString3 = new SpannableString(str3);
        localSpannableString3.setSpan(local4, 0, str3.length(), 34);
        localTextView6.setText(localSpannableString3);
        TextView localTextView7 = (TextView)localView.findViewById(2131689486);
        localTextView7.setMovementMethod(localMovementMethod);
        String str4 = Gservices.getString(localView.getContext().getContentResolver(), "gmail_privacy_policy_url", "http://www.google.com/policies/privacy/");
        URLSpan localURLSpan = new URLSpan(str4);
        String str5 = getString(2131427646);
        SpannableString localSpannableString4 = new SpannableString(str5);
        localSpannableString4.setSpan(localURLSpan, 0, str5.length(), 34);
        localTextView7.setText(localSpannableString4);
        setHasOptionsMenu(true);
        return localView;
        localTextView5.setVisibility(8);
        localView.findViewById(2131689483).setVisibility(8);
      }
    }
    catch (PackageManager.NameNotFoundException localNameNotFoundException)
    {
      break label60;
    }
  }

  public void onPrepareOptionsMenu(Menu paramMenu)
  {
    paramMenu.clear();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.preference.AboutFragment
 * JD-Core Version:    0.6.2
 */