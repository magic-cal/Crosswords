package com.example.callum.first.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.ArrayList;

public class DataBaseHelper extends SQLiteOpenHelper {

    //The Android's default system path of your application database.
    private static String databasepath = "/data/data/com.example.callum.first/databases/";

    private static String database = "LinkedDatabase";

    private SQLiteDatabase myDataBase;

    private final Context myContext;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {

        super(context, database, null, 1);
        this.myContext = context;
    }


    public void insert() {
//	myDataBase.execSQL("INSERT INTO level VALUES('1','board1','answers1');");
//	TEGER PRIMARY KEY,\"board\" TEXT,\"answers\" TEXT\"");
//	myDataBase.execSQL("DROP TABLE LinkedDatabase.Level;");
    }

    public void logIn(String username) {
        String query = "UPDATE Users SET loggedIn ='1' WHERE username = '" + username + "' ;";
        getWritableDatabase().execSQL(query);
//        createDetail();
    }

    public void logOut() {
        String query = "UPDATE Users SET loggedIn ='0' WHERE loggedIn = '1';";
        getWritableDatabase().execSQL(query);
    }

    public void saveGame(String passedpboard) {
        String query = "UPDATE Users SET pboard ='" + passedpboard + "' WHERE loggedIn = '1' AND plevel > '0';";
        getWritableDatabase().execSQL(query);
    }

    public String getLevel() {
        try {
            Cursor resultSet = getReadableDatabase().rawQuery("Select plevel from users where loggedIn ='1' AND plevel > '0'", null);
//			Log.d(resultSet.getColumnCount() + "", "no of columns");
            resultSet.moveToFirst();
            String temp = resultSet.getString(0);
            resultSet.close();
            return (temp);
        } catch (Exception e) {
            return "0";
        }
    }

    public void createDetail() {
        String plevel = read("Users", "plevel", "loggedIn='1'");
        String usernmame = read("Users", "username", "loggedIn = '1'");
        Log.d(plevel, usernmame);
        String query = ("INSERT INTO details VALUES('" + plevel + "','" + usernmame + "','0','0')");
        getWritableDatabase().execSQL(query);
//		read("Level","board","levelNo = "+NewLevel+"")
    }

    public void changeLevel(int NewLevel) {
        Log.d("New level", NewLevel + "");
        String query = "UPDATE Users SET plevel ='" + NewLevel + "' WHERE loggedIn = '1';";
        String query1 = "UPDATE Users SET pboard ='" + read("Level", "board", "levelNo = " + NewLevel + "") + "' WHERE loggedIn = '1';";
        getWritableDatabase().execSQL(query);
        getWritableDatabase().execSQL(query1);
        createDetail();
    }

    public boolean CheckIsDataAlreadyInDBorNot(String table, String field, String desiredValue) {
        String Query = "Select * from " + table + " where " + field + " = " + desiredValue;
        Log.d("Here", "Iam");
        Cursor resultSet = getReadableDatabase().rawQuery(Query, null);
        if (resultSet.getCount() <= 0) {
            resultSet.close();
            Log.d("Here", "false");
            return false;
        }
        resultSet.close();
        Log.d("Here", "true");
        return true;
    }

    public void adduser(String Username, String Password) {
        getWritableDatabase().execSQL("INSERT INTO users VALUES('" + Username + "','" + Password + "','" + read("Level", "board", "levelNo = 1") + "','1','1');");

    }

    public void updateDetails(int time, int guesses) {
        String plevel = read("Users", "plevel", "loggedIn='1'");
        String usernmame = read("Users", "username", "loggedIn = '1'");
        Log.d("facing it"+plevel, usernmame);
        int oldTime = Integer.parseInt(read("Details", "time", "levelNo= '" + plevel + "' AND username ='" + usernmame + "'"));
        int oldGuesses = Integer.parseInt(read("Details", "guesses", "levelNo= '" + plevel + "' AND username ='" + usernmame + "'"));
        Log.d("guesses "+guesses,"old guesses "+oldGuesses);
        guesses = guesses+oldGuesses;
        time = time +oldTime;

        String query = "UPDATE Details SET time ='" + (time) + "' WHERE username = '"+usernmame+"' and levelNo = '"+plevel+"';";
        String query1 = "UPDATE Details SET guesses ='" + (guesses) + "' WHERE username = '"+usernmame+"' and levelNo = '"+plevel+"';";
        getWritableDatabase().execSQL(query);
        getWritableDatabase().execSQL(query1);
    }

    public boolean CheckIsDataAlreadyInDBorNot(String Password, String Username) {
//		String Query = "Select * from " + TableName + " where " + dbfield + " = '" + fieldValue +"' AND username = "+Username;
        String Query = "Select * from users where username = '" + Username + "' AND password = '" + Password + "'";
        Log.d("Here", "Iam");
        Cursor results = getReadableDatabase().rawQuery(Query, null);
        if (results.getCount() <= 0) {
            results.close();
            return false;
        }
        results.close();
        return true;
    }

    public String read(String tableName, String entityName) {
        Cursor results = getReadableDatabase().rawQuery("Select " + entityName + " from " + tableName, null);
        Log.d(results.getColumnCount() + "", "no");
        results.moveToFirst();
        String temp = results.getString(0);
        results.close();
        return temp;

    }

    public String read(String tableName, String entityName, String condition) {
        Cursor results = getReadableDatabase().rawQuery("Select " + entityName + " from " + tableName + " where " + condition, null);
//		Cursor results = getReadableDatabase().rawQuery("Select answers from Level where levelNo ='1'", null);
        Log.d(results.getColumnCount() + "", "no");
        results.moveToFirst();
        String temp = results.getString(0);
        results.close();
        return temp;

    }

    public void read() {
        Cursor results = myDataBase.rawQuery("Select * from users", null);
        results.moveToFirst();
        results.getColumnCount();
        String username = results.getString(0);
        String password = results.getString(1);
        String pboard = results.getString(2);
        String plevel = results.getString(3);
        Log.d("username", "" + username);
        Log.d("password", password);
        Log.d("pboard", pboard);
        Log.d("plevel", plevel);
        results.close();
    }

    public ArrayList<String> getHighScores (int LevelNo,String sortBy){
        String username=null;
        String time = null;
        String guesses = null;
        ArrayList<String> data=new ArrayList<String>();
        String query ="Select username, time, guesses from details where levelNo = '" + LevelNo+"'"+sortBy;
        Cursor results = getReadableDatabase().rawQuery(query, null);
        results.moveToFirst();
        data.add("Username");
        data.add("Time");
        data.add("Guesses");
        while(results.moveToNext()){
            username=results.getString(0);
            time =results.getString(1);
            guesses =results.getString(2);
            data.add(username);
            data.add(time);
            data.add(guesses);
        }
        results.close();
        return data;

    }

//         this creates a empty database on the system and rewrites it with the databse from my assets folder

    public void createDataBase() {
        boolean dbExist = checkDataBase();
        if (dbExist) {
            //database already exists
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (Exception e) {
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {

        SQLiteDatabase checkDB = null;

        try {
            String myPath = databasepath + database;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
            //database does't exist yet.
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null ? true : false;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() {
        try {
            //Open your local db as the input stream
            InputStream myInput = myContext.getAssets().open(database);

            // Path to the just created empty db
            String outFileName = databasepath + database;

            //Open the empty db as the output stream
            OutputStream myOutput = new FileOutputStream(outFileName);

            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            //Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }

    }

    public void openDataBase() throws SQLException {

        //Open the database
        String myPath = databasepath + database;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
    }

    @Override
    public synchronized void close() {

        if (myDataBase != null)
            myDataBase.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            copyDataBase();
    }

}