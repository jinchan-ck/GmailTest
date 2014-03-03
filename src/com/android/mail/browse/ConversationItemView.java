package com.android.mail.browse;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.Layout.Alignment;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.format.DateUtils;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.util.SparseArray;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import com.android.mail.perf.Timer;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.UIProvider.ConversationColumns;
import com.android.mail.ui.AnimatedAdapter;
import com.android.mail.ui.ControllableActivity;
import com.android.mail.ui.ConversationSelectionSet;
import com.android.mail.ui.FolderDisplayer;
import com.android.mail.ui.SwipeableItemView;
import com.android.mail.ui.SwipeableListView;
import com.android.mail.ui.ViewMode;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.android.common.html.parser.HtmlParser;
import com.google.android.common.html.parser.HtmlTreeBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.SortedSet;

public class ConversationItemView extends View implements ToggleableItem,
		SwipeableItemView {
	private static Bitmap ATTACHMENT;
	private static Bitmap CHECKMARK_OFF;
	private static Bitmap CHECKMARK_ON;
	private static Bitmap DATE_BACKGROUND;
	private static Bitmap IMPORTANT_ONLY_TO_ME;
	private static Bitmap IMPORTANT_TO_ME_AND_OTHERS;
	private static Bitmap IMPORTANT_TO_OTHERS;
	private static final String LOG_TAG;
	private static Bitmap MORE_FOLDERS;
	private static Bitmap ONLY_TO_ME;
	private static Bitmap STAR_OFF;
	private static Bitmap STAR_ON;
	private static Bitmap STATE_CALENDAR_INVITE;
	private static Bitmap STATE_FORWARDED;
	private static Bitmap STATE_REPLIED;
	private static Bitmap STATE_REPLIED_AND_FORWARDED;
	private static Bitmap TO_ME_AND_OTHERS;
	private static int sActivatedTextColor;
	private static CharacterStyle sActivatedTextSpan;
	private static int sAnimatingBackgroundColor;
	private static Bitmap sDateBackgroundAttachment;
	private static int sDateBackgroundHeight;
	private static Bitmap sDateBackgroundNoAttachment;
	private static int sDateBackgroundPaddingLeft;
	private static int sDateTextColor;
	private static String sElidedPaddingToken;
	private static int sFadedActivatedColor;
	private static TextPaint sFoldersPaint;
	private static HtmlTreeBuilder sHtmlBuilder;
	private static HtmlParser sHtmlParser;
	private static int sLayoutCount = 0;
	private static TextPaint sPaint;
	private static int sScrollSlop;
	private static String sSendersSplitToken;
	private static int sSendersTextColorRead;
	private static int sSendersTextColorUnread;
	private static TextView sSendersTextView;
	private static int sSendersTextViewHeight;
	private static int sSendersTextViewTopPadding;
	private static int sShrinkAnimationDuration;
	private static int sSlideAnimationDuration;
	private static ForegroundColorSpan sSnippetTextReadSpan;
	private static ForegroundColorSpan sSnippetTextUnreadSpan;
	private static int sStandardScaledDimen;
	private static TextAppearanceSpan sSubjectTextReadSpan;
	private static TextAppearanceSpan sSubjectTextUnreadSpan;
	private static TextView sSubjectTextView;
	private static Timer sTimer;
	private static int sTouchSlop;
	private String mAccount;
	private ControllableActivity mActivity;
	private AnimatedAdapter mAdapter;
	private int mAnimatedHeight = -1;
	private int mBackgroundOverride = -1;
	private final SparseArray<Drawable> mBackgrounds = new SparseArray();
	private boolean mCheckboxesEnabled;
	private boolean mChecked = false;
	private final Context mContext;
	ConversationItemViewCoordinates mCoordinates;
	private int mDateX;
	private Folder mDisplayedFolder;
	private boolean mDownEvent;
	private int mFoldersXEnd;
	public ConversationItemViewModel mHeader;
	private int mLastTouchX;
	private int mLastTouchY;
	private int mMode = -1;
	private int mPaperclipX;
	private int mPreviousMode;
	private boolean mPriorityMarkersEnabled;
	private ConversationSelectionSet mSelectedConversationSet;
	private int mSendersWidth;
	private boolean mStarEnabled;
	private boolean mSwipeEnabled;
	private final boolean mTabletDevice;
	private boolean mTesting = false;
	private int mViewWidth = -1;

	static {
		LOG_TAG = LogTag.getLogTag();
		sPaint = new TextPaint();
		sFoldersPaint = new TextPaint();
		sFadedActivatedColor = -1;
		sPaint.setAntiAlias(true);
		sFoldersPaint.setAntiAlias(true);
	}

	public ConversationItemView(Context paramContext, String paramString) {
		super(paramContext);
		setClickable(true);
		setLongClickable(true);
		this.mContext = paramContext.getApplicationContext();
		this.mTabletDevice = Utils.useTabletUI(this.mContext);
		this.mAccount = paramString;
		Resources localResources = this.mContext.getResources();
		if (CHECKMARK_OFF == null) {
			CHECKMARK_OFF = BitmapFactory.decodeResource(localResources,
					2130837512);
			CHECKMARK_ON = BitmapFactory.decodeResource(localResources,
					2130837513);
			STAR_OFF = BitmapFactory.decodeResource(localResources, 2130837516);
			STAR_ON = BitmapFactory.decodeResource(localResources, 2130837520);
			ONLY_TO_ME = BitmapFactory.decodeResource(localResources,
					2130837569);
			TO_ME_AND_OTHERS = BitmapFactory.decodeResource(localResources,
					2130837572);
			IMPORTANT_ONLY_TO_ME = BitmapFactory.decodeResource(localResources,
					2130837570);
			IMPORTANT_TO_ME_AND_OTHERS = BitmapFactory.decodeResource(
					localResources, 2130837573);
			IMPORTANT_TO_OTHERS = BitmapFactory.decodeResource(localResources,
					2130837571);
			ATTACHMENT = BitmapFactory.decodeResource(localResources,
					2130837560);
			MORE_FOLDERS = BitmapFactory.decodeResource(localResources,
					2130837575);
			DATE_BACKGROUND = BitmapFactory.decodeResource(localResources,
					2130837533);
			STATE_REPLIED = BitmapFactory.decodeResource(localResources,
					2130837564);
			STATE_FORWARDED = BitmapFactory.decodeResource(localResources,
					2130837561);
			STATE_REPLIED_AND_FORWARDED = BitmapFactory.decodeResource(
					localResources, 2130837563);
			STATE_CALENDAR_INVITE = BitmapFactory.decodeResource(
					localResources, 2130837562);
			sActivatedTextColor = localResources.getColor(17170443);
			sActivatedTextSpan = CharacterStyle.wrap(new ForegroundColorSpan(
					sActivatedTextColor));
			sSendersTextColorRead = localResources.getColor(2131230730);
			sSendersTextColorUnread = localResources.getColor(2131230727);
			sSubjectTextUnreadSpan = new TextAppearanceSpan(this.mContext,
					2131492948);
			sSubjectTextReadSpan = new TextAppearanceSpan(this.mContext,
					2131492949);
			sSnippetTextUnreadSpan = new ForegroundColorSpan(2131230726);
			sSnippetTextReadSpan = new ForegroundColorSpan(2131230729);
			sDateTextColor = localResources.getColor(2131230731);
			sDateBackgroundPaddingLeft = localResources
					.getDimensionPixelSize(2131361828);
			sTouchSlop = localResources.getDimensionPixelSize(2131361829);
			sDateBackgroundHeight = localResources
					.getDimensionPixelSize(2131361827);
			sStandardScaledDimen = localResources
					.getDimensionPixelSize(2131361831);
			sShrinkAnimationDuration = localResources.getInteger(2131296265);
			sSlideAnimationDuration = localResources.getInteger(2131296266);
			sSendersSplitToken = localResources.getString(2131427587);
			sElidedPaddingToken = localResources.getString(2131427590);
			sAnimatingBackgroundColor = localResources.getColor(2131230762);
			sSendersTextViewTopPadding = localResources
					.getDimensionPixelSize(2131361890);
			sSendersTextViewHeight = localResources
					.getDimensionPixelSize(2131361891);
			sScrollSlop = localResources.getInteger(2131296285);
		}
	}

	private void beginDragMode() {
		if ((this.mLastTouchX < 0) || (this.mLastTouchY < 0))
			return;
		if (!this.mChecked)
			toggleCheckMark();
		int i = this.mSelectedConversationSet.size();
		String str1 = Utils.formatPlural(this.mContext, 2131755009, i);
		ClipData localClipData = ClipData.newUri(
				this.mContext.getContentResolver(), str1,
				Conversation.MOVE_CONVERSATIONS_URI);
		Iterator localIterator = this.mSelectedConversationSet.values()
				.iterator();
		while (localIterator.hasNext())
			localClipData.addItem(new ClipData.Item(String
					.valueOf(((Conversation) localIterator.next()).position)));
		int j = getWidth();
		int k = getHeight();
		if ((j < 0) || (k < 0))
			;
		for (int m = 1; m != 0; m = 0) {
			String str2 = LOG_TAG;
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = Integer.valueOf(j);
			arrayOfObject[1] = Integer.valueOf(k);
			LogUtils.e(
					str2,
					"ConversationItemView: dimension is negative: width=%d, height=%d",
					arrayOfObject);
			return;
		}
		this.mActivity.startDragMode();
		startDrag(localClipData, new ShadowBuilder(this, i, this.mLastTouchX,
				this.mLastTouchY), null, 0);
	}

	private void bind(ConversationItemViewModel paramConversationItemViewModel,
			ControllableActivity paramControllableActivity,
			ConversationSelectionSet paramConversationSelectionSet,
			Folder paramFolder, boolean paramBoolean1, boolean paramBoolean2,
			boolean paramBoolean3, AnimatedAdapter paramAnimatedAdapter) {
		boolean bool1 = true;
		this.mHeader = paramConversationItemViewModel;
		this.mActivity = paramControllableActivity;
		this.mSelectedConversationSet = paramConversationSelectionSet;
		this.mDisplayedFolder = paramFolder;
		boolean bool2;
		if (!paramBoolean1) {
			bool2 = bool1;
			this.mCheckboxesEnabled = bool2;
			if ((paramFolder == null) || (paramFolder.isTrash()))
				break label91;
		}
		while (true) {
			this.mStarEnabled = bool1;
			this.mSwipeEnabled = paramBoolean2;
			this.mPriorityMarkersEnabled = paramBoolean3;
			this.mAdapter = paramAnimatedAdapter;
			setContentDescription();
			requestLayout();
			return;
			bool2 = false;
			break;
			label91: bool1 = false;
		}
	}

	private void calculateCoordinates() {
		startTimer("CCHV.coordinates");
		sPaint.setTextSize(this.mCoordinates.dateFontSize);
		sPaint.setTypeface(Typeface.DEFAULT);
		this.mDateX = (this.mCoordinates.dateXEnd - (int) sPaint
				.measureText(this.mHeader.dateText));
		this.mPaperclipX = (this.mDateX - ATTACHMENT.getWidth());
		int i = this.mContext.getResources().getDimensionPixelSize(2131361832);
		if (ConversationItemViewCoordinates.isWideMode(this.mMode)) {
			this.mFoldersXEnd = this.mCoordinates.dateXEnd;
			this.mSendersWidth = this.mCoordinates.sendersWidth;
			if (this.mHeader.isLayoutValid(this.mContext))
				pauseTimer("CCHV.coordinates");
		} else {
			if (this.mCoordinates.showFolders) {
				if (this.mHeader.paperclip != null)
					;
				for (this.mFoldersXEnd = this.mPaperclipX;; this.mFoldersXEnd = (this.mDateX - i / 2)) {
					this.mSendersWidth = (this.mFoldersXEnd
							- this.mCoordinates.sendersX - i * 2);
					if (!this.mHeader.folderDisplayer.hasVisibleFolders())
						break;
					this.mSendersWidth -= ConversationItemViewCoordinates
							.getFoldersWidth(this.mContext, this.mMode);
					break;
				}
			}
			if (this.mHeader.paperclip != null)
				;
			for (int j = this.mPaperclipX;; j = this.mDateX) {
				this.mSendersWidth = (j - this.mCoordinates.sendersX - i);
				break;
			}
		}
		int k = this.mCoordinates.sendersY - this.mCoordinates.sendersAscent;
		if (this.mHeader.styledSenders != null) {
			ellipsizeStyledSenders();
			sPaint.setTextSize(this.mCoordinates.sendersFontSize);
			sPaint.setTypeface(Typeface.DEFAULT);
			if (this.mSendersWidth < 0)
				this.mSendersWidth = 0;
			pauseTimer("CCHV.coordinates");
			return;
		}
		int m = 0;
		int n = 0;
		sPaint.setTextSize(this.mCoordinates.sendersFontSize);
		sPaint.setTypeface(Typeface.DEFAULT);
		Iterator localIterator = this.mHeader.senderFragments.iterator();
		while (localIterator.hasNext()) {
			ConversationItemViewModel.SenderFragment localSenderFragment = (ConversationItemViewModel.SenderFragment) localIterator
					.next();
			CharacterStyle localCharacterStyle = localSenderFragment.style;
			int i3 = localSenderFragment.start;
			int i4 = localSenderFragment.end;
			localCharacterStyle.updateDrawState(sPaint);
			localSenderFragment.width = ((int) sPaint.measureText(
					this.mHeader.sendersText, i3, i4));
			if (localSenderFragment.isFixed)
				n += localSenderFragment.width;
			m += localSenderFragment.width;
		}
		if (!ConversationItemViewCoordinates.displaySendersInline(this.mMode)) {
			int i1 = this.mSendersWidth;
			if (m > i1)
				break label599;
		}
		label599: for (int i2 = this.mCoordinates.sendersLineHeight / 2;; i2 = 0) {
			k += i2;
			if (this.mSendersWidth < 0)
				this.mSendersWidth = 0;
			ellipsize(n, k);
			this.mHeader.sendersDisplayLayout = new StaticLayout(
					this.mHeader.sendersDisplayText, sPaint,
					this.mSendersWidth, Layout.Alignment.ALIGN_NORMAL, 1.0F,
					0.0F, true);
			break;
		}
	}

	private void calculateTextsAndBitmaps()
  {
    startTimer("CCHV.txtsbmps");
    if (this.mSelectedConversationSet != null)
      this.mChecked = this.mSelectedConversationSet.contains(this.mHeader.conversation);
    boolean bool1 = this.mCheckboxesEnabled;
    if (this.mHeader.checkboxVisible != bool1)
      this.mHeader.checkboxVisible = bool1;
    boolean bool2 = this.mHeader.unread;
    updateBackground(bool2);
    if (this.mHeader.isLayoutValid(this.mContext))
    {
      pauseTimer("CCHV.txtsbmps");
      return;
    }
    startTimer("CCHV.folders");
    if (this.mCoordinates.showFolders)
    {
      this.mHeader.folderDisplayer = new ConversationItemFolderDisplayer(this.mContext);
      this.mHeader.folderDisplayer.loadConversationFolders(this.mHeader.conversation, this.mDisplayedFolder);
    }
    pauseTimer("CCHV.folders");
    this.mHeader.dateText = DateUtils.getRelativeTimeSpanString(this.mContext, this.mHeader.conversation.dateMs).toString();
    this.mHeader.paperclip = null;
    if (this.mHeader.conversation.hasAttachments)
      this.mHeader.paperclip = ATTACHMENT;
    this.mHeader.personalLevelBitmap = null;
    int k;
    int m;
    int n;
    label270: Bitmap localBitmap2;
    label292: label299: boolean bool3;
    if (this.mCoordinates.showPersonalLevel)
    {
      k = this.mHeader.conversation.personalLevel;
      if (this.mHeader.conversation.priority == 1)
      {
        m = 1;
        if ((m == 0) || (!this.mPriorityMarkersEnabled))
          break label493;
        n = 1;
        if (k != 2)
          break label507;
        ConversationItemViewModel localConversationItemViewModel2 = this.mHeader;
        if (n == 0)
          break label499;
        localBitmap2 = IMPORTANT_ONLY_TO_ME;
        localConversationItemViewModel2.personalLevelBitmap = localBitmap2;
      }
    }
    else
    {
      startTimer("CCHV.sendersubj");
      layoutSubjectSpans(bool2);
      this.mHeader.sendersDisplayText = new SpannableStringBuilder();
      this.mHeader.styledSendersString = new SpannableStringBuilder();
      if (this.mHeader.conversation.conversationInfo == null)
        break label571;
      Context localContext = getContext();
      this.mHeader.messageInfoString = SendersView.createMessageInfo(localContext, this.mHeader.conversation);
      int i = ConversationItemViewCoordinates.getMode(localContext, this.mActivity.getViewMode());
      if ((this.mHeader.folderDisplayer == null) || (this.mHeader.folderDisplayer.mFoldersCount <= 0))
        break label565;
      bool3 = true;
      label415: int j = ConversationItemViewCoordinates.getSubjectLength(localContext, i, bool3, this.mHeader.conversation.hasAttachments);
      this.mHeader.styledSenders = SendersView.format(localContext, this.mHeader.conversation.conversationInfo, this.mHeader.messageInfoString.toString(), j, getParser(), getBuilder());
    }
    while (true)
    {
      pauseTimer("CCHV.sendersubj");
      pauseTimer("CCHV.txtsbmps");
      return;
      m = 0;
      break;
      label493: n = 0;
      break label270;
      label499: localBitmap2 = ONLY_TO_ME;
      break label292;
      label507: if (k == 1)
      {
        ConversationItemViewModel localConversationItemViewModel1 = this.mHeader;
        if (n != 0);
        for (Bitmap localBitmap1 = IMPORTANT_TO_ME_AND_OTHERS; ; localBitmap1 = TO_ME_AND_OTHERS)
        {
          localConversationItemViewModel1.personalLevelBitmap = localBitmap1;
          break;
        }
      }
      if (n == 0)
        break label299;
      this.mHeader.personalLevelBitmap = IMPORTANT_TO_OTHERS;
      break label299;
      label565: bool3 = false;
      break label415;
      label571: SendersView.formatSenders(this.mHeader, getContext());
    }
  }

	private boolean canFitFragment(int paramInt1, int paramInt2, int paramInt3) {
		if (paramInt2 == this.mCoordinates.sendersLineCount)
			if (paramInt1 + paramInt3 > this.mSendersWidth)
				;
		while (paramInt1 <= this.mSendersWidth) {
			return true;
			return false;
		}
		return false;
	}

	private SpannableString copyStyles(
			CharacterStyle[] paramArrayOfCharacterStyle,
			CharSequence paramCharSequence) {
		SpannableString localSpannableString = new SpannableString(
				paramCharSequence);
		if ((paramArrayOfCharacterStyle != null)
				&& (paramArrayOfCharacterStyle.length > 0))
			localSpannableString.setSpan(paramArrayOfCharacterStyle[0], 0,
					localSpannableString.length(), 0);
		return localSpannableString;
	}

	private ObjectAnimator createHeightAnimation(boolean paramBoolean) {
		int i = ConversationItemViewCoordinates.getMinHeight(getContext(),
				this.mActivity.getViewMode());
		int j;
		if (paramBoolean) {
			j = 0;
			if (!paramBoolean)
				break label78;
		}
		while (true) {
			ObjectAnimator localObjectAnimator = ObjectAnimator.ofInt(this,
					"animatedHeight", new int[] { j, i });
			localObjectAnimator
					.setInterpolator(new DecelerateInterpolator(2.0F));
			localObjectAnimator.setDuration(sShrinkAnimationDuration);
			return localObjectAnimator;
			j = i;
			break;
			label78: i = 0;
		}
	}

	private SpannableStringBuilder createSubject(boolean paramBoolean1,
			boolean paramBoolean2) {
		String str1 = filterTag(this.mHeader.conversation.subject);
		String str2 = this.mHeader.conversation.getSnippet();
		SpannableStringBuilder localSpannableStringBuilder = Conversation
				.getSubjectAndSnippetForDisplay(this.mContext, str1, str2);
		TextAppearanceSpan localTextAppearanceSpan;
		int i;
		if (!TextUtils.isEmpty(str1)) {
			if (paramBoolean1) {
				localTextAppearanceSpan = sSubjectTextUnreadSpan;
				localSpannableStringBuilder.setSpan(
						TextAppearanceSpan.wrap(localTextAppearanceSpan), 0,
						str1.length(), 33);
			}
		} else if (!TextUtils.isEmpty(str2)) {
			boolean bool = TextUtils.isEmpty(str1);
			i = 0;
			if (!bool)
				i = 1 + str1.length();
			if (!paramBoolean1)
				break label141;
		}
		label141: for (ForegroundColorSpan localForegroundColorSpan = sSnippetTextUnreadSpan;; localForegroundColorSpan = sSnippetTextReadSpan) {
			localSpannableStringBuilder.setSpan(
					ForegroundColorSpan.wrap(localForegroundColorSpan), i,
					localSpannableStringBuilder.length(), 33);
			return localSpannableStringBuilder;
			localTextAppearanceSpan = sSubjectTextReadSpan;
			break;
		}
	}

	private ObjectAnimator createTranslateXAnimation(boolean paramBoolean)
  {
    SwipeableListView localSwipeableListView = getListView();
    int i;
    float f1;
    label22: float f2;
    if (localSwipeableListView != null)
    {
      i = localSwipeableListView.getMeasuredWidth();
      if (!paramBoolean)
        break label82;
      f1 = i;
      f2 = 0.0F;
      if (!paramBoolean)
        break label88;
    }
    while (true)
    {
      ObjectAnimator localObjectAnimator = ObjectAnimator.ofFloat(this, "translationX", new float[] { f1, f2 });
      localObjectAnimator.setInterpolator(new DecelerateInterpolator(2.0F));
      localObjectAnimator.setDuration(sSlideAnimationDuration);
      return localObjectAnimator;
      i = 0;
      break;
      label82: f1 = 0.0F;
      break label22;
      label88: f2 = i;
    }
  }

	private void drawSenders(Canvas paramCanvas) {
		paramCanvas.translate(this.mCoordinates.sendersX,
				this.mCoordinates.sendersY + sSendersTextViewTopPadding);
		this.mHeader.sendersTextView = getSendersTextView();
		if (this.mHeader.styledSendersString != null) {
			if ((!isActivated()) || (!showActivatedText()))
				break label158;
			this.mHeader.styledSendersString.setSpan(sActivatedTextSpan, 0,
					this.mHeader.styledMessageInfoStringOffset, 33);
		}
		while (true) {
			this.mHeader.sendersTextView.setText(
					this.mHeader.styledSendersString,
					TextView.BufferType.SPANNABLE);
			int i = View.MeasureSpec.makeMeasureSpec(this.mSendersWidth,
					1073741824);
			this.mHeader.sendersTextView.measure(i, sSendersTextViewHeight);
			this.mHeader.sendersTextView.layout(0, 0, this.mSendersWidth,
					sSendersTextViewHeight);
			this.mHeader.sendersTextView.draw(paramCanvas);
			return;
			label158: this.mHeader.styledSendersString
					.removeSpan(sActivatedTextSpan);
		}
	}

	private void drawSubject(Canvas paramCanvas) {
		TextView localTextView = getSubjectTextView();
		int i = this.mCoordinates.subjectWidth;
		int j = (int) (2 * localTextView.getLineHeight() + sPaint.descent());
		sPaint.setTextSize(this.mCoordinates.subjectFontSize);
		if ((isActivated()) && (showActivatedText()))
			this.mHeader.subjectText.setSpan(sActivatedTextSpan, 0,
					this.mHeader.subjectText.length(), 33);
		while (true) {
			paramCanvas.translate(this.mCoordinates.subjectX,
					this.mCoordinates.subjectY + sSendersTextViewTopPadding);
			localTextView.setText(this.mHeader.subjectText,
					TextView.BufferType.SPANNABLE);
			localTextView.measure(
					View.MeasureSpec.makeMeasureSpec(i, 1073741824), j);
			localTextView.layout(0, 0, i, j);
			localTextView.draw(paramCanvas);
			return;
			this.mHeader.subjectText.removeSpan(sActivatedTextSpan);
		}
	}

	private void drawText(Canvas paramCanvas, CharSequence paramCharSequence,
			int paramInt1, int paramInt2, TextPaint paramTextPaint) {
		paramCanvas.drawText(paramCharSequence, 0, paramCharSequence.length(),
				paramInt1, paramInt2, paramTextPaint);
	}

	private int ellipsize(int paramInt1, int paramInt2) {
		int i = 0;
		int j = 1;
		int k = 0;
		Iterator localIterator = this.mHeader.senderFragments.iterator();
		while (localIterator.hasNext()) {
			ConversationItemViewModel.SenderFragment localSenderFragment = (ConversationItemViewModel.SenderFragment) localIterator
					.next();
			CharacterStyle localCharacterStyle = localSenderFragment.style;
			int m = localSenderFragment.start;
			int n = localSenderFragment.end;
			int i1 = localSenderFragment.width;
			boolean bool = localSenderFragment.isFixed;
			localCharacterStyle.updateDrawState(sPaint);
			if ((k != 0) && (!bool)) {
				localSenderFragment.shouldDisplay = false;
			} else {
				localSenderFragment.ellipsizedText = null;
				if (bool)
					paramInt1 -= i1;
				if (!canFitFragment(i + i1, j, paramInt1)) {
					if (i != 0)
						break label303;
					k = 1;
					label141: if (k != 0) {
						int i3 = this.mSendersWidth - i;
						if (j == this.mCoordinates.sendersLineCount)
							i3 -= paramInt1;
						localSenderFragment.ellipsizedText = TextUtils
								.ellipsize(
										this.mHeader.sendersText
												.substring(m, n),
										sPaint, i3, TextUtils.TruncateAt.END)
								.toString();
						i1 = (int) sPaint
								.measureText(localSenderFragment.ellipsizedText);
					}
				}
				localSenderFragment.shouldDisplay = true;
				i += i1;
				if (localSenderFragment.ellipsizedText != null)
					;
				for (String str = localSenderFragment.ellipsizedText;; str = this.mHeader.sendersText
						.substring(m, n)) {
					int i2 = this.mHeader.sendersDisplayText.length();
					this.mHeader.sendersDisplayText.append(str);
					this.mHeader.sendersDisplayText.setSpan(
							localSenderFragment.style, i2,
							this.mHeader.sendersDisplayText.length(), 33);
					break;
					label303: if (j < this.mCoordinates.sendersLineCount) {
						j++;
						paramInt2 += this.mCoordinates.sendersLineHeight;
						int i4 = 0 + i1;
						int i5 = this.mSendersWidth;
						i = 0;
						if (i4 <= i5)
							break label141;
						k = 1;
						i = 0;
						break label141;
					}
					k = 1;
					break label141;
				}
			}
		}
		return i;
	}

	private int ellipsizeStyledSenders()
  {
    SpannableStringBuilder localSpannableStringBuilder1 = new SpannableStringBuilder();
    int i = 0;
    SpannableStringBuilder localSpannableStringBuilder2 = this.mHeader.messageInfoString;
    if (localSpannableStringBuilder2.length() > 0)
    {
      CharacterStyle[] arrayOfCharacterStyle2 = (CharacterStyle[])localSpannableStringBuilder2.getSpans(0, localSpannableStringBuilder2.length(), CharacterStyle.class);
      if (arrayOfCharacterStyle2.length > 0)
        arrayOfCharacterStyle2[0].updateDrawState(sPaint);
    }
    for (float f1 = 0.0F + sPaint.measureText(localSpannableStringBuilder2.toString()); ; f1 = 0.0F)
    {
      SpannableString[] arrayOfSpannableString = this.mHeader.styledSenders;
      int j = arrayOfSpannableString.length;
      int k = 0;
      float f2 = f1;
      Object localObject1 = null;
      Object localObject2;
      int m;
      float f5;
      while (k < j)
      {
        localObject2 = arrayOfSpannableString[k];
        if (localObject2 == null)
        {
          localObject2 = localObject1;
          m = i;
          f5 = f2;
          k++;
          f2 = f5;
          i = m;
          localObject1 = localObject2;
        }
        else
        {
          if (i == 0)
            break label179;
        }
      }
      this.mHeader.styledMessageInfoStringOffset = localSpannableStringBuilder1.length();
      if (localSpannableStringBuilder2 != null)
        localSpannableStringBuilder1.append(localSpannableStringBuilder2);
      this.mHeader.styledSendersString = localSpannableStringBuilder1;
      return (int)f2;
      label179: CharacterStyle[] arrayOfCharacterStyle1 = (CharacterStyle[])((SpannableString)localObject2).getSpans(0, ((SpannableString)localObject2).length(), CharacterStyle.class);
      if (arrayOfCharacterStyle1.length > 0)
        arrayOfCharacterStyle1[0].updateDrawState(sPaint);
      Object localObject3;
      label262: float f3;
      SpannableString localSpannableString;
      if (SendersView.sElidedString.equals(((SpannableString)localObject2).toString()))
      {
        localObject3 = copyStyles(arrayOfCharacterStyle1, sElidedPaddingToken + localObject2 + sElidedPaddingToken);
        if (arrayOfCharacterStyle1.length > 0)
          arrayOfCharacterStyle1[0].updateDrawState(sPaint);
        f3 = (int)sPaint.measureText(((SpannableString)localObject3).toString());
        if (f3 + f2 <= this.mSendersWidth)
          break label457;
        float f6 = this.mSendersWidth - f2;
        localSpannableString = copyStyles(arrayOfCharacterStyle1, TextUtils.ellipsize((CharSequence)localObject3, sPaint, f6, TextUtils.TruncateAt.END));
        float f7 = (int)sPaint.measureText(localSpannableString.toString());
        i = 1;
        f3 = f7;
      }
      while (true)
      {
        float f4 = f2 + f3;
        if (localSpannableString != null)
          localObject3 = localSpannableString;
        localSpannableStringBuilder1.append((CharSequence)localObject3);
        m = i;
        f5 = f4;
        break;
        if ((localSpannableStringBuilder1.length() > 0) && ((localObject1 == null) || (!SendersView.sElidedString.equals(localObject1.toString()))))
        {
          localObject3 = copyStyles(arrayOfCharacterStyle1, sSendersSplitToken + localObject2);
          break label262;
        }
        localObject3 = localObject2;
        break label262;
        label457: localSpannableString = null;
      }
    }
  }

	private String filterTag(String paramString) {
		String str1 = getContext().getResources().getString(2131427479);
		if ((!TextUtils.isEmpty(paramString)) && (paramString.charAt(0) == '[')) {
			int i = paramString.indexOf(']');
			if (i > 0) {
				String str2 = paramString.substring(1, i);
				Object[] arrayOfObject = new Object[2];
				arrayOfObject[0] = Utils.ellipsize(str2, 7);
				arrayOfObject[1] = paramString.substring(i + 1);
				paramString = String.format(str1, arrayOfObject);
			}
		}
		return paramString;
	}

	private static HtmlTreeBuilder getBuilder() {
		if (sHtmlBuilder == null)
			sHtmlBuilder = new HtmlTreeBuilder();
		return sHtmlBuilder;
	}

	private Bitmap getDateBackground(boolean paramBoolean) {
		if (paramBoolean)
			;
		int j;
		for (int i = this.mPaperclipX;; i = this.mDateX) {
			j = i - sDateBackgroundPaddingLeft;
			if (!paramBoolean)
				break;
			if (sDateBackgroundAttachment == null)
				sDateBackgroundAttachment = Bitmap.createScaledBitmap(
						DATE_BACKGROUND, this.mViewWidth - j,
						sDateBackgroundHeight, false);
			return sDateBackgroundAttachment;
		}
		if (sDateBackgroundNoAttachment == null)
			sDateBackgroundNoAttachment = Bitmap.createScaledBitmap(
					DATE_BACKGROUND, this.mViewWidth - j,
					sDateBackgroundHeight, false);
		return sDateBackgroundNoAttachment;
	}

	private int getFontColor(int paramInt) {
		if ((isActivated()) && (showActivatedText()))
			;
		for (int i = 1;; i = 0) {
			if (i != 0)
				paramInt = sActivatedTextColor;
			return paramInt;
		}
	}

	private SwipeableListView getListView() {
		SwipeableListView localSwipeableListView = (SwipeableListView) ((SwipeableConversationItemView) getParent())
				.getListView();
		if (localSwipeableListView == null)
			localSwipeableListView = this.mAdapter.getListView();
		return localSwipeableListView;
	}

	private static int getPadding(int paramInt1, int paramInt2) {
		return (paramInt1 - paramInt2) / 2;
	}

	private static HtmlParser getParser() {
		if (sHtmlParser == null)
			sHtmlParser = new HtmlParser();
		return sHtmlParser;
	}

	private TextView getSendersTextView() {
		if (sSendersTextView == null) {
			TextView localTextView = new TextView(this.mContext);
			localTextView.setMaxLines(1);
			localTextView.setEllipsize(TextUtils.TruncateAt.END);
			localTextView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
			sSendersTextView = localTextView;
		}
		return sSendersTextView;
	}

	private Bitmap getStarBitmap() {
		if (this.mHeader.conversation.starred)
			return STAR_ON;
		return STAR_OFF;
	}

	private TextView getSubjectTextView() {
		if (sSubjectTextView == null) {
			TextView localTextView = new TextView(this.mContext);
			localTextView.setEllipsize(TextUtils.TruncateAt.END);
			localTextView.setLayoutParams(new ViewGroup.LayoutParams(-2, -2));
			sSubjectTextView = localTextView;
		}
		return sSubjectTextView;
	}

	private boolean isTouchInCheckmark(float paramFloat1, float paramFloat2) {
		return (this.mHeader.checkboxVisible)
				&& (paramFloat1 < this.mCoordinates.sendersX + sTouchSlop);
	}

	private boolean isTouchInStar(float paramFloat1, float paramFloat2) {
		return (this.mStarEnabled)
				&& (paramFloat1 > this.mCoordinates.starX - sTouchSlop);
	}

	private void layoutSubjectSpans(boolean paramBoolean) {
		this.mHeader.subjectText = createSubject(paramBoolean, false);
	}

	private int measureHeight(int paramInt1, int paramInt2) {
		int i = View.MeasureSpec.getMode(paramInt1);
		int j = View.MeasureSpec.getSize(paramInt1);
		if (i == 1073741824)
			return j;
		int k = ConversationItemViewCoordinates.getHeight(this.mContext,
				paramInt2);
		if (i == -2147483648)
			return Math.min(k, j);
		return k;
	}

	private boolean onTouchEventNoSwipe(MotionEvent paramMotionEvent) {
		int i = (int) paramMotionEvent.getX();
		int j = (int) paramMotionEvent.getY();
		this.mLastTouchX = i;
		this.mLastTouchY = j;
		int k = paramMotionEvent.getAction();
		boolean bool1 = false;
		switch (k) {
		case 2:
		default:
		case 0:
		case 3:
		case 1:
		}
		boolean bool2;
		do {
			while (true) {
				if (!bool1)
					bool1 = super.onTouchEvent(paramMotionEvent);
				return bool1;
				if (!isTouchInCheckmark(i, j)) {
					boolean bool3 = isTouchInStar(i, j);
					bool1 = false;
					if (!bool3)
						;
				} else {
					this.mDownEvent = true;
					bool1 = true;
					continue;
					this.mDownEvent = false;
					bool1 = false;
				}
			}
			bool2 = this.mDownEvent;
			bool1 = false;
		} while (!bool2);
		if (isTouchInCheckmark(i, j))
			toggleCheckMark();
		while (true) {
			bool1 = true;
			break;
			if (isTouchInStar(i, j))
				toggleStar();
		}
	}

	private static void pauseTimer(String paramString) {
		if (sTimer != null)
			sTimer.pause(paramString);
	}

	private void setCheckedActivatedBackground() {
		if ((isActivated()) && (this.mTabletDevice)) {
			setBackgroundResource(2130837610);
			return;
		}
		setBackgroundResource(2130837626);
	}

	private void setContentDescription() {
		if (this.mActivity.isAccessibilityEnabled()) {
			this.mHeader.resetContentDescription();
			setContentDescription(this.mHeader
					.getContentDescription(this.mContext));
		}
	}

	private boolean showActivatedText() {
		boolean bool = this.mContext.getResources().getBoolean(2131623936);
		return (this.mTabletDevice) && (!bool);
	}

	private static void startTimer(String paramString) {
		if (sTimer != null)
			sTimer.start(paramString);
	}

	private void toggleCheckMark() {
		boolean bool;
		Conversation localConversation;
		SwipeableListView localSwipeableListView;
		if ((this.mHeader != null) && (this.mHeader.conversation != null)) {
			if (this.mChecked)
				break label104;
			bool = true;
			this.mChecked = bool;
			localConversation = this.mHeader.conversation;
			localSwipeableListView = getListView();
			if ((!this.mChecked) || (localSwipeableListView == null))
				break label109;
		}
		label104: label109: for (int i = localSwipeableListView
				.getPositionForView(this);; i = -1) {
			localConversation.position = i;
			if (this.mSelectedConversationSet != null)
				this.mSelectedConversationSet.toggle(this, localConversation);
			if (this.mSelectedConversationSet.isEmpty())
				localSwipeableListView.commitDestructiveActions(true);
			requestLayout();
			return;
			bool = false;
			break;
		}
	}

	private void updateBackground(boolean paramBoolean) {
		if (this.mBackgroundOverride != -1) {
			setBackgroundColor(this.mBackgroundOverride);
			return;
		}
		int i;
		if ((this.mTabletDevice) && (this.mActivity.getViewMode().isListMode()))
			i = 1;
		while (paramBoolean)
			if (i != 0) {
				if (this.mChecked) {
					setBackgroundResource(2130837618);
					return;
					i = 0;
				} else {
					setBackgroundResource(2130837530);
				}
			} else {
				if (this.mChecked) {
					setCheckedActivatedBackground();
					return;
				}
				setBackgroundResource(2130837528);
				return;
			}
		if (i != 0) {
			if (this.mChecked) {
				setBackgroundResource(2130837614);
				return;
			}
			setBackgroundResource(2130837529);
			return;
		}
		if (this.mChecked) {
			setCheckedActivatedBackground();
			return;
		}
		setBackgroundResource(2130837527);
	}

	public void bind(Cursor paramCursor,
			ControllableActivity paramControllableActivity,
			ConversationSelectionSet paramConversationSelectionSet,
			Folder paramFolder, boolean paramBoolean1, boolean paramBoolean2,
			boolean paramBoolean3, AnimatedAdapter paramAnimatedAdapter) {
		bind(ConversationItemViewModel.forCursor(this.mAccount, paramCursor),
				paramControllableActivity, paramConversationSelectionSet,
				paramFolder, paramBoolean1, paramBoolean2, paramBoolean3,
				paramAnimatedAdapter);
	}

	public void bind(Conversation paramConversation,
			ControllableActivity paramControllableActivity,
			ConversationSelectionSet paramConversationSelectionSet,
			Folder paramFolder, boolean paramBoolean1, boolean paramBoolean2,
			boolean paramBoolean3, AnimatedAdapter paramAnimatedAdapter) {
		bind(ConversationItemViewModel.forConversation(this.mAccount,
				paramConversation), paramControllableActivity,
				paramConversationSelectionSet, paramFolder, paramBoolean1,
				paramBoolean2, paramBoolean3, paramAnimatedAdapter);
	}

	public boolean canChildBeDismissed() {
		return true;
	}

	public void dismiss() {
		if (getListView() != null)
			getListView().dismissChild(this);
	}

	public Conversation getConversation() {
		return this.mHeader.conversation;
	}

	public float getMinAllowScrollDistance() {
		return sScrollSlop;
	}

	public View getSwipeableView() {
		return this;
	}

	public boolean onDragEvent(DragEvent paramDragEvent) {
		switch (paramDragEvent.getAction()) {
		default:
			return false;
		case 4:
		}
		this.mActivity.stopDragMode();
		return true;
	}

	protected void onDraw(Canvas paramCanvas)
  {
    Bitmap localBitmap;
    int i3;
    label159: label212: int k;
    label441: int n;
    if (this.mHeader.checkboxVisible)
    {
      if (this.mChecked)
      {
        localBitmap = CHECKMARK_ON;
        paramCanvas.drawBitmap(localBitmap, this.mCoordinates.checkmarkX, this.mCoordinates.checkmarkY, sPaint);
      }
    }
    else
    {
      if ((this.mCoordinates.showPersonalLevel) && (this.mHeader.personalLevelBitmap != null))
        paramCanvas.drawBitmap(this.mHeader.personalLevelBitmap, this.mCoordinates.personalLevelX, this.mCoordinates.personalLevelY, sPaint);
      boolean bool = this.mHeader.unread;
      paramCanvas.save();
      if (this.mHeader.sendersDisplayLayout == null)
        break label837;
      sPaint.setTextSize(this.mCoordinates.sendersFontSize);
      sPaint.setTypeface(SendersView.getTypeface(bool));
      TextPaint localTextPaint = sPaint;
      if (!bool)
        break label829;
      i3 = sSendersTextColorUnread;
      localTextPaint.setColor(getFontColor(i3));
      paramCanvas.translate(this.mCoordinates.sendersX, this.mCoordinates.sendersY + this.mHeader.sendersDisplayLayout.getTopPadding());
      this.mHeader.sendersDisplayLayout.draw(paramCanvas);
      paramCanvas.restore();
      sPaint.setTypeface(Typeface.DEFAULT);
      paramCanvas.save();
      drawSubject(paramCanvas);
      paramCanvas.restore();
      if ((this.mCoordinates.showFolders) && (this.mHeader.folderDisplayer != null))
        this.mHeader.folderDisplayer.drawFolders(paramCanvas, this.mCoordinates, this.mFoldersXEnd, this.mMode);
      if (this.mHeader.conversation.color != 0)
      {
        sFoldersPaint.setColor(this.mHeader.conversation.color);
        sFoldersPaint.setStyle(Paint.Style.FILL);
        int i1 = ConversationItemViewCoordinates.getColorBlockWidth(this.mContext);
        int i2 = ConversationItemViewCoordinates.getColorBlockHeight(this.mContext);
        paramCanvas.drawRect(this.mCoordinates.dateXEnd - i1, 0.0F, this.mCoordinates.dateXEnd, i2, sFoldersPaint);
      }
      if ((isActivated()) || ((!this.mHeader.conversation.hasAttachments) && ((this.mHeader.folderDisplayer == null) || (!this.mHeader.folderDisplayer.hasVisibleFolders()))) || (!ConversationItemViewCoordinates.showAttachmentBackground(this.mMode)))
        break label866;
      if (!this.mHeader.conversation.hasAttachments)
        break label845;
      k = this.mPaperclipX;
      int m = k - sDateBackgroundPaddingLeft;
      if (!this.mCoordinates.showFolders)
        break label854;
      n = this.mCoordinates.foldersY;
      label468: this.mHeader.dateBackground = getDateBackground(this.mHeader.conversation.hasAttachments);
      paramCanvas.drawBitmap(this.mHeader.dateBackground, m, n, sPaint);
      label509: if (this.mCoordinates.showReplyState)
      {
        if ((!this.mHeader.hasBeenRepliedTo) || (!this.mHeader.hasBeenForwarded))
          break label877;
        paramCanvas.drawBitmap(STATE_REPLIED_AND_FORWARDED, this.mCoordinates.replyStateX, this.mCoordinates.replyStateY, null);
      }
    }
    while (true)
    {
      sPaint.setTextSize(this.mCoordinates.dateFontSize);
      sPaint.setTypeface(Typeface.DEFAULT);
      sPaint.setColor(sDateTextColor);
      drawText(paramCanvas, this.mHeader.dateText, this.mDateX, this.mCoordinates.dateY - this.mCoordinates.dateAscent, sPaint);
      if (this.mHeader.paperclip != null)
        paramCanvas.drawBitmap(this.mHeader.paperclip, this.mPaperclipX, this.mCoordinates.paperclipY, sPaint);
      if (this.mHeader.faded)
      {
        if (sFadedActivatedColor == -1)
          sFadedActivatedColor = this.mContext.getResources().getColor(2131230736);
        int i = sFadedActivatedColor;
        int j = paramCanvas.save();
        Rect localRect = paramCanvas.getClipBounds();
        paramCanvas.clipRect(localRect.left, localRect.top, localRect.right - this.mContext.getResources().getDimensionPixelSize(2131361833), localRect.bottom);
        paramCanvas.drawARGB(Color.alpha(i), Color.red(i), Color.green(i), Color.blue(i));
        paramCanvas.restoreToCount(j);
      }
      if (this.mStarEnabled)
        paramCanvas.drawBitmap(getStarBitmap(), this.mCoordinates.starX, this.mCoordinates.starY, sPaint);
      return;
      localBitmap = CHECKMARK_OFF;
      break;
      label829: i3 = sSendersTextColorRead;
      break label159;
      label837: drawSenders(paramCanvas);
      break label212;
      label845: k = this.mDateX;
      break label441;
      label854: n = this.mCoordinates.dateY;
      break label468;
      label866: this.mHeader.dateBackground = null;
      break label509;
      label877: if (this.mHeader.hasBeenRepliedTo)
        paramCanvas.drawBitmap(STATE_REPLIED, this.mCoordinates.replyStateX, this.mCoordinates.replyStateY, null);
      else if (this.mHeader.hasBeenForwarded)
        paramCanvas.drawBitmap(STATE_FORWARDED, this.mCoordinates.replyStateX, this.mCoordinates.replyStateY, null);
      else if (this.mHeader.isInvite)
        paramCanvas.drawBitmap(STATE_CALENDAR_INVITE, this.mCoordinates.replyStateX, this.mCoordinates.replyStateY, null);
    }
  }

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		startTimer("CCHV.layout");
		super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
		int i = paramInt3 - paramInt1;
		int j = this.mActivity.getViewMode().getMode();
		if ((i != this.mViewWidth) || (this.mPreviousMode != j)) {
			this.mViewWidth = i;
			this.mPreviousMode = j;
			if (!this.mTesting)
				this.mMode = ConversationItemViewCoordinates.getMode(
						this.mContext, this.mPreviousMode);
		}
		this.mHeader.viewWidth = this.mViewWidth;
		Resources localResources = getResources();
		this.mHeader.standardScaledDimen = localResources
				.getDimensionPixelOffset(2131361831);
		if (this.mHeader.standardScaledDimen != sStandardScaledDimen) {
			sStandardScaledDimen = this.mHeader.standardScaledDimen;
			ConversationItemViewCoordinates
					.refreshConversationHeights(this.mContext);
			sDateBackgroundHeight = localResources
					.getDimensionPixelSize(2131361827);
		}
		this.mCoordinates = ConversationItemViewCoordinates.forWidth(
				this.mContext, this.mViewWidth, this.mMode,
				this.mHeader.standardScaledDimen);
		calculateTextsAndBitmaps();
		calculateCoordinates();
		if (!this.mHeader.isLayoutValid(this.mContext))
			setContentDescription();
		this.mHeader.validate(this.mContext);
		pauseTimer("CCHV.layout");
		if (sTimer != null) {
			int k = 1 + sLayoutCount;
			sLayoutCount = k;
			if (k >= 50) {
				sTimer.dumpResults();
				sTimer = new Timer();
				sLayoutCount = 0;
			}
		}
	}

	protected void onMeasure(int paramInt1, int paramInt2) {
		if (this.mAnimatedHeight == -1) {
			setMeasuredDimension(
					paramInt1,
					measureHeight(paramInt2, ConversationItemViewCoordinates
							.getMode(this.mContext,
									this.mActivity.getViewMode())));
			return;
		}
		setMeasuredDimension(View.MeasureSpec.getSize(paramInt1),
				this.mAnimatedHeight);
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		int i = (int) paramMotionEvent.getX();
		int j = (int) paramMotionEvent.getY();
		this.mLastTouchX = i;
		this.mLastTouchY = j;
		boolean bool;
		if (!this.mSwipeEnabled) {
			bool = onTouchEventNoSwipe(paramMotionEvent);
			return bool;
		}
		switch (paramMotionEvent.getAction()) {
		default:
		case 0:
		case 1:
		}
		do {
			do {
				do {
					bool = super.onTouchEvent(paramMotionEvent);
					if (paramMotionEvent.getAction() != 0)
						break;
					return true;
				} while ((!isTouchInCheckmark(i, j)) && (!isTouchInStar(i, j)));
				this.mDownEvent = true;
				return true;
			} while (!this.mDownEvent);
			if (isTouchInCheckmark(i, j)) {
				this.mDownEvent = false;
				toggleCheckMark();
				return true;
			}
		} while (!isTouchInStar(i, j));
		this.mDownEvent = false;
		toggleStar();
		return true;
	}

	public boolean performClick() {
		boolean bool = super.performClick();
		SwipeableListView localSwipeableListView = getListView();
		if ((localSwipeableListView != null)
				&& (localSwipeableListView.getAdapter() != null))
			localSwipeableListView.performItemClick(this,
					localSwipeableListView.findConversation(this,
							this.mHeader.conversation),
					this.mHeader.conversation.id);
		return bool;
	}

	public void reset() {
		this.mBackgroundOverride = -1;
		setAlpha(1.0F);
		setTranslationX(0.0F);
		setAnimatedHeight(-1);
		setMinimumHeight(ConversationItemViewCoordinates.getMinHeight(
				this.mContext, this.mActivity.getViewMode()));
	}

	public void setAnimatedHeight(int paramInt) {
		this.mAnimatedHeight = paramInt;
		requestLayout();
	}

	public void setBackgroundResource(int paramInt) {
		Drawable localDrawable = (Drawable) this.mBackgrounds.get(paramInt);
		if (localDrawable == null) {
			localDrawable = getResources().getDrawable(paramInt);
			this.mBackgrounds.put(paramInt, localDrawable);
		}
		if (getBackground() != localDrawable)
			super.setBackgroundDrawable(localDrawable);
	}

	public void setItemAlpha(float paramFloat) {
		setAlpha(paramFloat);
		invalidate();
	}

	void setMode(int paramInt) {
		this.mMode = paramInt;
		this.mTesting = true;
	}

	public void startDestroyAnimation(
			Animator.AnimatorListener paramAnimatorListener) {
		ObjectAnimator localObjectAnimator = createHeightAnimation(false);
		int i = ConversationItemViewCoordinates.getMinHeight(this.mContext,
				this.mActivity.getViewMode());
		setMinimumHeight(0);
		this.mBackgroundOverride = sAnimatingBackgroundColor;
		setBackgroundColor(this.mBackgroundOverride);
		this.mAnimatedHeight = i;
		localObjectAnimator.addListener(paramAnimatorListener);
		localObjectAnimator.start();
	}

	public void startDestroyWithSwipeAnimation(
			Animator.AnimatorListener paramAnimatorListener) {
		ObjectAnimator localObjectAnimator1 = createTranslateXAnimation(false);
		ObjectAnimator localObjectAnimator2 = createHeightAnimation(false);
		AnimatorSet localAnimatorSet = new AnimatorSet();
		localAnimatorSet.playSequentially(new Animator[] {
				localObjectAnimator1, localObjectAnimator2 });
		localAnimatorSet.addListener(paramAnimatorListener);
		localAnimatorSet.start();
	}

	public void startSwipeUndoAnimation(ViewMode paramViewMode,
			Animator.AnimatorListener paramAnimatorListener) {
		ObjectAnimator localObjectAnimator = createTranslateXAnimation(true);
		localObjectAnimator.addListener(paramAnimatorListener);
		localObjectAnimator.start();
	}

	public void startUndoAnimation(ViewMode paramViewMode,
			Animator.AnimatorListener paramAnimatorListener) {
		setMinimumHeight(ConversationItemViewCoordinates.getMinHeight(
				this.mContext, paramViewMode));
		this.mAnimatedHeight = 0;
		ObjectAnimator localObjectAnimator1 = createHeightAnimation(true);
		ObjectAnimator localObjectAnimator2 = ObjectAnimator.ofFloat(this,
				"itemAlpha", new float[] { 0.0F, 1.0F });
		localObjectAnimator2.setDuration(sShrinkAnimationDuration);
		localObjectAnimator2.setInterpolator(new DecelerateInterpolator(2.0F));
		AnimatorSet localAnimatorSet = new AnimatorSet();
		localAnimatorSet.playTogether(new Animator[] { localObjectAnimator1,
				localObjectAnimator2 });
		localAnimatorSet.addListener(paramAnimatorListener);
		localAnimatorSet.start();
	}

	public void toggleCheckMarkOrBeginDrag() {
		ViewMode localViewMode = this.mActivity.getViewMode();
		if ((!this.mTabletDevice) || (!localViewMode.isListMode())) {
			toggleCheckMark();
			return;
		}
		beginDragMode();
	}

	public void toggleStar() {
		Conversation localConversation = this.mHeader.conversation;
		if (!this.mHeader.conversation.starred)
			;
		for (boolean bool = true;; bool = false) {
			localConversation.starred = bool;
			Bitmap localBitmap = getStarBitmap();
			postInvalidate(this.mCoordinates.starX, this.mCoordinates.starY,
					this.mCoordinates.starX + localBitmap.getWidth(),
					this.mCoordinates.starY + localBitmap.getHeight());
			((ConversationCursor) this.mAdapter.getCursor()).updateBoolean(
					this.mContext, this.mHeader.conversation,
					UIProvider.ConversationColumns.STARRED,
					this.mHeader.conversation.starred);
			return;
		}
	}

	static class ConversationItemFolderDisplayer extends FolderDisplayer {
		private int mFoldersCount;
		private boolean mHasMoreFolders;

		public ConversationItemFolderDisplayer(Context paramContext) {
			super();
		}

		private int measureFolders(int paramInt) {
			int i = ConversationItemViewCoordinates.getFoldersWidth(
					this.mContext, paramInt);
			int j = ConversationItemViewCoordinates.getFolderCellWidth(
					this.mContext, paramInt, this.mFoldersCount);
			int k = 0;
			Iterator localIterator = this.mFoldersSortedSet.iterator();
			do {
				if (!localIterator.hasNext())
					break;
				String str = ((Folder) localIterator.next()).name;
				int m = j
						+ (int) ConversationItemView.sFoldersPaint
								.measureText(str);
				if (m % j != 0)
					m += j - m % j;
				k += m;
			} while (k <= i);
			return k;
		}

		public void drawFolders(
				Canvas paramCanvas,
				ConversationItemViewCoordinates paramConversationItemViewCoordinates,
				int paramInt1, int paramInt2) {
			if (this.mFoldersCount == 0)
				return;
			int i;
			int j;
			int k;
			int m;
			int n;
			int i1;
			int i2;
			int i3;
			int i4;
			Iterator localIterator;
			while (!localIterator.hasNext()) {
				i = paramConversationItemViewCoordinates.foldersY
						- paramConversationItemViewCoordinates.foldersAscent;
				j = paramConversationItemViewCoordinates.foldersHeight;
				k = paramConversationItemViewCoordinates.foldersTopPadding;
				m = paramConversationItemViewCoordinates.foldersAscent;
				ConversationItemView.sFoldersPaint
						.setTextSize(paramConversationItemViewCoordinates.foldersFontSize);
				n = ConversationItemViewCoordinates.getFoldersWidth(
						this.mContext, paramInt2);
				i1 = n / this.mFoldersCount;
				i2 = ConversationItemViewCoordinates.getFolderCellWidth(
						this.mContext, paramInt2, this.mFoldersCount);
				i3 = measureFolders(paramInt2);
				i4 = paramInt1 - Math.min(n, i3);
				localIterator = this.mFoldersSortedSet.iterator();
			}
			Folder localFolder = (Folder) localIterator.next();
			String str = localFolder.name;
			int i5 = localFolder.getForegroundColor(this.mDefaultFgColor);
			int i6 = localFolder.getBackgroundColor(this.mDefaultBgColor);
			int i7 = i2
					+ (int) ConversationItemView.sFoldersPaint.measureText(str);
			if (i7 % i2 != 0)
				i7 += i2 - i7 % i2;
			int i8 = 0;
			if (i3 > n) {
				int i13 = i7;
				i8 = 0;
				if (i13 > i1) {
					i7 = i1;
					i8 = 1;
				}
			}
			ConversationItemView.sFoldersPaint.setColor(i6);
			ConversationItemView.sFoldersPaint
					.setStyle(Paint.Style.FILL_AND_STROKE);
			paramCanvas.drawRect(i4, i + m, i4 + i7, j + (i + m),
					ConversationItemView.sFoldersPaint);
			int i9 = (int) ConversationItemView.sFoldersPaint.measureText(str);
			int i10 = ConversationItemView.getPadding(i7, i9);
			if (i8 != 0) {
				TextPaint localTextPaint = new TextPaint();
				localTextPaint.setColor(i5);
				localTextPaint
						.setTextSize(paramConversationItemViewCoordinates.foldersFontSize);
				int i11 = i2 / 2;
				int i12 = i4 + i7 - i11;
				localTextPaint.setShader(new LinearGradient(i12 - i11, i, i12,
						i, i5, Utils.getTransparentColor(i5),
						Shader.TileMode.CLAMP));
				paramCanvas.drawText(str, i4 + i11, i + k, localTextPaint);
			}
			while (true) {
				n -= i7;
				i4 += i7;
				if ((n > 0) || (!this.mHasMoreFolders))
					break;
				paramCanvas.drawBitmap(ConversationItemView.MORE_FOLDERS,
						paramInt1, i + m, ConversationItemView.sFoldersPaint);
				return;
				ConversationItemView.sFoldersPaint.setColor(i5);
				paramCanvas.drawText(str, i4 + i10, i + k,
						ConversationItemView.sFoldersPaint);
			}
		}

		public boolean hasVisibleFolders() {
			return this.mFoldersCount > 0;
		}

		public void loadConversationFolders(Conversation paramConversation,
				Folder paramFolder) {
			super.loadConversationFolders(paramConversation, paramFolder);
			this.mFoldersCount = this.mFoldersSortedSet.size();
			if (this.mFoldersCount > 4)
				;
			for (boolean bool = true;; bool = false) {
				this.mHasMoreFolders = bool;
				this.mFoldersCount = Math.min(this.mFoldersCount, 4);
				return;
			}
		}
	}

	private class ShadowBuilder extends View.DragShadowBuilder {
		private final Drawable mBackground;
		private final String mDragDesc;
		private int mDragDescX;
		private int mDragDescY;
		private final int mTouchX;
		private final int mTouchY;
		private final View mView;

		public ShadowBuilder(View paramInt1, int paramInt2, int paramInt3,
				int arg5) {
			super();
			this.mView = paramInt1;
			this.mBackground = this.mView.getResources()
					.getDrawable(2130837623);
			this.mDragDesc = Utils.formatPlural(this.mView.getContext(),
					2131755009, paramInt2);
			this.mTouchX = paramInt3;
			int i;
			this.mTouchY = i;
		}

		public void onDrawShadow(Canvas paramCanvas) {
			this.mBackground.setBounds(0, 0, this.mView.getWidth(),
					this.mView.getHeight());
			this.mBackground.draw(paramCanvas);
			ConversationItemView.sPaint
					.setTextSize(ConversationItemView.this.mCoordinates.subjectFontSize);
			paramCanvas.drawText(this.mDragDesc, this.mDragDescX,
					this.mDragDescY, ConversationItemView.sPaint);
		}

		public void onProvideShadowMetrics(Point paramPoint1, Point paramPoint2) {
			int i = this.mView.getWidth();
			int j = this.mView.getHeight();
			this.mDragDescX = ConversationItemView.this.mCoordinates.sendersX;
			this.mDragDescY = (ConversationItemView.getPadding(j,
					ConversationItemView.this.mCoordinates.subjectFontSize) - ConversationItemView.this.mCoordinates.subjectAscent);
			paramPoint1.set(i, j);
			paramPoint2.set(this.mTouchX, this.mTouchY);
		}
	}
}

/*
 * Location:
 * C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name: com.android.mail.browse.ConversationItemView JD-Core Version:
 * 0.6.2
 */