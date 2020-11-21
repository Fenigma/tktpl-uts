package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.CommandHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.CommandHistoryDao;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHost;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.KnownHostDao;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.LoginHistory;
import id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models.LoginHistoryDao;

@Database(entities = {CommandHistory.class, LoginHistory.class, KnownHost.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class,
                    "app_db").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        }
        return instance;
    }
    public abstract CommandHistoryDao commandHistoryDao();
    public abstract LoginHistoryDao loginHistoryDao();
    public abstract KnownHostDao knownHostDao();
}

