package com.denyyyys.fluentLift.validators;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ClozeTemplateValidator.class)
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidClozeBlock {
    String message() default "Template keys and answers do not match";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}