package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHost;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories.KnownHostRepository;

public class KnownHostViewModel extends ViewModel {
    private KnownHostRepository knownHostRepository;
    private static KnownHostViewModel instance;

    public static KnownHostViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new KnownHostViewModel();
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context) {
        knownHostRepository = KnownHostRepository.getInstance(context);
    }

    public LiveData<ArrayList<KnownHost>> getKnownHosts() {
        return knownHostRepository.getKnownHosts();
    }

}
