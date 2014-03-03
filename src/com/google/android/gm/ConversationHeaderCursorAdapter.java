package com.google.android.gm;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.BecomeActiveNetworkCursor;
import com.google.android.gm.provider.Gmail.ConversationCursor;
import com.google.android.gm.provider.Gmail.CursorError;
import com.google.android.gm.provider.Gmail.CursorStatus;

public class ConversationHeaderCursorAdapter extends BaseAdapter {
	private static final int VIEW_TYPE_FOOTER = 1;
	private static final int VIEW_TYPE_NORMAL;
	private String mAccount;
	private Context mContext;
	private Gmail.ConversationCursor mConversationCursor;
	private CharSequence mCurrentlyDisplayedLabel;
	private Gmail.CursorError mError;
	private LayoutInflater mFactory;
	private Gmail mGmail;
	private InnerAdapter mInnerAdapter;
	IProgressMonitor mProgressMonitor;
	private String mQuery;
	private AsyncQueryHandler mQueryHandler;
	private boolean mRespondToUpdates = false;
	private Gmail.CursorStatus mStatus;

	public ConversationHeaderCursorAdapter(Context paramContext,
			IProgressMonitor paramIProgressMonitor,
			CharSequence paramCharSequence, Gmail paramGmail, String paramString) {
		this.mContext = paramContext;
		this.mFactory = LayoutInflater.from(paramContext);
		this.mProgressMonitor = paramIProgressMonitor;
		this.mStatus = Gmail.CursorStatus.LOADED;
		this.mError = Gmail.CursorError.NO_ERROR;
		this.mCurrentlyDisplayedLabel = paramCharSequence;
		this.mGmail = paramGmail;
		this.mAccount = paramString;
		this.mInnerAdapter = new InnerAdapter(paramContext, null);
	}

	private void cursorStatusChanged(Gmail.CursorStatus paramCursorStatus, Gmail.CursorError paramCursorError)
  {
    if (paramCursorStatus != this.mStatus)
    {
      Log.d("Gmail", "ConversationHeaderCursorAdapter.cursorStatusChanged: " + paramCursorStatus);
      this.mStatus = paramCursorStatus;
      this.mError = paramCursorError;
    }
    switch (1.$SwitchMap$com$google$android$gm$provider$Gmail$CursorStatus[this.mStatus.ordinal()])
    {
    default:
      return;
    case 1:
      this.mProgressMonitor.done();
      return;
    case 2:
      this.mProgressMonitor.beginTask(null, 0);
      return;
    case 3:
    }
    this.mProgressMonitor.done();
  }

	private void startQuery(AsyncQueryHandler paramAsyncQueryHandler,
			String paramString) {
		this.mGmail.runQueryForConversations(this.mAccount,
				paramAsyncQueryHandler, 0, paramString,
				Gmail.BecomeActiveNetworkCursor.YES);
	}

	public final void changeCursor(
			Gmail.ConversationCursor paramConversationCursor) {
		this.mConversationCursor = paramConversationCursor;
		if (this.mConversationCursor != null) {
			this.mInnerAdapter.changeCursor(this.mConversationCursor
					.getCursor());
			cursorStatusChanged(this.mConversationCursor.getStatus(),
					this.mConversationCursor.getError());
		}
		while (true) {
			super.notifyDataSetChanged();
			return;
			this.mInnerAdapter.changeCursor(null);
			cursorStatusChanged(Gmail.CursorStatus.LOADED,
					Gmail.CursorError.NO_ERROR);
		}
	}

	public int getCount() {
		if (!this.mInnerAdapter.isValid())
			return 0;
		int i;
		int j;
		if ((this.mStatus == Gmail.CursorStatus.ERROR)
				|| (this.mStatus == Gmail.CursorStatus.LOADING)) {
			i = 1;
			j = this.mInnerAdapter.getCount();
			if (i == 0)
				break label57;
		}
		label57: for (int k = 1;; k = 0) {
			return j + k;
			i = 0;
			break;
		}
	}

	public final Gmail.ConversationCursor getCursor() {
		return this.mConversationCursor;
	}

	public Object getItem(int paramInt) {
		if (paramInt < this.mConversationCursor.count())
			return this.mInnerAdapter.getItem(paramInt);
		return null;
	}

	public long getItemId(int paramInt) {
		if (paramInt < this.mConversationCursor.count())
			return this.mInnerAdapter.getItemId(paramInt);
		return -1L;
	}

	public int getItemViewType(int paramInt) {
		if (paramInt == this.mInnerAdapter.getCount())
			return 1;
		return 0;
	}

	public View getView(int paramInt, View paramView, ViewGroup paramViewGroup) {
		if (paramInt == this.mInnerAdapter.getCount())
			;
		for (int i = 1; i != 0; i = 0) {
			ConversationListFooterView localConversationListFooterView = (ConversationListFooterView) paramView;
			if (localConversationListFooterView == null)
				localConversationListFooterView = (ConversationListFooterView) ((LayoutInflater) this.mContext
						.getSystemService("layout_inflater")).inflate(
						2130903051, null);
			localConversationListFooterView.bind(this.mStatus, this.mError,
					this.mConversationCursor);
			return localConversationListFooterView;
		}
		return this.mInnerAdapter.getView(paramInt, paramView, paramViewGroup);
	}

	public int getViewTypeCount() {
		return 2;
	}

	public boolean hasStableIds() {
		return true;
	}

	public void notifyDataSetChanged() {
		this.mInnerAdapter.notifyDataSetChanged();
	}

	public void registerDataSetObserver(DataSetObserver paramDataSetObserver) {
		if (this.mInnerAdapter != null)
			this.mInnerAdapter.registerDataSetObserver(paramDataSetObserver);
	}

	public void restartQuery() {
		if ((this.mQueryHandler != null) && (this.mQuery != null))
			startQuery(this.mQueryHandler, this.mQuery);
	}

	public void startQueryAndUpdateOnChange(
			AsyncQueryHandler paramAsyncQueryHandler, String paramString) {
		if ((paramAsyncQueryHandler == null) || (paramString == null))
			return;
		this.mQueryHandler = paramAsyncQueryHandler;
		this.mQuery = paramString;
		startQuery(this.mQueryHandler, this.mQuery);
		this.mRespondToUpdates = true;
	}

	public void stopRespondingToUpdates() {
		this.mRespondToUpdates = false;
	}

	public void unregisterDataSetObserver(DataSetObserver paramDataSetObserver) {
		if (this.mInnerAdapter != null)
			this.mInnerAdapter.unregisterDataSetObserver(paramDataSetObserver);
	}

	private class InnerAdapter extends CursorAdapter {
		public InnerAdapter(Context paramCursor, Cursor arg3) {
			super(localCursor, false);
		}

		public final void bindView(View paramView, Context paramContext,
				Cursor paramCursor) {
			boolean bool = ((ConversationListActivity) paramContext)
					.allowBatch();
			((CanvasConversationHeaderView) paramView)
					.bind(ConversationHeaderCursorAdapter.this.mConversationCursor,
							ConversationHeaderCursorAdapter.this.mGmail,
							ConversationHeaderCursorAdapter.this.mAccount,
							ConversationHeaderCursorAdapter.this.mCurrentlyDisplayedLabel,
							bool);
		}

		public boolean isValid() {
			return getCursor() != null;
		}

		public final View newView(Context paramContext, Cursor paramCursor,
				ViewGroup paramViewGroup) {
			CanvasConversationHeaderView localCanvasConversationHeaderView = new CanvasConversationHeaderView(
					paramContext);
			localCanvasConversationHeaderView
					.setActivity((ConversationListActivity) paramContext);
			return localCanvasConversationHeaderView;
		}

		public void notifyDataSetChanged() {
			if (ConversationHeaderCursorAdapter.this.mConversationCursor != null)
				ConversationHeaderCursorAdapter.this
						.cursorStatusChanged(
								ConversationHeaderCursorAdapter.this.mConversationCursor
										.getStatus(),
								ConversationHeaderCursorAdapter.this.mConversationCursor
										.getError());
			super.notifyDataSetChanged();
		}

		public void onContentChanged() {
			super.onContentChanged();
			if (ConversationHeaderCursorAdapter.this.mRespondToUpdates)
				ConversationHeaderCursorAdapter.this.restartQuery();
		}
	}
}

/*
 * Location: C:\Users\科\Desktop\classes_dex2jar.jar Qualified Name:
 * com.google.android.gm.ConversationHeaderCursorAdapter JD-Core Version: 0.6.2
 */