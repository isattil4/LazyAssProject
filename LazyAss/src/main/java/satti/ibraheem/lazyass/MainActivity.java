package satti.ibraheem.lazyass;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.net.DatagramSocket;

public class MainActivity extends Activity {
    private DatagramSocket socket;
    //private Context context;
    protected String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            builder.setMessage("Open Wifi Dialog?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        //Add Actions to Buttons
        Button shut = (Button) findViewById(R.id.shutdown);
        Button hiber = (Button) findViewById(R.id.hibernate);
        shut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                action = "sh";
                //try {
                    displayNotification("shut");
                    new Send().execute(action);
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
                    displayNotification("Hiber");
                    new Send().execute(action);
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
                return true;
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
        /*Notification noti = new Notification.Builder()
                .setSmallIcon(R.drawable.codies)
                .setContentTitle("Lazy Assistant")
                .setContentText(content)
                .setSubText("Command Notification")
                .setNumber(1)
                .setStyle(new Notification.BigTextStyle()
                        .bigText(fullContent))
                .setPriority(1)
                .setContentIntent(resultPendingIntent)
                .build()
                ;*/
        /*mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());*/
        /***************************************************************/
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

}
