package com.google.android.gm.template;

import java.io.IOException;
import java.util.Map;

public class Call extends Command
{
  private final String mName;
  private final Expression mReference;

  public Call(String paramString, Expression paramExpression)
  {
    this.mName = paramString;
    this.mReference = paramExpression;
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    paramJavaCodeGenerator.append(JavaCodeGenerator.getSafeTemplateName(this.mName)).append("(").appendOutputVariable().append(",");
    if (this.mReference != null)
      paramJavaCodeGenerator.append("new EvalContext((java.util.Map<String, ? extends Object>)").append(this.mReference).append(")");
    while (true)
    {
      paramJavaCodeGenerator.appendLine(");");
      return;
      paramJavaCodeGenerator.appendContextVariable();
    }
  }

  public void writeValue(Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    Template localTemplate = Templates.get(this.mName);
    if (localTemplate == null)
      throw new RuntimeException("Can't call template \"" + this.mName + "\" (not found)");
    EvalContext localEvalContext = paramEvalContext;
    if (this.mReference != null)
    {
      Object localObject = this.mReference.evaluate(paramEvalContext);
      if ((localObject != null) && ((localObject instanceof Map)))
        localEvalContext = new EvalContext((Map)localObject);
    }
    else
    {
      localTemplate.writeValue(paramAppendable, localEvalContext);
      return;
    }
    throw new RuntimeException("Can't call template\"" + this.mName + "\", \"" + this.mReference + "\" is not a map");
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.Call
 * JD-Core Version:    0.6.2
 */