package com.jobayerjim.minecraftskins.controller.helper.dbhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.jobayerjim.minecraftskins.models.SkinsModel;

import java.util.ArrayList;

public class DatabaseAccess {
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase sqLiteDatabase;
    private static DatabaseAccess instance;
    Cursor cursor=null;
    private DatabaseAccess(Context context) {
        this.databaseHelper=new DatabaseHelper(context);

    }
    public static DatabaseAccess getInstance(Context context) {
        if (instance==null) {
            instance=new DatabaseAccess(context);
        }
        return instance;
    }
    public void open() {
        this.sqLiteDatabase=databaseHelper.getWritableDatabase();
    }
    public void close() {
        if (sqLiteDatabase!=null) {
            this.sqLiteDatabase.close();
        }
    }

    public ArrayList<SkinsModel> getAllData() {
        cursor=sqLiteDatabase.rawQuery("select * from skins",new String[]{});
        ArrayList<SkinsModel> data=new ArrayList<>();

        while (cursor.moveToNext()) {
            SkinsModel skinsModel=new SkinsModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));
            data.add(skinsModel);
        }
        return data;
    }
    public Boolean makeFavourite(int id,int favourite) {
        try {
            String sql="UPDATE skins SET favorite="+favourite+" WHERE _id="+id;
            Log.d("sql",sql);
            sqLiteDatabase.execSQL(sql);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public ArrayList<SkinsModel> getAllFavourite() {
        cursor=sqLiteDatabase.rawQuery("select * from skins where favorite=1",new String[]{});
        ArrayList<SkinsModel> data=new ArrayList<>();

        while (cursor.moveToNext()) {
            SkinsModel skinsModel=new SkinsModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3),cursor.getInt(4),cursor.getInt(5),cursor.getInt(6));
            data.add(skinsModel);
        }
        return data;
    }
}
