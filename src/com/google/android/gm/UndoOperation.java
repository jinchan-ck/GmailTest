package com.google.android.gm;

import android.os.Bundle;
import android.util.Log;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

public class UndoOperation
{
  private static final String ACCOUNT = "undo-account";
  private static final String CONVERSATIONS = "undo-conversations";
  private static final String DESCRIPTION = "undo-description";
  private static final String OPERATIONS = "undo-operations";
  public final String mAccount;
  public final Collection<ConversationInfo> mConversations;
  public final String mDescription;
  public final LabelOperations mOperations;

  public UndoOperation(String paramString1, Collection<ConversationInfo> paramCollection, LabelOperations paramLabelOperations, String paramString2)
  {
    this.mAccount = paramString1;
    this.mOperations = paramLabelOperations.undoOperation();
    this.mConversations = paramCollection;
    this.mDescription = paramString2;
  }

  private static Collection<ConversationInfo> decodeConversations(Bundle paramBundle)
  {
    ArrayList localArrayList = Lists.newArrayList();
    String[] arrayOfString = paramBundle.getString("undo-conversations").split(" ");
    int i = arrayOfString.length;
    int j = 0;
    while (true)
      if (j < i)
      {
        String str = arrayOfString[j];
        try
        {
          localArrayList.add(ConversationInfo.deserialize(str));
          j++;
        }
        catch (NumberFormatException localNumberFormatException)
        {
          while (true)
            Log.e("Gmail", "Exception caught parsing serialized conversation: " + localNumberFormatException.toString());
        }
      }
    return localArrayList;
  }

  public static UndoOperation restoreFromExtras(Bundle paramBundle)
  {
    UndoOperation localUndoOperation = null;
    if (paramBundle != null)
    {
      String str1 = paramBundle.getString("undo-account");
      String str2 = paramBundle.getString("undo-operations");
      localUndoOperation = null;
      if (str1 != null)
        localUndoOperation = new UndoOperation(str1, decodeConversations(paramBundle), LabelOperations.deserialize(str2), paramBundle.getString("undo-description"));
    }
    return localUndoOperation;
  }

  public void saveToExtras(Bundle paramBundle)
  {
    paramBundle.putString("undo-account", this.mAccount);
    paramBundle.putString("undo-operations", LabelOperations.serialize(this.mOperations));
    paramBundle.putString("undo-description", this.mDescription);
    StringBuffer localStringBuffer = new StringBuffer();
    Iterator localIterator = this.mConversations.iterator();
    while (localIterator.hasNext())
    {
      ConversationInfo localConversationInfo = (ConversationInfo)localIterator.next();
      localStringBuffer.append(localConversationInfo.serialize() + " ");
    }
    paramBundle.putString("undo-conversations", localStringBuffer.toString());
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.UndoOperation
 * JD-Core Version:    0.6.2
 */