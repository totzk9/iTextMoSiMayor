package gte.com.itextmosimayor.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;

public class Queries {

    private static Queries instance;
    private SQLiteDatabase database;

    private Queries(Context context) {
        Context mContext = context.getApplicationContext();
        DatabaseHandler db = new DatabaseHandler(mContext);
        database = db.getReadableDatabase();
    }

    public static synchronized Queries getInstance(Context context) {
        if (instance == null) {
            instance = new Queries(context.getApplicationContext());
        }
        return instance;
    }

    public Cursor getDepartments(String DepartmentID) {
        return database.query(DBInfo.Department, null, "DepartmentID = ?", new String[]{DepartmentID}, null, null, null);
    }


    public Cursor getAllDepartments() {
        return database.query(DBInfo.Department, null, null, null, null, null, DBInfo.DEPARTMENTID + " ASC");
    }

    public Cursor getMessages() {
        return database.query(DBInfo.Message, null, null, null, null, null, DBInfo.MESSAGEID + " DESC");
    }

    public Cursor getConvo() {
        return database.query(DBInfo.Convo_Kuan, null, null, null, null, null, DBInfo.CONTENTID + " ASC");
    }
}
