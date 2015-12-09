package archon.tp_yarr_app.Fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import archon.tp_yarr_app.R;

public class StateFragment extends Fragment {

    public String mode;

    public static final String MODE_SUBREDDITS = "SUBREDDITS";
    public static final String MODE_THREADS = "THREADS";
    public static final String MODE_COMMENTS = "COMMENTS";

    public SubredditFragment subredditFragment;
    public ThreadsFragment threadsFragment;
    public CommentsFragment commentsFragment;

    public String[] subreddits;
    public String[] threads;
    // comments

    public StateFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return null;
    }

}
