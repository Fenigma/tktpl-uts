package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.AppDatabase;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.LoginHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.LoginHistoryDao;

public class LoginHistoryRepository {
    private static LoginHistoryRepository instance;
    private static ArrayList<LoginHistory> loginHistoryList = new ArrayList<>();
    private static MutableLiveData<ArrayList<LoginHistory>> loginHistoryLiveData = new MutableLiveData<>();
    private static LoginHistoryDao loginHistoryDao;


    public static LoginHistoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new LoginHistoryRepository();
            AppDatabase db = AppDatabase.getInstance(context);
            LoginHistoryDao dao = db.loginHistoryDao();
            loginHistoryDao = dao;
            setUpKnownHosts();
            loginHistoryLiveData.setValue(loginHistoryList);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<LoginHistory>> getLoginHistory() {
        return loginHistoryLiveData;
    }

    private static void setUpKnownHosts() {
        loginHistoryList = (ArrayList) loginHistoryDao.getAll();
    }

    public void addLoginHistory(String access_date) {
        LoginHistory login = new LoginHistory(access_date);
        loginHistoryList.add(0, login);
        loginHistoryLiveData.setValue(loginHistoryList);
        loginHistoryDao.insertLoginHistory(login);
    }
}