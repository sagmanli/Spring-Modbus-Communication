package com.ves.main.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.ves.main.GlobalVariables;
import com.ves.main.integration.model.abstracts.ADevice;

@Controller
@RequestMapping("/monitorDevice")
public class MonitorDeviceController {
	@RequestMapping(method = RequestMethod.GET)
	public String monitorDevice(ModelMap model, HttpServletRequest request, @RequestParam(required = true) String deviceId) {
		try {
			ADevice device = GlobalVariables.getConfiguredDeviceById(deviceId);
			HashMap<String, Integer> inputRegisterMap = device.readInputRegisters();
			HashMap<String, Integer> holdingRegisterMap = device.readHoldingRegisters();

			model.addAttribute("inputRegisters", inputRegisterMap);
			model.addAttribute("holdingRegisters", holdingRegisterMap);

			if (inputRegisterMap == null || inputRegisterMap.size() == 0) {
				model.addAttribute("alert", "Tanımlı input register bulunamadı");
			}
			if (holdingRegisterMap == null || holdingRegisterMap.size() == 0) {
				model.addAttribute("alert", "Tanımlı holding register bulunamadı");
			}
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("alert", "Device bilgileri alınırken hata oluştu");
		}

		return "monitorDevice";
	}
}
