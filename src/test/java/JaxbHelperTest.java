import static org.junit.Assert.fail;

import org.junit.Test;
import org.springframework.util.Assert;

import com.ves.main.Application;
import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.util.FileContentUtil;
import com.ves.main.util.JaxbHelper;

public class JaxbHelperTest {

	@Test
	public void marshalAndUnmarshal() {
		try {
			Device device = JaxbHelper.unmarshal(Device.class, new String(FileContentUtil.bytes(Application.class.getResourceAsStream("integration/xml/device/ESM-4450.xml"))));

			Assert.notNull(device);
			System.out.println("Device Name: \n" + device.getName() + "\n");

			try {
				String xmlString = JaxbHelper.marshal(device);

				Assert.notNull(xmlString);
				Assert.isTrue(!xmlString.isEmpty());

				System.out.println("XML String: \n" + xmlString);
			} catch (Exception e) {
				fail("Could not be converted to XML!");
			}
		} catch (Exception e) {
			fail("Could not read device info!");
		}
	}
}
