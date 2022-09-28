package ru.practicum.shareit.dto;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.comment.dto.CommentInDto;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@JsonTest
class CommentInDtoTest {
    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();
    @Autowired
    private JacksonTester<CommentInDto> json;

    @Test
    void testSerialize() throws Exception {
        var dto = new CommentInDto(
                "Comment 1"
        );

        var result = json.write(dto);

        assertThat(result).hasJsonPath("$.text");
        assertThat(result).extractingJsonPathStringValue("$.text").isEqualTo(dto.getText());
    }

    @DisplayName("Check Blank")
    @ParameterizedTest
    @CsvSource({
            "null, 1, NotBlank.message",
            "'', 1, NotBlank.message",
            "text, 0, OK",
    })
    void textNotBlank(String text, int expectSize, String expectedMessage) {
        if ("null".equals(text)) {
            text = null;
        }
        var dto = new CommentInDto(text);
        Set<ConstraintViolation<CommentInDto>> violations = validator.validate(dto);
        Assertions.assertEquals(expectSize, violations.size());

        if (!violations.isEmpty()) {
            assertThat(violations.iterator().next().getMessageTemplate())
                    .contains(expectedMessage);
        }
    }
}