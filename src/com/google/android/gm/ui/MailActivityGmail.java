package com.google.android.gm.ui;

import android.content.Intent;
import android.content.UriMatcher;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import com.android.mail.ui.MailActivity;
import com.google.android.gm.Utils;
import com.google.android.gm.persistence.Persistence;
import java.util.List;

public class MailActivityGmail extends MailActivity
{
  static final String EXTRA_ACCOUNT = "account";
  static final String EXTRA_LABEL = "label";
  private static final UriMatcher sUrlMatcher = new UriMatcher(-1);

  static
  {
    sUrlMatcher.addURI("gmail-ls", "account/*/label/*", 0);
  }

  public void onCreate(Bundle paramBundle)
  {
    Intent localIntent = getIntent();
    String str1 = localIntent.getAction();
    String str3;
    String str2;
    if ((str1 == null) && (localIntent.hasExtra("label")) && (localIntent.hasExtra("account")))
    {
      str3 = localIntent.getStringExtra("label");
      str2 = localIntent.getStringExtra("account");
    }
    while (true)
    {
      if ((str2 != null) && (str3 != null))
        localIntent = Utils.createViewFolderIntent(this, str2, str3);
      setIntent(localIntent);
      super.onCreate(paramBundle);
      return;
      boolean bool = "android.intent.action.VIEW".equals(str1);
      str2 = null;
      str3 = null;
      if (bool)
      {
        Uri localUri1 = localIntent.getData();
        str2 = null;
        str3 = null;
        if (localUri1 != null)
        {
          Uri localUri2 = localIntent.getData();
          int i = sUrlMatcher.match(localUri2);
          str2 = null;
          str3 = null;
          if (i != -1)
          {
            List localList = localIntent.getData().getPathSegments();
            str2 = (String)localList.get(1);
            str3 = (String)localList.get(3);
          }
        }
      }
    }
  }

  public void onWhatsNewDialogLayoutInflated(View paramView)
  {
    CheckBox localCheckBox = (CheckBox)paramView.findViewById(2131689718);
    localCheckBox.setChecked(Persistence.getInstance().getConversationOverviewMode(this));
    localCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
    {
      public void onCheckedChanged(CompoundButton paramAnonymousCompoundButton, boolean paramAnonymousBoolean)
      {
        Persistence.getInstance().setConversationOverviewMode(MailActivityGmail.this, paramAnonymousBoolean);
      }
    });
    paramView.findViewById(2131689719).setVisibility(8);
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ui.MailActivityGmail
 * JD-Core Version:    0.6.2
 */