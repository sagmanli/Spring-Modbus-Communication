package com.ves.main;

import java.util.ArrayList;
import java.util.List;

import com.ves.main.integration.jaxb.user.userlist.User;
import com.ves.main.integration.model.abstracts.ADevice;
import com.ves.main.util.ObjectUtil;

/**
 * !!!!! - Önemli not! Bütün global değişkenler için yazılan methodlar burada olacak ve "syncronized" bloğu içine "lock"lar konulacak!
 */
public class GlobalVariables {
	private static final List<ADevice> deviceInfos    = new ArrayList<ADevice>();
	private static final List<ADevice> devices        = new ArrayList<ADevice>();
	private static final List<User>    users          = new ArrayList<User>();

	private static final Object        lockDeviceInfo = new Object();
	private static final Object        lockDevices    = new Object();
	private static final Object        lockUsers      = new Object();

	/* +++++++++++++++DeviceInfo++++++++++++++ */
	public static ADevice getDeviceinfoByName(String deviceName) {
		synchronized (lockDeviceInfo) {
			for (ADevice tmpADevice : deviceInfos) {
				if (tmpADevice.getDevice().getName().equals(deviceName)) {
					return ObjectUtil.clone(tmpADevice);
				}
			}

			return null;
		}
	}

	public static List<ADevice> getDeviceInfos() {
		synchronized (lockDeviceInfo) {
			return ObjectUtil.clone(deviceInfos);
		}
	}

	public static void setDeviceinfos(List<ADevice> infos) {
		synchronized (lockDeviceInfo) {
			deviceInfos.clear();
			deviceInfos.addAll(infos);
		}
	}

	/* ---------------DeviceInfo-------------- */

	/* +++++++++++++++++Devices+++++++++++++++ */
	public static ADevice getConfiguredDeviceById(String deviceId) {
		synchronized (lockDevices) {
			for (ADevice device : devices) {
				if (deviceId.equals(device.getDevice().getId()))
					return ObjectUtil.clone(device);
			}
			return null;
		}
	}

	public static void setDevices(List<ADevice> devices) {
		synchronized (lockDevices) {
			devices.clear();
			devices.addAll(devices);
		}
	}

	public static void addDevice(ADevice device) {
		synchronized (lockDevices) {
			devices.add(device);
		}
	}

	public static List<ADevice> getDevices() {
		synchronized (lockDevices) {
			return ObjectUtil.clone(devices);
		}
	}

	/* -----------------Devices--------------- */
	/* ++++++++++++++++++Users++++++++++++++++ */

	public static void setUsers(List<User> userlist) {
		synchronized (lockUsers) {
			users.clear();
			users.addAll(userlist);
		}
	}

	public static List<User> getUsers() {
		synchronized (lockUsers) {
			return ObjectUtil.clone(users);
		}
	}

	/* ------------------Users---------------- */
}
