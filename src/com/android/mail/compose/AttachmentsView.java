package com.android.mail.compose;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.android.mail.providers.Account;
import com.android.mail.providers.Attachment;
import com.android.mail.providers.Settings;
import com.android.mail.ui.AttachmentTile;
import com.android.mail.ui.AttachmentTile.AttachmentPreview;
import com.android.mail.ui.AttachmentTileGrid;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.google.common.collect.Lists;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

class AttachmentsView extends LinearLayout
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private LinearLayout mAttachmentLayout;
  private ArrayList<Attachment> mAttachments = Lists.newArrayList();
  private AttachmentAddedOrDeletedListener mChangeListener;
  private AttachmentTileGrid mTileGrid;

  public AttachmentsView(Context paramContext)
  {
    this(paramContext, null);
  }

  public AttachmentsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
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

  public long addAttachment(Account paramAccount, Uri paramUri)
    throws AttachmentsView.AttachmentFailureException
  {
    return addAttachment(paramAccount, generateLocalAttachment(paramUri));
  }

  public long addAttachment(Account paramAccount, Attachment paramAttachment)
    throws AttachmentsView.AttachmentFailureException
  {
    int i = paramAccount.settings.getMaxAttachmentSize();
    if ((paramAttachment.size == -1) || (paramAttachment.size > i))
      throw new AttachmentFailureException("Attachment too large to attach", 2131427373);
    if (getTotalAttachmentsSize() + paramAttachment.size > i)
      throw new AttachmentFailureException("Attachment too large to attach", 2131427375);
    addAttachment(paramAttachment);
    return paramAttachment.size;
  }

  public void addAttachment(final Attachment paramAttachment)
  {
    if (!isShown())
      setVisibility(0);
    this.mAttachments.add(paramAttachment);
    expandView();
    if (AttachmentTile.isTiledAttachment(paramAttachment))
    {
      final ComposeAttachmentTile localComposeAttachmentTile = this.mTileGrid.addComposeTileFromAttachment(paramAttachment);
      localComposeAttachmentTile.addDeleteListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          AttachmentsView.this.deleteAttachment(localComposeAttachmentTile, paramAttachment);
        }
      });
    }
    while (true)
    {
      if (this.mChangeListener != null)
        this.mChangeListener.onAttachmentAdded();
      return;
      final AttachmentComposeView localAttachmentComposeView = new AttachmentComposeView(getContext(), paramAttachment);
      localAttachmentComposeView.addDeleteListener(new View.OnClickListener()
      {
        public void onClick(View paramAnonymousView)
        {
          AttachmentsView.this.deleteAttachment(localAttachmentComposeView, paramAttachment);
        }
      });
      this.mAttachmentLayout.addView(localAttachmentComposeView, new LinearLayout.LayoutParams(-1, -1));
    }
  }

  public void deleteAllAttachments()
  {
    this.mAttachments.clear();
    this.mTileGrid.removeAllViews();
    this.mAttachmentLayout.removeAllViews();
    setVisibility(8);
  }

  protected void deleteAttachment(View paramView, Attachment paramAttachment)
  {
    this.mAttachments.remove(paramAttachment);
    ((ViewGroup)paramView.getParent()).removeView(paramView);
    if (this.mChangeListener != null)
      this.mChangeListener.onAttachmentDeleted();
  }

  public void expandView()
  {
    this.mTileGrid.setVisibility(0);
    this.mAttachmentLayout.setVisibility(0);
    InputMethodManager localInputMethodManager = (InputMethodManager)getContext().getSystemService("input_method");
    if (localInputMethodManager != null)
      localInputMethodManager.hideSoftInputFromWindow(getWindowToken(), 0);
  }

  public void focusLastAttachment()
  {
    View localView;
    if (AttachmentTile.isTiledAttachment((Attachment)this.mAttachments.get(-1 + this.mAttachments.size())))
    {
      int j = -1 + this.mTileGrid.getChildCount();
      localView = null;
      if (j > 0)
        localView = this.mTileGrid.getChildAt(j);
    }
    while (true)
    {
      if (localView != null)
        localView.requestFocus();
      return;
      int i = -1 + this.mAttachmentLayout.getChildCount();
      localView = null;
      if (i > 0)
        localView = this.mAttachmentLayout.getChildAt(i);
    }
  }

  // ERROR //
  public Attachment generateLocalAttachment(Uri paramUri)
    throws AttachmentsView.AttachmentFailureException
  {
    // Byte code:
    //   0: aload_0
    //   1: invokevirtual 149	com/android/mail/compose/AttachmentsView:getContext	()Landroid/content/Context;
    //   4: invokevirtual 242	android/content/Context:getContentResolver	()Landroid/content/ContentResolver;
    //   7: astore_2
    //   8: aload_2
    //   9: aload_1
    //   10: invokevirtual 246	android/content/ContentResolver:getType	(Landroid/net/Uri;)Ljava/lang/String;
    //   13: astore_3
    //   14: aload_1
    //   15: ifnull +13 -> 28
    //   18: aload_1
    //   19: invokevirtual 251	android/net/Uri:getPath	()Ljava/lang/String;
    //   22: invokestatic 257	android/text/TextUtils:isEmpty	(Ljava/lang/CharSequence;)Z
    //   25: ifeq +14 -> 39
    //   28: new 55	com/android/mail/compose/AttachmentsView$AttachmentFailureException
    //   31: dup
    //   32: ldc_w 259
    //   35: invokespecial 262	com/android/mail/compose/AttachmentsView$AttachmentFailureException:<init>	(Ljava/lang/String;)V
    //   38: athrow
    //   39: aload_3
    //   40: ifnonnull +7 -> 47
    //   43: ldc_w 264
    //   46: astore_3
    //   47: new 76	com/android/mail/providers/Attachment
    //   50: dup
    //   51: invokespecial 266	com/android/mail/providers/Attachment:<init>	()V
    //   54: astore 4
    //   56: aload 4
    //   58: aconst_null
    //   59: putfield 270	com/android/mail/providers/Attachment:uri	Landroid/net/Uri;
    //   62: aload 4
    //   64: aconst_null
    //   65: putfield 273	com/android/mail/providers/Attachment:name	Ljava/lang/String;
    //   68: aload 4
    //   70: aload_3
    //   71: putfield 276	com/android/mail/providers/Attachment:contentType	Ljava/lang/String;
    //   74: aload 4
    //   76: iconst_0
    //   77: putfield 80	com/android/mail/providers/Attachment:size	I
    //   80: aload 4
    //   82: aload_1
    //   83: putfield 279	com/android/mail/providers/Attachment:contentUri	Landroid/net/Uri;
    //   86: aconst_null
    //   87: astore 5
    //   89: aload_2
    //   90: aload_1
    //   91: iconst_2
    //   92: anewarray 45	java/lang/String
    //   95: dup
    //   96: iconst_0
    //   97: ldc_w 281
    //   100: aastore
    //   101: dup
    //   102: iconst_1
    //   103: ldc_w 283
    //   106: aastore
    //   107: aconst_null
    //   108: aconst_null
    //   109: aconst_null
    //   110: invokevirtual 51	android/content/ContentResolver:query	(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
    //   113: astore 10
    //   115: aload 10
    //   117: astore 5
    //   119: aload 5
    //   121: ifnull +46 -> 167
    //   124: aload 5
    //   126: invokeinterface 288 1 0
    //   131: ifeq +29 -> 160
    //   134: aload 4
    //   136: aload 5
    //   138: iconst_0
    //   139: invokeinterface 292 2 0
    //   144: putfield 273	com/android/mail/providers/Attachment:name	Ljava/lang/String;
    //   147: aload 4
    //   149: aload 5
    //   151: iconst_1
    //   152: invokeinterface 296 2 0
    //   157: putfield 80	com/android/mail/providers/Attachment:size	I
    //   160: aload 5
    //   162: invokeinterface 299 1 0
    //   167: aload 4
    //   169: getfield 273	com/android/mail/providers/Attachment:name	Ljava/lang/String;
    //   172: ifnonnull +12 -> 184
    //   175: aload 4
    //   177: aload_1
    //   178: invokevirtual 302	android/net/Uri:getLastPathSegment	()Ljava/lang/String;
    //   181: putfield 273	com/android/mail/providers/Attachment:name	Ljava/lang/String;
    //   184: aload 4
    //   186: areturn
    //   187: astore 11
    //   189: aload 5
    //   191: invokeinterface 299 1 0
    //   196: aload 11
    //   198: athrow
    //   199: astore 7
    //   201: aload_0
    //   202: aload_2
    //   203: aload_1
    //   204: ldc_w 281
    //   207: invokespecial 304	com/android/mail/compose/AttachmentsView:getOptionalColumn	(Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;)Landroid/database/Cursor;
    //   210: astore 5
    //   212: aload 5
    //   214: ifnull +26 -> 240
    //   217: aload 5
    //   219: invokeinterface 288 1 0
    //   224: ifeq +16 -> 240
    //   227: aload 4
    //   229: aload 5
    //   231: iconst_0
    //   232: invokeinterface 292 2 0
    //   237: putfield 273	com/android/mail/providers/Attachment:name	Ljava/lang/String;
    //   240: aload 5
    //   242: ifnull +10 -> 252
    //   245: aload 5
    //   247: invokeinterface 299 1 0
    //   252: aload_0
    //   253: aload_2
    //   254: aload_1
    //   255: ldc_w 283
    //   258: invokespecial 304	com/android/mail/compose/AttachmentsView:getOptionalColumn	(Landroid/content/ContentResolver;Landroid/net/Uri;Ljava/lang/String;)Landroid/database/Cursor;
    //   261: astore 5
    //   263: aload 5
    //   265: ifnull +58 -> 323
    //   268: aload 5
    //   270: invokeinterface 288 1 0
    //   275: ifeq +48 -> 323
    //   278: aload 4
    //   280: aload 5
    //   282: iconst_0
    //   283: invokeinterface 296 2 0
    //   288: putfield 80	com/android/mail/providers/Attachment:size	I
    //   291: aload 5
    //   293: ifnull -126 -> 167
    //   296: aload 5
    //   298: invokeinterface 299 1 0
    //   303: goto -136 -> 167
    //   306: astore 8
    //   308: aload 5
    //   310: ifnull +10 -> 320
    //   313: aload 5
    //   315: invokeinterface 299 1 0
    //   320: aload 8
    //   322: athrow
    //   323: aload 4
    //   325: aload_0
    //   326: aload_1
    //   327: aload_2
    //   328: invokevirtual 308	com/android/mail/compose/AttachmentsView:getSizeFromFile	(Landroid/net/Uri;Landroid/content/ContentResolver;)I
    //   331: putfield 80	com/android/mail/providers/Attachment:size	I
    //   334: goto -43 -> 291
    //   337: astore 9
    //   339: aload 5
    //   341: ifnull +10 -> 351
    //   344: aload 5
    //   346: invokeinterface 299 1 0
    //   351: aload 9
    //   353: athrow
    //   354: astore 6
    //   356: new 55	com/android/mail/compose/AttachmentsView$AttachmentFailureException
    //   359: dup
    //   360: ldc_w 310
    //   363: aload 6
    //   365: invokespecial 313	com/android/mail/compose/AttachmentsView$AttachmentFailureException:<init>	(Ljava/lang/String;Ljava/lang/Throwable;)V
    //   368: athrow
    //
    // Exception table:
    //   from	to	target	type
    //   124	160	187	finally
    //   89	115	199	android/database/sqlite/SQLiteException
    //   160	167	199	android/database/sqlite/SQLiteException
    //   189	199	199	android/database/sqlite/SQLiteException
    //   201	212	306	finally
    //   217	240	306	finally
    //   252	263	337	finally
    //   268	291	337	finally
    //   323	334	337	finally
    //   89	115	354	java/lang/SecurityException
    //   160	167	354	java/lang/SecurityException
    //   189	199	354	java/lang/SecurityException
  }

  public ArrayList<AttachmentTile.AttachmentPreview> getAttachmentPreviews()
  {
    return this.mTileGrid.getAttachmentPreviews();
  }

  public ArrayList<Attachment> getAttachments()
  {
    return this.mAttachments;
  }

  protected int getSizeFromFile(Uri paramUri, ContentResolver paramContentResolver)
  {
    int i = -1;
    ParcelFileDescriptor localParcelFileDescriptor = null;
    try
    {
      localParcelFileDescriptor = paramContentResolver.openFileDescriptor(paramUri, "r");
      long l = localParcelFileDescriptor.getStatSize();
      i = (int)l;
      if (localParcelFileDescriptor != null);
      try
      {
        localParcelFileDescriptor.close();
        return i;
      }
      catch (IOException localIOException3)
      {
        LogUtils.w(LOG_TAG, "Error closing file opened to obtain size.", new Object[0]);
        return i;
      }
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      do
        LogUtils.w(LOG_TAG, "Error opening file to obtain size.", new Object[0]);
      while (localParcelFileDescriptor == null);
      try
      {
        localParcelFileDescriptor.close();
        return i;
      }
      catch (IOException localIOException2)
      {
        LogUtils.w(LOG_TAG, "Error closing file opened to obtain size.", new Object[0]);
        return i;
      }
    }
    finally
    {
      if (localParcelFileDescriptor == null);
    }
    try
    {
      localParcelFileDescriptor.close();
      throw localObject;
    }
    catch (IOException localIOException1)
    {
      while (true)
        LogUtils.w(LOG_TAG, "Error closing file opened to obtain size.", new Object[0]);
    }
  }

  public long getTotalAttachmentsSize()
  {
    long l = 0L;
    Iterator localIterator = this.mAttachments.iterator();
    while (localIterator.hasNext())
      l += ((Attachment)localIterator.next()).size;
    return l;
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTileGrid = ((AttachmentTileGrid)findViewById(2131689544));
    this.mAttachmentLayout = ((LinearLayout)findViewById(2131689545));
  }

  public void setAttachmentChangesListener(AttachmentAddedOrDeletedListener paramAttachmentAddedOrDeletedListener)
  {
    this.mChangeListener = paramAttachmentAddedOrDeletedListener;
  }

  public void setAttachmentPreviews(ArrayList<AttachmentTile.AttachmentPreview> paramArrayList)
  {
    this.mTileGrid.setAttachmentPreviews(paramArrayList);
  }

  public static abstract interface AttachmentAddedOrDeletedListener
  {
    public abstract void onAttachmentAdded();

    public abstract void onAttachmentDeleted();
  }

  static class AttachmentFailureException extends Exception
  {
    private static final long serialVersionUID = 1L;
    private final int errorRes;

    public AttachmentFailureException(String paramString)
    {
      super();
      this.errorRes = 2131427376;
    }

    public AttachmentFailureException(String paramString, int paramInt)
    {
      super();
      this.errorRes = paramInt;
    }

    public AttachmentFailureException(String paramString, Throwable paramThrowable)
    {
      super(paramThrowable);
      this.errorRes = 2131427376;
    }

    public int getErrorRes()
    {
      return this.errorRes;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.compose.AttachmentsView
 * JD-Core Version:    0.6.2
 */