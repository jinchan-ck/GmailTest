package javax.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import javax.annotation.meta.TypeQualifierDefault;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Nonnull
@TypeQualifierDefault({java.lang.annotation.ElementType.PARAMETER})
public @interface ParametersAreNonnullByDefault
{
}

/* Location:           C:\Users\科\Desktop\classes_dex2jar.jar
 * Qualified Name:     javax.annotation.ParametersAreNonnullByDefault
 * JD-Core Version:    0.6.2
 */