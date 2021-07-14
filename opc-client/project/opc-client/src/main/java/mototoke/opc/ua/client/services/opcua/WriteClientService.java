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

    // MethoType is "Random" or "Fluctuation"
    // Defalut is Flutuation
    private String methodType = "Fluctuation";

    private double writeValue = 0;
    private static double addValue = 0;

    public WriteClientService(NodeId targetNodeId, String type) {
        super();

        this.nodeId = targetNodeId;

        // 処理定義の決定
        switch (type) {
            case "Random":
                this.methodType = type;
                break;
            default:
                this.methodType = "Fluctuation";
                break;
        }
    }

    @Override
    public void run(OpcUaClient client) throws Exception {

        Variant v;

        switch (this.methodType) {
            case "Random":
                // Set Random Value(-1000 ~ 1000)
                v = new Variant(this.randDouble(-1000.0, 1000));
                break;
        
            default:
                // Sin Curve
                this.writeValue = (this.writeValue + WriteClientService.addValue) * 0.1;
                v = new Variant(Math.sin(this.writeValue));
                break;
        }

        WriteClientService.addValue = WriteClientService.addValue + 1;


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
