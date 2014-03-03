package com.android.mail.browse;

import android.content.Context;
import android.content.res.Resources;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.android.mail.providers.Account;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Settings;
import com.android.mail.ui.FolderDisplayer;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.util.Iterator;
import java.util.SortedSet;

public class ConversationViewHeader extends RelativeLayout
  implements View.OnClickListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private ConversationAccountController mAccountController;
  private ConversationViewHeaderCallbacks mCallbacks;
  private ConversationFolderDisplayer mFolderDisplayer;
  private FolderSpanTextView mFoldersView;
  private ConversationViewAdapter.ConversationHeaderItem mHeaderItem;
  private TextView mSubjectView;

  public ConversationViewHeader(Context paramContext)
  {
    this(paramContext, null);
  }

  public ConversationViewHeader(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private static int getTotalMeasuredChildWidth(View paramView)
  {
    RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)paramView.getLayoutParams();
    return paramView.getMeasuredWidth() + localLayoutParams.leftMargin + localLayoutParams.rightMargin;
  }

  private int measureHeight()
  {
    ViewGroup localViewGroup = (ViewGroup)getParent();
    if (localViewGroup == null)
    {
      LogUtils.e(LOG_TAG, "Unable to measure height of conversation header", new Object[0]);
      return getHeight();
    }
    return Utils.measureViewHeight(this, localViewGroup);
  }

  public void bind(ConversationViewAdapter.ConversationHeaderItem paramConversationHeaderItem)
  {
    this.mHeaderItem = paramConversationHeaderItem;
  }

  public void onClick(View paramView)
  {
    if ((2131689503 == paramView.getId()) && (this.mCallbacks != null))
      this.mCallbacks.onFoldersClicked();
  }

  public void onConversationUpdated(Conversation paramConversation)
  {
    setFolders(paramConversation);
    if (this.mHeaderItem != null)
    {
      int i = measureHeight();
      if (this.mHeaderItem.setHeight(i))
        this.mCallbacks.onConversationViewHeaderHeightChange(i);
    }
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mSubjectView = ((TextView)findViewById(2131689537));
    this.mFoldersView = ((FolderSpanTextView)findViewById(2131689503));
    this.mFoldersView.setOnClickListener(this);
    this.mFolderDisplayer = new ConversationFolderDisplayer(getContext(), this.mFoldersView);
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    if (getTotalMeasuredChildWidth(this.mSubjectView) + getTotalMeasuredChildWidth(this.mFoldersView) + getPaddingLeft() + getPaddingRight() > getMeasuredWidth())
    {
      RelativeLayout.LayoutParams localLayoutParams = (RelativeLayout.LayoutParams)this.mFoldersView.getLayoutParams();
      localLayoutParams.addRule(3, 2131689537);
      localLayoutParams.addRule(4, 0);
      super.onMeasure(paramInt1, paramInt2);
    }
  }

  public void setCallbacks(ConversationViewHeaderCallbacks paramConversationViewHeaderCallbacks, ConversationAccountController paramConversationAccountController)
  {
    this.mCallbacks = paramConversationViewHeaderCallbacks;
    this.mAccountController = paramConversationAccountController;
  }

  public void setFolders(Conversation paramConversation)
  {
    setFoldersVisible(true);
    SpannableStringBuilder localSpannableStringBuilder = new SpannableStringBuilder();
    if ((this.mAccountController.getAccount().settings.priorityArrowsEnabled) && (paramConversation.isImportant()))
    {
      localSpannableStringBuilder.append('.');
      localSpannableStringBuilder.setSpan(new PriorityIndicatorSpan(getContext(), 2130837571, this.mFoldersView.getPadding(), 0), 0, 1, 17);
    }
    this.mFolderDisplayer.loadConversationFolders(paramConversation, null);
    this.mFolderDisplayer.appendFolderSpans(localSpannableStringBuilder);
    this.mFoldersView.setText(localSpannableStringBuilder);
  }

  public void setFoldersVisible(boolean paramBoolean)
  {
    FolderSpanTextView localFolderSpanTextView = this.mFoldersView;
    if (paramBoolean);
    for (int i = 0; ; i = 8)
    {
      localFolderSpanTextView.setVisibility(i);
      return;
    }
  }

  public void setSubject(String paramString)
  {
    String str = paramString;
    if ((this.mCallbacks != null) && (this.mCallbacks.getSubjectRemainder(paramString) == null))
      str = null;
    this.mSubjectView.setText(str);
    if (TextUtils.isEmpty(str))
      this.mSubjectView.setVisibility(8);
  }

  private static class ConversationFolderDisplayer extends FolderDisplayer
  {
    private FolderSpan.FolderSpanDimensions mDims;

    public ConversationFolderDisplayer(Context paramContext, FolderSpan.FolderSpanDimensions paramFolderSpanDimensions)
    {
      super();
      this.mDims = paramFolderSpanDimensions;
    }

    private void addSpan(SpannableStringBuilder paramSpannableStringBuilder, Folder paramFolder)
    {
      int i = paramSpannableStringBuilder.length();
      paramSpannableStringBuilder.append(paramFolder.name);
      int j = paramSpannableStringBuilder.length();
      int k = paramFolder.getForegroundColor(this.mDefaultFgColor);
      paramSpannableStringBuilder.setSpan(new BackgroundColorSpan(paramFolder.getBackgroundColor(this.mDefaultBgColor)), i, j, 33);
      paramSpannableStringBuilder.setSpan(new ForegroundColorSpan(k), i, j, 33);
      paramSpannableStringBuilder.setSpan(new FolderSpan(paramSpannableStringBuilder, this.mDims), i, j, 33);
    }

    public void appendFolderSpans(SpannableStringBuilder paramSpannableStringBuilder)
    {
      Iterator localIterator = this.mFoldersSortedSet.iterator();
      while (localIterator.hasNext())
        addSpan(paramSpannableStringBuilder, (Folder)localIterator.next());
      if (this.mFoldersSortedSet.isEmpty())
      {
        Folder localFolder = Folder.newUnsafeInstance();
        Resources localResources = this.mContext.getResources();
        localFolder.name = localResources.getString(2131427503);
        localFolder.bgColor = ("" + localResources.getColor(2131230751));
        localFolder.fgColor = ("" + localResources.getColor(2131230750));
        addSpan(paramSpannableStringBuilder, localFolder);
      }
    }
  }

  public static abstract interface ConversationViewHeaderCallbacks
  {
    public abstract String getSubjectRemainder(String paramString);

    public abstract void onConversationViewHeaderHeightChange(int paramInt);

    public abstract void onFoldersClicked();
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.ConversationViewHeader
 * JD-Core Version:    0.6.2
 */