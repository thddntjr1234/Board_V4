package com.ebstudy.board.global.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class CustomValidator implements ConstraintValidator<CustomValidation, Object> {

    private String values[];

    @Override
    public void initialize(CustomValidation constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object validationData, ConstraintValidatorContext context) {

        // Java Reflection 사용
        Field[] fields = validationData.getClass().getDeclaredFields();
        HashMap<String, Object> parameters = new HashMap<>();

        // fields에서 값을 받아 HashMap에 key-value 저장
        for (Field field : fields) {
            try {
                field.setAccessible(true); // private 필드 접근을 위한 설정

                parameters.put(field.getName(), field.get(validationData));
                System.out.println(field.getName() + "  " + field.get(validationData));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        List<String> errorMessages = new LinkedList<>();

        // 검사할 필드에 대하여 유효성 검증 진행
        for (String targetField : values) {

            Object value = parameters.get(targetField);

            if (value == null) {
                errorMessages.add("게시글 작성을 위해 필요한 정보가 존재하지 않습니다.");
            } else {
                switch (targetField) {
                    case "categoryId":
                        break;
                    case "title":
                        validateLength((String) value, 4, 100, "제목은 4글자 이상 100글자 미만이어야 합니다", errorMessages);
                        break;
                    case "content":
                        validateLength((String) value, 4, 2000, "내용은 4글자 이상 2000글자 미만이어야 합니다", errorMessages);
                        break;
                }
            }
        }

        if (!errorMessages.isEmpty()) {
            String errorMessage = String.join("\n", errorMessages);
            throw new ValidationException(errorMessage);
        }

        return true;
    }

    private void validateLength(String value, int minLength, int maxLength, String errorMessage, List<String> errorMessages) {
        if (!(value.length() >= minLength) || !(value.length() < maxLength)) {
            errorMessages.add(errorMessage);
        }
    }
}
