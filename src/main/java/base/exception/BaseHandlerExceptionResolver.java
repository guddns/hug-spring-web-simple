package base.exception;

import base.dto.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 3. 3.
 */
public class BaseHandlerExceptionResolver extends AbstractHandlerExceptionResolver {
	private String defaultErrorView;
	private HttpMessageConverter<?>[] messageConverters;

	@Autowired(required = false)
	private MessageSourceAccessor message;

	
	@SuppressWarnings("unchecked")
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		HttpInputMessage inputMessage = new ServletServerHttpRequest(request);
		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();

		// 기본 http 상태코드 설정 (500)
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

		// Exception 에 따라 Response 를 생성한다.
		Response errorResponse = new Response();

		if (MethodArgumentNotValidException.class.isAssignableFrom(ex.getClass())) {
			MethodArgumentNotValidException exception = (MethodArgumentNotValidException) ex;
			List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
			List<ObjectError> globalErrors = exception.getBindingResult().getGlobalErrors();
			List<String> errors = new ArrayList<String>(fieldErrors.size() + globalErrors.size());
			for (FieldError fieldError : fieldErrors) {
				errors.add(fieldError.getField() + ", " + fieldError.getDefaultMessage());
			}
			for (ObjectError objectError : globalErrors) {
				errors.add(objectError.getObjectName() + ", " + objectError.getDefaultMessage());
			}
			errorResponse.setMsg(errors);
		} else if (MissingServletRequestParameterException.class.isAssignableFrom(ex.getClass())) {
			MissingServletRequestParameterException exception = (MissingServletRequestParameterException) ex;
			errorResponse.setMsg(message.getMessage("error.missing.parameter", new String[]{
					exception.getParameterName()
			}));
		} else {
			errorResponse.setMsg(ex.getLocalizedMessage());
		}

		// request header 의 accept 값이 application/json, application/xml 인 경우 등록된 messageConverter 에 따라 처리한다.
		if (acceptedMediaTypes.contains(MediaType.APPLICATION_JSON) || acceptedMediaTypes.contains(MediaType.APPLICATION_XML)) {
			try {
				ServletWebRequest webRequest = new ServletWebRequest(request, response);
				return this.handleResponseBody(errorResponse, webRequest);
			} catch (Exception e) {
				logger.warn("Handling of [" + ex.getClass().getName() + "] resulted in Exception", e);
			}
		} else {
			ModelAndView mav = new ModelAndView(defaultErrorView);
			mav.addObject("error", errorResponse);
			return mav;
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	private ModelAndView handleResponseBody(Object returnValue, ServletWebRequest webRequest) throws IOException {
		HttpInputMessage inputMessage = new ServletServerHttpRequest(webRequest.getRequest());

		List<MediaType> acceptedMediaTypes = inputMessage.getHeaders().getAccept();
		if (acceptedMediaTypes.isEmpty()) {
			acceptedMediaTypes = Collections.singletonList(MediaType.ALL);
		}
		MediaType.sortByQualityValue(acceptedMediaTypes);

		HttpOutputMessage outputMessage = new ServletServerHttpResponse(webRequest.getResponse());
		Class<?> returnValueType = returnValue.getClass();
		if (this.messageConverters != null) {
			for (MediaType acceptedMediaType : acceptedMediaTypes) {
				for (HttpMessageConverter messageConverter : this.messageConverters) {
					if (messageConverter.canWrite(returnValueType, acceptedMediaType)) {
						messageConverter.write(returnValue, acceptedMediaType, outputMessage);
						return new ModelAndView();
					}
				}
			}
		}

		if (logger.isWarnEnabled()) {
			logger.warn("Could not find HttpMessageConverter that supports return type [" + returnValueType + "] and " + acceptedMediaTypes);
		}

		return null;
	}

	public void setMessageConverters(HttpMessageConverter<?>[] messageConverters) {
		this.messageConverters = messageConverters;
	}

	public void setDefaultErrorView(String defaultErrorView) {
		this.defaultErrorView = defaultErrorView;
	}

}
