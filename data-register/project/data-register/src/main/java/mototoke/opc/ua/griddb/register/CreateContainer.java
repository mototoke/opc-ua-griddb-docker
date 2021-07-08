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
            this.InitEquip(store);
            // Register Alert(アラートはとりあえず保留)
            // this.InitAlert(store);

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
    private void InitEquip(GridStore store) throws GSException {
        try {
            // コレクションを作成する
            Collection<String, Equip> equipCol = store.putCollection(equipColName, Equip.class);

            // カラムに索引を設定　タイプがStringなのでTREE索引
            equipCol.createIndex("id");
            equipCol.createIndex("name");
            // 自動コミットモードをOFF
            equipCol.setAutoCommit(false);            

            // トランザクションを確定
            equipCol.commit();
        } catch (GSException e) {
            throw e;
        }
    }

    /**
     * アラート履歴コンテナ作成（アラーム履歴のデータは作らない）
     * @param store
     * @throws GSException
     */
    private void InitAlert(GridStore store) throws GSException {
        try {
            // コレクションを作成する
            Collection<Long, Alert> alertCol = store.putCollection(alertColName, Alert.class);

            // カラムに索引を設定　タイプがStringなのでTREE索引
            alertCol.createIndex("timestamp");
            alertCol.createIndex("level");
            // 自動コミットモードをOFF
            alertCol.setAutoCommit(false);

            // トランザクションを確定
            alertCol.commit();
        } catch (GSException e) {
            throw e;
        }
    }
}
