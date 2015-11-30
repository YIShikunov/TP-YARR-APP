package archon.tp_yarr_app.Fragments;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Context;
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

        String[] values = new String[] { };

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < values.length; ++i) {
            list.add(values[i]);
        }

        mAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_list_item_1, list);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subreddit_list, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.subreddits_list);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                                             @Override
                                             public void onItemClick(AdapterView<?> parent, final View view,
                                                                     int position, long id) {
                                                 mListener.onFragmentInteraction((String) mAdapter.getItem(position));
                                             }
             }
        );
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //Activity activity = (Activity) context;
        try {
            mListener = (OnFragmentInteractionListener) activity;
            Toast.makeText(getActivity(), "Attach", Toast.LENGTH_SHORT).show();
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String item_id);
    }

    public void setList(String[] array) {
        mAdapter = new ArrayAdapter(getActivity(),
                        android.R.layout.simple_list_item_1, array);
        mListView.setAdapter(mAdapter);
    }
}
