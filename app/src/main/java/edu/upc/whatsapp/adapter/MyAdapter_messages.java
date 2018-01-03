package edu.upc.whatsapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import edu.upc.whatsapp.R;
import entity.Message;
import entity.UserInfo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 *
 * @author juanluis
 */
public class MyAdapter_messages extends BaseAdapter {

    private Context mContext;
    private List<Message> messages;
    private List<Integer> date_visibility;
    private UserInfo my_user;
    private String last_date_shown;

    public MyAdapter_messages(Context context, List<Message> messages, UserInfo user) {
        mContext = context;
        this.messages = messages;
        my_user = user;
        last_date_shown = "none";
        date_visibility = new ArrayList<Integer>();
        for (Message message : messages) {
            date_visibility(message);
        }
    }

    public void date_visibility(Message message) {
        Date date = message.getDate();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String day_of_week = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.ALL_STYLES, Locale.US);
        if (last_date_shown.equals("none")) {
            last_date_shown = day_of_week;
            date_visibility.add(View.VISIBLE);
        } else if (!last_date_shown.equals(day_of_week)) {
            last_date_shown = day_of_week;
            date_visibility.add(View.VISIBLE);
        } else {
            date_visibility.add(View.GONE);
        }
    }

    public int getCount() {
        return messages.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            if (getItemViewType(position) == 0) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_whatsapp_right_bubble, parent, false);
            }
            if (getItemViewType(position) == 1) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.row_whatsapp_left_bubble, parent, false);
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
        Date date = messages.get(position).getDate();
        ((TextView) ((RelativeLayout) convertView).getChildAt(0)).setVisibility(date_visibility.get(position));
        ((TextView) ((RelativeLayout) convertView).getChildAt(0)).setText(sdf.format(date));
        ((TextView) ((RelativeLayout) convertView).getChildAt(1)).setText(messages.get(position).getContent());
        ((TextView) ((RelativeLayout) convertView).getChildAt(2)).setText(sdf2.format(date));

        return convertView;
    }

    public Object getItem(int arg0) {
        return messages.get(arg0);
    }

    public long getItemId(int arg0) {
        return messages.get(arg0).getId();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);

        if(message.getUserSender().getId().equals(my_user.getId())){
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2; // Count of different layouts
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }

}
