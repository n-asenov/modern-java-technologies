package bg.sofia.uni.fmi.mjt.smartcity.hub;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import bg.sofia.uni.fmi.mjt.smartcity.device.SmartDevice;
import bg.sofia.uni.fmi.mjt.smartcity.enums.DeviceType;

public class SmartCityHub {
	private Set<SmartDevice> devices;

	public SmartCityHub() {
		devices = new LinkedHashSet<SmartDevice>();
	}

	/**
	 * Adds a @device to the SmartCityHub.
	 *
	 * @throws IllegalArgumentException         in case @device is null.
	 * @throws DeviceAlreadyRegisteredException in case the @device is already
	 *                                          registered.
	 */
	public void register(SmartDevice device) throws DeviceAlreadyRegisteredException {
		if (device == null) {
			throw new IllegalArgumentException();
		}

		if (!devices.add(device)) {
			throw new DeviceAlreadyRegisteredException();
		}
	}

	/**
	 * Removes the @device from the SmartCityHub.
	 *
	 * @throws IllegalArgumentException in case null is passed.
	 * @throws DeviceNotFoundException  in case the @device is not found.
	 */
	public void unregister(SmartDevice device) throws DeviceNotFoundException {
		if (device == null) {
			throw new IllegalArgumentException();
		}

		if (!devices.remove(device)) {
			throw new DeviceNotFoundException();
		}
	}

	/**
	 * Returns a SmartDevice with an ID @id.
	 *
	 * @throws IllegalArgumentException in case @id is null.
	 * @throws DeviceNotFoundException  in case device with ID @id is not found.
	 */
	public SmartDevice getDeviceById(String id) throws DeviceNotFoundException {
		if (id == null) {
			throw new IllegalArgumentException();
		}

		for (SmartDevice device : devices) {
			if (device.getId().equals(id)) {
				return device;
			}
		}

		throw new DeviceNotFoundException();
	}

	/**
	 * Returns the total number of devices with type @type registered in
	 * SmartCityHub.
	 *
	 * @throws IllegalArgumentException in case @type is null.
	 */
	public int getDeviceQuantityPerType(DeviceType type) {
		if (type == null) {
			throw new IllegalArgumentException();
		}

		int counter = 0;

		for (SmartDevice device : devices) {
			if (device.getType() == type) {
				counter++;
			}
		}

		return counter;
	}

	/**
	 * Returns a collection of IDs of the top @n devices which consumed the most
	 * power from the time of their installation until now.
	 * 
	 * The total power consumption of a device is calculated by the hours elapsed
	 * between the two LocalDateTime-s multiplied by the stated power consumption of
	 * the device.
	 *
	 * If @n exceeds the total number of devices, return all devices available
	 * sorted by the given criterion.
	 * 
	 * @throws IllegalArgumentException in case @n is a negative number.
	 */
	public Collection<String> getTopNDevicesByPowerConsumption(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		TreeSet<SmartDevice> devicesSortedByTotalPowerConsumpiton = new TreeSet<SmartDevice>(getComparator());
		devicesSortedByTotalPowerConsumpiton.addAll(devices);
		
		int size = n < devices.size() ? n : devices.size();

		List<String> topNDevices = new ArrayList<String>(size);
		Iterator<SmartDevice> iterator = devicesSortedByTotalPowerConsumpiton.iterator();
		
		for (int index = 0; index < size; index++) {
			topNDevices.add(iterator.next().getId());
		}

		return topNDevices;
	}

	/**
	 * Returns a collection of the first @n registered devices, i.e the first @n
	 * that were added in the SmartCityHub (registration != installation).
	 * 
	 * If @n exceeds the total number of devices, return all devices available
	 * sorted by the given criterion.
	 *
	 * @throws IllegalArgumentException in case @n is a negative number.
	 */
	public Collection<SmartDevice> getFirstNDevicesByRegistration(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}

		int size = n < devices.size() ? n : devices.size();

		List<SmartDevice> firstNDevices = new ArrayList<SmartDevice>(size);
		Iterator<SmartDevice> iterator = devices.iterator();

		for (int index = 0; index < size; index++) {
			firstNDevices.add(iterator.next());
		}

		return firstNDevices;
	}

	private Comparator<SmartDevice> getComparator() {
		return new Comparator<SmartDevice>() {
			@Override
			public int compare(SmartDevice firstDevice, SmartDevice secondDevice) {
				LocalDateTime now = LocalDateTime.now();
				double firstDeviceTotalPowerConsumtion = Duration.between(firstDevice.getInstallationDateTime(), now).toHours()
						* firstDevice.getPowerConsumption();
				double secondDeviceTotalPowerConsumtion = Duration.between(secondDevice.getInstallationDateTime(), now).toHours()
						* secondDevice.getPowerConsumption();

				return Double.compare(secondDeviceTotalPowerConsumtion, firstDeviceTotalPowerConsumtion);
			}
		};
	}

}
