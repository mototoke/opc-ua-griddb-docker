package mototoke.opc.ua.client.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.common.collect.ImmutableList;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyClientService implements IClientBase {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
    /**
     * コンストラクタ
     */
    public MyClientService(String tagName){
        this.SetTagName(tagName);
    }

    // Write TagName
    private String _tagName;
    public void SetTagName(String tagName) {
        this._tagName = tagName;
    }
    public String GetTagName() {
        return this._tagName;
    }

    @Override
    public void run(OpcUaClient client, CompletableFuture<OpcUaClient> future) throws Exception {
        // synchronous connect
        client.connect().get();

        List<NodeId> nodeIds = ImmutableList.of(new NodeId(2, "Int64"));

        for (int i = 0; i < 10; i++) {
            Variant v = new Variant(i);

            // don't write status or timestamps
            DataValue dv = new DataValue(v, null, null);

            // write asynchronously....
            CompletableFuture<List<StatusCode>> f =
                client.writeValues(nodeIds, ImmutableList.of(dv));

            // ...but block for the results so we write in order
            List<StatusCode> statusCodes = f.get();
            StatusCode status = statusCodes.get(0);

            if (status.isGood()) {
                logger.info("Wrote '{}' to nodeId={}", v, nodeIds.get(0));
            }
        }

        future.complete(client);
    }
}
