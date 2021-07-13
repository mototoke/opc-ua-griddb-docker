package mototoke.opc.ua.griddb.register.services.griddb.nosql;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import com.toshiba.mwcloud.gs.Collection;
import com.toshiba.mwcloud.gs.GSException;
import com.toshiba.mwcloud.gs.GridStore;
import com.toshiba.mwcloud.gs.GridStoreFactory;
import com.toshiba.mwcloud.gs.Query;
import com.toshiba.mwcloud.gs.RowKey;
import com.toshiba.mwcloud.gs.RowSet;
import com.toshiba.mwcloud.gs.TimeSeries;

import mototoke.opc.ua.griddb.register.entities.griddb.Point;

public class RegisterService {
    
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
    public void debugSelectTimeSeriesValue(Properties props, String containerName, String timeSeriesName){
        try {
            // Create Gridstore Object
            GridStore store = GridStoreFactory.getInstance().getGridStore(props);
            // Connect Cluster
            store.getContainer(containerName);


            // Collection<String,Equip> equipCol = store.getCollection(equipColName, Equip.class);
            // Equip equip = equipCol.get(sensorId);

            store.close();
        } catch (GSException e) {
            e.printStackTrace();
        }
    }
}
