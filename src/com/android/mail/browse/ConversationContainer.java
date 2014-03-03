package com.android.mail.browse;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import com.android.mail.providers.Account;
import com.android.mail.utils.DequeMap;
import com.android.mail.utils.InputSmoother;
import com.android.mail.utils.LogUtils;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

public class ConversationContainer extends ViewGroup implements
		ScrollNotifier.ScrollListener {
	private static final int[] BOTTOM_LAYER_VIEW_IDS = { 2131689634 };
	private static final int[] TOP_LAYER_VIEW_IDS = { 2131689635 };
	private ConversationAccountController mAccountController;
	private int mActivePointerId;
	private final DataSetObserver mAdapterObserver = new AdapterObserver();
	private boolean mAttachedOverlaySinceLastDraw;
	private boolean mDisableLayoutTracing;
	private float mLastMotionY;
	private boolean mMissedPointerDown;
	private final List<View> mNonScrollingChildren = Lists.newArrayList();
	private int mOffsetY;
	private ConversationViewAdapter mOverlayAdapter;
	private OverlayPosition[] mOverlayPositions;
	private final SparseArray<OverlayView> mOverlayViews = new SparseArray();
	private float mScale;
	private final DequeMap<Integer, View> mScrapViews = new DequeMap();
	private boolean mSnapEnabled;
	private MessageHeaderView mSnapHeader;
	private int mSnapIndex;
	private View mTopMostOverlay;
	private boolean mTouchInitialized;
	private boolean mTouchIsDown = false;
	private final int mTouchSlop;
	private final InputSmoother mVelocityTracker;
	private ConversationWebView mWebView;
	private int mWidthMeasureSpec;

	public ConversationContainer(Context paramContext) {
		this(paramContext, null);
	}

	public ConversationContainer(Context paramContext, AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		this.mVelocityTracker = new InputSmoother(paramContext);
		this.mTouchSlop = ViewConfiguration.get(paramContext).getScaledTouchSlop();
		setMotionEventSplittingEnabled(false);
	}

	private View addOverlayView(int paramInt) {
		int i = this.mOverlayAdapter.getItemViewType(paramInt);
		View localView1 = (View) this.mScrapViews.poll(Integer.valueOf(i));
		View localView2 = this.mOverlayAdapter.getView(paramInt, localView1,
				this);
		this.mOverlayViews.put(paramInt, new OverlayView(localView2, i));
		int j = BOTTOM_LAYER_VIEW_IDS.length;
		if (localView1 == localView2) {
			Object[] arrayOfObject2 = new Object[2];
			arrayOfObject2[0] = Integer.valueOf(paramInt);
			arrayOfObject2[1] = localView2;
			LogUtils.d("ConvLayout",
					"want to REUSE scrolled-in view: index=%d obj=%s",
					arrayOfObject2);
		}
		while (true) {
			addViewInLayout(localView2, j, localView2.getLayoutParams(), true);
			this.mAttachedOverlaySinceLastDraw = true;
			return localView2;
			Object[] arrayOfObject1 = new Object[2];
			arrayOfObject1[0] = Integer.valueOf(paramInt);
			arrayOfObject1[1] = localView2;
			LogUtils.d("ConvLayout",
					"want to CREATE scrolled-in view: index=%d obj=%s",
					arrayOfObject1);
		}
	}

	private OverlayPosition calculatePosition(
			ConversationOverlayItem paramConversationOverlayItem,
			int paramInt1, int paramInt2, int paramInt3) {
		if (paramConversationOverlayItem.getHeight() == 0) {
			if (paramInt3 == 48)
				;
			for (int j = paramInt1;; j = paramInt2)
				return new OverlayPosition(j, j);
		}
		if (paramInt3 != 0)
			;
		while (true) {
			int i = paramInt3 & 0x70;
			switch (i) {
			default:
				throw new UnsupportedOperationException("unsupported gravity: "
						+ i);
				paramInt3 = paramConversationOverlayItem.getGravity();
			case 80:
			case 48:
			}
		}
		return new OverlayPosition(paramInt2
				- paramConversationOverlayItem.getHeight(), paramInt2);
		return new OverlayPosition(paramInt1, paramInt1
				+ paramConversationOverlayItem.getHeight());
	}

	private void clearOverlays() {
		int i = 0;
		int j = this.mOverlayViews.size();
		while (i < j) {
			detachOverlay((OverlayView) this.mOverlayViews.valueAt(i));
			i++;
		}
		this.mOverlayViews.clear();
	}

	private void detachOverlay(OverlayView paramOverlayView) {
		removeViewInLayout(paramOverlayView.view);
		this.mScrapViews.add(Integer.valueOf(paramOverlayView.itemType),
				paramOverlayView.view);
		if ((paramOverlayView.view instanceof DetachListener))
			((DetachListener) paramOverlayView.view).onDetachedFromParent();
	}

	private ConversationOverlayItem findNextPushingOverlay(int paramInt) {
		int i = paramInt;
		int j = this.mOverlayAdapter.getCount();
		while (i < j) {
			ConversationOverlayItem localConversationOverlayItem = this.mOverlayAdapter
					.getItem(i);
			if (localConversationOverlayItem.canPushSnapHeader())
				return localConversationOverlayItem;
			i++;
		}
		return null;
	}

	private void forwardFakeMotionEvent(MotionEvent paramMotionEvent,
			int paramInt) {
		MotionEvent localMotionEvent = MotionEvent.obtain(paramMotionEvent);
		localMotionEvent.setAction(paramInt);
		this.mWebView.onTouchEvent(localMotionEvent);
		Object[] arrayOfObject = new Object[4];
		arrayOfObject[0] = Integer.valueOf(localMotionEvent.getActionMasked());
		arrayOfObject[1] = Float.valueOf(localMotionEvent.getX());
		arrayOfObject[2] = Float.valueOf(localMotionEvent.getY());
		arrayOfObject[3] = Integer.valueOf(localMotionEvent.getPointerCount());
		LogUtils.v("ConvLayout",
				"in Container.OnTouch. fake: action=%d x/y=%f/%f pointers=%d",
				arrayOfObject);
	}

	private int getOverlayBottom(int paramInt) {
		return webPxToScreenPx(this.mOverlayPositions[paramInt].bottom);
	}

	private int getOverlayTop(int paramInt) {
		return webPxToScreenPx(this.mOverlayPositions[paramInt].top);
	}

	private boolean isSnapEnabled() {
		if ((this.mAccountController == null)
				|| (this.mAccountController.getAccount() == null)
				|| (this.mAccountController.getAccount().settings == null))
			;
		int i;
		do {
			return true;
			i = this.mAccountController.getAccount().settings.snapHeaders;
		} while ((i == 0)
				|| ((i == 1) && (getResources().getConfiguration().orientation == 1)));
		return false;
	}

	private void layoutOverlay(View paramView, int paramInt1, int paramInt2) {
		int i = paramInt1 - this.mOffsetY;
		int j = paramInt2 - this.mOffsetY;
		ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) paramView
				.getLayoutParams();
		int k = getPaddingLeft() + localMarginLayoutParams.leftMargin;
		paramView.layout(k, i, k + paramView.getMeasuredWidth(), j);
	}

	private void measureOverlayView(View paramView) {
		ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) paramView
				.getLayoutParams();
		if (localMarginLayoutParams == null)
			localMarginLayoutParams = (ViewGroup.MarginLayoutParams) generateDefaultLayoutParams();
		int i = ViewGroup.getChildMeasureSpec(this.mWidthMeasureSpec,
				getPaddingLeft() + getPaddingRight()
						+ localMarginLayoutParams.leftMargin
						+ localMarginLayoutParams.rightMargin,
				localMarginLayoutParams.width);
		int j = localMarginLayoutParams.height;
		if (j > 0)
			;
		for (int k = View.MeasureSpec.makeMeasureSpec(j, 1073741824);; k = View.MeasureSpec
				.makeMeasureSpec(0, 0)) {
			paramView.measure(i, k);
			return;
		}
	}

	private void onDataSetChanged() {
		clearOverlays();
		this.mSnapHeader.unbind();
		this.mSnapEnabled = isSnapEnabled();
		positionOverlays(0, this.mOffsetY);
	}

	private void onOverlayScrolledOff(int paramInt1,
			final OverlayView paramOverlayView, int paramInt2, int paramInt3) {
		this.mOverlayViews.remove(paramInt1);
		post(new Runnable() {
			public void run() {
				ConversationContainer.this.detachOverlay(paramOverlayView);
			}
		});
		layoutOverlay(paramOverlayView.view, paramInt2, paramInt3);
	}

	private void positionOverlay(int paramInt1, int paramInt2, int paramInt3)
  {
    OverlayView localOverlayView = (OverlayView)this.mOverlayViews.get(paramInt1);
    ConversationOverlayItem localConversationOverlayItem = this.mOverlayAdapter.getItem(paramInt1);
    localConversationOverlayItem.setTop(paramInt2);
    View localView;
    if ((paramInt2 != paramInt3) && (paramInt3 > this.mOffsetY) && (paramInt2 < this.mOffsetY + getHeight()))
      if (localOverlayView != null)
      {
        localView = localOverlayView.view;
        if (localView != null)
          break label198;
        localView = addOverlayView(paramInt1);
        measureOverlayView(localView);
        localConversationOverlayItem.markMeasurementValid();
        Object[] arrayOfObject6 = new Object[1];
        arrayOfObject6[0] = Integer.valueOf(paramInt1);
        traceLayout("show/measure overlay %d", arrayOfObject6);
        label113: Object[] arrayOfObject5 = new Object[2];
        arrayOfObject5[0] = Integer.valueOf(paramInt1);
        arrayOfObject5[1] = Integer.valueOf(localView.getMeasuredHeight());
        traceLayout("laying out overlay %d with h=%d", arrayOfObject5);
        layoutOverlay(localView, paramInt2, paramInt2 + localView.getMeasuredHeight());
        label162: if ((paramInt2 <= this.mOffsetY) && (localConversationOverlayItem.canPushSnapHeader()))
        {
          if (this.mSnapIndex != -1)
            break label356;
          this.mSnapIndex = paramInt1;
        }
      }
    label198: label356: 
    while (paramInt1 <= this.mSnapIndex)
    {
      return;
      localView = null;
      break;
      Object[] arrayOfObject3 = new Object[1];
      arrayOfObject3[0] = Integer.valueOf(paramInt1);
      traceLayout("move overlay %d", arrayOfObject3);
      if (localConversationOverlayItem.isMeasurementValid())
        break label113;
      measureOverlayView(localView);
      localConversationOverlayItem.markMeasurementValid();
      Object[] arrayOfObject4 = new Object[3];
      arrayOfObject4[0] = Integer.valueOf(paramInt1);
      arrayOfObject4[1] = Integer.valueOf(localView.getHeight());
      arrayOfObject4[2] = Integer.valueOf(localView.getMeasuredHeight());
      traceLayout("and (re)measure overlay %d, old/new heights=%d/%d", arrayOfObject4);
      break label113;
      if (localOverlayView != null)
      {
        Object[] arrayOfObject2 = new Object[1];
        arrayOfObject2[0] = Integer.valueOf(paramInt1);
        traceLayout("hide overlay %d", arrayOfObject2);
        onOverlayScrolledOff(paramInt1, localOverlayView, paramInt2, paramInt3);
        break label162;
      }
      Object[] arrayOfObject1 = new Object[1];
      arrayOfObject1[0] = Integer.valueOf(paramInt1);
      traceLayout("ignore non-visible overlay %d", arrayOfObject1);
      break label162;
    }
    this.mSnapIndex = paramInt1;
  }

	private void positionOverlays(int paramInt1, int paramInt2)
  {
    this.mOffsetY = paramInt2;
    if (this.mTouchInitialized)
      this.mScale = this.mWebView.getScale();
    while (true)
    {
      Object[] arrayOfObject1 = new Object[2];
      arrayOfObject1[0] = Float.valueOf(this.mWebView.getScale());
      arrayOfObject1[1] = Float.valueOf(this.mScale);
      traceLayout("in positionOverlays, raw scale=%f, effective scale=%f", arrayOfObject1);
      if ((this.mOverlayPositions != null) && (this.mOverlayAdapter != null))
        break;
      return;
      if (this.mScale == 0.0F)
        this.mScale = this.mWebView.getInitialScale();
    }
    Object[] arrayOfObject2 = new Object[2];
    arrayOfObject2[0] = Integer.valueOf(this.mOverlayPositions.length);
    arrayOfObject2[1] = Integer.valueOf(this.mOverlayAdapter.getCount());
    traceLayout("IN positionOverlays, spacerCount=%d overlayCount=%d", arrayOfObject2);
    this.mSnapIndex = -1;
    int i = -1 + this.mOverlayAdapter.getCount();
    int j = -1 + this.mOverlayPositions.length;
    if ((j >= 0) && (i >= 0))
    {
      int k = getOverlayTop(j);
      int m = getOverlayBottom(j);
      int n;
      int i1;
      int i2;
      label205: int i3;
      label217: OverlayPosition localOverlayPosition;
      if (j == 0)
      {
        n = 1;
        i1 = i;
        i2 = 48;
        if (n == 0)
          break label383;
        i3 = i1 - i;
        ConversationOverlayItem localConversationOverlayItem1 = this.mOverlayAdapter.getItem(i3);
        localOverlayPosition = calculatePosition(localConversationOverlayItem1, k, m, i2);
        Object[] arrayOfObject3 = new Object[5];
        arrayOfObject3[0] = Integer.valueOf(j);
        arrayOfObject3[1] = Integer.valueOf(i3);
        arrayOfObject3[2] = Integer.valueOf(localOverlayPosition.top);
        arrayOfObject3[3] = Integer.valueOf(localOverlayPosition.bottom);
        arrayOfObject3[4] = localConversationOverlayItem1;
        traceLayout("in loop, spacer=%d overlay=%d t/b=%d/%d (%s)", arrayOfObject3);
        positionOverlay(i3, localOverlayPosition.top, localOverlayPosition.bottom);
        i--;
        if (i >= 0)
          if (n == 0)
            break label390;
      }
      ConversationOverlayItem localConversationOverlayItem2;
      label390: for (int i4 = i1 - i; ; i4 = i)
      {
        localConversationOverlayItem2 = this.mOverlayAdapter.getItem(i4);
        if ((j <= 0) || (localConversationOverlayItem2.isContiguous()))
          break label397;
        j--;
        break;
        n = 0;
        i1 = 0;
        i2 = 0;
        break label205;
        label383: i3 = i;
        break label217;
      }
      label397: int i5;
      if (n != 0)
      {
        i5 = localOverlayPosition.bottom;
        label409: if (n == 0)
          break label521;
      }
      label521: for (int i6 = m; ; i6 = localOverlayPosition.top)
      {
        localOverlayPosition = calculatePosition(localConversationOverlayItem2, i5, i6, i2);
        Object[] arrayOfObject4 = new Object[5];
        arrayOfObject4[0] = Integer.valueOf(j);
        arrayOfObject4[1] = Integer.valueOf(i4);
        arrayOfObject4[2] = Integer.valueOf(localOverlayPosition.top);
        arrayOfObject4[3] = Integer.valueOf(localOverlayPosition.bottom);
        arrayOfObject4[4] = localConversationOverlayItem2;
        traceLayout("in contig loop, spacer=%d overlay=%d t/b=%d/%d (%s)", arrayOfObject4);
        positionOverlay(i4, localOverlayPosition.top, localOverlayPosition.bottom);
        break;
        i5 = k;
        break label409;
      }
    }
    positionSnapHeader(this.mSnapIndex);
  }

	private void positionSnapHeader(int paramInt) {
		boolean bool1 = this.mSnapEnabled;
		Object localObject = null;
		if (bool1) {
			localObject = null;
			if (paramInt != -1) {
				ConversationOverlayItem localConversationOverlayItem2 = this.mOverlayAdapter
						.getItem(paramInt);
				boolean bool2 = localConversationOverlayItem2
						.canBecomeSnapHeader();
				localObject = null;
				if (bool2)
					localObject = localConversationOverlayItem2;
			}
		}
		if (localObject == null) {
			this.mSnapHeader.setVisibility(8);
			this.mSnapHeader.unbind();
			return;
		}
		localObject.bindView(this.mSnapHeader, false);
		this.mSnapHeader.setVisibility(0);
		ConversationOverlayItem localConversationOverlayItem1 = findNextPushingOverlay(paramInt + 1);
		int i = 0;
		if (localConversationOverlayItem1 != null) {
			i = Math.min(0, localConversationOverlayItem1.getTop()
					- this.mSnapHeader.getHeight() - this.mOffsetY);
			if (i < 0) {
				Float localFloat = this.mVelocityTracker.getSmoothedVelocity();
				if ((localFloat != null) && (localFloat.floatValue() > 600.0F))
					i = 0;
			}
		}
		this.mSnapHeader.setTranslationY(i);
	}

	private void traceLayout(String paramString, Object[] paramArrayOfObject) {
		if (this.mDisableLayoutTracing)
			return;
		LogUtils.d("ConvLayout", paramString, paramArrayOfObject);
	}

	private int webPxToScreenPx(int paramInt) {
		return (int) (paramInt * this.mScale);
	}

	public void addScrapView(int paramInt, View paramView) {
		this.mScrapViews.add(Integer.valueOf(paramInt), paramView);
	}

	protected boolean checkLayoutParams(ViewGroup.LayoutParams paramLayoutParams) {
		return paramLayoutParams instanceof ViewGroup.MarginLayoutParams;
	}

	protected void dispatchDraw(Canvas paramCanvas) {
		super.dispatchDraw(paramCanvas);
		if (this.mAttachedOverlaySinceLastDraw) {
			drawChild(paramCanvas, this.mTopMostOverlay, getDrawingTime());
			this.mAttachedOverlaySinceLastDraw = false;
		}
	}

	protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
		return new ViewGroup.MarginLayoutParams(-1, -2);
	}

	public ViewGroup.LayoutParams generateLayoutParams(
			AttributeSet paramAttributeSet) {
		return new ViewGroup.MarginLayoutParams(getContext(), paramAttributeSet);
	}

	protected ViewGroup.LayoutParams generateLayoutParams(
			ViewGroup.LayoutParams paramLayoutParams) {
		return new ViewGroup.MarginLayoutParams(paramLayoutParams);
	}

	public View getScrapView(int paramInt) {
		return (View) this.mScrapViews.peek(Integer.valueOf(paramInt));
	}

	public MessageHeaderView getSnapHeader() {
		return this.mSnapHeader;
	}

	public void invalidateSpacerGeometry() {
		this.mOverlayPositions = null;
	}

	public int measureOverlay(View paramView) {
		measureOverlayView(paramView);
		return paramView.getMeasuredHeight();
	}

	protected void onFinishInflate() {
		super.onFinishInflate();
		this.mWebView = ((ConversationWebView) findViewById(2131689634));
		this.mWebView.addScrollListener(this);
		this.mTopMostOverlay = findViewById(2131689635);
		this.mSnapHeader = ((MessageHeaderView) findViewById(2131689636));
		this.mSnapHeader.setSnappy(true);
		for (int i1 : BOTTOM_LAYER_VIEW_IDS)
			this.mNonScrollingChildren.add(findViewById(i1));
		for (int n : TOP_LAYER_VIEW_IDS)
			this.mNonScrollingChildren.add(findViewById(n));
	}

	public void onGeometryChange(OverlayPosition[] paramArrayOfOverlayPosition) {
		traceLayout("*** got overlay spacer positions:", new Object[0]);
		int i = paramArrayOfOverlayPosition.length;
		for (int j = 0; j < i; j++) {
			OverlayPosition localOverlayPosition = paramArrayOfOverlayPosition[j];
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = Integer.valueOf(localOverlayPosition.top);
			arrayOfObject[1] = Integer.valueOf(localOverlayPosition.bottom);
			traceLayout("top=%d bottom=%d", arrayOfObject);
		}
		this.mOverlayPositions = paramArrayOfOverlayPosition;
		positionOverlays(0, this.mOffsetY);
	}

	public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
		if (!this.mTouchInitialized)
			this.mTouchInitialized = true;
		if (this.mWebView.isHandlingTouch())
			;
		float f;
		do {
			return false;
			switch (paramMotionEvent.getActionMasked()) {
			case 1:
			case 3:
			case 4:
			default:
				return false;
			case 0:
				this.mLastMotionY = paramMotionEvent.getY();
				this.mActivePointerId = paramMotionEvent.getPointerId(0);
				return false;
			case 5:
				LogUtils.d("ConvLayout",
						"Container is intercepting non-primary touch!",
						new Object[0]);
				this.mMissedPointerDown = true;
				requestDisallowInterceptTouchEvent(true);
				return true;
			case 2:
			}
			f = paramMotionEvent.getY(paramMotionEvent
					.findPointerIndex(this.mActivePointerId));
		} while ((int) Math.abs(f - this.mLastMotionY) <= this.mTouchSlop);
		this.mLastMotionY = f;
		return true;
	}

	protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2,
			int paramInt3, int paramInt4) {
		LogUtils.d("ConvLayout", "*** IN header container onLayout",
				new Object[0]);
		Iterator localIterator = this.mNonScrollingChildren.iterator();
		while (localIterator.hasNext()) {
			View localView = (View) localIterator.next();
			if (localView.getVisibility() != 8) {
				int k = localView.getMeasuredWidth();
				int m = localView.getMeasuredHeight();
				ViewGroup.MarginLayoutParams localMarginLayoutParams = (ViewGroup.MarginLayoutParams) localView
						.getLayoutParams();
				int n = localMarginLayoutParams.leftMargin;
				int i1 = localMarginLayoutParams.topMargin;
				localView.layout(n, i1, n + k, i1 + m);
			}
		}
		if (this.mOverlayAdapter != null) {
			int i = 0;
			int j = this.mOverlayAdapter.getCount();
			while (i < j) {
				this.mOverlayAdapter.getItem(i).invalidateMeasurement();
				i++;
			}
		}
		positionOverlays(0, this.mOffsetY);
	}

	protected void onMeasure(int paramInt1, int paramInt2) {
		super.onMeasure(paramInt1, paramInt2);
		if (LogUtils.isLoggable("ConvLayout", 3)) {
			Object[] arrayOfObject = new Object[2];
			arrayOfObject[0] = View.MeasureSpec.toString(paramInt1);
			arrayOfObject[1] = View.MeasureSpec.toString(paramInt2);
			LogUtils.d("ConvLayout",
					"*** IN header container onMeasure spec for w/h=%s/%s",
					arrayOfObject);
		}
		Iterator localIterator = this.mNonScrollingChildren.iterator();
		while (localIterator.hasNext()) {
			View localView = (View) localIterator.next();
			if (localView.getVisibility() != 8)
				measureChildWithMargins(localView, paramInt1, 0, paramInt2, 0);
		}
		this.mWidthMeasureSpec = paramInt1;
	}

	public void onNotifierScroll(int paramInt1, int paramInt2) {
		this.mVelocityTracker.onInput(paramInt2);
		this.mDisableLayoutTracing = true;
		positionOverlays(paramInt1, paramInt2);
		this.mDisableLayoutTracing = false;
	}

	public void onOverlayModelUpdate(List<Integer> paramList) {
		Iterator localIterator = paramList.iterator();
		while (localIterator.hasNext()) {
			Integer localInteger = (Integer) localIterator.next();
			ConversationOverlayItem localConversationOverlayItem = this.mOverlayAdapter
					.getItem(localInteger.intValue());
			OverlayView localOverlayView = (OverlayView) this.mOverlayViews
					.get(localInteger.intValue());
			if ((localOverlayView != null) && (localOverlayView.view != null)
					&& (localConversationOverlayItem != null))
				localConversationOverlayItem
						.onModelUpdated(localOverlayView.view);
			if ((localInteger.intValue() == this.mSnapIndex)
					&& (this.mSnapHeader
							.isBoundTo(localConversationOverlayItem)))
				this.mSnapHeader.refresh();
		}
	}

	public boolean onTouchEvent(MotionEvent paramMotionEvent) {
		int i = paramMotionEvent.getActionMasked();
		if ((i == 1) || (i == 3))
			;
		for (this.mTouchIsDown = false;; this.mTouchIsDown = true) {
			do
				return this.mWebView.onTouchEvent(paramMotionEvent);
			while ((this.mTouchIsDown) || ((i != 2) && (i != 5)));
			forwardFakeMotionEvent(paramMotionEvent, 0);
			if (this.mMissedPointerDown) {
				forwardFakeMotionEvent(paramMotionEvent, 5);
				this.mMissedPointerDown = false;
			}
		}
	}

	public void setAccountController(
			ConversationAccountController paramConversationAccountController) {
		this.mAccountController = paramConversationAccountController;
		this.mSnapEnabled = isSnapEnabled();
	}

	public void setOverlayAdapter(
			ConversationViewAdapter paramConversationViewAdapter) {
		if (this.mOverlayAdapter != null) {
			this.mOverlayAdapter
					.unregisterDataSetObserver(this.mAdapterObserver);
			clearOverlays();
		}
		this.mOverlayAdapter = paramConversationViewAdapter;
		if (this.mOverlayAdapter != null)
			this.mOverlayAdapter.registerDataSetObserver(this.mAdapterObserver);
	}

	private class AdapterObserver extends DataSetObserver {
		private AdapterObserver() {
		}

		public void onChanged() {
			ConversationContainer.this.onDataSetChanged();
		}
	}

	public static abstract interface DetachListener {
		public abstract void onDetachedFromParent();
	}

	public static class OverlayPosition {
		public final int bottom;
		public final int top;

		public OverlayPosition(int paramInt1, int paramInt2) {
			this.top = paramInt1;
			this.bottom = paramInt2;
		}
	}

	private static class OverlayView {
		int itemType;
		public View view;

		public OverlayView(View paramView, int paramInt) {
			this.view = paramView;
			this.itemType = paramInt;
		}
	}
}

/*
 * Location:
 * C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name: com.android.mail.browse.ConversationContainer JD-Core
 * Version: 0.6.2
 */