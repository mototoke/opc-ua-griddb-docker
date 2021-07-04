package mototoke.opc.ua.client.services.opcua;

import java.util.concurrent.CompletableFuture;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Random;

public class WriteClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private NodeId nodeId;

    public WriteClientService(NodeId targetNodeId) {
        super();

        this.nodeId = targetNodeId;
    }

    @Override
    public void run(OpcUaClient client) throws Exception {

        // Set Random Value(-1000 ~ 1000)
        Variant v = new Variant(this.randDouble(-1000.0, 1000));

        DataValue dv = new DataValue(v, null, null);
        CompletableFuture<StatusCode> statusFuture =  client.writeValue(this.nodeId, dv);
        StatusCode status = statusFuture.get();
        if (status.isGood()) {
            logger.info("Wrote '{}' to nodeId={}", v, this.nodeId);
        }
    }

    /**
     * https://stackoverflow.com/questions/40431966/what-is-the-best-way-to-generate-a-random-float-value-included-into-a-specified/51247968
     * @param min Random Range MIN Value
     * @param max Random Range MAX Value
     * @return randome value
     */
    private double randDouble(double min, double max) {
        Random rand = new Random();

        return rand.nextDouble() * (max - min) + min;
    }
}
