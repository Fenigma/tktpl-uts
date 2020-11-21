package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface CommandHistoryDao {
    @Query("SELECT * FROM CommandHistory ORDER BY access_date DESC LIMIT 10")
    List<CommandHistory> getAll();

    @Insert
    void insertCommandHistory(CommandHistory commandHistories);

}
