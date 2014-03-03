package com.android.mail.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.widget.RemoteViews;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.ui.FolderDisplayer;
import java.util.Iterator;
import java.util.SortedSet;

public class WidgetConversationViewBuilder
{
  private static Bitmap ATTACHMENT;
  private static int DATE_FONT_SIZE;
  private static int DATE_TEXT_COLOR;
  private static int SUBJECT_FONT_SIZE;
  private static int SUBJECT_TEXT_COLOR_READ;
  private static int SUBJECT_TEXT_COLOR_UNREAD;
  private final Context mContext;
  private WidgetFolderDisplayer mFolderDisplayer;

  public WidgetConversationViewBuilder(Context paramContext, Account paramAccount)
  {
    this.mContext = paramContext;
    Resources localResources = paramContext.getResources();
    DATE_FONT_SIZE = localResources.getDimensionPixelSize(2131361871);
    SUBJECT_FONT_SIZE = localResources.getDimensionPixelSize(2131361870);
    SUBJECT_TEXT_COLOR_READ = localResources.getColor(2131230728);
    SUBJECT_TEXT_COLOR_UNREAD = localResources.getColor(2131230725);
    DATE_TEXT_COLOR = localResources.getColor(2131230731);
    ATTACHMENT = BitmapFactory.decodeResource(localResources, 2130837560);
  }

  private CharSequence addStyle(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder(paramCharSequence);
    localSpannableStringBuilder.setSpan(new AbsoluteSizeSpan(paramInt1), 0, paramCharSequence.length(), 33);
    if (paramInt2 != 0)
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(paramInt2), 0, paramCharSequence.length(), 33);
    return localSpannableStringBuilder;
  }

  public RemoteViews getStyledView(CharSequence paramCharSequence1, CharSequence paramCharSequence2, Conversation paramConversation, Folder paramFolder, SpannableStringBuilder paramSpannableStringBuilder, String paramString)
  {
    int i;
    int j;
    label45: RemoteViews localRemoteViews;
    if (!paramConversation.read)
    {
      i = 1;
      String str = paramConversation.getSnippet();
      boolean bool = paramConversation.hasAttachments;
      CharSequence localCharSequence1 = addStyle(paramCharSequence2, DATE_FONT_SIZE, DATE_TEXT_COLOR);
      if (i == 0)
        break label278;
      j = SUBJECT_TEXT_COLOR_UNREAD;
      SpannableStringBuilder localSpannableStringBuilder = Conversation.getSubjectAndSnippetForDisplay(this.mContext, paramString, str);
      if (i != 0)
        localSpannableStringBuilder.setSpan(new StyleSpan(1), 0, paramString.length(), 33);
      localSpannableStringBuilder.setSpan(new ForegroundColorSpan(j), 0, localSpannableStringBuilder.length(), 33);
      CharSequence localCharSequence2 = addStyle(localSpannableStringBuilder, SUBJECT_FONT_SIZE, 0);
      Bitmap localBitmap = null;
      if (bool)
        localBitmap = ATTACHMENT;
      localRemoteViews = new RemoteViews(this.mContext.getPackageName(), 2130968686);
      localRemoteViews.setTextViewText(2131689739, paramSpannableStringBuilder);
      localRemoteViews.setTextViewText(2131689738, localCharSequence1);
      localRemoteViews.setTextViewText(2131689740, localCharSequence2);
      if (localBitmap == null)
        break label286;
      localRemoteViews.setViewVisibility(2131689737, 0);
      localRemoteViews.setImageViewBitmap(2131689737, localBitmap);
      label198: if (i == 0)
        break label298;
      localRemoteViews.setViewVisibility(2131689731, 0);
      localRemoteViews.setViewVisibility(2131689732, 8);
    }
    while (true)
    {
      if (this.mContext.getResources().getBoolean(2131623940))
      {
        this.mFolderDisplayer = new WidgetFolderDisplayer(this.mContext);
        this.mFolderDisplayer.loadConversationFolders(paramConversation, paramFolder);
        this.mFolderDisplayer.displayFolders(localRemoteViews);
      }
      return localRemoteViews;
      i = 0;
      break;
      label278: j = SUBJECT_TEXT_COLOR_READ;
      break label45;
      label286: localRemoteViews.setViewVisibility(2131689737, 8);
      break label198;
      label298: localRemoteViews.setViewVisibility(2131689731, 8);
      localRemoteViews.setViewVisibility(2131689732, 0);
    }
  }

  protected static class WidgetFolderDisplayer extends FolderDisplayer
  {
    public WidgetFolderDisplayer(Context paramContext)
    {
      super();
    }

    private int getFolderViewId(int paramInt)
    {
      switch (paramInt)
      {
      default:
        return 0;
      case 0:
        return 2131689733;
      case 1:
        return 2131689734;
      case 2:
      }
      return 2131689735;
    }

    public void displayFolders(RemoteViews paramRemoteViews)
    {
      int i = 0;
      Iterator localIterator = this.mFoldersSortedSet.iterator();
      do
      {
        Folder localFolder;
        int k;
        do
        {
          if (!localIterator.hasNext())
            break;
          localFolder = (Folder)localIterator.next();
          k = getFolderViewId(i);
        }
        while (k == 0);
        paramRemoteViews.setViewVisibility(k, 0);
        int[] arrayOfInt = new int[1];
        arrayOfInt[0] = localFolder.getBackgroundColor(this.mDefaultBgColor);
        paramRemoteViews.setImageViewBitmap(k, Bitmap.createBitmap(arrayOfInt, 1, 1, Bitmap.Config.RGB_565));
        i++;
      }
      while (i != 3);
      for (int j = i; j < 3; j++)
        paramRemoteViews.setViewVisibility(getFolderViewId(j), 8);
    }

    public void loadConversationFolders(Conversation paramConversation, Folder paramFolder)
    {
      super.loadConversationFolders(paramConversation, paramFolder);
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.widget.WidgetConversationViewBuilder
 * JD-Core Version:    0.6.2
 */