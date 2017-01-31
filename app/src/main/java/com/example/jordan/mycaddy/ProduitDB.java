package com.example.jordan.mycaddy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ProduitDB {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_QUANTITY = "quantity";

    private static final String TAG = "ProduitDB";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    ///Variables utilisées pour la création de la base
    private static final String DATABASE_CREATE =
            "create table produits (_id integer primary key autoincrement, "
                    + "name text not null, quantity integer not null);";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "produits";
    private static final int DATABASE_VERSION = 2;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS produits");
            onCreate(db);
        }
    }

    public ProduitDB(Context ctx) {
        this.mCtx = ctx;
    }

    public ProduitDB open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    /**
     * Ajoute un produit avec son nom. La quantité est par défaut à 1. Si le produit est bien créé,
     * retourne la nouvelle rowId pour ce produit, sinon retourne -1
     * @param name le nom du produit
     * @return rowId ou -1 si erreur
     */
    public long addproduit(String name) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_QUANTITY, 1);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    /**
     * Efface un produit de la liste, identifié par son ID
     * @param rowId l'id du produit à supprimer
     * @return true si le produit a été effacé
     */
    public boolean deleteproduit(long rowId) {
        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    /**
     * Efface toute la liste de produit
     */
    public void deleteAllNote() {
        mDb.delete(DATABASE_TABLE,null,null);
    }

    /**
     * Mettre à jour le produit avec les paramètres donnés.
     * @param rowId id du produit à mettre à jour
     * @param name nom du produit
     * @param quantity quantité du produit
     * @return vrai si le produit est mis à jour, faux sinon.
     */
    public boolean updateproduit(long rowId, String name, int quantity) {
        ContentValues args = new ContentValues();
        args.put(KEY_NAME, name);
        args.put(KEY_QUANTITY, quantity);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }


    public Cursor fetchAllNotes() {
        return mDb.query(DATABASE_TABLE, new String[] {KEY_ROWID, KEY_NAME,
                KEY_QUANTITY}, null, null, null, null, null);
    }

}
