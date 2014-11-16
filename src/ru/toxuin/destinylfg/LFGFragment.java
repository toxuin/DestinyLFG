package ru.toxuin.destinylfg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LFGFragment extends Fragment {
    public LFGFragment() {
        // Empty constructor required for fragment subclasses
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lfg, container, false);
        getActivity().setTitle(this.getString(R.string.app_name));
        return rootView;
    }
}