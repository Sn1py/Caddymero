package com.example.jordan.mycaddy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DB {

    public static final String KEY_ID = "_id";
    public static final String KEY_ID_CATEGORIE = "id_categorie";
    public static final String KEY_ID_PRODUIT = "id_produit";
    public static final String KEY_ID_LISTE_ACTUELLE = "id_liste_actuelle";
    public static final String KEY_ID_LISTE = "id_liste";
    public static final String KEY_NOM = "nom";
    public static final String KEY_NOM_PRODUIT = "nom_produit";
    public static final String KEY_QUANTITE = "quantite";
    public static final String KEY_LOGO = "logo";
    public static final String KEY_COCHE = "coche";

    private static final String TAG = "DB";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    ///Variables utilisées pour la création de la base
    private static final String DATABASE_CREATE_LISTES =
            "create table listes (_id integer primary key autoincrement, "
                    + "nom text not null);";
    private static final String DATABASE_CREATE_CATEGORIES =
            "create table categories (_id integer primary key autoincrement, "
                    + "nom text not null, logo text not null);";
    private static final String DATABASE_CREATE_PRODUITS =
            "create table produits (_id integer primary key autoincrement, "
            + "nom text not null, id_categorie integer not null, logo text not null);";
    private static final String DATABASE_CREATE_ELEMENTS =
            "create table elements (_id integer primary key autoincrement, "
                    + "id_produit integer not null, nom_produit text not null, id_liste integer not null, quantite integer not null, coche integer not null, icone text not null);";
    private static final String DATABASE_CREATE_PARAMETRES =
            "create table parametres (_id integer primary key autoincrement, "
                    + "id_liste_actuelle integer);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_PRODUITS = "produits";
    private static final String DATABASE_TABLE_CATEGORIES = "categories";
    private static final String DATABASE_TABLE_LISTES = "listes";
    private static final String DATABASE_TABLE_ELEMENTS = "elements";
    private static final String DATABASE_TABLE_PARAMETRES = "parametres";
    private static final int DATABASE_VERSION = 3;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE_PRODUITS);
            db.execSQL(DATABASE_CREATE_CATEGORIES);
            db.execSQL(DATABASE_CREATE_LISTES);
            db.execSQL(DATABASE_CREATE_ELEMENTS);
            db.execSQL(DATABASE_CREATE_PARAMETRES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS produits");
            onCreate(db);
        }
    }

    public DB(Context ctx) {
        this.mCtx = ctx;
    }

    public DB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /* Gestion Table Listes */

    public long ajouterListe(String nom) {

        // A corriger avec les nouveaux champs

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM, nom);
        return mDb.insert(DATABASE_TABLE_LISTES, null, initialValues);
    }

    public boolean viderListe(long id_liste) {
        return mDb.delete(DATABASE_TABLE_ELEMENTS, KEY_ID_LISTE + "=" + id_liste, null) > 0;
    }

    public boolean viderElementsCoches(long id_liste) {
        return mDb.delete(DATABASE_TABLE_ELEMENTS, "(" + KEY_ID_LISTE + "=" + id_liste + " AND coche=1)", null) > 0;
    }

    public boolean supprimerListeId(long id) {
        return mDb.delete(DATABASE_TABLE_LISTES, KEY_ID + "=" + id, null) > 0;
    }

    public Cursor recupererListes() {
        return mDb.query(DATABASE_TABLE_LISTES, new String[] {KEY_ID, KEY_NOM}, null, null, null, null, null);
    }

    public Cursor recupererListesId(int id) {
        return mDb.query(DATABASE_TABLE_LISTES, new String[] {KEY_ID, KEY_NOM}, null, new String[] {KEY_ID, String.valueOf(id)}, null, null, null);
    }

    /* Gestion Table Categorie */

    public long ajouterCategorie(String nom, String logo) {

        // A corriger avec les nouveaux champs

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM, nom);
        initialValues.put(KEY_LOGO, logo);
        return mDb.insert(DATABASE_TABLE_CATEGORIES, null, initialValues);
    }

    public boolean supprimerCategoriesId(long id) {
        return mDb.delete(DATABASE_TABLE_CATEGORIES, KEY_ID + "=" + id, null) > 0;
    }

    public boolean supprimerLesCategories() {
        return mDb.delete(DATABASE_TABLE_CATEGORIES, null, null) > 0;
    }

    public Cursor recupererCategories() {
        return mDb.query(DATABASE_TABLE_CATEGORIES, new String[] {KEY_ID, KEY_NOM, KEY_LOGO}, null, null, null, null, null);
    }

    public Cursor recupererCategoriesOrdreAlphabetique() {
        return mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_CATEGORIES + "ORDER BY nom asc", null);
    }

    public Cursor recupererCategorieId(int id) {
        return mDb.query(DATABASE_TABLE_CATEGORIES, new String[] {KEY_ID, KEY_NOM, KEY_LOGO}, null, new String[] {KEY_ID, String.valueOf(id)}, null, null, null);
    }


    /* Gestion Table Produits */

    public long ajouterProduit(String nom, int categorie, String logo) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NOM, nom);
        initialValues.put(KEY_ID_CATEGORIE, categorie);
        initialValues.put(KEY_LOGO, logo);
        return mDb.insert(DATABASE_TABLE_PRODUITS, null, initialValues);
    }

    public boolean supprimerProduit(long _id) {
        return mDb.delete(DATABASE_TABLE_PRODUITS, KEY_ID + "=" + _id, null) > 0;
    }

    public void supprimerTousLesProduits() {
        mDb.delete(DATABASE_TABLE_PRODUITS,null,null);
    }

    public boolean majProduit(long _id, String nom, int categorie, String logo) {

        ContentValues args = new ContentValues();
        args.put(KEY_NOM, nom);
        args.put(KEY_ID_CATEGORIE, categorie);
        args.put(KEY_LOGO, logo);
        return mDb.update(DATABASE_TABLE_PRODUITS, args, KEY_ID + "=" + _id, null) > 0;
    }

    public Cursor recupererProduits() {

        //return mDb.query(DATABASE_TABLE_PRODUITS, new String[] {KEY_ID, KEY_NOM, KEY_ID_CATEGORIE,KEY_LOGO}, null, null, null, null, null);
        return mDb.rawQuery("SELECT _id, nom, id_categorie, logo FROM produits ORDER BY nom ASC", null);
    }

    public Cursor recupererProduitsParId(long id) {

        //return mDb.query(DATABASE_TABLE_PRODUITS, new String[] {KEY_ID, KEY_NOM, KEY_ID_CATEGORIE,KEY_LOGO}, null, null, null, null, null);
        return mDb.rawQuery("SELECT _id, nom, logo FROM produits WHERE _id="+id, null);
    }

    public Cursor recupererProduitsDeListe(long id_liste) {
        return mDb.rawQuery("SELECT _id, nom FROM produits WHERE _id = (SELECT id_produit FROM elements WHERE id_liste="+id_liste+") ORDER BY nom asc", null);
    }

    public Cursor recupererProduitsOrdreAlphabetique() {
        return mDb.rawQuery("SELECT * FROM " + DATABASE_TABLE_PRODUITS + " ORDER BY nom", null);
    }

    public Cursor recupererProduitsSelonCategorie(int id_categorie){
        return mDb.query(DATABASE_TABLE_CATEGORIES,new String[] {KEY_ID, KEY_NOM,
                KEY_ID_CATEGORIE, KEY_LOGO},KEY_ID_CATEGORIE+" = "+id_categorie,null,null,null,null);
    }

    /* Gestion Table Elements */

    public long ajouterElement(long id_produit, String nom_produit, long id_liste, int quantite, int coche, String icone) {

        // A corriger avec les nouveaux champs

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID_PRODUIT, id_produit);
        initialValues.put("nom_produit", nom_produit);
        initialValues.put(KEY_ID_LISTE, id_liste);
        initialValues.put(KEY_QUANTITE, quantite);
        initialValues.put(KEY_COCHE, coche);
        initialValues.put("icone", icone);
        return mDb.insert(DATABASE_TABLE_ELEMENTS, null, initialValues);
    }

    public boolean supprimerElementId(long id) {
        return mDb.delete(DATABASE_TABLE_PRODUITS, KEY_ID + "=" + id, null) > 0;
    }

    public void supprimerElements() {
        mDb.delete(DATABASE_TABLE_ELEMENTS,null,null);
    }

    public Cursor recupererElementsId(long id) {
        //return mDb.query(DATABASE_TABLE_ELEMENTS, new String[] {KEY_ID, KEY_LOGO}, null, new String[] {KEY_ID, String.valueOf(id)}, null, null, null);
        return mDb.rawQuery("SELECT _id, id_produit FROM elements WHERE id_liste="+id,null);
    }

    public Cursor recupererElementsNonCochesId(long id) {
        //return mDb.query(DATABASE_TABLE_ELEMENTS, new String[] {KEY_ID, KEY_LOGO}, null, new String[] {KEY_ID, String.valueOf(id)}, null, null, null);
        return mDb.rawQuery("SELECT _id, id_produit, nom_produit, icone FROM elements WHERE id_liste="+id+" AND coche=0",null);
    }

    public Cursor recupererElementsCochesId(long id) {
        //return mDb.query(DATABASE_TABLE_ELEMENTS, new String[] {KEY_ID, KEY_LOGO}, null, new String[] {KEY_ID, String.valueOf(id)}, null, null, null);
        return mDb.rawQuery("SELECT _id, id_produit, nom_produit, icone FROM elements WHERE id_liste="+id+" AND coche=1",null);
    }

    public Cursor recupererNomsProduitsParElementsId(long id) {
        return mDb.rawQuery("SELECT a.nom FROM produits a INNER JOIN elements b ON a._id=b.id_produit WHERE b._id = ?", new String[]{String.valueOf(id)});
    }

    public boolean setElementCoche(long _id) {
        ContentValues args = new ContentValues();
        args.put(KEY_COCHE, 1);
        return mDb.update(DATABASE_TABLE_ELEMENTS, args, KEY_ID + "=" + _id, null) > 0;
    }

    public boolean isElementCoche(long id_produit) {
        Cursor cu = mDb.rawQuery("SELECT coche from elements WHERE id_produit="+id_produit, null);
        cu.moveToFirst();
        int etat = cu.getInt(cu.getColumnIndex("coche"));

        if(etat == 1) return true;
        else return false;
    }

    /* Gestion Table Parametres */

    public long ajouterParametre(long idListeActuelle) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID_LISTE_ACTUELLE, idListeActuelle);
        return mDb.insert(DATABASE_TABLE_PARAMETRES, null, initialValues);
    }

    public boolean majParametreIdListeActuelle(int id, long idListeActuelle) {
        ContentValues args = new ContentValues();
        args.put(KEY_ID_LISTE_ACTUELLE, idListeActuelle);
        return mDb.update(DATABASE_TABLE_PARAMETRES, args, KEY_ID + "=" + id, null) > 0;
    }

    // Récupérer les éléments de la table Paramètres
    public Cursor recupererParametres() {
        return mDb.query(DATABASE_TABLE_PARAMETRES, new String[] {KEY_ID, KEY_ID_LISTE_ACTUELLE}, null, null, null, null, null);
    }
}
