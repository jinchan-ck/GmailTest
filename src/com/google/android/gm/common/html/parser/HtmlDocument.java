package com.google.android.gm.common.html.parser;

import com.google.android.gm.common.base.CharEscaper;
import com.google.android.gm.common.base.CharEscapers;
import com.google.android.gm.common.base.CharMatcher;
import com.google.android.gm.common.base.StringUtil;
import com.google.android.gm.common.base.X;
import com.google.common.collect.Lists;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class HtmlDocument
{
  private final List<Node> nodes;

  public HtmlDocument(List<Node> paramList)
  {
    this.nodes = paramList;
  }

  public static CDATA createCDATA(String paramString)
  {
    return new CDATA(paramString, null);
  }

  public static EndTag createEndTag(HTML.Element paramElement)
  {
    return createEndTag(paramElement, null);
  }

  public static EndTag createEndTag(HTML.Element paramElement, String paramString)
  {
    return new EndTag(paramElement, paramString, null);
  }

  public static Text createEscapedText(String paramString1, String paramString2)
  {
    return new EscapedText(paramString1, paramString2, null);
  }

  public static Comment createHtmlComment(String paramString)
  {
    return new Comment(paramString);
  }

  public static Tag createSelfTerminatingTag(HTML.Element paramElement, List<TagAttribute> paramList)
  {
    return createSelfTerminatingTag(paramElement, paramList, null, null);
  }

  public static Tag createSelfTerminatingTag(HTML.Element paramElement, List<TagAttribute> paramList, String paramString1, String paramString2)
  {
    return new Tag(paramElement, paramList, true, paramString1, paramString2, null);
  }

  public static Tag createTag(HTML.Element paramElement, List<TagAttribute> paramList)
  {
    return createTag(paramElement, paramList, null, null);
  }

  public static Tag createTag(HTML.Element paramElement, List<TagAttribute> paramList, String paramString1, String paramString2)
  {
    return new Tag(paramElement, paramList, false, paramString1, paramString2, null);
  }

  public static TagAttribute createTagAttribute(HTML.Attribute paramAttribute, String paramString)
  {
    return createTagAttribute(paramAttribute, paramString, null);
  }

  public static TagAttribute createTagAttribute(HTML.Attribute paramAttribute, String paramString1, String paramString2)
  {
    if (paramAttribute != null);
    for (boolean bool = true; ; bool = false)
    {
      X.assertTrue(bool);
      return new TagAttribute(paramAttribute, paramString1, paramString2, null);
    }
  }

  public static Text createText(String paramString)
  {
    return createText(paramString, null);
  }

  public static Text createText(String paramString1, String paramString2)
  {
    return new UnescapedText(paramString1, paramString2, null);
  }

  public void accept(Visitor paramVisitor)
  {
    paramVisitor.start();
    Iterator localIterator = this.nodes.iterator();
    while (localIterator.hasNext())
      ((Node)localIterator.next()).accept(paramVisitor);
    paramVisitor.finish();
  }

  public HtmlDocument filter(MultiplexFilter paramMultiplexFilter)
  {
    paramMultiplexFilter.start();
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.nodes.iterator();
    while (localIterator.hasNext())
      paramMultiplexFilter.filter((Node)localIterator.next(), localArrayList);
    paramMultiplexFilter.finish(localArrayList);
    return new HtmlDocument(localArrayList);
  }

  public List<Node> getNodes()
  {
    return this.nodes;
  }

  public String toHTML()
  {
    StringBuilder localStringBuilder = new StringBuilder(10 * this.nodes.size());
    Iterator localIterator = this.nodes.iterator();
    while (localIterator.hasNext())
      ((Node)localIterator.next()).toHTML(localStringBuilder);
    return localStringBuilder.toString();
  }

  public String toOriginalHTML()
  {
    StringBuilder localStringBuilder = new StringBuilder(10 * this.nodes.size());
    Iterator localIterator = this.nodes.iterator();
    while (localIterator.hasNext())
      ((Node)localIterator.next()).toOriginalHTML(localStringBuilder);
    return localStringBuilder.toString();
  }

  public String toString()
  {
    StringWriter localStringWriter = new StringWriter();
    accept(new DebugPrinter(new PrintWriter(localStringWriter)));
    return localStringWriter.toString();
  }

  public String toXHTML()
  {
    StringBuilder localStringBuilder = new StringBuilder(10 * this.nodes.size());
    Iterator localIterator = this.nodes.iterator();
    while (localIterator.hasNext())
      ((Node)localIterator.next()).toXHTML(localStringBuilder);
    return localStringBuilder.toString();
  }

  public static class Builder
    implements HtmlDocument.Visitor
  {
    private HtmlDocument doc;
    private final List<HtmlDocument.Node> nodes = new ArrayList();
    private final boolean preserveComments;

    public Builder()
    {
      this(false);
    }

    public Builder(boolean paramBoolean)
    {
      this.preserveComments = paramBoolean;
    }

    public void addNode(HtmlDocument.Node paramNode)
    {
      this.nodes.add(paramNode);
    }

    public void finish()
    {
      this.doc = new HtmlDocument(this.nodes);
    }

    public HtmlDocument getDocument()
    {
      return this.doc;
    }

    public void start()
    {
    }

    public void visitComment(HtmlDocument.Comment paramComment)
    {
      if (this.preserveComments)
        addNode(paramComment);
    }

    public void visitEndTag(HtmlDocument.EndTag paramEndTag)
    {
      addNode(paramEndTag);
    }

    public void visitTag(HtmlDocument.Tag paramTag)
    {
      addNode(paramTag);
    }

    public void visitText(HtmlDocument.Text paramText)
    {
      addNode(paramText);
    }
  }

  public static class CDATA extends HtmlDocument.UnescapedText
  {
    private CDATA(String paramString)
    {
      super(paramString, null);
    }

    public void toHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append(this.text);
    }

    public void toXHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append("<![CDATA[").append(this.text).append("]]>");
    }
  }

  public static class Comment extends HtmlDocument.Node
  {
    private final String content;

    public Comment(String paramString)
    {
      this.content = paramString;
    }

    public void accept(HtmlDocument.Visitor paramVisitor)
    {
      paramVisitor.visitComment(this);
    }

    public String getContent()
    {
      return this.content;
    }

    public void toHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append(this.content);
    }

    public void toOriginalHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append(this.content);
    }

    public void toXHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append(this.content);
    }
  }

  public static class DebugPrinter
    implements HtmlDocument.Visitor
  {
    private final PrintWriter writer;

    public DebugPrinter(PrintWriter paramPrintWriter)
    {
      this.writer = paramPrintWriter;
    }

    private void writeCollapsed(String paramString1, String paramString2)
    {
      this.writer.print(paramString1);
      this.writer.print(": ");
      String str1 = paramString2.replace("\n", " ");
      String str2 = CharMatcher.LEGACY_WHITESPACE.trimAndCollapseFrom(str1, ' ');
      this.writer.print(str2);
    }

    public void finish()
    {
    }

    public void start()
    {
    }

    public void visitComment(HtmlDocument.Comment paramComment)
    {
      writeCollapsed("COMMENT", paramComment.getContent());
    }

    public void visitEndTag(HtmlDocument.EndTag paramEndTag)
    {
      this.writer.println("==</" + paramEndTag.getName() + ">");
    }

    public void visitTag(HtmlDocument.Tag paramTag)
    {
      this.writer.print("==<" + paramTag.getName() + ">");
      List localList = paramTag.getAttributes();
      if (localList != null)
      {
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator = localList.iterator();
        while (localIterator.hasNext())
        {
          HtmlDocument.TagAttribute localTagAttribute = (HtmlDocument.TagAttribute)localIterator.next();
          localArrayList.add("[" + localTagAttribute.getName() + " : " + localTagAttribute.getValue() + "]");
        }
        String[] arrayOfString = (String[])localArrayList.toArray(new String[localArrayList.size()]);
        Arrays.sort(arrayOfString);
        for (int i = 0; i < arrayOfString.length; i++)
          this.writer.print(" " + arrayOfString[i]);
      }
      this.writer.println();
    }

    public void visitText(HtmlDocument.Text paramText)
    {
      writeCollapsed("TEXT", paramText.getText());
    }
  }

  public static class EndTag extends HtmlDocument.Node
  {
    private final HTML.Element element;
    private final String originalHtml;

    private EndTag(HTML.Element paramElement, String paramString)
    {
      if (paramElement != null);
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        this.element = paramElement;
        this.originalHtml = paramString;
        return;
      }
    }

    public void accept(HtmlDocument.Visitor paramVisitor)
    {
      paramVisitor.visitEndTag(this);
    }

    public HTML.Element getElement()
    {
      return this.element;
    }

    public String getName()
    {
      return this.element.getName();
    }

    public void toHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append("</");
      paramStringBuilder.append(this.element.getName());
      paramStringBuilder.append('>');
    }

    public void toOriginalHTML(StringBuilder paramStringBuilder)
    {
      if (this.originalHtml != null)
      {
        paramStringBuilder.append(this.originalHtml);
        return;
      }
      toHTML(paramStringBuilder);
    }

    public String toString()
    {
      return "End Tag: " + this.element.getName();
    }

    public void toXHTML(StringBuilder paramStringBuilder)
    {
      toHTML(paramStringBuilder);
    }
  }

  private static class EscapedText extends HtmlDocument.Text
  {
    private final String htmlText;
    private String text;

    private EscapedText(String paramString1, String paramString2)
    {
      super();
      this.htmlText = paramString1;
    }

    public String getText()
    {
      if (this.text == null)
        this.text = StringUtil.unescapeHTML(this.htmlText);
      return this.text;
    }
  }

  public static abstract interface Filter
  {
    public abstract void finish();

    public abstract void start();

    public abstract HtmlDocument.Comment visitComment(HtmlDocument.Comment paramComment);

    public abstract HtmlDocument.EndTag visitEndTag(HtmlDocument.EndTag paramEndTag);

    public abstract HtmlDocument.Tag visitTag(HtmlDocument.Tag paramTag);

    public abstract HtmlDocument.Text visitText(HtmlDocument.Text paramText);
  }

  public static abstract interface MultiplexFilter
  {
    public abstract void filter(HtmlDocument.Node paramNode, List<HtmlDocument.Node> paramList);

    public abstract void finish(List<HtmlDocument.Node> paramList);

    public abstract void start();
  }

  public static class MultiplexFilterAdapter
    implements HtmlDocument.MultiplexFilter
  {
    private final HtmlDocument.Filter filter;

    public MultiplexFilterAdapter(HtmlDocument.Filter paramFilter)
    {
      this.filter = paramFilter;
    }

    public void filter(HtmlDocument.Node paramNode, List<HtmlDocument.Node> paramList)
    {
      if (paramNode == null);
      while (true)
      {
        return;
        Object localObject;
        if ((paramNode instanceof HtmlDocument.Tag))
          localObject = this.filter.visitTag((HtmlDocument.Tag)paramNode);
        while (localObject != null)
        {
          paramList.add(localObject);
          return;
          if ((paramNode instanceof HtmlDocument.Text))
          {
            localObject = this.filter.visitText((HtmlDocument.Text)paramNode);
          }
          else if ((paramNode instanceof HtmlDocument.EndTag))
          {
            localObject = this.filter.visitEndTag((HtmlDocument.EndTag)paramNode);
          }
          else
          {
            if (!(paramNode instanceof HtmlDocument.Comment))
              break label111;
            localObject = this.filter.visitComment((HtmlDocument.Comment)paramNode);
          }
        }
      }
      label111: throw new IllegalArgumentException("unknown node type: " + paramNode.getClass());
    }

    public void finish(List<HtmlDocument.Node> paramList)
    {
      this.filter.finish();
    }

    public void start()
    {
      this.filter.start();
    }
  }

  public static class MultiplexFilterChain
    implements HtmlDocument.MultiplexFilter
  {
    private final List<HtmlDocument.MultiplexFilter> filters = new ArrayList();

    public MultiplexFilterChain(List<HtmlDocument.MultiplexFilter> paramList)
    {
      this.filters.addAll(paramList);
    }

    public void filter(HtmlDocument.Node paramNode, List<HtmlDocument.Node> paramList)
    {
      Object localObject = new ArrayList();
      ((List)localObject).add(paramNode);
      Iterator localIterator1 = this.filters.iterator();
      while (localIterator1.hasNext())
      {
        HtmlDocument.MultiplexFilter localMultiplexFilter = (HtmlDocument.MultiplexFilter)localIterator1.next();
        if (((List)localObject).isEmpty())
          return;
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator2 = ((List)localObject).iterator();
        while (localIterator2.hasNext())
          localMultiplexFilter.filter((HtmlDocument.Node)localIterator2.next(), localArrayList);
        localObject = localArrayList;
      }
      paramList.addAll((Collection)localObject);
    }

    public void finish(List<HtmlDocument.Node> paramList)
    {
      Object localObject = new ArrayList();
      Iterator localIterator1 = this.filters.iterator();
      while (localIterator1.hasNext())
      {
        HtmlDocument.MultiplexFilter localMultiplexFilter = (HtmlDocument.MultiplexFilter)localIterator1.next();
        ArrayList localArrayList = new ArrayList();
        Iterator localIterator2 = ((List)localObject).iterator();
        while (localIterator2.hasNext())
          localMultiplexFilter.filter((HtmlDocument.Node)localIterator2.next(), localArrayList);
        localMultiplexFilter.finish(localArrayList);
        localObject = localArrayList;
      }
      paramList.addAll((Collection)localObject);
    }

    public void start()
    {
      Iterator localIterator = this.filters.iterator();
      while (localIterator.hasNext())
        ((HtmlDocument.MultiplexFilter)localIterator.next()).start();
    }
  }

  public static abstract class Node
  {
    public abstract void accept(HtmlDocument.Visitor paramVisitor);

    public String toHTML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      toHTML(localStringBuilder);
      return localStringBuilder.toString();
    }

    public abstract void toHTML(StringBuilder paramStringBuilder);

    public String toOriginalHTML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      toOriginalHTML(localStringBuilder);
      return localStringBuilder.toString();
    }

    public abstract void toOriginalHTML(StringBuilder paramStringBuilder);

    public String toXHTML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      toXHTML(localStringBuilder);
      return localStringBuilder.toString();
    }

    public abstract void toXHTML(StringBuilder paramStringBuilder);
  }

  public static abstract class SimpleMultiplexFilter
    implements HtmlDocument.MultiplexFilter
  {
    public void filter(HtmlDocument.Node paramNode, List<HtmlDocument.Node> paramList)
    {
      if (paramNode == null)
        return;
      if ((paramNode instanceof HtmlDocument.Tag))
      {
        filterTag((HtmlDocument.Tag)paramNode, paramList);
        return;
      }
      if ((paramNode instanceof HtmlDocument.Text))
      {
        filterText((HtmlDocument.Text)paramNode, paramList);
        return;
      }
      if ((paramNode instanceof HtmlDocument.EndTag))
      {
        filterEndTag((HtmlDocument.EndTag)paramNode, paramList);
        return;
      }
      if ((paramNode instanceof HtmlDocument.Comment))
      {
        filterComment((HtmlDocument.Comment)paramNode, paramList);
        return;
      }
      throw new IllegalArgumentException("unknown node type: " + paramNode.getClass());
    }

    public void filterComment(HtmlDocument.Comment paramComment, List<HtmlDocument.Node> paramList)
    {
    }

    public abstract void filterEndTag(HtmlDocument.EndTag paramEndTag, List<HtmlDocument.Node> paramList);

    public abstract void filterTag(HtmlDocument.Tag paramTag, List<HtmlDocument.Node> paramList);

    public abstract void filterText(HtmlDocument.Text paramText, List<HtmlDocument.Node> paramList);
  }

  public static class Tag extends HtmlDocument.Node
  {
    private List<HtmlDocument.TagAttribute> attributes;
    private final HTML.Element element;
    private final boolean isSelfTerminating;
    private final String originalHtmlAfterAttributes;
    private final String originalHtmlBeforeAttributes;

    private Tag(HTML.Element paramElement, List<HtmlDocument.TagAttribute> paramList, boolean paramBoolean, String paramString1, String paramString2)
    {
      if (paramElement != null);
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        this.element = paramElement;
        this.attributes = paramList;
        this.isSelfTerminating = paramBoolean;
        this.originalHtmlBeforeAttributes = paramString1;
        this.originalHtmlAfterAttributes = paramString2;
        return;
      }
    }

    private void serialize(StringBuilder paramStringBuilder, SerializeType paramSerializeType)
    {
      Iterator localIterator;
      if ((paramSerializeType == SerializeType.ORIGINAL_HTML) && (this.originalHtmlBeforeAttributes != null))
      {
        paramStringBuilder.append(this.originalHtmlBeforeAttributes);
        if (this.attributes != null)
          localIterator = this.attributes.iterator();
      }
      else
      {
        while (true)
        {
          if (!localIterator.hasNext())
            break label126;
          HtmlDocument.TagAttribute localTagAttribute = (HtmlDocument.TagAttribute)localIterator.next();
          if (paramSerializeType == SerializeType.ORIGINAL_HTML)
          {
            localTagAttribute.toOriginalHTML(paramStringBuilder);
            continue;
            paramStringBuilder.append('<');
            paramStringBuilder.append(this.element.getName());
            break;
          }
          if (paramSerializeType == SerializeType.HTML)
            localTagAttribute.toHTML(paramStringBuilder);
          else
            localTagAttribute.toXHTML(paramStringBuilder);
        }
      }
      label126: if ((paramSerializeType == SerializeType.ORIGINAL_HTML) && (this.originalHtmlAfterAttributes != null))
      {
        paramStringBuilder.append(this.originalHtmlAfterAttributes);
        return;
      }
      if ((paramSerializeType == SerializeType.XHTML) && ((this.isSelfTerminating) || (getElement().isEmpty())))
      {
        paramStringBuilder.append(" />");
        return;
      }
      paramStringBuilder.append('>');
    }

    public void accept(HtmlDocument.Visitor paramVisitor)
    {
      paramVisitor.visitTag(this);
    }

    public void addAttribute(HTML.Attribute paramAttribute, String paramString)
    {
      if (paramAttribute != null);
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        addAttribute(new HtmlDocument.TagAttribute(paramAttribute, paramString, null, null));
        return;
      }
    }

    public void addAttribute(HtmlDocument.TagAttribute paramTagAttribute)
    {
      if (paramTagAttribute != null);
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        if (this.attributes == null)
          this.attributes = new ArrayList();
        this.attributes.add(paramTagAttribute);
        return;
      }
    }

    public HtmlDocument.TagAttribute getAttribute(HTML.Attribute paramAttribute)
    {
      if (this.attributes != null)
      {
        Iterator localIterator = this.attributes.iterator();
        while (localIterator.hasNext())
        {
          HtmlDocument.TagAttribute localTagAttribute = (HtmlDocument.TagAttribute)localIterator.next();
          if (localTagAttribute.getAttribute().equals(paramAttribute))
            return localTagAttribute;
        }
      }
      return null;
    }

    public List<HtmlDocument.TagAttribute> getAttributes()
    {
      return this.attributes;
    }

    public List<HtmlDocument.TagAttribute> getAttributes(HTML.Attribute paramAttribute)
    {
      ArrayList localArrayList = Lists.newArrayList();
      if (this.attributes != null)
      {
        Iterator localIterator = this.attributes.iterator();
        while (localIterator.hasNext())
        {
          HtmlDocument.TagAttribute localTagAttribute = (HtmlDocument.TagAttribute)localIterator.next();
          if (localTagAttribute.getAttribute().equals(paramAttribute))
            localArrayList.add(localTagAttribute);
        }
      }
      return localArrayList;
    }

    public HTML.Element getElement()
    {
      return this.element;
    }

    public String getName()
    {
      return this.element.getName();
    }

    public String getOriginalHtmlAfterAttributes()
    {
      return this.originalHtmlAfterAttributes;
    }

    public String getOriginalHtmlBeforeAttributes()
    {
      return this.originalHtmlBeforeAttributes;
    }

    public boolean isSelfTerminating()
    {
      return this.isSelfTerminating;
    }

    public void toHTML(StringBuilder paramStringBuilder)
    {
      serialize(paramStringBuilder, SerializeType.HTML);
    }

    public void toOriginalHTML(StringBuilder paramStringBuilder)
    {
      serialize(paramStringBuilder, SerializeType.ORIGINAL_HTML);
    }

    public String toString()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      localStringBuilder.append("Start Tag: ");
      localStringBuilder.append(this.element.getName());
      if (this.attributes != null)
      {
        Iterator localIterator = this.attributes.iterator();
        while (localIterator.hasNext())
        {
          HtmlDocument.TagAttribute localTagAttribute = (HtmlDocument.TagAttribute)localIterator.next();
          localStringBuilder.append(' ');
          localStringBuilder.append(localTagAttribute.toString());
        }
      }
      return localStringBuilder.toString();
    }

    public void toXHTML(StringBuilder paramStringBuilder)
    {
      serialize(paramStringBuilder, SerializeType.XHTML);
    }

    private static enum SerializeType
    {
      static
      {
        HTML = new SerializeType("HTML", 1);
        XHTML = new SerializeType("XHTML", 2);
        SerializeType[] arrayOfSerializeType = new SerializeType[3];
        arrayOfSerializeType[0] = ORIGINAL_HTML;
        arrayOfSerializeType[1] = HTML;
        arrayOfSerializeType[2] = XHTML;
      }
    }
  }

  public static class TagAttribute
  {
    private final HTML.Attribute attribute;
    private String originalHtml;
    private String value;

    private TagAttribute(HTML.Attribute paramAttribute, String paramString1, String paramString2)
    {
      if (paramAttribute != null);
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        this.attribute = paramAttribute;
        this.value = paramString1;
        this.originalHtml = paramString2;
        return;
      }
    }

    public HTML.Attribute getAttribute()
    {
      return this.attribute;
    }

    public String getName()
    {
      return this.attribute.getName();
    }

    public String getValue()
    {
      if (this.value != null)
        return this.value;
      return "";
    }

    public boolean hasValue()
    {
      return this.value != null;
    }

    public void setValue(String paramString)
    {
      this.value = paramString;
      this.originalHtml = null;
    }

    public String toHTML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      toHTML(localStringBuilder);
      return localStringBuilder.toString();
    }

    public void toHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append(' ');
      paramStringBuilder.append(this.attribute.getName());
      if ((this.value != null) && (this.attribute.getType() != 4))
      {
        paramStringBuilder.append("=\"");
        paramStringBuilder.append(CharEscapers.asciiHtmlEscaper().escape(this.value));
        paramStringBuilder.append("\"");
      }
    }

    public String toOriginalHTML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      toOriginalHTML(localStringBuilder);
      return localStringBuilder.toString();
    }

    public void toOriginalHTML(StringBuilder paramStringBuilder)
    {
      if (this.originalHtml != null)
      {
        paramStringBuilder.append(this.originalHtml);
        return;
      }
      toHTML(paramStringBuilder);
    }

    public String toString()
    {
      return "{" + this.attribute.getName() + "=" + this.value + "}";
    }

    public String toXHTML()
    {
      StringBuilder localStringBuilder = new StringBuilder();
      toXHTML(localStringBuilder);
      return localStringBuilder.toString();
    }

    public void toXHTML(StringBuilder paramStringBuilder)
    {
      paramStringBuilder.append(' ');
      paramStringBuilder.append(this.attribute.getName()).append("=\"");
      if (hasValue())
        paramStringBuilder.append(CharEscapers.asciiHtmlEscaper().escape(this.value));
      while (true)
      {
        paramStringBuilder.append("\"");
        return;
        paramStringBuilder.append(this.attribute.getName());
      }
    }
  }

  public static abstract class Text extends HtmlDocument.Node
  {
    private String html;
    private final String originalHtml;

    protected Text(String paramString)
    {
      this.originalHtml = paramString;
    }

    public void accept(HtmlDocument.Visitor paramVisitor)
    {
      paramVisitor.visitText(this);
    }

    public boolean equals(Object paramObject)
    {
      if (paramObject == this)
        return true;
      if ((paramObject instanceof Text))
      {
        Text localText = (Text)paramObject;
        if (this.originalHtml == null)
          return localText.originalHtml == null;
        return this.originalHtml.equals(localText.originalHtml);
      }
      return false;
    }

    public String getOriginalHTML()
    {
      return this.originalHtml;
    }

    public abstract String getText();

    public int hashCode()
    {
      if (this.originalHtml == null)
        return 0;
      return this.originalHtml.hashCode();
    }

    public boolean isWhitespace()
    {
      String str = getText();
      int i = str.length();
      for (int j = 0; j < i; j++)
        if (!Character.isWhitespace(str.charAt(j)))
          return false;
      return true;
    }

    public void toHTML(StringBuilder paramStringBuilder)
    {
      if (this.html == null)
        this.html = CharEscapers.asciiHtmlEscaper().escape(getText());
      paramStringBuilder.append(this.html);
    }

    public void toOriginalHTML(StringBuilder paramStringBuilder)
    {
      if (this.originalHtml != null)
      {
        paramStringBuilder.append(this.originalHtml);
        return;
      }
      toHTML(paramStringBuilder);
    }

    public String toString()
    {
      return getText();
    }

    public void toXHTML(StringBuilder paramStringBuilder)
    {
      toHTML(paramStringBuilder);
    }
  }

  private static class UnescapedText extends HtmlDocument.Text
  {
    protected final String text;

    private UnescapedText(String paramString1, String paramString2)
    {
      super();
      if (paramString1 != null);
      for (boolean bool = true; ; bool = false)
      {
        X.assertTrue(bool);
        this.text = paramString1;
        return;
      }
    }

    public String getText()
    {
      return this.text;
    }
  }

  public static abstract interface Visitor
  {
    public abstract void finish();

    public abstract void start();

    public abstract void visitComment(HtmlDocument.Comment paramComment);

    public abstract void visitEndTag(HtmlDocument.EndTag paramEndTag);

    public abstract void visitTag(HtmlDocument.Tag paramTag);

    public abstract void visitText(HtmlDocument.Text paramText);
  }

  public static class VisitorWrapper
    implements HtmlDocument.Visitor
  {
    private final HtmlDocument.Visitor wrapped;

    protected VisitorWrapper(HtmlDocument.Visitor paramVisitor)
    {
      this.wrapped = paramVisitor;
    }

    public void finish()
    {
      this.wrapped.finish();
    }

    public void start()
    {
      this.wrapped.start();
    }

    public void visitComment(HtmlDocument.Comment paramComment)
    {
      this.wrapped.visitComment(paramComment);
    }

    public void visitEndTag(HtmlDocument.EndTag paramEndTag)
    {
      this.wrapped.visitEndTag(paramEndTag);
    }

    public void visitTag(HtmlDocument.Tag paramTag)
    {
      this.wrapped.visitTag(paramTag);
    }

    public void visitText(HtmlDocument.Text paramText)
    {
      this.wrapped.visitText(paramText);
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.html.parser.HtmlDocument
 * JD-Core Version:    0.6.2
 */