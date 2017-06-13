package com.ves.main.util;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JaxbHelper {
	public static String marshal(Object object) throws IOException, JAXBException {
		if (object == null) {
			return null;
		}

		StringWriter writer = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(object.getClass());
		Marshaller m = context.createMarshaller();
		m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
		m.marshal(object, writer);
		writer.close();

		return writer.toString();
	}

	@SuppressWarnings("unchecked")
	public static <T> T unmarshal(Class<T> klass, Object obj) throws JAXBException, IOException {
		String xmlString;
		if (obj instanceof File) {
			File file = (File) obj;
			xmlString = new String(FileContentUtil.bytes(file));
		} else {
			xmlString = (String) obj;
		}

		if (xmlString == null || xmlString.isEmpty()) {
			return null;
		}

		JAXBContext jaxbContext = JAXBContext.newInstance(klass);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xmlString);
		return (T) unmarshaller.unmarshal(reader);
	}
}
