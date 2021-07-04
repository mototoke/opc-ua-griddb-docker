package mototoke.opc.ua.client.services.opcua;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Both;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadWriteIdsClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private NodeId[] nodeIds;

    public ReadWriteIdsClientService(NodeId... targetNodeIds) {
        super();

        this.nodeIds = targetNodeIds;
    }

    @Override
    public void run(OpcUaClient client) throws Exception {
        read(client, null, this.nodeIds)
                            .thenAccept(value -> {
                                System.out.println(value);
                            })
                            .thenCompose(v -> client.disconnect());
    }
    
    public CompletableFuture<List<DataValue>> read(
        final OpcUaClient client,
        final AttributeId attributeId,
        final NodeId... nodeIds) {

    return client
            .read(
                    0,
                    Both,
                    asList(nodeIds),
                    nCopies(nodeIds.length, attributeId.uid()));
    }

    public void write(
        final OpcUaClient client, 
        final List<NodeId> nodeIds, 
        final List<DataValue> writeValues) throws InterruptedException, ExecutionException{

        CompletableFuture<List<StatusCode>> statusFutures =  client.writeValues(nodeIds, writeValues);
        List<StatusCode> statuses = statusFutures.get();
        statuses.forEach(s -> {
            if(s.isGood()) logger.info("'{}' is Good", s);
        });
    }
}
