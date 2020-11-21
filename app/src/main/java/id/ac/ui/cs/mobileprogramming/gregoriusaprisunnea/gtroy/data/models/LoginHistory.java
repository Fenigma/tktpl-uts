package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class LoginHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "access_date")
    public String access_date;

    public LoginHistory(String access_date) {
        this.access_date = access_date;
    }
}