package com.ves.main.integration.doc.factory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.springframework.util.StringUtils;

import com.ves.main.Application;
import com.ves.main.GlobalVariables;
import com.ves.main.integration.exception.ComPrototypeException;
import com.ves.main.integration.jaxb.layout.ConnectionType;
import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.jaxb.layout.Layout;
import com.ves.main.integration.jaxb.layout.Pair;
import com.ves.main.integration.jaxb.layout.Property;
import com.ves.main.integration.jaxb.layout.ValueType;
import com.ves.main.integration.model.abstracts.ADevice;
import com.ves.main.util.FileContentUtil;
import com.ves.main.util.JaxbHelper;

public class DeviceInfoFactory {
	// private static final Logger logger = Logger.getLogger(DeviceInfoFactory.class);

	private static final String devicePathPattern        = "Config/DeviceInfo/{0}.xml";
	private static final String layoutPathPattern        = "Config/Layout/{0}.xml";
	private static final String connectionXmlPathPattern = "integration/xml/connection_type/{0}.xml";

	public static <T> T getInfo(Class<T> klass, String deviceName) throws JAXBException, IOException, ComPrototypeException {
		String pathPattern = getPattern(klass);

		return JaxbHelper.unmarshal(klass, new File(MessageFormat.format(pathPattern, deviceName)));
	}

	public static void saveInfo(Object obj, String name) throws JAXBException, IOException, ComPrototypeException {
		String pathPattern = getPattern(obj.getClass());
		FileWriter fw = new FileWriter(MessageFormat.format(pathPattern, name));
		fw.write(JaxbHelper.marshal(obj));
		fw.close();
	}

	private static String getPattern(Class<?> klass) throws ComPrototypeException {
		String pathPattern = null;

		if (klass == Device.class) {
			pathPattern = devicePathPattern;
		} else if (klass == Layout.class) {
			pathPattern = layoutPathPattern;
		}
		if (pathPattern == null) {
			throw new ComPrototypeException("Unsupported object type!");
		}
		return pathPattern;
	}

	public static List<Object[]> getDeviceConnectionProperties(String deviceName) throws IOException, JAXBException {
		if (StringUtils.isEmpty(deviceName)) {
			return null;
		}

		ADevice aDevice = GlobalVariables.getDeviceinfoByName(deviceName);

		if (aDevice == null) {
			return null;
		}

		Device device = aDevice.getDevice();

		List<Object[]> resultList = new LinkedList<Object[]>();

		List<Property> properties = device.getProperties().getProperty();
		for (Iterator<Property> iterator = properties.iterator(); iterator.hasNext();) {
			Property property = (Property) iterator.next();
			if (StringUtils.isEmpty(property.getCategory()) || !property.getCategory().equals("CONNECTION")) {
				iterator.remove();
			}
		}

		for (Property property : properties) {
			resultList.add(new Object[] { property.getType(), property.getMaxValue() == null && property.getMinValue() == null ? ValueType.COMBO : ValueType.NUMBER, property.getPair() });
		}

		final String CONNECTION_TYPE = device.getConnectionType().getType();
		URL url = Application.class.getResource(MessageFormat.format(connectionXmlPathPattern, CONNECTION_TYPE));

		if (url == null) {
			return resultList;
		}

		byte[] bytes = FileContentUtil.bytes(Application.class.getResourceAsStream(MessageFormat.format(connectionXmlPathPattern, CONNECTION_TYPE)));
		String connectionXml = new String(bytes);
		ConnectionType connectionType = JaxbHelper.unmarshal(ConnectionType.class, connectionXml);

		String propertyKey;

		for (Pair pair : connectionType.getPair()) {
			propertyKey = pair.getKey();
			if (!StringUtils.isEmpty(propertyKey)) {
				Object[] detail;
				if (pair.getValueType() == ValueType.COMBO) {
					detail = aDevice.getConnectionPropertyDetail(propertyKey);
				} else {
					detail = new Object[] { propertyKey, pair.getValueType(), pair.getValueType() == ValueType.NUMBER ? "0" : "" };
				}
				if (detail != null) {
					resultList.add(detail);
				}
			}
		}

		return resultList;
	}
}
