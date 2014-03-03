package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextPaint;
import android.util.DisplayMetrics;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewParent;
import android.widget.TextView;
import com.android.mail.ui.ViewMode;

public class ConversationItemViewCoordinates
{
  private static int COLOR_BLOCK_HEIGHT;
  private static int COLOR_BLOCK_WIDTH;
  private static int FOLDER_CELL_WIDTH;
  private static int TOTAL_FOLDER_WIDTH = -1;
  private static int TOTAL_FOLDER_WIDTH_WIDE = -1;
  private static SparseArray<ConversationItemViewCoordinates> mCache;
  private static int[] sConversationHeights;
  private static TextPaint sPaint;
  int checkmarkX;
  int checkmarkY;
  int dateAscent;
  int dateFontSize;
  int dateXEnd;
  int dateY;
  int foldersAscent;
  int foldersFontSize;
  int foldersHeight;
  int foldersTopPadding;
  int foldersXEnd;
  int foldersY;
  int paperclipY;
  int personalLevelX;
  int personalLevelY;
  int replyStateX;
  int replyStateY;
  int sendersAscent;
  int sendersFontSize;
  int sendersLineCount;
  int sendersLineHeight;
  TextView sendersView;
  int sendersWidth;
  int sendersX;
  int sendersY;
  boolean showColorBlock;
  boolean showFolders;
  boolean showPersonalLevel;
  boolean showReplyState;
  int starX;
  int starY;
  int subjectAscent;
  int subjectFontSize;
  int subjectLineCount;
  int subjectWidth;
  int subjectX;
  int subjectY;

  static
  {
    FOLDER_CELL_WIDTH = -1;
    COLOR_BLOCK_WIDTH = -1;
    COLOR_BLOCK_HEIGHT = -1;
    mCache = new SparseArray();
    sPaint = new TextPaint();
    sPaint.setAntiAlias(true);
  }

  public static boolean displaySendersInline(int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown conversation header view mode " + paramInt);
    case 0:
      return false;
    case 1:
    }
    return true;
  }

  public static ConversationItemViewCoordinates forWidth(Context paramContext, int paramInt1, int paramInt2, int paramInt3)
  {
    ConversationItemViewCoordinates localConversationItemViewCoordinates = (ConversationItemViewCoordinates)mCache.get(paramInt1 ^ paramInt3);
    View localView1;
    if (localConversationItemViewCoordinates == null)
    {
      localConversationItemViewCoordinates = new ConversationItemViewCoordinates();
      mCache.put(paramInt1 ^ paramInt3, localConversationItemViewCoordinates);
      int i = getHeight(paramContext, paramInt2);
      localView1 = LayoutInflater.from(paramContext).inflate(getLayoutId(paramInt2), null);
      localView1.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), View.MeasureSpec.makeMeasureSpec(i, 1073741824));
      localView1.layout(0, 0, paramInt1, i);
      View localView2 = localView1.findViewById(2131689557);
      localConversationItemViewCoordinates.checkmarkX = getX(localView2);
      localConversationItemViewCoordinates.checkmarkY = getY(localView2);
      View localView3 = localView1.findViewById(2131689558);
      localConversationItemViewCoordinates.starX = getX(localView3);
      localConversationItemViewCoordinates.starY = getY(localView3);
      View localView4 = localView1.findViewById(2131689473);
      if (localView4 == null)
        break label640;
      localConversationItemViewCoordinates.showPersonalLevel = true;
      localConversationItemViewCoordinates.personalLevelX = getX(localView4);
      localConversationItemViewCoordinates.personalLevelY = getY(localView4);
      TextView localTextView1 = (TextView)localView1.findViewById(2131689554);
      localConversationItemViewCoordinates.sendersView = localTextView1;
      localConversationItemViewCoordinates.sendersX = getX(localTextView1);
      localConversationItemViewCoordinates.sendersY = getY(localTextView1);
      localConversationItemViewCoordinates.sendersWidth = localTextView1.getWidth();
      localConversationItemViewCoordinates.sendersLineCount = getLineCount(localTextView1);
      localConversationItemViewCoordinates.sendersLineHeight = localTextView1.getLineHeight();
      localConversationItemViewCoordinates.sendersFontSize = ((int)localTextView1.getTextSize());
      sPaint.setTextSize(localConversationItemViewCoordinates.sendersFontSize);
      localConversationItemViewCoordinates.sendersAscent = ((int)sPaint.ascent());
      TextView localTextView2 = (TextView)localView1.findViewById(2131689537);
      localConversationItemViewCoordinates.subjectX = getX(localTextView2);
      localConversationItemViewCoordinates.subjectY = getY(localTextView2);
      localConversationItemViewCoordinates.subjectWidth = localTextView2.getWidth();
      localConversationItemViewCoordinates.subjectLineCount = getLineCount(localTextView2);
      localConversationItemViewCoordinates.subjectFontSize = ((int)localTextView2.getTextSize());
      sPaint.setTextSize(localConversationItemViewCoordinates.subjectFontSize);
      localConversationItemViewCoordinates.subjectAscent = ((int)sPaint.ascent());
      View localView5 = localView1.findViewById(2131689503);
      if (localView5 == null)
        break label649;
      localConversationItemViewCoordinates.showFolders = true;
      localConversationItemViewCoordinates.foldersXEnd = (getX(localView5) + localView5.getWidth());
      localConversationItemViewCoordinates.foldersY = getY(localView5);
      localConversationItemViewCoordinates.foldersHeight = localView5.getHeight();
      localConversationItemViewCoordinates.foldersTopPadding = localView5.getPaddingTop();
      if ((localView5 instanceof TextView))
      {
        localConversationItemViewCoordinates.foldersFontSize = ((int)((TextView)localView5).getTextSize());
        sPaint.setTextSize(localConversationItemViewCoordinates.foldersFontSize);
        localConversationItemViewCoordinates.foldersAscent = ((int)sPaint.ascent());
      }
      label489: if (localView1.findViewById(2131689646) != null)
        localConversationItemViewCoordinates.showColorBlock = true;
      View localView6 = localView1.findViewById(2131689474);
      if (localView6 == null)
        break label658;
      localConversationItemViewCoordinates.showReplyState = true;
      localConversationItemViewCoordinates.replyStateX = getX(localView6);
      localConversationItemViewCoordinates.replyStateY = getY(localView6);
    }
    while (true)
    {
      TextView localTextView3 = (TextView)localView1.findViewById(2131689556);
      localConversationItemViewCoordinates.dateXEnd = (getX(localTextView3) + localTextView3.getWidth());
      localConversationItemViewCoordinates.dateY = getY(localTextView3);
      localConversationItemViewCoordinates.dateFontSize = ((int)localTextView3.getTextSize());
      sPaint.setTextSize(localConversationItemViewCoordinates.dateFontSize);
      localConversationItemViewCoordinates.dateAscent = ((int)sPaint.ascent());
      localConversationItemViewCoordinates.paperclipY = getY(localView1.findViewById(2131689555));
      return localConversationItemViewCoordinates;
      label640: localConversationItemViewCoordinates.showPersonalLevel = false;
      break;
      label649: localConversationItemViewCoordinates.showFolders = false;
      break label489;
      label658: localConversationItemViewCoordinates.showReplyState = false;
    }
  }

  public static int getColorBlockHeight(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    if (COLOR_BLOCK_HEIGHT <= 0)
      COLOR_BLOCK_HEIGHT = localResources.getDimensionPixelSize(2131361820);
    return COLOR_BLOCK_HEIGHT;
  }

  public static int getColorBlockWidth(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    if (COLOR_BLOCK_WIDTH <= 0)
      COLOR_BLOCK_WIDTH = localResources.getDimensionPixelSize(2131361819);
    return COLOR_BLOCK_WIDTH;
  }

  public static int[] getDensityDependentArray(int[] paramArrayOfInt, float paramFloat)
  {
    int[] arrayOfInt = new int[paramArrayOfInt.length];
    for (int i = 0; i < paramArrayOfInt.length; i++)
      arrayOfInt[i] = ((int)(paramFloat * paramArrayOfInt[i]));
    return arrayOfInt;
  }

  public static int getFolderCellWidth(Context paramContext, int paramInt1, int paramInt2)
  {
    Resources localResources = paramContext.getResources();
    if (FOLDER_CELL_WIDTH <= 0)
      FOLDER_CELL_WIDTH = localResources.getDimensionPixelSize(2131361832);
    switch (paramInt1)
    {
    default:
      throw new IllegalArgumentException("Unknown conversation header view mode " + paramInt1);
    case 0:
    case 1:
    }
    return FOLDER_CELL_WIDTH;
  }

  public static int getFoldersWidth(Context paramContext, int paramInt)
  {
    Resources localResources = paramContext.getResources();
    if (TOTAL_FOLDER_WIDTH <= 0)
    {
      TOTAL_FOLDER_WIDTH = localResources.getDimensionPixelSize(2131361817);
      TOTAL_FOLDER_WIDTH_WIDE = localResources.getDimensionPixelSize(2131361818);
    }
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown conversation header view mode " + paramInt);
    case 0:
      return TOTAL_FOLDER_WIDTH_WIDE;
    case 1:
    }
    return TOTAL_FOLDER_WIDTH;
  }

  public static int getHeight(Context paramContext, int paramInt)
  {
    Resources localResources = paramContext.getResources();
    float f = localResources.getDisplayMetrics().scaledDensity;
    if (sConversationHeights == null)
      sConversationHeights = getDensityDependentArray(localResources.getIntArray(2131558400), f);
    return sConversationHeights[paramInt];
  }

  private static int getLayoutId(int paramInt)
  {
    switch (paramInt)
    {
    default:
      throw new IllegalArgumentException("Unknown conversation header view mode " + paramInt);
    case 0:
      return 2130968605;
    case 1:
    }
    return 2130968604;
  }

  private static int getLineCount(TextView paramTextView)
  {
    return Math.round(paramTextView.getHeight() / paramTextView.getLineHeight());
  }

  public static int getMinHeight(Context paramContext, ViewMode paramViewMode)
  {
    int i = getMode(paramContext, paramViewMode);
    Resources localResources = paramContext.getResources();
    if (i == 0);
    for (int j = 2131361877; ; j = 2131361876)
      return localResources.getDimensionPixelSize(j);
  }

  public static int getMode(Context paramContext, int paramInt)
  {
    Resources localResources = paramContext.getResources();
    switch (paramInt)
    {
    case 3:
    default:
      return localResources.getInteger(2131296276);
    case 2:
      return localResources.getInteger(2131296275);
    case 4:
    }
    return localResources.getInteger(2131296277);
  }

  public static int getMode(Context paramContext, ViewMode paramViewMode)
  {
    return getMode(paramContext, paramViewMode.getMode());
  }

  public static int getSubjectLength(Context paramContext, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    if (paramBoolean1)
    {
      if (paramBoolean2)
        return paramContext.getResources().getIntArray(2131558401)[paramInt];
      return paramContext.getResources().getIntArray(2131558402)[paramInt];
    }
    if (paramBoolean2)
      return paramContext.getResources().getIntArray(2131558403)[paramInt];
    return paramContext.getResources().getIntArray(2131558404)[paramInt];
  }

  private static int getX(View paramView)
  {
    int i = 0;
    if (paramView != null)
    {
      i += (int)paramView.getX();
      ViewParent localViewParent = paramView.getParent();
      if (localViewParent != null);
      for (paramView = (View)localViewParent; ; paramView = null)
        break;
    }
    return i;
  }

  private static int getY(View paramView)
  {
    int i = 0;
    if (paramView != null)
    {
      i += (int)paramView.getY();
      ViewParent localViewParent = paramView.getParent();
      if (localViewParent != null);
      for (paramView = (View)localViewParent; ; paramView = null)
        break;
    }
    return i;
  }

  public static boolean isWideMode(int paramInt)
  {
    return paramInt == 0;
  }

  public static void refreshConversationHeights(Context paramContext)
  {
    Resources localResources = paramContext.getResources();
    float f = localResources.getDisplayMetrics().scaledDensity;
    sConversationHeights = getDensityDependentArray(localResources.getIntArray(2131558400), f);
  }

  public static boolean showAttachmentBackground(int paramInt)
  {
    return paramInt != 0;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationItemViewCoordinates
 * JD-Core Version:    0.6.2
 */