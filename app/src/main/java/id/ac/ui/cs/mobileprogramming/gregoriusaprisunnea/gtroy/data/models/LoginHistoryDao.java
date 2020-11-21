package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface LoginHistoryDao {
    @Query("SELECT * FROM LoginHistory ORDER BY access_date ASC LIMIT 10")
    List<LoginHistory> getAll();

    @Insert
    void insertLoginHistory(LoginHistory loginHistories);

}
