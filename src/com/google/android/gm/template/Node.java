package com.google.android.gm.template;

import java.io.IOException;
import java.util.Map;

public abstract class Node
{
  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
  }

  public final void renderTo(Appendable paramAppendable, Map<String, ? extends Object> paramMap)
  {
    try
    {
      writeValue(paramAppendable, new EvalContext(paramMap));
      return;
    }
    catch (IOException localIOException)
    {
      throw new RuntimeException("Exception during template rendering", localIOException);
    }
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    throw new UnsupportedOperationException("writeValue() not implemented");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Node
 * JD-Core Version:    0.6.2
 */