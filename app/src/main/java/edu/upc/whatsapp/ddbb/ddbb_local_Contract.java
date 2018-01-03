package edu.upc.whatsapp.ddbb;

public interface ddbb_local_Contract {
  
  String DATABASE_NAME = "local_ddbb.db";
  int DATABASE_VERSION = 3;
    
  public interface Message {    
    String TABLE_NAME = "message";    
    String COLUMN_ID = "_id";
    String COLUMN_CONTENT = "content";
    String COLUMN_USER_SENDER = "user_sender";
    String COLUMN_USER_RECEIVER = "user_receiver";
    String COLUMN_DATE = "date";
    
    String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + Message.TABLE_NAME + " (" +
               Message.COLUMN_ID + " INTEGER PRIMARY KEY, " +
               Message.COLUMN_CONTENT + " TEXT, " +
               Message.COLUMN_USER_SENDER + " INTEGER, " +
               Message.COLUMN_USER_RECEIVER + " INTEGER, " +
               Message.COLUMN_DATE + " DATE )";

    String DROP_TABLE = "DROP TABLE IF EXISTS " + Message.TABLE_NAME;
  }

}
