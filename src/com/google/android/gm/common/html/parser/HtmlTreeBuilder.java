package com.google.android.gm.common.html.parser;

import com.google.android.gm.common.base.X;
import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HtmlTreeBuilder
  implements HtmlDocument.Visitor
{
  private static final Logger logger = Logger.getLogger(HtmlTreeBuilder.class.getName());
  private boolean built = false;
  private final List<HTML.Element> stack = new ArrayList();
  private final TableFixer tableFixer = new TableFixer();
  private HtmlTree tree;

  private void addMissingEndTag()
  {
    HtmlDocument.EndTag localEndTag = HtmlDocument.createEndTag(pop());
    this.tableFixer.seeEndTag(localEndTag);
    this.tree.addEndTag(localEndTag);
  }

  private int findStartTag(HTML.Element paramElement)
  {
    for (int i = this.stack.size() - 1; i >= 0; i--)
      if ((HTML.Element)this.stack.get(i) == paramElement)
        return i;
    return -1;
  }

  public static void main(String[] paramArrayOfString)
    throws IOException
  {
    logger.setLevel(Level.FINEST);
    String str1 = new String(ByteStreams.toByteArray(System.in));
    HtmlDocument localHtmlDocument = new HtmlParser().parse(str1);
    HtmlTreeBuilder localHtmlTreeBuilder = new HtmlTreeBuilder();
    localHtmlDocument.accept(localHtmlTreeBuilder);
    String str2 = localHtmlTreeBuilder.getTree().getHtml();
    System.out.println(str2);
  }

  private HTML.Element pop()
  {
    return (HTML.Element)this.stack.remove(this.stack.size() - 1);
  }

  private void push(HTML.Element paramElement)
  {
    this.stack.add(paramElement);
  }

  public void finish()
  {
    while (this.stack.size() > 0)
      addMissingEndTag();
    this.tableFixer.finish();
    this.tree.finish();
    this.built = true;
  }

  public HtmlTree getTree()
  {
    X.assertTrue(this.built);
    return this.tree;
  }

  public void start()
  {
    this.tree = new HtmlTree();
    this.tree.start();
  }

  public void visitComment(HtmlDocument.Comment paramComment)
  {
  }

  public void visitEndTag(HtmlDocument.EndTag paramEndTag)
  {
    HTML.Element localElement = paramEndTag.getElement();
    int i = findStartTag(localElement);
    if (i >= 0)
    {
      while (i < this.stack.size() - 1)
        addMissingEndTag();
      pop();
      this.tableFixer.seeEndTag(paramEndTag);
      this.tree.addEndTag(paramEndTag);
      return;
    }
    logger.finest("Ignoring end tag: " + localElement.getName());
  }

  public void visitTag(HtmlDocument.Tag paramTag)
  {
    this.tableFixer.seeTag(paramTag);
    HTML.Element localElement = paramTag.getElement();
    if (localElement.isEmpty())
    {
      this.tree.addSingularTag(paramTag);
      return;
    }
    if (paramTag.isSelfTerminating())
    {
      this.tree.addStartTag(HtmlDocument.createTag(localElement, paramTag.getAttributes(), paramTag.getOriginalHtmlBeforeAttributes(), paramTag.getOriginalHtmlAfterAttributes()));
      HtmlDocument.EndTag localEndTag = HtmlDocument.createEndTag(localElement);
      this.tableFixer.seeEndTag(localEndTag);
      this.tree.addEndTag(localEndTag);
      return;
    }
    this.tree.addStartTag(paramTag);
    push(localElement);
  }

  public void visitText(HtmlDocument.Text paramText)
  {
    this.tableFixer.seeText(paramText);
    this.tree.addText(paramText);
  }

  class TableFixer
  {
    static final int IN_CAPTION = 2;
    static final int IN_CELL = 1;
    static final int NULL;
    private int state;
    private int tables = 0;

    TableFixer()
    {
    }

    private void ensureCellState()
    {
      if (this.state != 1)
      {
        HtmlTreeBuilder.this.push(HTML4.TD_ELEMENT);
        HtmlDocument.Tag localTag = HtmlDocument.createTag(HTML4.TD_ELEMENT, null);
        HtmlTreeBuilder.this.tree.addStartTag(localTag);
        this.state = 1;
      }
    }

    private void ensureTableState()
    {
      if (this.tables == 0)
      {
        HtmlTreeBuilder.this.push(HTML4.TABLE_ELEMENT);
        HtmlDocument.Tag localTag = HtmlDocument.createTag(HTML4.TABLE_ELEMENT, null);
        HtmlTreeBuilder.this.tree.addStartTag(localTag);
        this.tables = (1 + this.tables);
      }
    }

    void finish()
    {
      boolean bool1;
      if (this.tables == 0)
      {
        bool1 = true;
        X.assertTrue(bool1);
        if (this.state != 0)
          break label32;
      }
      label32: for (boolean bool2 = true; ; bool2 = false)
      {
        X.assertTrue(bool2);
        return;
        bool1 = false;
        break;
      }
    }

    void seeEndTag(HtmlDocument.EndTag paramEndTag)
    {
      HTML.Element localElement = paramEndTag.getElement();
      if ((this.tables > 0) && (localElement.getType() == 1))
      {
        if ((!HTML4.TD_ELEMENT.equals(localElement)) && (!HTML4.TR_ELEMENT.equals(localElement)) && (!HTML4.TH_ELEMENT.equals(localElement)))
          break label56;
        this.state = 0;
      }
      label56: 
      do
      {
        return;
        if (HTML4.CAPTION_ELEMENT.equals(localElement))
        {
          this.state = 0;
          return;
        }
      }
      while (!HTML4.TABLE_ELEMENT.equals(localElement));
      boolean bool;
      if (this.tables > 0)
      {
        bool = true;
        X.assertTrue(bool);
        this.tables -= 1;
        if (this.tables <= 0)
          break label127;
      }
      label127: for (int i = 1; ; i = 0)
      {
        this.state = i;
        return;
        bool = false;
        break;
      }
    }

    void seeTag(HtmlDocument.Tag paramTag)
    {
      HTML.Element localElement = paramTag.getElement();
      if (localElement.getType() == 1)
        if (HTML4.TABLE_ELEMENT.equals(localElement))
        {
          if (this.tables > 0)
            ensureCellState();
          this.tables = (1 + this.tables);
          this.state = 0;
        }
      while ((this.tables <= 0) || (HTML4.FORM_ELEMENT.equals(localElement)))
      {
        do
        {
          return;
          ensureTableState();
          if ((HTML4.TD_ELEMENT.equals(localElement)) || (HTML4.TH_ELEMENT.equals(localElement)))
          {
            this.state = 1;
            return;
          }
        }
        while (!HTML4.CAPTION_ELEMENT.equals(localElement));
        this.state = 2;
        return;
      }
      ensureCellState();
    }

    void seeText(HtmlDocument.Text paramText)
    {
      if ((this.tables > 0) && (this.state == 0) && (!paramText.isWhitespace()))
        ensureCellState();
    }
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.common.html.parser.HtmlTreeBuilder
 * JD-Core Version:    0.6.2
 */