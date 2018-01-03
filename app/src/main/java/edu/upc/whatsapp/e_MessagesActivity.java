package edu.upc.whatsapp;

import edu.upc.whatsapp.REST_API.Message_REST_API;
import edu.upc.whatsapp.adapter.MyAdapter_messages;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import entity.Message;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class e_MessagesActivity extends Activity {

    _GlobalState globalState;
    ProgressDialog progressDialog;
    private ListView conversation;
    private MyAdapter_messages adapter;
    private List<Message> messages;
    private EditText input_text;
    private Button button;
    private InputMethodManager inMgr;
    private boolean enlarged = false, shrunk = true;
    private Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.e_messages);

        globalState = (_GlobalState) getApplication();

        TextView title = (TextView) findViewById(R.id.title);
        title.setText("Talking with: " + globalState.user_to_talk_to.getName());

        conversation = (ListView) findViewById(R.id.conversation);
        new fetchAllMessages_Task().execute(globalState.my_user.getId(), globalState.user_to_talk_to.getId());

        button = (Button) findViewById(R.id.mybutton);
        button.setEnabled(false);

        inMgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        input_text = (EditText) findViewById(R.id.input);
        input_text.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
                if (arg0.toString().equals("")) {
                    button.setEnabled(false);
                } else {
                    button.setEnabled(true);
                }
            }
        });
        //to program the send soft key from the soft keyboard:
        input_text.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    addText(null);
                    handled = true;
                }
                return handled;
            }
        });
        //to detect a change on the height of the visible window on the screen.
        input_text.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int screenHeight = input_text.getRootView().getHeight();
                Rect r = new Rect();
                input_text.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.bottom - r.top;
                int heightDifference = screenHeight - visibleHeight;
                if (heightDifference > 50 && !enlarged) {
                    LayoutParams layoutparams = input_text.getLayoutParams();
                    layoutparams.height = layoutparams.height * 2;
                    input_text.setLayoutParams(layoutparams);
                    enlarged = true;
                    shrunk = false;
                    conversation.post(new Runnable() {
                        @Override
                        public void run() {
                            conversation.setSelection(conversation.getCount() - 1);
                        }
                    });
                }
                if (heightDifference < 50 && !shrunk) {
                    LayoutParams layoutparams = input_text.getLayoutParams();
                    layoutparams.height = layoutparams.height / 2;
                    input_text.setLayoutParams(layoutparams);
                    shrunk = true;
                    enlarged = false;
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        timer = new Timer();
        timer.schedule(new fetchNewMessagesTimerTask(), 10000, 10000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timer.cancel();
    }

    public void addText(final View view) {

        Message message = new Message();

        message.setDate(new Date());
        message.setUserSender(globalState.my_user);
        message.setUserReceiver(globalState.user_to_talk_to);
        message.setContent(input_text.getEditableText().toString());

        new SendMessage_Task().execute(message);

        input_text.setText("");

        //to hide the soft keyboard after sending the message:
        inMgr.hideSoftInputFromWindow(input_text.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private class SendMessage_Task extends AsyncTask<Message, Void, Integer> {

        @Override
        protected void onPreExecute() {
            toastShow("sending message");
        }

        @Override
        protected Integer doInBackground(Message... messages) {
            try {

                Message_REST_API.myCreateMessage(messages[0]);

                return 0;
            } catch (Exception ex) {
                ex.printStackTrace();
                return -1;
            }
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 0) {
                toastShow("message sent");
                new fetchNewMessages_Task().execute(globalState.my_user.getId(),globalState.user_to_talk_to.getId());

            } else {
                toastShow("message not sent");
            }
        }
    }

    private class fetchAllMessages_Task extends AsyncTask<Integer, Void, List<Message>> {

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(e_MessagesActivity.this, "MessagesActivity", "downloading messages...");
        }

        @Override
        protected List<Message> doInBackground(Integer... userIds) {
            try {
                return Message_REST_API.retrieveMessages(userIds[0],userIds[1]);
            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Message> all_messages) {
            progressDialog.dismiss();
            if (all_messages == null) {
                toastShow("There's been an error downloading the messages");
            } else {
                toastShow(all_messages.size()+" messages downloaded");

                messages = all_messages;
                adapter = new MyAdapter_messages(e_MessagesActivity.this, messages, globalState.user_to_talk_to);
                conversation.setAdapter(adapter);

                conversation.post(new Runnable() {
                    @Override
                    public void run() {
                        conversation.setSelection(conversation.getCount() - 1);
                    }
                });
            }
        }
    }

    private class fetchNewMessages_Task extends AsyncTask<Integer, Void, List<Message>> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected List<Message> doInBackground(Integer... userIds) {
            try {

                if(!messages.isEmpty()){
                    return Message_REST_API.retrieveNewMessages(userIds[0],userIds[1], messages.get(messages.size()-1));

                } else{
                    return  Message_REST_API.retrieveMessages(userIds[0],userIds[1]);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Message> new_messages) {
            if (new_messages == null) {
                toastShow("There's been an error downloading new messages");
            } else {
                toastShow(new_messages.size()+" new messages downloaded");

                for (Message message : new_messages) {
                    messages.add(message);
                    adapter.date_visibility(message);
                }

                adapter.notifyDataSetChanged();

                conversation.post(new Runnable() {
                    @Override
                    public void run() {
                        conversation.setSelection(conversation.getCount() - 1);
                    }
                });
            }
        }
    }

    private class fetchNewMessagesTimerTask extends TimerTask {

        @Override
        public void run() {

            new fetchNewMessages_Task().execute(globalState.my_user.getId(),globalState.user_to_talk_to.getId());

        }
    }

    private void toastShow(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.setGravity(0, 0, 200);
        toast.show();
    }

}
