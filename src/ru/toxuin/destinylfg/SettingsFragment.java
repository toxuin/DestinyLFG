package ru.toxuin.destinylfg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class SettingsFragment extends Fragment {
    private SharedPreferences spref;

    private static final String HAS_MIC_KEY = "has_mic";
    private static final String LEVEL_KEY = "lvl";
    private static final String PLAYER_ID_KEY = "player_id";
    private static final String CLASS_KEY = "class";
    private static final String PLATFORM_KEY = "platform";

    public SettingsFragment() {} // Empty constructor required for fragment subclasses

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        getActivity().setTitle("Settings");

        final CheckBox hasMicBox = (CheckBox) rootView.findViewById(R.id.pref_mic);
        final NumberPicker lvlPicker = (NumberPicker) rootView.findViewById(R.id.pref_lvl);
        final Spinner classSpinner = (Spinner) rootView.findViewById(R.id.pref_class);
        final Spinner platformSpinner = (Spinner) rootView.findViewById(R.id.pref_platform);
        final EditText playerId = (EditText) rootView.findViewById(R.id.pref_player_id);
        final TextView playerIdCaption = (TextView) rootView.findViewById(R.id.pref_player_id_caption);

        // INITIAL VALUES
        hasMicBox.setChecked(spref.getBoolean(HAS_MIC_KEY, true));

        lvlPicker.setMinValue(1);
        lvlPicker.setMaxValue(30);
        lvlPicker.setValue(spref.getInt(LEVEL_KEY, 30) > 30 ? 30 : spref.getInt(LEVEL_KEY, 30));

        ArrayAdapter<CharSequence> classAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pref_classes, android.R.layout.simple_spinner_item);
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);
        classSpinner.setSelection(spref.getInt(CLASS_KEY, 0), false);

        ArrayAdapter<CharSequence> platformAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.pref_platforms, android.R.layout.simple_spinner_item);
        platformAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        platformSpinner.setAdapter(platformAdapter);

        platformSpinner.setSelection(spref.getInt(PLATFORM_KEY, 0), false);

        playerId.setText(spref.getString(PLAYER_ID_KEY, ""));

        // HANDLERS
        hasMicBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spref.edit().putBoolean(HAS_MIC_KEY, hasMicBox.isChecked()).apply();
            }
        });

        lvlPicker.setOnValueChangedListener(new OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                spref.edit().putInt(LEVEL_KEY, newVal).apply();
            }
        });

        classSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spref.edit().putInt(CLASS_KEY, position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        platformSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || position == 2) {
                    playerIdCaption.setText(getResources().getString(R.string.psn_id));
                } else {
                    playerIdCaption.setText(getResources().getString(R.string.gamertag));
                }
                spref.edit().putInt(PLATFORM_KEY, position).apply();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        playerId.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    spref.edit().putString(PLAYER_ID_KEY, playerId.getText().toString()).apply();
                    return true;
                }
                return false;
            }
        });

        return rootView;
    }

}
