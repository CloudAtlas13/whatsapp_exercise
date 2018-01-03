package edu.upc.whatsapp.REST_API;

import com.google.gson.Gson;
import entity.User;
import entity.UserInfo;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.upc.whatsapp.REST_API.Comms.gson;
import static edu.upc.whatsapp.REST_API.Comms.url_rpc;

public class UserInfo_REST_API {

  public static List<UserInfo> allUserInfos(){
    try {
      URL url = new URL(url_rpc+"/entity.userinfo");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("GET");
      ucon.setDoInput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      UserInfo[] userArray = gson.fromJson(in, UserInfo[].class);
      List<UserInfo> users = new ArrayList<UserInfo>();
      users.addAll(Arrays.asList(userArray));
      return users;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
