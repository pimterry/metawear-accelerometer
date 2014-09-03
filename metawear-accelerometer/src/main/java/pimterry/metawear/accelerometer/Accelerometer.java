package pimterry.metawear.accelerometer;

import android.app.Activity;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import static android.view.View.OnClickListener;


public class Accelerometer extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MetawearManager metawearManager = new MetawearManager((BluetoothManager) this.getSystemService(Context.BLUETOOTH_SERVICE));

        setContentView(R.layout.activity_accelerometer);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                                .add(R.id.container, new PlaceholderFragment(metawearManager))
                                .commit();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accelerometer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements MetawearDeviceFoundListener {

        private final MetawearManager metawearManager;

        private TextView statusText;
        private Button scanButton;

        public PlaceholderFragment(MetawearManager metawearManager) {
            this.metawearManager = metawearManager;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_accelerometer, container, false);

            scanButton = (Button) rootView.findViewById(R.id.scanButton);
            scanButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    toggleScanningForDevices();
                }
            });
            metawearManager.setMetawearDeviceFoundListener(this);

            statusText = (TextView) rootView.findViewById(R.id.statusText);

            return rootView;
        }

        private void toggleScanningForDevices() {
            if (metawearManager.isScanning()) {
                metawearManager.stopScanningForDevices();
                scanButton.setText("Scan");
                statusText.setText("Stopped");
            } else {
                metawearManager.scanForDevices();
                statusText.setText("Scanning...");
                scanButton.setText("Stop");
            }
        }

        @Override
        public void deviceFound(final BluetoothDevice device) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    statusText.setText(device.getName());
                }
            });
        }
    }
}
