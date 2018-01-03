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

public class c_RegistrationActivity extends Activity implements View.OnClickListener {

    _GlobalState globalState;
    ProgressDialog progressDialog;
    User user;
    OperationPerformer operationPerformer;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        globalState = (_GlobalState)getApplication();
        setContentView(R.layout.c_registration);
        ((Button) findViewById(R.id.registration_Button)).setOnClickListener(this);
    }

    public void onClick(View arg0) {
        if (arg0 == findViewById(R.id.registration_Button)) {

            String login = ((EditText)findViewById(R.id.login_text)).getText().toString();
            String password = ((EditText)findViewById(R.id.password_text)).getText().toString();
            String name = ((EditText)findViewById(R.id.name_text)).getText().toString();
            String email = ((EditText)findViewById(R.id.email_text)).getText().toString();
            String surname = ((EditText)findViewById(R.id.surname_text)).getText().toString();

            UserInfo userInfo = new UserInfo();
            userInfo.setName(name);
            userInfo.setSurname(surname);

            user = new User();
            user.setEmail(email);
            user.setPassword(password);
            user.setLogin(login);
            user.setUserInfo(userInfo);

            progressDialog = ProgressDialog.show(this, "RegistrationActivity", "Registering for service...");
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
                    toastShow("Registration successful");

                    Intent intent = new Intent(c_RegistrationActivity.this, d_UsersListActivity.class);
                    startActivity(intent);
                    finish();

                    break;
                case -1:
                    toastShow("Registration unsuccessful,\nemail account already in use");
                    break;
                case -2:
                    toastShow("Not registered, connection problem due to: " + msg.getData().getString("error"));
                    System.out.println("--------------------------------------------------");
                    System.out.println("error!!!");
                    System.out.println(msg.getData().getString("error"));
                    System.out.println("--------------------------------------------------");
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

                globalState.my_user = User_REST_API.createUser_return_UserInfo(user);

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
