package th.ac.up.agr.thai_mini_chicken.SQLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import th.ac.up.agr.thai_mini_chicken.R
import android.content.ContentValues
import android.database.Cursor
import androidx.fragment.app.FragmentActivity


class AppTheme(private var context: FragmentActivity) : SQLiteOpenHelper(context, Con.NAME, null, Con.VERSION) {

    var TABLE_NAME = "APP_THEME"
    var COL_STYLE = "STYLE"
    private var COL_MATERIAL = "MATERIAL"


    override fun onCreate(db: SQLiteDatabase?) {
        db!!.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_STYLE + " INTEGER);")
        db.execSQL("INSERT INTO " + TABLE_NAME + " (" + COL_STYLE + ") VALUES (" + R.style.MelonTheme_Amber_Material + ");")
        /*
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
        + COL_NAME + " TEXT, " + COL_PIECE_PRICE + " INTEGER, "
        + COL_CAKE_PRICE + " INTEGER);")
        */
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")

        onCreate(db)
    }

    fun update(db: SQLiteDatabase?, style: Int) {
        //db!!.execSQL("UPDATE " + TABLE_NAME + " SET username='android', password='jellybean', level=99 WHERE username='sleeping' AND password='forless' AND level=19;\n")
        val cv = ContentValues()
        cv.put(COL_STYLE, style)
        db!!.update(TABLE_NAME, cv, "_id=" + 1, null)
    }

    fun read() :Int {
        var database: AppTheme = AppTheme(context)
        var sqLiteDatabase: SQLiteDatabase = database.writableDatabase
        var cursor: Cursor = sqLiteDatabase.rawQuery(("SELECT " + database.COL_STYLE + " FROM " + database.TABLE_NAME), null)
        cursor.moveToFirst()
        var x = cursor.getInt(cursor.getColumnIndex(database.COL_STYLE))
        return x
    }

    class Con {
        companion object {
            val NAME = "APP_THEME"
            val VERSION = 1
        }
    }
}