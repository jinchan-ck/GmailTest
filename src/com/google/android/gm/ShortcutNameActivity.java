package com.google.android.gm;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class ShortcutNameActivity extends Activity
  implements View.OnClickListener, TextView.OnEditorActionListener
{
  static final String EXTRA_LABEL_CLICK_INTENT = "extra_label_click_intent";
  static final String EXTRA_SHORTCUT_NAME = "extra_shortcut_name";
  private EditText mLabelText;
  private Intent mShortcutClickIntent;

  private void doCancel()
  {
    setResult(0);
    finish();
  }

  private void doCreateShortcut()
  {
    Editable localEditable = this.mLabelText.getText();
    Intent localIntent = new Intent();
    localIntent.putExtra("extra_label_click_intent", this.mShortcutClickIntent);
    localIntent.putExtra("extra_shortcut_name", localEditable.toString());
    setResult(-1, localIntent);
    finish();
  }

  public void onClick(View paramView)
  {
    int i = paramView.getId();
    if (2131361913 == i)
      doCreateShortcut();
    while (2131361914 != i)
      return;
    doCancel();
  }

  public void onCreate(Bundle paramBundle)
  {
    super.onCreate(paramBundle);
    setContentView(2130903074);
    getWindow().setBackgroundDrawable(null);
    this.mShortcutClickIntent = ((Intent)getIntent().getParcelableExtra("extra_label_click_intent"));
    String str = getIntent().getStringExtra("extra_shortcut_name");
    this.mLabelText = ((EditText)findViewById(2131361912));
    this.mLabelText.setText(str);
    this.mLabelText.setOnEditorActionListener(this);
    this.mLabelText.requestFocus();
    Editable localEditable = this.mLabelText.getText();
    Selection.setSelection(localEditable, localEditable.length());
    findViewById(2131361913).setOnClickListener(this);
    findViewById(2131361914).setOnClickListener(this);
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

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ShortcutNameActivity
 * JD-Core Version:    0.6.2
 */