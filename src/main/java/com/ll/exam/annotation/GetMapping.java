package com.ll.exam.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD) // 이 어노테이션은 메서드에 붙는거다.
@Retention(RetentionPolicy.RUNTIME) // 라이프 사이클 - 이 애노테이션이 언제까지 살아 남아 있을지를 정하는

public @interface GetMapping {
    String value(); // 어노테이션 달 때, value(String) 입력가능하게 함
}