package archon.tp_yarr_app.Activities;

import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import archon.tp_yarr_app.R;

public class MainActivity extends NavigationDrawerActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        super.setUpDrawer();
        setUpListView();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    private void setUpListView() {
        final ListView listview = (ListView) findViewById(R.id.subreddits_list);
        String[] values = new String[] { "Hearthstone", "Androiddev", "Destinygame", "HPMOR",
                "askreddit", "showerthoughts", "dota", "blizzard", "changemyview", "funny",
                "undertale", "fallout", "discordian", "hacking", "gamedev"
        };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }
        final ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                            @Override
                                            public void onItemClick(AdapterView<?> parent, final View view,
                                                                    int position, long id) {
                                                openSubreddit();
                                            }
                                        }
        );



    }

    private void openSubreddit() {
        Intent i = new Intent(this, ThreadsActivity.class);
        startActivity(i);
    }
}
