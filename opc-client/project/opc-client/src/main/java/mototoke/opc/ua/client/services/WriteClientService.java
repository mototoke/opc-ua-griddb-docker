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

public class WriteClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client) throws Exception {

        NodeId nodeId = new NodeId(2, "MyDevice");

        Variant v = new Variant(1.0);
        DataValue dv = new DataValue(v, null, null);
        CompletableFuture<StatusCode> statusFuture =  client.writeValue(nodeId, dv);
        StatusCode status = statusFuture.get();
        if (status.isGood()) {
            logger.info("Wrote '{}' to nodeId={}", v, nodeId);
        }
    }
}
