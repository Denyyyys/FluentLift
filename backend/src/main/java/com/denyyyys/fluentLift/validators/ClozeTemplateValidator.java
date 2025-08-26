package com.denyyyys.fluentLift.validators;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.denyyyys.fluentLift.model.postgres.dto.request.course.lessonBlock.ClozeBlockCreateDto;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ClozeTemplateValidator implements ConstraintValidator<ValidClozeBlock, ClozeBlockCreateDto> {

    private static final Pattern TEMPLATE_KEY_PATTERN = Pattern.compile("\\{\\{(.*?)}}");

    @Override
    public boolean isValid(ClozeBlockCreateDto block, ConstraintValidatorContext context) {
        if (block == null)
            return true;

        String template = block.getTemplate();

        if (template == null && block.getAnswers() == null)
            return true;

        if (template == null ^ block.getAnswers() == null) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Both template and answers must be present or both absent").addConstraintViolation();
            return false;
        }

        Set<String> answerKeys = block.getAnswers().stream()
                .map(answer -> {
                    return answer.getKey();
                })
                .collect(Collectors.toSet());

        Matcher matcher = TEMPLATE_KEY_PATTERN.matcher(template);
        Set<String> templateKeys = new HashSet<>();
        while (matcher.find()) {
            templateKeys.add(matcher.group(1));
        }

        if (templateKeys.size() != answerKeys.size()) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Expected keys " + templateKeys + " but got " + answerKeys)
                    .addPropertyNode("answers").addConstraintViolation();
            return false;
        }

        if (!templateKeys.equals(answerKeys)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(
                    "Expected keys " + templateKeys + " but got " + answerKeys).addPropertyNode("answers")
                    .addConstraintViolation();
            return false;
        }

        return true;
    }
}
