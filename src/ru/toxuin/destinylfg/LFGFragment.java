package ru.toxuin.destinylfg;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.toxuin.destinylfg.library.AnimatedExpandableListView;
import ru.toxuin.destinylfg.library.JSONParser;

import java.util.ArrayList;

public class LFGFragment extends Fragment {
    View rootView;

    public LFGFragment() { } // Empty constructor required for fragment subclasses

    public static String serverUrl = "http://nighthunters.ca/destiny_lfg/";
    public static final String TAG_ROOT = "lfg";
    public static final String TAG_LEVEL = "lvl";
    public static final String TAG_CLASS = "class";
    public static final String TAG_EVENT = "event";
    public static final String TAG_PLAYER_NAME = "playerName";
    public static final String TAG_HAS_MIC = "hasMic";
    public static final String TAG_NOTES = "notes";
    public static final String TAG_PLATFORM = "platform";

    private AnimatedExpandableListView itemsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_lfg, container, false);

        getActivity().setTitle(this.getString(R.string.app_name));

        itemsList = (AnimatedExpandableListView) rootView.findViewById(R.id.items_list);
        itemsList.setGroupIndicator(null);
        itemsList.setDividerHeight(0);

        itemsList.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (itemsList.isGroupExpanded(groupPosition)) {
                    itemsList.collapseGroupWithAnimation(groupPosition);
                } else {
                    itemsList.expandGroupWithAnimation(groupPosition);
                }
                return true;
            }
        });
        refreshList();

        return rootView;
    }

    public void refreshList() {
        LinearLayout errorPane = (LinearLayout) rootView.findViewById(R.id.errorPanel);
        if (!isNetworkAvailable()) {
            TextView errorMessage = (TextView) rootView.findViewById(R.id.errorMessage);
            errorPane.setVisibility(View.VISIBLE);
            itemsList.setVisibility(View.GONE);
            errorMessage.setText("No network connection available. Once you reconnect - hit refresh button!");
        } else {
            errorPane.setVisibility(View.GONE);
            itemsList.setVisibility(View.VISIBLE);
            new JSONParse().execute();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshList();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    private class JSONParse extends AsyncTask<String, String, JSONObject> {
        private ProgressDialog pDialog;
        SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int platform = spref.getInt("platform", 0);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Getting Data ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser jParser = new JSONParser();
            return jParser.getJSONFromUrl(serverUrl + "?" + TAG_PLATFORM + "=" + platform);
        }

        @Override
        protected void onPostExecute(JSONObject json) {
            pDialog.dismiss();
            Log.d("LALALA", json.toString());
            ArrayList<LfgItem> items = new ArrayList<>();
            try {
                // Getting JSON Array from URL
                JSONArray array = json.getJSONArray(TAG_ROOT);
                if (json.isNull(TAG_ROOT)) {
                    Log.d("LALALAL", json.getString("error"));
                    return;
                }
                for (int i = 0; i < array.length(); i++){
                    JSONObject c = array.getJSONObject(i);
                    // Storing  JSON item in a Variable

                    // lvl, class, event, playername, hasMic, notes
                    int lvl = c.getInt(TAG_LEVEL);
                    int clas = c.getInt(TAG_CLASS);
                    int event = c.getInt(TAG_EVENT);
                    String playerName = c.getString(TAG_PLAYER_NAME);
                    boolean hasMic = c.getBoolean(TAG_HAS_MIC);
                    String notes = c.getString(TAG_NOTES);
                    items.add(new LfgItem(lvl, platform, clas, event, hasMic, playerName, notes));
                }
                FoldableAdapter adapter = new FoldableAdapter(getActivity(), items);
                itemsList.setAdapter(adapter);
            } catch (JSONException e) {
                TextView errorMessage = (TextView) rootView.findViewById(R.id.errorMessage);
                LinearLayout errorPane = (LinearLayout) rootView.findViewById(R.id.errorPanel);
                errorPane.setVisibility(View.VISIBLE);
                itemsList.setVisibility(View.GONE);
                try {
                    if (json.get("error") != null) {
                        errorMessage.setText("Error: " + json.getString("error"));
                    }
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }

            }
        }
    }




}