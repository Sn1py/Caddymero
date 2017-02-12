package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jordan.mycaddy.DB;
import com.example.jordan.mycaddy.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Liste.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Liste#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Liste extends Fragment {

    private Cursor c;
    private DB base;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Liste() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Liste.
     */
    // TODO: Rename and change types and number of parameters
    public static Liste newInstance(String param1, String param2) {
        Liste fragment = new Liste();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);

        base = new DB(getContext());
        base.open();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_liste, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        // Définition de la ListeView permettant d'afficher les éléments de la liste actuelle
        //ListView listView_afficher_elements = (ListView) getActivity().findViewById(R.id.t);

        /** Spécification du ContextMenu **/
        //registerForContextMenu(listView_afficher_elements);

        TextView txtView = (TextView) getActivity().findViewById(R.id.textView);

        /** Récupération de la liste actuelle **/
        c = base.recupererParametres();
        getActivity().startManagingCursor(c);
        String[] from = new String[] { DB.KEY_ID_LISTE_ACTUELLE };
        int[] to = new int[] { R.id.nom };
        SimpleCursorAdapter notes = new SimpleCursorAdapter(getContext(), R.layout.produit_row, c, from, to);

        //Toast.makeText(getContext(), "Valeur : " + c.getCount(), Toast.LENGTH_LONG).show();

        // Si aucune list n'est sélectionnée
        if(c.getCount() == 0){
            // Alors on ne masque pas le message d'information
        }
        else{
            // Sinon, on masque le message d'information
            txtView.setVisibility(View.GONE);

            // Et on affiche le contenu de la liste

        }




        /*
        listView_afficher_elements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                 // Récupérer le texte
                 TextView tv=(TextView) view;
                 //tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                 }
                 });

                 actualiser();
                 }*/
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem vider_element_barres = menu.findItem(R.id.vider_element_barres);
        MenuItem vider_liste = menu.findItem(R.id.vider_liste);
        vider_element_barres.setVisible(true);
        vider_liste.setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.vider_element_barres:
                // Supprimer les éléments cochés
                return true;
            case R.id.vider_liste:
                viderListe();
                return true;
            default:
                break;
        }

        return false;
    }

    public void viderListe(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setNegativeButton(R.string.non, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
    }

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
}
