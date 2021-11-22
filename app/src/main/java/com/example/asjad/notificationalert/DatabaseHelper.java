package com.example.asjad.notificationalert;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by Asjad on 4/16/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Contacts.db";

    //----------------Var for contacts

    public static final String CONTACTS_TABLE_NAME = "contacts_table";
    public static final String COL_CONTACT_ID = "CONTACT_ID";
    public static final String COL_CONTACT_NAME = "CONTACT_NAME";
    public static final String COL_CONTACT_NUMBER = "CONTACT_NUMBER";
    public static final String COL_CONTACT_UNIQUE_ID = "CONTACT_UNIQUE_ID";

    private static final String SQL_CREATE_TABLE_CONTACTS = "CREATE TABLE " + CONTACTS_TABLE_NAME + "("
            + COL_CONTACT_ID  +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_CONTACT_NAME +" TEXT, "
            + COL_CONTACT_UNIQUE_ID +" INTEGER, "
            + COL_CONTACT_NUMBER +" INTEGER)";

    //----------------Var for groups

    public static final String GROUPS_TABLE_NAME = "groups_table";
    public static final String COL_GROUP_ID = "GROUP_ID";
    public static final String COL_GROUP_NAME = "GROUP_NAME";

    private static final String SQL_CREATE_TABLE_GROUPS = "CREATE TABLE " + GROUPS_TABLE_NAME + "("
            + COL_GROUP_ID  +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_GROUP_NAME +" TEXT) ";

    //-----------------Var for members-----------

    public static final String MEMBERS_TABLE_NAME = "members_table";
    public static final String COL_MEMBER_ID = "MEMBER_ID";
    public static final String COL_MEMBER_GROUP_ID = "GROUP_ID";
    public static final String COL_MEMBER_CONTACT_ID = "CONTACT_ID";

    private static final String SQL_CREATE_TABLE_MEMBERS = "CREATE TABLE " + MEMBERS_TABLE_NAME + "("
            + COL_MEMBER_ID  +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_MEMBER_GROUP_ID  +" INTEGER, "
            + COL_MEMBER_CONTACT_ID +" INTEGER) ";

    //-----------------Var for messages-----

    public static final String MESSAGES_TABLE_NAME = "messages_table";
    public static final String COL_MESSAGES_ID = "MESSAGE_ID";
    public static final String COL_MESSAGES_GROUP_ID = "GROUP_ID";
    public static final String COL_MESSAGES_MESSAGE = "MESSAGE";
    public static final String COL_MESSAGES_STATUS = "STATUS";
    public static final String COL_MESSAGES_TIME = "TIME";

    private static final String SQL_CREATE_TABLE_MESSAGES = "CREATE TABLE " + MESSAGES_TABLE_NAME + "("
            + COL_MESSAGES_ID  +" INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_MESSAGES_GROUP_ID  +" INTEGER, "
            + COL_MESSAGES_MESSAGE  +" TEXT, "
            + COL_MESSAGES_STATUS  +" TEXT, "
            + COL_MESSAGES_TIME +" TEXT) ";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_CONTACTS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_GROUPS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MEMBERS);
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE_MESSAGES);


    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CONTACTS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GROUPS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MEMBERS_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MESSAGES_TABLE_NAME);
        onCreate(sqLiteDatabase);

    }


    //-------------------Functions for Contacts----------------


    public boolean insertContact(String unique_id, String name, String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTACT_NAME, name);
        contentValues.put(COL_CONTACT_NUMBER, number);
        contentValues.put(COL_CONTACT_UNIQUE_ID, unique_id);

        long result = db.insert(CONTACTS_TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean ifHasContact(String unique_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select " + COL_CONTACT_UNIQUE_ID + " from "+CONTACTS_TABLE_NAME + " Where " + COL_CONTACT_UNIQUE_ID + " = " + unique_id,null);
        boolean bool;
        if (res.getCount() == 0){
            bool = false;
        }else {
            bool = true;
        }
        return bool;
    }



    public boolean updateContact(String id,String name,String number) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CONTACT_ID ,id);
        contentValues.put(COL_CONTACT_NAME,name);
        contentValues.put(COL_CONTACT_NUMBER,number);
        db.update(CONTACTS_TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Cursor getAllContacts() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+CONTACTS_TABLE_NAME+" order by "+COL_CONTACT_ID+" DESC",null);
        return res;
    }


    //-----------------Functions for Group-------------------



    public long insertGroup(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GROUP_NAME, name);

        long result = db.insert(GROUPS_TABLE_NAME, null, contentValues);

        return result;
    }

    public boolean updateGroup(String id,String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_GROUP_ID ,id);
        contentValues.put(COL_GROUP_NAME,name);
        db.update(GROUPS_TABLE_NAME, contentValues, COL_GROUP_ID + " = ?",new String[] { id });
        return true;
    }

    public Cursor getAllGroups() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+GROUPS_TABLE_NAME+" ORDER BY "+COL_GROUP_ID+" DESC",null);
        return res;
    }

    public Integer deleteGroup (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        deleteAllMembers(id);
        deleteAllMessages(id);
        return db.delete(GROUPS_TABLE_NAME, COL_GROUP_ID +" = ?",new String[] {id});

    }


    //----------------Functions for Members------------------


    public boolean insertMember(String group_id, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MEMBER_GROUP_ID, group_id);
        contentValues.put(COL_MEMBER_CONTACT_ID, contact_id);

        long result = db.insert(MEMBERS_TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllMember() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + MEMBERS_TABLE_NAME, null);
        return res;
    }

    public Cursor getAllMember(String group_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Cursor res = db.rawQuery("select * from members_table where GROUP_ID =" + group_id,null);
        String sql = "select * ";
        sql += " from groups_table ";
        sql += " inner join members_table on groups_table.GROUP_ID=members_table.GROUP_ID ";
        sql += " inner join contacts_table on contacts_table.CONTACT_ID=members_table.CONTACT_ID ";
        sql += " WHERE groups_table.GROUP_ID= "+  group_id;
        Cursor res = db.rawQuery(sql ,null);
        return res;
    }

    public Cursor countAllMember(String group_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select count(*) as total from members_table where GROUP_ID= "+ group_id,null);
        return res;
    }

    public Integer deleteMember (String group_id, String contact_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MEMBERS_TABLE_NAME, COL_MEMBER_GROUP_ID +" = ? AND "+ COL_MEMBER_CONTACT_ID + " = ?",new String[] {group_id,contact_id});
    }

    public Integer deleteAllMembers (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MEMBERS_TABLE_NAME, COL_MEMBER_GROUP_ID +" = ?",new String[] {id});
    }

    //----------------Functions for Messages------------------


    public long insertMessages (String group_id, String message , String status, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MESSAGES_GROUP_ID, group_id);
        contentValues.put(COL_MESSAGES_MESSAGE, message);
        contentValues.put(COL_MESSAGES_STATUS, status);
        contentValues.put(COL_MESSAGES_TIME, time);

        long result = db.insert(MESSAGES_TABLE_NAME, null, contentValues);

        return result;
    }

    public boolean updateMessage(String id,String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_MESSAGES_ID ,id);
        contentValues.put(COL_MESSAGES_STATUS,status);
        db.update(MESSAGES_TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Cursor getAllMessages() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + MESSAGES_TABLE_NAME, null);
        return res;
    }

    public Cursor getAllMessages(String group_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from messages_table where GROUP_ID =" + group_id +" ORDER BY "+COL_MESSAGES_ID+" DESC",null);
        return res;
    }

    public Cursor countAllMessages (String group_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select count(*) as total from messages_table where GROUP_ID= "+ group_id,null);
        return res;
    }

    public Integer deleteMessage (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MESSAGES_TABLE_NAME, COL_MESSAGES_ID +" = ?",new String[] {id});
    }

    public Integer deleteAllMessages (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(MESSAGES_TABLE_NAME, COL_MESSAGES_GROUP_ID +" = ?",new String[] {id});
    }


}
