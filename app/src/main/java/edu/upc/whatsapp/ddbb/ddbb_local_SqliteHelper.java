package edu.upc.whatsapp.ddbb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ddbb_local_SqliteHelper extends SQLiteOpenHelper {

  public ddbb_local_SqliteHelper(Context context, String name, CursorFactory factory, int version) {
    super(context, name, factory, version);
  }

  @Override
  //called when the database is created for the first time:
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(ddbb_local_Contract.Message.CREATE_TABLE);
  }

  //called when a new_version, greater than the old_version is requested to open the database:
  @Override
  public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
    db.execSQL(ddbb_local_Contract.Message.DROP_TABLE);
    onCreate(db);
  }
}
