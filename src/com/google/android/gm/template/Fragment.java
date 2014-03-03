package com.google.android.gm.template;

import java.io.IOException;

public class Fragment extends Node
{
  private final String mText;

  public Fragment(String paramString)
  {
    this.mText = paramString;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.appendOutputVariable().appendLine(".append(\"" + JavaCodeGenerator.escapeJavaString(this.mText) + "\");");
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    paramAppendable.append(this.mText);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Fragment
 * JD-Core Version:    0.6.2
 */