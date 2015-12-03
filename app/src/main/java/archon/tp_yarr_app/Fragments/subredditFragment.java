package archon.tp_yarr_app.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import archon.tp_yarr_app.R;

public class SubredditFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    public SubredditFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, new ArrayList<String>());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subreddit_list, container, false);

        mListView = (ListView) view.findViewById(R.id.subreddits_list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                 @Override
                 public void onItemClick(AdapterView<?> parent, final View view,
                                         int position, long id) {
                     if (mListener != null)
                        mListener.onFragmentInteraction(position);
                 }
             }
        );
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(int position);
    }

    public void setList(String[] array) {
        mAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(mAdapter);
    }
}