package com.goorm.devsync.responses.dto;

import com.goorm.devsync.dto.Responses.CreateResponseRequest;
import jakarta.validation.*;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateResponseRequestTest {

    private final Validator validator;

    public CreateResponseRequestTest() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

    }

    @Test
    void shouldPassValidationWithValidContent() {
        CreateResponseRequest request = new CreateResponseRequest();
        request.setContent("This is a valid response content");

        Set<ConstraintViolation<CreateResponseRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    void shouldFailValidationWhenContentIsNull() {
        CreateResponseRequest request = new CreateResponseRequest();
        request.setContent(null);

        Set<ConstraintViolation<CreateResponseRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<CreateResponseRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("답변 내용은 필수입니다.");
    }

    @Test
    void shouldFailValidationWhenContentIsBlank() {
        CreateResponseRequest request = new CreateResponseRequest();
        request.setContent(" ");

        Set<ConstraintViolation<CreateResponseRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<CreateResponseRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("답변 내용은 필수입니다.");
    }

    @Test
    void shouldFailValidationWhenContentExceedsMaxLength() {
        CreateResponseRequest request = new CreateResponseRequest();
        String tooLongContent = "a".repeat(2001);
        request.setContent(tooLongContent);

        Set<ConstraintViolation<CreateResponseRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations.size()).isEqualTo(1);
        ConstraintViolation<CreateResponseRequest> violation = violations.iterator().next();
        assertThat(violation.getMessage()).isEqualTo("답변 내용은 1자 이상 2000자 이하여야 합니다.");
    }
}
