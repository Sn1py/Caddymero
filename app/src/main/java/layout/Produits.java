package layout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Paint;
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
import static com.example.jordan.mycaddy.R.id.supprimer;

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
    private long id_liste_selectionnee = 0;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int categorieSelectionee = 0;

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

        suppressionBase();
        insertionBase();
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

        /** Spécification du ContextMenu **/
        ListView maVariableListView = (ListView) getActivity().findViewById(R.id.listView_ajouter_produit);
        registerForContextMenu(maVariableListView);

        /** Ajoute d'un nouveau produit **/
        // Bouton d'ajout d'un produit
        final FloatingActionButton ajouter_produit = (FloatingActionButton) getView().findViewById(R.id.ajouter_produit);
        // Si click sur le bouton

        //A CORRIGER DOUBLE APPUI
        ajouter_produit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                        LayoutInflater li = LayoutInflater.from(getContext());
                        // Récupérer la vue liée au xml de la fenêtre de dialogue
                        final View promptsView = li.inflate(R.layout.dialog, null);

                        // Définit l'EditText
                        final EditText editText_NomProduit = (EditText) promptsView.findViewById(R.id.editText);

                        // Instancier la boite de dialogue
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());

                        // Associer la vue du XML à la boite de dialogue
                        alertDialogBuilder.setView(promptsView);

                        // Titre de la boite de dialogue
                        alertDialogBuilder.setTitle("Ajouter un produit");

                        // Création de la boite de dialogue
                        final AlertDialog alertDialog = alertDialogBuilder.create();

                        // Création du Spinner
                        final Spinner spinner_categories = (Spinner) promptsView.findViewById(R.id.spinner);

                        // Récupérer les id des produits de la liste dont l'ID et celui de la liste actuelle
                        Cursor categories = base.recupererCategories();

                        // Ajout des produits dans la ListView
                        getActivity().startManagingCursor(categories);
                        String[] from_produits = new String[] { DB.KEY_NOM };
                        int[] to_produits = new int[] { R.id.nom };
                        SimpleCursorAdapter sca_categories = new SimpleCursorAdapter(getContext(), R.layout.produit_row, categories, from_produits, to_produits);
                        spinner_categories.setAdapter(sca_categories);

                        // Définition du bouton d'ajout
                        final Button mButton = (Button) promptsView.findViewById(R.id.button);

                        // OnItemSelectedListener
                        spinner_categories.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                                // Récupérer la catégorie du nouveau produit
                                String categorie = parent.getItemAtPosition(pos).toString();

                                categorieSelectionee = (int)id;

                                //setIdCategorie(id);
                            }

                            public void onNothingSelected(AdapterView<?> arg0) {
                                // TODO Auto-generated method stub

                            }
                        });

                        // Valider l'ajout du produit
                        mButton.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                String nom_produit = editText_NomProduit.getText().toString();
                                Cursor c = base.recupererCategorieId(categorieSelectionee);
                                c.moveToFirst();
                                String logo = c.getString(c.getColumnIndex("logo"));
                                base.ajouterProduit(nom_produit, categorieSelectionee, logo);
                                actualiser();
                                alertDialog.dismiss();
                            }
                        });
                        alertDialog.show();
                        alertDialog.setCanceledOnTouchOutside(true);
                    }
                });

                actualiser();



        /** Intéraction au simple clic sur un item de la ListView **/
        maVariableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // ID du produit sélectionné
               final long id_produit_selectionne =  parent.getItemIdAtPosition(position);

                LayoutInflater li = LayoutInflater.from(getContext());
                // Récupérer la vue liée au xml de la fenêtre de dialogue
                final View promptsView = li.inflate(R.layout.dialog_assigner_produit_liste, null);

                // Instance dialog
                final Dialog dialog = new Dialog(getContext());

                // Définition du titre
                dialog.setTitle("Sélectionnez une liste");

                // Récupération du XML de la boite de dialogue
                dialog.setContentView(R.layout.dialog_assigner_produit_liste);

                // Récupération des informations des listes
                Cursor select = base.recupererListes();

                // Création du spinner
                final Spinner spinner = (Spinner) dialog.findViewById(R.id.spinner);
                SimpleCursorAdapter myAdapt = new SimpleCursorAdapter(dialog.getContext(), android.R.layout.two_line_list_item, select, new String[] {DB.KEY_NOM, DB.KEY_ID,}, new int[] {android.R.id.text1});
                spinner.setAdapter(myAdapt);

                // Définition du bouton d'ajout
                final Button assigner = (Button) dialog.findViewById(R.id.button);

                // OnItemSelectedListener
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        // Sauvegarder l'ID de la liste sélectionnée
                        setIdListeSelectionnee(parent.getItemIdAtPosition(pos));
                    }

                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });

                // Valider l'ajout du produit dans éléments
                assigner.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        // Récupération de l'ID du produit sélectionné
                        Cursor te = base.recupererProduitsParId(id_produit_selectionne);

                        // Positionnement du cursor en première poition
                        te.moveToFirst();

                        // Spécification de l'icone
                        String icone = "android.resource://com.example.jordan.mycaddy/drawable/restaurant";

                        // Ajout du produit à la liste voulue

                        Cursor c = base.recupererProduitsParId(id_produit_selectionne);
                        c.moveToFirst();
                        String logo = c.getString(c.getColumnIndex("logo"));

                        base.ajouterElement(id_produit_selectionne, te.getString(te.getColumnIndex(DB.KEY_NOM)), getIdListeSelectionnee(), 1, 0, logo);

                        // Suppression de la boite de dialogue
                        dialog.dismiss();

                        // Récupération des fragments de l'application
                        List<Fragment> frags = getFragmentManager().getFragments();

                        // Sélection du fragment 1 qui correspond à Liste.java
                        Fragment fragListe = frags.get(1);

                        // Appel à la fonction onResume qui va actualiser le contenu
                        fragListe.onResume();
                    }
                });

                // Affichage de la boite de dialogue
                dialog.show();
                dialog.setCanceledOnTouchOutside(true);

            }
        });

        actualiser();
    }

    public void setIdListeSelectionnee(long id){
        this.id_liste_selectionnee = id;
    }

    public long getIdListeSelectionnee(){
        return id_liste_selectionnee;
    }

    public void suppressionBase(){
        base.supprimerLesCategories();
        base.supprimerTousLesProduits();
    }


    public void insertionBase(){
        insererCategories();
        insererProduits();
    }

    public void insererCategories(){
        base.ajouterCategorie("Fruits", "fruits");      // 1
        base.ajouterCategorie("Legumes", "legumes");    // 2
        base.ajouterCategorie("Viandes", "viandes");     // 3
        base.ajouterCategorie("Boissons", "boissons");   // 4
    }

    public void insererProduits(){

        // Synthaxe : base.ajouterProduit(Nom du produit, id de la catégorie, logo du produit)

        // Fruits
        base.ajouterProduit("Pomme", 1, "logo");
        base.ajouterProduit("Poire", 1, "logo");
        base.ajouterProduit("Fraise", 1, "logo");
        base.ajouterProduit("Banane", 1, "logo");
        base.ajouterProduit("Orange", 1, "logo");
        base.ajouterProduit("Abricot", 1, "logo");

        // Legumes
        base.ajouterProduit("Carotte", 2, "logo");
        base.ajouterProduit("Salade", 2, "logo");
        base.ajouterProduit("Courgette", 2, "logo");
        base.ajouterProduit("Radis", 2, "logo");
        base.ajouterProduit("Poivron", 2, "logo");
        base.ajouterProduit("Pommes de terre", 2, "logo");

        // Viandes
        base.ajouterProduit("Boeuf", 3, "logo");
        base.ajouterProduit("Poulet", 3, "logo");
        base.ajouterProduit("Cheval", 3, "logo");
        base.ajouterProduit("Mouton", 3, "logo");
        base.ajouterProduit("Porc", 3, "logo");

        // Boissons
        base.ajouterProduit("Eau", 4, "logo");
        base.ajouterProduit("Coca Cola", 4, "logo");
        base.ajouterProduit("Jus de fruits", 4, "logo");
        base.ajouterProduit("Bière", 4, "logo");
        base.ajouterProduit("Champagne", 4, "logo");
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem ajouter_liste = menu.findItem(R.id.ajouter_liste);
        MenuItem vider_element_barres = menu.findItem(R.id.vider_element_barres);
        MenuItem vider_liste = menu.findItem(R.id.vider_liste);
        ajouter_liste.setVisible(false);
        vider_element_barres.setVisible(false);
        vider_liste.setVisible(false);
    }

    // Gestion de l'action des boutons du menu
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.ajouter_liste:
                return false;

            case R.id.vider_liste:
                return false;

            case R.id.vider_element_barres:
                return false;

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
        c = base.recupererProduits();

        // Affichage des données
        getActivity().startManagingCursor(c);

        String[] from = new String[] { DB.KEY_NOM };
        int[] to = new int[] { R.id.nom };

        // Now create an array adapter and set it to display using our row
        SimpleCursorAdapter notes = new SimpleCursorAdapter(getContext(), R.layout.produit, c, from, to);
        maVariableListView.setAdapter(notes);

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
