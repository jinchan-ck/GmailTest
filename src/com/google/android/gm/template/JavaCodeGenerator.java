package com.google.android.gm.template;

import java.io.IOException;

public class JavaCodeGenerator
{
  private static final String INDENT = "    ";
  private String curIndent;
  private final boolean mContextInitialized = false;
  private boolean mInTemplate = false;
  private final boolean mIndentingEnabled = true;
  private boolean mIsNewLine = true;
  private final Appendable mOut;
  private final boolean mOutputInitialized = false;

  static
  {
    if (!JavaCodeGenerator.class.desiredAssertionStatus());
    for (boolean bool = true; ; bool = false)
    {
      $assertionsDisabled = bool;
      return;
    }
  }

  public JavaCodeGenerator(Appendable paramAppendable)
  {
    this.mOut = paramAppendable;
    this.curIndent = "";
  }

  public static String escapeJavaString(String paramString)
  {
    return paramString.replace("\\", "\\\\").replace("\b", "\\b").replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t").replace("\"", "\\\"");
  }

  public static String getSafeTemplateName(String paramString)
  {
    return "render_" + paramString.replace('.', '$');
  }

  public JavaCodeGenerator append(Node paramNode)
    throws IOException
  {
    paramNode.emitCode(this);
    return this;
  }

  public JavaCodeGenerator append(String paramString)
    throws IOException
  {
    if (this.mIsNewLine)
    {
      this.mOut.append(this.curIndent);
      this.mIsNewLine = false;
    }
    this.mOut.append(paramString);
    return this;
  }

  public JavaCodeGenerator append(String paramString, Object[] paramArrayOfObject)
    throws IOException
  {
    append(String.format(paramString, paramArrayOfObject));
    return this;
  }

  public JavaCodeGenerator appendContextVariable()
    throws IOException
  {
    if (!$assertionsDisabled)
      throw new AssertionError();
    append("__context");
    return this;
  }

  public JavaCodeGenerator appendLine(String paramString)
    throws IOException
  {
    if (this.mIsNewLine)
      this.mOut.append(this.curIndent);
    this.mOut.append(paramString).append("\n");
    this.mIsNewLine = true;
    return this;
  }

  public JavaCodeGenerator appendLine(String paramString, Object[] paramArrayOfObject)
    throws IOException
  {
    appendLine(String.format(paramString, paramArrayOfObject));
    return this;
  }

  public JavaCodeGenerator appendOutputVariable()
    throws IOException
  {
    if (!$assertionsDisabled)
      throw new AssertionError();
    append("__out");
    return this;
  }

  public JavaCodeGenerator beginTemplateOutput(String paramString)
    throws IOException
  {
    assert (!this.mInTemplate);
    append("public static final void ").append(getSafeTemplateName(paramString)).appendLine("(final Appendable out, java.util.Map<String,? extends Object> data) {").increaseIndent().append(getSafeTemplateName(paramString)).appendLine("(out, new EvalContext(data));").decreaseIndent().appendLine("}").appendLine("");
    append("public static final void ").append(getSafeTemplateName(paramString)).appendLine("(final Appendable __out, final EvalContext __context) {");
    increaseIndent();
    appendLine("try {");
    return this;
  }

  public JavaCodeGenerator decreaseIndent()
  {
    if (this.curIndent.length() >= "    ".length())
      this.curIndent = this.curIndent.substring(0, this.curIndent.length() - "    ".length());
    return this;
  }

  public JavaCodeGenerator endTemplateOutput()
    throws IOException
  {
    assert (this.mInTemplate);
    this.mInTemplate = false;
    appendLine("} catch (java.io.IOException ex) {").increaseIndent().append("throw new RuntimeException(").appendLine("\"Exception during template rendering\", ex);").decreaseIndent().appendLine("}");
    decreaseIndent();
    appendLine("}");
    return this;
  }

  public JavaCodeGenerator increaseIndent()
  {
    this.curIndent += "    ";
    return this;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.JavaCodeGenerator
 * JD-Core Version:    0.6.2
 */