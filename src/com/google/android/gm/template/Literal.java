package com.google.android.gm.template;

import java.io.IOException;

public class Literal extends Command
{
  public static final Literal LB = new Literal(new Fragment("{"));
  public static final Literal RB = new Literal(new Fragment("}"));
  private final Fragment mFragment;

  public Literal(Fragment paramFragment)
  {
    this.mFragment = paramFragment;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    this.mFragment.emitCode(paramJavaCodeGenerator);
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    this.mFragment.writeValue(paramAppendable, paramEvalContext);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Literal
 * JD-Core Version:    0.6.2
 */