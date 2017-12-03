package colorlines.color_lines;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by yoavl_000 on 03/12/2017.
 */

public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "topScores";

    // Contacts table name
    private static final String TABLE_SCORES = "scores";

    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_SCORE= "score";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_SCORES + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_SCORE + " INTEGER" +" )";
        db.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SCORES);
        // Creating tables again
        onCreate(db);
    }


    // Adding a new score
    public void addScore(int score) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_SCORE, score); // Shop Name

        // Inserting Row
        db.insert(TABLE_SCORES, null, values);
        db.close(); // Closing database connection
    }

    public List<Integer> getTwelveScores()
    {
        List<Integer> twelveScores= new ArrayList<>();
        //  String selectQuery = "SELECT" +KEY_SCORE+ "FROM " + TABLE_SCORES+ " LIMIT 12";
//        String selectQuery="SELECT %s FROM %s LIMIT 12";
//        selectQuery= String.format(selectQuery, KEY_SCORE, TABLE_SCORES);

        // String selectQuery="SELECT "+KEY_SCORE+" FROM "+TABLE_SCORES +" ORDER BY "+KEY_SCORE +" DESC LIMIT 12"; //the problem with this method is that if there is less then 12 scores. the scores will repeat is self
        String selectQuery="SELECT "+KEY_SCORE+" FROM "+TABLE_SCORES +" ORDER BY "+KEY_SCORE+ " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                // Adding contact to list
                int scoreFromDb=Integer.parseInt(cursor.getString(0));
                twelveScores.add(scoreFromDb);
            } while (cursor.moveToNext());
        }

        // return contact list
        return twelveScores;

    }
}

