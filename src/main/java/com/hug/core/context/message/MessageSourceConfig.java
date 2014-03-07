package com.hug.core.context.message;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 2. 21.
 */
@Configuration
public class MessageSourceConfig implements ImportAware, ApplicationContextAware, BeanClassLoaderAware
{
	protected ApplicationContext applicationContext;
	protected ClassLoader classLoader;

	private String[] basenames;


	@Bean(name = "messageSource")
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setUseCodeAsDefaultMessage(true);
		messageSource.setCacheSeconds(5);
		messageSource.setBasenames(basenames);
		return messageSource;
	}

	@Bean
	public MessageSourceAccessor messageSourceAccessor() {
		return new MessageSourceAccessor(messageSource());
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setImportMetadata(AnnotationMetadata importMetadata) {
		AnnotationAttributes attributes = this.getAnnotationAttributes(importMetadata, EnableMessageSource.class);
		this.basenames = attributes.getStringArray("basenames");
	}

	private <A extends Annotation> AnnotationAttributes getAnnotationAttributes(AnnotationMetadata importMetadata, Class<A> annotationType) {
		Map<String, Object> annotationAttributesMap = importMetadata.getAnnotationAttributes(annotationType.getName());
		AnnotationAttributes annotationAttributes = AnnotationAttributes.fromMap(annotationAttributesMap);
		if (annotationAttributes == null) {
			Class<?> currentClass = ClassUtils.resolveClassName(importMetadata.getClassName(), classLoader);
			for (Class<?> classToInspect = currentClass; classToInspect != null; classToInspect = classToInspect.getSuperclass()) {
				A annotation = AnnotationUtils.findAnnotation(classToInspect, annotationType);
				if (annotation == null) {
					continue;
				}
				annotationAttributesMap = AnnotationUtils.getAnnotationAttributes(annotation);
				annotationAttributes = AnnotationAttributes.fromMap(annotationAttributesMap);
			}
		}

		return annotationAttributes;
	}

}
