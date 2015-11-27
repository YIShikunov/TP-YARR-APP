package archon.tp_yarr_app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RedditService extends Service {
    public RedditService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private String[] getFakeSubreddit() {
        String[] values = new String[] { "hearthstone", "Androiddev", "Destinygame", "HPMOR",
                "askreddit", "showerthoughts", "dota", "blizzard", "changemyview", "funny",
                "undertale", "fallout", "hacking", "gamedev"
        };
        return values;
    }
}
