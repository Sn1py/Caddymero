package com.example.jordan.mycaddy;

import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import layout.Produits;

import static com.example.jordan.mycaddy.R.id.container;

public class MainActivity extends AppCompatActivity implements Produits.OnFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
/**
        final View rootView = inflater.inflate(R.layout.fragment_produits, container, false);
        ListView maVariableListView = (ListView) findViewById(R.id.listView_ajouter_produit);
        registerForContextMenu(maVariableListView);
 **/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.vider_liste) {
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.vider_element_barres) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {

            // Contenu de l'onglet à insérer ici

            if(getArguments().getInt(ARG_SECTION_NUMBER) == 1) {

                final View rootView = inflater.inflate(R.layout.fragment_produits, container, false);

/**
                final View rootView = inflater.inflate(R.layout.fragment_produits, container, false);

                // Gestion du clique sur le bouton d'ajout d'un nouveau produit pré-défini

                // Définition du bouton
                final Button button = (Button) rootView.findViewById(R.id.button_ajouter_produit);
                // Définition du onClickListener
                button.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        ProduitDB produit_predef;
                        Cursor c;
                        produit_predef = new ProduitDB(getContext());
                        produit_predef.open();
                        final EditText maVariableEditText = (EditText) rootView.findViewById(R.id.editText_ajouter_produit);


                        // Ajout dans la BDD
                        String txt = maVariableEditText.getText().toString();
                        produit_predef.addproduit(txt);

                        // Récupération des données dans la BDD
                        c = produit_predef.fetchAllNotes();

                        // Affichage des données
                        getActivity().startManagingCursor(c);

                        String[] from = new String[] { ProduitDB.KEY_NAME };
                        int[] to = new int[] { R.id.name };

                        // Now create an array adapter and set it to display using our row
                        SimpleCursorAdapter notes = new SimpleCursorAdapter(getContext(), R.layout.produit_row, c, from, to);
                        ListView maVariableListView = (ListView) getActivity().findViewById(R.id.listView_ajouter_produit);
                        maVariableListView.setAdapter(notes);

                        maVariableEditText.setText(""); // 3 - remise à vide de l'EditText
                    }
                });
                **/
                return rootView;
            }


            else{
                View rootView = inflater.inflate(R.layout.fragment_main, container, false);
                TextView textView = (TextView) rootView.findViewById(R.id.section_label);
                return rootView;
            }
        }

        public interface OnFragmentInteractionListener {
            // TODO: Update argument type and name
            void onFragmentInteraction(Uri uri);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.context_menu_produit, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.supprimer:

                return true;

           default:
                return super.onContextItemSelected(item);
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter{

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).

            switch (position+1){
                case 1: return new Produits();

                default:
                    break;
            }

            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Produits";
                case 1:
                    return "Liste";
                case 2:
                    return "Mes listes";
            }
            return null;
        }
    }

    public void onFragmentInteraction(Uri uri){

    }

}
