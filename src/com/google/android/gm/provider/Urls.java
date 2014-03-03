package com.google.android.gm.provider;

import android.content.ContentResolver;
import android.net.http.AndroidHttpClient;
import android.text.TextUtils;
import com.google.android.gm.provider.protos.GmsProtosMessageTypes;
import com.google.android.gsf.Gservices;
import com.google.common.io.protocol.ProtoBuf;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

public class Urls
{
  private String mAccount;
  private URI mUri;

  public Urls(String paramString)
  {
    this.mAccount = paramString;
    this.mUri = getUri(paramString);
  }

  private static boolean accountDomainIsGmail(String paramString)
  {
    return (TextUtils.isEmpty(paramString)) || (paramString.toLowerCase().equals("gmail.com")) || (paramString.toLowerCase().equals("googlemail.com"));
  }

  private static String accountGetDomain(String paramString)
  {
    String str = "";
    if (paramString != null)
    {
      int i = paramString.indexOf("@");
      if (i != -1)
        str = paramString.substring(i + 1);
    }
    return str;
  }

  private void addStandardParams(int paramInt, List<NameValuePair> paramList)
  {
    paramList.add(new BasicNameValuePair("version", Integer.toString(paramInt)));
    paramList.add(new BasicNameValuePair("clientVersion", Integer.toString(25)));
    paramList.add(new BasicNameValuePair("allowAnyVersion", "1"));
  }

  public static String buildUri(String paramString, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    ArrayList localArrayList = new ArrayList(i / 2);
    for (int j = 0; j < i; j += 2)
      localArrayList.add(new BasicNameValuePair(paramArrayOfString[j], paramArrayOfString[(j + 1)]));
    StringBuilder localStringBuilder = new StringBuilder(paramString);
    if (paramString.indexOf("?") == -1)
      localStringBuilder.append("?");
    while (true)
    {
      localStringBuilder.append(URLEncodedUtils.format(localArrayList, "UTF-8"));
      return localStringBuilder.toString();
      if (!paramString.endsWith("&"))
        localStringBuilder.append("&");
    }
  }

  private void fillEntity(ContentResolver paramContentResolver, ProtoBuf paramProtoBuf, HttpPost paramHttpPost)
  {
    try
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      paramProtoBuf.outputTo(localByteArrayOutputStream);
      int i = Gservices.getInt(paramContentResolver, "gmail_max_gzip_size_bytes", 250000);
      if (localByteArrayOutputStream.size() <= i);
      for (Object localObject = AndroidHttpClient.getCompressedEntity(localByteArrayOutputStream.toByteArray(), paramContentResolver); ; localObject = new ByteArrayEntity(localByteArrayOutputStream.toByteArray()))
      {
        paramHttpPost.setEntity((HttpEntity)localObject);
        return;
      }
    }
    catch (IOException localIOException)
    {
    }
    throw new RuntimeException("Should not get IO errors while writing to ram");
  }

  public static Cookie getCookie(String paramString1, String paramString2)
  {
    String str1 = accountGetDomain(paramString1);
    String str2;
    if ((str1 != null) && (!accountDomainIsGmail(str1)))
      str2 = str1 + "=";
    for (String str3 = "GXAS_SEC"; ; str3 = "GX")
    {
      BasicClientCookie localBasicClientCookie = new BasicClientCookie(str3, str2 + paramString2);
      localBasicClientCookie.setDomain("google.com");
      return localBasicClientCookie;
      str2 = "";
    }
  }

  public static String getCookieString(String paramString1, String paramString2)
  {
    Cookie localCookie = getCookie(paramString1, paramString2);
    String str1 = localCookie.getPath();
    String str2 = localCookie.getDomain();
    StringBuilder localStringBuilder1 = new StringBuilder(localCookie.getName()).append("=").append(localCookie.getValue()).append("; path=");
    if (str1 == null)
      str1 = "/";
    StringBuilder localStringBuilder2 = localStringBuilder1.append(str1).append("; domain=");
    if (str2 == null)
      str2 = "google.com";
    return str2;
  }

  public static URI getUri(String paramString)
  {
    String str1 = accountGetDomain(paramString);
    if ((str1 != null) && (!accountDomainIsGmail(str1)));
    for (String str2 = "https://mail.google.com/a/%domain%/g/".replace("%domain%", str1); ; str2 = "https://mail.google.com/mail/g/")
      return URI.create(str2);
  }

  public static String getUriString(String paramString)
  {
    String str = accountGetDomain(paramString);
    if ((str != null) && (!accountDomainIsGmail(str)))
      return "https://mail.google.com/a/%domain%/g/".replace("%domain%", str);
    return "https://mail.google.com/mail/g/";
  }

  private URI getUriWithParams(List<NameValuePair> paramList)
  {
    String str = URLEncodedUtils.format(paramList, "UTF-8");
    if (TextUtils.isEmpty(str))
      return this.mUri;
    return URI.create(this.mUri + "?" + str);
  }

  public HttpUriRequest getConversationListUrl(ContentResolver paramContentResolver, int paramInt1, String paramString, long paramLong, int paramInt2, int paramInt3)
  {
    if (paramInt1 >= 25)
    {
      ProtoBuf localProtoBuf1 = new ProtoBuf(GmsProtosMessageTypes.REQUEST);
      ProtoBuf localProtoBuf2 = localProtoBuf1.setNewProtoBuf(5);
      localProtoBuf2.setString(1, paramString);
      localProtoBuf2.setLong(2, paramLong);
      localProtoBuf2.setInt(3, paramInt2);
      localProtoBuf2.setInt(4, paramInt3);
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = paramString;
      arrayOfObject[1] = Long.valueOf(paramLong);
      arrayOfObject[2] = Integer.valueOf(paramInt2);
      arrayOfObject[3] = Integer.valueOf(paramInt3);
      LogUtils.d("Gmail", "getConversationListUrl: query: %s, highestMessageId: %d, maxResults = %d, maxSenders %d", arrayOfObject);
      return newProtoRequest(paramContentResolver, paramInt1, 0L, localProtoBuf1, true);
    }
    LinkedList localLinkedList = new LinkedList();
    addStandardParams(paramInt1, localLinkedList);
    localLinkedList.add(new BasicNameValuePair("view", "query"));
    localLinkedList.add(new BasicNameValuePair("query", paramString));
    localLinkedList.add(new BasicNameValuePair("highestMessageId", Long.toString(paramLong)));
    localLinkedList.add(new BasicNameValuePair("maxResults", Integer.toString(paramInt2)));
    localLinkedList.add(new BasicNameValuePair("maxSenders", Integer.toString(paramInt3)));
    return new HttpGet(getUriWithParams(localLinkedList));
  }

  public URI getFetchAttachmentUri(int paramInt1, String paramString, int paramInt2, boolean paramBoolean)
  {
    String[] arrayOfString = Gmail.AttachmentOrigin.splitServerExtras(paramString);
    String str1 = arrayOfString[1];
    String str2 = arrayOfString[2];
    LinkedList localLinkedList = new LinkedList();
    addStandardParams(paramInt1, localLinkedList);
    localLinkedList.add(new BasicNameValuePair("view", "att"));
    localLinkedList.add(new BasicNameValuePair("messageId", str1));
    localLinkedList.add(new BasicNameValuePair("partId", str2));
    localLinkedList.add(new BasicNameValuePair("maxWidth", Integer.toString(paramInt2)));
    localLinkedList.add(new BasicNameValuePair("maxHeight", Integer.toString(paramInt2)));
    if (paramBoolean);
    for (String str3 = "1"; ; str3 = "0")
    {
      localLinkedList.add(new BasicNameValuePair("showOriginal", str3));
      return getUriWithParams(localLinkedList);
    }
  }

  public HttpUriRequest getGetSyncConfigSuggestionRequest(ContentResolver paramContentResolver, int paramInt1, int paramInt2, int paramInt3, double paramDouble, long paramLong)
  {
    if (paramInt1 >= 25)
    {
      ProtoBuf localProtoBuf = new ProtoBuf(GmsProtosMessageTypes.REQUEST);
      localProtoBuf.setNewProtoBuf(8);
      LogUtils.d("Gmail", "getSyncConfigSuggestion: GetConfigInfo", new Object[0]);
      return newProtoRequest(paramContentResolver, paramInt1, 0L, localProtoBuf, true);
    }
    LinkedList localLinkedList = new LinkedList();
    addStandardParams(paramInt1, localLinkedList);
    localLinkedList.add(new BasicNameValuePair("view", "configInfo"));
    localLinkedList.add(new BasicNameValuePair("max_message_count", Long.toString(paramInt2)));
    localLinkedList.add(new BasicNameValuePair("always_download_label_limit", Long.toString(paramInt3)));
    localLinkedList.add(new BasicNameValuePair("unread_fraction_limit", Double.toString(paramDouble)));
    localLinkedList.add(new BasicNameValuePair("recent_label_duration_days", Long.toString(paramLong)));
    return new HttpGet(getUriWithParams(localLinkedList));
  }

  public ProtoBuf getMainSyncRequestProto(ContentResolver paramContentResolver, long paramLong1, long paramLong2, long paramLong3, ArrayList<MailSync.ConversationInfo> paramArrayList, ArrayList<Long> paramArrayList1, ArrayList<Long> paramArrayList2, MailEngine.SyncInfo paramSyncInfo)
  {
    ProtoBuf localProtoBuf1 = new ProtoBuf(GmsProtosMessageTypes.REQUEST);
    ProtoBuf localProtoBuf2 = localProtoBuf1.setNewProtoBuf(4);
    localProtoBuf2.setLong(1, paramLong1);
    localProtoBuf2.setLong(2, paramLong2);
    localProtoBuf2.setInt(3, 200);
    localProtoBuf2.setBool(6, true);
    localProtoBuf2.setBool(8, true);
    localProtoBuf2.setBool(9, true);
    localProtoBuf2.setInt(7, Gservices.getInt(paramContentResolver, "gmail_compression_type", 3));
    localProtoBuf2.setBool(10, true);
    localProtoBuf2.setInt(11, Gservices.getInt(paramContentResolver, "gmail_main_sync_max_conversion_headers", 0));
    localProtoBuf2.setInt(12, 5);
    localProtoBuf1.setNewProtoBuf(7).setLong(2, paramLong3);
    Object[] arrayOfObject1 = new Object[3];
    arrayOfObject1[0] = Long.valueOf(paramLong1);
    arrayOfObject1[1] = Long.valueOf(paramLong2);
    arrayOfObject1[2] = Boolean.valueOf(paramSyncInfo.normalSync);
    LogUtils.i("Gmail", "MainSyncRequestProto: lowestBkwdConvoId: %d, highestHandledServerOp: %d, normalSync: %b", arrayOfObject1);
    ProtoBuf localProtoBuf3 = null;
    int i = 0;
    int j = paramArrayList.size();
    while (i < j)
    {
      MailSync.ConversationInfo localConversationInfo = (MailSync.ConversationInfo)paramArrayList.get(i);
      if (localProtoBuf3 == null)
        localProtoBuf3 = localProtoBuf1.setNewProtoBuf(3);
      ProtoBuf localProtoBuf4 = localProtoBuf3.addNewProtoBuf(1);
      localProtoBuf4.setLong(1, localConversationInfo.id);
      localProtoBuf4.setLong(2, localConversationInfo.highestFetchedMessageId);
      if (localConversationInfo.highestFetchedMessageId == 0L)
        localProtoBuf3.addLong(4, localConversationInfo.id);
      Object[] arrayOfObject4 = new Object[2];
      arrayOfObject4[0] = Long.valueOf(localConversationInfo.id);
      arrayOfObject4[1] = Long.valueOf(localConversationInfo.highestFetchedMessageId);
      LogUtils.v("Gmail", "MainSyncRequestProto: fetchConversation: ConvoId: %d, HighestMessageIdOnClient: %d", arrayOfObject4);
      i++;
    }
    int k = 0;
    int m = paramArrayList2.size();
    while (k < m)
    {
      long l2 = ((Long)paramArrayList2.get(k)).longValue();
      if (localProtoBuf3 == null)
        localProtoBuf3 = localProtoBuf1.setNewProtoBuf(3);
      localProtoBuf3.addLong(4, l2);
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Long.valueOf(l2);
      LogUtils.d("Gmail", "MainSyncRequestProto: ConversationSyncDirtyConversationId: %d", arrayOfObject3);
      k++;
    }
    if (!paramSyncInfo.normalSync)
      localProtoBuf2.setInt(5, 0);
    while (true)
    {
      return localProtoBuf1;
      localProtoBuf2.setInt(5, Gservices.getInt(paramContentResolver, "gmail_main_sync_max_forward_sync_items_limit", 1000));
      int n = 0;
      int i1 = paramArrayList1.size();
      while (n < i1)
      {
        long l1 = ((Long)paramArrayList1.get(n)).longValue();
        if (localProtoBuf3 == null)
          localProtoBuf3 = localProtoBuf1.setNewProtoBuf(3);
        localProtoBuf3.addLong(2, l1);
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Long.valueOf(l1);
        LogUtils.d("Gmail", "MainSyncRequestProto: ConversationSyncMessageId: %d", arrayOfObject2);
        n++;
      }
    }
  }

  public HttpUriRequest getStartSyncRequest(ContentResolver paramContentResolver, int paramInt, long paramLong1, long paramLong2, long paramLong3, long paramLong4, long paramLong5)
  {
    if (paramInt >= 25)
    {
      ProtoBuf localProtoBuf1 = new ProtoBuf(GmsProtosMessageTypes.REQUEST);
      ProtoBuf localProtoBuf2 = localProtoBuf1.setNewProtoBuf(6);
      localProtoBuf2.setLong(1, paramLong2);
      localProtoBuf2.setLong(2, paramLong3);
      localProtoBuf2.setLong(3, paramLong4);
      localProtoBuf2.setLong(4, paramLong5);
      localProtoBuf2.setBool(5, true);
      localProtoBuf2.setBool(6, true);
      localProtoBuf2.setBool(7, true);
      localProtoBuf2.setBool(9, true);
      Object[] arrayOfObject = new Object[4];
      arrayOfObject[0] = Long.valueOf(paramLong2);
      arrayOfObject[1] = Long.valueOf(paramLong3);
      arrayOfObject[2] = Long.valueOf(paramLong4);
      arrayOfObject[3] = Long.valueOf(paramLong5);
      LogUtils.i("Gmail", "getStartSyncRequest: handledServerOpId: %d, upperFetchedConvoId: %d, lowerFetchedConvoId: %d, ackedClientOp: %d", arrayOfObject);
      return newProtoRequest(paramContentResolver, paramInt, paramLong1, localProtoBuf1, true);
    }
    LinkedList localLinkedList = new LinkedList();
    addStandardParams(paramInt, localLinkedList);
    localLinkedList.add(new BasicNameValuePair("view", "start"));
    localLinkedList.add(new BasicNameValuePair("client", Long.toString(paramLong1)));
    localLinkedList.add(new BasicNameValuePair("acked_client_op", Long.toString(paramLong5)));
    localLinkedList.add(new BasicNameValuePair("server_op", Long.toString(paramLong2)));
    localLinkedList.add(new BasicNameValuePair("upper_message", Long.toString(paramLong3)));
    localLinkedList.add(new BasicNameValuePair("lower_message", Long.toString(paramLong4)));
    return new HttpGet(getUriWithParams(localLinkedList));
  }

  public HttpUriRequest getSyncConfigRequest(ContentResolver paramContentResolver, int paramInt, long paramLong1, Set<String> paramSet1, Set<String> paramSet2, long paramLong2, long paramLong3)
  {
    if (paramInt >= 25)
    {
      ProtoBuf localProtoBuf1 = new ProtoBuf(GmsProtosMessageTypes.REQUEST);
      ProtoBuf localProtoBuf2 = localProtoBuf1.setNewProtoBuf(2);
      localProtoBuf2.setInt(1, (int)paramLong2);
      Iterator localIterator1 = paramSet1.iterator();
      while (localIterator1.hasNext())
        localProtoBuf2.addString(2, (String)localIterator1.next());
      Iterator localIterator2 = paramSet2.iterator();
      while (localIterator2.hasNext())
        localProtoBuf2.addString(3, (String)localIterator2.next());
      Object[] arrayOfObject = new Object[3];
      arrayOfObject[0] = Long.valueOf(paramLong2);
      arrayOfObject[1] = paramSet1;
      arrayOfObject[2] = paramSet2;
      LogUtils.d("Gmail", "getSyncConfigRequest: conversationAgeDays: %d, labelsIncluded: %s, labelsPartial: %s", arrayOfObject);
      return newProtoRequest(paramContentResolver, paramInt, paramLong1, localProtoBuf1, true);
    }
    LinkedList localLinkedList = new LinkedList();
    addStandardParams(paramInt, localLinkedList);
    localLinkedList.add(new BasicNameValuePair("view", "config"));
    localLinkedList.add(new BasicNameValuePair("client", Long.toString(paramLong1)));
    Iterator localIterator3 = paramSet1.iterator();
    while (localIterator3.hasNext())
      localLinkedList.add(new BasicNameValuePair("labelsIncluded", (String)localIterator3.next()));
    Iterator localIterator4 = paramSet2.iterator();
    while (localIterator4.hasNext())
      localLinkedList.add(new BasicNameValuePair("labelsInDuration", (String)localIterator4.next()));
    localLinkedList.add(new BasicNameValuePair("age", Long.toString(paramLong2)));
    localLinkedList.add(new BasicNameValuePair("attach_size", Long.toString(paramLong3)));
    localLinkedList.add(new BasicNameValuePair("includeInDuration", Boolean.toString(true)));
    localLinkedList.add(new BasicNameValuePair("notificationMethod", "syncServer"));
    return new HttpGet(getUriWithParams(localLinkedList));
  }

  public HttpContext newHttpContext(String paramString, CookieStore paramCookieStore)
  {
    BasicHttpContext localBasicHttpContext = new BasicHttpContext();
    localBasicHttpContext.setAttribute("http.cookie-store", paramCookieStore);
    paramCookieStore.addCookie(getCookie(this.mAccount, paramString));
    return localBasicHttpContext;
  }

  public HttpPost newProtoRequest(ContentResolver paramContentResolver, int paramInt, long paramLong, ProtoBuf paramProtoBuf, boolean paramBoolean)
  {
    if (paramInt < 25)
      throw new IllegalArgumentException("Cannot make a proto request for version " + paramInt);
    if (paramLong != 0L)
    {
      paramProtoBuf.setLong(1, paramLong);
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = Long.valueOf(paramLong);
      LogUtils.d("Gmail", "ProtoRequest: clientid: %d", arrayOfObject);
    }
    LinkedList localLinkedList = new LinkedList();
    addStandardParams(paramInt, localLinkedList);
    HttpPost localHttpPost = new HttpPost(getUriWithParams(localLinkedList));
    if (paramBoolean)
      fillEntity(paramContentResolver, paramProtoBuf, localHttpPost);
    return localHttpPost;
  }

  public String serverUrl()
  {
    return this.mUri.toString();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.Urls
 * JD-Core Version:    0.6.2
 */