package com.ebstudy.board.global.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({PARAMETER})
@Retention(RetentionPolicy.RUNTIME) // 어노테이션은 실행시간동안 유지하기 위해 RUNTIME 설정
@Constraint(validatedBy = CustomValidator.class)
public @interface CustomValidation {

    // 어노테이션에 선언한 필드 리스트
    String[] value();
    String message() default "유효성 검증 실패"; // log로 출력되는 메세지
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
