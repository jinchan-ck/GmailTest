package com.google.android.gm;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;
import com.android.common.Rfc822InputFilter;
import com.android.common.Rfc822Validator;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.android.gm.provider.Gmail.MessageCursor;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ComposeArea
  implements QuotedTextView.RespondInlineListener, QuotedTextView.ShowHideQuotedTextListener, AttachmentsView.AttachmentChangesListener
{
  static final String[] ALL_EXTRAS = { "subject", "body", "to", "cc", "bcc" };
  private static final String BLOCKQUOTE_BEGIN = "<blockquote class=\"gmail_quote\" style=\"margin:0 0 0 .8ex;border-left:1px #ccc solid;padding-left:1ex\">";
  private static final String BLOCKQUOTE_END = "</blockquote>";
  private static final int DEFAULT_MAX_ATTACHMENT_SIZE = 26214400;
  static final String EXTRA_ATTACHMENTS = "attachments";
  private static final String EXTRA_BCC = "bcc";
  private static final String EXTRA_BODY = "body";
  private static final String EXTRA_CC = "cc";
  private static final String EXTRA_SHOW_QUOTED_TEXT = "showQuotedText";
  private static final String EXTRA_SUBJECT = "subject";
  private static final String EXTRA_TO = "to";
  private static final String GMAIL_FROM = "gmailfrom";
  static final String GMAIL_QUOTE_BEGIN = "<div class=\"gmail_quote\">";
  private static final String GMAIL_QUOTE_END = "</div>";
  private static final String MAIL_TO = "mailto";
  private static InputFilter[] sRecipientFilters = arrayOfInputFilter;
  private final String mAccount;
  private EmailAddressAdapter mAddressAdapter;
  private boolean mAttachmentsChanged = false;
  private AttachmentsView mAttachmentsView;
  MultiAutoCompleteTextView mBccList;
  private EditText mBodyText;
  MultiAutoCompleteTextView mCcList;
  private final ComposeController mController;
  private LayoutInflater mInflater;
  private final Activity mParent;
  private final Persistence mPrefs;
  private QuotedTextView mQuotedTextView;
  private AlertDialog mRecipientErrorDialog;
  private View mRootView;
  private Boolean mShowCcBcc = null;
  private EditText mSubjectText;
  MultiAutoCompleteTextView mToList;
  private Rfc822Validator mValidator;

  static
  {
    InputFilter[] arrayOfInputFilter = new InputFilter[1];
    arrayOfInputFilter[0] = new Rfc822InputFilter();
  }

  public ComposeArea(Activity paramActivity, ComposeController paramComposeController, Persistence paramPersistence, String paramString)
  {
    this.mParent = paramActivity;
    this.mPrefs = paramPersistence;
    this.mAccount = paramString;
    this.mController = paramComposeController;
  }

  private void addAddressToList(String paramString, MultiAutoCompleteTextView paramMultiAutoCompleteTextView)
  {
    if (paramString == null);
    while (true)
    {
      return;
      Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramString);
      for (int i = 0; i < arrayOfRfc822Token.length; i++)
        paramMultiAutoCompleteTextView.append(arrayOfRfc822Token[i] + ", ");
    }
  }

  private void addAddressesToList(List<Rfc822Token[]> paramList, MultiAutoCompleteTextView paramMultiAutoCompleteTextView)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Rfc822Token[] arrayOfRfc822Token = (Rfc822Token[])localIterator.next();
      for (int i = 0; i < arrayOfRfc822Token.length; i++)
      {
        paramMultiAutoCompleteTextView.append(arrayOfRfc822Token[i].toString());
        paramMultiAutoCompleteTextView.append(", ");
      }
    }
  }

  // ERROR //
  private void addAttachment(Uri paramUri, String paramString, boolean paramBoolean)
    throws ComposeArea.AttachmentFailureException
  {
    // Byte code:
    //   0: aload_0
    //   1: getfield 112	com/google/android/gm/ComposeArea:mParent	Landroid/app/Activity;
    //   4: invokevirtual 186	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   7: astore 4
    //   9: aload_2
    //   10: ifnonnull +6 -> 16
    //   13: ldc 188
    //   15: astore_2
    //   16: new 190	com/google/android/gm/provider/Gmail$Attachment
    //   19: dup
    //   20: invokespecial 191	com/google/android/gm/provider/Gmail$Attachment:<init>	()V
    //   23: astore 5
    //   25: aload 5
    //   27: aconst_null
    //   28: putfield 194	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
    //   31: aload 5
    //   33: aload_2
    //   34: putfield 197	com/google/android/gm/provider/Gmail$Attachment:contentType	Ljava/lang/String;
    //   37: aload 5
    //   39: iconst_0
    //   40: putfield 200	com/google/android/gm/provider/Gmail$Attachment:size	I
    //   43: aload 5
    //   45: aload_2
    //   46: putfield 203	com/google/android/gm/provider/Gmail$Attachment:simpleContentType	Ljava/lang/String;
    //   49: aload 5
    //   51: getstatic 209	com/google/android/gm/provider/Gmail$AttachmentOrigin:LOCAL_FILE	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
    //   54: putfield 212	com/google/android/gm/provider/Gmail$Attachment:origin	Lcom/google/android/gm/provider/Gmail$AttachmentOrigin;
    //   57: aload 5
    //   59: aload_1
    //   60: invokestatic 216	com/google/android/gm/provider/Gmail$AttachmentOrigin:localFileExtras	(Landroid/net/Uri;)Ljava/lang/String;
    //   63: putfield 219	com/google/android/gm/provider/Gmail$Attachment:originExtras	Ljava/lang/String;
    //   66: aconst_null
    //   67: astore 6
    //   69: aload 4
    //   71: aload_1
    //   72: iconst_2
    //   73: anewarray 93	java/lang/String
    //   76: dup
    //   77: iconst_0
    //   78: ldc 221
    //   80: aastore
    //   81: dup
    //   82: iconst_1
    //   83: ldc 223
    //   85: aastore
    //   86: aconst_null
    //   87: aconst_null
    //   88: aconst_null
    //   89: invokevirtual 229	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   92: astore 11
    //   94: aload 11
    //   96: astore 6
    //   98: aload 6
    //   100: ifnull +46 -> 146
    //   103: aload 6
    //   105: invokeinterface 234 1 0
    //   110: ifeq +29 -> 139
    //   113: aload 5
    //   115: aload 6
    //   117: iconst_0
    //   118: invokeinterface 238 2 0
    //   123: putfield 194	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
    //   126: aload 5
    //   128: aload 6
    //   130: iconst_1
    //   131: invokeinterface 242 2 0
    //   136: putfield 200	com/google/android/gm/provider/Gmail$Attachment:size	I
    //   139: aload 6
    //   141: invokeinterface 245 1 0
    //   146: aload 5
    //   148: getfield 194	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
    //   151: ifnonnull +12 -> 163
    //   154: aload 5
    //   156: aload_1
    //   157: invokevirtual 250	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   160: putfield 194	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
    //   163: aload_0
    //   164: getfield 112	com/google/android/gm/ComposeArea:mParent	Landroid/app/Activity;
    //   167: invokevirtual 186	android/app/Activity:getContentResolver	()Landroid/content/ContentResolver;
    //   170: ldc 252
    //   172: ldc 22
    //   174: invokestatic 257	com/google/android/gsf/Gservices:getInt	(Landroid/content/ContentResolver;Ljava/lang/String;I)I
    //   177: istore 10
    //   179: aload 5
    //   181: getfield 200	com/google/android/gm/provider/Gmail$Attachment:size	I
    //   184: iconst_m1
    //   185: if_icmpeq +13 -> 198
    //   188: aload 5
    //   190: getfield 200	com/google/android/gm/provider/Gmail$Attachment:size	I
    //   193: iload 10
    //   195: if_icmple +182 -> 377
    //   198: aload_0
    //   199: getfield 112	com/google/android/gm/ComposeArea:mParent	Landroid/app/Activity;
    //   202: ldc_w 258
    //   205: iconst_1
    //   206: invokestatic 264	android/widget/Toast:makeText	(Landroid/content/Context;II)Landroid/widget/Toast;
    //   209: invokevirtual 267	android/widget/Toast:show	()V
    //   212: new 178	com/google/android/gm/ComposeArea$AttachmentFailureException
    //   215: dup
    //   216: aload_0
    //   217: ldc_w 269
    //   220: invokespecial 272	com/google/android/gm/ComposeArea$AttachmentFailureException:<init>	(Lcom/google/android/gm/ComposeArea;Ljava/lang/String;)V
    //   223: athrow
    //   224: astore 12
    //   226: aload 6
    //   228: invokeinterface 245 1 0
    //   233: aload 12
    //   235: athrow
    //   236: astore 7
    //   238: aload_0
    //   239: aload 4
    //   241: aload_1
    //   242: ldc 221
    //   244: invokespecial 276	com/google/android/gm/ComposeArea:getOptionalColumn	(Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;)Landroid/database/Cursor;
    //   247: astore 6
    //   249: aload 6
    //   251: ifnull +26 -> 277
    //   254: aload 6
    //   256: invokeinterface 234 1 0
    //   261: ifeq +16 -> 277
    //   264: aload 5
    //   266: aload 6
    //   268: iconst_0
    //   269: invokeinterface 238 2 0
    //   274: putfield 194	com/google/android/gm/provider/Gmail$Attachment:name	Ljava/lang/String;
    //   277: aload 6
    //   279: ifnull +10 -> 289
    //   282: aload 6
    //   284: invokeinterface 245 1 0
    //   289: aload_0
    //   290: aload 4
    //   292: aload_1
    //   293: ldc 223
    //   295: invokespecial 276	com/google/android/gm/ComposeArea:getOptionalColumn	(Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;)Landroid/database/Cursor;
    //   298: astore 6
    //   300: aload 6
    //   302: ifnull +26 -> 328
    //   305: aload 6
    //   307: invokeinterface 234 1 0
    //   312: ifeq +16 -> 328
    //   315: aload 5
    //   317: aload 6
    //   319: iconst_0
    //   320: invokeinterface 242 2 0
    //   325: putfield 200	com/google/android/gm/provider/Gmail$Attachment:size	I
    //   328: aload 6
    //   330: ifnull -184 -> 146
    //   333: aload 6
    //   335: invokeinterface 245 1 0
    //   340: goto -194 -> 146
    //   343: astore 8
    //   345: aload 6
    //   347: ifnull +10 -> 357
    //   350: aload 6
    //   352: invokeinterface 245 1 0
    //   357: aload 8
    //   359: athrow
    //   360: astore 9
    //   362: aload 6
    //   364: ifnull +10 -> 374
    //   367: aload 6
    //   369: invokeinterface 245 1 0
    //   374: aload 9
    //   376: athrow
    //   377: aload_0
    //   378: getfield 278	com/google/android/gm/ComposeArea:mAttachmentsView	Lcom/google/android/gm/AttachmentsView;
    //   381: invokevirtual 284	com/google/android/gm/AttachmentsView:getTotalAttachmentsSize	()I
    //   384: aload 5
    //   386: getfield 200	com/google/android/gm/provider/Gmail$Attachment:size	I
    //   389: iadd
    //   390: iload 10
    //   392: if_icmple +29 -> 421
    //   395: aload_0
    //   396: getfield 112	com/google/android/gm/ComposeArea:mParent	Landroid/app/Activity;
    //   399: ldc_w 258
    //   402: iconst_1
    //   403: invokestatic 264	android/widget/Toast:makeText	(Landroid/content/Context;II)Landroid/widget/Toast;
    //   406: invokevirtual 267	android/widget/Toast:show	()V
    //   409: new 178	com/google/android/gm/ComposeArea$AttachmentFailureException
    //   412: dup
    //   413: aload_0
    //   414: ldc_w 269
    //   417: invokespecial 272	com/google/android/gm/ComposeArea$AttachmentFailureException:<init>	(Lcom/google/android/gm/ComposeArea;Ljava/lang/String;)V
    //   420: athrow
    //   421: aload_0
    //   422: aload 5
    //   424: invokevirtual 287	com/google/android/gm/ComposeArea:addAttachment	(Lcom/google/android/gm/provider/Gmail$Attachment;)V
    //   427: iload_3
    //   428: ifeq +13 -> 441
    //   431: aload_0
    //   432: getfield 118	com/google/android/gm/ComposeArea:mController	Lcom/google/android/gm/ComposeController;
    //   435: iconst_0
    //   436: invokeinterface 293 2 0
    //   441: return
    //
    // Exception table:
    //   from	to	target	type
    //   103	139	224	finally
    //   69	94	236	android/database/sqlite/SQLiteException
    //   139	146	236	android/database/sqlite/SQLiteException
    //   226	236	236	android/database/sqlite/SQLiteException
    //   238	249	343	finally
    //   254	277	343	finally
    //   289	300	360	finally
    //   305	328	360	finally
  }

  private void addBccAddresses(Collection<String> paramCollection)
  {
    addAddressesToList(paramCollection, this.mBccList);
  }

  private void addCcAddresses(Collection<String> paramCollection)
  {
    addAddressesToList(tokenizeAddressList(paramCollection), this.mCcList);
  }

  private void addCcAddresses(Collection<String> paramCollection1, Collection<String> paramCollection2)
  {
    addCcAddressesToList(tokenizeAddressList(paramCollection1), tokenizeAddressList(paramCollection2), this.mCcList);
  }

  private static void addRecipients(String paramString, Set<String> paramSet, String[] paramArrayOfString)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if (!paramString.equals(Gmail.getEmailFromAddressString(str)))
        paramSet.add(str.replace("\"\"", ""));
    }
  }

  private void addToAddresses(Collection<String> paramCollection)
  {
    addAddressesToList(paramCollection, this.mToList);
  }

  private String cleanUpString(String paramString, boolean paramBoolean)
  {
    if (paramBoolean);
    for (String str = paramString.replace("\"\"", ""); ; str = paramString)
      return TextUtils.htmlEncode(str);
  }

  private void clearComposeArea()
  {
    this.mToList.setText("");
    this.mCcList.setText("");
    this.mBccList.setText("");
    this.mQuotedTextView.setQuotedText("");
  }

  private HashSet<String> convertToHashSet(List<Rfc822Token[]> paramList)
  {
    HashSet localHashSet = new HashSet();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Rfc822Token[] arrayOfRfc822Token = (Rfc822Token[])localIterator.next();
      for (int i = 0; i < arrayOfRfc822Token.length; i++)
        localHashSet.add(arrayOfRfc822Token[i].getAddress());
    }
    return localHashSet;
  }

  private String decode(String paramString)
    throws UnsupportedEncodingException
  {
    return URLDecoder.decode(paramString, "UTF-8");
  }

  private View findViewById(int paramInt)
  {
    return this.mRootView.findViewById(paramInt);
  }

  private void focusSubject()
  {
    this.mSubjectText.requestFocus();
  }

  private LayoutInflater getInflater()
  {
    if (this.mInflater == null)
      this.mInflater = ((LayoutInflater)this.mParent.getSystemService("layout_inflater"));
    return this.mInflater;
  }

  private Cursor getOptionalColumn(ContentResolver paramContentResolver, Uri paramUri, String paramString)
  {
    try
    {
      Cursor localCursor = paramContentResolver.query(paramUri, new String[] { paramString }, null, null, null);
      return localCursor;
    }
    catch (SQLiteException localSQLiteException)
    {
    }
    return null;
  }

  private void initAttachments(List<Gmail.Attachment> paramList)
  {
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Gmail.Attachment localAttachment = (Gmail.Attachment)localIterator.next();
      this.mAttachmentsView.addAttachment(localAttachment);
    }
  }

  private void initBodyFromRefMessage(Gmail.MessageCursor paramMessageCursor, int paramInt, boolean paramBoolean)
  {
    DateFormat localDateFormat = DateFormat.getDateTimeInstance(2, 3);
    Date localDate = new Date(paramMessageCursor.getDateReceivedMs());
    StringBuffer localStringBuffer = new StringBuffer();
    String str2;
    if ((paramInt == 0) || (paramInt == 1))
    {
      localStringBuffer.append("<div class=\"gmail_quote\">");
      String str1 = this.mParent.getString(2131296329);
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = localDateFormat.format(localDate);
      arrayOfObject1[1] = cleanUpString(paramMessageCursor.getFromAddress(), true);
      localStringBuffer.append(String.format(str1, arrayOfObject1));
      localStringBuffer.append("<br type='attribution'>");
      localStringBuffer.append("<blockquote class=\"gmail_quote\" style=\"margin:0 0 0 .8ex;border-left:1px #ccc solid;padding-left:1ex\">");
      localStringBuffer.append(paramMessageCursor.getBody());
      localStringBuffer.append("</blockquote>");
      localStringBuffer.append("</div>");
      str2 = localStringBuffer.toString();
      if (paramBoolean)
        break label355;
    }
    label355: for (boolean bool = true; ; bool = false)
    {
      setQuotedText(str2, bool);
      return;
      if (paramInt != 2)
        break;
      localStringBuffer.append("<div class=\"gmail_quote\">");
      String str3 = this.mParent.getString(2131296330);
      Object[] arrayOfObject2 = new Object[4];
      arrayOfObject2[0] = cleanUpString(paramMessageCursor.getFromAddress(), true);
      arrayOfObject2[1] = localDateFormat.format(localDate);
      arrayOfObject2[2] = cleanUpString(paramMessageCursor.getSubject(), false);
      arrayOfObject2[3] = cleanUpString(TextUtils.join(", ", paramMessageCursor.getToAddresses()), true);
      localStringBuffer.append(String.format(str3, arrayOfObject2));
      String[] arrayOfString = paramMessageCursor.getCcAddresses();
      if (arrayOfString.length > 0)
      {
        String str4 = this.mParent.getString(2131296331);
        Object[] arrayOfObject3 = new Object[1];
        arrayOfObject3[0] = cleanUpString(TextUtils.join(", ", arrayOfString), true);
        localStringBuffer.append(String.format(str4, arrayOfObject3));
      }
      localStringBuffer.append("<br type='attribution'>");
      localStringBuffer.append(paramMessageCursor.getBody());
      localStringBuffer.append("</div>");
      break;
    }
  }

  private MultiAutoCompleteTextView initMultiAutoCompleteTextView(int paramInt1, int paramInt2)
  {
    MultiAutoCompleteTextView localMultiAutoCompleteTextView = (MultiAutoCompleteTextView)findViewById(paramInt1);
    localMultiAutoCompleteTextView.setAdapter(this.mAddressAdapter);
    localMultiAutoCompleteTextView.setTokenizer(new Rfc822Tokenizer());
    localMultiAutoCompleteTextView.setValidator(this.mValidator);
    localMultiAutoCompleteTextView.setFilters(sRecipientFilters);
    return localMultiAutoCompleteTextView;
  }

  private void initRecipientsFromRefMessageCursor(String paramString, Gmail.MessageCursor paramMessageCursor, int paramInt)
  {
    if (paramInt == 2)
      return;
    String str1 = Gmail.getEmailFromAddressString(paramString);
    HashSet localHashSet1 = Sets.newHashSet();
    String[] arrayOfString = paramMessageCursor.getReplyToAddress();
    if ((arrayOfString != null) && (arrayOfString.length != 0))
      localHashSet1.addAll(Arrays.asList(arrayOfString));
    while (true)
    {
      addToAddresses(localHashSet1);
      if (paramInt != 1)
        break;
      HashSet localHashSet2 = Sets.newHashSet();
      addRecipients(str1, localHashSet2, paramMessageCursor.getToAddresses());
      addRecipients(str1, localHashSet2, paramMessageCursor.getCcAddresses());
      addCcAddresses(localHashSet2, localHashSet1);
      return;
      String str2 = paramMessageCursor.getFromAddress();
      if ((str2 != null) && (!str1.equals(Gmail.getEmailFromAddressString(str2))))
        localHashSet1.add(str2);
      else
        localHashSet1.addAll(Arrays.asList(paramMessageCursor.getToAddresses()));
    }
  }

  private boolean isEmpty(EditText paramEditText)
  {
    return TextUtils.isEmpty(paramEditText.toString().trim());
  }

  private void updateAttachments(int paramInt, List<Gmail.Attachment> paramList)
  {
    if (this.mAttachmentsChanged)
      return;
    if (paramInt == 2)
    {
      initAttachments(paramList);
      return;
    }
    ((LinearLayout)findViewById(2131361818)).removeAllViews();
  }

  @VisibleForTesting
  void addAddressesToList(Collection<String> paramCollection, MultiAutoCompleteTextView paramMultiAutoCompleteTextView)
  {
    addAddressesToList(tokenizeAddressList(paramCollection), paramMultiAutoCompleteTextView);
  }

  public void addAttachment(Gmail.Attachment paramAttachment)
  {
    this.mAttachmentsView.addAttachment(paramAttachment);
  }

  public void addAttachmentAndUpdateView(Intent paramIntent)
  {
    Uri localUri;
    if (paramIntent != null)
      localUri = paramIntent.getData();
    while ((localUri != null) && (!TextUtils.isEmpty(localUri.getPath())))
    {
      this.mAttachmentsChanged = true;
      this.mController.onAttachmentsChanged();
      String str = this.mParent.getContentResolver().getType(localUri);
      try
      {
        addAttachment(localUri, str, true);
        return;
        localUri = null;
      }
      catch (AttachmentFailureException localAttachmentFailureException)
      {
        Log.e("Gmail", "Error adding attachment", localAttachmentFailureException);
        return;
      }
    }
    Toast.makeText(this.mParent, 2131296432, 1).show();
  }

  protected void addCcAddressesToList(List<Rfc822Token[]> paramList1, List<Rfc822Token[]> paramList2, MultiAutoCompleteTextView paramMultiAutoCompleteTextView)
  {
    HashSet localHashSet = convertToHashSet(paramList2);
    Iterator localIterator = paramList1.iterator();
    while (localIterator.hasNext())
    {
      Rfc822Token[] arrayOfRfc822Token = (Rfc822Token[])localIterator.next();
      for (int i = 0; i < arrayOfRfc822Token.length; i++)
      {
        String str = arrayOfRfc822Token[i].toString();
        if (!localHashSet.contains(arrayOfRfc822Token[i].getAddress()))
        {
          paramMultiAutoCompleteTextView.append(str);
          paramMultiAutoCompleteTextView.append(", ");
        }
      }
    }
  }

  public void appendSignature(String paramString)
  {
    this.mBodyText.append("\n\n" + paramString);
  }

  public void appendToBody(CharSequence paramCharSequence, boolean paramBoolean)
  {
    Editable localEditable = this.mBodyText.getText();
    if ((localEditable != null) && (localEditable.length() > 0))
    {
      localEditable.append(paramCharSequence);
      return;
    }
    setBody(paramCharSequence, paramBoolean);
  }

  public void checkInvalidEmails(String[] paramArrayOfString, List<String> paramList)
  {
    int i = paramArrayOfString.length;
    for (int j = 0; j < i; j++)
    {
      String str = paramArrayOfString[j];
      if (!this.mValidator.isValid(str))
        paramList.add(str);
    }
  }

  public boolean currentlyShowingCcBcc()
  {
    return findViewById(2131361813).getVisibility() == 0;
  }

  public void dismissAllDialogs()
  {
    if (this.mRecipientErrorDialog != null)
      this.mRecipientErrorDialog.dismiss();
  }

  public void focusBody()
  {
    this.mBodyText.requestFocus();
    this.mBodyText.setSelection(0);
  }

  public String[] getAddressesFromList(MultiAutoCompleteTextView paramMultiAutoCompleteTextView)
  {
    if (paramMultiAutoCompleteTextView == null)
      return new String[0];
    paramMultiAutoCompleteTextView.clearComposingText();
    Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramMultiAutoCompleteTextView.getText());
    int i = arrayOfRfc822Token.length;
    String[] arrayOfString = new String[i];
    for (int j = 0; j < i; j++)
      arrayOfString[j] = arrayOfRfc822Token[j].toString();
    return arrayOfString;
  }

  public List<Gmail.Attachment> getAttachments()
  {
    return this.mAttachmentsView.getAttachments();
  }

  public String[] getBccAddresses()
  {
    return getAddressesFromList(this.mBccList);
  }

  public MultiAutoCompleteTextView getBccList()
  {
    return this.mBccList;
  }

  public Editable getBodyText()
  {
    this.mBodyText.clearComposingText();
    return this.mBodyText.getText();
  }

  public String[] getCcAddresses()
  {
    return getAddressesFromList(this.mCcList);
  }

  public MultiAutoCompleteTextView getCcList()
  {
    return this.mCcList;
  }

  @VisibleForTesting
  protected String getCorrectedSubject(int paramInt, String paramString, Context paramContext)
  {
    String str;
    if (paramInt == -1)
      str = "";
    while (paramString.toLowerCase().startsWith(str.toLowerCase()))
    {
      return paramString;
      if (paramInt == 2)
        str = paramContext.getResources().getString(2131296328);
      else
        str = paramContext.getResources().getString(2131296327);
    }
    return str + " " + paramString;
  }

  public CharSequence getQuotedText()
  {
    return this.mQuotedTextView.getQuotedTextIfIncluded();
  }

  public String getSubject()
  {
    return this.mSubjectText.getText().toString();
  }

  public String[] getToAddresses()
  {
    return getAddressesFromList(this.mToList);
  }

  public MultiAutoCompleteTextView getToList()
  {
    return this.mToList;
  }

  public View getView()
  {
    if (this.mRootView.getParent() != null)
      ((ViewGroup)this.mRootView.getParent()).removeView(this.mRootView);
    return this.mRootView;
  }

  public void hideOrShowCcBcc(boolean paramBoolean)
  {
    this.mShowCcBcc = Boolean.valueOf(paramBoolean);
    int i;
    if (paramBoolean)
    {
      i = 0;
      findViewById(2131361813).setVisibility(i);
      findViewById(2131361815).setVisibility(i);
      if (paramBoolean)
        break label65;
      this.mCcList.setText("");
      this.mBccList.setText("");
    }
    label65: 
    do
    {
      return;
      i = 8;
      break;
      this.mCcList.requestFocus();
    }
    while ((isEmpty(this.mCcList)) || (!isEmpty(this.mBccList)));
    this.mBccList.requestFocus();
  }

  public void initFromCursor(Gmail.MessageCursor paramMessageCursor)
  {
    clearComposeArea();
    setSubject(paramMessageCursor.getSubject());
    String str1 = paramMessageCursor.getBody();
    int i = str1.indexOf("<div class=\"gmail_quote\">");
    boolean bool;
    if (i >= 0)
    {
      setQuotedSectionVisibility(true);
      setBody(Html.fromHtml(str1.substring(0, i)), false);
      String str2 = str1.substring(i);
      if (!paramMessageCursor.getForward())
      {
        bool = true;
        setQuotedText(str2, bool);
      }
    }
    while (true)
    {
      initAttachments(paramMessageCursor.getAttachmentInfos());
      addToAddresses(Arrays.asList(paramMessageCursor.getToAddresses()));
      addCcAddresses(Arrays.asList(paramMessageCursor.getCcAddresses()));
      addBccAddresses(Arrays.asList(paramMessageCursor.getBccAddresses()));
      updateHideOrShowCcBcc();
      return;
      bool = false;
      break;
      setQuotedSectionVisibility(false);
      setBody(Html.fromHtml(str1), false);
    }
  }

  public void initFromExtras(Intent paramIntent)
  {
    Uri localUri1 = paramIntent.getData();
    int j;
    label128: String str4;
    String str5;
    if (localUri1 != null)
      if ("mailto".equals(localUri1.getScheme()))
      {
        initFromMailTo(localUri1.toString());
        String[] arrayOfString1 = paramIntent.getStringArrayExtra("android.intent.extra.EMAIL");
        if (arrayOfString1 != null)
          addToAddresses(Arrays.asList(arrayOfString1));
        String[] arrayOfString2 = paramIntent.getStringArrayExtra("android.intent.extra.CC");
        if (arrayOfString2 != null)
          addCcAddresses(Arrays.asList(arrayOfString2));
        String[] arrayOfString3 = paramIntent.getStringArrayExtra("android.intent.extra.BCC");
        if (arrayOfString3 != null)
          addBccAddresses(Arrays.asList(arrayOfString3));
        String str1 = paramIntent.getStringExtra("android.intent.extra.SUBJECT");
        if (str1 != null)
          setSubject(str1);
        String[] arrayOfString4 = ALL_EXTRAS;
        int i = arrayOfString4.length;
        j = 0;
        if (j >= i)
          break label342;
        str4 = arrayOfString4[j];
        if (paramIntent.hasExtra(str4))
        {
          str5 = paramIntent.getStringExtra(str4);
          if (!"to".equals(str4))
            break label247;
          addToAddresses(Arrays.asList(str5.split(",")));
        }
      }
    while (true)
    {
      j++;
      break label128;
      if ("gmailfrom".equals(localUri1.getScheme()))
        break;
      String str6 = localUri1.getSchemeSpecificPart();
      if (str6 == null)
        break;
      this.mToList.setText("");
      addToAddresses(Arrays.asList(str6.split(",")));
      break;
      clearComposeArea();
      break;
      label247: if ("cc".equals(str4))
        addCcAddresses(Arrays.asList(str5.split(",")));
      else if ("bcc".equals(str4))
        addBccAddresses(Arrays.asList(str5.split(",")));
      else if ("subject".equals(str4))
        setSubject(str5);
      else if ("body".equals(str4))
        setBody(str5, true);
    }
    label342: Bundle localBundle = paramIntent.getExtras();
    if (localBundle != null)
    {
      String str2 = paramIntent.getAction();
      CharSequence localCharSequence = localBundle.getCharSequence("android.intent.extra.TEXT");
      if (localCharSequence != null)
        setBody(localCharSequence, true);
      if (!this.mAttachmentsChanged)
      {
        if (localBundle.containsKey("attachments"))
        {
          String[] arrayOfString5 = (String[])localBundle.getSerializable("attachments");
          int k = arrayOfString5.length;
          int m = 0;
          while (m < k)
          {
            Uri localUri3 = Uri.parse(arrayOfString5[m]);
            String str3 = this.mParent.getContentResolver().getType(localUri3);
            try
            {
              addAttachment(localUri3, str3, false);
              m++;
            }
            catch (AttachmentFailureException localAttachmentFailureException3)
            {
              Log.e("Gmail", "Error adding attachment", localAttachmentFailureException3);
              this.mParent.finish();
              return;
            }
          }
          this.mController.doSave(false);
        }
        Uri localUri2;
        if ((("android.intent.action.SEND".equals(str2)) || ("com.google.android.gm.action.AUTO_SEND".equals(str2))) && (localBundle.containsKey("android.intent.extra.STREAM")))
          localUri2 = (Uri)localBundle.getParcelable("android.intent.extra.STREAM");
        try
        {
          addAttachment(localUri2, paramIntent.getType(), true);
          if ((!"android.intent.action.SEND_MULTIPLE".equals(str2)) || (!localBundle.containsKey("android.intent.extra.STREAM")))
            break label672;
          Iterator localIterator = localBundle.getParcelableArrayList("android.intent.extra.STREAM").iterator();
          while (true)
            if (localIterator.hasNext())
            {
              Parcelable localParcelable = (Parcelable)localIterator.next();
              try
              {
                addAttachment((Uri)localParcelable, paramIntent.getType(), false);
              }
              catch (AttachmentFailureException localAttachmentFailureException1)
              {
                this.mParent.finish();
                return;
              }
            }
        }
        catch (AttachmentFailureException localAttachmentFailureException2)
        {
          Log.e("Gmail", "Error adding attachment", localAttachmentFailureException2);
          this.mParent.finish();
          return;
        }
        this.mController.doSave(false);
      }
    }
    label672: updateHideOrShowCcBcc();
  }

  public void initFromMailTo(String paramString)
  {
    clearComposeArea();
    Uri localUri = Uri.parse("foo://" + paramString);
    int i = paramString.indexOf("?");
    int j = 1 + "mailto".length();
    if (i == -1);
    try
    {
      String str;
      for (Object localObject = decode(paramString.substring(j)); ; localObject = str)
      {
        addToAddresses(Arrays.asList(((String)localObject).split(",")));
        List localList1 = localUri.getQueryParameters("cc");
        addCcAddresses(Arrays.asList(localList1.toArray(new String[localList1.size()])));
        List localList2 = localUri.getQueryParameters("to");
        addToAddresses(Arrays.asList(localList2.toArray(new String[localList2.size()])));
        List localList3 = localUri.getQueryParameters("bcc");
        addBccAddresses(Arrays.asList(localList3.toArray(new String[localList3.size()])));
        List localList4 = localUri.getQueryParameters("subject");
        if (localList4.size() > 0)
          setSubject((String)localList4.get(0));
        List localList5 = localUri.getQueryParameters("body");
        if (localList5.size() > 0)
          setBody((CharSequence)localList5.get(0), true);
        updateHideOrShowCcBcc();
        return;
        str = decode(paramString.substring(j, i));
      }
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      while (true)
        Log.e("Gmail", localUnsupportedEncodingException.getMessage() + " while decoding '" + paramString + "'");
    }
  }

  public void initFromRefMessage(Gmail.MessageCursor paramMessageCursor, int paramInt, boolean paramBoolean)
  {
    clearComposeArea();
    setQuotedSectionVisibility(true);
    initRecipientsFromRefMessageCursor(this.mAccount, paramMessageCursor, paramInt);
    setSubject(paramMessageCursor, paramInt);
    initBodyFromRefMessage(paramMessageCursor, paramInt, paramBoolean);
    if ((paramInt == 2) || (this.mAttachmentsChanged))
      updateAttachments(paramInt, paramMessageCursor.getAttachmentInfos());
    while (true)
    {
      updateHideOrShowCcBcc();
      return;
      ((LinearLayout)findViewById(2131361818)).removeAllViews();
    }
  }

  public boolean isBlank()
  {
    return (this.mSubjectText.getText().length() == 0) && (this.mBodyText.getText().length() == 0) && (this.mToList.length() == 0) && (this.mCcList.length() == 0) && (this.mBccList.length() == 0) && (this.mAttachmentsView.getAttachments().size() == 0);
  }

  public boolean isBodyEmpty()
  {
    return !this.mQuotedTextView.isTextIncluded();
  }

  public boolean isSubjectEmpty()
  {
    return TextUtils.getTrimmedLength(this.mSubjectText.getText()) == 0;
  }

  public void onAttachmentAdded()
  {
  }

  public void onAttachmentDeleted()
  {
    this.mAttachmentsChanged = true;
    this.mController.onAttachmentsChanged();
  }

  public void onRespondInline(String paramString)
  {
    appendToBody(paramString, false);
  }

  public void onRestoreInstanceState(Bundle paramBundle)
  {
    setQuotedSectionVisibility(paramBundle.getBoolean("showQuotedText", true));
  }

  public Bundle onSaveInstanceState(Bundle paramBundle)
  {
    boolean bool1;
    if (findViewById(2131361813).getVisibility() == 0)
    {
      bool1 = true;
      paramBundle.putBoolean("showCcBcc", bool1);
      if (this.mQuotedTextView.getVisibility() != 0)
        break label49;
    }
    label49: for (boolean bool2 = true; ; bool2 = false)
    {
      paramBundle.putBoolean("showQuotedText", bool2);
      return paramBundle;
      bool1 = false;
      break;
    }
  }

  public void onShowHideQuotedText(boolean paramBoolean)
  {
    this.mController.onUiChanged();
  }

  public void renderComposeArea()
  {
    this.mRootView = getInflater().inflate(2130903046, null);
    this.mBodyText = ((EditText)findViewById(2131361819));
    this.mBodyText.setImeOptions(1073741830);
    this.mBodyText.addTextChangedListener(this.mController);
    this.mQuotedTextView = ((QuotedTextView)findViewById(2131361820));
    this.mQuotedTextView.setShowHideListener(this);
    this.mQuotedTextView.setRespondInlineListener(this);
    this.mAttachmentsView = ((AttachmentsView)findViewById(2131361818));
    this.mAttachmentsView.setAttachmentChangesListener(this);
    this.mSubjectText = ((EditText)findViewById(2131361817));
    this.mSubjectText.addTextChangedListener(this.mController);
    this.mAddressAdapter = new EmailAddressAdapter(this.mParent);
    this.mValidator = new GmailRfc822Validator(this.mAccount.substring(1 + this.mAccount.indexOf("@")));
    this.mToList = initMultiAutoCompleteTextView(2131361812, 2131296460);
    this.mCcList = initMultiAutoCompleteTextView(2131361814, 2131296461);
    this.mBccList = initMultiAutoCompleteTextView(2131361816, 2131296458);
    this.mToList.addTextChangedListener(this.mController);
    this.mCcList.addTextChangedListener(this.mController);
    this.mBccList.addTextChangedListener(this.mController);
  }

  public void requestFocus()
  {
    if (this.mToList.length() == 0)
    {
      this.mToList.requestFocus();
      return;
    }
    if (isSubjectEmpty())
    {
      focusSubject();
      return;
    }
    focusBody();
  }

  public void setBody(CharSequence paramCharSequence, boolean paramBoolean)
  {
    this.mBodyText.setText(paramCharSequence);
    if (paramBoolean)
    {
      String str = this.mPrefs.getSignature(this.mParent);
      if (!Utils.isStringEmpty(str))
        appendSignature(str);
    }
  }

  public void setQuotedSectionVisibility(boolean paramBoolean)
  {
    QuotedTextView localQuotedTextView = this.mQuotedTextView;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localQuotedTextView.setVisibility(i);
      return;
    }
  }

  public void setQuotedText(CharSequence paramCharSequence, boolean paramBoolean)
  {
    this.mQuotedTextView.setQuotedText(paramCharSequence);
    this.mQuotedTextView.allowQuotedText(paramBoolean);
    this.mQuotedTextView.allowRespondInline(true);
  }

  public void setSubject(Gmail.MessageCursor paramMessageCursor, int paramInt)
  {
    setSubject(getCorrectedSubject(paramInt, paramMessageCursor.getSubject(), this.mParent));
  }

  public void setSubject(String paramString)
  {
    this.mSubjectText.setText(paramString);
  }

  public void showRecipientErrorDialog(String paramString)
  {
    if (this.mRecipientErrorDialog != null)
      this.mRecipientErrorDialog.dismiss();
    this.mRecipientErrorDialog = new AlertDialog.Builder(this.mParent).setMessage(paramString).setTitle(2131296497).setIcon(17301543).setPositiveButton(2131296496, new DialogInterface.OnClickListener()
    {
      public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
      {
        ComposeArea.this.mToList.requestFocus();
        ComposeArea.access$002(ComposeArea.this, null);
      }
    }).show();
  }

  protected List<Rfc822Token[]> tokenizeAddressList(Collection<String> paramCollection)
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = paramCollection.iterator();
    while (localIterator.hasNext())
      localArrayList.add(Rfc822Tokenizer.tokenize((String)localIterator.next()));
    return localArrayList;
  }

  public void updateFromCursor(Gmail.MessageCursor paramMessageCursor1, Gmail.MessageCursor paramMessageCursor2, int paramInt)
  {
    clearComposeArea();
    updateAttachments(paramInt, paramMessageCursor1.getAttachmentInfos());
    if ((paramMessageCursor2 != null) && (paramMessageCursor2.next()))
      if (paramInt == 0)
      {
        String[] arrayOfString = new String[1];
        arrayOfString[0] = paramMessageCursor2.getFromAddress();
        addToAddresses(Arrays.asList(arrayOfString));
        setSubject(paramMessageCursor2, paramInt);
      }
    while (true)
    {
      updateHideOrShowCcBcc();
      return;
      if (paramInt != 1)
        break;
      addToAddresses(Arrays.asList(paramMessageCursor2.getToAddresses()));
      addAddressToList(paramMessageCursor2.getFromAddress(), this.mToList);
      addCcAddresses(Arrays.asList(paramMessageCursor2.getCcAddresses()));
      addBccAddresses(Arrays.asList(paramMessageCursor2.getBccAddresses()));
      break;
      addToAddresses(Arrays.asList(paramMessageCursor1.getToAddresses()));
      addCcAddresses(Arrays.asList(paramMessageCursor1.getCcAddresses()));
      addBccAddresses(Arrays.asList(paramMessageCursor1.getBccAddresses()));
      setSubject(paramMessageCursor1, paramInt);
    }
  }

  public void updateHideOrShowCcBcc()
  {
    if (this.mShowCcBcc != null)
    {
      if (this.mShowCcBcc.booleanValue());
      for (i = 0; ; i = 8)
      {
        findViewById(2131361813).setVisibility(i);
        findViewById(2131361815).setVisibility(i);
        ComposeController localComposeController = this.mController;
        boolean bool = false;
        if (i == 0)
          bool = true;
        localComposeController.updateHideOrShowCcBcc(bool);
        return;
      }
    }
    if ((this.mCcList.length() != 0) || (this.mBccList.length() != 0));
    for (int i = 0; ; i = 8)
      break;
  }

  class AttachmentFailureException extends Exception
  {
    public AttachmentFailureException(String arg2)
    {
      super();
    }

    public AttachmentFailureException(String paramThrowable, Throwable arg3)
    {
      super(localThrowable);
    }
  }

  static class GmailRfc822Validator extends Rfc822Validator
  {
    private static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[^\\s@]+@([^\\s@\\.]+\\.)+[a-zA-z][a-zA-Z][a-zA-Z]*");

    public GmailRfc822Validator(String paramString)
    {
      super();
    }

    @Deprecated
    public boolean isValid(CharSequence paramCharSequence)
    {
      Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramCharSequence);
      return (arrayOfRfc822Token.length == 1) && (EMAIL_ADDRESS_PATTERN.matcher(arrayOfRfc822Token[0].getAddress()).matches());
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.ComposeArea
 * JD-Core Version:    0.6.2
 */