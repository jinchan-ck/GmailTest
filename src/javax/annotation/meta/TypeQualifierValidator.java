package javax.annotation.meta;

import java.lang.annotation.Annotation;
import javax.annotation.Nonnull;

public abstract interface TypeQualifierValidator<A extends Annotation>
{
  @Nonnull
  public abstract When forConstantValue(@Nonnull A paramA, Object paramObject);
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.meta.TypeQualifierValidator
 * JD-Core Version:    0.6.2
 */