package com.example.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class MySQLiteHelper extends SQLiteOpenHelper {
    private Context context;

    public MySQLiteHelper(Context context, String name,
                          SQLiteDatabase.CursorFactory factory, int version) {

        super(context, "final.db", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE tem" +
                "(_id INTEGER PRIMARY KEY,tem_account_name TEXT)";
        db.execSQL(CREATE_TABLE);

        String sql = "INSERT INTO tem (_id,tem_account_name) VALUES (1,'')";
        db.execSQL(sql);

        String CREATE_TABLE2 = "CREATE TABLE member" +
                "(_id INTEGER PRIMARY KEY,account_name TEXT, identity TEXT)";
        db.execSQL(CREATE_TABLE2);

        String CREATE_TABLE3 = "CREATE TABLE class" +
                "(_id INTEGER PRIMARY KEY,account_name TEXT, class_name TEXT, classtime TEXT, teacher_name TEXT)";
        db.execSQL(CREATE_TABLE3);

        String CREATE_TABLE4 = "CREATE TABLE message" +
                "(_id INTEGER PRIMARY KEY,account_name TEXT, class_name TEXT, title TEXT, stu_name TEXT, stu_id TEXT, type TEXT, leave TEXT, attachment TEXT, content TEXT, leave_allow TEXT, stu_account_name, Notice INTEGER)";
        db.execSQL(CREATE_TABLE4);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        // TODO Auto-generated method stub
        db.execSQL("drop table if exists member");
        onCreate(db);
//        db.execSQL("drop table if exists tem");
//        onCreate(db);
    }

    public ArrayList<HashMap<String, String>> getMessageByName(String account_name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM message WHERE account_name = '" + account_name + "'", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String message_id = c.getString(0);
            String title = c.getString(3);
            String stu_name = c.getString(4);
            String stu_id = c.getString(5);
            String type = c.getString(6);

            hashMap.put("message_id", message_id);
            hashMap.put("title", title);
            hashMap.put("stu_name", stu_name);
            hashMap.put("stu_id", stu_id);
            hashMap.put("type", type);

            arrayList.add(hashMap);
        }
        return arrayList;
    }
    public ArrayList<HashMap<String, String>> getAllClass() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(" SELECT * FROM class", null);
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        while (c.moveToNext()) {
            HashMap<String, String> hashMap = new HashMap<>();

            String class_id = c.getString(0);
            String class_name = c.getString(2);
            String classtime = c.getString(3);
            String teacher_name = c.getString(4);

            hashMap.put("class_id", class_id);
            hashMap.put("class_name", class_name);
            hashMap.put("classtime", classtime);
            hashMap.put("teacher_name", teacher_name);
            arrayList.add(hashMap);
        }
        return arrayList;
    }
}
