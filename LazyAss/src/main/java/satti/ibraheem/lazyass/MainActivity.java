package satti.ibraheem.lazyass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

public class MainActivity extends Activity {
    protected String action;
    protected String ip;
    protected int port;
    protected SharedPreferences preferences;
    private static final int RESULT_SETTINGS = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
        //Check if WIFI is Working
        //WifiManager manager=(WifiManager) getSystemService(Context.WIFI_SERVICE);
        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (!mWifi.isConnected()) {
            //Display Message to enable WIFI
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(R.string.Wifi_String).setPositiveButton(R.string.yes_btn_String, dialogClickListener)
                    .setNegativeButton(R.string.No_btn_string, dialogClickListener).show();
        }
        MenuItem settings =(MenuItem) findViewById(R.id.action_settings);

        //Add Actions to Buttons
        Button shut = (Button) findViewById(R.id.shutdown);
        Button hiber = (Button) findViewById(R.id.hibernate);
        shut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "sh";
                //try {
                    Send sh=new Send();
                    sh.execute(action);
                try {
                    displayNotification(sh.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                /*} catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
            }
        });
        hiber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "h";
               // try {
                    Send h=new Send();
                    h.execute(action);
                try {
                    displayNotification(h.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                /*} catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }*/
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent i=new Intent(this,SettingsActivity.class);
                startActivityForResult(i,RESULT_SETTINGS);
                break;
            case R.id.about:
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setMessage(R.string.about_message)
                        .setTitle(R.string.About_title);
                builder.setPositiveButton(R.string.ok, null);
                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void displayNotification(String rec)
    {
        String content="";
        String fullContent="";
        if(rec.equalsIgnoreCase("shut"))
        {
            content="Shutdown Command Sent Successfully!";
            fullContent="Shutdown Command Sent Successfully to Remote Computer!";

        }
        else if(rec.equalsIgnoreCase("hiber"))
        {
            content="Hibernate Command Sent Successfully!";
            fullContent="Hibernate Command Sent Successfully to Remote Computer!";
        }
        else
        {
            content="Communication Failed!";
            fullContent="Communication Failed \n it seems like The Application is not running on the Remote Computer";
        }
        /***************************************************************/

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.codies)
                        .setContentTitle("Lazy Assistant")
                        .setContentText(content)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(fullContent))

                ;
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(Notification.FLAG_ONGOING_EVENT, mBuilder.build());
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_SETTINGS:
                break;

        }

    }

    protected String getIP()
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        return pref.getString("Server_IP", "NULL");
    }
    protected int getPort()
    {
        SharedPreferences pref= PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(pref.getString("Server_Port","NULL"));
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

    public class Send extends AsyncTask<String, Void, String> {
        //private DatagramSocket socket;
        //private MainActivity main=new MainActivity();
        int port;
        String ip;

        //protected
   /* protected Send(MainActivity local)
    {
        main=local;
    }*/
        @Override
        protected String doInBackground(String... strings) {
            Socket tcp;
            try {
                tcp = new Socket(ip, port);//("192.168.137.1",4445);
                PrintWriter out = new PrintWriter(tcp.getOutputStream(), true);
                out.println(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "";
            } catch (Exception e) {
                e.printStackTrace();
                return "failed";
            }
            if (strings[0].equalsIgnoreCase("sh"))
                return "shut";
            else
                return "hiber";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            port = Integer.parseInt(MainActivity.this.preferences.getString("Server_Port", "NULL"));
            ip = MainActivity.this.preferences.getString("Server_IP", "NULL");
            //int port= Integer.parseInt(pref.getString("Server_Port","NULL"));
        }

        protected void onPostExecute() {
            // str=new MainActivity().action;
        }
    }
}
