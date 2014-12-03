package up.ndsg.join;

import android.app.Application;
import android.content.Intent;
import android.net.Network;
import android.view.Menu;


public class JoinApplication extends Application {

    private NetworkManager mNetworkManager;

    @Override
    public void onCreate() {
        super.onCreate();
        mNetworkManager = new NetworkManager(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public NetworkManager getNetworkManager() {
        return this.mNetworkManager;
    }
}
