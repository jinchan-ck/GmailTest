package com.google.android.gm.template;

import java.io.IOException;

public class Print extends Command
{
  private final Expression mExpression;
  private final boolean mIsAutoEscape;

  public Print(Expression paramExpression, boolean paramBoolean)
  {
    this.mExpression = paramExpression;
    this.mIsAutoEscape = paramBoolean;
  }

  public static void print(Appendable paramAppendable, Object paramObject)
    throws IOException
  {
    if (paramObject != null)
      paramAppendable.append(paramObject.toString());
  }

  public static void printEscaped(Appendable paramAppendable, Object paramObject)
    throws IOException
  {
    if (paramObject != null)
    {
      String str = paramObject.toString();
      int i = 0;
      int j = str.length();
      if (i < j)
      {
        char c = str.charAt(i);
        switch (c)
        {
        default:
          paramAppendable.append(c);
        case '<':
        case '>':
        case '&':
        case '"':
        case '\'':
        }
        while (true)
        {
          i++;
          break;
          paramAppendable.append("&lt;");
          continue;
          paramAppendable.append("&gt;");
          continue;
          paramAppendable.append("&amp;");
          continue;
          paramAppendable.append("&quot;");
          continue;
          paramAppendable.append("&#39;");
        }
      }
    }
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    if (this.mIsAutoEscape)
    {
      paramJavaCodeGenerator.append("Print.printEscaped(").appendOutputVariable().append(",").append(this.mExpression).appendLine(");");
      return;
    }
    paramJavaCodeGenerator.append("Print.print(").appendOutputVariable().append(",").append(this.mExpression).appendLine(");");
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    if (this.mIsAutoEscape)
    {
      printEscaped(paramAppendable, this.mExpression.evaluate(paramEvalContext));
      return;
    }
    print(paramAppendable, this.mExpression.evaluate(paramEvalContext));
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Print
 * JD-Core Version:    0.6.2
 */