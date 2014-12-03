package up.ndsg.join;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

public class PeerAdapter extends ArrayAdapter<Peer> implements ListAdapter{

    public PeerAdapter(Context context, int resource) {
        super(context, resource);
    }
}
