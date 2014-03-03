package com.android.ex.chips;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipData.Item;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Matrix.ScaleToFit;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.text.TextWatcher;
import android.text.method.QwertyKeyListener;
import android.text.style.ImageSpan;
import android.text.util.Rfc822Token;
import android.text.util.Rfc822Tokenizer;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Patterns;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnClickListener;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.MultiAutoCompleteTextView.Tokenizer;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RecipientEditTextView extends MultiAutoCompleteTextView
  implements DialogInterface.OnDismissListener, ActionMode.Callback, GestureDetector.OnGestureListener, View.OnClickListener, AdapterView.OnItemClickListener, TextView.OnEditorActionListener, RecipientAlternatesAdapter.OnCheckedItemChangedListener
{
  private static int DISMISS = "dismiss".hashCode();
  private static int sSelectedTextColor = -1;
  private final Runnable mAddTextWatcher = new Runnable()
  {
    public void run()
    {
      if (RecipientEditTextView.this.mTextWatcher == null)
      {
        RecipientEditTextView.access$002(RecipientEditTextView.this, new RecipientEditTextView.RecipientTextWatcher(RecipientEditTextView.this, null));
        RecipientEditTextView.this.addTextChangedListener(RecipientEditTextView.this.mTextWatcher);
      }
    }
  };
  private ListPopupWindow mAddressPopup;
  private int mAlternatesLayout;
  private AdapterView.OnItemClickListener mAlternatesListener;
  private ListPopupWindow mAlternatesPopup;
  private int mCheckedItem;
  private Drawable mChipBackground = null;
  private Drawable mChipBackgroundPressed;
  private Drawable mChipDelete = null;
  private float mChipFontSize;
  private float mChipHeight;
  private int mChipPadding;
  private String mCopyAddress;
  private Dialog mCopyDialog;
  private Bitmap mDefaultContactPhoto;
  private Runnable mDelayedShrink = new Runnable()
  {
    public void run()
    {
      RecipientEditTextView.this.shrink();
    }
  };
  private boolean mDragEnabled = false;
  private GestureDetector mGestureDetector;
  private Runnable mHandlePendingChips = new Runnable()
  {
    public void run()
    {
      RecipientEditTextView.this.handlePendingChips();
    }
  };
  private Handler mHandler;
  private IndividualReplacementTask mIndividualReplacements;
  private Drawable mInvalidChipBackground;
  private float mLineSpacingExtra;
  private int mMaxLines;
  private ImageSpan mMoreChip;
  private TextView mMoreItem;
  private boolean mNoChips = false;
  private final ArrayList<String> mPendingChips = new ArrayList();
  private int mPendingChipsCount = 0;
  private ArrayList<RecipientChip> mRemovedSpans;
  private ScrollView mScrollView;
  private RecipientChip mSelectedChip;
  private boolean mShouldShrink = true;
  private ArrayList<RecipientChip> mTemporaryRecipients;
  private TextWatcher mTextWatcher;
  private MultiAutoCompleteTextView.Tokenizer mTokenizer;
  private boolean mTriedGettingScrollView;
  private AutoCompleteTextView.Validator mValidator;

  public RecipientEditTextView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setChipDimensions(paramContext, paramAttributeSet);
    if (sSelectedTextColor == -1)
      sSelectedTextColor = paramContext.getResources().getColor(17170443);
    this.mAlternatesPopup = new ListPopupWindow(paramContext);
    this.mAddressPopup = new ListPopupWindow(paramContext);
    this.mCopyDialog = new Dialog(paramContext);
    this.mAlternatesListener = new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        RecipientEditTextView.this.mAlternatesPopup.setOnItemClickListener(null);
        RecipientEditTextView.this.replaceChip(RecipientEditTextView.this.mSelectedChip, ((RecipientAlternatesAdapter)paramAnonymousAdapterView.getAdapter()).getRecipientEntry(paramAnonymousInt));
        Message localMessage = Message.obtain(RecipientEditTextView.this.mHandler, RecipientEditTextView.DISMISS);
        localMessage.obj = RecipientEditTextView.this.mAlternatesPopup;
        RecipientEditTextView.this.mHandler.sendMessageDelayed(localMessage, 300L);
        RecipientEditTextView.this.clearComposingText();
      }
    };
    setInputType(0x80000 | getInputType());
    setOnItemClickListener(this);
    setCustomSelectionActionModeCallback(this);
    this.mHandler = new Handler()
    {
      public void handleMessage(Message paramAnonymousMessage)
      {
        if (paramAnonymousMessage.what == RecipientEditTextView.DISMISS)
        {
          ((ListPopupWindow)paramAnonymousMessage.obj).dismiss();
          return;
        }
        super.handleMessage(paramAnonymousMessage);
      }
    };
    this.mTextWatcher = new RecipientTextWatcher(null);
    addTextChangedListener(this.mTextWatcher);
    this.mGestureDetector = new GestureDetector(paramContext, this);
    setOnEditorActionListener(this);
    this.mMaxLines = getLineCount();
  }

  private boolean alreadyHasChip(int paramInt1, int paramInt2)
  {
    if (this.mNoChips)
      return true;
    RecipientChip[] arrayOfRecipientChip = (RecipientChip[])getSpannable().getSpans(paramInt1, paramInt2, RecipientChip.class);
    return (arrayOfRecipientChip != null) && (arrayOfRecipientChip.length != 0);
  }

  private float calculateAvailableWidth(boolean paramBoolean)
  {
    return getWidth() - getPaddingLeft() - getPaddingRight() - 2 * this.mChipPadding;
  }

  private int calculateOffsetFromBottom(int paramInt)
  {
    return -((getLineCount() - (paramInt + 1)) * (int)this.mChipHeight + getPaddingBottom() + getPaddingTop()) + getDropDownVerticalOffset();
  }

  private void checkChipWidths()
  {
    RecipientChip[] arrayOfRecipientChip = getSortedRecipients();
    if (arrayOfRecipientChip != null)
    {
      int i = arrayOfRecipientChip.length;
      for (int j = 0; j < i; j++)
      {
        RecipientChip localRecipientChip = arrayOfRecipientChip[j];
        Rect localRect = localRecipientChip.getDrawable().getBounds();
        if ((getWidth() > 0) && (localRect.right - localRect.left > getWidth()))
          replaceChip(localRecipientChip, localRecipientChip.getEntry());
      }
    }
  }

  private boolean chipsPending()
  {
    return (this.mPendingChipsCount > 0) || ((this.mRemovedSpans != null) && (this.mRemovedSpans.size() > 0));
  }

  private void clearSelectedChip()
  {
    if (this.mSelectedChip != null)
    {
      unselectChip(this.mSelectedChip);
      this.mSelectedChip = null;
    }
    setCursorVisible(true);
  }

  private void commitByCharacter()
  {
    if (this.mTokenizer == null)
      return;
    Editable localEditable = getText();
    int i = getSelectionEnd();
    int j = this.mTokenizer.findTokenStart(localEditable, i);
    if (shouldCreateChip(j, i))
      commitChip(j, i, localEditable);
    setSelection(getText().length());
  }

  private boolean commitChip(int paramInt1, int paramInt2, Editable paramEditable)
  {
    ListAdapter localListAdapter = getAdapter();
    if ((localListAdapter != null) && (localListAdapter.getCount() > 0) && (enoughToFilter()) && (paramInt2 == getSelectionEnd()) && (!isPhoneQuery()))
    {
      submitItemAtPosition(0);
      dismissDropDown();
      return true;
    }
    int i = this.mTokenizer.findTokenEnd(paramEditable, paramInt1);
    if (paramEditable.length() > i + 1)
    {
      int j = paramEditable.charAt(i + 1);
      if ((j == 44) || (j == 59))
        i++;
    }
    String str = paramEditable.toString().substring(paramInt1, i).trim();
    clearComposingText();
    if ((str != null) && (str.length() > 0) && (!str.equals(" ")))
    {
      RecipientEntry localRecipientEntry = createTokenizedEntry(str);
      if (localRecipientEntry != null)
      {
        QwertyKeyListener.markAsReplaced(paramEditable, paramInt1, paramInt2, "");
        CharSequence localCharSequence = createChip(localRecipientEntry, false);
        if ((localCharSequence != null) && (paramInt1 > -1) && (paramInt2 > -1))
          paramEditable.replace(paramInt1, paramInt2, localCharSequence);
      }
      if (paramInt2 == getSelectionEnd())
        dismissDropDown();
      sanitizeBetween();
      return true;
    }
    return false;
  }

  private boolean commitDefault()
  {
    if (this.mTokenizer == null);
    Editable localEditable;
    int i;
    int j;
    do
    {
      return false;
      localEditable = getText();
      i = getSelectionEnd();
      j = this.mTokenizer.findTokenStart(localEditable, i);
    }
    while (!shouldCreateChip(j, i));
    int k = movePastTerminators(this.mTokenizer.findTokenEnd(getText(), j));
    if (k != getSelectionEnd())
    {
      handleEdit(j, k);
      return true;
    }
    return commitChip(j, i, localEditable);
  }

  private RecipientChip constructChipSpan(RecipientEntry paramRecipientEntry, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
    throws NullPointerException
  {
    if (this.mChipBackground == null)
      throw new NullPointerException("Unable to render any chips as setChipDimensions was not called.");
    Layout localLayout = getLayout();
    TextPaint localTextPaint = getPaint();
    float f = localTextPaint.getTextSize();
    int i = localTextPaint.getColor();
    if (paramBoolean1);
    for (Bitmap localBitmap = createSelectedChip(paramRecipientEntry, localTextPaint, localLayout); ; localBitmap = createUnselectedChip(paramRecipientEntry, localTextPaint, localLayout, paramBoolean2))
    {
      BitmapDrawable localBitmapDrawable = new BitmapDrawable(getResources(), localBitmap);
      localBitmapDrawable.setBounds(0, 0, localBitmap.getWidth(), localBitmap.getHeight());
      RecipientChip localRecipientChip = new RecipientChip(localBitmapDrawable, paramRecipientEntry, paramInt);
      localTextPaint.setTextSize(f);
      localTextPaint.setColor(i);
      return localRecipientChip;
    }
  }

  private ListAdapter createAlternatesAdapter(RecipientChip paramRecipientChip)
  {
    return new RecipientAlternatesAdapter(getContext(), paramRecipientChip.getContactId(), paramRecipientChip.getDataId(), this.mAlternatesLayout, ((BaseRecipientAdapter)getAdapter()).getQueryType(), this);
  }

  private CharSequence createChip(RecipientEntry paramRecipientEntry, boolean paramBoolean)
  {
    String str = createAddressText(paramRecipientEntry);
    Object localObject;
    if (TextUtils.isEmpty(str))
      localObject = null;
    int j;
    int k;
    do
    {
      return localObject;
      int i = getSelectionEnd();
      j = this.mTokenizer.findTokenStart(getText(), i);
      k = -1 + str.length();
      localObject = new SpannableString(str);
    }
    while (this.mNoChips);
    try
    {
      RecipientChip localRecipientChip = constructChipSpan(paramRecipientEntry, j, paramBoolean, false);
      ((SpannableString)localObject).setSpan(localRecipientChip, 0, k, 33);
      localRecipientChip.setOriginalText(((SpannableString)localObject).toString());
      return localObject;
    }
    catch (NullPointerException localNullPointerException)
    {
      Log.e("RecipientEditTextView", localNullPointerException.getMessage(), localNullPointerException);
    }
    return null;
  }

  private MoreImageSpan createMoreSpan(int paramInt)
  {
    String str1 = this.mMoreItem.getText().toString();
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = Integer.valueOf(paramInt);
    String str2 = String.format(str1, arrayOfObject);
    TextPaint localTextPaint = new TextPaint(getPaint());
    localTextPaint.setTextSize(this.mMoreItem.getTextSize());
    localTextPaint.setColor(this.mMoreItem.getCurrentTextColor());
    int i = (int)localTextPaint.measureText(str2) + this.mMoreItem.getPaddingLeft() + this.mMoreItem.getPaddingRight();
    int j = getLineHeight();
    Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    int k = j;
    Layout localLayout = getLayout();
    if (localLayout != null)
      k -= localLayout.getLineDescent(0);
    localCanvas.drawText(str2, 0, str2.length(), 0.0F, k, localTextPaint);
    BitmapDrawable localBitmapDrawable = new BitmapDrawable(getResources(), localBitmap);
    localBitmapDrawable.setBounds(0, 0, i, j);
    return new MoreImageSpan(localBitmapDrawable);
  }

  private void createReplacementChip(int paramInt1, int paramInt2, Editable paramEditable)
  {
    if (alreadyHasChip(paramInt1, paramInt2));
    while (true)
    {
      return;
      String str = paramEditable.toString().substring(paramInt1, paramInt2);
      if (str.trim().lastIndexOf(',') == -1 + str.length())
        str = str.substring(0, -1 + str.length());
      RecipientEntry localRecipientEntry = createTokenizedEntry(str);
      if (localRecipientEntry == null)
        continue;
      SpannableString localSpannableString = new SpannableString(createAddressText(localRecipientEntry));
      int i = getSelectionEnd();
      int j;
      if (this.mTokenizer != null)
        j = this.mTokenizer.findTokenStart(getText(), i);
      try
      {
        label114: boolean bool1 = this.mNoChips;
        localObject = null;
        if (!bool1)
        {
          if (TextUtils.isEmpty(localRecipientEntry.getDisplayName()))
            break label265;
          if (!TextUtils.equals(localRecipientEntry.getDisplayName(), localRecipientEntry.getDestination()))
            break label237;
          break label265;
        }
        while (true)
        {
          RecipientChip localRecipientChip = constructChipSpan(localRecipientEntry, j, false, bool2);
          localObject = localRecipientChip;
          paramEditable.setSpan(localObject, paramInt1, paramInt2, 33);
          if (localObject == null)
            break;
          if (this.mTemporaryRecipients == null)
            this.mTemporaryRecipients = new ArrayList();
          localObject.setOriginalText(localSpannableString.toString());
          this.mTemporaryRecipients.add(localObject);
          return;
          j = 0;
          break label114;
          label237: bool2 = false;
        }
      }
      catch (NullPointerException localNullPointerException)
      {
        while (true)
        {
          Log.e("RecipientEditTextView", localNullPointerException.getMessage(), localNullPointerException);
          Object localObject = null;
          continue;
          label265: boolean bool2 = true;
        }
      }
    }
  }

  private Bitmap createSelectedChip(RecipientEntry paramRecipientEntry, TextPaint paramTextPaint, Layout paramLayout)
  {
    int i = (int)this.mChipHeight;
    float[] arrayOfFloat = new float[1];
    paramTextPaint.getTextWidths(" ", arrayOfFloat);
    CharSequence localCharSequence = ellipsizeText(createChipDisplayText(paramRecipientEntry), paramTextPaint, calculateAvailableWidth(true) - i - arrayOfFloat[0]);
    int j = Math.max(i * 2, i + ((int)Math.floor(paramTextPaint.measureText(localCharSequence, 0, localCharSequence.length())) + 2 * this.mChipPadding));
    Bitmap localBitmap = Bitmap.createBitmap(j, i, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    if (this.mChipBackgroundPressed != null)
    {
      this.mChipBackgroundPressed.setBounds(0, 0, j, i);
      this.mChipBackgroundPressed.draw(localCanvas);
      paramTextPaint.setColor(sSelectedTextColor);
      localCanvas.drawText(localCharSequence, 0, localCharSequence.length(), this.mChipPadding, getTextYOffset((String)localCharSequence, paramTextPaint, i), paramTextPaint);
      Rect localRect = new Rect();
      this.mChipBackgroundPressed.getPadding(localRect);
      this.mChipDelete.setBounds(j - i + localRect.left, 0 + localRect.top, j - localRect.right, i - localRect.bottom);
      this.mChipDelete.draw(localCanvas);
      return localBitmap;
    }
    Log.w("RecipientEditTextView", "Unable to draw a background for the chips as it was never set");
    return localBitmap;
  }

  private ListAdapter createSingleAddressAdapter(RecipientChip paramRecipientChip)
  {
    return new SingleRecipientArrayAdapter(getContext(), this.mAlternatesLayout, paramRecipientChip.getEntry());
  }

  private RecipientEntry createTokenizedEntry(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return null;
    if ((isPhoneQuery()) && (isPhoneNumber(paramString)))
      return RecipientEntry.constructFakeEntry(paramString);
    Rfc822Token[] arrayOfRfc822Token1 = Rfc822Tokenizer.tokenize(paramString);
    if ((isValid(paramString)) && (arrayOfRfc822Token1 != null) && (arrayOfRfc822Token1.length > 0))
    {
      String str2 = arrayOfRfc822Token1[0].getName();
      if (!TextUtils.isEmpty(str2))
      {
        if (!isPhoneQuery())
        {
          if (!TextUtils.isEmpty(paramString))
            paramString = paramString.trim();
          int i = paramString.charAt(-1 + paramString.length());
          if ((i == 44) || (i == 59))
            paramString = paramString.substring(0, -1 + paramString.length());
        }
        return RecipientEntry.constructGeneratedEntry(str2, paramString);
      }
      String str3 = arrayOfRfc822Token1[0].getAddress();
      if (!TextUtils.isEmpty(str3))
        return RecipientEntry.constructFakeEntry(str3);
    }
    AutoCompleteTextView.Validator localValidator = this.mValidator;
    String str1 = null;
    if (localValidator != null)
    {
      boolean bool = this.mValidator.isValid(paramString);
      str1 = null;
      if (!bool)
      {
        str1 = this.mValidator.fixText(paramString).toString();
        if (!TextUtils.isEmpty(str1))
        {
          if (!str1.contains(paramString))
            break label252;
          Rfc822Token[] arrayOfRfc822Token2 = Rfc822Tokenizer.tokenize(str1);
          if (arrayOfRfc822Token2.length > 0)
            str1 = arrayOfRfc822Token2[0].getAddress();
        }
      }
    }
    if (!TextUtils.isEmpty(str1));
    while (true)
    {
      return RecipientEntry.constructFakeEntry(str1);
      label252: str1 = null;
      break;
      str1 = paramString;
    }
  }

  private Bitmap createUnselectedChip(RecipientEntry paramRecipientEntry, TextPaint paramTextPaint, Layout paramLayout, boolean paramBoolean)
  {
    int i = (int)this.mChipHeight;
    float[] arrayOfFloat = new float[1];
    paramTextPaint.getTextWidths(" ", arrayOfFloat);
    CharSequence localCharSequence = ellipsizeText(createChipDisplayText(paramRecipientEntry), paramTextPaint, calculateAvailableWidth(false) - i - arrayOfFloat[0]);
    int j = Math.max(i * 2, i + ((int)Math.floor(paramTextPaint.measureText(localCharSequence, 0, localCharSequence.length())) + 2 * this.mChipPadding));
    Bitmap localBitmap1 = Bitmap.createBitmap(j, i, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap1);
    Drawable localDrawable = getChipBackground(paramRecipientEntry);
    if (localDrawable != null)
    {
      localDrawable.setBounds(0, 0, j, i);
      localDrawable.draw(localCanvas);
      long l = paramRecipientEntry.getContactId();
      int k;
      Bitmap localBitmap2;
      if (isPhoneQuery())
        if (l != -1L)
        {
          k = 1;
          if (k == 0)
            break label462;
          byte[] arrayOfByte = paramRecipientEntry.getPhotoBytes();
          if ((arrayOfByte == null) && (paramRecipientEntry.getPhotoThumbnailUri() != null))
          {
            ((BaseRecipientAdapter)getAdapter()).fetchPhoto(paramRecipientEntry, paramRecipientEntry.getPhotoThumbnailUri());
            arrayOfByte = paramRecipientEntry.getPhotoBytes();
          }
          if (arrayOfByte == null)
            break label453;
          int m = arrayOfByte.length;
          localBitmap2 = BitmapFactory.decodeByteArray(arrayOfByte, 0, m);
          label228: if (localBitmap2 != null)
          {
            RectF localRectF1 = new RectF(0.0F, 0.0F, localBitmap2.getWidth(), localBitmap2.getHeight());
            Rect localRect = new Rect();
            this.mChipBackground.getPadding(localRect);
            RectF localRectF2 = new RectF(j - i + localRect.left, 0 + localRect.top, j - localRect.right, i - localRect.bottom);
            Matrix localMatrix = new Matrix();
            localMatrix.setRectToRect(localRectF1, localRectF2, Matrix.ScaleToFit.FILL);
            localCanvas.drawBitmap(localBitmap2, localMatrix, paramTextPaint);
          }
        }
      while (true)
      {
        paramTextPaint.setColor(getContext().getResources().getColor(17170444));
        localCanvas.drawText(localCharSequence, 0, localCharSequence.length(), this.mChipPadding, getTextYOffset((String)localCharSequence, paramTextPaint, i), paramTextPaint);
        return localBitmap1;
        k = 0;
        break;
        if ((l != -1L) && (l != -2L) && (!TextUtils.isEmpty(paramRecipientEntry.getDisplayName())))
        {
          k = 1;
          break;
        }
        k = 0;
        break;
        label453: localBitmap2 = this.mDefaultContactPhoto;
        break label228;
        label462: if ((paramBoolean) && (!isPhoneQuery()));
      }
    }
    Log.w("RecipientEditTextView", "Unable to draw a background for the chips as it was never set");
    return localBitmap1;
  }

  private RecipientEntry createValidatedEntry(RecipientEntry paramRecipientEntry)
  {
    if (paramRecipientEntry == null)
      return null;
    String str = paramRecipientEntry.getDestination();
    if ((!isPhoneQuery()) && (paramRecipientEntry.getContactId() == -2L))
      return RecipientEntry.constructGeneratedEntry(paramRecipientEntry.getDisplayName(), str);
    if ((RecipientEntry.isCreatedRecipient(paramRecipientEntry.getContactId())) && ((TextUtils.isEmpty(paramRecipientEntry.getDisplayName())) || (TextUtils.equals(paramRecipientEntry.getDisplayName(), str)) || ((this.mValidator != null) && (!this.mValidator.isValid(str)))))
      return RecipientEntry.constructFakeEntry(str);
    return paramRecipientEntry;
  }

  private CharSequence ellipsizeText(CharSequence paramCharSequence, TextPaint paramTextPaint, float paramFloat)
  {
    paramTextPaint.setTextSize(this.mChipFontSize);
    if ((paramFloat <= 0.0F) && (Log.isLoggable("RecipientEditTextView", 3)))
      Log.d("RecipientEditTextView", "Max width is negative: " + paramFloat);
    return TextUtils.ellipsize(paramCharSequence, paramTextPaint, paramFloat, TextUtils.TruncateAt.END);
  }

  private void expand()
  {
    if (this.mShouldShrink)
      setMaxLines(2147483647);
    removeMoreChip();
    setCursorVisible(true);
    Editable localEditable = getText();
    if ((localEditable != null) && (localEditable.length() > 0));
    for (int i = localEditable.length(); ; i = 0)
    {
      setSelection(i);
      if ((this.mTemporaryRecipients != null) && (this.mTemporaryRecipients.size() > 0))
      {
        new RecipientReplacementTask(null).execute(new Void[0]);
        this.mTemporaryRecipients = null;
      }
      return;
    }
  }

  private RecipientChip findChip(int paramInt)
  {
    int i = 0;
    RecipientChip[] arrayOfRecipientChip = (RecipientChip[])getSpannable().getSpans(0, getText().length(), RecipientChip.class);
    while (i < arrayOfRecipientChip.length)
    {
      RecipientChip localRecipientChip = arrayOfRecipientChip[i];
      int j = getChipStart(localRecipientChip);
      int k = getChipEnd(localRecipientChip);
      if ((paramInt >= j) && (paramInt <= k))
        return localRecipientChip;
      i++;
    }
    return null;
  }

  private int findText(Editable paramEditable, int paramInt)
  {
    if (paramEditable.charAt(paramInt) != ' ')
      return paramInt;
    return -1;
  }

  private boolean focusNext()
  {
    View localView = focusSearch(130);
    if (localView != null)
    {
      localView.requestFocus();
      return true;
    }
    return false;
  }

  private int getChipEnd(RecipientChip paramRecipientChip)
  {
    return getSpannable().getSpanEnd(paramRecipientChip);
  }

  private int getChipStart(RecipientChip paramRecipientChip)
  {
    return getSpannable().getSpanStart(paramRecipientChip);
  }

  private float getTextYOffset(String paramString, TextPaint paramTextPaint, int paramInt)
  {
    Rect localRect = new Rect();
    paramTextPaint.getTextBounds(paramString, 0, paramString.length(), localRect);
    return paramInt - (paramInt - (localRect.bottom - localRect.top)) / 2 - (int)paramTextPaint.descent();
  }

  private void handleEdit(int paramInt1, int paramInt2)
  {
    if ((paramInt1 == -1) || (paramInt2 == -1))
    {
      dismissDropDown();
      return;
    }
    Editable localEditable = getText();
    setSelection(paramInt2);
    String str = getText().toString().substring(paramInt1, paramInt2);
    if (!TextUtils.isEmpty(str))
    {
      RecipientEntry localRecipientEntry = RecipientEntry.constructFakeEntry(str);
      QwertyKeyListener.markAsReplaced(localEditable, paramInt1, paramInt2, "");
      CharSequence localCharSequence = createChip(localRecipientEntry, false);
      int i = getSelectionEnd();
      if ((localCharSequence != null) && (paramInt1 > -1) && (i > -1))
        localEditable.replace(paramInt1, i, localCharSequence);
    }
    dismissDropDown();
  }

  private void handlePasteAndReplace()
  {
    ArrayList localArrayList = handlePaste();
    if ((localArrayList != null) && (localArrayList.size() > 0))
      new IndividualReplacementTask(null).execute(new Object[] { localArrayList });
  }

  private void handlePasteClip(ClipData paramClipData)
  {
    removeTextChangedListener(this.mTextWatcher);
    if ((paramClipData != null) && (paramClipData.getDescription().hasMimeType("text/plain")))
    {
      int i = 0;
      if (i < paramClipData.getItemCount())
      {
        CharSequence localCharSequence = paramClipData.getItemAt(i).getText();
        int k;
        Editable localEditable;
        if (localCharSequence != null)
        {
          int j = getSelectionStart();
          k = getSelectionEnd();
          localEditable = getText();
          if ((j < 0) || (k < 0) || (j == k))
            break label109;
          localEditable.append(localCharSequence, j, k);
        }
        while (true)
        {
          handlePasteAndReplace();
          i++;
          break;
          label109: localEditable.insert(k, localCharSequence);
        }
      }
    }
    this.mHandler.post(this.mAddTextWatcher);
  }

  private boolean isInDelete(RecipientChip paramRecipientChip, int paramInt, float paramFloat1, float paramFloat2)
  {
    return (paramRecipientChip.isSelected()) && (paramInt == getChipEnd(paramRecipientChip));
  }

  private static boolean isPhoneNumber(String paramString)
  {
    if (TextUtils.isEmpty(paramString))
      return false;
    return Patterns.PHONE.matcher(paramString).matches();
  }

  private boolean isValid(String paramString)
  {
    if (this.mValidator == null)
      return true;
    return this.mValidator.isValid(paramString);
  }

  private void postHandlePendingChips()
  {
    this.mHandler.removeCallbacks(this.mHandlePendingChips);
    this.mHandler.post(this.mHandlePendingChips);
  }

  private int putOffsetInRange(int paramInt)
  {
    int i = paramInt;
    Editable localEditable1 = getText();
    int j = localEditable1.length();
    int k = j;
    for (int m = j - 1; (m >= 0) && (localEditable1.charAt(m) == ' '); m--)
      k--;
    if (i >= k)
      return i;
    Editable localEditable2 = getText();
    while ((i >= 0) && (findText(localEditable2, i) == -1) && (findChip(i) == null))
      i--;
    return i;
  }

  private void scrollBottomIntoView()
  {
    if (this.mScrollView != null)
      this.mScrollView.scrollBy(0, (int)(getLineCount() * this.mChipHeight));
  }

  private void scrollLineIntoView(int paramInt)
  {
    if (this.mScrollView != null)
      this.mScrollView.scrollBy(0, calculateOffsetFromBottom(paramInt));
  }

  private RecipientChip selectChip(RecipientChip paramRecipientChip)
  {
    if (shouldShowEditableText(paramRecipientChip))
    {
      CharSequence localCharSequence = paramRecipientChip.getValue();
      Editable localEditable3 = getText();
      getSpannable().removeSpan(paramRecipientChip);
      setCursorVisible(true);
      setSelection(localEditable3.length());
      return new RecipientChip(null, RecipientEntry.constructFakeEntry((String)localCharSequence), -1);
    }
    if (paramRecipientChip.getContactId() == -2L)
    {
      int k = getChipStart(paramRecipientChip);
      int m = getChipEnd(paramRecipientChip);
      getSpannable().removeSpan(paramRecipientChip);
      while (true)
      {
        RecipientChip localRecipientChip2;
        Editable localEditable2;
        try
        {
          if (this.mNoChips)
            return null;
          localRecipientChip2 = constructChipSpan(paramRecipientChip.getEntry(), k, true, false);
          localEditable2 = getText();
          QwertyKeyListener.markAsReplaced(localEditable2, k, m, "");
          if ((k == -1) || (m == -1))
          {
            Log.d("RecipientEditTextView", "The chip being selected no longer exists but should.");
            localRecipientChip2.setSelected(true);
            if (shouldShowEditableText(localRecipientChip2))
              scrollLineIntoView(getLayout().getLineForOffset(getChipStart(localRecipientChip2)));
            showAddress(localRecipientChip2, this.mAddressPopup, getWidth(), getContext());
            setCursorVisible(false);
            return localRecipientChip2;
          }
        }
        catch (NullPointerException localNullPointerException2)
        {
          Log.e("RecipientEditTextView", localNullPointerException2.getMessage(), localNullPointerException2);
          return null;
        }
        localEditable2.setSpan(localRecipientChip2, k, m, 33);
      }
    }
    int i = getChipStart(paramRecipientChip);
    int j = getChipEnd(paramRecipientChip);
    getSpannable().removeSpan(paramRecipientChip);
    while (true)
    {
      RecipientChip localRecipientChip1;
      Editable localEditable1;
      try
      {
        localRecipientChip1 = constructChipSpan(paramRecipientChip.getEntry(), i, true, false);
        localEditable1 = getText();
        QwertyKeyListener.markAsReplaced(localEditable1, i, j, "");
        if ((i == -1) || (j == -1))
        {
          Log.d("RecipientEditTextView", "The chip being selected no longer exists but should.");
          localRecipientChip1.setSelected(true);
          if (shouldShowEditableText(localRecipientChip1))
            scrollLineIntoView(getLayout().getLineForOffset(getChipStart(localRecipientChip1)));
          showAlternates(localRecipientChip1, this.mAlternatesPopup, getWidth(), getContext());
          setCursorVisible(false);
          return localRecipientChip1;
        }
      }
      catch (NullPointerException localNullPointerException1)
      {
        Log.e("RecipientEditTextView", localNullPointerException1.getMessage(), localNullPointerException1);
        return null;
      }
      localEditable1.setSpan(localRecipientChip1, i, j, 33);
    }
  }

  private void setChipDimensions(Context paramContext, AttributeSet paramAttributeSet)
  {
    TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RecipientEditTextView, 0, 0);
    Resources localResources = getContext().getResources();
    this.mChipBackground = localTypedArray.getDrawable(1);
    if (this.mChipBackground == null)
      this.mChipBackground = localResources.getDrawable(R.drawable.chip_background);
    this.mChipBackgroundPressed = localTypedArray.getDrawable(2);
    if (this.mChipBackgroundPressed == null)
      this.mChipBackgroundPressed = localResources.getDrawable(R.drawable.chip_background_selected);
    this.mChipDelete = localTypedArray.getDrawable(3);
    if (this.mChipDelete == null)
      this.mChipDelete = localResources.getDrawable(R.drawable.chip_delete);
    this.mChipPadding = localTypedArray.getDimensionPixelSize(5, -1);
    if (this.mChipPadding == -1)
      this.mChipPadding = ((int)localResources.getDimension(R.dimen.chip_padding));
    this.mAlternatesLayout = localTypedArray.getResourceId(4, -1);
    if (this.mAlternatesLayout == -1)
      this.mAlternatesLayout = R.layout.chips_alternate_item;
    this.mDefaultContactPhoto = BitmapFactory.decodeResource(localResources, R.drawable.ic_contact_picture);
    this.mMoreItem = ((TextView)LayoutInflater.from(getContext()).inflate(R.layout.more_item, null));
    this.mChipHeight = localTypedArray.getDimensionPixelSize(6, -1);
    if (this.mChipHeight == -1.0F)
      this.mChipHeight = localResources.getDimension(R.dimen.chip_height);
    this.mChipFontSize = localTypedArray.getDimensionPixelSize(7, -1);
    if (this.mChipFontSize == -1.0F)
      this.mChipFontSize = localResources.getDimension(R.dimen.chip_text_size);
    this.mInvalidChipBackground = localTypedArray.getDrawable(0);
    if (this.mInvalidChipBackground == null)
      this.mInvalidChipBackground = localResources.getDrawable(R.drawable.chip_background_invalid);
    this.mLineSpacingExtra = paramContext.getResources().getDimension(R.dimen.line_spacing_extra);
    localTypedArray.recycle();
  }

  private boolean shouldCreateChip(int paramInt1, int paramInt2)
  {
    return (!this.mNoChips) && (hasFocus()) && (enoughToFilter()) && (!alreadyHasChip(paramInt1, paramInt2));
  }

  private boolean shouldShowEditableText(RecipientChip paramRecipientChip)
  {
    long l = paramRecipientChip.getContactId();
    return (l == -1L) || ((!isPhoneQuery()) && (l == -2L));
  }

  private void showAddress(final RecipientChip paramRecipientChip, final ListPopupWindow paramListPopupWindow, int paramInt, Context paramContext)
  {
    int i = calculateOffsetFromBottom(getLayout().getLineForOffset(getChipStart(paramRecipientChip)));
    paramListPopupWindow.setWidth(paramInt);
    paramListPopupWindow.setAnchorView(this);
    paramListPopupWindow.setVerticalOffset(i);
    paramListPopupWindow.setAdapter(createSingleAddressAdapter(paramRecipientChip));
    paramListPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener()
    {
      public void onItemClick(AdapterView<?> paramAnonymousAdapterView, View paramAnonymousView, int paramAnonymousInt, long paramAnonymousLong)
      {
        RecipientEditTextView.this.unselectChip(paramRecipientChip);
        paramListPopupWindow.dismiss();
      }
    });
    paramListPopupWindow.show();
    ListView localListView = paramListPopupWindow.getListView();
    localListView.setChoiceMode(1);
    localListView.setItemChecked(0, true);
  }

  private void showAlternates(RecipientChip paramRecipientChip, ListPopupWindow paramListPopupWindow, int paramInt, Context paramContext)
  {
    int i = getLayout().getLineForOffset(getChipStart(paramRecipientChip));
    if (i == -1 + getLineCount());
    for (int j = 0; ; j = -(int)((this.mChipHeight + 2.0F * this.mLineSpacingExtra) * Math.abs(-1 + getLineCount() - i)))
    {
      paramListPopupWindow.setWidth(paramInt);
      paramListPopupWindow.setAnchorView(this);
      paramListPopupWindow.setVerticalOffset(j);
      paramListPopupWindow.setAdapter(createAlternatesAdapter(paramRecipientChip));
      paramListPopupWindow.setOnItemClickListener(this.mAlternatesListener);
      this.mCheckedItem = -1;
      paramListPopupWindow.show();
      ListView localListView = paramListPopupWindow.getListView();
      localListView.setChoiceMode(1);
      if (this.mCheckedItem != -1)
      {
        localListView.setItemChecked(this.mCheckedItem, true);
        this.mCheckedItem = -1;
      }
      return;
    }
  }

  private void showCopyDialog(String paramString)
  {
    this.mCopyAddress = paramString;
    this.mCopyDialog.setTitle(paramString);
    this.mCopyDialog.setContentView(R.layout.copy_chip_dialog_layout);
    this.mCopyDialog.setCancelable(true);
    this.mCopyDialog.setCanceledOnTouchOutside(true);
    Button localButton = (Button)this.mCopyDialog.findViewById(16908313);
    localButton.setOnClickListener(this);
    if (isPhoneQuery());
    for (int i = R.string.copy_number; ; i = R.string.copy_email)
    {
      localButton.setText(getContext().getResources().getString(i));
      this.mCopyDialog.setOnDismissListener(this);
      this.mCopyDialog.show();
      return;
    }
  }

  private void shrink()
  {
    if (this.mTokenizer == null)
      return;
    if (this.mSelectedChip != null);
    for (long l = this.mSelectedChip.getEntry().getContactId(); (this.mSelectedChip != null) && (l != -1L) && (!isPhoneQuery()) && (l != -2L); l = -1L)
    {
      clearSelectedChip();
      createMoreChip();
      return;
    }
    if (getWidth() <= 0)
    {
      this.mHandler.removeCallbacks(this.mDelayedShrink);
      this.mHandler.post(this.mDelayedShrink);
      return;
    }
    if (this.mPendingChipsCount > 0)
      postHandlePendingChips();
    while (true)
    {
      this.mHandler.post(this.mAddTextWatcher);
      break;
      Editable localEditable1 = getText();
      int i = getSelectionEnd();
      int j = this.mTokenizer.findTokenStart(localEditable1, i);
      RecipientChip[] arrayOfRecipientChip = (RecipientChip[])getSpannable().getSpans(j, i, RecipientChip.class);
      if ((arrayOfRecipientChip == null) || (arrayOfRecipientChip.length == 0))
      {
        Editable localEditable2 = getText();
        int k = this.mTokenizer.findTokenEnd(localEditable2, j);
        if ((k < localEditable2.length()) && (localEditable2.charAt(k) == ','))
          k = movePastTerminators(k);
        if (k != getSelectionEnd())
          handleEdit(j, k);
        else
          commitChip(j, i, localEditable1);
      }
    }
  }

  private void startDrag(RecipientChip paramRecipientChip)
  {
    String str = paramRecipientChip.getEntry().getDestination();
    startDrag(ClipData.newPlainText(str, str + ','), new RecipientChipShadow(paramRecipientChip), null, 0);
    removeChip(paramRecipientChip);
  }

  private void submitItemAtPosition(int paramInt)
  {
    RecipientEntry localRecipientEntry = createValidatedEntry((RecipientEntry)getAdapter().getItem(paramInt));
    if (localRecipientEntry == null)
      return;
    clearComposingText();
    int i = getSelectionEnd();
    int j = this.mTokenizer.findTokenStart(getText(), i);
    Editable localEditable = getText();
    QwertyKeyListener.markAsReplaced(localEditable, j, i, "");
    CharSequence localCharSequence = createChip(localRecipientEntry, false);
    if ((localCharSequence != null) && (j >= 0) && (i >= 0))
      localEditable.replace(j, i, localCharSequence);
    sanitizeBetween();
  }

  private String tokenizeAddress(String paramString)
  {
    Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(paramString);
    if ((arrayOfRfc822Token != null) && (arrayOfRfc822Token.length > 0))
      paramString = arrayOfRfc822Token[0].getAddress();
    return paramString;
  }

  private void unselectChip(RecipientChip paramRecipientChip)
  {
    int i = getChipStart(paramRecipientChip);
    int j = getChipEnd(paramRecipientChip);
    Editable localEditable = getText();
    this.mSelectedChip = null;
    if ((i == -1) || (j == -1))
    {
      Log.w("RecipientEditTextView", "The chip doesn't exist or may be a chip a user was editing");
      setSelection(localEditable.length());
      commitDefault();
    }
    while (true)
    {
      setCursorVisible(true);
      setSelection(localEditable.length());
      if ((this.mAlternatesPopup != null) && (this.mAlternatesPopup.isShowing()))
        this.mAlternatesPopup.dismiss();
      return;
      getSpannable().removeSpan(paramRecipientChip);
      QwertyKeyListener.markAsReplaced(localEditable, i, j, "");
      localEditable.removeSpan(paramRecipientChip);
      try
      {
        if (!this.mNoChips)
          localEditable.setSpan(constructChipSpan(paramRecipientChip.getEntry(), i, false, false), i, j, 33);
      }
      catch (NullPointerException localNullPointerException)
      {
        Log.e("RecipientEditTextView", localNullPointerException.getMessage(), localNullPointerException);
      }
    }
  }

  public void append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
  {
    if (this.mTextWatcher != null)
      removeTextChangedListener(this.mTextWatcher);
    super.append(paramCharSequence, paramInt1, paramInt2);
    if ((!TextUtils.isEmpty(paramCharSequence)) && (TextUtils.getTrimmedLength(paramCharSequence) > 0))
    {
      String str1 = paramCharSequence.toString();
      int i = str1.lastIndexOf(',');
      if (i > -1)
      {
        String str2 = str1.substring(i);
        int j = str2.indexOf('"');
        if (j > i)
          str2.lastIndexOf(',', j);
      }
      if ((!TextUtils.isEmpty(str1)) && (TextUtils.getTrimmedLength(str1) > 0))
      {
        this.mPendingChipsCount = (1 + this.mPendingChipsCount);
        this.mPendingChips.add(paramCharSequence.toString());
      }
    }
    if (this.mPendingChipsCount > 0)
      postHandlePendingChips();
    this.mHandler.post(this.mAddTextWatcher);
  }

  int countTokens(Editable paramEditable)
  {
    int i = 0;
    int j = 0;
    do
    {
      if (j >= paramEditable.length())
        break;
      j = movePastTerminators(this.mTokenizer.findTokenEnd(paramEditable, j));
      i++;
    }
    while (j < paramEditable.length());
    return i;
  }

  String createAddressText(RecipientEntry paramRecipientEntry)
  {
    String str1 = paramRecipientEntry.getDisplayName();
    String str2 = paramRecipientEntry.getDestination();
    if ((TextUtils.isEmpty(str1)) || (TextUtils.equals(str1, str2)))
      str1 = null;
    if ((isPhoneQuery()) && (isPhoneNumber(str2)));
    for (String str3 = str2.trim(); ; str3 = new Rfc822Token(str1, str2, null).toString().trim())
    {
      int i = str3.indexOf(",");
      if ((this.mTokenizer != null) && (!TextUtils.isEmpty(str3)) && (i < -1 + str3.length()))
        str3 = (String)this.mTokenizer.terminateToken(str3);
      return str3;
      if (str2 != null)
      {
        Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(str2);
        if ((arrayOfRfc822Token != null) && (arrayOfRfc822Token.length > 0))
          str2 = arrayOfRfc822Token[0].getAddress();
      }
    }
  }

  String createChipDisplayText(RecipientEntry paramRecipientEntry)
  {
    String str1 = paramRecipientEntry.getDisplayName();
    String str2 = paramRecipientEntry.getDestination();
    if ((TextUtils.isEmpty(str1)) || (TextUtils.equals(str1, str2)))
      str1 = null;
    if ((str2 != null) && ((!isPhoneQuery()) || (!isPhoneNumber(str2))))
    {
      Rfc822Token[] arrayOfRfc822Token = Rfc822Tokenizer.tokenize(str2);
      if ((arrayOfRfc822Token != null) && (arrayOfRfc822Token.length > 0))
        str2 = arrayOfRfc822Token[0].getAddress();
    }
    if (!TextUtils.isEmpty(str1))
      return str1;
    if (!TextUtils.isEmpty(str2))
      return str2;
    return new Rfc822Token(str1, str2, null).toString();
  }

  void createMoreChip()
  {
    if (this.mNoChips)
      createMoreChipPlainText();
    do
    {
      do
        return;
      while (!this.mShouldShrink);
      ImageSpan[] arrayOfImageSpan = (ImageSpan[])getSpannable().getSpans(0, getText().length(), MoreImageSpan.class);
      if (arrayOfImageSpan.length > 0)
        getSpannable().removeSpan(arrayOfImageSpan[0]);
      RecipientChip[] arrayOfRecipientChip = getSortedRecipients();
      if ((arrayOfRecipientChip == null) || (arrayOfRecipientChip.length <= 2))
      {
        this.mMoreChip = null;
        return;
      }
      Spannable localSpannable = getSpannable();
      int i = arrayOfRecipientChip.length;
      int j = i - 2;
      MoreImageSpan localMoreImageSpan = createMoreSpan(j);
      this.mRemovedSpans = new ArrayList();
      Editable localEditable = getText();
      int k = i - j;
      int m = 0;
      int n = 0;
      while (k < arrayOfRecipientChip.length)
      {
        this.mRemovedSpans.add(arrayOfRecipientChip[k]);
        if (k == i - j)
          n = localSpannable.getSpanStart(arrayOfRecipientChip[k]);
        if (k == -1 + arrayOfRecipientChip.length)
          m = localSpannable.getSpanEnd(arrayOfRecipientChip[k]);
        if ((this.mTemporaryRecipients == null) || (!this.mTemporaryRecipients.contains(arrayOfRecipientChip[k])))
        {
          int i3 = localSpannable.getSpanStart(arrayOfRecipientChip[k]);
          int i4 = localSpannable.getSpanEnd(arrayOfRecipientChip[k]);
          arrayOfRecipientChip[k].setOriginalText(localEditable.toString().substring(i3, i4));
        }
        localSpannable.removeSpan(arrayOfRecipientChip[k]);
        k++;
      }
      if (m < localEditable.length())
        m = localEditable.length();
      int i1 = Math.max(n, m);
      int i2 = Math.min(n, m);
      SpannableString localSpannableString = new SpannableString(localEditable.subSequence(i2, i1));
      localSpannableString.setSpan(localMoreImageSpan, 0, localSpannableString.length(), 33);
      localEditable.replace(i2, i1, localSpannableString);
      this.mMoreChip = localMoreImageSpan;
    }
    while ((isPhoneQuery()) || (getLineCount() <= this.mMaxLines));
    setMaxLines(getLineCount());
  }

  void createMoreChipPlainText()
  {
    Editable localEditable = getText();
    int i = 0;
    int j = 0;
    for (int k = 0; k < 2; k++)
    {
      j = movePastTerminators(this.mTokenizer.findTokenEnd(localEditable, i));
      i = j;
    }
    MoreImageSpan localMoreImageSpan = createMoreSpan(-2 + countTokens(localEditable));
    SpannableString localSpannableString = new SpannableString(localEditable.subSequence(j, localEditable.length()));
    localSpannableString.setSpan(localMoreImageSpan, 0, localSpannableString.length(), 33);
    localEditable.replace(j, localEditable.length(), localSpannableString);
    this.mMoreChip = localMoreImageSpan;
  }

  Drawable getChipBackground(RecipientEntry paramRecipientEntry)
  {
    if ((this.mValidator != null) && (this.mValidator.isValid(paramRecipientEntry.getDestination())))
      return this.mChipBackground;
    return this.mInvalidChipBackground;
  }

  RecipientChip getLastChip()
  {
    RecipientChip[] arrayOfRecipientChip = getSortedRecipients();
    RecipientChip localRecipientChip = null;
    if (arrayOfRecipientChip != null)
    {
      int i = arrayOfRecipientChip.length;
      localRecipientChip = null;
      if (i > 0)
        localRecipientChip = arrayOfRecipientChip[(-1 + arrayOfRecipientChip.length)];
    }
    return localRecipientChip;
  }

  ImageSpan getMoreChip()
  {
    MoreImageSpan[] arrayOfMoreImageSpan = (MoreImageSpan[])getSpannable().getSpans(0, getText().length(), MoreImageSpan.class);
    if ((arrayOfMoreImageSpan != null) && (arrayOfMoreImageSpan.length > 0))
      return arrayOfMoreImageSpan[0];
    return null;
  }

  RecipientChip[] getSortedRecipients()
  {
    ArrayList localArrayList = new ArrayList(Arrays.asList((RecipientChip[])getSpannable().getSpans(0, getText().length(), RecipientChip.class)));
    Collections.sort(localArrayList, new Comparator()
    {
      public int compare(RecipientChip paramAnonymousRecipientChip1, RecipientChip paramAnonymousRecipientChip2)
      {
        int i = this.val$spannable.getSpanStart(paramAnonymousRecipientChip1);
        int j = this.val$spannable.getSpanStart(paramAnonymousRecipientChip2);
        if (i < j)
          return -1;
        if (i > j)
          return 1;
        return 0;
      }
    });
    return (RecipientChip[])localArrayList.toArray(new RecipientChip[localArrayList.size()]);
  }

  Spannable getSpannable()
  {
    return getText();
  }

  int getViewWidth()
  {
    return getWidth();
  }

  ArrayList<RecipientChip> handlePaste()
  {
    String str1 = getText().toString();
    int i = this.mTokenizer.findTokenStart(str1, getSelectionEnd());
    String str2 = str1.substring(i);
    int j = i;
    int k = j;
    ArrayList localArrayList = new ArrayList();
    RecipientChip localRecipientChip1 = null;
    if (j != 0)
    {
      while ((j != 0) && (localRecipientChip1 == null))
      {
        k = j;
        j = this.mTokenizer.findTokenStart(str1, j);
        localRecipientChip1 = findChip(j);
      }
      if (j != i)
        if (localRecipientChip1 != null)
          j = k;
    }
    while (true)
    {
      RecipientChip localRecipientChip2;
      if (j < i)
      {
        commitChip(j, movePastTerminators(this.mTokenizer.findTokenEnd(getText().toString(), j)), getText());
        localRecipientChip2 = findChip(j);
        if (localRecipientChip2 != null);
      }
      else
      {
        if (isCompletedToken(str2))
        {
          Editable localEditable = getText();
          int m = localEditable.toString().indexOf(str2, i);
          commitChip(m, localEditable.length(), localEditable);
          localArrayList.add(findChip(m));
        }
        return localArrayList;
      }
      j = 1 + getSpannable().getSpanEnd(localRecipientChip2);
      localArrayList.add(localRecipientChip2);
    }
  }

  void handlePendingChips()
  {
    if (getViewWidth() <= 0);
    while (this.mPendingChipsCount <= 0)
      return;
    while (true)
    {
      synchronized (this.mPendingChips)
      {
        Editable localEditable = getText();
        if (this.mPendingChipsCount <= 50)
        {
          int i = 0;
          if (i < this.mPendingChips.size())
          {
            String str = (String)this.mPendingChips.get(i);
            int j = localEditable.toString().indexOf(str);
            int k = j + str.length();
            if (j >= 0)
            {
              if ((k < -2 + localEditable.length()) && (localEditable.charAt(k) == ','))
                k++;
              createReplacementChip(j, k, localEditable);
            }
            this.mPendingChipsCount = (-1 + this.mPendingChipsCount);
            i++;
            continue;
          }
          sanitizeEnd();
          if ((this.mTemporaryRecipients == null) || (this.mTemporaryRecipients.size() <= 0) || (this.mTemporaryRecipients.size() > 50))
            break label352;
          if ((!hasFocus()) && (this.mTemporaryRecipients.size() >= 2))
            break label247;
          new RecipientReplacementTask(null).execute(new Void[0]);
          this.mTemporaryRecipients = null;
          this.mPendingChipsCount = 0;
          this.mPendingChips.clear();
          return;
        }
      }
      this.mNoChips = true;
      continue;
      label247: this.mIndividualReplacements = new IndividualReplacementTask(null);
      IndividualReplacementTask localIndividualReplacementTask = this.mIndividualReplacements;
      Object[] arrayOfObject = new Object[1];
      arrayOfObject[0] = new ArrayList(this.mTemporaryRecipients.subList(0, 2));
      localIndividualReplacementTask.execute(arrayOfObject);
      if (this.mTemporaryRecipients.size() > 2);
      for (this.mTemporaryRecipients = new ArrayList(this.mTemporaryRecipients.subList(2, this.mTemporaryRecipients.size())); ; this.mTemporaryRecipients = null)
      {
        createMoreChip();
        break;
      }
      label352: this.mTemporaryRecipients = null;
      createMoreChip();
    }
  }

  boolean isCompletedToken(CharSequence paramCharSequence)
  {
    if (TextUtils.isEmpty(paramCharSequence));
    int k;
    do
    {
      String str;
      do
      {
        return false;
        int i = paramCharSequence.length();
        int j = this.mTokenizer.findTokenStart(paramCharSequence, i);
        str = paramCharSequence.toString().substring(j, i).trim();
      }
      while (TextUtils.isEmpty(str));
      k = str.charAt(-1 + str.length());
    }
    while ((k != 44) && (k != 59));
    return true;
  }

  protected boolean isPhoneQuery()
  {
    return (getAdapter() != null) && (((BaseRecipientAdapter)getAdapter()).getQueryType() == 1);
  }

  int movePastTerminators(int paramInt)
  {
    if (paramInt >= length())
      return paramInt;
    int i = getText().toString().charAt(paramInt);
    if ((i == 44) || (i == 59))
      paramInt++;
    if ((paramInt < length()) && (getText().toString().charAt(paramInt) == ' '))
      paramInt++;
    return paramInt;
  }

  public boolean onActionItemClicked(ActionMode paramActionMode, MenuItem paramMenuItem)
  {
    return false;
  }

  public void onCheckedItemChanged(int paramInt)
  {
    ListView localListView = this.mAlternatesPopup.getListView();
    if ((localListView != null) && (localListView.getCheckedItemCount() == 0))
      localListView.setItemChecked(paramInt, true);
    this.mCheckedItem = paramInt;
  }

  public void onClick(View paramView)
  {
    ((ClipboardManager)getContext().getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("", this.mCopyAddress));
    this.mCopyDialog.dismiss();
  }

  public void onClick(RecipientChip paramRecipientChip, int paramInt, float paramFloat1, float paramFloat2)
  {
    if (paramRecipientChip.isSelected())
    {
      if (isInDelete(paramRecipientChip, paramInt, paramFloat1, paramFloat2))
        removeChip(paramRecipientChip);
    }
    else
      return;
    clearSelectedChip();
  }

  public boolean onCreateActionMode(ActionMode paramActionMode, Menu paramMenu)
  {
    return false;
  }

  public InputConnection onCreateInputConnection(EditorInfo paramEditorInfo)
  {
    InputConnection localInputConnection = super.onCreateInputConnection(paramEditorInfo);
    int i = 0xFF & paramEditorInfo.imeOptions;
    if ((i & 0x6) != 0)
    {
      paramEditorInfo.imeOptions = (i ^ paramEditorInfo.imeOptions);
      paramEditorInfo.imeOptions = (0x6 | paramEditorInfo.imeOptions);
    }
    if ((0x40000000 & paramEditorInfo.imeOptions) != 0)
      paramEditorInfo.imeOptions = (0xBFFFFFFF & paramEditorInfo.imeOptions);
    paramEditorInfo.actionLabel = getContext().getString(R.string.done);
    return localInputConnection;
  }

  public void onDestroyActionMode(ActionMode paramActionMode)
  {
  }

  public void onDismiss(DialogInterface paramDialogInterface)
  {
    this.mCopyAddress = null;
  }

  public boolean onDown(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public boolean onDragEvent(DragEvent paramDragEvent)
  {
    switch (paramDragEvent.getAction())
    {
    case 2:
    case 4:
    default:
      return false;
    case 1:
      return paramDragEvent.getClipDescription().hasMimeType("text/plain");
    case 5:
      requestFocus();
      return true;
    case 3:
    }
    handlePasteClip(paramDragEvent.getClipData());
    return true;
  }

  public boolean onEditorAction(TextView paramTextView, int paramInt, KeyEvent paramKeyEvent)
  {
    if (paramInt == 6)
    {
      if (commitDefault());
      do
      {
        return true;
        if (this.mSelectedChip != null)
        {
          clearSelectedChip();
          return true;
        }
      }
      while (focusNext());
    }
    return false;
  }

  public boolean onFling(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    return false;
  }

  public void onFocusChanged(boolean paramBoolean, int paramInt, Rect paramRect)
  {
    super.onFocusChanged(paramBoolean, paramInt, paramRect);
    if (!paramBoolean)
    {
      shrink();
      return;
    }
    expand();
  }

  public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong)
  {
    submitItemAtPosition(paramInt);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((this.mSelectedChip != null) && (paramInt == 67))
    {
      if ((this.mAlternatesPopup != null) && (this.mAlternatesPopup.isShowing()))
        this.mAlternatesPopup.dismiss();
      removeChip(this.mSelectedChip);
    }
    if ((paramInt == 66) && (paramKeyEvent.hasNoModifiers()))
      return true;
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  public boolean onKeyPreIme(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) && (this.mSelectedChip != null))
    {
      clearSelectedChip();
      return true;
    }
    return super.onKeyPreIme(paramInt, paramKeyEvent);
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    boolean bool = true;
    switch (paramInt)
    {
    default:
    case 23:
    case 66:
    case 61:
    }
    while (true)
    {
      bool = super.onKeyUp(paramInt, paramKeyEvent);
      do
      {
        return bool;
        if (!paramKeyEvent.hasNoModifiers())
          break;
      }
      while (commitDefault());
      if (this.mSelectedChip != null)
      {
        clearSelectedChip();
        return bool;
      }
      if (focusNext())
      {
        return bool;
        if (paramKeyEvent.hasNoModifiers())
        {
          if (this.mSelectedChip != null)
            clearSelectedChip();
          while (focusNext())
          {
            return bool;
            commitDefault();
          }
        }
      }
    }
  }

  public void onLongPress(MotionEvent paramMotionEvent)
  {
    if (this.mSelectedChip != null);
    RecipientChip localRecipientChip;
    do
    {
      return;
      localRecipientChip = findChip(putOffsetInRange(getOffsetForPosition(paramMotionEvent.getX(), paramMotionEvent.getY())));
    }
    while (localRecipientChip == null);
    if (this.mDragEnabled)
    {
      startDrag(localRecipientChip);
      return;
    }
    showCopyDialog(localRecipientChip.getEntry().getDestination());
  }

  public boolean onPrepareActionMode(ActionMode paramActionMode, Menu paramMenu)
  {
    return false;
  }

  public void onRestoreInstanceState(Parcelable paramParcelable)
  {
    if (!TextUtils.isEmpty(getText()))
    {
      super.onRestoreInstanceState(null);
      return;
    }
    super.onRestoreInstanceState(paramParcelable);
  }

  public Parcelable onSaveInstanceState()
  {
    clearSelectedChip();
    return super.onSaveInstanceState();
  }

  public boolean onScroll(MotionEvent paramMotionEvent1, MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2)
  {
    return false;
  }

  public void onSelectionChanged(int paramInt1, int paramInt2)
  {
    RecipientChip localRecipientChip = getLastChip();
    if ((localRecipientChip != null) && (paramInt1 < getSpannable().getSpanEnd(localRecipientChip)))
      setSelection(Math.min(1 + getSpannable().getSpanEnd(localRecipientChip), getText().length()));
    super.onSelectionChanged(paramInt1, paramInt2);
  }

  public void onShowPress(MotionEvent paramMotionEvent)
  {
  }

  public boolean onSingleTapUp(MotionEvent paramMotionEvent)
  {
    return false;
  }

  public void onSizeChanged(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    super.onSizeChanged(paramInt1, paramInt2, paramInt3, paramInt4);
    if ((paramInt1 != 0) && (paramInt2 != 0))
    {
      if (this.mPendingChipsCount <= 0)
        break label73;
      postHandlePendingChips();
    }
    while ((this.mScrollView == null) && (!this.mTriedGettingScrollView))
    {
      ViewParent localViewParent = getParent();
      while (true)
        if ((localViewParent != null) && (!(localViewParent instanceof ScrollView)))
        {
          localViewParent = localViewParent.getParent();
          continue;
          label73: checkChipWidths();
          break;
        }
      if (localViewParent != null)
        this.mScrollView = ((ScrollView)localViewParent);
      this.mTriedGettingScrollView = true;
    }
  }

  public boolean onTextContextMenuItem(int paramInt)
  {
    if (paramInt == 16908322)
    {
      handlePasteClip(((ClipboardManager)getContext().getSystemService("clipboard")).getPrimaryClip());
      return true;
    }
    return super.onTextContextMenuItem(paramInt);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    boolean bool1;
    if (!isFocused())
      bool1 = super.onTouchEvent(paramMotionEvent);
    label159: label214: label250: 
    while (true)
    {
      return bool1;
      bool1 = super.onTouchEvent(paramMotionEvent);
      int i = paramMotionEvent.getAction();
      if (this.mSelectedChip == null)
        this.mGestureDetector.onTouchEvent(paramMotionEvent);
      String str = this.mCopyAddress;
      int j = 0;
      float f1;
      float f2;
      int k;
      RecipientChip localRecipientChip1;
      if (str == null)
      {
        j = 0;
        if (i == 1)
        {
          f1 = paramMotionEvent.getX();
          f2 = paramMotionEvent.getY();
          k = putOffsetInRange(getOffsetForPosition(f1, f2));
          localRecipientChip1 = findChip(k);
          if (localRecipientChip1 == null)
            break label214;
          if (i == 1)
          {
            if ((this.mSelectedChip == null) || (this.mSelectedChip == localRecipientChip1))
              break label159;
            clearSelectedChip();
            this.mSelectedChip = selectChip(localRecipientChip1);
          }
          j = 1;
          bool1 = true;
        }
      }
      while (true)
      {
        if ((i != 1) || (j != 0))
          break label250;
        clearSelectedChip();
        return bool1;
        if (this.mSelectedChip == null)
        {
          setSelection(getText().length());
          commitDefault();
          this.mSelectedChip = selectChip(localRecipientChip1);
          break;
        }
        onClick(this.mSelectedChip, k, f1, f2);
        break;
        RecipientChip localRecipientChip2 = this.mSelectedChip;
        j = 0;
        if (localRecipientChip2 != null)
        {
          boolean bool2 = shouldShowEditableText(this.mSelectedChip);
          j = 0;
          if (bool2)
            j = 1;
        }
      }
    }
  }

  protected void performFiltering(CharSequence paramCharSequence, int paramInt)
  {
    if ((enoughToFilter()) && (!isCompletedToken(paramCharSequence)))
    {
      int i = getSelectionEnd();
      int j = this.mTokenizer.findTokenStart(paramCharSequence, i);
      RecipientChip[] arrayOfRecipientChip = (RecipientChip[])getSpannable().getSpans(j, i, RecipientChip.class);
      if ((arrayOfRecipientChip != null) && (arrayOfRecipientChip.length > 0))
        return;
    }
    super.performFiltering(paramCharSequence, paramInt);
  }

  public void performValidation()
  {
  }

  void removeChip(RecipientChip paramRecipientChip)
  {
    Spannable localSpannable = getSpannable();
    int i = localSpannable.getSpanStart(paramRecipientChip);
    int j = localSpannable.getSpanEnd(paramRecipientChip);
    Editable localEditable = getText();
    int k = j;
    if (paramRecipientChip == this.mSelectedChip);
    for (int m = 1; ; m = 0)
    {
      if (m != 0)
        this.mSelectedChip = null;
      while ((k >= 0) && (k < localEditable.length()) && (localEditable.charAt(k) == ' '))
        k++;
    }
    localSpannable.removeSpan(paramRecipientChip);
    if ((i >= 0) && (k > 0))
      localEditable.delete(i, k);
    if (m != 0)
      clearSelectedChip();
  }

  void removeMoreChip()
  {
    Spannable localSpannable;
    RecipientChip[] arrayOfRecipientChip;
    if (this.mMoreChip != null)
    {
      localSpannable = getSpannable();
      localSpannable.removeSpan(this.mMoreChip);
      this.mMoreChip = null;
      if ((this.mRemovedSpans != null) && (this.mRemovedSpans.size() > 0))
      {
        arrayOfRecipientChip = getSortedRecipients();
        if ((arrayOfRecipientChip != null) && (arrayOfRecipientChip.length != 0))
          break label59;
      }
    }
    return;
    label59: int i = localSpannable.getSpanEnd(arrayOfRecipientChip[(-1 + arrayOfRecipientChip.length)]);
    Editable localEditable = getText();
    Iterator localIterator = this.mRemovedSpans.iterator();
    while (localIterator.hasNext())
    {
      RecipientChip localRecipientChip = (RecipientChip)localIterator.next();
      String str = (String)localRecipientChip.getOriginalText();
      int j = localEditable.toString().indexOf(str, i);
      int k = Math.min(localEditable.length(), j + str.length());
      i = k;
      if (j != -1)
        localEditable.setSpan(localRecipientChip, j, k, 33);
    }
    this.mRemovedSpans.clear();
  }

  public void removeTextChangedListener(TextWatcher paramTextWatcher)
  {
    this.mTextWatcher = null;
    super.removeTextChangedListener(paramTextWatcher);
  }

  void replaceChip(RecipientChip paramRecipientChip, RecipientEntry paramRecipientEntry)
  {
    int i;
    int j;
    int k;
    Editable localEditable;
    CharSequence localCharSequence;
    if (paramRecipientChip == this.mSelectedChip)
    {
      i = 1;
      if (i != 0)
        this.mSelectedChip = null;
      j = getChipStart(paramRecipientChip);
      k = getChipEnd(paramRecipientChip);
      getSpannable().removeSpan(paramRecipientChip);
      localEditable = getText();
      localCharSequence = createChip(paramRecipientEntry, false);
      if (localCharSequence != null)
      {
        if ((j != -1) && (k != -1))
          break label114;
        Log.e("RecipientEditTextView", "The chip to replace does not exist but should.");
        localEditable.insert(0, localCharSequence);
      }
    }
    while (true)
    {
      setCursorVisible(true);
      if (i != 0)
        clearSelectedChip();
      return;
      i = 0;
      break;
      label114: if (!TextUtils.isEmpty(localCharSequence))
      {
        while ((k >= 0) && (k < localEditable.length()) && (localEditable.charAt(k) == ' '))
          k++;
        localEditable.replace(j, k, localCharSequence);
      }
    }
  }

  protected void replaceText(CharSequence paramCharSequence)
  {
  }

  void sanitizeBetween()
  {
    if (this.mPendingChipsCount > 0);
    int j;
    int k;
    do
    {
      Editable localEditable;
      do
      {
        RecipientChip[] arrayOfRecipientChip;
        do
        {
          return;
          arrayOfRecipientChip = getSortedRecipients();
        }
        while ((arrayOfRecipientChip == null) || (arrayOfRecipientChip.length <= 0));
        RecipientChip localRecipientChip1 = arrayOfRecipientChip[(-1 + arrayOfRecipientChip.length)];
        int i = arrayOfRecipientChip.length;
        RecipientChip localRecipientChip2 = null;
        if (i > 1)
          localRecipientChip2 = arrayOfRecipientChip[(-2 + arrayOfRecipientChip.length)];
        j = getSpannable().getSpanStart(localRecipientChip1);
        k = 0;
        if (localRecipientChip2 == null)
          break;
        k = getSpannable().getSpanEnd(localRecipientChip2);
        localEditable = getText();
      }
      while ((k == -1) || (k > -1 + localEditable.length()));
      if (localEditable.charAt(k) == ' ')
        k++;
    }
    while ((k < 0) || (j < 0) || (k >= j));
    getText().delete(k, j);
  }

  void sanitizeEnd()
  {
    if (this.mPendingChipsCount > 0);
    RecipientChip[] arrayOfRecipientChip;
    do
    {
      return;
      arrayOfRecipientChip = getSortedRecipients();
    }
    while ((arrayOfRecipientChip == null) || (arrayOfRecipientChip.length <= 0));
    this.mMoreChip = getMoreChip();
    if (this.mMoreChip != null);
    for (Object localObject = this.mMoreChip; ; localObject = getLastChip())
    {
      int i = getSpannable().getSpanEnd(localObject);
      Editable localEditable = getText();
      int j = localEditable.length();
      if (j <= i)
        break;
      if (Log.isLoggable("RecipientEditTextView", 3))
        Log.d("RecipientEditTextView", "There were extra characters after the last tokenizable entry." + localEditable);
      localEditable.delete(i + 1, j);
      return;
    }
  }

  public void setTokenizer(MultiAutoCompleteTextView.Tokenizer paramTokenizer)
  {
    this.mTokenizer = paramTokenizer;
    super.setTokenizer(this.mTokenizer);
  }

  public void setValidator(AutoCompleteTextView.Validator paramValidator)
  {
    this.mValidator = paramValidator;
    super.setValidator(paramValidator);
  }

  private class IndividualReplacementTask extends AsyncTask<Object, Void, Void>
  {
    private IndividualReplacementTask()
    {
    }

    protected Void doInBackground(Object[] paramArrayOfObject)
    {
      ArrayList localArrayList1 = (ArrayList)paramArrayOfObject[0];
      ArrayList localArrayList2 = new ArrayList();
      for (int i = 0; i < localArrayList1.size(); i++)
      {
        RecipientChip localRecipientChip2 = (RecipientChip)localArrayList1.get(i);
        if (localRecipientChip2 != null)
          localArrayList2.add(RecipientEditTextView.this.createAddressText(localRecipientChip2.getEntry()));
      }
      HashMap localHashMap = RecipientAlternatesAdapter.getMatchingRecipients(RecipientEditTextView.this.getContext(), localArrayList2);
      Iterator localIterator = localArrayList1.iterator();
      while (localIterator.hasNext())
      {
        final RecipientChip localRecipientChip1 = (RecipientChip)localIterator.next();
        if ((RecipientEntry.isCreatedRecipient(localRecipientChip1.getEntry().getContactId())) && (RecipientEditTextView.this.getSpannable().getSpanStart(localRecipientChip1) != -1))
        {
          RecipientEntry localRecipientEntry1 = RecipientEditTextView.this.createValidatedEntry((RecipientEntry)localHashMap.get(RecipientEditTextView.access$2200(RecipientEditTextView.this, localRecipientChip1.getEntry().getDestination()).toLowerCase()));
          if ((localRecipientEntry1 == null) && (!RecipientEditTextView.this.isPhoneQuery()))
            localRecipientEntry1 = localRecipientChip1.getEntry();
          final RecipientEntry localRecipientEntry2 = localRecipientEntry1;
          if (localRecipientEntry2 != null)
            RecipientEditTextView.this.mHandler.post(new Runnable()
            {
              public void run()
              {
                RecipientEditTextView.this.replaceChip(localRecipientChip1, localRecipientEntry2);
              }
            });
        }
      }
      return null;
    }
  }

  private class MoreImageSpan extends ImageSpan
  {
    public MoreImageSpan(Drawable arg2)
    {
      super();
    }
  }

  private final class RecipientChipShadow extends View.DragShadowBuilder
  {
    private final RecipientChip mChip;

    public RecipientChipShadow(RecipientChip arg2)
    {
      Object localObject;
      this.mChip = localObject;
    }

    public void onDrawShadow(Canvas paramCanvas)
    {
      this.mChip.getDrawable().draw(paramCanvas);
    }

    public void onProvideShadowMetrics(Point paramPoint1, Point paramPoint2)
    {
      Rect localRect = this.mChip.getDrawable().getBounds();
      paramPoint1.set(localRect.width(), localRect.height());
      paramPoint2.set(localRect.centerX(), localRect.centerY());
    }
  }

  private class RecipientReplacementTask extends AsyncTask<Void, Void, Void>
  {
    private RecipientReplacementTask()
    {
    }

    private RecipientChip createFreeChip(RecipientEntry paramRecipientEntry)
    {
      try
      {
        if (RecipientEditTextView.this.mNoChips)
          return null;
        RecipientChip localRecipientChip = RecipientEditTextView.this.constructChipSpan(paramRecipientEntry, -1, false, false);
        return localRecipientChip;
      }
      catch (NullPointerException localNullPointerException)
      {
        Log.e("RecipientEditTextView", localNullPointerException.getMessage(), localNullPointerException);
      }
      return null;
    }

    protected Void doInBackground(Void[] paramArrayOfVoid)
    {
      if (RecipientEditTextView.this.mIndividualReplacements != null)
        RecipientEditTextView.this.mIndividualReplacements.cancel(true);
      final ArrayList localArrayList1 = new ArrayList();
      RecipientChip[] arrayOfRecipientChip = RecipientEditTextView.this.getSortedRecipients();
      for (int i = 0; i < arrayOfRecipientChip.length; i++)
        localArrayList1.add(arrayOfRecipientChip[i]);
      if (RecipientEditTextView.this.mRemovedSpans != null)
        localArrayList1.addAll(RecipientEditTextView.this.mRemovedSpans);
      ArrayList localArrayList2 = new ArrayList();
      for (int j = 0; j < localArrayList1.size(); j++)
      {
        RecipientChip localRecipientChip2 = (RecipientChip)localArrayList1.get(j);
        if (localRecipientChip2 != null)
          localArrayList2.add(RecipientEditTextView.this.createAddressText(localRecipientChip2.getEntry()));
      }
      HashMap localHashMap = RecipientAlternatesAdapter.getMatchingRecipients(RecipientEditTextView.this.getContext(), localArrayList2);
      final ArrayList localArrayList3 = new ArrayList();
      Iterator localIterator = localArrayList1.iterator();
      while (localIterator.hasNext())
      {
        RecipientChip localRecipientChip1 = (RecipientChip)localIterator.next();
        boolean bool = RecipientEntry.isCreatedRecipient(localRecipientChip1.getEntry().getContactId());
        RecipientEntry localRecipientEntry = null;
        if (bool)
        {
          int k = RecipientEditTextView.this.getSpannable().getSpanStart(localRecipientChip1);
          localRecipientEntry = null;
          if (k != -1)
            localRecipientEntry = RecipientEditTextView.this.createValidatedEntry((RecipientEntry)localHashMap.get(RecipientEditTextView.access$2200(RecipientEditTextView.this, localRecipientChip1.getEntry().getDestination())));
        }
        if (localRecipientEntry != null)
          localArrayList3.add(createFreeChip(localRecipientEntry));
        else
          localArrayList3.add(localRecipientChip1);
      }
      if ((localArrayList3 != null) && (localArrayList3.size() > 0))
        RecipientEditTextView.this.mHandler.post(new Runnable()
        {
          public void run()
          {
            Editable localEditable = RecipientEditTextView.this.getText();
            int i = 0;
            Iterator localIterator = localArrayList1.iterator();
            while (localIterator.hasNext())
            {
              RecipientChip localRecipientChip1 = (RecipientChip)localIterator.next();
              int j = localEditable.getSpanStart(localRecipientChip1);
              if (j != -1)
              {
                int k = localEditable.getSpanEnd(localRecipientChip1);
                localEditable.removeSpan(localRecipientChip1);
                RecipientChip localRecipientChip2 = (RecipientChip)localArrayList3.get(i);
                SpannableString localSpannableString = new SpannableString(RecipientEditTextView.this.createAddressText(localRecipientChip2.getEntry()).trim() + " ");
                localSpannableString.setSpan(localRecipientChip2, 0, -1 + localSpannableString.length(), 33);
                localEditable.replace(j, k, localSpannableString);
                localRecipientChip2.setOriginalText(localSpannableString.toString());
              }
              i++;
            }
            localArrayList1.clear();
          }
        });
      return null;
    }
  }

  private class RecipientTextWatcher
    implements TextWatcher
  {
    private RecipientTextWatcher()
    {
    }

    public void afterTextChanged(Editable paramEditable)
    {
      int i = 0;
      if (TextUtils.isEmpty(paramEditable))
      {
        Spannable localSpannable = RecipientEditTextView.this.getSpannable();
        RecipientChip[] arrayOfRecipientChip = (RecipientChip[])localSpannable.getSpans(0, RecipientEditTextView.this.getText().length(), RecipientChip.class);
        int i1 = arrayOfRecipientChip.length;
        while (i < i1)
        {
          localSpannable.removeSpan(arrayOfRecipientChip[i]);
          i++;
        }
        if (RecipientEditTextView.this.mMoreChip != null)
          localSpannable.removeSpan(RecipientEditTextView.this.mMoreChip);
      }
      label97: label252: String str2;
      label265: 
      do
      {
        int m;
        do
        {
          do
          {
            break label97;
            do
              return;
            while (RecipientEditTextView.this.chipsPending());
            if ((RecipientEditTextView.this.mSelectedChip != null) && (RecipientEditTextView.this.shouldShowEditableText(RecipientEditTextView.this.mSelectedChip)))
            {
              RecipientEditTextView.this.setCursorVisible(true);
              RecipientEditTextView.this.setSelection(RecipientEditTextView.this.getText().length());
              RecipientEditTextView.this.clearSelectedChip();
            }
          }
          while (paramEditable.length() <= 1);
          int j;
          int k;
          if (RecipientEditTextView.this.getSelectionEnd() == 0)
          {
            j = 0;
            k = -1 + RecipientEditTextView.this.length();
            if (j == k)
              break label252;
          }
          for (m = paramEditable.charAt(j); ; m = paramEditable.charAt(k))
          {
            if ((m != 59) && (m != 44))
              break label265;
            RecipientEditTextView.this.commitByCharacter();
            return;
            j = -1 + RecipientEditTextView.this.getSelectionEnd();
            break;
          }
        }
        while ((m != 32) || (RecipientEditTextView.this.isPhoneQuery()));
        String str1 = RecipientEditTextView.this.getText().toString();
        int n = RecipientEditTextView.this.mTokenizer.findTokenStart(str1, RecipientEditTextView.this.getSelectionEnd());
        str2 = str1.substring(n, RecipientEditTextView.this.mTokenizer.findTokenEnd(str1, n));
      }
      while ((TextUtils.isEmpty(str2)) || (RecipientEditTextView.this.mValidator == null) || (!RecipientEditTextView.this.mValidator.isValid(str2)));
      RecipientEditTextView.this.commitByCharacter();
    }

    public void beforeTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
    }

    public void onTextChanged(CharSequence paramCharSequence, int paramInt1, int paramInt2, int paramInt3)
    {
      if (paramInt2 > paramInt3)
      {
        i = RecipientEditTextView.this.getSelectionStart();
        arrayOfRecipientChip = (RecipientChip[])RecipientEditTextView.this.getSpannable().getSpans(i, i, RecipientChip.class);
        if (arrayOfRecipientChip.length > 0)
        {
          localEditable = RecipientEditTextView.this.getText();
          j = RecipientEditTextView.this.mTokenizer.findTokenStart(localEditable, i);
          k = 1 + RecipientEditTextView.this.mTokenizer.findTokenEnd(localEditable, j);
          if (k > localEditable.length())
            k = localEditable.length();
          localEditable.delete(j, k);
          RecipientEditTextView.this.getSpannable().removeSpan(arrayOfRecipientChip[0]);
        }
      }
      while (paramInt3 <= paramInt2)
      {
        int i;
        RecipientChip[] arrayOfRecipientChip;
        Editable localEditable;
        int j;
        int k;
        return;
      }
      RecipientEditTextView.this.scrollBottomIntoView();
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.ex.chips.RecipientEditTextView
 * JD-Core Version:    0.6.2
 */