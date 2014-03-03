package com.android.mail.browse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.mail.utils.LogUtils;

public abstract class ConversationOverlayItem {
	private int mHeight;
	private boolean mNeedsMeasure;
	private int mTop;

	public boolean belongsToMessage(
			MessageCursor.ConversationMessage paramConversationMessage) {
		return false;
	}

	public abstract void bindView(View paramView, boolean paramBoolean);

	public boolean canBecomeSnapHeader() {
		return false;
	}

	public boolean canPushSnapHeader() {
		return false;
	}

	public abstract View createView(Context paramContext,
			LayoutInflater paramLayoutInflater, ViewGroup paramViewGroup);

	public int getGravity() {
		return 80;
	}

	public int getHeight() {
		return this.mHeight;
	}

	public int getTop() {
		return this.mTop;
	}

	public abstract int getType();

	public void invalidateMeasurement() {
		this.mNeedsMeasure = true;
	}

	public abstract boolean isContiguous();

	public boolean isMeasurementValid() {
		return !this.mNeedsMeasure;
	}

	public void markMeasurementValid() {
		this.mNeedsMeasure = false;
	}

	public void onModelUpdated(View paramView) {
	}

	public boolean setHeight(int paramInt) {
		Object[] arrayOfObject = new Object[2];
		arrayOfObject[0] = Integer.valueOf(paramInt);
		arrayOfObject[1] = this;
		LogUtils.i("ConvLayout", "IN setHeight=%dpx of overlay item: %s",
				arrayOfObject);
		if (this.mHeight != paramInt) {
			this.mHeight = paramInt;
			this.mNeedsMeasure = true;
			return true;
		}
		return false;
	}

	public void setMessage(
			MessageCursor.ConversationMessage paramConversationMessage) {
	}

	public void setTop(int paramInt) {
		this.mTop = paramInt;
	}
}

/*
 * Location:
 * C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name: com.android.mail.browse.ConversationOverlayItem JD-Core
 * Version: 0.6.2
 */