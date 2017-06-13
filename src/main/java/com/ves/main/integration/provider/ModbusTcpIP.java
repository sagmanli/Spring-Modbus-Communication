package com.ves.main.integration.provider;

import java.net.InetAddress;
import java.util.HashMap;

import net.wimpi.modbus.Modbus;
import net.wimpi.modbus.io.ModbusTCPTransaction;
import net.wimpi.modbus.msg.ReadInputRegistersRequest;
import net.wimpi.modbus.msg.ReadInputRegistersResponse;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteSingleRegisterRequest;
import net.wimpi.modbus.msg.WriteSingleRegisterResponse;
import net.wimpi.modbus.net.TCPMasterConnection;
import net.wimpi.modbus.procimg.Register;
import net.wimpi.modbus.procimg.SimpleRegister;

import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.jaxb.layout.Property;
import com.ves.main.integration.model.abstracts.ADevice;

public class ModbusTcpIP extends ADevice {

	public ModbusTcpIP(Device parent) {
		super(parent);
	}

	@Override
	public Object[] getConnectionPropertyDetail(String propertyKey) {
		// if () {
		// }
		return null;
	}

	@Override
	public boolean testConnection() throws Exception {
		TCPMasterConnection con = null; // the connection
		ModbusTCPTransaction trans = null; // the transaction
		ReadInputRegistersRequest req = null; // the request
		ReadInputRegistersResponse res = null; // the response

		/* Variables for storing the parameters */
		InetAddress addr = null; // the slave's address

		if ((String) getValue("PORT") == null || ((String) getValue("PORT")).isEmpty()) {
			throw new Exception();
		}
		if ((String) getValue("URL") == null || ((String) getValue("URL")).isEmpty()) {
			throw new Exception();
		}
		int port = Integer.parseInt((String) getValue("PORT"));

		Integer ref = null;

		for (Property property : getDevice().getProperties().getProperty()) {
			if (property.getAddressType().equals("INPUT_REGISTER")) {
				ref = Integer.parseInt(property.getAddress());
			}
		}

		int unitid = 0; // the unit identifier we will be talking to
		// the reference, where to start reading from
		int count = 1; // the count of IR's to read

		try {
			unitid = 1;
			ref = 0;

			addr = InetAddress.getByName((String) getValue("URL"));

			con = new TCPMasterConnection(addr);
			con.setPort(port);
			con.connect();

			// 5. Prepare a request
			req = new ReadInputRegistersRequest(ref, count);
			req.setUnitID(unitid);
			// req.setHeadless();

			// 6. Prepare a transaction
			trans = new ModbusTCPTransaction(con);
			trans.setRequest(req);

			req.setReference(ref);
			trans.setRequest(req);
			trans.execute();

			res = (ReadInputRegistersResponse) trans.getResponse();
			System.out.println("Function code: " + res.getFunctionCode());
			System.out.print("Ref: " + (ref - 1));
			System.out.println(" Mod: " + res.getRegisterValue(0));
		} catch (Exception e) {
			return false;
		} finally {
			con.close();
		}
		return true;
	}

	@Override
	public HashMap<String, Integer> readInputRegisters() {
		HashMap<String, Integer> inputRegisters = new HashMap<String, Integer>();

		/* The important instances of the classes mentioned before */
		TCPMasterConnection con = null; // the connection
		ModbusTCPTransaction trans = null; // the transaction
		ReadInputRegistersRequest req = null; // the request
		ReadInputRegistersResponse res = null; // the response

		/* Variables for storing the parameters */
		InetAddress addr = null; // the slave's address
		int port = Modbus.DEFAULT_PORT;

		int unitid = 0; // the unit identifier we will be talking to
		int ref = 0; // the reference, where to start reading from
		int count = 1; // the count of IR's to read

		try {
			unitid = 1;

			addr = InetAddress.getByName((String) getValue("URL"));
			port = Integer.parseInt((String) getValue("PORT"));

			con = new TCPMasterConnection(addr);
			con.setPort(port);
			con.connect();

			// 5. Prepare a request
			req = new ReadInputRegistersRequest(ref, count);
			req.setUnitID(unitid);
			// req.setHeadless();

			// 6. Prepare a transaction
			trans = new ModbusTCPTransaction(con);
			trans.setRequest(req);

			for (Property p : getInputRegisters()) {
				try {
					req.setReference(Integer.valueOf(p.getAddress()) - 1);
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

		/* The important instances of the classes mentioned before */
		TCPMasterConnection con = null; // the connection
		ModbusTCPTransaction trans = null; // the transaction
		ReadMultipleRegistersRequest req = null;
		ReadMultipleRegistersResponse res = null;

		/* Variables for storing the parameters */
		InetAddress addr = null; // the slave's address
		int port = Modbus.DEFAULT_PORT;

		int unitid = 0; // the unit identifier we will be talking to
		int ref = 0; // the reference, where to start reading from
		int count = 1; // the count of IR's to read

		try {
			unitid = 1;

			addr = InetAddress.getByName((String) getValue("URL"));
			port = Integer.parseInt((String) getValue("PORT"));

			con = new TCPMasterConnection(addr);
			con.setPort(port);
			con.connect();

			// 5. Prepare a request
			req = new ReadMultipleRegistersRequest(ref, count);
			req.setUnitID(unitid);
			// req.setHeadless();

			// 6. Prepare a transaction
			trans = new ModbusTCPTransaction(con);
			trans.setRequest(req);

			for (Property p : getHoldingRegisters()) {
				try {
					req.setReference(Integer.valueOf(p.getAddress()) - 1);
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

		TCPMasterConnection con = null; // the connection
		ModbusTCPTransaction trans = null; // the transaction
		WriteSingleRegisterRequest req = null; // the request
		WriteSingleRegisterResponse res = null; // the response

		/* Variables for storing the parameters */
		InetAddress addr = null; // the slave's address

		/* Variables for storing the parameters */
		// String portname = null; // the name of the serial port to be used
		int unitid = 0; // the unit identifier we will be talking to
		int ref = 0; // the reference, where to start reading from
		// int repeat = 1; // a loop for repeating the transaction
		Register reg = null;

		try {

			addr = InetAddress.getByName((String) getValue("URL"));
			int port = Integer.parseInt((String) getValue("PORT"));

			con = new TCPMasterConnection(addr);
			con.setPort(port);
			con.connect();
			ref = Integer.parseInt(registerKey) - 1;

			// 5. Prepare a request
			reg = new SimpleRegister(Integer.parseInt(propertyValue + ""));
			req = new WriteSingleRegisterRequest(ref, reg);
			req.setUnitID(unitid);

			// 6. Prepare a transaction
			trans = new ModbusTCPTransaction(con);
			trans.setRequest(req);

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

}
