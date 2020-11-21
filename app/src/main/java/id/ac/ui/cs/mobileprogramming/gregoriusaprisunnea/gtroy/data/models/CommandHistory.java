package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class CommandHistory {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "command")
    public String command;

    @ColumnInfo(name = "access_date")
    public String access_date;

    public CommandHistory(String command, String access_date) {
        this.command = command;
        this.access_date = access_date;
    }
}

