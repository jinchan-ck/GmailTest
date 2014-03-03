package com.google.android.common.html.parser;

import com.google.android.common.base.Preconditions;
import java.util.Set;

public final class HTML
{
  public static final class Attribute
  {
    private final String name;
    private final int type;
    private final Set<String> values;

    public Attribute(String paramString, int paramInt)
    {
      this(paramString, paramInt, null);
    }

    public Attribute(String paramString, int paramInt, Set<String> paramSet)
    {
      Preconditions.checkNotNull(paramString, "Attribute name can not be null");
      int j;
      if (paramSet == null)
      {
        j = i;
        if (paramInt != 3)
          break label59;
      }
      while (true)
      {
        Preconditions.checkArgument(i ^ j, "Only ENUM_TYPE can have values != null");
        this.name = paramString;
        this.type = paramInt;
        this.values = paramSet;
        return;
        j = 0;
        break;
        label59: i = 0;
      }
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this)
        return true;
      if ((paramObject instanceof Attribute))
      {
        Attribute localAttribute = (Attribute)paramObject;
        return this.name.equals(localAttribute.name);
      }
      return false;
    }

    public String getName()
    {
      return this.name;
    }

    public int hashCode()
    {
      return this.name.hashCode();
    }

    public String toString()
    {
      return this.name;
    }
  }

  public static final class Element
  {
    private final boolean breaksFlow;
    private final boolean empty;
    private final Flow flow;
    private final String name;
    private final boolean optionalEndTag;
    private final int type;

    public Element(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2, boolean paramBoolean3, Flow paramFlow)
    {
      Preconditions.checkNotNull(paramString, "Element name can not be null");
      Preconditions.checkNotNull(paramFlow, "Element flow can not be null");
      this.name = paramString;
      this.type = paramInt;
      this.empty = paramBoolean1;
      this.optionalEndTag = paramBoolean2;
      this.breaksFlow = paramBoolean3;
      this.flow = paramFlow;
    }

    public boolean breaksFlow()
    {
      return this.breaksFlow;
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this)
        return true;
      if ((paramObject instanceof Element))
      {
        Element localElement = (Element)paramObject;
        return this.name.equals(localElement.name);
      }
      return false;
    }

    public String getName()
    {
      return this.name;
    }

    public int getType()
    {
      return this.type;
    }

    public int hashCode()
    {
      return this.name.hashCode();
    }

    public boolean isEmpty()
    {
      return this.empty;
    }

    public String toString()
    {
      return this.name;
    }

    public static enum Flow
    {
      static
      {
        BLOCK = new Flow("BLOCK", 1);
        NONE = new Flow("NONE", 2);
        Flow[] arrayOfFlow = new Flow[3];
        arrayOfFlow[0] = INLINE;
        arrayOfFlow[1] = BLOCK;
        arrayOfFlow[2] = NONE;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.html.parser.HTML
 * JD-Core Version:    0.6.2
 */