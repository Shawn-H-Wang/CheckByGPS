package henu.wh.checkbygps.client;

import com.alibaba.fastjson.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import henu.wh.checkbygps.role.User;

public class Client {

    private Socket socket;
    private DataOutputStream writer;
    private DataInputStream reader;
    private static Client client;

    public synchronized static Client getClient() {
        if (client == null) {
            client = new Client();
        }
        return client;
    }

    public Client() {
        try {
            socket = new Socket(SendMessage.serverIp, 5001);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendLogin(User user) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("phone", user.getPhone());
            jsonObject.put("passwd", user.getPassword());
            jsonObject.put("operation", user.getOpeartion());
            writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(JSONObject jsonObject) {
        try {
            writer = new DataOutputStream(socket.getOutputStream());
            writer.writeUTF(jsonObject.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String read() {
        String s = null;
        try {
            reader = new DataInputStream(socket.getInputStream());
            s = reader.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return s;
    }

    public boolean readBoolean() {
        try {
            reader = new DataInputStream(socket.getInputStream());
            boolean b = reader.readBoolean();
            if (b) {    // 登陆成功
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;   // 登陆失败
    }

    public void close() throws IOException {
        if (writer != null) {
            writer.close();
        }
        if (socket != null) {
            socket.close();
        }
        client = null;
    }


}
