package up.ndsg.join;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;

import static android.net.wifi.p2p.WifiP2pManager.ActionListener;

public class NetworkManager extends BroadcastReceiver {

    private Context mContext;
    private WifiP2pManager mWiFi;
    private Channel mChannel;

    public NetworkManager(Context context) {
        super();
        this.mContext =  context;
        this.mWiFi = (WifiP2pManager) this.mContext.getSystemService(Context.WIFI_P2P_SERVICE);
    }

    public void scanForPeers(Context context, ActionListener listener) {
        this.mWiFi.discoverPeers(mChannel, listener);
        //context.
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            // Check to see if Wi-Fi is enabled and notify appropriate activity
        } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            // Call WifiP2pManager.requestPeers() to get a list of current peers
        } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            // Respond to new connection or disconnections
        } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            // Respond to this device's wifi state changing
        }
    }
}