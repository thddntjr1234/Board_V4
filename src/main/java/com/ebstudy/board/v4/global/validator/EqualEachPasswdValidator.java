package com.ebstudy.board.v4.global.validator;

import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.global.exception.CustomException;
import com.ebstudy.board.v4.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * EqualEachPasswd 어노테이션을 적용한 파라미와 어노테이션에 검사 대상으로 설정한 필드로 유효성 검증 수행
 */
@Slf4j
public class EqualEachPasswdValidator implements ConstraintValidator<EqualEachPasswd, Object> {

    private String values[];

    @Override
    public void initialize(EqualEachPasswd constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {

        PostDTO post = (PostDTO) value;

        if (post.getPasswd().equals(post.getConfirmPasswd())) {
            log.info("두 패스워드 값이 같음, 유효성 통과");
            return true;
        }

        throw new CustomException(ErrorCode.INVALID_REQUEST);
    }

}
