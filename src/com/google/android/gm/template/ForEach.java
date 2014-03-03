package com.google.android.gm.template;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ForEach extends Command
{
  private final ArrayList<Node> mChildren;
  private final Expression mListExpression;
  private final String mVariableName;

  public ForEach(String paramString, Expression paramExpression, ArrayList<Node> paramArrayList)
  {
    this.mVariableName = paramString;
    this.mListExpression = paramExpression;
    this.mChildren = paramArrayList;
  }

  public static void iterateListExpression(Object paramObject, String paramString, EvalContext paramEvalContext, ItemVisitor paramItemVisitor)
    throws IOException
  {
    if ((paramObject instanceof List))
    {
      List localList = (List)paramObject;
      int i = localList.size();
      if (i > 0)
      {
        HashMap localHashMap = new HashMap();
        localHashMap.put(paramString + "$lastIndex", Integer.valueOf(i - 1));
        paramEvalContext.push(localHashMap);
        String str = paramString + "$index";
        for (int j = 0; j < i; j++)
        {
          localHashMap.put(str, Integer.valueOf(j));
          localHashMap.put(paramString, localList.get(j));
          paramItemVisitor.visit(paramEvalContext);
        }
        paramEvalContext.pop();
      }
      return;
    }
    throw new RuntimeException("Invalid collection");
  }

  public void emitCode(JavaCodeGenerator paramJavaCodeGenerator)
    throws IOException
  {
    String str = this.mVariableName + "$visitor";
    paramJavaCodeGenerator.appendLine("ForEach.ItemVisitor %s = new ForEach.ItemVisitor() {", new Object[] { str });
    paramJavaCodeGenerator.increaseIndent();
    paramJavaCodeGenerator.append("public void visit(EvalContext ").appendContextVariable().append(") ").appendLine("throws java.io.IOException {");
    paramJavaCodeGenerator.increaseIndent();
    int i = 0;
    int j = this.mChildren.size();
    while (i < j)
    {
      ((Node)this.mChildren.get(i)).emitCode(paramJavaCodeGenerator);
      i++;
    }
    paramJavaCodeGenerator.decreaseIndent();
    paramJavaCodeGenerator.appendLine("}");
    paramJavaCodeGenerator.decreaseIndent();
    paramJavaCodeGenerator.appendLine("};");
    JavaCodeGenerator localJavaCodeGenerator = paramJavaCodeGenerator.append("ForEach.iterateListExpression(").append(this.mListExpression);
    Object[] arrayOfObject = new Object[1];
    arrayOfObject[0] = this.mVariableName;
    localJavaCodeGenerator.append(",\"%s\"", arrayOfObject).append(",").appendContextVariable().append(",").append(str).appendLine(");");
  }

  public void writeValue(final Appendable paramAppendable, EvalContext paramEvalContext)
    throws IOException
  {
    Object localObject = this.mListExpression.evaluate(paramEvalContext);
    ItemVisitor local1 = new ItemVisitor()
    {
      public void visit(EvalContext paramAnonymousEvalContext)
        throws IOException
      {
        int i = 0;
        int j = ForEach.this.mChildren.size();
        while (i < j)
        {
          ((Node)ForEach.this.mChildren.get(i)).writeValue(paramAppendable, paramAnonymousEvalContext);
          i++;
        }
      }
    };
    iterateListExpression(localObject, this.mVariableName, paramEvalContext, local1);
  }

  public static abstract interface ItemVisitor
  {
    public abstract void visit(EvalContext paramEvalContext)
      throws IOException;
  }
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     com.google.android.gm.template.ForEach
 * JD-Core Version:    0.6.2
 */