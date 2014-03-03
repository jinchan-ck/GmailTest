package com.google.android.gm;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.google.android.gm.provider.Gmail.Attachment;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;

class AttachmentsView extends LinearLayout
{
  private List<Gmail.Attachment> mAttachments = Lists.newArrayList();
  private AttachmentChangesListener mChangeListener;

  public AttachmentsView(Context paramContext)
  {
    this(paramContext, null);
  }

  public AttachmentsView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
  }

  private void updateAttachmentViews()
  {
  }

  public void addAttachment(final Gmail.Attachment paramAttachment)
  {
    this.mAttachments.add(paramAttachment);
    final AttachmentComposeView localAttachmentComposeView = new AttachmentComposeView(getContext(), paramAttachment);
    localAttachmentComposeView.addDeleteListener(new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        AttachmentsView.this.mAttachments.remove(paramAttachment);
        AttachmentsView.this.removeView(localAttachmentComposeView);
        AttachmentsView.this.updateAttachmentViews();
        AttachmentsView.this.mChangeListener.onAttachmentDeleted();
      }
    });
    addView(localAttachmentComposeView, new LinearLayout.LayoutParams(-1, -1));
    updateAttachmentViews();
    this.mChangeListener.onAttachmentAdded();
  }

  public List<Gmail.Attachment> getAttachments()
  {
    return this.mAttachments;
  }

  public int getTotalAttachmentsSize()
  {
    int i = 0;
    Iterator localIterator = this.mAttachments.iterator();
    while (localIterator.hasNext())
      i += ((Gmail.Attachment)localIterator.next()).size;
    return i;
  }

  public void setAttachmentChangesListener(AttachmentChangesListener paramAttachmentChangesListener)
  {
    this.mChangeListener = paramAttachmentChangesListener;
  }

  public static abstract interface AttachmentChangesListener
  {
    public abstract void onAttachmentAdded();

    public abstract void onAttachmentDeleted();
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.AttachmentsView
 * JD-Core Version:    0.6.2
 */