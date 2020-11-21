package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.viewModels;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.CommandHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.repositories.CommandHistoryRepository;

public class CommandHistoryViewModel extends ViewModel {
    private CommandHistoryRepository commandHistoryRepository;
    private static CommandHistoryViewModel instance;

    public static CommandHistoryViewModel getInstance(Context context) {
        if (instance == null) {
            instance = new CommandHistoryViewModel();
            instance.init(context);
        }
        return instance;
    }

    public void init(Context context) {
        commandHistoryRepository = CommandHistoryRepository.getInstance(context);
    }

    public LiveData<ArrayList<CommandHistory>> getCommandHistory() {
        return commandHistoryRepository.getCommandHistory();
    }

    public void addCommandHistory(String command, String access_date) {
        commandHistoryRepository.addCommandHistory(command, access_date);
    }
}
