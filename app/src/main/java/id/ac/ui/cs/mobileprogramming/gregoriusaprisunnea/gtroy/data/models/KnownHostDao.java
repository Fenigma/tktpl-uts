package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface KnownHostDao {
    @Query("SELECT * FROM KnownHost")
    List<KnownHost> getAll();

    @Insert
    void insertKnownHost(KnownHost KnownHost);

}
