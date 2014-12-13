package up.ndsg.join;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.net.wifi.p2p.WifiP2pManager.PeerListListener;

public class PeerListFragment extends ListFragment implements PeerListListener {

    private List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    private WifiP2pDevice device;
    ProgressDialog progressDialog;
    View mContentView;
    public final static String EXTRA_MESSAGE = "up.ndsg.join.MESSAGE";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.setListAdapter(new WiFiPeerListAdapter(getActivity(), R.layout.row_peers, peers));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(R.layout.list_peers, null);
        this.setListAdapter(new WiFiPeerListAdapter(getActivity(), R.layout.row_peers, peers));
        return mContentView;
    }

    @Override
    public void onPeersAvailable(WifiP2pDeviceList peerList) {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        peers.clear();
        peers.addAll(peerList.getDeviceList());
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
        if (peers.size() == 0) {
            Toast.makeText(getActivity(),"No device found", Toast.LENGTH_SHORT).show();
            Log.d(PeersListActivity.TAG, "No devices found");
            return;
        }

    }

    public class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {
        private List<WifiP2pDevice> items;

        public WiFiPeerListAdapter(Context context, int resource, List<WifiP2pDevice> items) {
            super(context, resource, items);
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent ) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row_peers, null);
            }
            WifiP2pDevice device = items.get(position);
            if(device != null) {
                TextView top = (TextView) v.findViewById(R.id.device_name);
                TextView bottom = (TextView) v.findViewById(R.id.device_details);
//                Toast.makeText(getContext(), "" + top + " " + bottom, Toast.LENGTH_LONG).show();
                Log.d("PEER ADAPTER", "" + device.deviceName);
                if(top != null) {
                    top.setText(device.deviceName);
                }
                if(bottom != null) {
                    bottom.setText(getDeviceStatus(device.status));
                }
            }

            return v;
        }

    }

    private static String getDeviceStatus(int deviceStatus) {
        switch (deviceStatus) {
            case WifiP2pDevice.AVAILABLE:
                return "Available";
            case WifiP2pDevice.INVITED:
                return "Invited";
            case WifiP2pDevice.CONNECTED:
                return "Connected";
            case WifiP2pDevice.FAILED:
                return "Failed";
            case WifiP2pDevice.UNAVAILABLE:
                return "Unavailable";
            default:
                return "Unknown";

        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        WifiP2pDevice device = (WifiP2pDevice) getListAdapter().getItem(position);
        Toast.makeText(getActivity(), device.deviceName, Toast.LENGTH_SHORT).show();

        //CONNECT
        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        progressDialog = ProgressDialog.show(getActivity(), "Press back to cancel", "Connecting to :"
                + device.deviceAddress, true, true);
        ((DeviceActionListener) getActivity()).connect(config);

        Intent intent = new Intent(getActivity(), DisplayGroupPeers.class);
        intent.putExtra(EXTRA_MESSAGE, device.deviceName);
        startActivity(intent);
    }

    public void onInitiateDiscovery() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        progressDialog = ProgressDialog.show(getActivity(),
                "Press back to cancel",
                "Finding peers...",
                true,
                true,
                new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                });
    }

    public void clearPeers() {
        peers.clear();
        ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
    }

    public void updateThisDevice(WifiP2pDevice device){
        this.device = device;
        Toast.makeText(this.getActivity(), device.deviceName, Toast.LENGTH_SHORT).show();
        ImageButton view = (ImageButton) mContentView.findViewById(R.id.image_button_status);
        view.setImageResource(R.drawable.active_circle);
    }
}