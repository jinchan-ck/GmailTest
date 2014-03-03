package com.google.android.gm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import java.util.Collection;
import java.util.Set;

public abstract class AutoCompletionLabelPickerDialog extends AlertDialog
  implements DialogInterface.OnCancelListener, DialogInterface.OnClickListener
{
  private String mAccount;
  private Collection<ConversationInfo> mConversations;
  private AutoCompleteTextView mTextView;

  protected AutoCompletionLabelPickerDialog(Context paramContext)
  {
    super(paramContext);
    setOnCancelListener(this);
    setButton(-1, paramContext.getString(17039370), this);
    this.mTextView = ((AutoCompleteTextView)((LayoutInflater)paramContext.getSystemService("layout_inflater")).inflate(2130903070, null));
    setView(this.mTextView, 0, 0, 0, 0);
  }

  protected String getAccount()
  {
    return this.mAccount;
  }

  protected Collection<ConversationInfo> getConversations()
  {
    return this.mConversations;
  }

  protected String getLabel()
  {
    return this.mTextView.getText().toString();
  }

  public void onCancel(DialogInterface paramDialogInterface)
  {
  }

  public void onPrepare(String paramString, Set<String> paramSet, Collection<ConversationInfo> paramCollection)
  {
    this.mAccount = paramString;
    this.mConversations = paramCollection;
    this.mTextView.setAdapter(new ArrayAdapter(getContext(), 17367043, paramSet.toArray(new String[paramSet.size()])));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AutoCompletionLabelPickerDialog
 * JD-Core Version:    0.6.2
 */