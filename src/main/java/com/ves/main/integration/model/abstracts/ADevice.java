package com.ves.main.integration.model.abstracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.jaxb.layout.Pair;
import com.ves.main.integration.jaxb.layout.Property;

public abstract class ADevice {
	final Device device;

	public ADevice(Device device) {
		this.device = device;
	}

	public Object getValue(String propertyKey) {
		for (Property p : getDevice().getProperties().getProperty()) {
			if (propertyKey.equals(p.getType())) {
				for (Pair pair : p.getPair()) {
					if (pair.getKey().equals(p.getSelectedKey())) {
						return pair.getValue();
					}
				}
			}
		}
		for (Pair pair : getDevice().getConnectionType().getPair()) {
			if (propertyKey.equals(pair.getKey())) {
				return pair.getValue();
			}
		}
		return null;
	}

	public Object getKey(String propertyKey) {
		for (Property p : getDevice().getProperties().getProperty()) {
			if (propertyKey.equals(p.getType())) {
				return p.getSelectedKey();
			}
		}
		for (Pair pair : getDevice().getConnectionType().getPair()) {
			if (propertyKey.equals(pair.getKey())) {
				return pair.getKey();
			}
		}
		return null;
	}

	/**
	 * <pre>
	 * 		RESULT
	 * 		|-Anahtar sözcük: Key
	 * 		|-Değer tipi	: NUMBER, STRING, COMBO 
	 * 		+-Değerler		: List<Pair>
	 * </pre>
	 */
	public abstract Object[] getConnectionPropertyDetail(String propertyKey);

	public abstract HashMap<String, Integer> readInputRegisters();

	public abstract HashMap<String, Integer> readHoldingRegisters();

	public abstract boolean testConnection() throws Exception;

	public Device getDevice() {
		return device;
	}

	protected List<Property> getInputRegisters() {
		List<Property> inputRegisters = new ArrayList<Property>();
		for (Property p : getDevice().getProperties().getProperty()) {
			if ("INPUT_REGISTER".equals(p.getAddressType())) {
				inputRegisters.add(p);
			}
		}
		return inputRegisters;
	}

	protected List<Property> getHoldingRegisters() {
		List<Property> holdingRegisters = new ArrayList<Property>();
		for (Property p : getDevice().getProperties().getProperty()) {
			if ("HOLDING_REGISTER".equals(p.getAddressType())) {
				holdingRegisters.add(p);
			}
		}
		return holdingRegisters;
	}

	public void setValue(final String propertyKey, final Object propertyValue) {
		for (Property property : getDevice().getProperties().getProperty()) {
			if (property.getType().equals(propertyKey)) {
				property.setSelectedKey(propertyValue + "");
				return;
			}
		}
		for (Pair pair : getDevice().getConnectionType().getPair()) {
			if (pair.getKey().equals(propertyKey)) {
				pair.setValue(propertyValue + "");
				return;
			}
		}
		getDevice().getConnectionType().getPair().add(new Pair() {
			{
				setKey(propertyKey);
				setValue(propertyValue + "");
			}
		});
	}
}
