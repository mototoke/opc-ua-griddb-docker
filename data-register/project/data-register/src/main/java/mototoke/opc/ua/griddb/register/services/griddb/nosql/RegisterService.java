package mototoke.opc.ua.griddb.register.services.griddb.nosql;

import java.util.Properties;

import com.toshiba.mwcloud.gs.Collection;
import com.toshiba.mwcloud.gs.GSException;
import com.toshiba.mwcloud.gs.GridStore;
import com.toshiba.mwcloud.gs.GridStoreFactory;
import com.toshiba.mwcloud.gs.Query;
import com.toshiba.mwcloud.gs.RowKey;
import com.toshiba.mwcloud.gs.RowSet;

public class RegisterService {

    private GridStore store;
    
    /**
     * constuctor
     * @throws GSException
     */
    public RegisterService(Properties props, String containerName) throws GSException {
        
        // Create Gridstore Object
        store = GridStoreFactory.getInstance().getGridStore(props);

        // Connect Cluster
        store.getContainer(containerName);
    }

    /**
     * 
     * @return
     */
    public void Insert(){

    }
}
