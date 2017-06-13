import static org.junit.Assert.fail;

import org.junit.Assert;
import org.junit.Test;

import com.ves.main.integration.doc.factory.DeviceInfoFactory;
import com.ves.main.integration.jaxb.layout.Device;
import com.ves.main.integration.jaxb.layout.Layout;

public class DeviceInfoTest {
	Device device = null;
	Layout layout = null;

	@Test
	public void getDeviceInfo() {
		try {
			device = DeviceInfoFactory.getInfo(Device.class, "ESM-4450");
			Assert.assertNotNull(device);

			System.out.println(device.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Device info could not be read!");
		}
	}

	@Test
	public void setDeviceInfo() {
		try {
			getDeviceInfo();

			Assert.assertNotNull(device);
			DeviceInfoFactory.saveInfo(device, device.getName());

			System.out.println(device.getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Device info could not be seved!");
		}
	}

	@Test
	public void getLayoutInfo() {
		try {
			layout = DeviceInfoFactory.getInfo(Layout.class, "layout");
			Assert.assertNotNull(layout);

			System.out.println(layout.getDevice().get(0).getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Layout could not be read!");
		}
	}

	@Test
	public void setLayoutInfo() {
		try {
			getLayoutInfo();

			Assert.assertNotNull(layout);
			DeviceInfoFactory.saveInfo(layout, "layout");

			System.out.println(layout.getDevice().get(0).getName());
		} catch (Exception e) {
			e.printStackTrace();
			fail("Layout could not be seved!");
		}
	}
}
