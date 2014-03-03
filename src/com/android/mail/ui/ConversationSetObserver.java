package com.android.mail.ui;

public abstract interface ConversationSetObserver
{
  public abstract void onSetChanged(ConversationSelectionSet paramConversationSelectionSet);

  public abstract void onSetEmpty();

  public abstract void onSetPopulated(ConversationSelectionSet paramConversationSelectionSet);
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.ConversationSetObserver
 * JD-Core Version:    0.6.2
 */