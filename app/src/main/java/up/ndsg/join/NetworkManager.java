package up.ndsg.join;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.util.Log;
import android.widget.Toast;

import static android.net.wifi.p2p.WifiP2pManager.*;
import static android.net.wifi.p2p.WifiP2pManager.ActionListener;

public class NetworkManager extends BroadcastReceiver {

    private Context mContext;
    private WifiP2pManager mWiFi;
    private Channel mChannel;


    public NetworkManager(WifiP2pManager manager, Channel channel, Context context) {
        super();
        this.mWiFi = manager;
        this.mChannel = channel;
        this.mContext =  context;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        PeersListActivity activity = (PeersListActivity) this.mContext;

        if (WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
            int state = intent.getIntExtra(EXTRA_WIFI_STATE, -1);

            if (state == WIFI_P2P_STATE_ENABLED) {
                activity.setP2PEnabled(true);
            }
            else {
                activity.setP2PEnabled(false);
                activity.resetData();
            }
            Log.d(PeersListActivity.TAG, "P2P state changed - " + state);
        }
        else if (WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
            if (this.mWiFi != null) {
                this.mWiFi.requestPeers(this.mChannel,
                        (PeerListListener) activity.getFragmentManager().findFragmentById(R.id.fragment_peer_list));
            }
            Log.d(PeersListActivity.TAG, "P2P Peers changed");
        }
        else if (WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
            if (this.mWiFi == null) {
                return;
            }

            NetworkInfo networkInfo = (NetworkInfo) intent.getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

            if (networkInfo.isConnected()) {
                Toast.makeText(this.mContext, "Connection Successful", Toast.LENGTH_SHORT).show();
                Log.d(((PeersListActivity) this.mContext).TAG, "CONNECTION SUCCESSFULLLLLLLLL");
                //PeerDetailFragment fragment = (PeerDetailFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_peer_detail);
                //this.mWiFi.requestConnectionInfo(mChannel, fragment);
            }
            else {
                Toast.makeText(this.mContext, "Unsuccessful", Toast.LENGTH_SHORT).show();
                activity.resetData();
            }
        }
        else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
            PeerListFragment fragment = (PeerListFragment) activity.getFragmentManager().findFragmentById(R.id.fragment_peer_list);
            //fragment.updateThisDevice((WifiP2pDevice) intent.getParcelableExtra(WifiP2pManager.EXTRA_WIFI_P2P_DEVICE));
            //
        }


    }
}