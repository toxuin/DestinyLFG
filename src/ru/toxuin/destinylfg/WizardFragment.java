package ru.toxuin.destinylfg;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class WizardFragment extends Fragment {
    private static Dialog noSettingsDialog;
    int event = 0;
    ListView eventList;
    private LinearLayout notesPanel;

    public WizardFragment() { } // Empty constructor required for fragment subclasses

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard, container, false);
        getActivity().setTitle("New \"Looking for Group\"...");

        final SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (!spref.contains(SettingsFragment.PLAYER_ID_KEY) ||
                !spref.contains(SettingsFragment.PLATFORM_KEY) ||
                !spref.contains(SettingsFragment.CLASS_KEY) ||
                !spref.contains(SettingsFragment.LEVEL_KEY)) {
            Log.d("LALALALLA", "SETTINGS: " +
                            spref.contains(SettingsFragment.PLAYER_ID_KEY) +
                            spref.contains(SettingsFragment.PLATFORM_KEY) +
                            spref.contains(SettingsFragment.CLASS_KEY) +
                            spref.contains(SettingsFragment.LEVEL_KEY)
            );
            Builder builder = new Builder(getActivity());
            builder.setMessage(getResources().getString(R.string.no_settings_dialog))
                    .setPositiveButton(getResources().getString(R.string.open_settings),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    WizardFragment.noSettingsDialog.dismiss();
                                    BaseActivity.openSettings(getActivity());
                                }
                    });
            noSettingsDialog = builder.create();
            noSettingsDialog.show();
        }

        notesPanel = (LinearLayout) rootView.findViewById(R.id.notesPanel);
        eventList = (ListView) rootView.findViewById(R.id.eventSelector);
        //ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(getActivity(),
        //        R.array.events, R.layout.event_item);
        //ArrayAdapter<String> eventAdapter = new ArrayAdapter<>(getActivity(),
        //        android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.events));
        ArrayAdapter<CharSequence> eventAdapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.events, android.R.layout.simple_list_item_1);
        eventList.setAdapter(eventAdapter);

        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                event = position;
                eventList.setVisibility(View.GONE);
                notesPanel.setVisibility(View.VISIBLE);
            }
        });

        Button sendBtn = (Button) rootView.findViewById(R.id.finishButton);
        final EditText notesText = (EditText) rootView.findViewById(R.id.editNotes);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int lvl = spref.getInt(SettingsFragment.LEVEL_KEY, 0);
                int platform = spref.getInt(SettingsFragment.PLATFORM_KEY, 0);
                int clas = spref.getInt(SettingsFragment.CLASS_KEY, 0);
                String playerName = spref.getString(SettingsFragment.PLAYER_ID_KEY, "");
                boolean hasMic = spref.getBoolean(SettingsFragment.HAS_MIC_KEY, true);

                List<NameValuePair> nameValuePairs = new ArrayList<>();
                nameValuePairs.add(new BasicNameValuePair("NEW", "12345"));
                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_LEVEL, lvl+""));
                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_PLATFORM, ""+platform));
                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_CLASS, ""+clas));
                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_PLAYER_NAME, playerName));
                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_HAS_MIC, (hasMic?1:0)+""));

                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_EVENT, event+""));
                nameValuePairs.add(new BasicNameValuePair(LFGFragment.TAG_NOTES, notesText.getText().toString()));

                new DataSender().execute(nameValuePairs);

            }
        });

        return rootView;
    }

    private class DataSender extends AsyncTask<List<NameValuePair>, String, Object> {
        @Override
        protected void onPreExecute() {
            Toast.makeText(getActivity(), "SENDING....", Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Object doInBackground(List<NameValuePair>... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(LFGFragment.serverUrl);
            if (params[0] == null) return null;

            try {
                httppost.setEntity(new UrlEncodedFormEntity(params[0]));
                httpclient.execute(httppost);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Object obj) {
            Toast.makeText(getActivity(), "SENT SUCCESSFULLY", Toast.LENGTH_SHORT).show();
            eventList.setVisibility(View.VISIBLE);
            notesPanel.setVisibility(View.GONE);
        }
    }

}
