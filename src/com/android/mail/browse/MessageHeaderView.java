package com.android.mail.browse;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.provider.ContactsContract.StatusUpdates;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.QuickContactBadge;
import android.widget.TextView;
import android.widget.Toast;
import com.android.mail.ContactInfo;
import com.android.mail.ContactInfoSource;
import com.android.mail.FormattedDateBuilder;
import com.android.mail.compose.ComposeActivity;
import com.android.mail.perf.Timer;
import com.android.mail.providers.Account;
import com.android.mail.providers.Address;
import com.android.mail.providers.Conversation;
import com.android.mail.providers.Folder;
import com.android.mail.providers.Message;
import com.android.mail.providers.Settings;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class MessageHeaderView extends LinearLayout
  implements View.OnClickListener, PopupMenu.OnMenuItemClickListener, ConversationContainer.DetachListener
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private ConversationAccountController mAccountController;
  private Map<String, Address> mAddressCache;
  private View mAttachmentIcon;
  private String[] mBcc;
  private View mBottomBorderView;
  private MessageHeaderViewCallbacks mCallbacks;
  private String[] mCc;
  private boolean mCollapsedDetailsValid;
  private ViewGroup mCollapsedDetailsView;
  private boolean mCollapsedStarVisible;
  private final DataSetObserver mContactInfoObserver = new DataSetObserver()
  {
    public void onChanged()
    {
      MessageHeaderView.this.updateContactInfo();
    }
  };
  private ContactInfoSource mContactInfoSource;
  private FormattedDateBuilder mDateBuilder;
  private AlertDialog mDetailsPopup;
  private View mDraftIcon;
  private View mEditDraftButton;
  private int mExpandMode = 0;
  private boolean mExpandable = true;
  private boolean mExpandedDetailsValid;
  private ViewGroup mExpandedDetailsView;
  private View mForwardButton;
  private String[] mFrom;
  private ViewGroup mImagePromptView;
  private final LayoutInflater mInflater;
  private MessageInviteView mInviteView;
  private boolean mIsDraft = false;
  private boolean mIsSending;
  private boolean mIsSnappy;
  private View mLeftSpacer;
  private MessageCursor.ConversationMessage mMessage;
  private ConversationViewAdapter.MessageHeaderItem mMessageHeaderItem;
  private boolean mObservingContactInfo;
  private View mOverflowButton;
  private View mPhotoSpacerView;
  private QuickContactBadge mPhotoView;
  private PopupMenu mPopup;
  private boolean mPreMeasuring;
  private ImageView mPresenceView;
  private AsyncQueryHandler mQueryHandler;
  private View mReplyAllButton;
  private View mReplyButton;
  private String[] mReplyTo;
  private View mRightSpacer;
  private Address mSender;
  private TextView mSenderEmailView;
  private TextView mSenderNameView;
  private boolean mShowImagePrompt;
  private String mSnippet;
  private SpamWarningView mSpamWarningView;
  private boolean mStarShown;
  private ImageView mStarView;
  private long mTimestampMs;
  private CharSequence mTimestampShort;
  private int mTitleContainerCollapsedMarginRight;
  private ViewGroup mTitleContainerView;
  private String[] mTo;
  private TextView mUpperDateView;
  private ViewGroup mUpperHeaderView;

  public MessageHeaderView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MessageHeaderView(Context paramContext, AttributeSet paramAttributeSet)
  {
    this(paramContext, paramAttributeSet, -1);
  }

  public MessageHeaderView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    this.mInflater = LayoutInflater.from(paramContext);
  }

  private boolean ensureExpandedDetailsView()
  {
    if (this.mExpandedDetailsView == null)
    {
      View localView = this.mInflater.inflate(2130968612, null, false);
      localView.setOnClickListener(this);
      this.mExpandedDetailsView = ((ViewGroup)localView);
    }
    for (boolean bool = true; ; bool = false)
    {
      if (!this.mExpandedDetailsValid)
      {
        if (this.mMessageHeaderItem.timestampLong == null)
          this.mMessageHeaderItem.timestampLong = this.mDateBuilder.formatLongDateTime(this.mTimestampMs);
        ((TextView)this.mExpandedDetailsView.findViewById(2131689582)).setText(this.mMessageHeaderItem.timestampLong);
        renderEmailList(2131689586, 2131689588, this.mReplyTo, false, this.mExpandedDetailsView);
        if (this.mMessage.viaDomain != null)
          renderEmailList(2131689583, 2131689585, this.mFrom, true, this.mExpandedDetailsView);
        renderEmailList(2131689589, 2131689591, this.mTo, false, this.mExpandedDetailsView);
        renderEmailList(2131689592, 2131689594, this.mCc, false, this.mExpandedDetailsView);
        renderEmailList(2131689595, 2131689597, this.mBcc, false, this.mExpandedDetailsView);
        this.mExpandedDetailsValid = true;
      }
      return bool;
    }
  }

  private Account getAccount()
  {
    return this.mAccountController.getAccount();
  }

  private Address getAddress(String paramString)
  {
    return getAddress(this.mAddressCache, paramString);
  }

  private static Address getAddress(Map<String, Address> paramMap, String paramString)
  {
    Address localAddress = null;
    if (paramMap != null)
      localAddress = (Address)paramMap.get(paramString);
    if (localAddress == null)
    {
      localAddress = Address.getEmailAddress(paramString);
      if (paramMap != null)
        paramMap.put(paramString, localAddress);
    }
    return localAddress;
  }

  private CharSequence getHeaderSubtitle()
  {
    if (this.mIsSending)
      return null;
    if (isExpanded())
    {
      if (this.mMessage.viaDomain != null)
      {
        Resources localResources = getResources();
        Object[] arrayOfObject = new Object[1];
        arrayOfObject[0] = this.mMessage.viaDomain;
        return localResources.getString(2131427575, arrayOfObject);
      }
      return getSenderAddress(this.mSender);
    }
    return this.mSnippet;
  }

  private CharSequence getHeaderTitle()
  {
    if (this.mIsDraft)
      return getResources().getQuantityText(2131755010, 1);
    if (this.mIsSending)
      return getResources().getString(2131427481);
    return getSenderName(this.mSender);
  }

  private AsyncQueryHandler getQueryHandler()
  {
    if (this.mQueryHandler == null)
      this.mQueryHandler = new AsyncQueryHandler(getContext().getContentResolver())
      {
      };
    return this.mQueryHandler;
  }

  static CharSequence getRecipientSummaryText(Context paramContext, String paramString, String[] paramArrayOfString1, String[] paramArrayOfString2, String[] paramArrayOfString3, Map<String, Address> paramMap)
  {
    RecipientListsBuilder localRecipientListsBuilder = new RecipientListsBuilder(paramContext, paramString, paramMap);
    localRecipientListsBuilder.append(paramArrayOfString1, 2131427519);
    localRecipientListsBuilder.append(paramArrayOfString2, 2131427520);
    localRecipientListsBuilder.append(paramArrayOfString3, 2131427521);
    return localRecipientListsBuilder.build();
  }

  private static CharSequence getSenderAddress(Address paramAddress)
  {
    if (paramAddress == null);
    for (String str = ""; TextUtils.isEmpty(str); str = paramAddress.getName())
      return null;
    return paramAddress.getAddress();
  }

  private static CharSequence getSenderName(Address paramAddress)
  {
    String str = paramAddress.getName();
    if (TextUtils.isEmpty(str))
      str = paramAddress.getAddress();
    return str;
  }

  private void handleShowImagePromptClick(View paramView)
  {
    Integer localInteger = (Integer)paramView.getTag();
    if (localInteger == null)
      return;
    switch (localInteger.intValue())
    {
    default:
      return;
    case 1:
      if (this.mCallbacks != null)
        this.mCallbacks.showExternalResources(this.mMessage);
      if (this.mMessageHeaderItem != null)
        this.mMessageHeaderItem.setShowImages(true);
      showImagePromptAlways(false);
      return;
    case 2:
    }
    this.mMessage.markAlwaysShowImages(getQueryHandler(), 0, null);
    this.mShowImagePrompt = false;
    paramView.setTag(null);
    paramView.setVisibility(8);
    updateSpacerHeight();
    Toast.makeText(getContext(), 2131427524, 0).show();
  }

  private void hideCollapsedDetails()
  {
    if (this.mCollapsedDetailsView != null)
      this.mCollapsedDetailsView.setVisibility(8);
  }

  private void hideDetailsPopup()
  {
    if (this.mDetailsPopup != null)
      this.mDetailsPopup.hide();
  }

  private void hideExpandedDetails()
  {
    if (this.mExpandedDetailsView != null)
      this.mExpandedDetailsView.setVisibility(8);
  }

  private void hideInvite()
  {
    if (this.mInviteView != null)
      this.mInviteView.setVisibility(8);
  }

  private void hideShowImagePrompt()
  {
    if (this.mImagePromptView != null)
      this.mImagePromptView.setVisibility(8);
  }

  private void hideSpamWarning()
  {
    if (this.mSpamWarningView != null)
      this.mSpamWarningView.setVisibility(8);
  }

  static String makeSnippet(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return null;
    StringBuilder localStringBuilder1 = new StringBuilder(100);
    StringReader localStringReader = new StringReader(paramString);
    while (true)
    {
      int j;
      try
      {
        i = localStringReader.read();
        if ((i != -1) && (localStringBuilder1.length() < 100))
        {
          if (Character.isWhitespace(i))
          {
            localStringBuilder1.append(' ');
            i = localStringReader.read();
            boolean bool = Character.isWhitespace(i);
            if (bool)
              continue;
            if (i != -1);
          }
        }
        else
          return localStringBuilder1.toString();
        if (i == 60)
        {
          k = localStringReader.read();
          if (k != -1)
            if (k != 62)
              continue;
        }
        else if (i == 38)
        {
          StringBuilder localStringBuilder2 = new StringBuilder();
          j = localStringReader.read();
          if ((j == -1) || (j == 59))
          {
            str = localStringBuilder2.toString();
            if ("nbsp".equals(str))
            {
              localStringBuilder1.append(' ');
              break label381;
            }
          }
          else
          {
            localStringBuilder2.append((char)j);
            continue;
          }
        }
      }
      catch (IOException localIOException)
      {
        int i;
        int k;
        String str;
        LogUtils.wtf(LOG_TAG, localIOException, "Really? IOException while reading a freaking string?!? ", new Object[0]);
        continue;
        if ("lt".equals(str))
        {
          localStringBuilder1.append('<');
        }
        else if ("gt".equals(str))
        {
          localStringBuilder1.append('>');
        }
        else if ("amp".equals(str))
        {
          localStringBuilder1.append('&');
        }
        else if ("quot".equals(str))
        {
          localStringBuilder1.append('"');
        }
        else if (("apos".equals(str)) || ("#39".equals(str)))
        {
          localStringBuilder1.append('\'');
        }
        else
        {
          localStringBuilder1.append('&').append(str);
          if (j == 59)
          {
            localStringBuilder1.append(';');
            break label381;
            localStringBuilder1.append((char)i);
            continue;
            if (k != -1)
              continue;
            continue;
          }
        }
      }
      label381: if (j != -1);
    }
  }

  private int measureHeight()
  {
    ViewGroup localViewGroup = (ViewGroup)getParent();
    if (localViewGroup == null)
    {
      LogUtils.e(LOG_TAG, new Error(), "Unable to measure height of detached header", new Object[0]);
      return getHeight();
    }
    this.mPreMeasuring = true;
    int i = Utils.measureViewHeight(this, localViewGroup);
    this.mPreMeasuring = false;
    return i;
  }

  private void registerMessageClickTargets(int[] paramArrayOfInt)
  {
    int i = paramArrayOfInt.length;
    for (int j = 0; j < i; j++)
    {
      View localView = findViewById(paramArrayOfInt[j]);
      if (localView != null)
        localView.setOnClickListener(this);
    }
  }

  private void render(boolean paramBoolean)
  {
    if (this.mMessageHeaderItem == null)
      return;
    Timer localTimer = new Timer();
    localTimer.start("message header render");
    this.mCollapsedDetailsValid = false;
    this.mExpandedDetailsValid = false;
    this.mMessage = this.mMessageHeaderItem.getMessage();
    this.mShowImagePrompt = this.mMessage.shouldShowImagePrompt();
    setExpanded(this.mMessageHeaderItem.isExpanded());
    this.mTimestampMs = this.mMessage.dateReceivedMs;
    this.mTimestampShort = this.mMessageHeaderItem.timestampShort;
    if (this.mTimestampShort == null)
    {
      this.mTimestampShort = this.mDateBuilder.formatShortDate(this.mTimestampMs);
      this.mMessageHeaderItem.timestampShort = this.mTimestampShort;
    }
    this.mFrom = this.mMessage.getFromAddresses();
    this.mTo = this.mMessage.getToAddresses();
    this.mCc = this.mMessage.getCcAddresses();
    this.mBcc = this.mMessage.getBccAddresses();
    this.mReplyTo = this.mMessage.getReplyToAddresses();
    boolean bool;
    int i;
    if (this.mMessage.draftType != 0)
    {
      bool = true;
      this.mIsDraft = bool;
      this.mIsSending = this.mMessage.isSending;
      String str = this.mMessage.getFrom();
      if (TextUtils.isEmpty(str))
        str = getAccount().name;
      this.mSender = getAddress(str);
      this.mStarView.setSelected(this.mMessage.starred);
      ImageView localImageView = this.mStarView;
      Resources localResources = getResources();
      if (!this.mStarView.isSelected())
        break label437;
      i = 2131427391;
      label281: localImageView.setContentDescription(localResources.getString(i));
      this.mStarShown = true;
      Iterator localIterator = this.mMessage.getConversation().getRawFolders().iterator();
      while (localIterator.hasNext())
        if (((Folder)localIterator.next()).isTrash())
          this.mStarShown = false;
      updateChildVisibility();
      if ((!this.mIsDraft) && (!this.mIsSending))
        break label445;
      this.mSnippet = makeSnippet(this.mMessage.snippet);
      label376: this.mSenderNameView.setText(getHeaderTitle());
      this.mSenderEmailView.setText(getHeaderSubtitle());
      if (this.mUpperDateView != null)
        this.mUpperDateView.setText(this.mTimestampShort);
      if (!paramBoolean)
        break label459;
      unbind();
    }
    while (true)
    {
      localTimer.pause("message header render");
      return;
      bool = false;
      break;
      label437: i = 2131427390;
      break label281;
      label445: this.mSnippet = this.mMessage.snippet;
      break label376;
      label459: updateContactInfo();
      if (!this.mObservingContactInfo)
      {
        this.mContactInfoSource.registerObserver(this.mContactInfoObserver);
        this.mObservingContactInfo = true;
      }
    }
  }

  private void renderEmailList(int paramInt1, int paramInt2, String[] paramArrayOfString, boolean paramBoolean, View paramView)
  {
    if ((paramArrayOfString == null) || (paramArrayOfString.length == 0))
      return;
    String[] arrayOfString = new String[paramArrayOfString.length];
    int i = 0;
    if (i < paramArrayOfString.length)
    {
      Address localAddress = getAddress(paramArrayOfString[i]);
      String str1 = localAddress.getName();
      String str2 = localAddress.getAddress();
      if ((str1 == null) || (str1.length() == 0))
        arrayOfString[i] = str2;
      while (true)
      {
        i++;
        break;
        if (paramBoolean)
        {
          Resources localResources = getResources();
          Object[] arrayOfObject = new Object[3];
          arrayOfObject[0] = str1;
          arrayOfObject[1] = str2;
          arrayOfObject[2] = this.mMessage.viaDomain;
          arrayOfString[i] = localResources.getString(2131427526, arrayOfObject);
        }
        else
        {
          arrayOfString[i] = getResources().getString(2131427525, new Object[] { str1, str2 });
        }
      }
    }
    ((TextView)paramView.findViewById(paramInt2)).setText(TextUtils.join("\n", arrayOfString));
    paramView.findViewById(paramInt1).setVisibility(0);
  }

  private static void setChildMarginRight(View paramView, int paramInt)
  {
    ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams)paramView.getLayoutParams();
    localMarginLayoutParams.rightMargin = paramInt;
    paramView.setLayoutParams(localMarginLayoutParams);
  }

  private void setChildVisibility(int paramInt, View[] paramArrayOfView)
  {
    int i = paramArrayOfView.length;
    for (int j = 0; j < i; j++)
    {
      View localView = paramArrayOfView[j];
      if (localView != null)
        localView.setVisibility(paramInt);
    }
  }

  private void setExpanded(boolean paramBoolean)
  {
    setActivated(paramBoolean);
    if (this.mMessageHeaderItem != null)
      this.mMessageHeaderItem.setExpanded(paramBoolean);
  }

  private void setMessageDetailsExpanded(boolean paramBoolean)
  {
    if (this.mExpandMode == 0)
      if (paramBoolean)
      {
        showExpandedDetails();
        hideCollapsedDetails();
      }
    while (true)
    {
      if (this.mMessageHeaderItem != null)
        this.mMessageHeaderItem.detailsExpanded = paramBoolean;
      return;
      hideExpandedDetails();
      showCollapsedDetails();
      continue;
      if (this.mExpandMode == 1)
        if (paramBoolean)
        {
          showDetailsPopup();
        }
        else
        {
          hideDetailsPopup();
          showCollapsedDetails();
        }
    }
  }

  private void setReplyOrReplyAllVisible()
  {
    int i = 8;
    if (this.mIsDraft)
    {
      View[] arrayOfView4 = new View[2];
      arrayOfView4[0] = this.mReplyButton;
      arrayOfView4[1] = this.mReplyAllButton;
      setChildVisibility(i, arrayOfView4);
      return;
    }
    if (this.mOverflowButton == null)
    {
      View[] arrayOfView3 = new View[2];
      arrayOfView3[0] = this.mReplyButton;
      arrayOfView3[1] = this.mReplyAllButton;
      setChildVisibility(0, arrayOfView3);
      return;
    }
    int j;
    if (getAccount().settings.replyBehavior == 1)
    {
      j = 1;
      if (j == 0)
        break label153;
    }
    label153: for (int k = i; ; k = 0)
    {
      View[] arrayOfView1 = new View[1];
      arrayOfView1[0] = this.mReplyButton;
      setChildVisibility(k, arrayOfView1);
      if (j != 0)
        i = 0;
      View[] arrayOfView2 = new View[1];
      arrayOfView2[0] = this.mReplyAllButton;
      setChildVisibility(i, arrayOfView2);
      return;
      j = 0;
      break;
    }
  }

  private void showCollapsedDetails()
  {
    if (this.mCollapsedDetailsView == null)
    {
      this.mCollapsedDetailsView = ((ViewGroup)this.mInflater.inflate(2130968611, this, false));
      addView(this.mCollapsedDetailsView, 1 + indexOfChild(this.mUpperHeaderView));
      this.mCollapsedDetailsView.setOnClickListener(this);
    }
    if (!this.mCollapsedDetailsValid)
    {
      if (this.mMessageHeaderItem.recipientSummaryText == null)
        this.mMessageHeaderItem.recipientSummaryText = getRecipientSummaryText(getContext(), getAccount().name, this.mTo, this.mCc, this.mBcc, this.mAddressCache);
      ((TextView)findViewById(2131689578)).setText(this.mMessageHeaderItem.recipientSummaryText);
      ((TextView)findViewById(2131689579)).setText(this.mTimestampShort);
      this.mCollapsedDetailsValid = true;
    }
    this.mCollapsedDetailsView.setVisibility(0);
  }

  private void showDetailsPopup()
  {
    ensureExpandedDetailsView();
    if (this.mDetailsPopup == null)
    {
      AlertDialog.Builder localBuilder = new AlertDialog.Builder(getContext());
      this.mExpandedDetailsView.findViewById(2131689598).setVisibility(8);
      localBuilder.setView(this.mExpandedDetailsView).setCancelable(true).setTitle(getContext().getString(2131427596));
      this.mDetailsPopup = localBuilder.show();
      return;
    }
    this.mDetailsPopup.show();
  }

  private void showExpandedDetails()
  {
    if (ensureExpandedDetailsView())
      addView(this.mExpandedDetailsView, 1 + indexOfChild(this.mUpperHeaderView));
    this.mExpandedDetailsView.setVisibility(0);
  }

  private void showImagePromptAlways(boolean paramBoolean)
  {
    if (paramBoolean)
      showImagePromptOnce();
    ((ImageView)this.mImagePromptView.findViewById(2131689609)).setContentDescription(getResources().getString(2131427523));
    ((TextView)this.mImagePromptView.findViewById(2131689610)).setText(2131427523);
    this.mImagePromptView.setTag(Integer.valueOf(2));
    if (!paramBoolean)
      updateSpacerHeight();
  }

  private void showImagePromptOnce()
  {
    if (this.mImagePromptView == null)
    {
      ViewGroup localViewGroup = (ViewGroup)this.mInflater.inflate(2130968616, this, false);
      addView(localViewGroup);
      localViewGroup.setOnClickListener(this);
      localViewGroup.setTag(Integer.valueOf(1));
      this.mImagePromptView = localViewGroup;
    }
    this.mImagePromptView.setVisibility(0);
  }

  private void showInvite()
  {
    if (this.mInviteView == null)
    {
      this.mInviteView = ((MessageInviteView)this.mInflater.inflate(2130968615, this, false));
      addView(this.mInviteView);
    }
    this.mInviteView.bind(this.mMessage);
    this.mInviteView.setVisibility(0);
  }

  private void showSpamWarning()
  {
    if (this.mSpamWarningView == null)
    {
      this.mSpamWarningView = ((SpamWarningView)this.mInflater.inflate(2130968617, this, false));
      addView(this.mSpamWarningView);
    }
    this.mSpamWarningView.showSpamWarning(this.mMessage, this.mSender);
  }

  private void toggleMessageDetails(View paramView)
  {
    int i = measureHeight();
    if (paramView == this.mCollapsedDetailsView);
    for (boolean bool = true; ; bool = false)
    {
      setMessageDetailsExpanded(bool);
      updateSpacerHeight();
      if (this.mCallbacks != null)
        this.mCallbacks.setMessageDetailsExpanded(this.mMessageHeaderItem, bool, i);
      return;
    }
  }

  private void updateChildVisibility()
  {
    int i = 8;
    if (isExpanded())
    {
      int m;
      int i1;
      int n;
      if (this.mIsSnappy)
      {
        m = i;
        setMessageDetailsVisibility(m);
        if (!this.mIsDraft)
          break label202;
        i1 = 8;
        n = 0;
      }
      while (true)
      {
        setReplyOrReplyAllVisible();
        View[] arrayOfView10 = new View[5];
        arrayOfView10[0] = this.mPhotoView;
        arrayOfView10[1] = this.mPhotoSpacerView;
        arrayOfView10[2] = this.mForwardButton;
        arrayOfView10[3] = this.mSenderEmailView;
        arrayOfView10[4] = this.mOverflowButton;
        setChildVisibility(i1, arrayOfView10);
        View[] arrayOfView11 = new View[2];
        arrayOfView11[0] = this.mDraftIcon;
        arrayOfView11[1] = this.mEditDraftButton;
        setChildVisibility(n, arrayOfView11);
        View[] arrayOfView12 = new View[2];
        arrayOfView12[0] = this.mAttachmentIcon;
        arrayOfView12[1] = this.mUpperDateView;
        setChildVisibility(i, arrayOfView12);
        if (this.mStarShown)
          i = 0;
        View[] arrayOfView13 = new View[1];
        arrayOfView13[0] = this.mStarView;
        setChildVisibility(i, arrayOfView13);
        setChildMarginRight(this.mTitleContainerView, 0);
        return;
        m = 0;
        break;
        label202: n = 8;
        i1 = 0;
      }
    }
    setMessageDetailsVisibility(i);
    View[] arrayOfView1 = new View[2];
    arrayOfView1[0] = this.mSenderEmailView;
    arrayOfView1[1] = this.mUpperDateView;
    setChildVisibility(0, arrayOfView1);
    View[] arrayOfView2 = new View[4];
    arrayOfView2[0] = this.mEditDraftButton;
    arrayOfView2[1] = this.mReplyButton;
    arrayOfView2[2] = this.mReplyAllButton;
    arrayOfView2[3] = this.mForwardButton;
    setChildVisibility(i, arrayOfView2);
    View[] arrayOfView3 = new View[1];
    arrayOfView3[0] = this.mOverflowButton;
    setChildVisibility(i, arrayOfView3);
    int j;
    if (this.mMessage.hasAttachments)
    {
      j = 0;
      View[] arrayOfView4 = new View[1];
      arrayOfView4[0] = this.mAttachmentIcon;
      setChildVisibility(j, arrayOfView4);
      if ((!this.mCollapsedStarVisible) || (!this.mStarShown))
        break label451;
    }
    label451: for (int k = 0; ; k = i)
    {
      View[] arrayOfView5 = new View[1];
      arrayOfView5[0] = this.mStarView;
      setChildVisibility(k, arrayOfView5);
      setChildMarginRight(this.mTitleContainerView, this.mTitleContainerCollapsedMarginRight);
      if (!this.mIsDraft)
        break label457;
      View[] arrayOfView8 = new View[1];
      arrayOfView8[0] = this.mDraftIcon;
      setChildVisibility(0, arrayOfView8);
      View[] arrayOfView9 = new View[2];
      arrayOfView9[0] = this.mPhotoView;
      arrayOfView9[1] = this.mPhotoSpacerView;
      setChildVisibility(i, arrayOfView9);
      return;
      j = i;
      break;
    }
    label457: View[] arrayOfView6 = new View[1];
    arrayOfView6[0] = this.mDraftIcon;
    setChildVisibility(i, arrayOfView6);
    View[] arrayOfView7 = new View[2];
    arrayOfView7[0] = this.mPhotoView;
    arrayOfView7[1] = this.mPhotoSpacerView;
    setChildVisibility(0, arrayOfView7);
  }

  private void updateContactInfo()
  {
    this.mPresenceView.setImageDrawable(null);
    this.mPresenceView.setVisibility(8);
    if ((this.mContactInfoSource == null) || (this.mSender == null))
    {
      this.mPhotoView.setImageToDefault();
      this.mPhotoView.setContentDescription(getResources().getString(2131427513));
    }
    label269: label283: 
    while (true)
    {
      return;
      Resources localResources = getResources();
      Object[] arrayOfObject1 = new Object[1];
      String str1;
      String str3;
      int i;
      if (!TextUtils.isEmpty(this.mSender.getName()))
      {
        str1 = this.mSender.getName();
        arrayOfObject1[0] = str1;
        String str2 = localResources.getString(2131427512, arrayOfObject1);
        this.mPhotoView.setContentDescription(str2);
        str3 = this.mSender.getAddress();
        ContactInfo localContactInfo = this.mContactInfoSource.getContactInfo(str3);
        if (localContactInfo == null)
          break label269;
        this.mPhotoView.assignContactUri(localContactInfo.contactUri);
        Bitmap localBitmap = localContactInfo.photo;
        i = 0;
        if (localBitmap != null)
        {
          this.mPhotoView.setImageBitmap(localContactInfo.photo);
          Object[] arrayOfObject2 = new Object[1];
          arrayOfObject2[0] = this.mSender.getName();
          String.format(str2, arrayOfObject2);
          i = 1;
        }
        if ((!this.mIsDraft) && (localContactInfo.status != null))
        {
          this.mPresenceView.setImageResource(ContactsContract.StatusUpdates.getPresenceIconResourceId(localContactInfo.status.intValue()));
          this.mPresenceView.setVisibility(0);
        }
      }
      while (true)
      {
        if (i != 0)
          break label283;
        this.mPhotoView.setImageToDefault();
        return;
        str1 = this.mSender.getAddress();
        break;
        this.mPhotoView.assignContactFromEmail(str3, true);
        i = 0;
      }
    }
  }

  private void updateSpacerHeight()
  {
    int i = measureHeight();
    this.mMessageHeaderItem.setHeight(i);
    if (this.mCallbacks != null)
      this.mCallbacks.setMessageSpacerHeight(this.mMessageHeaderItem, i);
  }

  public void bind(ConversationViewAdapter.MessageHeaderItem paramMessageHeaderItem, boolean paramBoolean)
  {
    if ((this.mMessageHeaderItem != null) && (this.mMessageHeaderItem == paramMessageHeaderItem))
      return;
    this.mMessageHeaderItem = paramMessageHeaderItem;
    render(paramBoolean);
  }

  public void hideMessageDetails()
  {
    setMessageDetailsVisibility(8);
  }

  public void initialize(FormattedDateBuilder paramFormattedDateBuilder, ConversationAccountController paramConversationAccountController, Map<String, Address> paramMap)
  {
    this.mDateBuilder = paramFormattedDateBuilder;
    this.mAccountController = paramConversationAccountController;
    this.mAddressCache = paramMap;
  }

  public boolean isBoundTo(ConversationOverlayItem paramConversationOverlayItem)
  {
    return paramConversationOverlayItem == this.mMessageHeaderItem;
  }

  public boolean isExpanded()
  {
    return (this.mMessageHeaderItem == null) || (this.mMessageHeaderItem.isExpanded());
  }

  public void onClick(View paramView)
  {
    onClick(paramView, paramView.getId());
  }

  public boolean onClick(View paramView, int paramInt)
  {
    int i = 1;
    if (this.mMessage == null)
    {
      LogUtils.i(LOG_TAG, "ignoring message header tap on unbound view", new Object[0]);
      return false;
    }
    boolean bool = true;
    switch (paramInt)
    {
    default:
      String str = LOG_TAG;
      Object[] arrayOfObject = new Object[i];
      arrayOfObject[0] = Integer.valueOf(paramInt);
      LogUtils.i(str, "unrecognized header tap: %d", arrayOfObject);
      bool = false;
    case 2131689622:
    case 2131689623:
    case 2131689625:
    case 2131689558:
    case 2131689624:
    case 2131689572:
    case 2131689577:
    case 2131689580:
    case 2131689601:
    case 2131689608:
    }
    while (true)
    {
      return bool;
      ComposeActivity.reply(getContext(), getAccount(), this.mMessage);
      continue;
      ComposeActivity.replyAll(getContext(), getAccount(), this.mMessage);
      continue;
      ComposeActivity.forward(getContext(), getAccount(), this.mMessage);
      continue;
      if (!paramView.isSelected());
      while (true)
      {
        paramView.setSelected(i);
        this.mMessage.star(i);
        break;
        i = 0;
      }
      ComposeActivity.editDraft(getContext(), getAccount(), this.mMessage);
      continue;
      if (this.mPopup == null)
      {
        this.mPopup = new PopupMenu(getContext(), paramView);
        this.mPopup.getMenuInflater().inflate(2131820553, this.mPopup.getMenu());
        this.mPopup.setOnMenuItemClickListener(this);
      }
      label324: MenuItem localMenuItem;
      if (getAccount().settings.replyBehavior == i)
      {
        int k = i;
        this.mPopup.getMenu().findItem(2131689622).setVisible(k);
        localMenuItem = this.mPopup.getMenu().findItem(2131689623);
        if (k != 0)
          break label394;
      }
      while (true)
      {
        localMenuItem.setVisible(i);
        this.mPopup.show();
        break;
        int m = 0;
        break label324;
        label394: int j = 0;
      }
      toggleMessageDetails(paramView);
      continue;
      toggleExpanded();
      continue;
      handleShowImagePromptClick(paramView);
    }
  }

  public void onDetachedFromParent()
  {
    unbind();
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mUpperHeaderView = ((ViewGroup)findViewById(2131689601));
    this.mSenderNameView = ((TextView)findViewById(2131689629));
    this.mSenderEmailView = ((TextView)findViewById(2131689631));
    this.mPhotoView = ((QuickContactBadge)findViewById(2131689618));
    this.mPhotoSpacerView = findViewById(2131689619);
    this.mReplyButton = findViewById(2131689622);
    this.mReplyAllButton = findViewById(2131689623);
    this.mForwardButton = findViewById(2131689625);
    this.mStarView = ((ImageView)findViewById(2131689558));
    this.mPresenceView = ((ImageView)findViewById(2131689630));
    this.mTitleContainerView = ((ViewGroup)findViewById(2131689626));
    this.mOverflowButton = findViewById(2131689572);
    this.mDraftIcon = findViewById(2131689620);
    this.mEditDraftButton = findViewById(2131689624);
    this.mUpperDateView = ((TextView)findViewById(2131689627));
    this.mAttachmentIcon = findViewById(2131689628);
    if (this.mStarView.getVisibility() == 0);
    for (boolean bool = true; ; bool = false)
    {
      this.mCollapsedStarVisible = bool;
      this.mTitleContainerCollapsedMarginRight = ((ViewGroup.MarginLayoutParams)this.mTitleContainerView.getLayoutParams()).rightMargin;
      this.mBottomBorderView = findViewById(2131689602);
      this.mLeftSpacer = findViewById(2131689617);
      this.mRightSpacer = findViewById(2131689621);
      setExpanded(true);
      registerMessageClickTargets(new int[] { 2131689622, 2131689623, 2131689625, 2131689558, 2131689624, 2131689572, 2131689601 });
      return;
    }
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    Timer localTimer = new Timer();
    localTimer.start("message header layout");
    super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
    localTimer.pause("message header layout");
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    Timer localTimer = new Timer();
    super.onMeasure(paramInt1, paramInt2);
    if (!this.mPreMeasuring)
      localTimer.pause("message header measure");
  }

  public boolean onMenuItemClick(MenuItem paramMenuItem)
  {
    this.mPopup.dismiss();
    return onClick(null, paramMenuItem.getItemId());
  }

  public void refresh()
  {
    render(false);
  }

  public void setCallbacks(MessageHeaderViewCallbacks paramMessageHeaderViewCallbacks)
  {
    this.mCallbacks = paramMessageHeaderViewCallbacks;
  }

  public void setContactInfoSource(ContactInfoSource paramContactInfoSource)
  {
    this.mContactInfoSource = paramContactInfoSource;
  }

  public void setExpandMode(int paramInt)
  {
    this.mExpandMode = paramInt;
  }

  public void setExpandable(boolean paramBoolean)
  {
    this.mExpandable = paramBoolean;
  }

  public void setMessageDetailsVisibility(int paramInt)
  {
    if (paramInt == 8)
    {
      hideCollapsedDetails();
      hideExpandedDetails();
      hideSpamWarning();
      hideShowImagePrompt();
      hideInvite();
    }
    while (true)
    {
      if (this.mBottomBorderView != null)
        this.mBottomBorderView.setVisibility(paramInt);
      return;
      setMessageDetailsExpanded(this.mMessageHeaderItem.detailsExpanded);
      if (this.mMessage.spamWarningString == null)
      {
        hideSpamWarning();
        label67: if (!this.mShowImagePrompt)
          break label120;
        if (!this.mMessageHeaderItem.getShowImages())
          break label113;
        showImagePromptAlways(true);
      }
      while (true)
      {
        if (!this.mMessage.isFlaggedCalendarInvite())
          break label127;
        showInvite();
        break;
        showSpamWarning();
        break label67;
        label113: showImagePromptOnce();
        continue;
        label120: hideShowImagePrompt();
      }
      label127: hideInvite();
    }
  }

  public void setSnappy(boolean paramBoolean)
  {
    this.mIsSnappy = paramBoolean;
    hideMessageDetails();
    if (paramBoolean)
    {
      setBackgroundDrawable(null);
      this.mLeftSpacer.setVisibility(0);
      this.mRightSpacer.setVisibility(0);
      return;
    }
    setBackgroundColor(17170443);
    this.mLeftSpacer.setVisibility(8);
    this.mRightSpacer.setVisibility(8);
  }

  public void toggleExpanded()
  {
    if (!this.mExpandable)
      return;
    if (!isExpanded());
    for (boolean bool = true; ; bool = false)
    {
      setExpanded(bool);
      if (!this.mIsSnappy)
      {
        this.mSenderNameView.setText(getHeaderTitle());
        this.mSenderEmailView.setText(getHeaderSubtitle());
      }
      updateChildVisibility();
      int i = measureHeight();
      this.mMessageHeaderItem.setHeight(i);
      if (this.mCallbacks == null)
        break;
      this.mCallbacks.setMessageExpanded(this.mMessageHeaderItem, i);
      return;
    }
  }

  public void unbind()
  {
    this.mMessageHeaderItem = null;
    this.mMessage = null;
    if (this.mObservingContactInfo)
    {
      this.mContactInfoSource.unregisterObserver(this.mContactInfoObserver);
      this.mObservingContactInfo = false;
    }
  }

  public static abstract interface MessageHeaderViewCallbacks
  {
    public abstract void setMessageDetailsExpanded(ConversationViewAdapter.MessageHeaderItem paramMessageHeaderItem, boolean paramBoolean, int paramInt);

    public abstract void setMessageExpanded(ConversationViewAdapter.MessageHeaderItem paramMessageHeaderItem, int paramInt);

    public abstract void setMessageSpacerHeight(ConversationViewAdapter.MessageHeaderItem paramMessageHeaderItem, int paramInt);

    public abstract void showExternalResources(Message paramMessage);
  }

  private static class RecipientListsBuilder
  {
    private final Map<String, Address> mAddressCache;
    private final SpannableStringBuilder mBuilder = new SpannableStringBuilder();
    private final CharSequence mComma;
    private final Context mContext;
    boolean mFirst = true;
    private final String mMe;
    int mRecipientCount = 0;

    public RecipientListsBuilder(Context paramContext, String paramString, Map<String, Address> paramMap)
    {
      this.mContext = paramContext;
      this.mMe = paramString;
      this.mComma = this.mContext.getText(2131427538);
      this.mAddressCache = paramMap;
    }

    private CharSequence getSummaryTextForHeading(int paramInt1, String[] paramArrayOfString, int paramInt2)
    {
      if ((paramArrayOfString == null) || (paramArrayOfString.length == 0) || (paramInt2 == 0))
      {
        localObject = null;
        return localObject;
      }
      Object localObject = new SpannableStringBuilder(this.mContext.getString(paramInt1));
      ((SpannableStringBuilder)localObject).setSpan(new StyleSpan(1), 0, ((SpannableStringBuilder)localObject).length(), 33);
      ((SpannableStringBuilder)localObject).append(' ');
      int i = Math.min(paramInt2, paramArrayOfString.length);
      int j = 1;
      int k = 0;
      label79: Address localAddress;
      String str;
      if (k < i)
      {
        localAddress = MessageHeaderView.getAddress(this.mAddressCache, paramArrayOfString[k]);
        if (!this.mMe.equals(localAddress.getAddress()))
          break label147;
        str = this.mContext.getString(2131427483);
        label125: if (j == 0)
          break label157;
        j = 0;
      }
      while (true)
      {
        ((SpannableStringBuilder)localObject).append(str);
        k++;
        break label79;
        break;
        label147: str = localAddress.getSimplifiedName();
        break label125;
        label157: ((SpannableStringBuilder)localObject).append(this.mComma);
      }
    }

    public void append(String[] paramArrayOfString, int paramInt)
    {
      int i = 50 - this.mRecipientCount;
      CharSequence localCharSequence = getSummaryTextForHeading(paramInt, paramArrayOfString, i);
      if (localCharSequence != null)
      {
        if (!this.mFirst)
          break label60;
        this.mFirst = false;
      }
      while (true)
      {
        this.mBuilder.append(localCharSequence);
        this.mRecipientCount += Math.min(i, paramArrayOfString.length);
        return;
        label60: this.mBuilder.append("   ");
      }
    }

    public CharSequence build()
    {
      return this.mBuilder;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.MessageHeaderView
 * JD-Core Version:    0.6.2
 */