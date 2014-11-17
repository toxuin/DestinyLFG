package ru.toxuin.destinylfg;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseActivity extends ActionBarActivity {
    private DrawerLayout drawer;
    private Toolbar toolbar;
    private Menu mMenu;
    private Fragment activeFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        ListView drawerList = (ListView) findViewById(R.id.drawer_left);

        int[] icons = new int[] {
                R.drawable.ic_action_view_as_list,
                R.drawable.ic_action_new,
                R.drawable.ic_action_settings
        };
        String[] captions = new String[] {
                getString(R.string.home),
                getString(R.string.new_lfg),
                getString(R.string.settings)
        };

        List<HashMap<String, String>> itemList = new ArrayList<>();
        for (int i = 0; i<captions.length; i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put("image", Integer.toString(icons[i]));
            map.put("txt", captions[i]);
            itemList.add(map);
        }

        drawerList.setAdapter(new SimpleAdapter(getBaseContext(), itemList, R.layout.drawer_item,
                new String[] {"image", "txt"}, new int[] {R.id.drawer_item_image, R.id.drawer_item_caption}));
        toolbar.setNavigationIcon(R.drawable.ic_drawer);
        drawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        final FragmentManager fragmentManager = getSupportFragmentManager();
        activeFragment = new LFGFragment();
        fragmentManager.beginTransaction().replace(R.id.content, activeFragment).commit();

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    default:
                    case 0:
                        if (mMenu != null) mMenu.findItem(R.id.action_refresh).setVisible(true);
                        activeFragment = new LFGFragment();
                        break;
                    case 1:
                        if (mMenu != null) mMenu.findItem(R.id.action_refresh).setVisible(false);
                        activeFragment =  new WizardFragment();
                        break;
                    case 2:
                        if (mMenu != null) mMenu.findItem(R.id.action_refresh).setVisible(false);
                        activeFragment = new SettingsFragment();
                        break;
                }
                fragmentManager.beginTransaction().replace(R.id.content, activeFragment).commit();
                drawer.closeDrawer(GravityCompat.START);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.lfg_activity_actions, menu);
        mMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
            default:
                activeFragment.onOptionsItemSelected(item);
        }

        return super.onOptionsItemSelected(item);
    }

    public static void openSettings(FragmentActivity a) {
        a.getSupportFragmentManager().beginTransaction().replace(R.id.content, new SettingsFragment()).commit();
    }
}
