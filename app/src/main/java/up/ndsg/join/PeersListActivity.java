package up.ndsg.join;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static android.net.wifi.p2p.WifiP2pManager.Channel;
import static android.net.wifi.p2p.WifiP2pManager.ChannelListener;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION;
import static android.net.wifi.p2p.WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION;
import static android.os.Build.MANUFACTURER;
import static android.os.Build.MODEL;
import static android.view.View.OnCreateContextMenuListener;


public class PeersListActivity extends Activity implements OnCreateContextMenuListener,
                                                           ChannelListener,
                                                           DeviceActionListener {

    public static final String TAG = "Join";
    private final IntentFilter intentFilter = new IntentFilter();
    private WifiP2pManager manager;
    private Channel channel;
    public BroadcastReceiver receiver;
    private boolean isP2PEnabled;

    @Override
    public void onResume() {
        super.onResume();
        if (isWifiDirectSupported()){
            receiver = new NetworkManager(manager, channel, this);
            registerReceiver(receiver, intentFilter);
        }
        else {
            Toast.makeText(this, "Device not supported.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peers_list);

        intentFilter.addAction(WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        TextView device_name = (TextView) findViewById(R.id.textview_device_name);
        TextView device_model = (TextView) findViewById(R.id.textview_device_model);
        ImageButton status = (ImageButton) findViewById(R.id.image_button_status);

        device_name.setText(MANUFACTURER);
        device_model.setText(MODEL);
        status.setImageResource(R.drawable.active_circle);

        manager = (WifiP2pManager) getApplicationContext().getSystemService(Context.WIFI_P2P_SERVICE);
        channel = manager.initialize(this, getMainLooper(), null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_peers_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onChannelDisconnected() {

    }

    @Override
    public void showDetails(WifiP2pDevice device) {

    }

    @Override
    public void cancelDisconnect() {

    }

    @Override
    public void connect(WifiP2pConfig config) {
        manager.connect(channel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(PeersListActivity.this,"Device Invited",Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onFailure(int i) {
                Toast.makeText(PeersListActivity.this, "Connect failed. Retry.", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void disconnect() {

    }

    public void resetData() {
//        PeerListFragment fragmentList = (PeerListFragment) getFragmentManager()
//                .findFragmentById(R.id.fragment_peer_list);
//        PeerDetailFragment fragmentDetails = (PeerDetailFragment) getFragmentManager()
//                .findFragmentById(R.id.fragment_peer_detail);
//
//        if (fragmentList != null) {
//            fragmentList.clearPeers();
//        }
//        if (fragmentDetails != null) {
//            fragmentDetails.resetViews();
//        }
    }

    public void setP2PEnabled(boolean p2pEnabled){
        this.isP2PEnabled = p2pEnabled;
        ImageButton imageButton = (ImageButton) findViewById(R.id.image_button_status);
        imageButton.setImageResource(R.drawable.active_circle);
    }

    public void scanForPeers(View view) {
        final PeerListFragment fragment;

        if (!this.isP2PEnabled) {
            if (manager != null && channel != null) {
                startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
            }
            else {
                Log.e(TAG, "channel or manager is null");
            }
            return;
        }

        fragment = (PeerListFragment) getFragmentManager().findFragmentById(R.id.fragment_peer_list);
        fragment.onInitiateDiscovery();
        manager.discoverPeers(channel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                // Network Manager will update
            }

            @Override
            public void onFailure(int reason) {
                Toast.makeText(PeersListActivity.this, "Discovery Failed : " + reason,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isWifiDirectSupported() {
        PackageManager pm = (PackageManager) this.getPackageManager();
        return pm.hasSystemFeature("android.hardware.wifi.direct");
    }
}
