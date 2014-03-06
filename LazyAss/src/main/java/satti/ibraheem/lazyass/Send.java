package satti.ibraheem.lazyass;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

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
    //private DatagramSocket socket;
    private MainActivity main=new MainActivity();
    //protected
   /* protected Send(MainActivity local)
    {
        main=local;
    }*/
    @Override
    protected String doInBackground(String... strings) {
        Socket tcp;
        try {
            tcp= new Socket("192.168.137.1",4445);//(main.getIP(),main.getPort());
            PrintWriter out = new PrintWriter(tcp.getOutputStream(), true);
            out.println(strings[0]);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }catch (Exception e)
        {
            e.printStackTrace();
            return "failed";
        }
        if(strings[0].equalsIgnoreCase("sh"))
        return "shut";
        else
            return "hiber";
    }

    protected void onPostExecute() {
        // str=new MainActivity().action;
    }
}
