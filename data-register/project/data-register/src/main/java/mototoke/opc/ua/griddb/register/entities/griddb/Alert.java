package mototoke.opc.ua.griddb.register.entities.griddb;

import java.sql.Date;

import com.toshiba.mwcloud.gs.RowKey;

/**
 * 未使用
 */
public class Alert {
    @RowKey
    int id; // アラートID

    Date timeStampe; // 時刻

    String sensorId; // センサID

    int level; // アラートレベル

    String detail; // アラート詳細情報
}
