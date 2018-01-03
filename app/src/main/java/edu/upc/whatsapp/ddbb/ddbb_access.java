package edu.upc.whatsapp.ddbb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;
import entity.Message;
import entity.UserInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ddbb_access {

  SQLiteDatabase myDB;
  ddbb_local_SqliteHelper helper;
  Context context;

  public ddbb_access(Context context) {
    this.context = context;
    //a new data base version executes: onUpgrade() of SQLiteOpenHelper:
    helper = new ddbb_local_SqliteHelper(context, ddbb_local_Contract.DATABASE_NAME, null, ddbb_local_Contract.DATABASE_VERSION);
    myDB = helper.getWritableDatabase();
  }
  public List<Message> loadMessages(int from_user, int to_user) {
    List<Message> messages = new ArrayList<Message>();
    try {
      String[] args = new String[4];
      
      args[0] = from_user+"";
      args[1] = to_user+"";
      
      args[2] = to_user+"";
      args[3] = from_user+"";
      
      Cursor cursor = myDB.query(ddbb_local_Contract.Message.TABLE_NAME, null, 
        "(user_sender=? AND user_receiver=?) OR (user_sender=? AND user_receiver=?)", 
        args, null, null, "date ASC");
      if (cursor.moveToFirst()) {
        do {
          Message message = new Message();
          
          //...
          
          messages.add(message);
        }
        while (cursor.moveToNext());
      }
      cursor.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return messages;
  }
  public void saveMessage(Message message) {
      ContentValues values = new ContentValues();
      values.put(ddbb_local_Contract.Message.COLUMN_ID, message.getId());
      values.put(ddbb_local_Contract.Message.COLUMN_CONTENT, message.getContent());
      values.put(ddbb_local_Contract.Message.COLUMN_USER_SENDER, message.getUserSender().getId());
      values.put(ddbb_local_Contract.Message.COLUMN_USER_RECEIVER, message.getUserReceiver().getId());
      values.put(ddbb_local_Contract.Message.COLUMN_DATE, message.getDate().getTime());
      long newRowId = myDB.insert(ddbb_local_Contract.Message.TABLE_NAME, null, values);
      Toast.makeText(context, "new message saved id: " + newRowId, Toast.LENGTH_SHORT).show();
  }
  public void deleteMessage(Message message) {
    int num = myDB.delete(ddbb_local_Contract.Message.TABLE_NAME, message.getId() + "", null);
    Toast.makeText(context, "deleted rows: " + num, Toast.LENGTH_SHORT).show();
  }
}
