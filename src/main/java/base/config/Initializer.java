package base.config;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 3. 3.
 */
public class Initializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	protected Class<?>[] getRootConfigClasses() {
		return new Class[]{RootConfig.class};
	}

	protected Class<?>[] getServletConfigClasses() {
		return new Class[]{WebAppConfig.class};
	}

	protected String[] getServletMappings() {
		return new String[]{"/"};
	}

	protected Filter[] getServletFilters() {
		return new Filter[]{this.encodingFilter(), this.httpMethodFilter()};
	}

	protected void customizeRegistration(ServletRegistration.Dynamic registration) {
		// CORS 를 위해서 option request 도 받아들인다.
		registration.setInitParameter("dispatchOptionsRequest", "true");
	}

	private Filter encodingFilter() {
		CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
		encodingFilter.setEncoding("UTF-8");
		encodingFilter.setForceEncoding(true);
		return encodingFilter;
	}

	private Filter httpMethodFilter() {
		return new HiddenHttpMethodFilter();
	}

}