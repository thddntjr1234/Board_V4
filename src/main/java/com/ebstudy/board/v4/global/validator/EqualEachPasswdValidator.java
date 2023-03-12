package com.ebstudy.board.v4.global.validator;

import com.ebstudy.board.v4.dto.PostDTO;
import com.ebstudy.board.v4.global.exception.CustomException;
import com.ebstudy.board.v4.global.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * EqualEachPasswd 어노테이션을 적용한 파라미xㅓ와 어노테이션에 검사 대상으로 설정한 필드로 유효성 검증 수행
 */
@Slf4j
public class EqualEachPasswdValidator implements ConstraintValidator<EqualEachPasswd, Object> {

    private String values[];

    @Override
    public void initialize(EqualEachPasswd constraintAnnotation) {
        values = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(Object validationData, ConstraintValidatorContext context) {

        // PostDTO에 바로 캐스팅해서 쓰려고 했지만 이렇게 사용하면 PostDTO만 사용할 수 있는 Validator가 되어 버린다.
        // PostDTO post = (PostDTO) validationData;

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

        // TODO: 현재는 패스워드 일치 여부만 검사하지만 switch문으로 fields의 키 값이 매칭되는 조건문에서 유효성 검사를 수행하도록 하면
        //  @ValidationPost({"검사할 필드1", "검사할 필드2"}) PostDTO post 이런 방식으로 적용할 수 있게 된다.
        // 패스워드 일치여부 검사
        if (!parameters.get("passwd").equals(parameters.get("confirmPasswd"))) {
            log.info("패스워드 유효성 검증 실패");
            return false;
        }

        return true;
    }

}
