package ru.toxuin.destinylfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

public class FoldableAdapter {
    /* private final Map<String, View> items;
    private final Context context;

    public FoldableAdapter(Context context, LinkedHashMap<String, View> items) {

        super(context, R.layout.pref_row_layout, R.id.pref_row_caption, new LinkedList<>(items.values()));
        this.items = items;
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.pref_row_layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.pref_row_caption);

        return rowView;
    } */
}
