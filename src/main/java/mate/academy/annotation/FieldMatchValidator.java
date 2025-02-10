package mate.academy.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FieldMatchValidator implements ConstraintValidator<FieldMatch, String> {
    private String firstPass;
    private String secondPass;

    @Override
    public void initialize(FieldMatch constraintAnnotation) {
        firstPass = constraintAnnotation.first();
        secondPass = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(String pass, ConstraintValidatorContext constraintValidatorContext) {
        return firstPass.equals(secondPass);
    }
}
