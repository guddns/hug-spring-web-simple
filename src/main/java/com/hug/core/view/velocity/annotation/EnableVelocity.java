package com.hug.core.view.velocity.annotation;

import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 2. 19.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(VelocityConfig.class)
public @interface EnableVelocity {

	String suffix() default ".vm";

	String contentType() default "text/html; charset=UTF-8";

	/**
	 * toolbox 경로를 지정한다.
	 * ex)/WEB-INF/classes/velocity/toolbox.xml
	 */
	String toolboxConfigLocation() default "/WEB-INF/classes/velocity/toolbox.xml";

	/**
	 * .vm 의 기본 경로를 지정한다.
	 */
	String resourceLoaderPath() default "/WEB-INF/views/velocity";

	/**
	 * VelocityConfigurer 설정을 위한 properties 파일의 경로를 지정한다.
	 * ex)classpath:velocity/velocity.properties
	 */
	String configLocation() default "classpath:velocity/velocity.properties";

	/**
	 * ViewResolver 의 적용 순서를 정한다.
	 * 0 부터 시작하며 숫자가 작을수록 우선순위는 높다.
	 */
	int order() default 1;

}
