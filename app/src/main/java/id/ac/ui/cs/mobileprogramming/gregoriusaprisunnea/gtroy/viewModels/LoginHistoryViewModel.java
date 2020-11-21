package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.LoginHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories.LoginHistoryRepository;

public class LoginHistoryViewModel extends ViewModel {
    private LoginHistoryRepository loginHistoryRepository;
    private static LoginHistoryViewModel instance;

    public static LoginHistoryViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new LoginHistoryViewModel();
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context) {
            loginHistoryRepository = LoginHistoryRepository.getInstance(context);
    }

    public LiveData<ArrayList<LoginHistory>> getLoginHistory() {
        return loginHistoryRepository.getLoginHistory();
    }
}
