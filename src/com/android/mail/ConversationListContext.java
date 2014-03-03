package com.android.mail;

import android.content.UriMatcher;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.mail.providers.Account;
import com.android.mail.providers.Folder;
import com.google.common.base.Preconditions;

public class ConversationListContext
{
  private static final UriMatcher sUrlMatcher = new UriMatcher(-1);
  public final Account account;
  public final Folder folder;
  public final String searchQuery;

  static
  {
    sUrlMatcher.addURI("com.android.mail.providers", "account/*/folder/*", 0);
  }

  private ConversationListContext(Account paramAccount, String paramString, Folder paramFolder)
  {
    this.account = paramAccount;
    this.searchQuery = paramString;
    this.folder = paramFolder;
  }

  public static ConversationListContext forBundle(Bundle paramBundle)
  {
    Account localAccount = (Account)paramBundle.getParcelable("account");
    Folder localFolder = (Folder)paramBundle.getParcelable("folder");
    return new ConversationListContext(localAccount, paramBundle.getString("query"), localFolder);
  }

  public static ConversationListContext forFolder(Account paramAccount, Folder paramFolder)
  {
    return new ConversationListContext(paramAccount, null, paramFolder);
  }

  public static ConversationListContext forSearchQuery(Account paramAccount, Folder paramFolder, String paramString)
  {
    return new ConversationListContext(paramAccount, (String)Preconditions.checkNotNull(paramString), paramFolder);
  }

  public static final boolean isSearchResult(ConversationListContext paramConversationListContext)
  {
    return (paramConversationListContext != null) && (!TextUtils.isEmpty(paramConversationListContext.searchQuery));
  }

  public Bundle toBundle()
  {
    Bundle localBundle = new Bundle();
    localBundle.putParcelable("account", this.account);
    localBundle.putString("query", this.searchQuery);
    localBundle.putParcelable("folder", this.folder);
    return localBundle;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ConversationListContext
 * JD-Core Version:    0.6.2
 */