package com.android.mail.browse;

import android.app.FragmentManager;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.mail.providers.Attachment;
import com.android.mail.providers.Message;
import com.android.mail.ui.AttachmentTile;
import com.android.mail.ui.AttachmentTileGrid;
import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MessageFooterView extends LinearLayout
  implements ConversationContainer.DetachListener, LoaderManager.LoaderCallbacks<Cursor>
{
  private static final String LOG_TAG = LogTag.getLogTag();
  private LinearLayout mAttachmentBarList;
  private AttachmentTileGrid mAttachmentGrid;
  private AttachmentLoader.AttachmentCursor mAttachmentsCursor;
  private FragmentManager mFragmentManager;
  private final LayoutInflater mInflater;
  private LoaderManager mLoaderManager;
  private ConversationViewAdapter.MessageHeaderItem mMessageHeaderItem;
  private View mTitleBar;
  private TextView mTitleText;

  public MessageFooterView(Context paramContext)
  {
    this(paramContext, null);
  }

  public MessageFooterView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mInflater = LayoutInflater.from(paramContext);
  }

  private Integer getAttachmentLoaderId()
  {
    if (this.mMessageHeaderItem == null);
    for (MessageCursor.ConversationMessage localConversationMessage = null; ; localConversationMessage = this.mMessageHeaderItem.getMessage())
    {
      Integer localInteger = null;
      if (localConversationMessage != null)
      {
        boolean bool = localConversationMessage.hasAttachments;
        localInteger = null;
        if (bool)
        {
          Uri localUri = localConversationMessage.attachmentListUri;
          localInteger = null;
          if (localUri != null)
            localInteger = Integer.valueOf(localConversationMessage.attachmentListUri.hashCode());
        }
      }
      return localInteger;
    }
  }

  private void renderAttachments(List<Attachment> paramList, boolean paramBoolean)
  {
    if ((paramList == null) || (paramList.isEmpty()))
      return;
    int i = paramList.size();
    ArrayList localArrayList1 = new ArrayList(i);
    ArrayList localArrayList2 = new ArrayList(i);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Attachment localAttachment = (Attachment)localIterator.next();
      if (AttachmentTile.isTiledAttachment(localAttachment))
        localArrayList1.add(localAttachment);
      else
        localArrayList2.add(localAttachment);
    }
    this.mMessageHeaderItem.getMessage().attachmentsJson = Attachment.toJSONArray(paramList);
    this.mTitleText.setVisibility(0);
    this.mTitleBar.setVisibility(0);
    renderTiledAttachments(localArrayList1, paramBoolean);
    renderBarAttachments(localArrayList2, paramBoolean);
  }

  private void renderAttachments(boolean paramBoolean)
  {
    if ((this.mAttachmentsCursor != null) && (!this.mAttachmentsCursor.isClosed()))
    {
      int i = -1;
      localObject = Lists.newArrayList();
      while (true)
      {
        AttachmentLoader.AttachmentCursor localAttachmentCursor = this.mAttachmentsCursor;
        i++;
        if (!localAttachmentCursor.moveToPosition(i))
          break;
        ((List)localObject).add(this.mAttachmentsCursor.get());
      }
    }
    Object localObject = this.mMessageHeaderItem.getMessage().getAttachments();
    renderAttachments((List)localObject, paramBoolean);
  }

  private void renderBarAttachments(List<Attachment> paramList, boolean paramBoolean)
  {
    this.mAttachmentBarList.setVisibility(0);
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Attachment localAttachment = (Attachment)localIterator.next();
      Uri localUri = localAttachment.getIdentifierUri();
      MessageAttachmentBar localMessageAttachmentBar = (MessageAttachmentBar)this.mAttachmentBarList.findViewWithTag(localUri);
      if (localMessageAttachmentBar == null)
      {
        localMessageAttachmentBar = MessageAttachmentBar.inflate(this.mInflater, this);
        localMessageAttachmentBar.setTag(localUri);
        localMessageAttachmentBar.initialize(this.mFragmentManager);
        this.mAttachmentBarList.addView(localMessageAttachmentBar);
      }
      localMessageAttachmentBar.render(localAttachment, paramBoolean);
    }
  }

  private void renderTiledAttachments(List<Attachment> paramList, boolean paramBoolean)
  {
    this.mAttachmentGrid.setVisibility(0);
    this.mAttachmentGrid.configureGrid(this.mFragmentManager, this.mMessageHeaderItem.getMessage().attachmentListUri, paramList, paramBoolean);
  }

  public void bind(ConversationViewAdapter.MessageHeaderItem paramMessageHeaderItem, boolean paramBoolean)
  {
    if ((this.mMessageHeaderItem != null) && (this.mMessageHeaderItem.getMessage() != null) && (this.mMessageHeaderItem.getMessage().attachmentListUri != null) && (!this.mMessageHeaderItem.getMessage().attachmentListUri.equals(paramMessageHeaderItem.getMessage().attachmentListUri)))
    {
      this.mAttachmentGrid.removeAllViewsInLayout();
      this.mAttachmentBarList.removeAllViewsInLayout();
      this.mTitleText.setVisibility(8);
      this.mTitleBar.setVisibility(8);
      this.mAttachmentGrid.setVisibility(8);
      this.mAttachmentBarList.setVisibility(8);
    }
    Integer localInteger1 = getAttachmentLoaderId();
    this.mMessageHeaderItem = paramMessageHeaderItem;
    Integer localInteger2 = getAttachmentLoaderId();
    if ((localInteger1 != null) && (!Objects.equal(localInteger1, localInteger2)))
      this.mLoaderManager.destroyLoader(localInteger1.intValue());
    if ((!paramBoolean) && (localInteger2 != null))
    {
      LogUtils.i(LOG_TAG, "binding footer view, calling initLoader for message %d", new Object[] { localInteger2 });
      this.mLoaderManager.initLoader(localInteger2.intValue(), Bundle.EMPTY, this);
    }
    if ((this.mAttachmentGrid.getChildCount() == 0) && (this.mAttachmentBarList.getChildCount() == 0))
      renderAttachments(false);
    boolean bool = this.mMessageHeaderItem.isExpanded();
    int i = 0;
    if (bool);
    while (true)
    {
      setVisibility(i);
      return;
      i = 8;
    }
  }

  public void initialize(LoaderManager paramLoaderManager, FragmentManager paramFragmentManager)
  {
    this.mLoaderManager = paramLoaderManager;
    this.mFragmentManager = paramFragmentManager;
  }

  public Loader<Cursor> onCreateLoader(int paramInt, Bundle paramBundle)
  {
    return new AttachmentLoader(getContext(), this.mMessageHeaderItem.getMessage().attachmentListUri);
  }

  public void onDetachedFromParent()
  {
  }

  protected void onFinishInflate()
  {
    super.onFinishInflate();
    this.mTitleText = ((TextView)findViewById(2131689599));
    this.mTitleBar = findViewById(2131689600);
    this.mAttachmentGrid = ((AttachmentTileGrid)findViewById(2131689544));
    this.mAttachmentBarList = ((LinearLayout)findViewById(2131689545));
  }

  public void onLoadFinished(Loader<Cursor> paramLoader, Cursor paramCursor)
  {
    this.mAttachmentsCursor = ((AttachmentLoader.AttachmentCursor)paramCursor);
    if ((this.mAttachmentsCursor == null) || (this.mAttachmentsCursor.isClosed()))
      return;
    renderAttachments(true);
  }

  public void onLoaderReset(Loader<Cursor> paramLoader)
  {
    this.mAttachmentsCursor = null;
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.browse.MessageFooterView
 * JD-Core Version:    0.6.2
 */