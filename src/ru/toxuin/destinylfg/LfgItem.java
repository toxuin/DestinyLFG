package ru.toxuin.destinylfg;

import android.content.Context;

import java.util.Arrays;
import java.util.List;

public class LfgItem {
    private int lvl = 1;
    private int platform = 0;
    private int clas = 0;
    private int event = 0;
    private boolean hasMic = false;
    private String playerId = "";
    private String notes = "";

    public LfgItem(int level, int platform, int clas, int event, boolean hasMic, String player, String notes) {
        if (notes == null) notes = "";
        if (event < 0) event = 0;
        if (player == null) playerId = "";
        if (level > 30) level = 30;
        if (level < 1) level = 1;
        this.hasMic = hasMic;
        this.event = event;
        this.lvl = level;
        this.platform = platform;
        this.clas = clas;
        this.playerId = player;
        this.notes = notes;
    }

    public int getClas() {
        return clas;
    }
    public int getEvent() {
        return event;
    }
    public int getPlatform() {
        return platform;
    }
    public boolean hasMic() {
        return hasMic;
    }
    public String getPlayerId() {
        return playerId;
    }
    public int getLvl() {
        return lvl;
    }
    public String getNotes() {
        return notes;
    }

    public boolean hasNotes() {
        return !notes.trim().equals("");
    }

    public String getCanonicalEventName(Context context) {
        String result = "Unknown event";
        List<String> eventNames = Arrays.asList(context.getResources().getStringArray(R.array.events));
        if (event <= eventNames.size()-1) {
            result = eventNames.get(event);
        }
        return result;
    }

    public String getCanonicalClassName(Context context) {
        String result = "Unknown class";
        List<String> classNames = Arrays.asList(context.getResources().getStringArray(R.array.pref_classes));
        if (clas <= classNames.size()-1) {
            result = classNames.get(clas);
        }
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof LfgItem)) return false;
        LfgItem that = (LfgItem) o;
        if (!this.playerId.equals(that.playerId)) return false;
        if (this.lvl != that.lvl) return false;
        if (this.platform != that.platform) return false;
        if (this.clas != that.clas) return false;
        if (this.event != that.event) return false;
        if (this.hasMic != that.hasMic) return false;
        if (this.notes.equals(that.notes)) return false;
        return true;
    }

    @Override
    public int hashCode() {
        return Integer.getInteger("" + notes.hashCode() + playerId.hashCode() + lvl + platform + ((hasMic) ? 1 : 0) + clas + event);
    }
}