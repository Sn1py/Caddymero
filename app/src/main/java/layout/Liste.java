package layout;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Paint;
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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jordan.mycaddy.DB;
import com.example.jordan.mycaddy.R;

import java.util.ArrayList;
import java.util.List;

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
    private Cursor cursor;
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
        fragment.onDetach();
        fragment.onAttachFragment(fragment);
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

        /** Message indiquant de sélectionner une liste **/

        // Instanciation du TextView
        TextView txtView = (TextView) getActivity().findViewById(R.id.textView);

        // Si aucune liste n'est sélectionnée
        if(c.getCount() == 0){
            // Alors on ne masque pas le message d'information
        }
        else {
            // Sinon, on masque le message d'information
            txtView.setVisibility(View.GONE);
        }

        /** Récupération des listes **/
        Cursor listes = base.recupererListes();

        /** Création du spinner et ajout des listes*/
        final Spinner spinner_listes = (Spinner) getActivity().findViewById(R.id.spinner_listes);
        SimpleCursorAdapter sca = new SimpleCursorAdapter(getContext(), android.R.layout.two_line_list_item, listes, new String[] {DB.KEY_NOM, DB.KEY_ID,}, new int[] {android.R.id.text1});
        spinner_listes.setAdapter(sca);

        /** Afficher les éléments de la liste sélectionnée **/
        spinner_listes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                /* Définir la liste sélectionnée dans le spinner comme liste actuelle */

                // Récupérer le nombre de liste actuelle (0 si aucune, 1 sinon)
                c = base.recupererParametres();

                // On récupère l'ID de la liste sélectionnée
                final long id_liste_selectionnee =  parent.getItemIdAtPosition(pos);

                // Affichage de la liste sélectionnée
                Toast.makeText(getContext(), "ID Liste sélectionnée : " + id_liste_selectionnee, Toast.LENGTH_LONG).show();

                // Si aucune liste n'est sélectionnée
                if(c.getCount() == 0){
                    // on ajoute la liste sélectionnée dans la table paramètres
                    base.ajouterParametre(id_liste_selectionnee);
                }
                else{
                    // Sinon, on met à jour l'id de la liste actuelle dans la table paramètres
                    base.majParametreIdListeActuelle(1, id_liste_selectionnee);
                }


                /* Récupérer les éléments de la liste actuelle (sélectionnée dans le spinner) */

                // Récupérer à nouveau l'ID de la liste actuelle
                c = base.recupererParametres();

                // Instanciation du ListView
                ListView listView = (ListView) getActivity().findViewById(R.id.listView_afficher_produit);

                // Remise du curseur à zéro (à définir pourquoi c'est nécessaire, je sais juste qu'il faut le faire)
                c.moveToFirst();

                // Affichage de la liste actuelle
                Toast.makeText(getContext(), "ID liste actuelle : " + c.getInt(c.getColumnIndex(DB.KEY_ID_LISTE_ACTUELLE)), Toast.LENGTH_LONG).show();

                // Récupérer les produits de la liste dont l'ID et celui de la liste actuelle
                cursor = base.recupererElementsId(c.getInt(c.getColumnIndex(DB.KEY_ID_LISTE_ACTUELLE)));

                // Ajout des produits dans la ListView
                getActivity().startManagingCursor(cursor);
                String[] from_produits = new String[] { DB.KEY_ID_PRODUIT };
                int[] to_produits = new int[] { R.id.nom };
                SimpleCursorAdapter produits = new SimpleCursorAdapter(getContext(), R.layout.produit_row, cursor, from_produits, to_produits);
                listView.setAdapter(produits);

                /** Rayer les éléments dont l'attribut coche vaut 1 en base **/
                cocherElements(listView);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                // Aucune action à réaliser ici
            }
        });
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

    public void cocherElements(ListView lv) {
        for (int i = 0; i < lv.getCount(); i++) {
            View v;
            TextView tv;
            v = lv.getAdapter().getView(i, null, null);
            tv = (TextView) v;
            if(1 == 1) { // conditions liée à la bdd
                tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                Toast.makeText(getContext(), tv.getText(), Toast.LENGTH_LONG).show();
                // Le toast fonctionne bien et affiche les éléments de la liste alors pourquoi ça barre pas ?
            }
        }
    }

    public void viderListe(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title);

        // Add the buttons
        builder.setPositiveButton(R.string.oui, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                c.moveToFirst();
                base.viderListe(c.getInt(c.getColumnIndex(DB.KEY_ID_LISTE_ACTUELLE)));
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

    @Override
    public void onResume() {
        super.onResume();
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
