package mototoke.opc.ua.griddb.register;

import java.util.Properties;

import com.toshiba.mwcloud.gs.Collection;
import com.toshiba.mwcloud.gs.GSException;
import com.toshiba.mwcloud.gs.GridStore;
import com.toshiba.mwcloud.gs.GridStoreFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import mototoke.opc.ua.griddb.register.entities.griddb.Alert;
import mototoke.opc.ua.griddb.register.entities.griddb.Equip;
import mototoke.opc.ua.griddb.register.entities.griddb.Point;

public class CreateContainer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String equipColName = "equipment_col";
    private final String alertColName = "alert_col";
    
    /**
     * constructor
     */
    public CreateContainer(Properties props, String containerName) {

        // GridStoreオブジェクトの生成
        GridStore store;
        try {
            store = GridStoreFactory.getInstance().getGridStore(props);
            // コンテナ作成や取得などの操作を行うと、クラスタに接続される
            // すでに存在するコンテナと同じ名前を指定した場合、指定したスキーマ定義と
            // 既存コンテナのスキーマ定義が同じであればエラーになりません
            store.getContainer(containerName);

            // Register Equip
            this.initEquip(store);
            // Register Alert(アラートはとりあえず保留)
            // this.initAlert(store);
            // Register Time Series
            this.initPoint(store);

            store.close();
        } catch (GSException e) {
            logger.error("Error: " + e.toString());
        }
    }

    /**
     * 設備情報コンテナ作成（データなければ作成する）
     * @param store
     * @throws GSException
     */
    private void initEquip(GridStore store) throws GSException {
        try {
            // コレクションを作成する
            Collection<String, Equip> equipCol = store.putCollection(equipColName, Equip.class);

            // カラムに索引を設定　タイプがStringなのでTREE索引
            equipCol.createIndex("id");
            equipCol.createIndex("name");
            // 自動コミットモードをOFF
            equipCol.setAutoCommit(false);
            
            // データ登録
            Equip equip1 = new Equip();
            equip1.id = "panel001_voltage";
            equip1.name = "panel001";

            Equip equip2 = new Equip();
            equip2.id = "panel002_voltage";
            equip2.name = "panel002";

            Equip equip3 = new Equip();
            equip3.id = "panel003_voltage";
            equip3.name = "panel003";

            equipCol.put(equip1);
            equipCol.put(equip2);
            equipCol.put(equip3);

            // トランザクションを確定
            equipCol.commit();
        } catch (GSException e) {
            throw e;
        }
    }

    // /**
    //  * アラート履歴コンテナ作成（アラーム履歴のデータは作らない）
    //  * @param store
    //  * @throws GSException
    //  */
    // private void initAlert(GridStore store) throws GSException {
    //     try {
    //         // コレクションを作成する
    //         Collection<Long, Alert> alertCol = store.putCollection(alertColName, Alert.class);

    //         // カラムに索引を設定　タイプがStringなのでTREE索引
    //         alertCol.createIndex("timestamp");
    //         alertCol.createIndex("level");
    //         // 自動コミットモードをOFF
    //         alertCol.setAutoCommit(false);

    //         // トランザクションを確定
    //         alertCol.commit();
    //     } catch (GSException e) {
    //         throw e;
    //     }
    // }

    /**
     * センサーコンテナ（時系列）作成（データなければ作成する）
     * @param store
     * @throws GSException
     */
    private void initPoint(GridStore store) throws GSException {
        try {
            store.putTimeSeries("panel001_voltage", Point.class);
            store.putTimeSeries("panel002_voltage", Point.class);
            store.putTimeSeries("panel003_voltage", Point.class);
        } catch (GSException e) {
            throw e;
        }
    }
}
