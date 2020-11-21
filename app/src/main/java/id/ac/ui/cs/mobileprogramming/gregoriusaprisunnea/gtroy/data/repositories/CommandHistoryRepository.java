package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.AppDatabase;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.CommandHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.CommandHistoryDao;

public class CommandHistoryRepository {
    private static CommandHistoryRepository instance;
    private static ArrayList<CommandHistory> commandHistoryList = new ArrayList<>();
    private static MutableLiveData<ArrayList<CommandHistory>> commandHistoryLiveData = new MutableLiveData<>();
    private static CommandHistoryDao commandHistoryDao;

    public static CommandHistoryRepository getInstance(Context context) {
        if (instance == null) {
            instance = new CommandHistoryRepository();
            AppDatabase db = AppDatabase.getInstance(context);
            CommandHistoryDao dao = db.commandHistoryDao();
            commandHistoryDao = dao;
            setUpCommandHistory();
            commandHistoryLiveData.setValue(commandHistoryList);
        }
        return instance;
    }

    public MutableLiveData<ArrayList<CommandHistory>> getCommandHistory() {
        return commandHistoryLiveData;
    }

    private static void setUpCommandHistory() {
        commandHistoryList = (ArrayList) commandHistoryDao.getAll();
    }

    public void addCommandHistory(String cmd, String access_date) {
        CommandHistory command = new CommandHistory(cmd, access_date);
        commandHistoryList.add(0, command);
        commandHistoryLiveData.setValue(commandHistoryList);
        commandHistoryDao.insertCommandHistory(command);

    }
}
