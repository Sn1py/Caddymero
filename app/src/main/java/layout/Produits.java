package layout;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.jordan.mycaddy.ProduitDB;
import com.example.jordan.mycaddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Produits.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Produits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Produits extends Fragment {

    private ProduitDB produit_predef;
    private Cursor c;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Produits() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Produits.
     */
    // TODO: Rename and change types and number of parameters
    public static Produits newInstance(String param1, String param2) {
        Produits fragment = new Produits();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        produit_predef = new ProduitDB(getContext());
        produit_predef.open();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


/*
    private View mView;
    private Button button_submit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.fragment_produits, container,false);
        button_submit=(Button)mView.findViewById(R.id.button_ajouter_produit);


        button_submit.setOnClickListener();

        return mView;
    }


    @Override
    public void onClick(View v) {
        final EditText maVariableEditText = (EditText) getView().findViewById(R.id.editText_ajouter_produit);
        switch (v.getId()) {
            case R.id.button_ajouter_produit:
                maVariableEditText.setText("123456");
                break;
            default:
                maVariableEditText.setText("126");
                break;
        }

    }*/



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /* Ajout d'un nouveau produit à la base de connaissance */
    /*
    public void addButton(View view) {

        final EditText maVariableEditText = (EditText) view.findViewById(R.id.editText_ajouter_produit);
        /*
        ListView maVariableListView = (ListView) view.findViewById(R.id.listView_ajouter_produit);

        // Ajout dans la BDD
        String txt = maVariableEditText.getText().toString();
        produit_predef.addproduit(txt);


        // Récupération des données dans la BDD
        c = produit_predef.fetchAllNotes();


        // Affichage des données
        getActivity().startManagingCursor(c);

        String[] from = new String[] { ProduitDB.KEY_NAME,  ProduitDB.KEY_ROWID, ProduitDB.KEY_QUANTITY};
        int[] to = new int[] { R.id.section_label };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes = new SimpleCursorAdapter(getContext(), R.layout.fragment_produits, c, from, to);
        maVariableListView.setAdapter(notes);

        maVariableEditText.setText("11"); // 3 - remise à vide de l'EditText

    }*/

}
