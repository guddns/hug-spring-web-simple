package base.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import com.thoughtworks.xstream.annotations.XStreamConverters;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import com.thoughtworks.xstream.converters.extended.ToStringConverter;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.xml.bind.annotation.XmlElement;
import java.io.Serializable;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 3. 3.
 */
@XStreamAlias("response")
public class Response<M, T> implements Serializable {

	private int code;
	@XmlElement(name = "msg")
	private M msg;
	private T result;

	public Response() {
	}

	public Response(int code, M msg) {
		this.code = code;
		this.msg = msg;
	}

	public Response(int code, M msg,  T result) {
		this.code = code;
		this.msg = msg;
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public M getMsg() {
		return msg;
	}

	public void setMsg(M msg) {
		this.msg = msg;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}
}
