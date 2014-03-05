package base.config;

import com.hug.core.context.message.EnableMessageSource;
import org.springframework.context.annotation.Configuration;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 3. 3.
 */
@Configuration
@EnableMessageSource(basenames = {"classpath:i18n/messages", "classpath:i18n/errors"})
public class RootConfig {

}
