package id.ac.ui.cs.mobileprogramming.gregoriusaprisunnea.gtroy.data.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class KnownHost {
    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "ip")
    public String ip;

    @ColumnInfo(name = "port")
    public String port;

    public KnownHost(String ip, String port) {
        this.port = port;
        this.ip = ip;
    }
}