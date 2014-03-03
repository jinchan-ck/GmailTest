package com.google.android.gm.template;

import java.io.IOException;

public class UnaryNot extends Expression
{
  private final Expression mExpression;

  public UnaryNot(Expression paramExpression)
  {
    this.mExpression = paramExpression;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.append("!Expression.isTruthy(").append(this.mExpression).append(")");
  }

  public Object evaluate(EvalContext paramEvalContext)
  {
    if (!this.mExpression.booleanValue(paramEvalContext));
    for (boolean bool = true; ; bool = false)
      return Boolean.valueOf(bool);
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.UnaryNot
 * JD-Core Version:    0.6.2
 */