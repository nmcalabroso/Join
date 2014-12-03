package up.ndsg.join;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pManager;

public class NetworkManager {

    private Context mContext;
    private WifiP2pManager mWiFi;

    public NetworkManager(Context context) {
        this.mContext =  context;
        this.mWiFi = (WifiP2pManager) this.mContext.getSystemService(Context.WIFI_P2P_SERVICE);
    }


}