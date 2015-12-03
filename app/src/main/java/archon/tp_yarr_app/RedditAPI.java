package archon.tp_yarr_app;

import java.util.Random;

public class RedditAPI {

    public static String[] loadSubreddits() {
        imitateWork(500);
        String[] values = new String[] { "API", "Destinygame", "HPMOR", "hearthstone",
                "askreddit", "showerthoughts", "dota", "blizzard", "changemyview", "funny",
                "undertale", "fallout", "hacking", "gamedev"
        };
        return values;
    }

    private static void imitateWork(int n) {
        int i = (new Random()).nextInt(n);
        while (i != 42)
            i = (new Random()).nextInt(n);
    }
}
