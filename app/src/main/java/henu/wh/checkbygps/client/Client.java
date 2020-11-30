package henu.wh.checkbygps.client;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import henu.wh.checkbygps.role.Group;
import henu.wh.checkbygps.role.User;

public class Client {

    private Socket socket;

    public Client() throws IOException {
        socket = new Socket(SendMessage.serverIp, 5001);
        Log.i("Android", "与服务器建立连接：" + socket);
//        socket.setSoTimeout(5000);
    }

    public void sendAddGroup() throws IOException {
        DataOutputStream writer = new DataOutputStream(socket.getOutputStream());
        writer.writeUTF(SendMessage.ADDGROUP);
    }

    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
    }


}
