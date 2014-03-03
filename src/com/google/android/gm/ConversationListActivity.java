package com.google.android.gm;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.Dialog;
import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.provider.SearchRecentSuggestions;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gm.comm.NetworkProgressMonitor;
import com.google.android.gm.comm.longshadow.LongShadowUtils;
import com.google.android.gm.provider.Gmail;
import com.google.android.gm.provider.Gmail.BecomeActiveNetworkCursor;
import com.google.android.gm.provider.Gmail.ConversationCursor;
import com.google.android.gm.provider.Gmail.CursorError;
import com.google.android.gm.provider.Gmail.CursorStatus;
import com.google.android.gm.provider.Gmail.LabelMap;
import com.google.android.gsf.GoogleLoginServiceConstants;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.SortedSet;
import java.util.concurrent.ConcurrentHashMap;

public class ConversationListActivity extends GmailBaseListActivity implements
		View.OnClickListener {
	private static final int APPLY_LABELS_BUTTON_ID = 3;
	private static final int ARCHIVE_BUTTON_ID = 1;
	private static final String CONVERSATION_ID_KEY = MenuHandler.class
			.getName() + "_CONVERSATION_ID_KEY";
	public static final String CONVERSATION_REMOVED = "conversation-removed";
	private static final int DELETE_BUTTON_ID = 2;
	public static final String EXTRA_LABEL = "label";
	public static final String EXTRA_SHOW_WHATS_NEW = "show-whats-new";
	public static final String EXTRA_TITLE = "title";
	private static final int HIDE_UNDO_ANIMATION_DELAY = 200;
	private static final boolean LDEBUG = false;
	private static final String LIST_STATE_KEY = "liststate";
	static final String ON_CREATE_PERF_TAG = "CLA.onCreate";
	static final String ON_RESUME_PERF_TAG = "CLA.onResume";
	public static final int RESULT_FROM_CONVERSATION = 3;
	public static final int RESULT_MAILBOX_SELECTED = 4;
	static final String SETCONTENTVIEW_PERF_TAG = "CLA.setContentView";
	private static final int SHOW_UNDO_ANIMATION_DELAY = 300;
	private static final String TAG = "Gmail";
	private static final long TIMESTAMP_UPDATE_INTERVAL = 60000L;
	private static final int UNDO_ANIMATION_DURATION = 300;
	private static UndoOperation sUndoOperation;
	private String mAccount;
	private Activity mActivity;
	private boolean mActivityPaused;
	private boolean mAllowBatch;
	private Button mApplyLabelsButton;
	private Button mArchiveButton;
	private final Map<Long, ConversationInfo> mBatchConversations = new ConcurrentHashMap();
	private View mBetweenChromeView;
	private ConversationHeaderCursorAdapter mCursorAdapter;
	private View mCustomTitleBarView;
	private Button mDeleteButton;
	private String mDisplayedLabel;
	private View mEmptyView;
	private boolean mFooterInitialized = false;
	private LinearLayout mFooterOrganizeView;
	private Gmail mGmail;
	private final Handler mHandler = new Handler();
	private Animation mHideListUndoAnimation;
	private Animation mHideUndoAnimation;
	private String mIntentLabel;
	private Gmail.LabelMap mLabelMap;
	private final Observer mLabelsObserver = new Observer() {
		public void update(Observable paramAnonymousObservable,
				Object paramAnonymousObject) {
			ConversationListActivity.this.mHandler.post(new Runnable() {
				public void run() {
					ConversationListActivity.this.initBestGuessTitleBar();
				}
			});
		}
	};
	int mLastOpenedConversationIndex = -1;
	private Parcelable mListState = null;
	private ConversationInfo mLongPressedConversation;
	private MenuHandler mMenuHandler;
	private BroadcastReceiver mNewEmailReceiver = null;
	private String mNonVoicemailDisplayedLabel;
	private Persistence mPrefs;
	private boolean mPreserveUndo;
	private String mQuery;
	private QueryHandler mQueryHandler;
	private Animation mShowUndoAnimation;
	private TextView mUndoActionView;
	private LinearLayout mUndoButtonView;
	private TextView mUndoDescriptionView;
	private LinearLayout mUndoView;
	private final Runnable mUpdateTimestampsRunnable = new Runnable() {
		public void run() {
			ConversationListActivity.this.getListView().invalidateViews();
			ConversationListActivity.this.mHandler.postDelayed(
					ConversationListActivity.this.mUpdateTimestampsRunnable,
					60000L);
		}
	};
	private boolean mUserCancelledAuth = false;
	private String mUserSpecifiedInboxLabel;

	public ConversationListActivity() {
	}

	ConversationListActivity(Gmail paramGmail, Persistence paramPersistence) {
		this.mGmail = paramGmail;
		this.mPrefs = paramPersistence;
	}

	private void asyncGetAccountsInfo() {
		AccountManager.get(this).getAccountsByTypeAndFeatures("com.google",
				new String[] { "service_mail" }, new AccountManagerCallback() {
					public void run(
							AccountManagerFuture<Account[]> paramAnonymousAccountManagerFuture) {
						Account[] arrayOfAccount = null;
						try {
							arrayOfAccount = (Account[]) paramAnonymousAccountManagerFuture
									.getResult();
							int i = arrayOfAccount.length;
							if (i > 1)
								;
							while (true) {
								ConversationListActivity.this
										.onResumeAfterAccountsInfo(arrayOfAccount);
								return;
							}
						} catch (OperationCanceledException localOperationCanceledException) {
							while (true)
								Log.w("Gmail",
										"Unexpected exception trying to get accounts.",
										localOperationCanceledException);
						} catch (IOException localIOException) {
							while (true)
								Log.w("Gmail",
										"Unexpected exception trying to get accounts.",
										localIOException);
						} catch (AuthenticatorException localAuthenticatorException) {
							while (true)
								Log.w("Gmail",
										"Unexpected exception trying to get accounts.",
										localAuthenticatorException);
						}
					}
				}, null);
	}

	private void chooseAccount() {
		Intent localIntent = getIntent();
		this.mAccount = validateAccountName(localIntent
				.getStringExtra("account"));
		if (this.mAccount == null)
			this.mAccount = validateAccountName(localIntent
					.getStringExtra("account-shortcut"));
		if (this.mAccount == null)
			this.mAccount = WaitActivity.waitIfNeededAndGetAccount(this);
		if (this.mAccount != null)
			Persistence.getInstance(this).setActiveAccount(this, this.mAccount);
	}

	private Intent createConversationIntent(
			Gmail.ConversationCursor paramConversationCursor,
			boolean paramBoolean) {
		Intent localIntent = new Intent(this, HtmlConversationActivity.class);
		localIntent.putExtra("thread-id",
				paramConversationCursor.getConversationId());
		localIntent.putExtra("maxMessageId",
				paramConversationCursor.getMaxServerMessageId());
		localIntent.putExtra("account", this.mAccount);
		localIntent.putExtra("query",
				makeQueryString(this.mQuery, this.mIntentLabel));
		localIntent.putExtra("searchQuery", this.mQuery);
		localIntent.putExtra("position", paramConversationCursor.position());
		localIntent.putExtra("displayed-label",
				this.mNonVoicemailDisplayedLabel);
		localIntent.putExtra("is-read",
				TextUtils.join(" ", paramConversationCursor.getLabels()));
		boolean bool1;
		if (!paramBoolean) {
			int i = this.mCursorAdapter.getCount();
			this.mLastOpenedConversationIndex = paramConversationCursor
					.position();
			if (1 + this.mLastOpenedConversationIndex >= i)
				break label189;
			bool1 = true;
			localIntent.putExtra("older-exists", bool1);
			if (this.mLastOpenedConversationIndex <= 0)
				break label195;
		}
		label189: label195: for (boolean bool2 = true;; bool2 = false) {
			localIntent.putExtra("newer-exists", bool2);
			return localIntent;
			bool1 = false;
			break;
		}
	}

	private void deselectAll() {
		this.mBatchConversations.clear();
		this.mCursorAdapter.notifyDataSetChanged();
		hideFooter();
	}

	private void deselectConversation(long paramLong) {
		if (this.mBatchConversations.containsKey(Long.valueOf(paramLong)))
			toggleConversation(paramLong, 0L, null);
	}

	private Collection<ConversationInfo> getSelectedConversations() {
		int i = getSelectedItemPosition();
		if (this.mLongPressedConversation != null) {
			ConversationInfo[] arrayOfConversationInfo = new ConversationInfo[1];
			arrayOfConversationInfo[0] = this.mLongPressedConversation;
			return Arrays.asList(arrayOfConversationInfo);
		}
		if (this.mBatchConversations.size() > 0)
			return this.mBatchConversations.values();
		if (i != -1) {
			Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
					.getCursor();
			if (localConversationCursor.moveTo(i))
				return Arrays
						.asList(new ConversationInfo[] { new ConversationInfo(
								localConversationCursor.getConversationId(),
								localConversationCursor.getMaxServerMessageId(),
								getSelectedLabels()) });
			return Collections.emptyList();
		}
		return Collections.emptyList();
	}

	private void hideFooter() {
		if (this.mFooterOrganizeView.getVisibility() != 8) {
			this.mFooterOrganizeView.setVisibility(8);
			this.mFooterOrganizeView.startAnimation(AnimationUtils
					.loadAnimation(this, 2130968578));
		}
	}

	private void initBestGuessTitleBar() {
		Intent localIntent = getIntent();
		Object localObject;
		if ("android.intent.action.SEARCH".equals(localIntent.getAction()))
			localObject = localIntent.getStringExtra("query");
		try {
			int i;
			if ((Utils.isStringEmpty((CharSequence) localObject))
					&& (!Utils.isStringEmpty(this.mDisplayedLabel))) {
				if ((Gmail.LabelMap.getNoCountLabels()
						.contains(this.mDisplayedLabel))
						|| (Gmail.LabelMap.getTotalCountLabels()
								.contains(this.mDisplayedLabel)))
					break label221;
				i = this.mLabelMap
						.getNumUnreadConversations(this.mDisplayedLabel
								.toString());
				if (i < 0)
					i = 0;
			}
			label221: for (String str = " (" + i + ")";; str = "") {
				localObject = LongShadowUtils.getHumanLabelName(this,
						this.mLabelMap, this.mDisplayedLabel.toString()) + str;
				Utils.setTitleWithAccount(this, (CharSequence) localObject,
						this.mAccount);
				initCustomTitleBarButtons(this.mAccount);
				this.mCustomTitleBarView
						.setLayoutParams(new LinearLayout.LayoutParams(-1,
								getResources()
										.getDimensionPixelSize(2131230746)));
				this.mCustomTitleBarView.setVisibility(0);
				return;
				localObject = localIntent.getCharSequenceExtra("title");
				break;
			}
		} catch (IllegalArgumentException localIllegalArgumentException) {
			Utils.handleAccountNotSynchronized(this);
		}
	}

	private void initCustomTitleBarButtons(String paramString) {
		Button localButton1 = (Button) findViewById(2131361845);
		localButton1.setText(paramString);
		localButton1.setBackgroundResource(2130837531);
		localButton1.setOnClickListener(this);
		Button localButton2 = (Button) findViewById(2131361843);
		if (!"android.intent.action.SEARCH".equals(getIntent().getAction())) {
			localButton2.setBackgroundResource(2130837531);
			localButton2.setOnClickListener(this);
		}
	}

	private void initUndoAnimations() {
		int i = getResources().getDimensionPixelSize(2131230747);
		DecelerateInterpolator localDecelerateInterpolator = new DecelerateInterpolator();
		this.mShowUndoAnimation = new TranslateAnimation(0, 0.0F, 0, 0.0F, 0,
				-i, 0, 0.0F);
		this.mShowUndoAnimation.setDuration(300L);
		this.mShowUndoAnimation.setInterpolator(localDecelerateInterpolator);
		this.mShowUndoAnimation.setStartOffset(300L);
		this.mHideUndoAnimation = new TranslateAnimation(0, 0.0F, 0, 0.0F, 0,
				0.0F, 0, -i);
		this.mHideUndoAnimation.setDuration(300L);
		this.mHideUndoAnimation.setInterpolator(localDecelerateInterpolator);
		this.mHideUndoAnimation.setStartOffset(200L);
		this.mHideListUndoAnimation = new TranslateAnimation(0, 0.0F, 0, 0.0F,
				0, i, 0, 0.0F);
		this.mHideListUndoAnimation.setDuration(300L);
		this.mHideListUndoAnimation
				.setInterpolator(localDecelerateInterpolator);
		this.mHideListUndoAnimation.setStartOffset(200L);
	}

	private void initializeFooterIfNeeded() {
		int i;
		Button localButton2;
		if (!this.mFooterInitialized) {
			this.mArchiveButton = new Button(this);
			this.mArchiveButton.setText(MenuHandler.getYButtonText(this,
					this.mLabelMap, this.mDisplayedLabel));
			this.mArchiveButton.setOnClickListener(this);
			this.mArchiveButton.setId(1);
			Button localButton1 = this.mArchiveButton;
			if (!Gmail.isLabelUserSettable(this.mDisplayedLabel))
				break label249;
			i = 0;
			localButton1.setVisibility(i);
			this.mFooterOrganizeView.addView(this.mArchiveButton,
					new LinearLayout.LayoutParams(0, -1, 1.0F));
			this.mDeleteButton = new Button(this);
			this.mDeleteButton.setText(2131296293);
			localButton2 = this.mDeleteButton;
			if (!"^k".equals(this.mDisplayedLabel))
				break label255;
		}
		label249: label255: for (int j = 8;; j = 0) {
			localButton2.setVisibility(j);
			this.mDeleteButton.setOnClickListener(this);
			this.mDeleteButton.setId(2);
			this.mFooterOrganizeView.addView(this.mDeleteButton,
					new LinearLayout.LayoutParams(0, -1, 1.0F));
			this.mApplyLabelsButton = new Button(this);
			this.mApplyLabelsButton.setText(2131296360);
			this.mApplyLabelsButton.setOnClickListener(this);
			this.mApplyLabelsButton.setId(3);
			this.mFooterOrganizeView.addView(this.mApplyLabelsButton,
					new LinearLayout.LayoutParams(0, -1, 1.0F));
			this.mFooterInitialized = true;
			return;
			i = 8;
			break;
		}
	}

	private void initializeUndoViews() {
		this.mUndoView = ((LinearLayout) findViewById(2131361831));
		this.mUndoButtonView = ((LinearLayout) findViewById(2131361832));
		this.mUndoDescriptionView = ((TextView) findViewById(2131361833));
		this.mUndoActionView = ((TextView) findViewById(2131361834));
	}

	private String makeQueryString(String paramString1, String paramString2) {
		ArrayList localArrayList = Lists.newArrayList();
		if (!TextUtils.isEmpty(paramString2))
			localArrayList.add("label:" + paramString2);
		if (!TextUtils.isEmpty(paramString1))
			localArrayList.add(paramString1);
		return TextUtils.join(" ", localArrayList);
	}

	private void maybeHideAccountsMenuItem(final Menu paramMenu) {
		AccountManager localAccountManager = AccountManager.get(this);
		String[] arrayOfString = new String[1];
		arrayOfString[0] = GoogleLoginServiceConstants
				.featureForService("mail");
		localAccountManager.getAccountsByTypeAndFeatures("com.google",
				arrayOfString, new AccountManagerCallback() {
					public void run(
							AccountManagerFuture<Account[]> paramAnonymousAccountManagerFuture) {
						while (true) {
							try {
								Account[] arrayOfAccount = (Account[]) paramAnonymousAccountManagerFuture
										.getResult();
								MenuItem localMenuItem = paramMenu
										.findItem(2131361934);
								if (arrayOfAccount.length > 1) {
									bool = true;
									localMenuItem.setVisible(bool);
									return;
								}
							} catch (OperationCanceledException localOperationCanceledException) {
								return;
							} catch (IOException localIOException) {
								return;
							} catch (AuthenticatorException localAuthenticatorException) {
								return;
							}
							boolean bool = false;
						}
					}
				}, null);
	}

	private void onResumeAfterAccountsInfo(Account[] paramArrayOfAccount) {
		if ((paramArrayOfAccount == null)
				|| ((!Utils.isStringEmpty(this.mAccount)) && (!Utils
						.containsAccount(new Account(this.mAccount,
								"com.google"), paramArrayOfAccount)))) {
			finish();
			startActivity(new Intent(this, getClass()));
			return;
		}
		Persistence.getInstance(this).setActiveAccount(this, this.mAccount);
	}

	private void openConversation(int paramInt) {
		Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
				.getCursor();
		if ((!localConversationCursor.getCursor().isClosed())
				&& (localConversationCursor.moveTo(paramInt)))
			openConversationOrDraft(localConversationCursor);
	}

	private void openConversation(
			Gmail.ConversationCursor paramConversationCursor,
			boolean paramBoolean) {
		startActivityForResult(
				createConversationIntent(paramConversationCursor, paramBoolean),
				3);
	}

	private void openConversationOrDraft(
			Gmail.ConversationCursor paramConversationCursor) {
		int i = paramConversationCursor.getNumMessages();
		if (shouldGoStraightToDraft(paramConversationCursor.getLabelIds(), i)) {
			long l = LongShadowUtils.getComposableMessageId(LongShadowUtils
					.getContentProviderMailAccess(getContentResolver()),
					this.mAccount, paramConversationCursor.getConversationId());
			if (l != -1L) {
				ComposeActivity.draft(this, this.mAccount, l);
				return;
			}
			openConversation(paramConversationCursor, false);
			return;
		}
		openConversation(paramConversationCursor, false);
	}

	public static void setUndoOperation(Context paramContext,
			UndoOperation paramUndoOperation, String paramString) {
		sUndoOperation = paramUndoOperation;
	}

	private boolean shouldGoStraightToDraft(Set<Long> paramSet, int paramInt) {
		if ("^r".equals(this.mDisplayedLabel))
			return true;
		return ((paramSet.contains(Long.valueOf(this.mLabelMap
				.getLabelIdDraft()))) || (paramSet.contains(Long
				.valueOf(this.mLabelMap.getLabelIdOutbox()))))
				&& (paramInt == 1);
	}

	private void showFooter() {
		initializeFooterIfNeeded();
		this.mFooterOrganizeView.setVisibility(0);
		this.mFooterOrganizeView.startAnimation(AnimationUtils.loadAnimation(
				this, 2130968577));
	}

	private void switchAccounts() {
		Intent localIntent = new Intent(this, MailboxSelectionActivity.class);
		localIntent.addFlags(131072);
		if (Persistence.getInstance(this).getFastSwitching(this)) {
			Account[] arrayOfAccount = AccountManager.get(this)
					.getAccountsByType("com.google");
			String str1 = Persistence.getInstance(this).getActiveAccount(this);
			if (arrayOfAccount.length == 2) {
				if (arrayOfAccount[0].name.equals(str1))
					;
				for (String str2 = arrayOfAccount[1].name;; str2 = arrayOfAccount[0].name) {
					Utils.changeAccount(this, str2, true);
					finish();
					return;
				}
			}
			startActivity(localIntent);
			return;
		}
		startActivity(localIntent);
	}

	private void updateOrganizeMode() {
		ListView localListView = getListView();
		localListView.clearChoices();
		localListView.setChoiceMode(2);
		if (this.mBatchConversations.size() == 0)
			hideFooter();
	}

	private String validateAccountName(String paramString) {
		if ((paramString != null)
				&& (Utils.isValidGoogleAccount(this, paramString)))
			return paramString;
		return null;
	}

	boolean allowBatch() {
		return this.mAllowBatch;
	}

	public Intent createConversationIntent(int paramInt) {
		Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
				.getCursor();
		localConversationCursor.moveTo(paramInt);
		return createConversationIntent(localConversationCursor, false);
	}

	public Set<String> getSelectedLabels() {
		if (this.mLongPressedConversation != null)
			return this.mLongPressedConversation.getLabels();
		if (this.mBatchConversations.size() > 0)
			return ((ConversationInfo) this.mBatchConversations.values()
					.iterator().next()).getLabels();
		return Sets.newHashSet();
	}

	public boolean isSelected(long paramLong) {
		return this.mBatchConversations.containsKey(Long.valueOf(paramLong));
	}

	protected void onActivityResult(int paramInt1, int paramInt2,
			Intent paramIntent) {
		switch (paramInt1) {
		default:
		case 3:
		}
		long l;
		do {
			do
				return;
			while (paramIntent == null);
			l = paramIntent.getLongExtra("conversation-removed", 0L);
		} while (l == 0L);
		this.mBatchConversations.remove(Long.valueOf(l));
		this.mUndoView.setVisibility(8);
		updateUndoView(false);
		this.mPreserveUndo = true;
	}

	public void onClick(View paramView) {
		switch (paramView.getId()) {
		default:
		case 1:
		case 3:
		case 2:
		case 2131361845:
		case 2131361843:
		case 2131361832:
		}
		do {
			do {
				do {
					do
						return;
					while (this.mBatchConversations.values().size() <= 0);
					this.mMenuHandler.performYButtonAction(new ArrayList(
							this.mBatchConversations.values()));
					return;
				} while (this.mBatchConversations.values().size() <= 0);
				this.mMenuHandler.changeLabels();
				return;
			} while (this.mBatchConversations.values().size() <= 0);
			this.mMenuHandler.delete(new ArrayList(this.mBatchConversations
					.values()));
			return;
			switchAccounts();
			return;
			this.mMenuHandler.showLabels();
			return;
		} while (sUndoOperation == null);
		BulkOperationHelper.getInstance(this).performOperationInternal(this,
				sUndoOperation.mAccount, sUndoOperation.mOperations,
				sUndoOperation.mConversations, this.mDisplayedLabel);
		setUndoOperation(this, null, this.mDisplayedLabel);
	}

	public boolean onContextItemSelected(MenuItem paramMenuItem) {
		Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
				.getCursor();
		boolean bool;
		switch (paramMenuItem.getItemId()) {
		default:
			ConversationInfo localConversationInfo = this.mLongPressedConversation;
			bool = false;
			if (localConversationInfo != null) {
				MenuHandler localMenuHandler = this.mMenuHandler;
				ConversationInfo[] arrayOfConversationInfo = new ConversationInfo[1];
				arrayOfConversationInfo[0] = this.mLongPressedConversation;
				bool = localMenuHandler.onMenuItemSelected(paramMenuItem,
						Arrays.asList(arrayOfConversationInfo),
						localConversationCursor.getLabels(), false);
			}
			break;
		case 2131361925:
		}
		while (true) {
			this.mLongPressedConversation = null;
			return bool;
			try {
				openConversationOrDraft(localConversationCursor);
				label97: bool = true;
			} catch (CursorIndexOutOfBoundsException localCursorIndexOutOfBoundsException) {
				break label97;
			}
		}
	}

	public void onCreate(Bundle paramBundle) {
		Gmail.startTiming("CLA.onCreate");
		super.onCreate(paramBundle);
		Gmail.startTiming("CLA.setContentView");
		setContentView(2130903050);
		Gmail.stopTiming("CLA.setContentView");
		initializeUndoViews();
		this.mBetweenChromeView = findViewById(2131361830);
		initUndoAnimations();
		this.mCustomTitleBarView = findViewById(2131361821);
		chooseAccount();
		if (this.mAccount == null) {
			Gmail.stopTiming("CLA.onCreate");
			return;
		}
		Utils.enableShortcutIntentFilter(this);
		if (this.mGmail == null)
			this.mGmail = LongShadowUtils
					.getContentProviderMailAccess(getContentResolver());
		if (this.mPrefs == null)
			this.mPrefs = Persistence.getInstance(this);
		QueryHandler localQueryHandler = new QueryHandler(this);
		this.mQueryHandler = localQueryHandler;
		this.mLabelMap = LongShadowUtils.getLabelMap(getContentResolver(),
				this.mAccount);
		Intent localIntent = getIntent();
		this.mQuery = null;
		String str1 = null;
		if ("android.intent.action.SEARCH".equals(localIntent.getAction())) {
			this.mQuery = localIntent.getStringExtra("query");
			if (str1 == null)
				str1 = localIntent.getStringExtra("label");
			this.mIntentLabel = str1;
			this.mUserSpecifiedInboxLabel = Utils.getAccountInbox(this,
					this.mAccount);
			if (!TextUtils.isEmpty(str1))
				break label348;
		}
		label348: for (String str2 = this.mUserSpecifiedInboxLabel;; str2 = str1) {
			this.mDisplayedLabel = str2;
			if ((this.mQuery == null) && (TextUtils.isEmpty(this.mIntentLabel)))
				this.mIntentLabel = this.mDisplayedLabel;
			if ((this.mLabelMap.getSortedUserLabels()
					.contains(this.mDisplayedLabel))
					|| (Gmail.LabelMap.getSortedUserMeaningfulSystemLabels()
							.contains(this.mDisplayedLabel)))
				break label355;
			Toast.makeText(this, getResources().getString(2131296484), 0)
					.show();
			finish();
			Gmail.stopTiming("CLA.onCreate");
			return;
			boolean bool = "android.intent.action.PROVIDER_CHANGED"
					.equals(localIntent.getAction());
			str1 = null;
			if (!bool)
				break;
			str1 = localIntent.getData().getLastPathSegment();
			break;
		}
		label355: setDefaultKeyMode(2);
		ListView localListView = getListView();
		localListView.setSelector(17301602);
		localListView.setItemsCanFocus(true);
		this.mEmptyView = findViewById(2131361835);
		MenuHandler.ActivityCallback local3 = new MenuHandler.ActivityCallback() {
			public void doneChangingLabels(
					LabelOperations paramAnonymousLabelOperations) {
				if (Utils.isConversationBeingRemoved(
						paramAnonymousLabelOperations,
						ConversationListActivity.this.mDisplayedLabel))
					ConversationListActivity.this.deselectAll();
				ConversationListActivity.this.updateUndoView(true);
			}

			public void onLabelChanged(String paramAnonymousString,
					long paramAnonymousLong, boolean paramAnonymousBoolean) {
			}
		};
		NetworkProgressMonitor localNetworkProgressMonitor;
		int i;
		if ("^^vmi".equals(this.mDisplayedLabel)) {
			this.mNonVoicemailDisplayedLabel = this.mUserSpecifiedInboxLabel;
			MenuHandler localMenuHandler = new MenuHandler(this, local3,
					this.mNonVoicemailDisplayedLabel, this.mAccount);
			this.mMenuHandler = localMenuHandler;
			this.mFooterOrganizeView = ((LinearLayout) findViewById(2131361836));
			Runnable local4 = new Runnable() {
				public void run() {
				}
			};
			localNetworkProgressMonitor = new NetworkProgressMonitor(this,
					local4);
			if (!"android.intent.action.PROVIDER_CHANGED".equals(localIntent
					.getAction()))
				break label718;
			i = 1;
		}
		while (true) {
			try {
				int j = Utils.getUnreadConversations(this, this.mAccount,
						this.mUserSpecifiedInboxLabel);
				if (j > 1) {
					i = 1;
					Gmail.ConversationCursor localConversationCursor = this.mGmail
							.getConversationCursorForQuery(
									this.mAccount,
									Gmail.getLabelSearchQuery(this.mUserSpecifiedInboxLabel),
									Gmail.BecomeActiveNetworkCursor.NO);
					if (localConversationCursor != null) {
						if ((i == 0) && (localConversationCursor.next()))
							openConversation(localConversationCursor, true);
						localConversationCursor.release();
						Utils.closeCursor(localConversationCursor.getCursor());
					}
					((GmailApplication) getApplication())
							.getRecentSuggestions().saveRecentQuery(
									this.mQuery, null);
					this.mCursorAdapter = new ConversationHeaderCursorAdapter(
							this, localNetworkProgressMonitor,
							this.mNonVoicemailDisplayedLabel, this.mGmail,
							this.mAccount);
					localListView.setFocusable(true);
					localListView.setClickable(true);
					localListView.setSaveEnabled(false);
					registerForContextMenu(localListView);
					this.mGmail = LongShadowUtils
							.getContentProviderMailAccess(getContentResolver());
					Gmail.stopTiming("CLA.onCreate");
					return;
					this.mNonVoicemailDisplayedLabel = this.mDisplayedLabel;
					break;
				}
				i = 0;
				continue;
			} catch (IllegalArgumentException localIllegalArgumentException) {
				Log.d("Gmail", "Error retrieving unread count: "
						+ localIllegalArgumentException);
				continue;
			}
			label718: if (localIntent.getBooleanExtra("show-whats-new", false)) {
				showDialog(2130903080);
				localIntent.removeExtra("show-whats-new");
			}
		}
	}

	public void onCreateContextMenu(ContextMenu paramContextMenu,
			View paramView, ContextMenu.ContextMenuInfo paramContextMenuInfo) {
		if (this.mBatchConversations.size() > 0)
			;
		while (true) {
			return;
			try {
				AdapterView.AdapterContextMenuInfo localAdapterContextMenuInfo = (AdapterView.AdapterContextMenuInfo) paramContextMenuInfo;
				Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
						.getCursor();
				if (localConversationCursor
						.moveTo(localAdapterContextMenuInfo.position)) {
					paramContextMenu.setHeaderTitle(localConversationCursor
							.getSubject());
					MenuInflater localMenuInflater = getMenuInflater();
					if (!"^^out".equals(this.mDisplayedLabel))
						localMenuInflater.inflate(2131623937, paramContextMenu);
					long l1 = localConversationCursor.getConversationId();
					long l2 = localConversationCursor.getMaxServerMessageId();
					Set localSet = localConversationCursor.getLabels();
					this.mLongPressedConversation = new ConversationInfo(l1,
							l2, localSet);
					this.mMenuHandler.onPrepareMenu(paramContextMenu, localSet,
							getSelectedConversations(), false);
					return;
				}
			} catch (ClassCastException localClassCastException) {
				Log.e("Gmail", "bad menuInfo", localClassCastException);
			}
		}
	}

	protected Dialog onCreateDialog(int paramInt, Bundle paramBundle) {
		if (paramInt == 1)
			return this.mMenuHandler.createLabelDialog();
		if (paramInt == 2)
			return new GoToLabelDialog(this);
		if (paramInt == 3)
			return new AssignLabelDialog(this, this.mDisplayedLabel);
		if (paramInt == 2130903080)
			return new WhatsNewDialog(this);
		return super.onCreateDialog(paramInt, paramBundle);
	}

	protected void onDestroy() {
		if (this.mCursorAdapter != null) {
			AsyncTask local6 = new AsyncTask() {
				protected Void doInBackground(
						ConversationHeaderCursorAdapter[] paramAnonymousArrayOfConversationHeaderCursorAdapter) {
					paramAnonymousArrayOfConversationHeaderCursorAdapter[0]
							.changeCursor(null);
					return null;
				}
			};
			ConversationHeaderCursorAdapter[] arrayOfConversationHeaderCursorAdapter = new ConversationHeaderCursorAdapter[1];
			arrayOfConversationHeaderCursorAdapter[0] = this.mCursorAdapter;
			local6.execute(arrayOfConversationHeaderCursorAdapter);
		}
		super.onDestroy();
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		int i = getSelectedItemPosition();
		if (i == -1) {
			Collection localCollection = getSelectedConversations();
			int j = localCollection.size();
			boolean bool2 = false;
			if (j > 0)
				bool2 = this.mMenuHandler.onKeyDown(paramInt, paramKeyEvent,
						this.mAccount, localCollection, getSelectedLabels());
			if (!bool2)
				bool2 = super.onKeyDown(paramInt, paramKeyEvent);
			return bool2;
		}
		Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
				.getCursor();
		if ((!localConversationCursor.getCursor().isClosed())
				&& (i < localConversationCursor.count())) {
			localConversationCursor.moveTo(i);
			boolean bool1 = false;
			switch (paramInt) {
			default:
			case 52:
			}
			while (true) {
				if (!bool1)
					bool1 = this.mMenuHandler.onKeyDown(paramInt,
							paramKeyEvent, this.mAccount,
							getSelectedConversations(), getSelectedLabels());
				if (!bool1)
					bool1 = super.onKeyDown(paramInt, paramKeyEvent);
				return bool1;
				((CanvasConversationHeaderView) getListView().getSelectedView())
						.toggleCheckMark();
				bool1 = true;
			}
		}
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	protected void onListItemClick(ListView paramListView, View paramView,
			int paramInt, long paramLong) {
		openConversation(paramInt);
	}

	protected void onNewIntent(Intent paramIntent) {
		paramIntent.setFlags(0xDFFFFFFF & paramIntent.getFlags());
		startActivity(paramIntent);
	}

	public boolean onOptionsItemSelected(MenuItem paramMenuItem) {
		int i = paramMenuItem.getItemId();
		if (i == 2131361940) {
			deselectAll();
			return true;
		}
		if (i == 2131361934) {
			switchAccounts();
			return true;
		}
		if (i == 2131361961) {
			Utils.performAccountPreferences(this);
			return true;
		}
		boolean bool = this.mMenuHandler.onMenuItemSelected(i,
				this.mBatchConversations.values(), getSelectedLabels(), false);
		if (i == 2131361932)
			updateUndoView(false);
		return bool;
	}

	public void onPause() {
		super.onPause();
		if (this.mNewEmailReceiver != null)
			;
		try {
			unregisterReceiver(this.mNewEmailReceiver);
			this.mNewEmailReceiver = null;
			getListView().setEmptyView(null);
			this.mMenuHandler.onPause();
			this.mActivityPaused = true;
			return;
		} catch (IllegalArgumentException localIllegalArgumentException) {
			while (true)
				Log.e("Gmail", localIllegalArgumentException.toString());
		}
	}

	protected void onPrepareDialog(int paramInt, Dialog paramDialog) {
		if (paramInt == 1)
			this.mMenuHandler.prepareLabelDialog(
					(ApplyRemoveLabelDialog) paramDialog, getSelectedLabels(),
					getSelectedConversations());
		do {
			return;
			if (paramInt == 2) {
				((GoToLabelDialog) paramDialog).onPrepare(this.mAccount,
						this.mLabelMap.getSortedUserLabels(),
						getSelectedConversations());
				return;
			}
		} while (paramInt != 3);
		((AssignLabelDialog) paramDialog).onPrepare(this.mAccount,
				this.mLabelMap.getSortedUserLabels(),
				getSelectedConversations());
	}

	public boolean onPrepareOptionsMenu(Menu paramMenu)
  {
    paramMenu.clear();
    MenuInflater localMenuInflater = getMenuInflater();
    int i;
    int j;
    label95: boolean bool2;
    if (this.mBatchConversations.size() > 0)
    {
      localMenuInflater.inflate(2131623939, paramMenu);
      MenuItem localMenuItem2 = paramMenu.findItem(2131361928);
      Set localSet = getSelectedLabels();
      if (localMenuItem2 != null)
      {
        boolean bool3 = localSet.contains("^u");
        if (bool3)
        {
          i = 2130837581;
          localMenuItem2.setIcon(i);
          if (!bool3)
            break label222;
          j = 2131296286;
          localMenuItem2.setTitle(j);
        }
      }
      else
      {
        MenuItem localMenuItem3 = paramMenu.findItem(2131361931);
        if (localMenuItem3 != null)
        {
          if ("^s".equals(this.mDisplayedLabel))
            break label230;
          bool2 = true;
          label137: localMenuItem3.setVisible(bool2);
        }
        this.mMenuHandler.onPrepareMuteMenuItem(paramMenu, this.mBatchConversations.values());
        this.mMenuHandler.onPrepareStarMenuItem(paramMenu, this.mBatchConversations.values());
        this.mMenuHandler.onPrepareImportantMenuItem(paramMenu, this.mBatchConversations.values());
        this.mMenuHandler.onPrepareYMenuItem(paramMenu, localSet);
      }
    }
    while (true)
    {
      return super.onPrepareOptionsMenu(paramMenu);
      i = 2130837582;
      break;
      label222: j = 2131296287;
      break label95;
      label230: bool2 = false;
      break label137;
      localMenuInflater.inflate(2131623938, paramMenu);
      MenuItem localMenuItem1 = paramMenu.findItem(2131361936);
      if (!this.mUserSpecifiedInboxLabel.equals(this.mDisplayedLabel));
      for (boolean bool1 = true; ; bool1 = false)
      {
        localMenuItem1.setVisible(bool1);
        if (!Persistence.getInstance(this).getFastSwitching(this))
          break label323;
        paramMenu.findItem(2131361934).setTitle(2131296302);
        maybeHideAccountsMenuItem(paramMenu);
        break;
      }
      label323: paramMenu.findItem(2131361934).setTitle(2131296278);
    }
  }

	protected void onRestart() {
		super.onRestart();
		if (this.mAccount == null)
			;
	}

	protected void onRestoreInstanceState(Bundle paramBundle) {
		super.onRestoreInstanceState(paramBundle);
		this.mListState = paramBundle.getParcelable("liststate");
		Iterator localIterator = paramBundle.keySet().iterator();
		while (localIterator.hasNext()) {
			String str = (String) localIterator.next();
			if (str.startsWith(CONVERSATION_ID_KEY)) {
				ConversationInfo localConversationInfo = ConversationInfo
						.deserialize(paramBundle.getString(str));
				toggleConversation(localConversationInfo.getConversationId(),
						localConversationInfo.getMaxMessageId(),
						localConversationInfo.getLabels());
			}
		}
		UndoOperation localUndoOperation = UndoOperation
				.restoreFromExtras(paramBundle);
		if (localUndoOperation != null) {
			initializeUndoViews();
			setUndoOperation(this, localUndoOperation, this.mDisplayedLabel);
			updateUndoView(false);
			this.mPreserveUndo = true;
		}
		this.mMenuHandler.onRestoreInstanceState(paramBundle);
	}

	public void onResume() {
		Gmail.startTiming("CLA.onResume");
		super.onResume();
		this.mActivityPaused = false;
		this.mCursorAdapter.startQueryAndUpdateOnChange(this.mQueryHandler,
				makeQueryString(this.mQuery, this.mIntentLabel));
		try {
			initBestGuessTitleBar();
			asyncGetAccountsInfo();
			if (this.mUndoView == null)
				initializeUndoViews();
			this.mAllowBatch = Persistence.getInstance(this)
					.getAllowBatch(this);
			if (!this.mPreserveUndo) {
				setUndoOperation(this, null, this.mDisplayedLabel);
				updateUndoView(false);
			}
			this.mPreserveUndo = false;
			if (("^i".equals(this.mDisplayedLabel))
					|| ("^^vmi".equals(this.mDisplayedLabel))
					|| ("^iim".equals(this.mDisplayedLabel))) {
				if (this.mNewEmailReceiver == null)
					this.mNewEmailReceiver = new BroadcastReceiver() {
						public void onReceive(Context paramAnonymousContext,
								Intent paramAnonymousIntent) {
							if (!"android.intent.action.PROVIDER_CHANGED"
									.equals(paramAnonymousIntent.getAction()))
								;
							String str2;
							do {
								String str1;
								do {
									return;
									str1 = paramAnonymousIntent
											.getStringExtra("account");
								} while (!ConversationListActivity.this.mAccount
										.equals(str1));
								str2 = paramAnonymousIntent.getData()
										.getLastPathSegment();
							} while (((!TextUtils
									.equals(str2,
											ConversationListActivity.this.mDisplayedLabel)) && ((!"^^vmi"
									.equals(str2))
									|| (!"^i"
											.equals(ConversationListActivity.this.mDisplayedLabel)) || (!"^iim"
										.equals(ConversationListActivity.this.mDisplayedLabel))))
									|| (paramAnonymousIntent.getIntExtra(
											"count", 0) == 0));
							Log.i("Gmail",
									"Aborting broadcast of intent "
											+ paramAnonymousIntent
											+ ", mDisplayedLabel is "
											+ ConversationListActivity.this.mDisplayedLabel);
							abortBroadcast();
						}
					};
				IntentFilter localIntentFilter = new IntentFilter(
						"android.intent.action.PROVIDER_CHANGED");
				localIntentFilter.setPriority(0);
				localIntentFilter.addDataScheme("content");
				localIntentFilter.addDataAuthority("gmail-ls", null);
				localIntentFilter.addDataPath("/unread/", 1);
				registerReceiver(this.mNewEmailReceiver, localIntentFilter);
			}
			updateOrganizeMode();
			this.mMenuHandler.onResume();
			Gmail.stopTiming("CLA.onResume");
			return;
		} catch (IllegalArgumentException localIllegalArgumentException) {
			while (true)
				Utils.handleAccountNotSynchronized(this);
		}
	}

	protected void onSaveInstanceState(Bundle paramBundle) {
		super.onSaveInstanceState(paramBundle);
		this.mListState = getListView().onSaveInstanceState();
		paramBundle.putParcelable("liststate", this.mListState);
		if (this.mBatchConversations != null) {
			int i = 0;
			Iterator localIterator = this.mBatchConversations.values()
					.iterator();
			while (localIterator.hasNext()) {
				ConversationInfo localConversationInfo = (ConversationInfo) localIterator
						.next();
				StringBuilder localStringBuilder = new StringBuilder().append(
						CONVERSATION_ID_KEY).append("-");
				int j = i + 1;
				paramBundle.putString(i, localConversationInfo.serialize());
				i = j;
			}
		}
		if (sUndoOperation != null)
			sUndoOperation.saveToExtras(paramBundle);
		this.mMenuHandler.onSaveInstanceState(paramBundle);
	}

	protected void onStart() {
		super.onStart();
		setListAdapter(this.mCursorAdapter);
		this.mQueryHandler.setAdapter(this.mCursorAdapter);
		this.mHandler.postDelayed(this.mUpdateTimestampsRunnable, 60000L);
		this.mLabelMap.addObserver(this.mLabelsObserver);
	}

	protected void onStop() {
		if (this.mCursorAdapter != null) {
			setListAdapter(null);
			this.mCursorAdapter.stopRespondingToUpdates();
		}
		if (this.mLabelMap != null)
			this.mLabelMap.deleteObserver(this.mLabelsObserver);
		this.mHandler.removeCallbacks(this.mUpdateTimestampsRunnable);
		super.onStop();
	}

	public void onWindowFocusChanged(boolean paramBoolean) {
		super.onWindowFocusChanged(paramBoolean);
		Gmail.ConversationCursor localConversationCursor = this.mCursorAdapter
				.getCursor();
		if (paramBoolean)
			Utils.cancelNotification(this, this.mAccount);
		if (localConversationCursor != null)
			Utils.markConversationsVisible(localConversationCursor,
					paramBoolean);
	}

	public void toggleConversation(long paramLong1, long paramLong2,
			Set<String> paramSet) {
		int i = this.mBatchConversations.size();
		if (this.mBatchConversations.containsKey(Long.valueOf(paramLong1))) {
			this.mBatchConversations.remove(Long.valueOf(paramLong1));
			if ((this.mBatchConversations.size() != 1) || (i != 0))
				break label92;
			showFooter();
		}
		label92: while (this.mBatchConversations.size() != 0) {
			return;
			this.mBatchConversations.put(Long.valueOf(paramLong1),
					new ConversationInfo(paramLong1, paramLong2, paramSet));
			break;
		}
		hideFooter();
	}

	public void updateSelectedLabels(long paramLong, Set<String> paramSet) {
		ConversationInfo localConversationInfo = (ConversationInfo) this.mBatchConversations
				.get(Long.valueOf(paramLong));
		if (localConversationInfo != null)
			localConversationInfo.setLabels(paramSet);
	}

	public void updateUndoView(boolean paramBoolean) {
		if (this.mUndoView == null)
			return;
		UndoOperation localUndoOperation = sUndoOperation;
		if (localUndoOperation != null) {
			if ((paramBoolean) && (this.mUndoView.getVisibility() == 8))
				this.mBetweenChromeView.startAnimation(this.mShowUndoAnimation);
			this.mUndoView.setVisibility(0);
			this.mUndoButtonView.setOnClickListener(this);
			this.mUndoDescriptionView.setText(localUndoOperation.mDescription);
			this.mUndoActionView.setVisibility(0);
			return;
		}
		if ((paramBoolean) && (this.mUndoView.getVisibility() != 8)) {
			getListView().startAnimation(this.mHideListUndoAnimation);
			this.mUndoView.startAnimation(this.mHideUndoAnimation);
		}
		this.mUndoDescriptionView.setText("");
		this.mUndoActionView.setVisibility(8);
		this.mUndoView.setVisibility(8);
	}

	private class QueryHandler extends AsyncQueryHandler {
		private ConversationHeaderCursorAdapter mAdapter;

		public QueryHandler(Activity arg2) {
			super();
			Activity localActivity;
			ConversationListActivity.access$302(ConversationListActivity.this,
					localActivity);
		}

		private void promptForCredentials()
    {
      Account localAccount = new Account(ConversationListActivity.this.mAccount, "com.google");
      Bundle localBundle = new Bundle();
      AccountManager.get(ConversationListActivity.this.mActivity).getAuthToken(localAccount, "mail", localBundle, ConversationListActivity.this.mActivity, new AccountManagerCallback()
      {
        public void run(AccountManagerFuture<Bundle> paramAnonymousAccountManagerFuture)
        {
          try
          {
            ((Bundle)paramAnonymousAccountManagerFuture.getResult());
            ConversationListActivity.QueryHandler.this.mAdapter.getCursor().retry();
            return;
          }
          catch (OperationCanceledException localOperationCanceledException)
          {
            ConversationListActivity.access$1002(ConversationListActivity.this, true);
            return;
          }
          catch (AuthenticatorException localAuthenticatorException)
          {
          }
          catch (IOException localIOException)
          {
          }
        }
      }
      , null);
    }

		private void validateBatchConversations(
				Gmail.ConversationCursor paramConversationCursor) {
			int i = paramConversationCursor.position();
			if (i != -1)
				paramConversationCursor.moveTo(-1);
			ArrayList localArrayList = new ArrayList(
					ConversationListActivity.this.mBatchConversations.keySet());
			while ((localArrayList.size() > 0)
					&& (paramConversationCursor.next()))
				localArrayList.remove(Long.valueOf(paramConversationCursor
						.getConversationId()));
			Iterator localIterator = localArrayList.iterator();
			while (localIterator.hasNext()) {
				long l = ((Long) localIterator.next()).longValue();
				ConversationListActivity.this.deselectConversation(l);
			}
			paramConversationCursor.moveTo(i);
		}

		protected void onQueryComplete(int paramInt, Object paramObject,
				Cursor paramCursor) {
			if (ConversationListActivity.this.mActivityPaused) {
				if (paramCursor != null)
					Utils.closeCursor(paramCursor);
				return;
			}
			Gmail.ConversationCursor localConversationCursor = ConversationListActivity.this.mGmail
					.getConversationCursorForCursor(
							ConversationListActivity.this.mAccount, paramCursor);
			this.mAdapter.changeCursor(localConversationCursor);
			if (ConversationListActivity.this.mListState != null) {
				ConversationListActivity.this.getListView()
						.onRestoreInstanceState(
								ConversationListActivity.this.mListState);
				if (!ConversationListActivity.this.mActivityPaused)
					ConversationListActivity.access$702(
							ConversationListActivity.this, null);
			}
			while (true) {
				ConversationListActivity.this.mHandler.post(new Runnable() {
					public void run() {
						ConversationListActivity.this
								.getListView()
								.setEmptyView(
										ConversationListActivity.this.mEmptyView);
					}
				});
				if (ConversationListActivity.this.getListView()
						.hasWindowFocus())
					Utils.markConversationsVisible(localConversationCursor,
							true);
				Gmail.CursorError localCursorError = localConversationCursor
						.getError();
				if ((localConversationCursor.getStatus() != Gmail.CursorStatus.ERROR)
						|| (localCursorError != Gmail.CursorError.AUTH_ERROR)
						|| (ConversationListActivity.this.mUserCancelledAuth))
					break;
				promptForCredentials();
				return;
				if (ConversationListActivity.this.mBatchConversations.size() > 0)
					validateBatchConversations(localConversationCursor);
			}
		}

		public void setAdapter(
				ConversationHeaderCursorAdapter paramConversationHeaderCursorAdapter) {
			this.mAdapter = paramConversationHeaderCursorAdapter;
		}
	}
}

/*
 * Location: C:\Users\科\Desktop\classes_dex2jar.jar Qualified Name:
 * com.google.android.gm.ConversationListActivity JD-Core Version: 0.6.2
 */