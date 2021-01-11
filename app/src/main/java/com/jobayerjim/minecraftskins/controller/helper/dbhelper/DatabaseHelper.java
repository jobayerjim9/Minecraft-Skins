package com.jobayerjim.minecraftskins.controller.helper.dbhelper;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {
    private static final String DB_NAME="db.db";
    private static final int DB_VERSION=1;

    public DatabaseHelper(Context context) {
        super(context,DB_NAME,null,DB_VERSION);

    }
}
