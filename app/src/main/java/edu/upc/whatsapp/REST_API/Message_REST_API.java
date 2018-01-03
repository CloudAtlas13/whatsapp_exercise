package edu.upc.whatsapp.REST_API;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import entity.Message;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static edu.upc.whatsapp.REST_API.Comms.gson;
import static edu.upc.whatsapp.REST_API.Comms.url_rpc;

public class Message_REST_API {

  public static void myCreateMessage(Message message) {
    try {
      URL url = new URL(url_rpc+"/entity.message/create");
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(gson.toJson(message));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      String line;
      System.out.println("reply:");
      while ((line = in.readLine()) != null) {
        System.out.println(line);
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  public static List<Message> retrieveMessages(int from, int to) {
    try {
      URL url = new URL(url_rpc+"/entity.message/users/"+from+"/"+to);
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("GET");
      ucon.setDoInput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Message[] messageArray = gson.fromJson(in, Message[].class);
      List<Message> messages = new ArrayList<Message>();
      messages.addAll(Arrays.asList(messageArray));
      return messages;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }
  public static List<Message> retrieveNewMessages(int from, int to, Message message) {
    try {
      URL url = new URL(url_rpc+"/entity.message/users/"+from+"/"+to);
      HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
      ucon.setRequestMethod("POST");
      ucon.setDoInput(true);
      ucon.setDoOutput(true);
      ucon.setRequestProperty("Content-Type", "application/json");
      ucon.setRequestProperty("Accept", "application/json");

      PrintWriter out = new PrintWriter(ucon.getOutputStream(), true);
      out.println(gson.toJson(message));

      ucon.connect();

      BufferedReader in = new BufferedReader(new InputStreamReader(ucon.getInputStream()));
      Message[] messageArray = gson.fromJson(in, Message[].class);
      List<Message> messages = new ArrayList<Message>();
      messages.addAll(Arrays.asList(messageArray));
      return messages;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }

  }
}
