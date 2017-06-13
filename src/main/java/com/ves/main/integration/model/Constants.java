package com.ves.main.integration.model;

public class Constants {
	public static class ConnectionType {
		public static final String MODBUS_TCP_IP     = "MODBUS_TCP_IP";
		public static final String MODBUS_SERIAL_RTU = "MODBUS_SERIAL_RTU";
	}

	public static class RegisterOffset {
		public static final Integer INPUT_REGISTER   = 30001;
		public static final Integer HOLDING_REGISTER = 40001;
	}
}
