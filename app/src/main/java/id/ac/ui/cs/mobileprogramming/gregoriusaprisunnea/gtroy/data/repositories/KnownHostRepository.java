package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.AppDatabase;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHost;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHostDao;

public class KnownHostRepository {
    private static KnownHostRepository instance;
    private static ArrayList<KnownHost> knownHostList = new ArrayList<>();
    private static MutableLiveData<ArrayList<KnownHost>> knownHostsLiveData = new MutableLiveData<>();
    private static KnownHostDao knownHostDao;

    public static KnownHostRepository getInstance(Context context) {
        if (instance == null) {
            instance = new KnownHostRepository();
            AppDatabase db = AppDatabase.getInstance(context);
            KnownHostDao dao = db.knownHostDao();
            knownHostDao = dao;
            setUpKnownHosts();
            knownHostsLiveData.setValue(knownHostList);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<KnownHost>> getKnownHosts() {
        return knownHostsLiveData;
    }

    private static void setUpKnownHosts() {
        knownHostList = (ArrayList) knownHostDao.getAll();
    }

    public void addKnownHost(String ip, String port) {
        KnownHost host = new KnownHost(ip, port);
        knownHostList.add(0, host);
        knownHostsLiveData.setValue(knownHostList);
        knownHostDao.insertKnownHost(host);
    }
}
