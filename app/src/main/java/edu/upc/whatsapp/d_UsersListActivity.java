package edu.upc.whatsapp;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;
import edu.upc.whatsapp.REST_API.UserInfo_REST_API;
import edu.upc.whatsapp.adapter.MyAdapter_users;
import entity.UserInfo;
import java.util.ArrayList;
import java.util.List;

public class d_UsersListActivity extends ListActivity {

  _GlobalState globalState;
  MyAdapter_users adapter;
  ProgressDialog progressDialog;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    globalState = (_GlobalState) getApplication();
    final int sdk = android.os.Build.VERSION.SDK_INT;
    View layout = findViewById(android.R.id.content);
    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
      layout.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_picture));
    } else {
      layout.setBackground(getResources().getDrawable(R.drawable.background_picture));
    }
    
    new DownloadUsers_Task().execute();
  }

  @Override
  public void onListItemClick(ListView l, View v, int position, long id) {

    globalState.user_to_talk_to = ((MyAdapter_users) l.getAdapter()).users.get(position);
    Intent intent = new Intent(this, e_MessagesActivity.class);
    startActivity(intent);
  }

  private class DownloadUsers_Task extends AsyncTask<Void, Void, List<UserInfo>> {

    @Override
    protected void onPreExecute() {
      progressDialog = ProgressDialog.show(d_UsersListActivity.this, "UsersListActivity",
        "downloading the users...");
    }

    @Override
    protected List<UserInfo> doInBackground(Void... nothing) {
      try {

        return UserInfo_REST_API.allUserInfos();
        
      } catch (Exception ex) {
        return null;
      }
    }

    @Override
    protected void onPostExecute(List<UserInfo> users) {
      progressDialog.dismiss();
      if (users == null) {
        toastShow("There's been an error downloading the users");
      } else {
        adapter = new MyAdapter_users(d_UsersListActivity.this, users);
        setListAdapter(adapter);
      }
    }
  }

  private void toastShow(String text) {
    Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
    toast.setGravity(0, 0, 200);
    toast.show();
  }

}
