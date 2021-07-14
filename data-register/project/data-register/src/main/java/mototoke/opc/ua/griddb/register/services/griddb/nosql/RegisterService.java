package mototoke.opc.ua.griddb.register.services.griddb.nosql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;

import com.toshiba.mwcloud.gs.Collection;
import com.toshiba.mwcloud.gs.GSException;
import com.toshiba.mwcloud.gs.GridStore;
import com.toshiba.mwcloud.gs.GridStoreFactory;
import com.toshiba.mwcloud.gs.Query;
import com.toshiba.mwcloud.gs.RowKey;
import com.toshiba.mwcloud.gs.RowSet;
import com.toshiba.mwcloud.gs.TimeSeries;
import com.toshiba.mwcloud.gs.TimeUnit;
import com.toshiba.mwcloud.gs.TimestampUtils;

import mototoke.opc.ua.griddb.register.entities.griddb.Equip;
import mototoke.opc.ua.griddb.register.entities.griddb.Point;

public class RegisterService {

    private final String equipColName = "equipment_col";
    
    /**
     * constuctor
     */
    public RegisterService() {}

    /**
     * 
     * @param timeSeriesName
     * @param val
     */
    public void insertSensorValue(
        Properties props, String containerName,    
        String timeSeriesName, double val){

        try {
            // Create Gridstore Object
            GridStore store = GridStoreFactory.getInstance().getGridStore(props);
            // Connect Cluster
            store.getContainer(containerName);

            TimeSeries<Point> ts = store.getTimeSeries(timeSeriesName, Point.class);
            Date date = new Date();
            Point point = new Point();
            point.time   = date;
            point.value  = val;

            String status = "";
            if(val > 900) status = "ERROR";
            else status = "NONE";

            point.status = status;
            
            ts.put(date, point);

            store.close();
        } catch (GSException e) {
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    public void debugSelectTimeSeriesValue(Properties props, String containerName, String sensorId){
        try {
            // Create Gridstore Object
            GridStore store = GridStoreFactory.getInstance().getGridStore(props);
            // Connect Cluster
            store.getContainer(containerName);

            // 設備を検索
            Collection<String, Equip> equipCol = store.getCollection(equipColName, Equip.class);
            Equip equip = equipCol.get(sensorId);
            System.out.println("[Equipment] " + equip.name  + " (sensorid) "+ sensorId);

            // 直前の時系列を検索
            String tsName = sensorId;
            TimeSeries<Point> ts = store.getTimeSeries(tsName, Point.class);
            Date endDate   = new Date();
            Date startDate = TimestampUtils.add(endDate, -10, TimeUnit.MINUTE);
            RowSet<Point> rowSet =  ts.query(startDate, endDate).fetch();

            SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.JAPAN);
            while (rowSet.hasNext()) {
                Point ret = rowSet.next();
                System.out.println(
                        "[Result] " +sf.format(ret.time) +
                        " " + ret.value + " " + ret.status);
            }

            store.close();
        } catch (GSException e) {
            e.printStackTrace();
        }
    }
}
