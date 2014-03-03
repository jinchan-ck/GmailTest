package com.android.mail.ui;

import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.widget.FrameLayout;
import com.android.mail.browse.MessageAttachmentTile;
import com.android.mail.compose.ComposeAttachmentTile;
import com.android.mail.providers.Attachment;
import com.android.mail.utils.AttachmentUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class AttachmentTileGrid extends FrameLayout
  implements AttachmentTile.AttachmentPreviewCache
{
  private HashMap<String, AttachmentTile.AttachmentPreview> mAttachmentPreviews;
  private List<Attachment> mAttachments;
  private Uri mAttachmentsListUri;
  private int mColumnCount;
  private FragmentManager mFragmentManager;
  private LayoutInflater mInflater;
  private final int mTileMinSize;

  public AttachmentTileGrid(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    this.mInflater = LayoutInflater.from(paramContext);
    this.mTileMinSize = paramContext.getResources().getDimensionPixelSize(2131361885);
    this.mAttachmentPreviews = Maps.newHashMap();
  }

  private void addMessageTileFromAttachment(Attachment paramAttachment, int paramInt, boolean paramBoolean)
  {
    MessageAttachmentTile localMessageAttachmentTile;
    if (getChildCount() <= paramInt)
    {
      localMessageAttachmentTile = MessageAttachmentTile.inflate(this.mInflater, this);
      localMessageAttachmentTile.initialize(this.mFragmentManager);
      addView(localMessageAttachmentTile);
    }
    while (true)
    {
      localMessageAttachmentTile.render(paramAttachment, this.mAttachmentsListUri, paramInt, this, paramBoolean);
      return;
      localMessageAttachmentTile = (MessageAttachmentTile)getChildAt(paramInt);
    }
  }

  private void onLayoutForTiles()
  {
    int i = getChildCount();
    int j = 0;
    int k = 0;
    int m = 1;
    int n = 0;
    if (n < i)
    {
      View localView = getChildAt(n);
      int i1 = localView.getMeasuredWidth();
      int i2 = localView.getMeasuredHeight();
      if ((m == 0) && (n % this.mColumnCount == 0))
      {
        j = 0;
        k += i2;
      }
      while (true)
      {
        localView.layout(j, k, j + i1, k + i2);
        j += i1;
        n++;
        break;
        m = 0;
      }
    }
  }

  private void onMeasureForTiles(int paramInt)
  {
    int i = View.MeasureSpec.getSize(paramInt);
    int j = getChildCount();
    if (j == 0)
    {
      setMeasuredDimension(i, 0);
      return;
    }
    this.mColumnCount = (i / this.mTileMinSize);
    if (this.mColumnCount == 0)
      this.mColumnCount = 1;
    int k = i / this.mColumnCount;
    int m = i - k * this.mColumnCount;
    int n = 0;
    if (n < j)
    {
      View localView = getChildAt(n);
      if (n < m);
      for (int i1 = 1; ; i1 = 0)
      {
        localView.measure(View.MeasureSpec.makeMeasureSpec(k + i1, 1073741824), View.MeasureSpec.makeMeasureSpec(k, 1073741824));
        n++;
        break;
      }
    }
    setMeasuredDimension(i, (1 + (j - 1) / this.mColumnCount) * (k + getChildAt(0).getPaddingBottom()));
  }

  public ComposeAttachmentTile addComposeTileFromAttachment(Attachment paramAttachment)
  {
    ComposeAttachmentTile localComposeAttachmentTile = ComposeAttachmentTile.inflate(this.mInflater, this);
    addView(localComposeAttachmentTile);
    localComposeAttachmentTile.render(paramAttachment, null, -1, this, false);
    return localComposeAttachmentTile;
  }

  public void configureGrid(FragmentManager paramFragmentManager, Uri paramUri, List<Attachment> paramList, boolean paramBoolean)
  {
    this.mFragmentManager = paramFragmentManager;
    this.mAttachmentsListUri = paramUri;
    this.mAttachments = paramList;
    int i = 0;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      Attachment localAttachment = (Attachment)localIterator.next();
      int j = i + 1;
      addMessageTileFromAttachment(localAttachment, i, paramBoolean);
      i = j;
    }
  }

  public Bitmap get(Attachment paramAttachment)
  {
    String str = AttachmentUtils.getIdentifier(paramAttachment);
    if (str != null)
    {
      AttachmentTile.AttachmentPreview localAttachmentPreview = (AttachmentTile.AttachmentPreview)this.mAttachmentPreviews.get(str);
      if (localAttachmentPreview != null)
        return localAttachmentPreview.preview;
    }
    return null;
  }

  public ArrayList<AttachmentTile.AttachmentPreview> getAttachmentPreviews()
  {
    return Lists.newArrayList(this.mAttachmentPreviews.values());
  }

  public List<Attachment> getAttachments()
  {
    return this.mAttachments;
  }

  protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    onLayoutForTiles();
  }

  protected void onMeasure(int paramInt1, int paramInt2)
  {
    onMeasureForTiles(paramInt1);
  }

  public void sendAccessibilityEvent(int paramInt)
  {
  }

  public void set(Attachment paramAttachment, Bitmap paramBitmap)
  {
    String str = AttachmentUtils.getIdentifier(paramAttachment);
    if (str != null)
      this.mAttachmentPreviews.put(str, new AttachmentTile.AttachmentPreview(paramAttachment, paramBitmap));
  }

  public void setAttachmentPreviews(ArrayList<AttachmentTile.AttachmentPreview> paramArrayList)
  {
    if (paramArrayList != null)
    {
      Iterator localIterator = paramArrayList.iterator();
      while (localIterator.hasNext())
      {
        AttachmentTile.AttachmentPreview localAttachmentPreview = (AttachmentTile.AttachmentPreview)localIterator.next();
        this.mAttachmentPreviews.put(localAttachmentPreview.attachmentIdentifier, localAttachmentPreview);
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.android.mail.ui.AttachmentTileGrid
 * JD-Core Version:    0.6.2
 */