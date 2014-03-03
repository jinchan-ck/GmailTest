package com.android.mail.providers;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.provider.Settings;
import android.text.TextUtils;

import com.android.mail.utils.LogTag;
import com.android.mail.utils.LogUtils;
import com.android.mail.utils.Utils;
import com.google.common.base.Objects;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Account extends android.accounts.Account implements Parcelable {
	public static final Parcelable.Creator<Account> CREATOR;
	private static final String LOG_TAG;
	public String accountFromAddresses;
	public final Uri accoutCookieQueryUri;
	public final int capabilities;
	public final int color;
	public final Uri composeIntentUri;
	public final Uri defaultRecentFolderListUri;
	public final Uri expungeMessageUri;
	public final Uri folderListUri;
	public Uri fullFolderListUri;
	public final Uri helpIntentUri;
	private transient List<ReplyFromAccount> mReplyFroms;
	public final Uri manualSyncUri;
	public final String mimeType;
	public final int providerVersion;
	public final Uri reauthenticationIntentUri;
	public final Uri recentFolderListUri;

	@Deprecated
	public final Uri saveDraftUri;
	public final Uri searchUri;
	public final Uri sendFeedbackIntentUri;

	@Deprecated
	public final Uri sendMessageUri;
	public final Settings settings;
	public final Uri settingsIntentUri;
	public final int syncStatus;
	public final Uri undoUri;
	public final Uri updateSettingsUri;
	public final Uri uri;
	public final Uri viewIntentProxyUri;

	public Account(Cursor paramCursor) {
		super(paramCursor.getString(1), "unknown");
		this.accountFromAddresses = paramCursor.getString(8);
		this.capabilities = paramCursor.getInt(4);
		this.providerVersion = paramCursor.getInt(2);
		this.uri = Uri.parse(paramCursor.getString(3));
		this.folderListUri = Uri.parse(paramCursor.getString(5));
		this.fullFolderListUri = Utils.getValidUri(paramCursor.getString(6));
		this.searchUri = Utils.getValidUri(paramCursor.getString(7));
		this.saveDraftUri = Utils.getValidUri(paramCursor.getString(9));
		this.sendMessageUri = Utils.getValidUri(paramCursor.getString(10));
		this.expungeMessageUri = Utils.getValidUri(paramCursor.getString(11));
		this.undoUri = Utils.getValidUri(paramCursor.getString(12));
		this.settingsIntentUri = Utils.getValidUri(paramCursor.getString(13));
		this.helpIntentUri = Utils.getValidUri(paramCursor.getString(15));
		this.sendFeedbackIntentUri = Utils.getValidUri(paramCursor
				.getString(16));
		this.reauthenticationIntentUri = Utils.getValidUri(paramCursor
				.getString(17));
		this.syncStatus = paramCursor.getInt(14);
		this.composeIntentUri = Utils.getValidUri(paramCursor.getString(18));
		this.mimeType = paramCursor.getString(19);
		this.recentFolderListUri = Utils.getValidUri(paramCursor.getString(20));
		this.color = paramCursor.getInt(21);
		this.defaultRecentFolderListUri = Utils.getValidUri(paramCursor
				.getString(22));
		this.manualSyncUri = Utils.getValidUri(paramCursor.getString(23));
		this.viewIntentProxyUri = Utils.getValidUri(paramCursor.getString(24));
		this.accoutCookieQueryUri = Utils
				.getValidUri(paramCursor.getString(25));
		this.updateSettingsUri = Utils.getValidUri(paramCursor.getString(43));
		this.settings = new Settings(paramCursor);
	}

	public Account(Parcel paramParcel) {
		super(paramParcel);
		this.providerVersion = paramParcel.readInt();
		this.uri = ((Uri) paramParcel.readParcelable(null));
		this.capabilities = paramParcel.readInt();
		this.folderListUri = ((Uri) paramParcel.readParcelable(null));
		this.fullFolderListUri = ((Uri) paramParcel.readParcelable(null));
		this.searchUri = ((Uri) paramParcel.readParcelable(null));
		this.accountFromAddresses = paramParcel.readString();
		this.saveDraftUri = ((Uri) paramParcel.readParcelable(null));
		this.sendMessageUri = ((Uri) paramParcel.readParcelable(null));
		this.expungeMessageUri = ((Uri) paramParcel.readParcelable(null));
		this.undoUri = ((Uri) paramParcel.readParcelable(null));
		this.settingsIntentUri = ((Uri) paramParcel.readParcelable(null));
		this.helpIntentUri = ((Uri) paramParcel.readParcelable(null));
		this.sendFeedbackIntentUri = ((Uri) paramParcel.readParcelable(null));
		this.reauthenticationIntentUri = ((Uri) paramParcel
				.readParcelable(null));
		this.syncStatus = paramParcel.readInt();
		this.composeIntentUri = ((Uri) paramParcel.readParcelable(null));
		this.mimeType = paramParcel.readString();
		this.recentFolderListUri = ((Uri) paramParcel.readParcelable(null));
		this.color = paramParcel.readInt();
		this.defaultRecentFolderListUri = ((Uri) paramParcel
				.readParcelable(null));
		this.manualSyncUri = ((Uri) paramParcel.readParcelable(null));
		this.viewIntentProxyUri = ((Uri) paramParcel.readParcelable(null));
		this.accoutCookieQueryUri = ((Uri) paramParcel.readParcelable(null));
		this.updateSettingsUri = ((Uri) paramParcel.readParcelable(null));
		Settings localSettings = Settings.newInstance(paramParcel.readString());
		if (localSettings != null) {
			this.settings = localSettings;
			return;
		}
		LogUtils.e(LOG_TAG, new Throwable(),
				"Unexpected null settings in Account(Parcel)", new Object[0]);
		this.settings = Settings.EMPTY_SETTINGS;
	}

	private Account(String paramString1, String paramString2,
			String paramString3) throws JSONException {
		super(paramString1, paramString2);
		JSONObject localJSONObject = new JSONObject(paramString3);
		this.providerVersion = localJSONObject.getInt("providerVersion");
		this.uri = Uri.parse(localJSONObject.optString("accountUri"));
		this.capabilities = localJSONObject.getInt("capabilities");
		this.folderListUri = Utils.getValidUri(localJSONObject
				.optString("folderListUri"));
		this.fullFolderListUri = Utils.getValidUri(localJSONObject
				.optString("fullFolderListUri"));
		this.searchUri = Utils.getValidUri(localJSONObject
				.optString("searchUri"));
		this.accountFromAddresses = localJSONObject.optString(
				"accountFromAddresses", "");
		this.saveDraftUri = Utils.getValidUri(localJSONObject
				.optString("saveDraftUri"));
		this.sendMessageUri = Utils.getValidUri(localJSONObject
				.optString("sendMailUri"));
		this.expungeMessageUri = Utils.getValidUri(localJSONObject
				.optString("expungeMessageUri"));
		this.undoUri = Utils.getValidUri(localJSONObject.optString("undoUri"));
		this.settingsIntentUri = Utils.getValidUri(localJSONObject
				.optString(UIProvider.AccountColumns.SETTINGS_INTENT_URI));
		this.helpIntentUri = Utils.getValidUri(localJSONObject
				.optString(UIProvider.AccountColumns.HELP_INTENT_URI));
		this.sendFeedbackIntentUri = Utils.getValidUri(localJSONObject
				.optString(UIProvider.AccountColumns.SEND_FEEDBACK_INTENT_URI));
		this.reauthenticationIntentUri = Utils
				.getValidUri(localJSONObject
						.optString(UIProvider.AccountColumns.REAUTHENTICATION_INTENT_URI));
		this.syncStatus = localJSONObject.optInt("syncStatus");
		this.composeIntentUri = Utils.getValidUri(localJSONObject
				.optString("composeUri"));
		this.mimeType = localJSONObject.optString("mimeType");
		this.recentFolderListUri = Utils.getValidUri(localJSONObject
				.optString("recentFolderListUri"));
		this.color = localJSONObject.optInt("color", 0);
		this.defaultRecentFolderListUri = Utils.getValidUri(localJSONObject
				.optString("defaultRecentFolderListUri"));
		this.manualSyncUri = Utils.getValidUri(localJSONObject
				.optString("manualSyncUri"));
		this.viewIntentProxyUri = Utils.getValidUri(localJSONObject
				.optString("viewProxyUri"));
		this.accoutCookieQueryUri = Utils.getValidUri(localJSONObject
				.optString("accountCookieUri"));
		this.updateSettingsUri = Utils.getValidUri(localJSONObject
				.optString("updateSettingsUri"));
		Settings localSettings = Settings.newInstance(localJSONObject
				.optJSONObject("settings"));
		if (localSettings != null) {
			this.settings = localSettings;
			return;
		}
		LogUtils.e(LOG_TAG, new Throwable(),
				"Unexpected null settings in Account(name, type, jsonAccount)",
				new Object[0]);
		this.settings = Settings.EMPTY_SETTINGS;
	}

	public static int findPosition(Account[] paramArrayOfAccount, Uri paramUri) {
		if ((paramArrayOfAccount != null) && (paramArrayOfAccount.length > 0)
				&& (paramUri != null)) {
			int i = 0;
			int j = paramArrayOfAccount.length;
			while (i < j) {
				if (paramArrayOfAccount[i].uri.equals(paramUri)) {
					String str = LOG_TAG;
					Object[] arrayOfObject = new Object[1];
					arrayOfObject[0] = Integer.valueOf(i);
					LogUtils.d(
							str,
							"findPositionOfAccount: Found needle at position %d",
							arrayOfObject);
					return i;
				}
				i++;
			}
		}
		return -1;
	}

	public static Account[] getAllAccounts(Cursor paramCursor) {
		int i = paramCursor.getCount();
		if ((i <= 0) || (!paramCursor.moveToFirst())) {
			arrayOfAccount = new Account[0];
			return arrayOfAccount;
		}
		Account[] arrayOfAccount = new Account[i];
		int k;
		for (int j = 0;; j = k) {
			k = j + 1;
			arrayOfAccount[j] = new Account(paramCursor);
			if (!paramCursor.moveToNext()) {
				if (($assertionsDisabled) || (k == i))
					break;
				throw new AssertionError();
			}
		}
	}

	// ERROR //
	public static Account newinstance(String paramString) {
		// Byte code:
		// 0: new 195 org/json/JSONObject
		// 3: dup
		// 4: aload_0
		// 5: invokespecial 198 org/json/JSONObject:<init> (Ljava/lang/String;)V
		// 8: astore_1
		// 9: new 2 com/android/mail/providers/Account
		// 12: dup
		// 13: aload_1
		// 14: ldc_w 303
		// 17: invokevirtual 307 org/json/JSONObject:get
		// (Ljava/lang/String;)Ljava/lang/Object;
		// 20: checkcast 309 java/lang/String
		// 23: aload_1
		// 24: ldc_w 311
		// 27: invokevirtual 307 org/json/JSONObject:get
		// (Ljava/lang/String;)Ljava/lang/Object;
		// 30: checkcast 309 java/lang/String
		// 33: aload_0
		// 34: invokespecial 313 com/android/mail/providers/Account:<init>
		// (Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
		// 37: astore_2
		// 38: aload_2
		// 39: areturn
		// 40: astore_3
		// 41: getstatic 64 com/android/mail/providers/Account:LOG_TAG
		// Ljava/lang/String;
		// 44: aload_3
		// 45: ldc_w 315
		// 48: iconst_1
		// 49: anewarray 181 java/lang/Object
		// 52: dup
		// 53: iconst_0
		// 54: aload_0
		// 55: aastore
		// 56: invokestatic 187 com/android/mail/utils/LogUtils:e
		// (Ljava/lang/String;Ljava/lang/Throwable;Ljava/lang/String;[Ljava/lang/Object;)I
		// 59: pop
		// 60: aconst_null
		// 61: areturn
		// 62: astore_3
		// 63: goto -22 -> 41
		//
		// Exception table:
		// from to target type
		// 0 9 40 org/json/JSONException
		// 9 38 62 org/json/JSONException
	}

	public boolean equals(Object paramObject) {
		if (paramObject == this)
			;
		Account localAccount;
		do {
			return true;
			if ((paramObject == null) || (paramObject.getClass() != getClass()))
				return false;
			localAccount = (Account) paramObject;
		} while ((TextUtils.equals(this.name, localAccount.name))
				&& (TextUtils.equals(this.type, localAccount.type))
				&& (this.capabilities == localAccount.capabilities)
				&& (this.providerVersion == localAccount.providerVersion)
				&& (Objects.equal(this.uri, localAccount.uri))
				&& (Objects.equal(this.folderListUri,
						localAccount.folderListUri))
				&& (Objects.equal(this.fullFolderListUri,
						localAccount.fullFolderListUri))
				&& (Objects.equal(this.searchUri, localAccount.searchUri))
				&& (Objects.equal(this.accountFromAddresses,
						localAccount.accountFromAddresses))
				&& (Objects.equal(this.saveDraftUri, localAccount.saveDraftUri))
				&& (Objects.equal(this.sendMessageUri,
						localAccount.sendMessageUri))
				&& (Objects.equal(this.expungeMessageUri,
						localAccount.expungeMessageUri))
				&& (Objects.equal(this.undoUri, localAccount.undoUri))
				&& (Objects.equal(this.settingsIntentUri,
						localAccount.settingsIntentUri))
				&& (Objects.equal(this.helpIntentUri,
						localAccount.helpIntentUri))
				&& (Objects.equal(this.sendFeedbackIntentUri,
						localAccount.sendFeedbackIntentUri))
				&& (Objects.equal(this.reauthenticationIntentUri,
						localAccount.reauthenticationIntentUri))
				&& (this.syncStatus == localAccount.syncStatus)
				&& (Objects.equal(this.composeIntentUri,
						localAccount.composeIntentUri))
				&& (TextUtils.equals(this.mimeType, localAccount.mimeType))
				&& (Objects.equal(this.recentFolderListUri,
						localAccount.recentFolderListUri))
				&& (this.color == localAccount.color)
				&& (Objects.equal(this.defaultRecentFolderListUri,
						localAccount.defaultRecentFolderListUri))
				&& (Objects.equal(this.viewIntentProxyUri,
						localAccount.viewIntentProxyUri))
				&& (Objects.equal(this.accoutCookieQueryUri,
						localAccount.accoutCookieQueryUri))
				&& (Objects.equal(this.updateSettingsUri,
						localAccount.updateSettingsUri))
				&& (Objects.equal(this.settings, localAccount.settings)));
		return false;
	}

	public List<ReplyFromAccount> getReplyFroms() {
		if (this.mReplyFroms == null) {
			this.mReplyFroms = Lists.newArrayList();
			if (supportsCapability(524288))
				return this.mReplyFroms;
			this.mReplyFroms.add(new ReplyFromAccount(this, this.uri,
					this.name, this.name, this.name, false, false));
			if (!TextUtils.isEmpty(this.accountFromAddresses))
				try {
					JSONArray localJSONArray = new JSONArray(
							this.accountFromAddresses);
					int i = 0;
					int j = localJSONArray.length();
					while (i < j) {
						ReplyFromAccount localReplyFromAccount = ReplyFromAccount
								.deserialize(this,
										localJSONArray.getJSONObject(i));
						if (localReplyFromAccount != null)
							this.mReplyFroms.add(localReplyFromAccount);
						i++;
					}
				} catch (JSONException localJSONException) {
					String str = LOG_TAG;
					Object[] arrayOfObject = new Object[1];
					arrayOfObject[0] = this.name;
					LogUtils.e(str, localJSONException,
							"Unable to parse accountFromAddresses. name=%s",
							arrayOfObject);
				}
		}
		return this.mReplyFroms;
	}

	public int hashCode() {
		int i = super.hashCode();
		Object[] arrayOfObject = new Object[26];
		arrayOfObject[0] = this.name;
		arrayOfObject[1] = this.type;
		arrayOfObject[2] = Integer.valueOf(this.capabilities);
		arrayOfObject[3] = Integer.valueOf(this.providerVersion);
		arrayOfObject[4] = this.uri;
		arrayOfObject[5] = this.folderListUri;
		arrayOfObject[6] = this.fullFolderListUri;
		arrayOfObject[7] = this.searchUri;
		arrayOfObject[8] = this.accountFromAddresses;
		arrayOfObject[9] = this.saveDraftUri;
		arrayOfObject[10] = this.sendMessageUri;
		arrayOfObject[11] = this.expungeMessageUri;
		arrayOfObject[12] = this.undoUri;
		arrayOfObject[13] = this.settingsIntentUri;
		arrayOfObject[14] = this.helpIntentUri;
		arrayOfObject[15] = this.sendFeedbackIntentUri;
		arrayOfObject[16] = this.reauthenticationIntentUri;
		arrayOfObject[17] = Integer.valueOf(this.syncStatus);
		arrayOfObject[18] = this.composeIntentUri;
		arrayOfObject[19] = this.mimeType;
		arrayOfObject[20] = this.recentFolderListUri;
		arrayOfObject[21] = Integer.valueOf(this.color);
		arrayOfObject[22] = this.defaultRecentFolderListUri;
		arrayOfObject[23] = this.viewIntentProxyUri;
		arrayOfObject[24] = this.accoutCookieQueryUri;
		arrayOfObject[25] = this.updateSettingsUri;
		return i ^ Objects.hashCode(arrayOfObject);
	}

	public boolean isAccountInitializationRequired() {
		return (0x20 & this.syncStatus) == 32;
	}

	public boolean isAccountReady() {
		return (!isAccountInitializationRequired())
				&& (!isAccountSyncRequired());
	}

	public boolean isAccountSyncRequired() {
		return (0x8 & this.syncStatus) == 8;
	}

	public boolean matches(Account paramAccount) {
		return (paramAccount != null)
				&& (Objects.equal(this.uri, paramAccount.uri));
	}

	public boolean ownsFromAddress(String paramString) {
		Iterator localIterator = getReplyFroms().iterator();
		while (localIterator.hasNext())
			if (TextUtils.equals(
					((ReplyFromAccount) localIterator.next()).address,
					paramString))
				return true;
		return false;
	}

	public String serialize() {
		try {
			JSONObject localJSONObject = new JSONObject();
			try {
				localJSONObject.put("name", this.name);
				localJSONObject.put("type", this.type);
				localJSONObject.put("providerVersion", this.providerVersion);
				localJSONObject.put("accountUri", this.uri);
				localJSONObject.put("capabilities", this.capabilities);
				localJSONObject.put("folderListUri", this.folderListUri);
				localJSONObject
						.put("fullFolderListUri", this.fullFolderListUri);
				localJSONObject.put("searchUri", this.searchUri);
				localJSONObject.put("accountFromAddresses",
						this.accountFromAddresses);
				localJSONObject.put("saveDraftUri", this.saveDraftUri);
				localJSONObject.put("sendMailUri", this.sendMessageUri);
				localJSONObject
						.put("expungeMessageUri", this.expungeMessageUri);
				localJSONObject.put("undoUri", this.undoUri);
				localJSONObject.put(
						UIProvider.AccountColumns.SETTINGS_INTENT_URI,
						this.settingsIntentUri);
				localJSONObject.put(UIProvider.AccountColumns.HELP_INTENT_URI,
						this.helpIntentUri);
				localJSONObject.put(
						UIProvider.AccountColumns.SEND_FEEDBACK_INTENT_URI,
						this.sendFeedbackIntentUri);
				localJSONObject.put(
						UIProvider.AccountColumns.REAUTHENTICATION_INTENT_URI,
						this.reauthenticationIntentUri);
				localJSONObject.put("syncStatus", this.syncStatus);
				localJSONObject.put("composeUri", this.composeIntentUri);
				localJSONObject.put("mimeType", this.mimeType);
				localJSONObject.put("recentFolderListUri",
						this.recentFolderListUri);
				localJSONObject.put("color", this.color);
				localJSONObject.put("defaultRecentFolderListUri",
						this.defaultRecentFolderListUri);
				localJSONObject.put("manualSyncUri", this.manualSyncUri);
				localJSONObject.put("viewProxyUri", this.viewIntentProxyUri);
				localJSONObject.put("accountCookieUri",
						this.accoutCookieQueryUri);
				localJSONObject
						.put("updateSettingsUri", this.updateSettingsUri);
				if (this.settings != null)
					localJSONObject.put("settings", this.settings.toJSON());
				String str2 = localJSONObject.toString();
				return str2;
			} catch (JSONException localJSONException) {
				while (true) {
					String str1 = LOG_TAG;
					Object[] arrayOfObject = new Object[1];
					arrayOfObject[0] = this.name;
					LogUtils.wtf(str1, localJSONException,
							"Could not serialize account with name %s",
							arrayOfObject);
				}
			}
		} finally {
		}
	}

	public final boolean settingsDiffer(Account paramAccount) {
		if (paramAccount == null)
			;
		while ((this.syncStatus != paramAccount.syncStatus)
				|| (!Objects.equal(this.accountFromAddresses,
						paramAccount.accountFromAddresses))
				|| (this.color != paramAccount.color)
				|| (this.settings.hashCode() != paramAccount.settings
						.hashCode()))
			return true;
		return false;
	}

	public boolean supportsCapability(int paramInt) {
		return (paramInt & this.capabilities) != 0;
	}

	public String toString() {
		StringBuilder localStringBuilder = new StringBuilder();
		localStringBuilder.append("name=");
		localStringBuilder.append(this.name);
		localStringBuilder.append(",type=");
		localStringBuilder.append(this.type);
		localStringBuilder.append(",accountFromAddressUri=");
		localStringBuilder.append(this.accountFromAddresses);
		localStringBuilder.append(",capabilities=");
		localStringBuilder.append(this.capabilities);
		localStringBuilder.append(",providerVersion=");
		localStringBuilder.append(this.providerVersion);
		localStringBuilder.append(",folderListUri=");
		localStringBuilder.append(this.folderListUri);
		localStringBuilder.append(",fullFolderListUri=");
		localStringBuilder.append(this.fullFolderListUri);
		localStringBuilder.append(",searchUri=");
		localStringBuilder.append(this.searchUri);
		localStringBuilder.append(",saveDraftUri=");
		localStringBuilder.append(this.saveDraftUri);
		localStringBuilder.append(",sendMessageUri=");
		localStringBuilder.append(this.sendMessageUri);
		localStringBuilder.append(",expungeMessageUri=");
		localStringBuilder.append(this.expungeMessageUri);
		localStringBuilder.append(",undoUri=");
		localStringBuilder.append(this.undoUri);
		localStringBuilder.append(",settingsIntentUri=");
		localStringBuilder.append(this.settingsIntentUri);
		localStringBuilder.append(",helpIntentUri=");
		localStringBuilder.append(this.helpIntentUri);
		localStringBuilder.append(",sendFeedbackIntentUri=");
		localStringBuilder.append(this.sendFeedbackIntentUri);
		localStringBuilder.append(",reauthenticationIntentUri=");
		localStringBuilder.append(this.reauthenticationIntentUri);
		localStringBuilder.append(",syncStatus=");
		localStringBuilder.append(this.syncStatus);
		localStringBuilder.append(",composeIntentUri=");
		localStringBuilder.append(this.composeIntentUri);
		localStringBuilder.append(",mimeType=");
		localStringBuilder.append(this.mimeType);
		localStringBuilder.append(",recentFoldersUri=");
		localStringBuilder.append(this.recentFolderListUri);
		localStringBuilder.append(",color=");
		localStringBuilder.append(Integer.toHexString(this.color));
		localStringBuilder.append(",defaultRecentFoldersUri=");
		localStringBuilder.append(this.defaultRecentFolderListUri);
		localStringBuilder.append(",settings=");
		localStringBuilder.append(this.settings.serialize());
		return localStringBuilder.toString();
	}

	public void writeToParcel(Parcel paramParcel, int paramInt) {
		super.writeToParcel(paramParcel, paramInt);
		paramParcel.writeInt(this.providerVersion);
		paramParcel.writeParcelable(this.uri, 0);
		paramParcel.writeInt(this.capabilities);
		paramParcel.writeParcelable(this.folderListUri, 0);
		paramParcel.writeParcelable(this.fullFolderListUri, 0);
		paramParcel.writeParcelable(this.searchUri, 0);
		paramParcel.writeString(this.accountFromAddresses);
		paramParcel.writeParcelable(this.saveDraftUri, 0);
		paramParcel.writeParcelable(this.sendMessageUri, 0);
		paramParcel.writeParcelable(this.expungeMessageUri, 0);
		paramParcel.writeParcelable(this.undoUri, 0);
		paramParcel.writeParcelable(this.settingsIntentUri, 0);
		paramParcel.writeParcelable(this.helpIntentUri, 0);
		paramParcel.writeParcelable(this.sendFeedbackIntentUri, 0);
		paramParcel.writeParcelable(this.reauthenticationIntentUri, 0);
		paramParcel.writeInt(this.syncStatus);
		paramParcel.writeParcelable(this.composeIntentUri, 0);
		paramParcel.writeString(this.mimeType);
		paramParcel.writeParcelable(this.recentFolderListUri, 0);
		paramParcel.writeInt(this.color);
		paramParcel.writeParcelable(this.defaultRecentFolderListUri, 0);
		paramParcel.writeParcelable(this.manualSyncUri, 0);
		paramParcel.writeParcelable(this.viewIntentProxyUri, 0);
		paramParcel.writeParcelable(this.accoutCookieQueryUri, 0);
		paramParcel.writeParcelable(this.updateSettingsUri, 0);
		if (this.settings == null)
			LogUtils.e(LOG_TAG,
					"unexpected null settings object in writeToParcel",
					new Object[0]);
		if (this.settings != null)
			;
		for (String str = this.settings.serialize();; str = "") {
			paramParcel.writeString(str);
			return;
		}
	}
}

/*
 * Location:
 * C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name: com.android.mail.providers.Account JD-Core Version: 0.6.2
 */