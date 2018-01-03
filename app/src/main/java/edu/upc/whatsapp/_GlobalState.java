package edu.upc.whatsapp;

import android.app.Application;
import entity.UserInfo;

public class _GlobalState extends Application {

  public UserInfo my_user, user_to_talk_to;

  @Override
  public void onCreate() {
    super.onCreate();
  }

}
