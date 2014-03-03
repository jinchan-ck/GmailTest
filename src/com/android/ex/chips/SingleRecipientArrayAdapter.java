package com.android.ex.chips;

import android.content.Context;
import android.text.util.Rfc822Token;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

class SingleRecipientArrayAdapter extends ArrayAdapter<RecipientEntry>
{
  private int mLayoutId;
  private final LayoutInflater mLayoutInflater;

  public SingleRecipientArrayAdapter(Context paramContext, int paramInt, RecipientEntry paramRecipientEntry)
  {
    super(paramContext, paramInt, new RecipientEntry[] { paramRecipientEntry });
    this.mLayoutInflater = LayoutInflater.from(paramContext);
    this.mLayoutId = paramInt;
  }

  private void bindView(View paramView, Context paramContext, RecipientEntry paramRecipientEntry)
  {
    TextView localTextView = (TextView)paramView.findViewById(16908310);
    ImageView localImageView = (ImageView)paramView.findViewById(16908294);
    localTextView.setText(paramRecipientEntry.getDisplayName());
    localTextView.setVisibility(0);
    localImageView.setVisibility(0);
    ((TextView)paramView.findViewById(16908308)).setText(android.text.util.Rfc822Tokenizer.tokenize(paramRecipientEntry.getDestination())[0].getAddress());
  }

  private View newView()
  {
    return this.mLayoutInflater.inflate(this.mLayoutId, null);
  }

  public View getView(int paramInt, View paramView, ViewGroup paramViewGroup)
  {
    if (paramView == null)
      paramView = newView();
    bindView(paramView, paramView.getContext(), (RecipientEntry)getItem(paramInt));
    return paramView;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.chips.SingleRecipientArrayAdapter
 * JD-Core Version:    0.6.2
 */