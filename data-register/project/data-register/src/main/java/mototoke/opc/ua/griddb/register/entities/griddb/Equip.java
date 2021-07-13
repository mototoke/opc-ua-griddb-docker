package mototoke.opc.ua.griddb.register.entities.griddb;

// import java.sql.Blob;

import com.toshiba.mwcloud.gs.RowKey;

public class Equip {
    @RowKey
    public String id; // 設備ID＿センサ種別＝センサID

    public String name; // 設備名

    // Blob spec; // 仕様情報
}
