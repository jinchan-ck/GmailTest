package com.google.android.common.html.parser;

import com.google.android.common.base.CharMatcher;
import com.google.android.common.base.Preconditions;
import com.google.android.common.base.X;
import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Logger;

public class HtmlTree
{
  private static final PlainTextConverterFactory DEFAULT_CONVERTER_FACTORY = new PlainTextConverterFactory()
  {
    public HtmlTree.PlainTextConverter createInstance()
    {
      return new HtmlTree.DefaultPlainTextConverter();
    }
  };
  private static final Logger logger = Logger.getLogger(HtmlTree.class.getName());
  private final Stack<Integer> begins = new Stack();
  private PlainTextConverterFactory converterFactory = DEFAULT_CONVERTER_FACTORY;
  private final Stack<Integer> ends = new Stack();
  private final List<HtmlDocument.Node> nodes = new ArrayList();
  private int parent;
  private String plainText;
  private Stack<Integer> stack;
  private int[] textPositions;

  private void addNode(HtmlDocument.Node paramNode, int paramInt1, int paramInt2)
  {
    this.nodes.add(paramNode);
    this.begins.add(Integer.valueOf(paramInt1));
    this.ends.add(Integer.valueOf(paramInt2));
  }

  private void convertToPlainText()
  {
    if ((this.plainText == null) && (this.textPositions == null));
    int i;
    PlainTextConverter localPlainTextConverter;
    for (boolean bool = true; ; bool = false)
    {
      X.assertTrue(bool);
      i = this.nodes.size();
      this.textPositions = new int[i + 1];
      localPlainTextConverter = this.converterFactory.createInstance();
      for (int j = 0; j < i; j++)
      {
        this.textPositions[j] = localPlainTextConverter.getPlainTextLength();
        localPlainTextConverter.addNode((HtmlDocument.Node)this.nodes.get(j), j, ((Integer)this.ends.get(j)).intValue());
      }
    }
    this.textPositions[i] = localPlainTextConverter.getPlainTextLength();
    this.plainText = localPlainTextConverter.getPlainText();
  }

  void addEndTag(HtmlDocument.EndTag paramEndTag)
  {
    int i = this.nodes.size();
    addNode(paramEndTag, this.parent, i);
    if (this.parent != -1)
      this.ends.set(this.parent, Integer.valueOf(i));
    this.parent = ((Integer)this.stack.pop()).intValue();
  }

  void addSingularTag(HtmlDocument.Tag paramTag)
  {
    int i = this.nodes.size();
    addNode(paramTag, i, i);
  }

  void addStartTag(HtmlDocument.Tag paramTag)
  {
    int i = this.nodes.size();
    addNode(paramTag, i, -1);
    this.stack.add(Integer.valueOf(this.parent));
    this.parent = i;
  }

  void addText(HtmlDocument.Text paramText)
  {
    int i = this.nodes.size();
    addNode(paramText, i, i);
  }

  void finish()
  {
    boolean bool1 = true;
    boolean bool2;
    if (this.stack.size() == 0)
    {
      bool2 = bool1;
      X.assertTrue(bool2);
      if (this.parent != -1)
        break label36;
    }
    while (true)
    {
      X.assertTrue(bool1);
      return;
      bool2 = false;
      break;
      label36: bool1 = false;
    }
  }

  public String getPlainText()
  {
    if (this.plainText == null)
      convertToPlainText();
    return this.plainText;
  }

  public void setPlainTextConverterFactory(PlainTextConverterFactory paramPlainTextConverterFactory)
  {
    if (paramPlainTextConverterFactory == null)
      throw new NullPointerException("factory must not be null");
    this.converterFactory = paramPlainTextConverterFactory;
  }

  void start()
  {
    this.stack = new Stack();
    this.parent = -1;
  }

  public static class DefaultPlainTextConverter
    implements HtmlTree.PlainTextConverter
  {
    private static final Set<HTML.Element> BLANK_LINE_ELEMENTS = ImmutableSet.of(HTML4.P_ELEMENT, HTML4.BLOCKQUOTE_ELEMENT, HTML4.PRE_ELEMENT);
    private int preDepth = 0;
    private final HtmlTree.PlainTextPrinter printer = new HtmlTree.PlainTextPrinter();

    public void addNode(HtmlDocument.Node paramNode, int paramInt1, int paramInt2)
    {
      String str;
      if ((paramNode instanceof HtmlDocument.Text))
      {
        str = ((HtmlDocument.Text)paramNode).getText();
        if (this.preDepth > 0)
          this.printer.appendPreText(str);
      }
      HTML.Element localElement1;
      do
      {
        do
        {
          HTML.Element localElement2;
          do
          {
            return;
            this.printer.appendNormalText(str);
            return;
            if (!(paramNode instanceof HtmlDocument.Tag))
              break;
            localElement2 = ((HtmlDocument.Tag)paramNode).getElement();
            if (BLANK_LINE_ELEMENTS.contains(localElement2))
              this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.BlankLine);
            while (HTML4.BLOCKQUOTE_ELEMENT.equals(localElement2))
            {
              this.printer.incQuoteDepth();
              return;
              if (HTML4.BR_ELEMENT.equals(localElement2))
              {
                this.printer.appendForcedLineBreak();
              }
              else if (localElement2.breaksFlow())
              {
                this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.LineBreak);
                if (HTML4.HR_ELEMENT.equals(localElement2))
                {
                  this.printer.appendNormalText("________________________________");
                  this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.LineBreak);
                }
              }
            }
          }
          while (!HTML4.PRE_ELEMENT.equals(localElement2));
          this.preDepth = (1 + this.preDepth);
          return;
        }
        while (!(paramNode instanceof HtmlDocument.EndTag));
        localElement1 = ((HtmlDocument.EndTag)paramNode).getElement();
        if (BLANK_LINE_ELEMENTS.contains(localElement1))
          this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.BlankLine);
        while (HTML4.BLOCKQUOTE_ELEMENT.equals(localElement1))
        {
          this.printer.decQuoteDepth();
          return;
          if (localElement1.breaksFlow())
            this.printer.setSeparator(HtmlTree.PlainTextPrinter.Separator.LineBreak);
        }
      }
      while (!HTML4.PRE_ELEMENT.equals(localElement1));
      this.preDepth = (-1 + this.preDepth);
    }

    public final String getPlainText()
    {
      return this.printer.getText();
    }

    public final int getPlainTextLength()
    {
      return this.printer.getTextLength();
    }
  }

  public static abstract interface PlainTextConverter
  {
    public abstract void addNode(HtmlDocument.Node paramNode, int paramInt1, int paramInt2);

    public abstract String getPlainText();

    public abstract int getPlainTextLength();
  }

  public static abstract interface PlainTextConverterFactory
  {
    public abstract HtmlTree.PlainTextConverter createInstance();
  }

  static final class PlainTextPrinter
  {
    private int endingNewLines = 2;
    private int quoteDepth = 0;
    private final StringBuilder sb = new StringBuilder();
    private Separator separator = Separator.None;

    private void appendNewLine()
    {
      maybeAddQuoteMarks(false);
      this.sb.append('\n');
      this.endingNewLines = (1 + this.endingNewLines);
    }

    private void appendTextDirect(String paramString)
    {
      if (paramString.length() == 0)
        return;
      if (paramString.indexOf('\n') < 0);
      for (boolean bool = true; ; bool = false)
      {
        Preconditions.checkArgument(bool, "text must not contain newlines.");
        flushSeparator();
        maybeAddQuoteMarks(true);
        this.sb.append(paramString);
        this.endingNewLines = 0;
        return;
      }
    }

    private void flushSeparator()
    {
      switch (HtmlTree.2.$SwitchMap$com$google$android$common$html$parser$HtmlTree$PlainTextPrinter$Separator[this.separator.ordinal()])
      {
      default:
      case 1:
      case 2:
      case 3:
      }
      while (true)
      {
        this.separator = Separator.None;
        return;
        if (this.endingNewLines == 0)
        {
          this.sb.append(" ");
          continue;
          while (this.endingNewLines < 1)
            appendNewLine();
          while (this.endingNewLines < 2)
            appendNewLine();
        }
      }
    }

    private static boolean isHtmlWhiteSpace(char paramChar)
    {
      return " \n\r\t\f".indexOf(paramChar) >= 0;
    }

    private void maybeAddQuoteMarks(boolean paramBoolean)
    {
      if ((this.endingNewLines > 0) && (this.quoteDepth > 0))
      {
        for (int i = 0; i < this.quoteDepth; i++)
          this.sb.append('>');
        if (paramBoolean)
          this.sb.append(' ');
      }
    }

    final void appendForcedLineBreak()
    {
      flushSeparator();
      appendNewLine();
    }

    final void appendNormalText(String paramString)
    {
      if (paramString.length() == 0);
      boolean bool2;
      do
      {
        return;
        boolean bool1 = isHtmlWhiteSpace(paramString.charAt(0));
        bool2 = isHtmlWhiteSpace(paramString.charAt(-1 + paramString.length()));
        String str1 = CharMatcher.anyOf(" \n\r\t\f").trimFrom(paramString);
        String str2 = CharMatcher.anyOf(" \n\r\t\f").collapseFrom(str1, ' ');
        if (bool1)
          setSeparator(Separator.Space);
        appendTextDirect(str2);
      }
      while (!bool2);
      setSeparator(Separator.Space);
    }

    final void appendPreText(String paramString)
    {
      String[] arrayOfString = paramString.split("[\\r\\n]", -1);
      appendTextDirect(arrayOfString[0]);
      for (int i = 1; i < arrayOfString.length; i++)
      {
        appendNewLine();
        appendTextDirect(arrayOfString[i]);
      }
    }

    final void decQuoteDepth()
    {
      this.quoteDepth = Math.max(0, -1 + this.quoteDepth);
    }

    final String getText()
    {
      return this.sb.toString();
    }

    final int getTextLength()
    {
      return this.sb.length();
    }

    final void incQuoteDepth()
    {
      this.quoteDepth = (1 + this.quoteDepth);
    }

    final void setSeparator(Separator paramSeparator)
    {
      if (paramSeparator.ordinal() > this.separator.ordinal())
        this.separator = paramSeparator;
    }

    static enum Separator
    {
      static
      {
        LineBreak = new Separator("LineBreak", 2);
        BlankLine = new Separator("BlankLine", 3);
        Separator[] arrayOfSeparator = new Separator[4];
        arrayOfSeparator[0] = None;
        arrayOfSeparator[1] = Space;
        arrayOfSeparator[2] = LineBreak;
        arrayOfSeparator[3] = BlankLine;
      }
    }
  }
}

/* Location:           C:\Users\科\Desktop\dex2jar-0.0.9.15\dex2jar-0.0.9.15\classes_dex2jar.jar
 * Qualified Name:     com.google.android.common.html.parser.HtmlTree
 * JD-Core Version:    0.6.2
 */