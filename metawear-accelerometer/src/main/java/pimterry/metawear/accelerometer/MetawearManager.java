package pimterry.metawear.accelerometer;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;

public class MetawearManager {

    private final BluetoothAdapter bluetoothAdapter;
    private MetawearDeviceFoundListener deviceFoundListener;

    private BluetoothAdapter.LeScanCallback scanningCallback;

    public MetawearManager(BluetoothManager bluetoothManager) {
        this.bluetoothAdapter = bluetoothManager.getAdapter();
    }

    public void scanForDevices() {
        scanningCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                if (device != null && device.getName().equals("MetaWear")) {
                    deviceFoundListener.deviceFound(device);
                }
            }
        };

        bluetoothAdapter.startLeScan(scanningCallback);
    }

    public void stopScanningForDevices() {
        bluetoothAdapter.stopLeScan(scanningCallback);
        scanningCallback = null;
    }

    public void setMetawearDeviceFoundListener(MetawearDeviceFoundListener listener) {
        this.deviceFoundListener = listener;
    }

    public boolean isScanning() {
        return scanningCallback != null;
    }
}
