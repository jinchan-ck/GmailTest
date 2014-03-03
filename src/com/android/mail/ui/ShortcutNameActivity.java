package com.android.mail.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ShortcutNameActivity extends Activity
  implements View.OnClickListener, TextView.OnEditorActionListener
{
  private String mFolderName;
  private EditText mFolderText;
  private Intent mShortcutClickIntent;

  private void doCancel()
  {
    setResult(0);
    finish();
  }

  private void doCreateShortcut()
  {
    Editable localEditable = this.mFolderText.getText();
    Intent localIntent = new Intent();
    localIntent.putExtra("extra_folder_click_intent", this.mShortcutClickIntent);
    localIntent.putExtra("android.intent.extra.shortcut.NAME", this.mFolderName);
    String str = localEditable.toString();
    if (TextUtils.getTrimmedLength(str) > 0)
      this.mShortcutClickIntent.putExtra("android.intent.extra.shortcut.NAME", str);
    setResult(-1, this.mShortcutClickIntent);
    finish();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (2131689709 == i)
      doCreateShortcut();
    while (2131689649 != i)
      return;
    doCancel();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130968670);
    this.mShortcutClickIntent = ((Intent)getIntent().getParcelableExtra("extra_folder_click_intent"));
    this.mFolderName = getIntent().getStringExtra("extra_shortcut_name");
    this.mFolderText = ((EditText)findViewById(2131689695));
    this.mFolderText.setText(this.mFolderName);
    this.mFolderText.setOnEditorActionListener(this);
    this.mFolderText.requestFocus();
    Editable localEditable = this.mFolderText.getText();
    Selection.setSelection(localEditable, localEditable.length());
    findViewById(2131689709).setOnClickListener(this);
    findViewById(2131689649).setOnClickListener(this);
    ActionBar localActionBar = getActionBar();
    if (localActionBar != null)
      localActionBar.setIcon(2130903041);
  }

  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 6)
    {
      doCreateShortcut();
      return true;
    }
    return false;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ShortcutNameActivity
 * JD-Core Version:    0.6.2
 */