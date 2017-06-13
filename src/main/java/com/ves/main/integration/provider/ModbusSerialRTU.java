package com.ves.main.integration.provider;

import gnu.io.CommDriver;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;

import net.wimpi.modbus.ModbusCoupler;
import net.wimpi.modbus.io.ModbusSerialTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.SerialConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;
import net.wimpi.modbus.util.SerialParameters;

import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.jaxb.layout.Pair;
import com.ves.main.integration.jaxb.layout.Property;
import com.ves.main.integration.jaxb.layout.ValueType;
import com.ves.main.integration.model.Constants;
import com.ves.main.integration.model.abstracts.ADevice;

public class ModbusSerialRTU extends ADevice {

	private static final int    MASTER_UNIT_ID = 0;
	private static final String RTU            = "rtu";

	public ModbusSerialRTU(Device parent) {
		super(parent);
	}

	@Override
	public void setValue(String propertyKey, Object propertyValue) {/*
																	 * The important instances of the classes mentioned before
																	 */
		super.setValue(propertyKey, propertyValue);

		String registerKey = null;
		for (Property p : getDevice().getProperties().getProperty()) {
			if (propertyKey.equals(p.getType())) {
				registerKey = p.getAddress();
				break;
			}
		}
		if (registerKey == null) {
			return;
		}
		SerialConnection con = null; // the connection
		ModbusSerialTransaction trans = null; // the transaction
		WriteSingleRegisterRequest req = null; // the request
		WriteSingleRegisterResponse res = null; // the response

		/* Variables for storing the parameters */
		// String portname = null; // the name of the serial port to be used
		int ref = 0; // the reference, where to start reading from
		// int repeat = 1; // a loop for repeating the transaction
		Register reg = null;

		try {

			String portName = (String) getValue("COM_PORT");
			int baudRate = Integer.parseInt((String) getKey("BAUD_RATE"));
			int parity = Integer.parseInt((String) getValue("PARITY"));
			int stopBits = Integer.parseInt((String) getValue("STOP_BIT"));
			int dataBits = Integer.parseInt((String) getValue("DATA_BITS"));
			int unitid = Integer.parseInt((String) getValue("UNIT_ID"));

			String driverName = "gnu.io.RXTXCommDriver";
			CommDriver commdriver = (CommDriver) Class.forName(driverName).newInstance();
			commdriver.initialize();

			CommPortIdentifier.addPortName(portName, CommPortIdentifier.PORT_SERIAL, commdriver);
			CommPortIdentifier.getPortIdentifiers();
			ref = Integer.parseInt(registerKey) - 40001;
			// 2. Set master identifier
			ModbusCoupler coupler = (ModbusCoupler) ModbusCoupler.getReference();
			coupler.setUnitID(MASTER_UNIT_ID);
			coupler.setMaster(true);

			// 3. Setup serial parameters
			SerialParameters params = new SerialParameters();
			params.setPortName(portName);
			params.setBaudRate(baudRate);
			params.setDatabits(dataBits);
			params.setParity(parity);
			params.setStopbits(stopBits);
			params.setEncoding(RTU);
			params.setEcho(false);

			// 4. Open the connection
			con = new SerialConnection(params);
			waitForPort(portName);
			con.open();

			// 5. Prepare a request
			reg = new SimpleRegister(Integer.parseInt(propertyValue + ""));
			req = new WriteSingleRegisterRequest(ref, reg);
			req.setUnitID(unitid);
			req.setHeadless();

			// 6. Prepare a transaction
			trans = new ModbusSerialTransaction(con);
			trans.setRequest(req);
			trans.setTransDelayMS(100);

			// int k = 0;
			// do {
			trans.execute();
			res = (WriteSingleRegisterResponse) trans.getResponse();
			System.out.println("Response= " + res.getHexMessage());
			// k++;
			// } while (k < repeat);

			// 8. Close the connection
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			con.close();
		}
	}

	@Override
	public Object[] getConnectionPropertyDetail(String propertyKey) {
		if (propertyKey.equals("COM_PORT")) {
			return new Object[] { propertyKey, ValueType.COMBO, getComPortIdentifiers() };
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public static List<Pair> getComPortIdentifiers() {
		List<Pair> pairList = new ArrayList<Pair>();

		CommPortIdentifier cpi = null;
		Enumeration e = CommPortIdentifier.getPortIdentifiers();
		while (e.hasMoreElements()) {
			try {
				cpi = (CommPortIdentifier) e.nextElement();
			} catch (NoSuchElementException n) {

			}
			Pair pair = new Pair();
			pair.setKey(cpi.getName());
			pairList.add(pair);
		}
		// pairList.add(new Pair() {
		// {
		// setKey("COM3");
		// }
		// });

		return pairList;
	}

	public boolean testConnection() {
		/* The important instances of the classes mentioned before */
		SerialConnection con = null; // the connection
		ModbusSerialTransaction trans = null;
		ReadInputRegistersRequest req = null;

		try {
			String portName = (String) getValue("COM_PORT");
			int baudRate = Integer.parseInt((String) getKey("BAUD_RATE"));
			int parity = Integer.parseInt((String) getValue("PARITY"));
			int stopBits = Integer.parseInt((String) getValue("STOP_BIT"));
			int dataBits = Integer.parseInt((String) getValue("DATA_BITS"));
			int unitid = Integer.parseInt((String) getValue("UNIT_ID"));
			String driverName = "gnu.io.RXTXCommDriver";
			CommDriver commdriver = (CommDriver) Class.forName(driverName).newInstance();
			commdriver.initialize();
			CommPortIdentifier.addPortName(portName, CommPortIdentifier.PORT_SERIAL, commdriver);
			CommPortIdentifier.getPortIdentifiers();
			// 2. Set master identifier
			ModbusCoupler coupler = (ModbusCoupler) ModbusCoupler.getReference();
			coupler.setUnitID(MASTER_UNIT_ID);
			coupler.setMaster(true);

			// 3. Setup serial parameters
			SerialParameters params = new SerialParameters();
			params.setPortName(portName);
			params.setBaudRate(baudRate);
			params.setDatabits(dataBits);
			params.setParity(parity);
			params.setStopbits(stopBits);
			params.setEncoding(RTU);
			params.setEcho(false);

			// 4. Open the connection
			con = new SerialConnection(params);

			con.open();

			Integer ref = null;

			for (Property property : getDevice().getProperties().getProperty()) {
				if (property.getAddressType().equals("INPUT_REGISTER")) {
					ref = Integer.parseInt(property.getAddress());

					break;
				}
			}

			req = new ReadInputRegistersRequest(ref - 30001, 1);
			req.setUnitID(unitid);

			trans = new ModbusSerialTransaction(con);
			trans.setCheckingValidity(false);

			trans.setRequest(req);
			trans.execute();

			return true;
			// 8. Close the connection
		} catch (Throwable ex) {
			ex.printStackTrace();
			return false;
		} finally {
			con.close();
		}
	}// main

	@Override
	public HashMap<String, Integer> readInputRegisters() {
		HashMap<String, Integer> inputRegisters = new HashMap<String, Integer>();

		SerialConnection con = null;
		ModbusSerialTransaction trans = null;
		ReadInputRegistersRequest req = null;
		ReadInputRegistersResponse res = null;

		try {
			String portName = (String) getValue("COM_PORT");
			int baudRate = Integer.parseInt((String) getKey("BAUD_RATE"));
			int parity = Integer.parseInt((String) getValue("PARITY"));
			int stopBits = Integer.parseInt((String) getValue("STOP_BIT"));
			int dataBits = Integer.parseInt((String) getValue("DATA_BITS"));
			int unitid = Integer.parseInt((String) getValue("UNIT_ID"));
			String driverName = "gnu.io.RXTXCommDriver";
			CommDriver commdriver = (CommDriver) Class.forName(driverName).newInstance();
			commdriver.initialize();
			CommPortIdentifier.addPortName(getComPort(), CommPortIdentifier.PORT_SERIAL, commdriver);

			ModbusCoupler coupler = (ModbusCoupler) ModbusCoupler.getReference();
			coupler.setUnitID(MASTER_UNIT_ID);
			coupler.setMaster(true);

			SerialParameters params = new SerialParameters();
			params.setPortName(portName);
			params.setBaudRate(baudRate);
			params.setDatabits(dataBits);
			params.setParity(parity);
			params.setStopbits(stopBits);
			params.setEncoding(RTU);
			params.setEcho(false);

			con = new SerialConnection(params);
			waitForPort(portName);
			con.open();

			req = new ReadInputRegistersRequest(0, 1);
			req.setUnitID(unitid);
			// req.setHeadless();

			trans = new ModbusSerialTransaction(con);
			trans.setCheckingValidity(false);

			for (Property p : getInputRegisters()) {
				try {
					req.setReference(Integer.valueOf(p.getAddress()) - 30001);
					trans.setRequest(req);
					trans.execute();

					res = (ReadInputRegistersResponse) trans.getResponse();
					// System.out.println("Function code: " +
					// res.getFunctionCode());
					// System.out.print("Register: " + (p.getAddress()));
					// System.out.println(" Value: " + res.getRegisterValue(0));

					inputRegisters.put(p.getType(), res.getRegisterValue(0));

				} catch (Exception e) {
					System.out.println("N-Register: " + (p.getAddress()));

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			con.close();
		}
		return inputRegisters;
	}

	@Override
	public HashMap<String, Integer> readHoldingRegisters() {
		HashMap<String, Integer> holdingRegisters = new HashMap<String, Integer>();

		SerialConnection con = null;
		ModbusSerialTransaction trans = null;
		ReadMultipleRegistersRequest req = null;
		ReadMultipleRegistersResponse res = null;

		try {
			String portName = (String) getValue("COM_PORT");
			int baudRate = Integer.parseInt((String) getKey("BAUD_RATE"));
			int parity = Integer.parseInt((String) getValue("PARITY"));
			int stopBits = Integer.parseInt((String) getValue("STOP_BIT"));
			int dataBits = Integer.parseInt((String) getValue("DATA_BITS"));
			int unitid = Integer.parseInt((String) getValue("UNIT_ID"));
			String driverName = "gnu.io.RXTXCommDriver";
			CommDriver commdriver = (CommDriver) Class.forName(driverName).newInstance();
			commdriver.initialize();
			CommPortIdentifier.addPortName(getComPort(), CommPortIdentifier.PORT_SERIAL, commdriver);

			ModbusCoupler coupler = (ModbusCoupler) ModbusCoupler.getReference();
			coupler.setUnitID(MASTER_UNIT_ID);
			coupler.setMaster(true);

			SerialParameters params = new SerialParameters();
			params.setPortName(portName);
			params.setBaudRate(baudRate);
			params.setDatabits(dataBits);
			params.setParity(parity);
			params.setStopbits(stopBits);
			params.setEncoding(RTU);
			params.setEcho(false);

			con = new SerialConnection(params);
			waitForPort(portName);
			con.open();

			req = new ReadMultipleRegistersRequest(0, 1);
			req.setUnitID(unitid);
			// req.setHeadless();

			trans = new ModbusSerialTransaction(con);
			trans.setCheckingValidity(false);

			for (Property p : getHoldingRegisters()) {
				try {
					req.setReference(Integer.valueOf(p.getAddress()) - Constants.RegisterOffset.HOLDING_REGISTER);
					trans.setRequest(req);
					trans.execute();

					res = (ReadMultipleRegistersResponse) trans.getResponse();
					// System.out.println("Function code: " +
					// res.getFunctionCode());
					// System.out.print("Register: " + (p.getAddress()));
					// System.out.println(" Value: " + res.getRegisterValue(0));

					holdingRegisters.put(p.getType(), res.getRegisterValue(0));

				} catch (Exception e) {
					System.out.println("N-Register: " + (p.getAddress()));

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			con.close();
		}
		return holdingRegisters;
	}

	public static void waitForPort(String portNumber) throws NoSuchPortException {
		CommPortIdentifier comPort = CommPortIdentifier.getPortIdentifier(portNumber);
		while (comPort.isCurrentlyOwned()) {
			System.out.println("Port " + portNumber + " in use");
		}
	}

	private String getComPort() {
		return getDevice().getConnectionType().getPair().get(0).getValue();
	}
}
