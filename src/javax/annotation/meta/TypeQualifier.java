package javax.annotation.meta;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({java.lang.annotation.ElementType.ANNOTATION_TYPE})
public @interface TypeQualifier
{
  public abstract Class<?> applicableTo();
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.meta.TypeQualifier
 * JD-Core Version:    0.6.2
 */