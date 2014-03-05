package com.hug.core.context.message;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 2. 21.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(MessageSourceConfig.class)
public @interface EnableMessageSource {

	/**
	 * 기본 인코딩값
	 */
	String defaultEncoding() default "UTF-8";

	/**
	 * properties 의 경로를 지정한다.
	 */
	String[] basenames() default {"classpath:i18n/message"};

}
