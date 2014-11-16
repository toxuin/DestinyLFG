package ru.toxuin.destinylfg;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.ptr.folding.BaseFoldingLayout;
import com.ptr.folding.FoldingLayout;
import com.ptr.folding.listener.OnFoldListener;
import org.json.JSONObject;
import ru.toxuin.destinylfg.library.JSONParser;

import java.util.HashMap;

public class LFGFragment extends Fragment {
    View rootView;

    FoldingLayout dynPanel;
    LinearLayout staticPanel;

    ListView itemList;

    public LFGFragment() { } // Empty constructor required for fragment subclasses

    private static String serverUrl = "http://api.learn2crack.com/android/jsonos/";
    private static final String TAG_OS = "android";
    private static final String TAG_VER = "ver";
    private static final String TAG_NAME = "name";
    private static final String TAG_API = "api";

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        dynPanel.setFoldFactor(1);
        dynPanel.invalidate();
        dynPanel.getChildAt(0).invalidate();
//        Log.d("LALALAL", "PANEL FOLD FACTOR: " + dynPanel.getFoldFactor());
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dynPanel.setFoldFactor(1f);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.lfg_item, container, false);
        staticPanel = (LinearLayout) rootView.findViewById(R.id.staticPanel);
        itemList = (ListView) rootView.findViewById(R.id.items_list);
        dynPanel = (FoldingLayout) rootView.findViewById(R.id.dynamicPanel);

        getActivity().setTitle(this.getString(R.string.app_name));
        dynPanel.setOrientation(BaseFoldingLayout.Orientation.VERTICAL);


        //itemList.setAdapter(new FoldableAdapter());




        //ObjectAnimator.ofFloat(dynPanel, "foldFactor", dynPanel.getFoldFactor(), 1).setDuration(300).start();

        staticPanel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFold(dynPanel);
            }
        });

        ImageView image = (ImageView) rootView.findViewById(R.id.image_view);
        image.setPadding(1, 1, 1, 1);

        //new JSONParse().execute();

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void toggleFold(FoldingLayout layout) {
        final boolean open = (layout.getFoldFactor() == 0);

        Log.d("ALALALAL", "WAS OPEN: " + open);

        ObjectAnimator animator = ObjectAnimator.ofFloat(layout,
                "foldFactor", layout.getFoldFactor(), open ? 1 : 0);
        animator.setDuration(300);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (!open) {
                    dynPanel.setVisibility(View.VISIBLE);
                    dynPanel.getChildAt(0).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (open) {
                    dynPanel.setVisibility(View.GONE);
                    dynPanel.getChildAt(0).setVisibility(View.GONE);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {}
            @Override
            public void onAnimationRepeat(Animator animation) {}
        });
        animator.start();
    }




}