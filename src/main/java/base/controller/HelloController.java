package base.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class HelloController {

	@RequestMapping(method = RequestMethod.POST)
	public String index(ModelMap model) {
//		model.addAttribute("message", "Hello world!");
		return "index";
	}

	@RequestMapping(value = "/errorTest", method = RequestMethod.GET)
	public String errorTest(@RequestParam String name, ModelMap model) {
//		model.addAttribute("message", "Hello world!");
		if (true) {
			throw new RuntimeException("에러가 발생했습니다.");
		}
		return "index";
	}

}