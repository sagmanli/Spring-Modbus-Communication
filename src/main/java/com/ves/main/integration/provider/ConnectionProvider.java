package com.ves.main.integration.provider;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.model.Constants;
import com.ves.main.integration.model.abstracts.ADevice;

public class ConnectionProvider {
	private static final Logger logger = Logger.getLogger(ConnectionProvider.class);

	public static ADevice getNewConnectionProvider(Device device) {
		String connectionType;
		if (device == null) {
			logger.error("-device- is null!");
			return null;
		}
		if (device.getConnectionType() == null || StringUtils.isEmpty(connectionType = device.getConnectionType().getType())) {
			logger.error("-connection_type- is blank!");
			return null;
		}
		if (connectionType.equals(Constants.ConnectionType.MODBUS_SERIAL_RTU)) {
			return new ModbusSerialRTU(device);
		} else if (connectionType.equals(Constants.ConnectionType.MODBUS_TCP_IP)) {
			return new ModbusTcpIP(device);
		}
		return null;
	}
}
