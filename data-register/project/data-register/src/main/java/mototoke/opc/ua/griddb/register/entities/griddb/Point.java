package mototoke.opc.ua.griddb.register.entities.griddb;

import java.sql.Date;

import com.toshiba.mwcloud.gs.RowKey;

public class Point {
    @RowKey
    Date time; // 時刻

    double value; // センサ値

    String status; //  センサ情報
}
