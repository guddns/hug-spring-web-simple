package com.hug.core.view.velocity.annotation;

import com.hug.core.view.ViewAttributesMapConfigurer;
import com.hug.core.view.velocity.HVelocityTookboxView;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportAware;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.util.ClassUtils;
import org.springframework.web.servlet.view.velocity.VelocityConfigurer;
import org.springframework.web.servlet.view.velocity.VelocityViewResolver;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: pe.hug
 * Date: 13. 1. 23.
 * Time: 오전 11:05
 * To change this template use File | Settings | File Templates.
 */
@Configuration
public class VelocityConfig implements ImportAware, BeanClassLoaderAware, ApplicationContextAware
{
	protected ApplicationContext applicationContext;
	protected ClassLoader classLoader;

	private String resourceLoaderPath;
	private String configLocation;
	private String contentType;
	private String toolboxConfigLocation;
	private String suffix;
	private int order;

	@Autowired(required = false)
	ViewAttributesMapConfigurer attributesMapConfigurer;


	@Bean
	public VelocityViewResolver velocityViewResolver() {
		VelocityViewResolver viewResolver = new VelocityViewResolver();
		viewResolver.setExposeRequestAttributes(true);
		viewResolver.setExposeSessionAttributes(true);
		viewResolver.setExposeSpringMacroHelpers(true);
		viewResolver.setViewClass(HVelocityTookboxView.class);
		viewResolver.setCache(true);
		viewResolver.setContentType(this.contentType);
		viewResolver.setSuffix(this.suffix);
		viewResolver.setOrder(this.order);

		Resource resource = applicationContext.getResource(this.toolboxConfigLocation);
		if (resource.exists()) {
			viewResolver.setToolboxConfigLocation(this.toolboxConfigLocation);
		}

		// AttributesMapConfigurer 이 있는 경우 모든 뷰에 attribute 를 전달 한다.
		if (attributesMapConfigurer != null) {
			HashMap<String, Object> attributesMap = new HashMap<String, Object>();
			attributesMapConfigurer.addAttributesMap(attributesMap);
			viewResolver.setAttributesMap(attributesMap);
		}

		return viewResolver;
	}

	@Bean
	public VelocityConfigurer velocityConfigurer() {
		VelocityConfigurer configurer = new VelocityConfigurer();
		configurer.setResourceLoaderPath(this.resourceLoaderPath);

		Resource resource = applicationContext.getResource(this.configLocation);
		if (resource.exists()) {
			configurer.setConfigLocation(resource);
		}

		return configurer;
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	public void setImportMetadata(AnnotationMetadata importMetadata) {
		AnnotationAttributes annotationAttributes = this.getAnnotationAttributes(importMetadata, EnableVelocity.class);
		resourceLoaderPath = annotationAttributes.getString("resourceLoaderPath");
		configLocation = annotationAttributes.getString("configLocation");
		toolboxConfigLocation = annotationAttributes.getString("toolboxConfigLocation");
		contentType = annotationAttributes.getString("contentType");
		suffix = annotationAttributes.getString("suffix");
		order = annotationAttributes.getNumber("order");
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
