package archon.tp_yarr_app.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import archon.tp_yarr_app.R;

/**
 * Created by Artem on 26-Nov-15.
 */
public class ThreadsFragment extends Fragment implements AbsListView.OnItemClickListener  {

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static ThreadsFragment newInstance() {
        ThreadsFragment fragment = new ThreadsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ThreadsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            /*mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);*/
        }
        String[] values = new String[] { "How to do a barrel roll?", "Look at this funny cat!",
                "I want to kill superman please help!!!", "I am confused by the deckslots!",
                "Do people with lisp think with lisp?", "Just drafted 7 Dr. Booms in arena..."
        };

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
        View view = inflater.inflate(R.layout.fragment_thread_list, container, false);

        // Set the adapter
        mListView = (ListView) view.findViewById(R.id.threads_list);
        mListView.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
            Toast.makeText(getActivity(), "Attach", Toast.LENGTH_SHORT).show();
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            Toast.makeText(getActivity(), "Click", Toast.LENGTH_SHORT).show();
            mListener.onFragmentInteraction("0");
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }
}
