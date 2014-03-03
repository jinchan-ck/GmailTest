package com.google.android.gm.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class SenderInstructions
{
  private int numDrafts = 0;
  private int numVisible = 0;
  private final Set<String> seen = new HashSet();
  private final List<Sender> senders = new ArrayList();
  private int state = 0;
  private final Set<String> unreadAuthors = new HashSet();
  private final Set<String> unreadSeen = new HashSet();

  private void addNextSender()
  {
    int i;
    Sender localSender;
    switch (this.state)
    {
    default:
      i = -1 + this.senders.size() - (-2 + this.state);
      this.state = (1 + this.state);
      localSender = (Sender)this.senders.get(i);
      if (localSender.visibility != Visibility.VISIBLE)
      {
        if ((!this.seen.contains(localSender.name)) || ((localSender.unread) && (!this.unreadSeen.contains(localSender.name))))
        {
          this.seen.add(localSender.name);
          if (localSender.unread)
            this.unreadSeen.add(localSender.name);
          localSender.visibility = Visibility.VISIBLE;
          localSender.priority = this.numVisible;
          this.numVisible = (1 + this.numVisible);
        }
      }
      else
        return;
      break;
    case 0:
      if ((((Sender)this.senders.get(0)).unread) || (this.unreadAuthors.size() == 0));
      for (int j = 2; ; j = 1)
      {
        this.state = j;
        i = 0;
        break;
      }
    case 1:
      for (i = 1; ; i++)
        if ((i >= this.senders.size()) || (((Sender)this.senders.get(i)).unread))
        {
          this.state = 2;
          break;
        }
    }
    localSender.visibility = Visibility.SKIPPED;
  }

  public void addMessage(String paramString, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, int paramInt)
  {
    if (paramBoolean1)
      this.numDrafts = (1 + this.numDrafts);
    Sender localSender;
    do
    {
      return;
      localSender = new Sender();
      if (paramBoolean3)
        paramString = "";
      localSender.name = paramString;
      localSender.unread = paramBoolean2;
      localSender.visibility = Visibility.HIDDEN;
      localSender.priority = paramInt;
      this.senders.add(localSender);
    }
    while (!localSender.unread);
    this.unreadAuthors.add(localSender.name);
  }

  protected void calculateVisibility(int paramInt)
  {
    while ((canAddMore()) && (this.numVisible < paramInt))
      addNextSender();
    Iterator localIterator = this.senders.iterator();
    while (localIterator.hasNext())
    {
      Sender localSender = (Sender)localIterator.next();
      if ((localSender.visibility == Visibility.HIDDEN) && (this.seen.contains(localSender.name)))
        localSender.visibility = Visibility.SKIPPED;
    }
  }

  public boolean canAddMore()
  {
    return (this.senders.size() > 0) && (this.state < -1 + (2 + this.senders.size()));
  }

  public int getNumDrafts()
  {
    return this.numDrafts;
  }

  public int getNumVisible()
  {
    return this.numVisible;
  }

  public Collection<Sender> getSenders()
  {
    return this.senders;
  }

  public void reset()
  {
    this.senders.clear();
    this.seen.clear();
    this.unreadSeen.clear();
    this.unreadAuthors.clear();
    this.state = 0;
    this.numDrafts = 0;
    this.numVisible = 0;
  }

  void setNumDrafts(int paramInt)
  {
    this.numDrafts = paramInt;
  }

  public static class Sender
  {
    public String name;
    public int priority;
    public boolean unread;
    public SenderInstructions.Visibility visibility;
  }

  protected static enum Visibility
  {
    static
    {
      SKIPPED = new Visibility("SKIPPED", 1);
      HIDDEN = new Visibility("HIDDEN", 2);
      Visibility[] arrayOfVisibility = new Visibility[3];
      arrayOfVisibility[0] = VISIBLE;
      arrayOfVisibility[1] = SKIPPED;
      arrayOfVisibility[2] = HIDDEN;
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.provider.SenderInstructions
 * JD-Core Version:    0.6.2
 */