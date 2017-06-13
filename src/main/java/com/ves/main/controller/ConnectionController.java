package com.ves.main.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.UUID;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ves.main.GlobalVariables;
import com.ves.main.integration.jaxb.layout.Layout;
import com.ves.main.integration.model.abstracts.ADevice;
import com.ves.main.util.FileContentUtil;
import com.ves.main.util.JaxbHelper;

@Controller
public class ConnectionController {
	@RequestMapping(value = "/updateConnectionParameters", method = RequestMethod.POST)
	public String update(Model model, @RequestParam(required = true) Map<String, String> connectionParameters) throws IOException, JAXBException {
		if (!validateInputs(connectionParameters)) {
			model.addAttribute("alert", "Missing Parameter!");
			return "home";
		}

		String deviceName = connectionParameters.get("DEVICE_NAME");
		connectionParameters.remove("DEVICE_NAME");

		ADevice device = GlobalVariables.getDeviceinfoByName(deviceName);
		for (Entry<String, String> entry : connectionParameters.entrySet()) {
			device.setValue(entry.getKey(), entry.getValue());
		}
		device.getDevice().setId(UUID.randomUUID().toString().substring(0, 8));
		GlobalVariables.addDevice(device);

		Layout layout = new Layout();
		for (ADevice dev : GlobalVariables.getDevices()) {
			layout.getDevice().add(dev.getDevice());
		}
		FileContentUtil.saveAs(JaxbHelper.marshal(layout).getBytes(), "Config/Layout/Layout1.xml");
		return "home";

	}

	@RequestMapping(value = "/testConnectionParameters", method = RequestMethod.POST)
	public @ResponseBody Map<String, String> test(@RequestParam(required = true) Map<String, String> parameters) {
		Map<String, String> oMap = new HashMap<String, String>();

		try {

			String deviceType = parameters.get("DEVICE_NAME");
			parameters.remove("DEVICE_NAME");

			ADevice device = GlobalVariables.getDeviceinfoByName(deviceType);

			Set<Entry<String, String>> entries = parameters.entrySet();
			for (Entry<String, String> entry : entries) {
				device.setValue(entry.getKey(), entry.getValue());
			}

			if (device.testConnection()) {
				oMap.put("error", "Connected");
				return oMap;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		oMap.put("error", "Connection Failure");

		return oMap;
	}

	private boolean validateInputs(Map<String, String> connectionParameters) {
		for (String key : connectionParameters.keySet()) {
			if (connectionParameters.get(key) == null || connectionParameters.get(key).isEmpty()) {
				return false;
			}
		}

		return true;
	}
}
