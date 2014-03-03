package com.android.mail.providers;

import android.net.Uri;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

public class ReplyFromAccount
  implements Serializable
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private static final long serialVersionUID = 1L;
  public Account account;
  public String address;
  Uri baseAccountUri;
  public boolean isCustomFrom;
  public boolean isDefault;
  public String name;
  public String replyTo;

  public ReplyFromAccount(Account paramAccount, Uri paramUri, String paramString1, String paramString2, String paramString3, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.account = paramAccount;
    this.baseAccountUri = paramUri;
    this.address = paramString1;
    this.name = paramString2;
    this.replyTo = paramString3;
    this.isDefault = paramBoolean1;
    this.isCustomFrom = paramBoolean2;
  }

  public static ReplyFromAccount deserialize(Account paramAccount, String paramString)
  {
    try
    {
      ReplyFromAccount localReplyFromAccount = deserialize(paramAccount, new JSONObject(paramString));
      return localReplyFromAccount;
    }
    catch (JSONException localJSONException)
    {
      LogUtils.wtf(LOG_TAG, localJSONException, "Could not deserialize replyfromaccount", new Object[0]);
    }
    return null;
  }

  public static ReplyFromAccount deserialize(Account paramAccount, JSONObject paramJSONObject)
  {
    try
    {
      ReplyFromAccount localReplyFromAccount = new ReplyFromAccount(paramAccount, Utils.getValidUri(paramJSONObject.getString("baseAccountUri")), paramJSONObject.getString("address"), paramJSONObject.getString("name"), paramJSONObject.getString("replyTo"), paramJSONObject.getBoolean("isDefault"), paramJSONObject.getBoolean("isCustom"));
      return localReplyFromAccount;
    }
    catch (JSONException localJSONException)
    {
      LogUtils.wtf(LOG_TAG, localJSONException, "Could not deserialize replyfromaccount", new Object[0]);
    }
    return null;
  }

  public static boolean matchesAccountOrCustomFrom(Account paramAccount, String paramString, List<ReplyFromAccount> paramList)
  {
    String str = android.text.util.Rfc822Tokenizer.tokenize(paramString)[0].getAddress();
    if (TextUtils.equals(paramAccount.name, str))
      return true;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      ReplyFromAccount localReplyFromAccount = (ReplyFromAccount)localIterator.next();
      if ((TextUtils.equals(localReplyFromAccount.address, str)) && (localReplyFromAccount.isCustomFrom))
        return true;
    }
    return false;
  }

  public JSONObject serialize()
  {
    JSONObject localJSONObject = new JSONObject();
    try
    {
      localJSONObject.put("baseAccountUri", this.baseAccountUri);
      localJSONObject.put("address", this.address);
      localJSONObject.put("name", this.name);
      localJSONObject.put("replyTo", this.replyTo);
      localJSONObject.put("isDefault", this.isDefault);
      localJSONObject.put("isCustom", this.isCustomFrom);
      return localJSONObject;
    }
    catch (JSONException localJSONException)
    {
      LogUtils.wtf(LOG_TAG, localJSONException, "Could not serialize account with name " + this.name, new Object[0]);
    }
    return localJSONObject;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.providers.ReplyFromAccount
 * JD-Core Version:    0.6.2
 */