package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHost;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories.CommandHistoryRepository;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories.KnownHostRepository;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories.LoginHistoryRepository;

public class MainActivityViewModel extends ViewModel {
    private static MainActivityViewModel instance;
    private LoginHistoryRepository loginHistoryRepository;
    private CommandHistoryRepository commandHistoryRepository;
    private KnownHostRepository knownHostRepository;

    public static MainActivityViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new MainActivityViewModel();
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context) {
        loginHistoryRepository = LoginHistoryRepository.getInstance(context);
        commandHistoryRepository = CommandHistoryRepository.getInstance(context);
        knownHostRepository = KnownHostRepository.getInstance(context);
    }

   public void addLoginHistory() {
        String access_date = getCurrentDate();
        loginHistoryRepository.addLoginHistory(access_date);
   }

   public void addCommandHistory(String command) {
        String access_date = getCurrentDate();
        commandHistoryRepository.addCommandHistory(command, access_date);
   }

    public void addKnownHost(String ip, String port) {
        ArrayList<KnownHost> hosts = knownHostRepository.getKnownHosts().getValue();
        boolean exist = false;

        if(ip.equals("") && port.equals("")) {
            return;
        }

        for(KnownHost h : hosts) {
            if(h.ip.equals(ip) && h.port.equals(port)) {
                exist = true;
                break;
            }
        }
        if (! exist) {
            knownHostRepository.addKnownHost(ip, port);
        }
    }
   private String getCurrentDate() {
        String timeStamp = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        return timeStamp;
   }


}
