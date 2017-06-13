package com.ves.main.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ves.main.GlobalVariables;
import com.ves.main.integration.doc.factory.DeviceInfoFactory;
import com.ves.main.integration.model.abstracts.ADevice;

@Controller
public class RestController {

	@RequestMapping("/connection_parameter")
	public @ResponseBody List<Object[]> connectionParameter(@RequestParam(required = true) String deviceType) throws IOException, JAXBException {
		List<Object[]> properties = DeviceInfoFactory.getDeviceConnectionProperties(deviceType);
		return properties;
	}

	@RequestMapping("/device_list")
	public @ResponseBody List<String> deviceList() {

		List<String> deviceNames = new ArrayList<String>();

		for (ADevice device : GlobalVariables.getDeviceInfos()) {
			deviceNames.add(device.getDevice().getName());
		}

		return deviceNames;
	}

	@RequestMapping("/configured_device_list")
	public @ResponseBody Map<String, String> ConfiguredDeviceList() throws IOException, JAXBException {

		Map<String, String> deviceNames = new HashMap<String, String>();

		for (ADevice device : GlobalVariables.getDevices()) {

			deviceNames.put(device.getDevice().getId(), device.getDevice().getName());
		}

		return deviceNames;
	}
}