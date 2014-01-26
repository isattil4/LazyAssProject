package satti.ibraheem.lazyass;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Ibraheem on 1/18/14.
 */
public class Send extends AsyncTask<String, Void, String> {
    private DatagramSocket socket;
    private Socket tcp;
    //protected

    @Override
    protected String doInBackground(String... strings) {
        try {
            tcp= new Socket("192.168.137.1",4445);
            PrintWriter out = new PrintWriter(tcp.getOutputStream(), true);
            out.println(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return strings[0];
    }

    protected void onPostExecute() {
        // str=new MainActivity().action;
    }
}
