package com.android.mail.browse;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import android.R;
import android.app.FragmentManager;
import android.app.LoaderManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.mail.ContactInfoSource;
import com.android.mail.FormattedDateBuilder;
import com.android.mail.providers.Address;
import com.android.mail.providers.Conversation;
import com.android.mail.ui.ControllableActivity;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

public class ConversationViewAdapter extends BaseAdapter {
	private final ConversationAccountController mAccountController;
	private Map<String, Address> mAddressCache;
	private final ContactInfoSource mContactInfoSource;
	private Context mContext;
	private ConversationViewHeader.ConversationViewHeaderCallbacks mConversationCallbacks;
	private final FormattedDateBuilder mDateBuilder;
	private final FragmentManager mFragmentManager;
	private final LayoutInflater mInflater;
	private final List<ConversationOverlayItem> mItems;
	private final LoaderManager mLoaderManager;
	private final MessageHeaderView.MessageHeaderViewCallbacks mMessageCallbacks;
	private SuperCollapsedBlock.OnClickListener mSuperCollapsedListener;

	public ConversationViewAdapter(
			ControllableActivity paramControllableActivity,
			ConversationAccountController paramConversationAccountController,
			LoaderManager paramLoaderManager,
			MessageHeaderView.MessageHeaderViewCallbacks paramMessageHeaderViewCallbacks,
			ContactInfoSource paramContactInfoSource,
			ConversationViewHeader.ConversationViewHeaderCallbacks paramConversationViewHeaderCallbacks,
			SuperCollapsedBlock.OnClickListener paramOnClickListener,
			Map<String, Address> paramMap,
			FormattedDateBuilder paramFormattedDateBuilder) {
		this.mContext = paramControllableActivity.getActivityContext();
		this.mDateBuilder = paramFormattedDateBuilder;
		this.mAccountController = paramConversationAccountController;
		this.mLoaderManager = paramLoaderManager;
		this.mFragmentManager = paramControllableActivity.getFragmentManager();
		this.mMessageCallbacks = paramMessageHeaderViewCallbacks;
		this.mContactInfoSource = paramContactInfoSource;
		this.mConversationCallbacks = paramConversationViewHeaderCallbacks;
		this.mSuperCollapsedListener = paramOnClickListener;
		this.mAddressCache = paramMap;
		this.mInflater = LayoutInflater.from(this.mContext);
		this.mItems = Lists.newArrayList();
	}

	public int addConversationHeader(Conversation paramConversation) {
		return addItem(new ConversationHeaderItem(paramConversation));
	}

	public int addItem(ConversationOverlayItem paramConversationOverlayItem) {
		int i = this.mItems.size();
		this.mItems.add(paramConversationOverlayItem);
		return i;
	}

	public int addMessageFooter(MessageHeaderItem paramMessageHeaderItem) {
		return addItem(new MessageFooterItem(paramMessageHeaderItem));
	}

	public int addMessageHeader(
			MessageCursor.ConversationMessage paramConversationMessage,
			boolean paramBoolean1, boolean paramBoolean2) {
		return addItem(new MessageHeaderItem(paramConversationMessage,
				paramBoolean1, paramBoolean2));
	}

	public int addSuperCollapsedBlock(int paramInt1, int paramInt2) {
		return addItem(new SuperCollapsedBlockItem(paramInt1, paramInt2));
	}

	public void clear() {
		this.mItems.clear();
		notifyDataSetChanged();
	}

	public int getCount() {
		return this.mItems.size();
	}

	public ConversationOverlayItem getItem(int paramInt) {
		return (ConversationOverlayItem) this.mItems.get(paramInt);
	}

	public long getItemId(int paramInt) {
		return paramInt;
	}

	public int getItemViewType(int paramInt) {
		return ((ConversationOverlayItem) this.mItems.get(paramInt)).getType();
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		return getView(getItem(paramInt), paramView, paramViewGroup, false);
	}

	public View getView(ConversationOverlayItem paramConversationOverlayItem,
			View paramView, ViewGroup paramViewGroup, boolean paramBoolean) {
		View localView = paramConversationOverlayItem.createView(
				this.mContext, this.mInflater, paramViewGroup);
		if (paramView == null){
			localView = paramConversationOverlayItem.createView(this.mContext, this.mInflater, paramViewGroup);
		}else{
			localView = paramView;
		}
		paramConversationOverlayItem.bindView(localView, paramBoolean);
		return localView;
	}

	public int getViewTypeCount() {
		return 4;
	}

	public MessageFooterItem newMessageFooterItem(
			MessageHeaderItem paramMessageHeaderItem) {
		return new MessageFooterItem(paramMessageHeaderItem);
	}

	public MessageHeaderItem newMessageHeaderItem(
			MessageCursor.ConversationMessage paramConversationMessage,
			boolean paramBoolean1, boolean paramBoolean2) {
		return new MessageHeaderItem(paramConversationMessage, paramBoolean1,
				paramBoolean2);
	}

	public void replaceSuperCollapsedBlock(
			SuperCollapsedBlockItem paramSuperCollapsedBlockItem,
			Collection<ConversationOverlayItem> paramCollection) {
		int i = this.mItems.indexOf(paramSuperCollapsedBlockItem);
		if (i == -1)
			return;
		this.mItems.remove(i);
		this.mItems.addAll(i, paramCollection);
	}

	public void updateItemsForMessage(
			MessageCursor.ConversationMessage paramConversationMessage,
			List<Integer> paramList) {
		int i = 0;
		int j = this.mItems.size();
		while (i < j) {
			ConversationOverlayItem localConversationOverlayItem = (ConversationOverlayItem) this.mItems
					.get(i);
			if (localConversationOverlayItem
					.belongsToMessage(paramConversationMessage)) {
				localConversationOverlayItem
						.setMessage(paramConversationMessage);
				paramList.add(Integer.valueOf(i));
			}
			i++;
		}
	}

	public class ConversationHeaderItem extends ConversationOverlayItem {
		public final Conversation mConversation;

		private ConversationHeaderItem(Conversation arg2) {
			this.mConversation = arg2;
		}

		public void bindView(View paramView, boolean paramBoolean) {
			((ConversationViewHeader) paramView).bind(this);
		}

		public View createView(Context paramContext,
				LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
			ConversationViewHeader localConversationViewHeader = (ConversationViewHeader) paramLayoutInflater
					.inflate(R.id.conversation_view_header, paramViewGroup, false);
			localConversationViewHeader.setCallbacks(
					ConversationViewAdapter.this.mConversationCallbacks,
					ConversationViewAdapter.this.mAccountController);
			localConversationViewHeader.bind(this);
			localConversationViewHeader.setSubject(this.mConversation.subject);
			if (ConversationViewAdapter.this.mAccountController.getAccount()
					.supportsCapability(8192))
				localConversationViewHeader.setFolders(this.mConversation);
			return localConversationViewHeader;
		}

		public int getType() {
			return 0;
		}

		public boolean isContiguous() {
			return true;
		}
	}

	public class MessageFooterItem extends ConversationOverlayItem {
		private final ConversationViewAdapter.MessageHeaderItem headerItem;

		private MessageFooterItem(ConversationViewAdapter.MessageHeaderItem arg2) {
			this.headerItem = arg2;
		}

		public void bindView(View paramView, boolean paramBoolean) {
			((MessageFooterView) paramView).bind(this.headerItem, paramBoolean);
		}

		public View createView(Context paramContext,
				LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
			MessageFooterView localMessageFooterView = (MessageFooterView) paramLayoutInflater
					.inflate(2130968613, paramViewGroup, false);
			localMessageFooterView.initialize(
					ConversationViewAdapter.this.mLoaderManager,
					ConversationViewAdapter.this.mFragmentManager);
			return localMessageFooterView;
		}

		public int getGravity() {
			return 48;
		}

		public int getHeight() {
			if (!this.headerItem.isExpanded())
				return 0;
			return super.getHeight();
		}

		public int getType() {
			return 2;
		}

		public boolean isContiguous() {
			return true;
		}
	}

	public class MessageHeaderItem extends ConversationOverlayItem {
		public boolean detailsExpanded;
		private boolean mExpanded;
		private MessageCursor.ConversationMessage mMessage;
		private boolean mShowImages;
		public CharSequence recipientSummaryText;
		public CharSequence timestampLong;
		public CharSequence timestampShort;

		MessageHeaderItem(MessageCursor.ConversationMessage paramBoolean1,
				boolean paramBoolean2, boolean arg4) {
			this.mMessage = paramBoolean1;
			this.mExpanded = paramBoolean2;
			this.mShowImages = arg4;
			this.detailsExpanded = false;
		}

		public boolean belongsToMessage(
				MessageCursor.ConversationMessage paramConversationMessage) {
			return Objects.equal(this.mMessage, paramConversationMessage);
		}

		public void bindView(View paramView, boolean paramBoolean) {
			((MessageHeaderView) paramView).bind(this, paramBoolean);
		}

		public boolean canBecomeSnapHeader() {
			return isExpanded();
		}

		public boolean canPushSnapHeader() {
			return true;
		}

		public View createView(Context paramContext,
				LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
			MessageHeaderView localMessageHeaderView = (MessageHeaderView) paramLayoutInflater
					.inflate(2130968614, paramViewGroup, false);
			localMessageHeaderView.initialize(
					ConversationViewAdapter.this.mDateBuilder,
					ConversationViewAdapter.this.mAccountController,
					ConversationViewAdapter.this.mAddressCache);
			localMessageHeaderView
					.setCallbacks(ConversationViewAdapter.this.mMessageCallbacks);
			localMessageHeaderView
					.setContactInfoSource(ConversationViewAdapter.this.mContactInfoSource);
			return localMessageHeaderView;
		}

		public MessageCursor.ConversationMessage getMessage() {
			return this.mMessage;
		}

		public boolean getShowImages() {
			return this.mShowImages;
		}

		public int getType() {
			return 1;
		}

		public boolean isContiguous() {
			return !isExpanded();
		}

		public boolean isExpanded() {
			return this.mExpanded;
		}

		public void onModelUpdated(View paramView) {
			((MessageHeaderView) paramView).refresh();
		}

		public void setExpanded(boolean paramBoolean) {
			if (this.mExpanded != paramBoolean)
				this.mExpanded = paramBoolean;
		}

		public void setMessage(
				MessageCursor.ConversationMessage paramConversationMessage) {
			this.mMessage = paramConversationMessage;
		}

		public void setShowImages(boolean paramBoolean) {
			this.mShowImages = paramBoolean;
		}
	}

	public class SuperCollapsedBlockItem extends ConversationOverlayItem {
		private int mEnd;
		private final int mStart;

		private SuperCollapsedBlockItem(int paramInt1, int arg3) {
			this.mStart = paramInt1;
			this.mEnd = arg3;
		}

		public void bindView(View paramView, boolean paramBoolean) {
			((SuperCollapsedBlock) paramView).bind(this);
		}

		public boolean canPushSnapHeader() {
			return true;
		}

		public View createView(Context paramContext,
				LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup) {
			SuperCollapsedBlock localSuperCollapsedBlock = (SuperCollapsedBlock) paramLayoutInflater
					.inflate(2130968672, paramViewGroup, false);
			localSuperCollapsedBlock
					.initialize(ConversationViewAdapter.this.mSuperCollapsedListener);
			return localSuperCollapsedBlock;
		}

		public int getEnd() {
			return this.mEnd;
		}

		public int getStart() {
			return this.mStart;
		}

		public int getType() {
			return 3;
		}

		public boolean isContiguous() {
			return true;
		}
	}
}

/*
 * Location:
 * C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name: com.android.mail.browse.ConversationViewAdapter JD-Core
 * Version: 0.6.2
 */