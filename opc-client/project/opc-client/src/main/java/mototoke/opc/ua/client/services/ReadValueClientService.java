package mototoke.opc.ua.client.services;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static java.util.Collections.singletonList;
import static org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Both;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadValueClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client) throws Exception {
        final NodeId NODE_TO_READ = new NodeId(2, 1);
        read(client, NODE_TO_READ)
                            .thenAccept(value -> {
                                System.out.println(value);
                            })
                            .thenCompose(v -> client.disconnect());
    }

    public static CompletableFuture<DataValue> read(
            final OpcUaClient client,
            final NodeId nodeId) {

        return client.readValue(0, TimestampsToReturn.Both, nodeId);
    }

    public static CompletableFuture<List<DataValue>> read(
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
}
