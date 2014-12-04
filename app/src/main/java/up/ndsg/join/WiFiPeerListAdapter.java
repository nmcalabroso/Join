package up.ndsg.join;

import android.content.Context;
import android.net.wifi.p2p.WifiP2pDevice;
import android.widget.ArrayAdapter;

import java.util.List;

public class WiFiPeerListAdapter extends ArrayAdapter<WifiP2pDevice> {
    private List<WifiP2pDevice> items;

    public WiFiPeerListAdapter(Context context, int resource, List<WifiP2pDevice> items) {
        super(context, resource);
        this.items = items;
    }


}
