package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifier;
import javax.annotation.meta.When;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@TypeQualifier(applicableTo="Ljava/lang/String;")
public @interface Syntax
{
  public abstract String value();

  public abstract When when();
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.Syntax
 * JD-Core Version:    0.6.2
 */