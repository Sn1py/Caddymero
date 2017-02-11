package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.view.ContextMenu;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jordan.mycaddy.DB;
import com.example.jordan.mycaddy.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.jordan.mycaddy.R.id.container;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Produits.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Produits#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Produits extends Fragment {

    private DB base;
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

        base = new DB(getContext());
        base.open();

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produits, container, false);


        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);

        FloatingActionButton ajouter_produit = (FloatingActionButton) getView().findViewById(R.id.ajouter_produit);
        ajouter_produit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Définition du bouton
                final FloatingActionButton ajouter_produit_action = (FloatingActionButton) getActivity().findViewById(R.id.ajouter_produit);
                // Définition du onClickListener

                ajouter_produit_action.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        LayoutInflater li = LayoutInflater.from(getContext());

                        final View promptsView = li.inflate(R.layout.dialog, null);

                        final EditText editText_NomProduit = (EditText) promptsView.findViewById(R.id.editText);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                        alertDialogBuilder.setView(promptsView);

                        // set dialog message

                        alertDialogBuilder.setTitle("Ajouter un produit");
                        // create alert dialog
                        final AlertDialog alertDialog = alertDialogBuilder.create();

                        final Spinner mSpinner= (Spinner) promptsView.findViewById(R.id.spinner);
                        List<String> list = new ArrayList<String>();
                        list.add("list 1");
                        list.add("list 2");
                        list.add("list 3");
                        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, list);
                        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mSpinner.setAdapter(dataAdapter);
                        final Button mButton = (Button) promptsView.findViewById(R.id.button);

                        // reference UI elements from my_dialog_layout in similar fashion

                        //mSpinner.setOnItemSelectedListener(new OnSpinnerItemClicked());

                        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                String categorie = parent.getItemAtPosition(pos).toString();

                                Toast.makeText(parent.getContext(), "Clicked : " +
                                        parent.getItemAtPosition(pos).toString(), Toast.LENGTH_LONG).show();

                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

                        mButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String nom_produit = editText_NomProduit.getText().toString();
                                //base.ajouterProduit(nom_produit);
                                //actualiser();
                                Toast.makeText(getContext(), "Texte : " +
                                        nom_produit, Toast.LENGTH_LONG).show();
                                alertDialog.dismiss();

                            }
                        });

                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(false);
                    }
                });
            }
        });

        // Contexte menu
        ListView maVariableListView = (ListView) getActivity().findViewById(R.id.listView_ajouter_produit);
        registerForContextMenu(maVariableListView);

        maVariableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Récupérer le texte
                TextView tv=(TextView) view;
                //tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            }
        });

        //actualiser();
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.vider_element_barres);
        MenuItem item2 = menu.findItem(R.id.vider_liste);
        item.setVisible(false);
        item2.setVisible(false);
    }

    // Gestion de l'action des boutons du menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.vider_liste:
                return true;

            case R.id.vider_element_barres:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu_produit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.supprimer:

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

                // Add the buttons
                builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        base.supprimerProduit(info.id);
                        actualiser();
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

                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    public void actualiser(){
        ListView maVariableListView = (ListView) getActivity().findViewById(R.id.listView_ajouter_produit);

        // Récupération des données dans la BDD
        //c = base.recupererLignes();

        // Affichage des données
        getActivity().startManagingCursor(c);

        String[] from = new String[] { DB.KEY_NOM };
        int[] to = new int[] { R.id.nom };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes = new SimpleCursorAdapter(getContext(), R.layout.produit_row, c, from, to);
        maVariableListView.setAdapter(notes);

    }
    //


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
