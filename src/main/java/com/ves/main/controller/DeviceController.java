package com.ves.main.controller;

import java.util.Locale;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ves.main.GlobalVariables;
import com.ves.main.integration.model.abstracts.ADevice;

@Controller
public class DeviceController {

	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);

	@Autowired
	private ServletContext      servletContext;

	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@RequestMapping(value = "/editDevice", method = RequestMethod.GET)
	public String home(Locale locale, Model model, @RequestParam(required = true) String deviceId) {
		logger.info("Welcome home! The client locale is {}.", locale);

		ADevice device = GlobalVariables.getConfiguredDeviceById(deviceId);

		model.addAttribute("deviceId", deviceId);
		model.addAttribute("DEVICE_NAME", device.getDevice().getName());
		model.addAttribute("alert", "");

		return "editDevice";
	}

	@RequestMapping(value = "/updateRegister", method = RequestMethod.POST)
	public String updateRegister(Locale locale, Model model, @RequestParam(required = true) String deviceId, @RequestParam(required = true) String registerKey, @RequestParam(required = true) String oldRegisterValue, @RequestParam(required = false) String newRegisterValue) {
		model.addAttribute("deviceId", deviceId);

		if (newRegisterValue == null) {
			model.addAttribute("alert", "Girdiğiniz değer uygun formatta değil");
			return "editDevice";
		}

		if (Integer.valueOf(oldRegisterValue) == Integer.valueOf(newRegisterValue)) {
			model.addAttribute("alert", "Girdiğiniz değer eski değerle aynı!");
			return "editDevice";
		}

		ADevice device = GlobalVariables.getConfiguredDeviceById(deviceId);
		device.setValue(registerKey, newRegisterValue);

		return "editDevice";
	}
}
