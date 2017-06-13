package com.ves.main.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ves.main.GlobalVariables;
import com.ves.main.integration.doc.factory.DeviceInfoFactory;
import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.jaxb.user.userlist.UserList;
import com.ves.main.integration.model.abstracts.ADevice;
import com.ves.main.integration.provider.ConnectionProvider;
import com.ves.main.util.FileContentUtil;
import com.ves.main.util.JaxbHelper;

@Service
public class ScheduledEvents {
	private static final Logger logger = Logger.getLogger(ScheduledEvents.class);

	// @Scheduled(cron = "*/5 * * * * ?")
	public void init() {
		// HashMap<Integer, Integer> registers = ModbusSerialRTU.readInputRegisters("COM6", 9600, SerialPort.DATABITS_8, SerialPort.PARITY_NONE, SerialPort.STOPBITS_1, Modbus.SERIAL_ENCODING_RTU, 1, 0, 1);
		//
		// for (Integer key : registers.keySet()) {
		// System.out.println("key : " + key + ", value : " + registers.get(key));
		// }

	}

	@Scheduled(cron = "*/10 * * * * ?")
	public static void loadDeviceInfos() {
		logger.info("-START- ScheduledEvents.loadDeviceInfos");

		File folder = new File("Config/DeviceInfo");
		File[] listOfFiles = folder.listFiles();

		List<ADevice> list = new ArrayList<ADevice>();

		for (int i = 0; listOfFiles != null && i < listOfFiles.length; i++) {
			String deviceName = StringUtils.stripFilenameExtension(listOfFiles[i].getName());
			try {
				Device device = DeviceInfoFactory.getInfo(Device.class, deviceName);
				list.add(ConnectionProvider.getNewConnectionProvider(device));
			} catch (Exception e) {
				logger.error("Could not load device info of " + deviceName);
			}
		}

		GlobalVariables.setDeviceinfos(list);

		logger.info("-END- ScheduledEvents.loadDeviceInfos");
	}

	@Scheduled(cron = "*/10 * * * * ?")
	public static void loadUsers() throws IOException, JAXBException {
		logger.info("-START- ScheduledEvents.loadUsers");

		File userListFile = new File("Config/User/userList.xml");

		if (!userListFile.exists()) {
			return;
		}

		String userListXml = new String(FileContentUtil.bytes(userListFile));

		if (StringUtils.isEmpty(userListXml)) {
			return;
		}

		UserList userList = JaxbHelper.unmarshal(UserList.class, userListXml);
		GlobalVariables.setUsers(userList.getUser());

		logger.info("-END- ScheduledEvents.loadUsers");
	}
}
