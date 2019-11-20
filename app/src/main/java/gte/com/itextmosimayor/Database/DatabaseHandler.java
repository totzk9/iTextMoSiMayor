package gte.com.itextmosimayor.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import gte.com.itextmosimayor.constant.Constants;
import gte.com.itextmosimayor.database.DatabaseInfo.DBInfo;

public class DatabaseHandler extends SQLiteOpenHelper implements Constants {

    //DataBase Name

    private static final int DATABASE_VERSION = 10;

    /* -------------------------------------------------------------------------------------------*/

    //region DATABASE TABLES

    private String CREATE_MESSAGE = "CREATE TABLE " + DBInfo.Message + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT, " +
            DBInfo.MAYORID + " TEXT, " +
            DBInfo.PRIORITYLEVEL + " TEXT, " +
            DBInfo.ISASSIGNED + " TEXT, " +
            DBInfo.STATUS + " TEXT);";

    private String CREATE_DELETED_MESSAGE = "CREATE TABLE " + DBInfo.DeletedMessage + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT, " +
            DBInfo.MAYORID + " TEXT, " +
            DBInfo.PRIORITYLEVEL + " TEXT, " +
            DBInfo.ISASSIGNED + " TEXT, " +
            DBInfo.STATUS + " TEXT);";

    private String CREATE_CONFIDENTIAL_MESSAGE = "CREATE TABLE " + DBInfo.ConfidentialMessage + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT);";

    private String CREATE_UNASSIGNED_MESSAGE = "CREATE TABLE " + DBInfo.UnassignedMessage + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT, " +
            DBInfo.MAYORID + " TEXT, " +
            DBInfo.PRIORITYLEVEL + " TEXT, " +
            DBInfo.ISASSIGNED + " TEXT, " +
            DBInfo.STATUS + " TEXT);";

    private String CREATE_OPEN_MESSAGE = "CREATE TABLE " + DBInfo.OpenMessage + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT, " +
            DBInfo.MAYORID + " TEXT, " +
            DBInfo.PRIORITYLEVEL + " TEXT, " +
            DBInfo.ISASSIGNED + " TEXT, " +
            DBInfo.DEPARTMENTNAME + " TEXT, " +
            DBInfo.CONVOID + " TEXT, " +
            DBInfo.STATUS + " TEXT);";

    private String CREATE_RESOLVED_MESSAGE = "CREATE TABLE " + DBInfo.ResolvedMessage + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT, " +
            DBInfo.MAYORID + " TEXT, " +
            DBInfo.PRIORITYLEVEL + " TEXT, " +
            DBInfo.ISASSIGNED + " TEXT, " +
            DBInfo.DEPARTMENTNAME + " TEXT, " +
            DBInfo.STATUS + " TEXT);";

    private String CREATE_IMPORTANT_MESSAGE = "CREATE TABLE " + DBInfo.ImportantMessage + "(" +
            DBInfo._ID +
            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.CLIENTMOBILENUMBER + " TEXT, " +
            DBInfo.CONTENT + " TEXT, " +
            DBInfo.MAYORID + " TEXT, " +
            DBInfo.PRIORITYLEVEL + " TEXT, " +
            DBInfo.ISASSIGNED + " TEXT, " +
            DBInfo.DEPARTMENTNAME + " TEXT, " +
            DBInfo.CONVOID + " TEXT, " +
            DBInfo.STATUS + " TEXT);";

//    String CREATE_MAYOR = "CREATE TABLE " + DBInfo.Mayor + "(" +
//            DBInfo._ID +
//            DBInfo.MAYORID + " TEXT, " +
//            DBInfo.DATEELECTED + " TEXT, " +
//            DBInfo.ISADMIN + " TEXT," +
//            DBInfo.MAYORCODE + " TEXT);";

    private String CREATE_DEPARTMENT = "CREATE TABLE " + DBInfo.Department + "(" +
            DBInfo._ID +
            DBInfo.DEPARTMENTID + " TEXT, " +
            DBInfo.DEPARTMENTNAME + " TEXT, " +
            DBInfo.STREETNAME + " TEXT, " +
            DBInfo.BARANGAY + " TEXT, " +
            DBInfo.MUNICIPALITY + " TEXT, " +
            DBInfo.PROVINCE + " TEXT, " +
            DBInfo.ZIPCODE + " TEXT, " +
            DBInfo.DEPARTMENTHEAD + " TEXT, " +
            DBInfo.DEPARTMENTCODE + " TEXT);";

//    String CREATE_DEPARTMENT_ADDRESS = "CREATE TABLE " + DBInfo.Department_Address + "(" +
//            DBInfo._ID +
//            DBInfo.ADDRESSID + " TEXT, " +
//            DBInfo.STREETNAME + " TEXT, " +
//            DBInfo.BARANGAY + " TEXT, " +
//            DBInfo.MUNICIPALITY + " TEXT, " +
//            DBInfo.PROVINCE + " TEXT, " +
//            DBInfo.ZIPCODE + " TEXT, " +
//            DBInfo.DEPARTMENTID + " TEXT, " +
//            DBInfo.DEPARTMENTCODE + " TEXT);";
//
//    String CREATE_DEPARTMENT_CODE = "CREATE TABLE " + DBInfo.Department_Code + "(" +
//            DBInfo._ID +
//            DBInfo.DEPARTMENTCODEID + " TEXT, " +
//            DBInfo.DEPARTMENTCODE + " TEXT);";


//    String CREATE_CONVO_CONTENT = "CREATE TABLE " + DBInfo.Convo_Content + " (" +
//            DBInfo._ID +
//            DBInfo.CONVOID + " TEXT, " +
//            DBInfo.MESSAGEID + " TEXT, " +
//            DBInfo.CONTENTID + " TEXT, " +
//            DBInfo.DEPARTMENTID + " TEXT, " +
//            DBInfo.MESSAGECONTENT + " TEXT, " +
//            DBInfo.DATESENT + " TEXT, " +
//            DBInfo.SENTBY + " TEXT);";

    private String CREATE_CONVO_KUAN = "CREATE TABLE " + DBInfo.Convo_Kuan + " (" +
            DBInfo._ID +
//            DBInfo.MESSAGEID + " TEXT, " +
            DBInfo.CONTENTID + " TEXT, " +
            DBInfo.CONVOID + " TEXT, " +
            DBInfo.MESSAGECONTENT + " TEXT, " +
            DBInfo.DATESENT + " TEXT, " +
            DBInfo.SENTBY + " TEXT);";

    //endregion

    /* -------------------------------------------------------------------------------------------*/

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USER = "CREATE TABLE " + DBInfo.User + "(" +
                DBInfo._ID +
                DBInfo.USERID + " TEXT, " +
                DBInfo.USERNAME + " TEXT, " +
                DBInfo.PASSWORD + " TEXT, " +
                DBInfo.FIRSTNAME + " TEXT, " +
                DBInfo.LASTNAME + " TEXT, " +
                DBInfo.MOBILENUMBER + " TEXT, " +
                DBInfo.DEPARTMENTID + " TEXT, " +
                DBInfo.MAYORID + " TEXT);";


        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_MESSAGE);
//        db.execSQL(CREATE_MAYOR);
        db.execSQL(CREATE_DEPARTMENT);
//        db.execSQL(CREATE_DEPARTMENT_ADDRESS);
//        db.execSQL(CREATE_DEPARTMENT_CODE);
//        db.execSQL(CREATE_CONVO);
//        db.execSQL(CREATE_CONVO_CONTENT);
        db.execSQL(CREATE_UNASSIGNED_MESSAGE);
        db.execSQL(CREATE_OPEN_MESSAGE);
        db.execSQL(CREATE_RESOLVED_MESSAGE);
        db.execSQL(CREATE_IMPORTANT_MESSAGE);
        db.execSQL(CREATE_DELETED_MESSAGE);
        db.execSQL(CREATE_CONVO_KUAN);
        db.execSQL(CREATE_CONFIDENTIAL_MESSAGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.User);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Message);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.AllMessage);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.UnassignedMessage);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.OpenMessage);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.ResolvedMessage);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.ImportantMessage);
//        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Mayor);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Department);
//        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Department_Address);
//        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Department_Code);
//        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Convo);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.Convo_Content);
        db.execSQL("DROP TABLE IF EXISTS " + DBInfo.ConfidentialMessage);
        onCreate(db);
    }

    @Override
    protected void finalize() throws Throwable {
//        this.close();
        super.finalize();
    }

    /* -------------------------------------------------------------------------------------------*/


    public void TRUNCATE_CONVO_KUAN() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.Convo_Kuan);
        sql.execSQL(CREATE_CONVO_KUAN);
    }

    public void INSERT_CONVO_KUAN(String content, String datesent, String sentby, String convoID,
                                  String contentID) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DBInfo.CONVOID, convoID);
//        cv.put(DBInfo.MESSAGEID, messageID);
        cv.put(DBInfo.MESSAGECONTENT, content);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.SENTBY, sentby);
        cv.put(DBInfo.CONTENTID, contentID);

        sql.insert(DBInfo.Convo_Kuan, null, cv);
    }

    /* -------------------------------------------------------------------------------------------*/

    //region DEPARTMENT

    public void TRUNCATE_DEPARTMENT() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.Department);
        sql.execSQL(CREATE_DEPARTMENT);
    }

    public void INSERT_DEPARTMENT(Object id, Object name, Object street, Object brgy,
                                  Object municipal, Object province, Object zipcode, Object head, Object code) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.DEPARTMENTID, id + "");
        cv.put(DBInfo.DEPARTMENTNAME, name + "");
        cv.put(DBInfo.STREETNAME, street + "");
        cv.put(DBInfo.BARANGAY, brgy + "");
        cv.put(DBInfo.MUNICIPALITY, municipal + "");
        cv.put(DBInfo.PROVINCE, province + "");
        cv.put(DBInfo.ZIPCODE, zipcode + "");
        cv.put(DBInfo.DEPARTMENTHEAD, head + "");
        cv.put(DBInfo.DEPARTMENTCODE, code + "");

        sql.insert(DBInfo.Department, null, cv);
    }

    public long DEPARTMENT_ENTRIES_COUNT() {
        SQLiteDatabase sd = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(sd, DBInfo.Department);
        return cnt;
    }

    //endregion

    /* -------------------------------------------------------------------------------------------*/

    //region USER

    public long USER_ENTRIES_COUNT() {
        SQLiteDatabase sd = this.getReadableDatabase();
        long cnt = DatabaseUtils.queryNumEntries(sd, DBInfo.User);
//        sd.close();
        return cnt;
    }

    public void INSERT_USER_INFO(int userID, String username, String password, String firstName, String lastName,
                                 String mobileNumber, String departmentID, int mayorID) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.USERID, userID);
        cv.put(DBInfo.USERNAME, username);
        cv.put(DBInfo.PASSWORD, password);
        cv.put(DBInfo.FIRSTNAME, firstName);
        cv.put(DBInfo.LASTNAME, lastName);
        cv.put(DBInfo.MOBILENUMBER, mobileNumber);
        cv.put(DBInfo.DEPARTMENTID, departmentID);
        cv.put(DBInfo.MAYORID, mayorID);

        sql.insert(DBInfo.User, null, cv);
    }

    //endregion

    /* -------------------------------------------------------------------------------------------*/

    //region MESSAGES

    public void INSERT_MESSAGE(String messageid, String datesent, String clientmobilenumber, String content, String
            mayorid, String prioritylevel, String isAssigned, String status) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);
        cv.put(DBInfo.MAYORID, mayorid);
        cv.put(DBInfo.PRIORITYLEVEL, prioritylevel);
        cv.put(DBInfo.ISASSIGNED, isAssigned);
        cv.put(DBInfo.STATUS, status);

        sql.insert(DBInfo.Message, null, cv);
    }

    public void INSERT_UNASSIGNED_MESSAGE(String messageid, String datesent, String clientmobilenumber,
                                          String content, String mayorid, String prioritylevel, Object isAssigned, String status) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);
        cv.put(DBInfo.MAYORID, mayorid);
        cv.put(DBInfo.PRIORITYLEVEL, prioritylevel);
        cv.put(DBInfo.ISASSIGNED, isAssigned + "");
        cv.put(DBInfo.STATUS, status);

        sql.insert(DBInfo.UnassignedMessage, null, cv);
    }

    public void TRUNCATE_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.Message);
        sql.execSQL(CREATE_MESSAGE);
    }

    public void TRUNCATE_UNASSIGNED_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.UnassignedMessage);
        sql.execSQL(CREATE_UNASSIGNED_MESSAGE);
    }

    public void INSERT_OPEN_MESSAGE(String messageid, String datesent, String clientmobilenumber, String content, String
            mayorid, String prioritylevel, String isAssigned, String status, String departmentID, String convoID) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);
        cv.put(DBInfo.MAYORID, mayorid);
        cv.put(DBInfo.PRIORITYLEVEL, prioritylevel);
        cv.put(DBInfo.ISASSIGNED, isAssigned);
        cv.put(DBInfo.STATUS, status);
        cv.put(DBInfo.DEPARTMENTNAME, departmentID);
        cv.put(DBInfo.CONVOID, convoID);

        sql.insert(DBInfo.OpenMessage, null, cv);
    }

    public void TRUNCATE_OPEN_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.OpenMessage);
        sql.execSQL(CREATE_OPEN_MESSAGE);
    }

    public void INSERT_RESOLVED_MESSAGE(String messageid, String datesent,
                                        String clientmobilenumber, String content, String mayorid,
                                        String prioritylevel, String isAssigned, String status) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);
        cv.put(DBInfo.MAYORID, mayorid);
        cv.put(DBInfo.PRIORITYLEVEL, prioritylevel);
        cv.put(DBInfo.ISASSIGNED, isAssigned);
        cv.put(DBInfo.STATUS, status);

        sql.insert(DBInfo.ResolvedMessage, null, cv);
    }

    public void TRUNCATE_RESOLVED_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.ResolvedMessage);
        sql.execSQL(CREATE_RESOLVED_MESSAGE);
    }

    public void INSERT_DELETED_MESSAGE(String messageid, String datesent,
                                       String clientmobilenumber, String content, String mayorid,
                                       String prioritylevel, String isAssigned, String status) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);
        cv.put(DBInfo.MAYORID, mayorid);
        cv.put(DBInfo.PRIORITYLEVEL, prioritylevel);
        cv.put(DBInfo.ISASSIGNED, isAssigned);
        cv.put(DBInfo.STATUS, status);

        sql.insert(DBInfo.DeletedMessage, null, cv);
    }

    public void TRUNCATE_DELETED_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.DeletedMessage);
        sql.execSQL(CREATE_DELETED_MESSAGE);
    }

    public void INSERT_CONFIDENTIAL_MESSAGE(String messageid, String datesent,
                                            String clientmobilenumber, String content) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);

        sql.insert(DBInfo.ConfidentialMessage, null, cv);
    }

    public void TRUNCATE_CONFIDENTIAL_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.ConfidentialMessage);
        sql.execSQL(CREATE_CONFIDENTIAL_MESSAGE);
    }

    public void INSERT_IMPORTANT_MESSAGE(String messageid, String datesent, String clientmobilenumber,
                                         String content, String mayorid, String prioritylevel, String isAssigned, String status,
                                         String departmentID, String convoID) {
        SQLiteDatabase sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(DBInfo.MESSAGEID, messageid);
        cv.put(DBInfo.DATESENT, datesent);
        cv.put(DBInfo.CLIENTMOBILENUMBER, clientmobilenumber);
        cv.put(DBInfo.CONTENT, content);
        cv.put(DBInfo.MAYORID, mayorid);
        cv.put(DBInfo.PRIORITYLEVEL, prioritylevel);
        cv.put(DBInfo.ISASSIGNED, isAssigned);
        cv.put(DBInfo.STATUS, status);
        cv.put(DBInfo.DEPARTMENTNAME, departmentID);
        cv.put(DBInfo.CONVOID, convoID);

        sql.insert(DBInfo.ImportantMessage, null, cv);
    }

    public void TRUNCATE_IMPORTANT_MESSAGE() {
        SQLiteDatabase sql = this.getWritableDatabase();
        sql.execSQL("DROP TABLE IF EXISTS " + DBInfo.ImportantMessage);
        sql.execSQL(CREATE_IMPORTANT_MESSAGE);
    }
    //endregion
}