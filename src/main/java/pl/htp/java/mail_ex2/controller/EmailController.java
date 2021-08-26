package pl.htp.java.mail_ex2.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.htp.java.mail_ex2.constant.ConstantVariable;
import pl.htp.java.mail_ex2.model.*;
import pl.htp.java.mail_ex2.service.ActionService;
import pl.htp.java.mail_ex2.service.UserService;

@Controller
@RequestMapping("/mail")
public class EmailController {

	@Autowired
	private ActionService actionService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/firstPage", method = RequestMethod.GET)
	public String loginPage() {
		return "registration";
	}

	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(HttpServletRequest request, User user) {
		userService.saveUser(user);
		request.getSession().setAttribute(ConstantVariable.USER, user);
		return "redirect:/mail/listAction";
	}

	@RequestMapping(value = "/listAction", method = RequestMethod.GET)
	public String listAction(Model model) {
		List<Action> actionList = actionService.findAllAction();
		model.addAttribute(ConstantVariable.QUEUE_ACTION_LIST, actionList);
		return "listAction";
	}

	@RequestMapping(value = "/addActionPage", method = RequestMethod.GET)
	public String addActionPage() {
		return "addAction";
	}

	@RequestMapping(value = "/addAction", method = RequestMethod.POST)
	public String addAction(Action action, HttpServletRequest request, Model model) {
		User user = (User) request.getSession().getAttribute(ConstantVariable.USER);
		actionService.saveAction(action, user);
		model.addAttribute(ConstantVariable.AMOUNT_QUEUE, actionService.amout(action));
		model.addAttribute(ConstantVariable.TIME_FOR_WITE, actionService.timeForWait());
		model.addAttribute(ConstantVariable.ACTION, action);
		return "newAction";
	}

	@RequestMapping(value = "/infoAction", method = RequestMethod.POST)
	public String infoAction(String number, HttpServletRequest request, Model model) {

		Action action = actionService.findActionByNumber(number);
		if (action.getNumber() == 0) {
			return "noAction";
		}
		model.addAttribute(ConstantVariable.AMOUNT_QUEUE, actionService.amout(number));
		model.addAttribute(ConstantVariable.TIME_FOR_WITE, actionService.timeForWait(number));
		model.addAttribute(ConstantVariable.ACTION, action);
		return "infoAction";

	}
}
