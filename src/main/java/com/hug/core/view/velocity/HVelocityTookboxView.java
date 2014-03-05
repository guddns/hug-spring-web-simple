package com.hug.core.view.velocity;

import org.apache.velocity.context.Context;
import org.apache.velocity.tools.Scope;
import org.apache.velocity.tools.ToolboxFactory;
import org.apache.velocity.tools.config.ConfigurationUtils;
import org.apache.velocity.tools.config.XmlFactoryConfiguration;
import org.apache.velocity.tools.view.ViewToolContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.view.velocity.VelocityToolboxView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author Kim Hyeong Un
 * @since 2014. 3. 3.
 */
public class HVelocityTookboxView extends VelocityToolboxView
{
	protected Context createVelocityContext(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		ViewToolContext context = new ViewToolContext(getVelocityEngine(), request, response, getServletContext());

		ToolboxFactory factory = null;
		if (getToolboxConfigLocation() == null) {
			factory = new ToolboxFactory();
			factory.configure(ConfigurationUtils.getVelocityView());
		} else {
			XmlFactoryConfiguration cfg = new XmlFactoryConfiguration();
			cfg.read(new ServletContextResource(getServletContext(), getToolboxConfigLocation()).getURL());
			factory = cfg.createFactory();
		}

		for (String scope : Scope.values()) {
			context.addToolbox(factory.createToolbox(scope));
		}

		context.put("ctx", request.getContextPath());
		if (model != null) {
			context.putAll(model);
		}

		return context;
	}
}