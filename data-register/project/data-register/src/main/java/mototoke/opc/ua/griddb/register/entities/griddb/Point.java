package mototoke.opc.ua.griddb.register.entities.griddb;

import java.util.Date;

import com.toshiba.mwcloud.gs.RowKey;

public class Point {
    @RowKey
    public Date time; // 時刻

    public double value; // センサ値

    public String status; //  センサ情報
}
