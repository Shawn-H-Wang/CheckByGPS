package henu.wh.checkbygps.chat;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import henu.wh.checkbygps.R;

public class ChatAdapter extends BaseAdapter {

    private Context context;
    private List<PersonChat> lists;

    public ChatAdapter(Context context, List<PersonChat> lists) {
        super();
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HolderView holderView = null;
        PersonChat entity = lists.get(position);
        boolean isMeSend = entity.isMeSend();
        if (holderView == null) {
            holderView = new HolderView();
            if (isMeSend) {
                convertView = View.inflate(context, R.layout.chat_dialog_right_item, null);
                holderView.tv_chat_me_message = (TextView) convertView.findViewById(R.id.tv_chat_me_message);
                holderView.tv_chat_me_message.setText(entity.getChatMessage());
            } else {
                convertView = View.inflate(context, R.layout.chat_dialog_left_item, null);
                holderView.tvname = (TextView) convertView.findViewById(R.id.tvname);
                holderView.tvname.setText(entity.getChatMessage());
            }
            convertView.setTag(holderView);
        } else {
            holderView = (HolderView) convertView.getTag();
        }

        return convertView;
    }

    public int getItemViewType(int position) {
        PersonChat entity = lists.get(position);

        if (entity.isMeSend()) {// 收到的消息
            return IMsgViewType.IMVT_COM_MSG;
        } else {// 自己发送的消息
            return IMsgViewType.IMVT_TO_MSG;
        }
    }

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;// 收到对方的消息
        int IMVT_TO_MSG = 1;// 自己发送出去的消息
    }

    class HolderView {
        TextView tv_chat_me_message;
        TextView tvname;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
