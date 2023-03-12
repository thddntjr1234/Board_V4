package com.ebstudy.board.v4.global.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

/**
 * 입력받은 두 비밀번호가 같은지
 */
@Target({PARAMETER})
@Retention(RetentionPolicy.RUNTIME) // 어노테이션은 실행시간동안 유지하기 위해 RUNTIME 설정
@Constraint(validatedBy = EqualEachPasswdValidator.class)
public @interface EqualEachPasswd {

    // 데이터 필드
    String[] value();
    String message() default "Validation Failed: 입력한 비밀번호와 실제 비밀번호가 일치하지 않습니다."; // log로 출력되는 메세지
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
