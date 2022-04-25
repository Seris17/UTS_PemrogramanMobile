package com.example.sigit.adapter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.sigit.Global;
import com.example.sigit.model.EntBuku;
import com.example.sigit.model.EntTransaksiBuku;
import com.example.sigit.model.EntTransaksiDetailBuku;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static final String database_name = "sbook_db";
    public static final String table_login = "table_login";
    public static final String login_id = "_id";
    public static final String login_email = "Email";
    public static final String login_username = "Username";
    public static final String login_password = "Password";

    public static final String table_master_buku = "table_master_buku";
    public static final String master_buku_id = "_id";
    public static final String master_buku_judul = "Judul";
    public static final String master_buku_harga = "Harga";
    public static final String master_buku_deskripsi = "Deskripsi";

    public static final String table_transaction_buku = "table_transaction";
    public static final String trans_buku_id = "_id";
    public static final String trans_buku_tanggal = "Tanggal";
    public static final String trans_buku_total = "Total";
    public static final String trans_buku_jumlah_barang = "Jumlahbarang";
    public static final String trans_buku_bayar = "Bayar";
    public static final String trans_buku_kembali = "Kembali";

    public static final String table_detail_transaction = "table_detail_transaction";
    public static final String trans_detail_buku_id = "_id";
    public static final String trans_detail_buku_trans_id = "TransId";
    public static final String trans_detail_buku_buku_id = "BukuId";
    public static final String trans_detail_buku_judul = "Judul";
    public static final String trans_detail_buku_harga = "Harga";
    public static final String trans_detail_buku_jumlah = "Jumlah";
    public static final String trans_detail_buku_subtotal = "Subtotal";


    private SQLiteDatabase db;

    public DBHelper(Context context) {
        super(context, database_name, null, 2);
        db = getWritableDatabase();
        EnsureTableCreated(db);
    }

    public boolean CheckTableExist(String tableName, SQLiteDatabase db) {
        String query = "select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'";
        try (Cursor cursor = db.rawQuery(query, null)) {
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    return true;
                }
            }
            return false;
        }
    }

    public void EnsureTableCreated(SQLiteDatabase db) {
        String query = "";
        if (!CheckTableExist(table_login, db)) {
            query += "CREATE TABLE " + table_login + "("
                    + login_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + login_email + " TEXT,"
                    + login_username + " TEXT,"
                    + login_password + " TEXT"
                    + ");";
        }
        if (!CheckTableExist(table_master_buku, db)) {
            query += "CREATE TABLE " + table_master_buku + "("
                    + master_buku_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + master_buku_judul + " TEXT,"
                    + master_buku_harga + " REAL,"
                    + master_buku_deskripsi + " TEXT"
                    + ");";
        }

        if (!CheckTableExist(table_transaction_buku, db)) {
            query += "CREATE TABLE " + table_transaction_buku + "("
                    + trans_buku_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + trans_buku_tanggal + " TEXT,"
                    + trans_buku_total + " REAL,"
                    + trans_buku_jumlah_barang + " REAL,"
                    + trans_buku_bayar + " REAL,"
                    + trans_buku_kembali + " REAL"
                    + ");";
        }

        if (!CheckTableExist(table_detail_transaction, db)) {
            query += "CREATE TABLE " + table_detail_transaction + "("
                    + trans_detail_buku_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + trans_detail_buku_trans_id + " TEXT,"
                    + trans_detail_buku_buku_id + " TEXT,"
                    + trans_detail_buku_judul + " TEXT,"
                    + trans_detail_buku_harga + " REAL,"
                    + trans_detail_buku_jumlah + " REAL,"
                    + trans_detail_buku_subtotal + " REAL"
                    + ");";
        }
        if (!query.isEmpty()) db.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + table_login + "("
                + login_id + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + login_email + " TEXT,"
                + login_username + " TEXT,"
                + login_password + " TEXT"
                + ");";

        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + table_login);
        db.execSQL("DROP TABLE IF EXISTS " + table_master_buku);
    }

    public void insertData(String table_name, ContentValues values) {
        db.insert(table_name, null, values);
    }

    public void updateData(String table_name, ContentValues values, String where, String id) {
        db.update(table_name, values, where + " = ?",
                new String[]{String.valueOf(id)});
    }

    public void deleteData(String table_name, String where, String id) {
        db.beginTransaction();
        try {
            db.delete(table_name, where + " = ?", new String[]{String.valueOf(id)});
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Global.Toas("error Delete " + e.getMessage());
        } finally {
            db.endTransaction();
        }
    }

    @SuppressLint("Range")
    public String GetMaxIdTransaction() {
        String query = String.format("SELECT " + DBHelper.trans_buku_id + " FROM " + DBHelper.table_transaction_buku + " order by " + DBHelper.trans_buku_id + " desc");

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndex(DBHelper.trans_buku_id));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return "1";
    }

    public boolean checkUser(String email, String password) {
        String[] columns = {login_id};
        SQLiteDatabase db = getReadableDatabase();
        String selection = login_email + "=?" + " and " + login_password + "=?";
        String[] selectionArgs = {email, password};
        Cursor cursor = db.query(table_login, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean checkemail(String email) {
        String[] columns = {login_id};
        SQLiteDatabase db = getReadableDatabase();
        String selection = login_email + "=?";
        String[] selectionArgs = {email};
        Cursor cursor = db.query(table_login, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public boolean checkPasswd(String password) {
        String[] columns = {login_id};
        SQLiteDatabase db = getReadableDatabase();
        String selection = login_password + "=?";
        String[] selectionArgs = {password};
        Cursor cursor = db.query(table_login, columns, selection, selectionArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    @SuppressLint("Range")
    public List<EntBuku> getAllBuku() {
        List<EntBuku> listBuku = new ArrayList<EntBuku>();

        String query = String.format("SELECT * FROM " + DBHelper.table_master_buku);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    EntBuku buku = new EntBuku();
                    buku._id = cursor.getString(cursor.getColumnIndex(DBHelper.master_buku_id));
                    buku.Judul = cursor.getString(cursor.getColumnIndex(DBHelper.master_buku_judul));
                    buku.Harga = cursor.getDouble(cursor.getColumnIndex(DBHelper.master_buku_harga));
                    buku.Deskripsi = cursor.getString(cursor.getColumnIndex(DBHelper.master_buku_deskripsi));

                    listBuku.add(buku);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return listBuku;
    }

    @SuppressLint("Range")
    public List<EntTransaksiBuku> getAllTransBuku() {
        List<EntTransaksiBuku> listTransBuku = new ArrayList<EntTransaksiBuku>();

        String query = String.format("SELECT * FROM " + DBHelper.table_transaction_buku);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    EntTransaksiBuku transBuku = new EntTransaksiBuku();
                    transBuku._id = cursor.getString(cursor.getColumnIndex(DBHelper.trans_buku_id));
                    transBuku.Tanggal = cursor.getString(cursor.getColumnIndex(DBHelper.trans_buku_tanggal));
                    transBuku.Total = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_buku_total));
                    transBuku.JumlahBarang = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_buku_jumlah_barang));
                    transBuku.Bayar = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_buku_bayar));
                    transBuku.Kembali = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_buku_kembali));

                    listTransBuku.add(transBuku);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return listTransBuku;
    }

    @SuppressLint("Range")
    public List<EntTransaksiDetailBuku> getAllTransBukuDetailByIdTransBuku(String transid) {
        List<EntTransaksiDetailBuku> listTransDetailBuku = new ArrayList<EntTransaksiDetailBuku>();

        String query = String.format("SELECT * FROM " + DBHelper.table_detail_transaction + "where " + DBHelper.trans_detail_buku_trans_id + " = " + transid);

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    EntTransaksiDetailBuku transDetailBuku = new EntTransaksiDetailBuku();
                    transDetailBuku._id = cursor.getString(cursor.getColumnIndex(DBHelper.trans_detail_buku_id));
                    transDetailBuku.TransId = cursor.getString(cursor.getColumnIndex(DBHelper.trans_detail_buku_trans_id));
                    transDetailBuku.Judul = cursor.getString(cursor.getColumnIndex(DBHelper.trans_detail_buku_judul));
                    transDetailBuku.Harga = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_detail_buku_harga));
                    transDetailBuku.Jumlah = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_detail_buku_jumlah));
                    transDetailBuku.Subtotal = cursor.getDouble(cursor.getColumnIndex(DBHelper.trans_detail_buku_subtotal));

                    listTransDetailBuku.add(transDetailBuku);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return listTransDetailBuku;
    }
}