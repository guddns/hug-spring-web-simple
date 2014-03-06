package base.config;

import base.exception.BaseHandlerExceptionResolver;
import com.hug.core.context.message.EnableMessageSource;
import com.hug.core.view.ViewAttributesMapConfigurer;
import com.hug.core.view.velocity.annotation.EnableVelocity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 3. 3.
 */
@Configuration
@EnableVelocity
@ComponentScan(basePackages = {"base.controller"})
@Slf4j
public class WebAppConfig extends WebMvcConfigurationSupport implements ViewAttributesMapConfigurer {

	@Autowired(required = false)
	private MessageSourceAccessor messageSourceAccessor;
	private MappingJackson2HttpMessageConverter jsonConverter;
	private MarshallingHttpMessageConverter xmlConverter;


	protected void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/css/").addResourceLocations("/css/**").setCachePeriod(31536000);
		registry.addResourceHandler("/js/").addResourceLocations("/js/**").setCachePeriod(31536000);
		registry.addResourceHandler("/img/").addResourceLocations("/img/**").setCachePeriod(31536000);
	}

	/**
	 * 뷰에 전달할 attribute를 추가한다.
	 */
	@SuppressWarnings("unchecked")
	public void addAttributesMap(Map attributesMap) {
		// 뷰에서 바로 MessageSource를 사용할 수 있다.
		attributesMap.put("message", messageSourceAccessor);
	}

	private MappingJackson2HttpMessageConverter getJsonConverter() {
		if (jsonConverter == null) {
			jsonConverter = new MappingJackson2HttpMessageConverter();
		}
		return jsonConverter;
	}

	private MarshallingHttpMessageConverter getXmlConverter() {
		if (xmlConverter == null) {
			XStreamMarshaller marshaller = new XStreamMarshaller();
			marshaller.getXStream().aliasSystemAttribute(null, "class");
			marshaller.setAutodetectAnnotations(true);

			xmlConverter = new MarshallingHttpMessageConverter();
			xmlConverter.setMarshaller(marshaller);
			xmlConverter.setUnmarshaller(marshaller);
		}
		return xmlConverter;
	}

	/**
	 * @ResponseBody 에 응답할 MessageConverter 를 등록한다.
	 */
	protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(this.getJsonConverter());
		converters.add(this.getXmlConverter());
	}

	/**
	 * ExceptionResolver 를 등록한다.
	 */
	@Bean
	public BaseHandlerExceptionResolver handlerExceptionResolver() {
		BaseHandlerExceptionResolver exceptionResolver = new BaseHandlerExceptionResolver();
		exceptionResolver.setDefaultErrorView("error");
		exceptionResolver.setMessageConverters(new HttpMessageConverter[]{
				this.getJsonConverter(),
				this.getXmlConverter(),
		});
		return exceptionResolver;
	}

}