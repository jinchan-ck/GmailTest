package com.google.android.gm;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.StringSplitter;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateUtils;
import android.text.style.CharacterStyle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.comm.longshadow.LongShadowUtils.LabelDisplayer;
import com.google.android.gm.comm.longshadow.LongShadowUtils.LabelDisplayer.LabelValues;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.ConversationCursor;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.android.gm.provider.Gmail.PersonalLevel;
import com.google.android.gm.utils.LabelColorUtils;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedSet;

public class CanvasConversationHeaderView extends View
{
  private static Bitmap ATTACHMENT;
  private static float CHECKMARK_AREA = 0.0F;
  private static Bitmap CHECKMARK_OFF;
  private static Bitmap CHECKMARK_ON;
  private static float CHECKMARK_SUBJECT = 0.0F;
  private static final Typeface DEFAULT_SENDER_TYPEFACE = Typeface.DEFAULT;
  private static final CharacterStyle DEFAULT_STYLE = new CharacterStyle()
  {
    public void updateDrawState(TextPaint paramAnonymousTextPaint)
    {
      paramAnonymousTextPaint.setColor(-16777216);
      paramAnonymousTextPaint.setTypeface(CanvasConversationHeaderView.DEFAULT_SENDER_TYPEFACE);
    }
  };
  private static final int DEFAULT_TEXT_COLOR = -16777216;
  private static Bitmap IMPORTANT_ONLY_TO_ME_READ;
  private static Bitmap IMPORTANT_ONLY_TO_ME_UNREAD;
  private static Bitmap IMPORTANT_TO_ME_AND_OTHERS_READ;
  private static Bitmap IMPORTANT_TO_ME_AND_OTHERS_UNREAD;
  private static Bitmap IMPORTANT_TO_OTHERS_READ;
  private static Bitmap IMPORTANT_TO_OTHERS_UNREAD;
  private static float LEFT_PADDING = 0.0F;
  private static float MIDDLE_PADDING = 0.0F;
  private static float MINIMUM_SNIPPET_WIDTH = 0.0F;
  private static Bitmap ONLY_TO_ME_READ;
  private static Bitmap ONLY_TO_ME_UNREAD;
  private static float PERSONAL_LEVEL_SENDERS = 0.0F;
  private static float RIGHT_PADDING = 0.0F;
  private static final int SNIPPET_COLOR = -8947849;
  private static final String SNIPPET_SEPARATOR = " - ";
  private static float STAR_AREA;
  private static Bitmap STAR_OFF;
  private static Bitmap STAR_ON;
  private static Bitmap TO_ME_AND_OTHERS_READ;
  private static Bitmap TO_ME_AND_OTHERS_UNREAD;
  private static Bitmap VOICEMAIL_ATTACHMENT;
  private static int mStatusColor;
  private static int sCheckmarkLeft;
  private static int sCheckmarkTop;
  private static TextPaint sPaint = createPaint();
  private static int sScaledDensity;
  private static int sSendersFontSize;
  private static int sSixPixels;
  private static int sStarTop;
  private static int sStatusFontSize;
  private static int sSubjectFontSize;
  private static int sTenPixels;
  private static int sTwoPixels;
  private String mAccount;
  private boolean mAllowBatch;
  private boolean mChecked = false;
  private long mConversationId;
  private ConversationListActivity mConversationListActivity;
  private Gmail.ConversationCursor mCursor;
  private int mCursorPosition;
  private long mDateMs = -1L;
  private CharSequence mDateText;
  int mDateX;
  int mDateY;
  private CharSequence mDisplayedLabel;
  private boolean mDownEvent;
  private Gmail mGmail;
  private final ConversationHeaderLabelDisplayer mLabelDisplayer;
  private final TextUtils.StringSplitter mLabelIdsSplitter = Gmail.newConversationLabelIdsSplitter();
  private Set<String> mLabels;
  private boolean mLabelsVisible;
  float mLabelsX;
  float mLabelsY;
  private long mMaxMessageId;
  private Bitmap mPaperclipBitmap;
  float mPaperclipX;
  int mPaperclipY;
  private Bitmap mPersonalLevelBitmap;
  float mPersonalLevelX;
  int mPersonalLevelY;
  private Drawable mReadBackground;
  private final ArrayList<SenderFragment> mSenderFragments = Lists.newArrayList();
  float mSendersLength;
  private CharSequence mSendersText;
  float mSendersWidth;
  float mSendersX;
  int mSendersY;
  private CharSequence mSnippetText;
  int mSnippetX;
  private Bitmap mStarBitmap;
  private boolean mStarred;
  private CharSequence mStatusText;
  float mStatusWidth;
  int mStatusX;
  int mStatusY;
  private CharSequence mSubjectText;
  private Typeface mSubjectTypeface;
  int mSubjectX;
  int mSubjectY;
  private int mViewWidth;

  static
  {
    sPaint.setAntiAlias(true);
    sPaint.setColor(0);
  }

  public CanvasConversationHeaderView(Context paramContext)
  {
    super(paramContext);
    Resources localResources = paramContext.getResources();
    if (CHECKMARK_OFF == null)
    {
      float f = localResources.getDisplayMetrics().scaledDensity;
      sSubjectFontSize = localResources.getDimensionPixelSize(2131230720);
      sSendersFontSize = localResources.getDimensionPixelSize(2131230722);
      sStatusFontSize = localResources.getDimensionPixelSize(2131230723);
      mStatusColor = localResources.getColor(2131165185);
      sTwoPixels = localResources.getDimensionPixelSize(2131230725);
      sSixPixels = localResources.getDimensionPixelSize(2131230728);
      sTenPixels = localResources.getDimensionPixelSize(2131230729);
      sCheckmarkLeft = localResources.getDimensionPixelSize(2131230738);
      sCheckmarkTop = localResources.getDimensionPixelSize(2131230739);
      sStarTop = localResources.getDimensionPixelSize(2131230740);
      sScaledDensity = (int)(60.0F * f);
      CHECKMARK_AREA = localResources.getDimensionPixelSize(2131230730);
      STAR_AREA = localResources.getDimensionPixelSize(2131230731);
      LEFT_PADDING = localResources.getDimensionPixelSize(2131230732);
      RIGHT_PADDING = localResources.getDimensionPixelSize(2131230733);
      MIDDLE_PADDING = localResources.getDimensionPixelSize(2131230734);
      CHECKMARK_SUBJECT = localResources.getDimensionPixelSize(2131230735);
      PERSONAL_LEVEL_SENDERS = localResources.getDimensionPixelSize(2131230736);
      CHECKMARK_OFF = BitmapFactory.decodeResource(localResources, 2130837510);
      CHECKMARK_ON = BitmapFactory.decodeResource(localResources, 2130837511);
      STAR_OFF = BitmapFactory.decodeResource(localResources, 2130837518);
      STAR_ON = BitmapFactory.decodeResource(localResources, 2130837519);
      ONLY_TO_ME_UNREAD = BitmapFactory.decodeResource(localResources, 2130837551);
      TO_ME_AND_OTHERS_UNREAD = BitmapFactory.decodeResource(localResources, 2130837557);
      IMPORTANT_ONLY_TO_ME_UNREAD = BitmapFactory.decodeResource(localResources, 2130837549);
      IMPORTANT_TO_ME_AND_OTHERS_UNREAD = BitmapFactory.decodeResource(localResources, 2130837555);
      IMPORTANT_TO_OTHERS_UNREAD = BitmapFactory.decodeResource(localResources, 2130837553);
      ONLY_TO_ME_READ = BitmapFactory.decodeResource(localResources, 2130837550);
      TO_ME_AND_OTHERS_READ = BitmapFactory.decodeResource(localResources, 2130837556);
      IMPORTANT_ONLY_TO_ME_READ = BitmapFactory.decodeResource(localResources, 2130837548);
      IMPORTANT_TO_ME_AND_OTHERS_READ = BitmapFactory.decodeResource(localResources, 2130837554);
      IMPORTANT_TO_OTHERS_READ = BitmapFactory.decodeResource(localResources, 2130837552);
      VOICEMAIL_ATTACHMENT = BitmapFactory.decodeResource(localResources, 2130837569);
      ATTACHMENT = BitmapFactory.decodeResource(localResources, 2130837571);
    }
    this.mLabelDisplayer = new ConversationHeaderLabelDisplayer(localResources);
    this.mAllowBatch = Persistence.getInstance(paramContext).getAllowBatch(paramContext);
  }

  private float addSenderFragment(int paramInt1, int paramInt2, float paramFloat, CharacterStyle paramCharacterStyle)
  {
    this.mSenderFragments.add(new SenderFragment(paramInt1, paramInt2, paramFloat, paramCharacterStyle));
    paramCharacterStyle.updateDrawState(sPaint);
    return sPaint.measureText(this.mSendersText, paramInt1, paramInt2);
  }

  private void calculateCoordinates()
  {
    int i = (int)sPaint.ascent();
    float f1 = LEFT_PADDING + getCheckmarkWidth(true);
    int j = getPaddingTop() - i + sTenPixels;
    this.mStatusX = ((int)f1);
    this.mStatusY = j;
    this.mSubjectX = ((int)f1);
    if (this.mStatusWidth > 0.0F)
      this.mSubjectX = ((int)(this.mSubjectX + (this.mStatusWidth + MIDDLE_PADDING)));
    this.mSubjectY = j;
    int k = j + (sSubjectFontSize + sSixPixels);
    float f2 = f1;
    if (this.mPersonalLevelBitmap != null)
    {
      this.mPersonalLevelX = f2;
      this.mPersonalLevelY = (k - sTenPixels);
      f2 += this.mPersonalLevelBitmap.getWidth() + PERSONAL_LEVEL_SENDERS;
    }
    sPaint.setTypeface(Typeface.DEFAULT);
    sPaint.setTextSize(sSendersFontSize);
    float f3 = sPaint.measureText(this.mDateText.toString()) + RIGHT_PADDING + MIDDLE_PADDING;
    this.mDateX = ((int)(this.mViewWidth - f3));
    this.mDateY = k;
    if (this.mLabelsVisible)
      this.mLabelsY = k;
    Bitmap localBitmap = this.mPaperclipBitmap;
    float f4 = 0.0F;
    if (localBitmap != null)
    {
      f4 = this.mPaperclipBitmap.getWidth() + MIDDLE_PADDING;
      this.mPaperclipY = (k - this.mPaperclipBitmap.getHeight() + sTwoPixels);
    }
    this.mSendersX = f2;
    this.mSendersY = k;
    this.mLabelDisplayer.increaseAvailableSpace(this.mSendersWidth - this.mSendersLength - f4);
    this.mLabelsX = (this.mViewWidth - f3 - (this.mLabelDisplayer.measureTotalDisplayWidth() + MIDDLE_PADDING));
    this.mPaperclipX = Math.max(this.mLabelsX - (f4 - MIDDLE_PADDING), 0.0F);
  }

  private void calculateTextsAndBitmaps()
  {
    this.mCursor.getCursor().moveToPosition(this.mCursorPosition);
    boolean bool1;
    ConversationHeaderLabelDisplayer localConversationHeaderLabelDisplayer;
    label338: boolean bool4;
    label408: SpannableStringBuilder localSpannableStringBuilder1;
    int i;
    float f2;
    int k;
    label504: CharacterStyle localCharacterStyle2;
    int n;
    label549: float f4;
    label658: Typeface localTypeface;
    label677: String str3;
    if (this.mConversationListActivity != null)
    {
      bool1 = this.mConversationListActivity.isSelected(this.mConversationId);
      this.mChecked = bool1;
      if (bool1)
        this.mConversationListActivity.updateSelectedLabels(this.mConversationId, this.mLabels);
      String str1 = this.mCursor.getRawLabelIds();
      localConversationHeaderLabelDisplayer = this.mLabelDisplayer;
      this.mLabelIdsSplitter.setString(str1);
      localConversationHeaderLabelDisplayer.initialize(getContext(), this.mAccount, this.mLabelIdsSplitter, this.mDisplayedLabel);
      this.mLabelsVisible = localConversationHeaderLabelDisplayer.hasVisibleLabels();
      this.mSendersWidth = (this.mViewWidth - getCheckmarkWidth(true) - LEFT_PADDING);
      boolean bool2 = localConversationHeaderLabelDisplayer.isUnread();
      this.mPersonalLevelBitmap = getPersonalIndicator(bool2, this.mCursor.getPersonalLevel(), localConversationHeaderLabelDisplayer.isImportant());
      if (this.mPersonalLevelBitmap != null)
      {
        float f12 = this.mPersonalLevelBitmap.getWidth() + PERSONAL_LEVEL_SENDERS;
        this.mSendersWidth -= f12;
      }
      long l = this.mCursor.getDateMs();
      if (this.mDateMs != l)
      {
        this.mDateMs = l;
        this.mDateText = DateUtils.getRelativeTimeSpanString(getContext(), l);
      }
      sPaint.setTypeface(Typeface.DEFAULT);
      sPaint.setTextSize(sSendersFontSize);
      float f1 = sPaint.measureText(this.mDateText.toString()) + RIGHT_PADDING + MIDDLE_PADDING;
      this.mSendersWidth -= f1;
      if (this.mLabelsVisible)
      {
        float f11 = this.mLabelDisplayer.measureTotalDisplayWidth() + 2.0F * MIDDLE_PADDING;
        this.mSendersWidth -= f11;
      }
      this.mPaperclipBitmap = null;
      if (!localConversationHeaderLabelDisplayer.isVoicemail())
        break label981;
      this.mPaperclipBitmap = VOICEMAIL_ATTACHMENT;
      if (this.mPaperclipBitmap != null)
      {
        float f10 = this.mPaperclipBitmap.getWidth() + MIDDLE_PADDING;
        this.mSendersWidth -= f10;
      }
      this.mSenderFragments.clear();
      String str2 = this.mCursor.getFromSnippetInstructions();
      boolean bool3 = this.mCursor.getForceAllUnread();
      if (this.mLabelDisplayer.isUnread())
        break label1001;
      bool4 = true;
      localSpannableStringBuilder1 = new SpannableStringBuilder();
      SpannableStringBuilder localSpannableStringBuilder2 = new SpannableStringBuilder();
      Utils.getStyledSenderSnippet(getContext(), str2, localSpannableStringBuilder1, localSpannableStringBuilder2, bool3, bool4);
      this.mSendersText = TextUtils.ellipsize(localSpannableStringBuilder1, sPaint, this.mSendersWidth, TextUtils.TruncateAt.END, false, null);
      Object[] arrayOfObject = localSpannableStringBuilder1.getSpans(0, localSpannableStringBuilder1.length(), Object.class);
      i = 0;
      f2 = 0.0F;
      if (arrayOfObject != null)
      {
        sPaint.setTextSize(sSendersFontSize);
        k = 0;
        int m = arrayOfObject.length;
        if (k < m)
        {
          localCharacterStyle2 = (CharacterStyle)arrayOfObject[k];
          n = localSpannableStringBuilder1.getSpanStart(localCharacterStyle2);
          if (n < this.mSendersText.length())
            break label1007;
        }
      }
      int j = this.mSendersText.length();
      this.mSendersLength = f2;
      if (i < j)
      {
        float f9 = this.mSendersLength;
        CharacterStyle localCharacterStyle1 = DEFAULT_STYLE;
        this.mSendersLength = (f9 + addSenderFragment(i, j, f2, localCharacterStyle1));
      }
      this.mStatusText = localSpannableStringBuilder2;
      DEFAULT_STYLE.updateDrawState(sPaint);
      sPaint.setTextSize(sStatusFontSize);
      float f3 = sPaint.measureText(this.mStatusText.toString());
      if (f3 == 0.0F)
        break label1098;
      f4 = f3 + MIDDLE_PADDING;
      this.mStatusWidth = f4;
      if (!localConversationHeaderLabelDisplayer.isUnread())
        break label1104;
      localTypeface = Typeface.DEFAULT_BOLD;
      this.mSubjectTypeface = localTypeface;
      str3 = this.mCursor.getSubject();
      if (!bool2)
        break label1112;
      setBackgroundDrawable(null);
    }
    while (true)
    {
      CharSequence localCharSequence = filterTag(str3);
      float f5 = this.mViewWidth - CHECKMARK_AREA - STAR_AREA - this.mStatusWidth;
      sPaint.setColor(-16777216);
      sPaint.setTypeface(this.mSubjectTypeface);
      sPaint.setTextSize(sSubjectFontSize);
      float f6 = LEFT_PADDING + getCheckmarkWidth(true) + this.mStatusWidth;
      this.mSubjectText = TextUtils.ellipsize(localCharSequence, sPaint, f5, TextUtils.TruncateAt.END, false, null);
      float f7 = sPaint.measureText(this.mSubjectText.toString());
      String str4 = this.mCursor.getSnippet();
      this.mSnippetText = null;
      if (str4.length() > 0)
      {
        float f8 = this.mViewWidth - f6 - RIGHT_PADDING - STAR_OFF.getWidth() - f7 - this.mStatusWidth;
        if (MINIMUM_SNIPPET_WIDTH == 0.0F)
          MINIMUM_SNIPPET_WIDTH = sPaint.measureText(" - ") + sTenPixels;
        if (f8 > MINIMUM_SNIPPET_WIDTH)
        {
          String str5 = " - " + this.mCursor.getSnippet();
          sPaint.measureText(str5.toString());
          this.mSnippetX = ((int)(f6 + f7));
          this.mSnippetText = TextUtils.ellipsize(str5, sPaint, f8, TextUtils.TruncateAt.END, false, null);
        }
      }
      this.mStarred = localConversationHeaderLabelDisplayer.isStarred();
      updateStarBitmap();
      return;
      bool1 = false;
      break;
      label981: if (!this.mCursor.hasAttachments())
        break label338;
      this.mPaperclipBitmap = ATTACHMENT;
      break label338;
      label1001: bool4 = false;
      break label408;
      label1007: int i1 = Math.min(localSpannableStringBuilder1.getSpanEnd(localCharacterStyle2), this.mSendersText.length());
      if (i <= n)
      {
        CharacterStyle localCharacterStyle3 = DEFAULT_STYLE;
        f2 += addSenderFragment(i, n, f2, localCharacterStyle3);
      }
      f2 += addSenderFragment(n, i1, f2, localCharacterStyle2);
      i = i1;
      if (i1 >= this.mSendersText.length())
        break label549;
      k++;
      break label504;
      label1098: f4 = 0.0F;
      break label658;
      label1104: localTypeface = Typeface.DEFAULT;
      break label677;
      label1112: if (this.mReadBackground == null)
        this.mReadBackground = getResources().getDrawable(2130837527);
      setBackgroundDrawable(this.mReadBackground);
    }
  }

  private static TextPaint createPaint()
  {
    TextPaint localTextPaint = new TextPaint();
    localTextPaint.setAntiAlias(true);
    return localTextPaint;
  }

  private void drawConversationStatus(Canvas paramCanvas)
  {
    sPaint.setColor(mStatusColor);
    sPaint.setTypeface(Typeface.DEFAULT);
    sPaint.setTextSize(sStatusFontSize);
    drawText(paramCanvas, this.mStatusText, this.mStatusX, this.mStatusY, sPaint);
  }

  private void drawText(Canvas paramCanvas, CharSequence paramCharSequence, int paramInt1, int paramInt2, TextPaint paramTextPaint)
  {
    paramCanvas.drawText(paramCharSequence, 0, paramCharSequence.length(), paramInt1, paramInt2, paramTextPaint);
  }

  private CharSequence filterTag(String paramString)
  {
    String str1 = paramString;
    if ((paramString.length() > 0) && (paramString.charAt(0) == '['))
    {
      int i = paramString.indexOf(']');
      if (i > 0)
      {
        String str2 = paramString.substring(1, i);
        str1 = "[" + Utils.ellipsize(str2, 7) + "]" + paramString.substring(i + 1);
      }
    }
    return str1;
  }

  private float getCheckmarkWidth(boolean paramBoolean)
  {
    if (!this.mAllowBatch)
      return 0.0F;
    float f1 = CHECKMARK_OFF.getWidth();
    float f2 = 0.0F;
    if (paramBoolean)
      f2 = CHECKMARK_SUBJECT;
    return f1 + f2;
  }

  private static Bitmap getPersonalIndicator(boolean paramBoolean1, Gmail.PersonalLevel paramPersonalLevel, boolean paramBoolean2)
  {
    Bitmap localBitmap;
    if (paramBoolean1)
      if (paramPersonalLevel == Gmail.PersonalLevel.ONLY_TO_ME)
        if (paramBoolean2)
          localBitmap = IMPORTANT_ONLY_TO_ME_UNREAD;
    do
    {
      do
      {
        return localBitmap;
        return ONLY_TO_ME_UNREAD;
        if (paramPersonalLevel == Gmail.PersonalLevel.TO_ME_AND_OTHERS)
        {
          if (paramBoolean2)
            return IMPORTANT_TO_ME_AND_OTHERS_UNREAD;
          return TO_ME_AND_OTHERS_UNREAD;
        }
        localBitmap = null;
      }
      while (!paramBoolean2);
      return IMPORTANT_TO_OTHERS_UNREAD;
      if (paramPersonalLevel == Gmail.PersonalLevel.ONLY_TO_ME)
      {
        if (paramBoolean2)
          return IMPORTANT_ONLY_TO_ME_READ;
        return ONLY_TO_ME_READ;
      }
      if (paramPersonalLevel == Gmail.PersonalLevel.TO_ME_AND_OTHERS)
      {
        if (paramBoolean2)
          return IMPORTANT_TO_ME_AND_OTHERS_READ;
        return TO_ME_AND_OTHERS_READ;
      }
      localBitmap = null;
    }
    while (!paramBoolean2);
    return IMPORTANT_TO_OTHERS_READ;
  }

  private int getStarLeftCoordinate()
  {
    return (int)(this.mViewWidth - this.mStarBitmap.getWidth() - RIGHT_PADDING);
  }

  private int measureHeight(int paramInt)
  {
    int i = View.MeasureSpec.getMode(paramInt);
    int j = View.MeasureSpec.getSize(paramInt);
    int k;
    if (i == 1073741824)
      k = j;
    do
    {
      return k;
      k = sScaledDensity;
    }
    while (i != -2147483648);
    return Math.min(k, j);
  }

  private int measureWidth(int paramInt)
  {
    int i = View.MeasureSpec.getMode(paramInt);
    int j = View.MeasureSpec.getSize(paramInt);
    int k;
    if (i == 1073741824)
      k = j;
    do
    {
      return k;
      k = this.mViewWidth;
    }
    while (i != -2147483648);
    return Math.min(k, j);
  }

  private void updateStarBitmap()
  {
    if (this.mStarred);
    for (Bitmap localBitmap = STAR_ON; ; localBitmap = STAR_OFF)
    {
      this.mStarBitmap = localBitmap;
      return;
    }
  }

  public final void bind(Gmail.ConversationCursor paramConversationCursor, Gmail paramGmail, String paramString, CharSequence paramCharSequence, boolean paramBoolean)
  {
    this.mGmail = paramGmail;
    this.mAccount = paramString;
    this.mConversationId = paramConversationCursor.getConversationId();
    this.mMaxMessageId = paramConversationCursor.getMaxServerMessageId();
    this.mLabels = paramConversationCursor.getLabels();
    this.mCursor = paramConversationCursor;
    this.mCursorPosition = paramConversationCursor.getCursor().getPosition();
    this.mDisplayedLabel = paramCharSequence;
    this.mAllowBatch = paramBoolean;
    requestLayout();
  }

  public CharSequence getSubject()
  {
    return this.mSubjectText;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    paramCanvas.drawBitmap(this.mStarBitmap, getStarLeftCoordinate(), sStarTop, sPaint);
    if (this.mAllowBatch)
      if (!this.mChecked)
        break label371;
    label371: for (Bitmap localBitmap = CHECKMARK_ON; ; localBitmap = CHECKMARK_OFF)
    {
      paramCanvas.drawBitmap(localBitmap, sCheckmarkLeft, sCheckmarkTop, sPaint);
      drawConversationStatus(paramCanvas);
      sPaint.setColor(-16777216);
      sPaint.setTypeface(this.mSubjectTypeface);
      sPaint.setTextSize(sSubjectFontSize);
      drawText(paramCanvas, this.mSubjectText, this.mSubjectX, this.mSubjectY, sPaint);
      if (this.mSnippetText != null)
      {
        sPaint.setColor(-8947849);
        sPaint.setTypeface(Typeface.DEFAULT);
        drawText(paramCanvas, this.mSnippetText, this.mSnippetX, this.mSubjectY, sPaint);
      }
      sPaint.setColor(-16777216);
      if (this.mPersonalLevelBitmap != null)
        paramCanvas.drawBitmap(this.mPersonalLevelBitmap, this.mPersonalLevelX, this.mPersonalLevelY, sPaint);
      sPaint.setTypeface(Typeface.DEFAULT);
      sPaint.setTextSize(sSendersFontSize);
      drawText(paramCanvas, this.mDateText, this.mDateX, this.mDateY, sPaint);
      if (this.mLabelsVisible)
        this.mLabelDisplayer.drawLabels(paramCanvas, this.mLabelsX, this.mLabelsY);
      if (this.mPaperclipBitmap != null)
        paramCanvas.drawBitmap(this.mPaperclipBitmap, this.mPaperclipX, this.mPaperclipY, sPaint);
      sPaint.setTextSize(sSendersFontSize);
      Iterator localIterator = this.mSenderFragments.iterator();
      while (localIterator.hasNext())
      {
        SenderFragment localSenderFragment = (SenderFragment)localIterator.next();
        localSenderFragment.style.updateDrawState(sPaint);
        paramCanvas.drawText(this.mSendersText, localSenderFragment.start, localSenderFragment.end, this.mSendersX + localSenderFragment.x, this.mSendersY, sPaint);
      }
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    this.mViewWidth = (paramInt3 - paramInt1);
    calculateTextsAndBitmaps();
    calculateCoordinates();
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    setMeasuredDimension(measureWidth(paramInt1), measureHeight(paramInt2));
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i = this.mViewWidth;
    int j = paramMotionEvent.getAction();
    boolean bool1 = false;
    switch (j)
    {
    case 2:
    default:
    case 0:
    case 3:
    case 1:
    }
    while (bool1)
    {
      postInvalidate();
      return bool1;
      this.mDownEvent = true;
      if ((!this.mAllowBatch) || (paramMotionEvent.getX() >= CHECKMARK_AREA))
      {
        boolean bool6 = paramMotionEvent.getX() < i - STAR_AREA;
        bool1 = false;
        if (!bool6)
          break;
      }
      else
      {
        bool1 = true;
        continue;
        this.mDownEvent = false;
        bool1 = false;
        continue;
        boolean bool2 = this.mDownEvent;
        bool1 = false;
        if (bool2)
          if ((this.mAllowBatch) && (paramMotionEvent.getX() < CHECKMARK_AREA))
          {
            toggleCheckMark();
            bool1 = true;
          }
          else
          {
            boolean bool3 = paramMotionEvent.getX() < i - STAR_AREA;
            bool1 = false;
            if (bool3)
            {
              boolean bool4 = paramMotionEvent.getY() < getHeight();
              bool1 = false;
              if (bool4)
              {
                if (!this.mStarred);
                for (boolean bool5 = true; ; bool5 = false)
                {
                  this.mStarred = bool5;
                  updateStarBitmap();
                  int k = getStarLeftCoordinate();
                  invalidate(k, sStarTop, k + this.mStarBitmap.getWidth(), sStarTop + this.mStarBitmap.getHeight());
                  this.mGmail.addOrRemoveLabelOnConversation(this.mAccount, this.mConversationId, this.mMaxMessageId, "^t", this.mStarred);
                  bool1 = true;
                  break;
                }
              }
            }
          }
      }
    }
    return super.onTouchEvent(paramMotionEvent);
  }

  void setActivity(ConversationListActivity paramConversationListActivity)
  {
    this.mConversationListActivity = paramConversationListActivity;
  }

  public void toggleCheckMark()
  {
    if (!this.mChecked);
    for (boolean bool = true; ; bool = false)
    {
      this.mChecked = bool;
      if (this.mConversationListActivity != null)
        this.mConversationListActivity.toggleConversation(this.mConversationId, this.mMaxMessageId, this.mLabels);
      postInvalidate();
      return;
    }
  }

  private static class ConversationHeaderLabelDisplayer extends LongShadowUtils.LabelDisplayer
  {
    private static float BASE_MAX_AVAILABLE_SPACE = 0.0F;
    private static float FADING_EDGE_LENGTH = 0.0F;
    private static final int LABELS_BACKGROUND_COLOR = -11034160;
    private static int LABELS_RADIUS = 0;
    private static int LABEL_FONT_SIZE = 0;
    private static float LABEL_RECT_PADDING = 0.0F;
    private static float LABEL_TEXT_BOTTOM_PADDING = 0.0F;
    private static float LABEL_TEXT_PADDING = 0.0F;
    private static float LABEL_TEXT_TOP_PADDING = 0.0F;
    private static final int MAX_DISPLAYED_LABELS_COUNT = 3;
    private static Bitmap MORE_LABELS;
    private static TextPaint sLabelsPaint = CanvasConversationHeaderView.access$000();
    private String mAccount;
    private boolean mAllLabelsDisplayed;
    private int mDisplayLabelCount;
    private int mLabelCount;
    private long mLabelIdIgnored;
    private float mMaxLabelWidth;
    private float mMaxTotalWidth;
    private final Bitmap mMoreLabelsBitmap;

    public ConversationHeaderLabelDisplayer(Resources paramResources)
    {
      LABELS_RADIUS = paramResources.getDimensionPixelSize(2131230737);
      FADING_EDGE_LENGTH = paramResources.getDimensionPixelSize(2131230742);
      LABEL_RECT_PADDING = paramResources.getDimensionPixelSize(2131230727);
      LABEL_TEXT_PADDING = paramResources.getDimensionPixelSize(2131230726);
      LABEL_TEXT_TOP_PADDING = paramResources.getDimensionPixelSize(2131230724);
      LABEL_TEXT_BOTTOM_PADDING = paramResources.getDimensionPixelSize(2131230725);
      LABEL_FONT_SIZE = paramResources.getDimensionPixelSize(2131230721);
      BASE_MAX_AVAILABLE_SPACE = paramResources.getDimensionPixelSize(2131230741);
      MORE_LABELS = BitmapFactory.decodeResource(paramResources, 2130837572);
      sLabelsPaint.setAntiAlias(true);
      sLabelsPaint.setTextSize(LABEL_FONT_SIZE);
      sLabelsPaint.setColor(-11034160);
      this.mMoreLabelsBitmap = MORE_LABELS;
    }

    private void initMaxLabelWidth()
    {
      this.mMaxLabelWidth = ((this.mMaxTotalWidth - LABEL_RECT_PADDING * (this.mDisplayLabelCount - 1.0F)) / this.mDisplayLabelCount);
      if (!this.mAllLabelsDisplayed)
        this.mMaxLabelWidth -= (this.mMoreLabelsBitmap.getWidth() + LABEL_RECT_PADDING) / 3.0F;
    }

    public void drawLabels(Canvas paramCanvas, float paramFloat1, float paramFloat2)
    {
      float f1 = 0.0F;
      (sLabelsPaint.getTextSize() + 2.0F * LABEL_TEXT_PADDING);
      float f2 = paramFloat2 + sLabelsPaint.ascent() - LABEL_TEXT_TOP_PADDING;
      float f3 = paramFloat2 + sLabelsPaint.descent() + LABEL_TEXT_BOTTOM_PADDING;
      int i = 0;
      Iterator localIterator = this.mLabelValuesSortedSet.iterator();
      LongShadowUtils.LabelDisplayer.LabelValues localLabelValues;
      String str;
      float f4;
      float f5;
      float f6;
      int j;
      label147: int[] arrayOfInt;
      label157: TextPaint localTextPaint;
      if (localIterator.hasNext())
      {
        localLabelValues = (LongShadowUtils.LabelDisplayer.LabelValues)localIterator.next();
        str = localLabelValues.name;
        f4 = sLabelsPaint.measureText(str);
        f5 = Math.min(f4 + 2.0F * LABEL_TEXT_PADDING, this.mMaxLabelWidth);
        f6 = paramFloat1 + f1;
        f1 += f5 + LABEL_RECT_PADDING;
        if (localLabelValues.labelId != this.mLabelIdIgnored)
          break label374;
        j = 1;
        if (j == 0)
          break label380;
        arrayOfInt = LabelColorUtils.getMutedColorInts();
        sLabelsPaint.setColor(arrayOfInt[0]);
        localTextPaint = sLabelsPaint;
        if (j == 0)
          break label397;
      }
      label397: for (Paint.Style localStyle = Paint.Style.STROKE; ; localStyle = Paint.Style.FILL_AND_STROKE)
      {
        localTextPaint.setStyle(localStyle);
        RectF localRectF = new RectF(f6, f2, f6 + f5, f3);
        paramCanvas.drawRoundRect(localRectF, LABELS_RADIUS, LABELS_RADIUS, sLabelsPaint);
        sLabelsPaint.setColor(arrayOfInt[1]);
        if (f4 > this.mMaxLabelWidth - 2.0F * LABEL_TEXT_PADDING)
        {
          float f7 = f6 + this.mMaxLabelWidth - LABEL_TEXT_PADDING;
          LinearGradient localLinearGradient = new LinearGradient(f7 - FADING_EDGE_LENGTH, f2, f7, f2, arrayOfInt[1], LabelColorUtils.getTransparentColor(arrayOfInt[1]), Shader.TileMode.CLAMP);
          sLabelsPaint.setShader(localLinearGradient);
        }
        paramCanvas.drawText(str, f6 + LABEL_TEXT_PADDING, paramFloat2, sLabelsPaint);
        sLabelsPaint.setShader(null);
        i++;
        int k = this.mDisplayLabelCount;
        if (i < k)
          break;
        if (!this.mAllLabelsDisplayed)
          paramCanvas.drawBitmap(this.mMoreLabelsBitmap, paramFloat1 + f1, f2, sLabelsPaint);
        return;
        label374: j = 0;
        break label147;
        label380: arrayOfInt = LabelColorUtils.getLabelColorInts(localLabelValues.colorId, this.mAccount);
        break label157;
      }
    }

    public boolean hasVisibleLabels()
    {
      return this.mDisplayLabelCount > 0;
    }

    public void increaseAvailableSpace(float paramFloat)
    {
      this.mMaxTotalWidth = (paramFloat + BASE_MAX_AVAILABLE_SPACE);
      initMaxLabelWidth();
    }

    public void initialize(Context paramContext, String paramString, TextUtils.StringSplitter paramStringSplitter, CharSequence paramCharSequence)
    {
      super.initialize(paramContext, paramString, paramStringSplitter, paramCharSequence);
      this.mAccount = paramString;
      this.mMaxTotalWidth = BASE_MAX_AVAILABLE_SPACE;
      this.mLabelCount = this.mLabelStringSet.size();
      this.mDisplayLabelCount = Math.min(this.mLabelCount, 3);
      if (this.mLabelCount == this.mDisplayLabelCount);
      for (boolean bool = true; ; bool = false)
      {
        this.mAllLabelsDisplayed = bool;
        this.mLabelIdIgnored = LongShadowUtils.getLabelMap(paramContext.getContentResolver(), paramString).getLabelIdIgnored();
        initMaxLabelWidth();
        return;
      }
    }

    public float measureTotalDisplayWidth()
    {
      float f1 = 0.0F;
      Iterator localIterator = this.mLabelStringSet.iterator();
      int i = 0;
      if (i < this.mDisplayLabelCount)
      {
        String str = (String)localIterator.next();
        float f2 = Math.min(this.mMaxLabelWidth, sLabelsPaint.measureText(str) + 2.0F * LABEL_TEXT_PADDING);
        if (i == 0);
        for (float f3 = 0.0F; ; f3 = LABEL_RECT_PADDING)
        {
          f1 += f3 + f2;
          i++;
          break;
        }
      }
      if (!this.mAllLabelsDisplayed)
        f1 += this.mMoreLabelsBitmap.getWidth() + LABEL_RECT_PADDING;
      return f1;
    }
  }

  static class SenderFragment
  {
    public int end;
    public int start;
    public CharacterStyle style;
    public float x;

    public SenderFragment(int paramInt1, int paramInt2, float paramFloat, CharacterStyle paramCharacterStyle)
    {
      this.start = paramInt1;
      this.end = paramInt2;
      this.x = paramFloat;
      this.style = paramCharacterStyle;
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.CanvasConversationHeaderView
 * JD-Core Version:    0.6.2
 */