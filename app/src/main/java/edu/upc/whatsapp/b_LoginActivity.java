package edu.upc.whatsapp;

import edu.upc.whatsapp.REST_API.User_REST_API;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import entity.User;
import entity.UserInfo;

public class b_LoginActivity extends Activity implements View.OnClickListener {

    _GlobalState globalState;
    ProgressDialog progressDialog;
    User user;
    OperationPerformer operationPerformer;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        globalState = (_GlobalState)getApplication();
        setContentView(R.layout.b_login);
        ((Button) findViewById(R.id.login_Button)).setOnClickListener(this);
    }

    public void onClick(View arg0) {
        if (arg0 == findViewById(R.id.login_Button)) {

            String login = ((EditText)findViewById(R.id.login_text)).getText().toString();
            String password = ((EditText)findViewById(R.id.password_text)).getText().toString();

            user = new User();
            user.setLogin(login);
            user.setPassword(password);

            progressDialog = ProgressDialog.show(this, "LoginActivity", "Logging into the server...");
            // if there's still a running thread doing something, we don't create a new one
            if (operationPerformer == null) {
                operationPerformer = new OperationPerformer(handler);
                operationPerformer.start();
            }
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            operationPerformer = null;
            progressDialog.dismiss();

            switch (msg.getData().getInt("result")) {
                case 1:
                    toastShow("Login successful");

                    Intent intent = new Intent(b_LoginActivity.this, d_UsersListActivity.class);
                    startActivity(intent);

                    finish();
                    break;
                case -1:
                    toastShow("Login unsuccessful, try again please");
                    break;
                case -2:
                    toastShow("Not logged in, connection problem due to: " + msg.getData().getString("error"));
                    break;
            }
        }
    };

    private class OperationPerformer extends Thread {

        Handler handler;

        OperationPerformer(Handler h) {
            handler = h;
        }

        @Override
        public void run() {
            Message msg = handler.obtainMessage();
            Bundle b = new Bundle();
            try {

                globalState.my_user = User_REST_API.loginUser(user);

                if (globalState.my_user != null) {
                    b.putInt("result", 1);
                } else {
                    b.putInt("result", -1);
                }
            } catch (Exception e) {
                b.putInt("result", -2);
                b.putString("error", e.getMessage());
            }
            msg.setData(b);
            handler.sendMessage(msg);
        }
    }

    private void toastShow(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.setGravity(0, 0, 200);
        toast.show();
    }
}
