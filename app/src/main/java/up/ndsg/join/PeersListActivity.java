package up.ndsg.join;

import android.app.Activity;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import static android.view.View.OnCreateContextMenuListener;


public class PeersListActivity extends ListActivity implements OnCreateContextMenuListener {

    private NetworkManager mNetworkManager;
    private ListView mList;
    private PeerAdapter mAdapter;
    private ProgressDialog mLoadingPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_peers_list);

        this.mNetworkManager = ((JoinApplication) getApplication()).getNetworkManager();
        this.mList = (ListView) findViewById(android.R.id.list);
        this.mAdapter = new PeerAdapter(this, R.id.textview_row_peer_name);
        this.mList.setAdapter(this.mAdapter);
        this.mLoadingPopup = new ProgressDialog(this);
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

    public void scanPeers(View view) {
        this.mLoadingPopup = ProgressDialog.show(this, "", "Scanning...");
        //this.mNetworkManager.scanForPeers(getApplicationContext(), )
    }
}
