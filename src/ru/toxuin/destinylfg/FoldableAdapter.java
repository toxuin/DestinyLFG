package ru.toxuin.destinylfg;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.w3c.dom.Text;
import ru.toxuin.destinylfg.library.AnimatedExpandableListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static ru.toxuin.destinylfg.library.AnimatedExpandableListView.*;

public class FoldableAdapter extends AnimatedExpandableListAdapter {

    private ArrayList<LfgItem> mItems;
    private Context mContext;

    public FoldableAdapter(Context context, ArrayList<LfgItem> items){
        mContext = context;
        mItems = items;
    }

    @Override
    public int getGroupCount() {
        return mItems.size();
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return mItems.get(groupPosition).hasNotes()?1:0;
    }

    @Override
    public LfgItem getGroup(int groupPosition) {
        return mItems.get(groupPosition);
    }

    @Override
    public LfgItem getChild(int groupPosition, int childPosition) {
        return mItems.get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lfg_item, null);
        }

        /*
        if (isExpanded) {
            //Изменяем что-нибудь, если текущая Group раскрыта
        } else {
            //Изменяем что-нибудь, если текущая Group скрыта
        } */

        LfgItem item = getGroup(groupPosition);

        TextView levelCaption = (TextView) convertView.findViewById(R.id.levelCaption);
        TextView eventCaption = (TextView) convertView.findViewById(R.id.eventCaption);
        TextView classCaption = (TextView) convertView.findViewById(R.id.classCaption);
        TextView playerIdCaption = (TextView) convertView.findViewById(R.id.playerIdCation);
        ImageView hasMicPic = (ImageView) convertView.findViewById(R.id.hasMicPic);
        ImageView platformPic = (ImageView) convertView.findViewById(R.id.platformPic);
        ImageView hasNotesPic = (ImageView) convertView.findViewById(R.id.hasNotesPic);

        levelCaption.setText(item.getLvl()+"");
        if (item.getLvl() >= 30) {
            levelCaption.setTextColor(mContext.getResources().getColor(R.color.lightLvlColor));
        } else {
            levelCaption.setTextColor(mContext.getResources().getColor(R.color.usualLvlColor));
        }

        eventCaption.setText(item.getCanonicalEventName(mContext));
        classCaption.setText(item.getCanonicalClassName(mContext));
        playerIdCaption.setText(item.getPlayerId());
        hasMicPic.setImageResource(item.hasMic()?R.drawable.speak_now_level0:R.drawable.mic_slash);
        if (item.hasNotes()) {
            hasNotesPic.setVisibility(View.VISIBLE);
        } else {
            hasNotesPic.setVisibility(View.GONE);
        }

        if (item.getPlatform() == 0 || item.getPlatform() == 1) {
            platformPic.setImageResource(R.drawable.platform_psn);
        } else {
            platformPic.setImageResource(R.drawable.platform_live);
        }

        return convertView;

    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.lfg_notes_view, null);
        }

        LfgItem item = mItems.get(groupPosition);
        TextView notes = (TextView) convertView.findViewById(R.id.notes);
        notes.setText(item.getNotes());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
