package com.ebstudy.board.global.validator;

import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * 어노테이션에 검사 대상으로 설정한 필드 대상으로 유효성 검증 수행
 */
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

        // 검사할 필드에 대하여 유효성 검증 진행
        for (String targetField : values) {

            Object value = parameters.get(targetField);

            if (value == null) {
                String errorMessage = targetField + " 유효성 검증 실패: 필드에 유효한 값이 존재하지 않습니다.";
                throw new ValidationException(errorMessage);
            }

            switch (targetField) {
                case "categoryId":
                    break;
                case "title":
                    validateLength((String) value, 4, 100, "제목은 4글자 이상 100글자 미만이어야 합니다");
                    break;
                case "content":
                    validateLength((String) value, 4, 2000, "내용은 4글자 이상 2000글자 미만이어야 합니다");
                    break;
            }
        }
        return true;
    }

    private void validateLength(String value, int minLength, int maxLength, String errorMessage) {
        if (!(value.length() >= minLength) || !(value.length() < maxLength)) {
            throw new ValidationException(errorMessage);
        }
    }

    private void validatePassword(String password) {
        String regex = "^.*(?=^.{4,15}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$";
        if (!password.matches(regex)) {
            throw new ValidationException("비밀번호는 4글자 이상 16글자 미만, 영문과 숫자, 특수문자를 포함해야 합니다");
        }
    }
}
