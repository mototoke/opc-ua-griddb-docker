package mototoke.opc.ua.client.services.opcua;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.eclipse.milo.examples.server.types.CustomStructType;
import org.eclipse.milo.opcua.sdk.client.DataTypeTreeBuilder;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaVariableNode;
import org.eclipse.milo.opcua.sdk.core.DataTypeTree;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.ExtensionObject;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.milo.opcua.stack.core.types.OpcUaDefaultBinaryEncoding;
import static org.eclipse.milo.opcua.stack.core.types.builtin.unsigned.Unsigned.uint;

public class ReadWriteIdClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private NodeId nodeId;

    /**
     * コンストラクタ
     */
    public ReadWriteIdClientService(NodeId targetNodeId) {
        super();

        this.nodeId = targetNodeId;
    }

    @Override
    public void run(OpcUaClient client) throws Exception {
        DataValue readValue = this.read(client, this.nodeId).get();
        Variant value = readValue.getValue();

        // 値がどんな型か知っている前提
        String readObject = value.getValue().toString();
        Double readData = Double.parseDouble(readObject);
        
        // 値に０．１足して書き込み
        DataValue writeValue = new DataValue(new Variant(readData + 0.1));
        this.write(client, this.nodeId, writeValue);

        // DataTypeTree tree = DataTypeTreeBuilder.build(client);
        // Class<?> clazz = tree.getBackingClass(this.nodeId);
        // System.out.println(clazz); // output: class java.lang.Object


        // ↓についてはまだわからない
        // ExtensionObject xo = (ExtensionObject) value.getValue();
        // CustomStructType decoded = (CustomStructType) xo.decode(
        //     client.getStaticSerializationContext()
        // );
        // logger.info("Decoded={}", decoded);

        // // Write a modified value
        // CustomStructType modified = new CustomStructType(
        //     decoded.getFoo() + "bar",
        //     uint(decoded.getBar().intValue() + 1),
        //     !decoded.isBaz()
        // );
        // ExtensionObject modifiedXo = ExtensionObject.encode(
        //     client.getStaticSerializationContext(),
        //     modified,
        //     xo.getEncodingId(),
        //     OpcUaDefaultBinaryEncoding.getInstance()
        // );

        // this.write(client, this.nodeId, new DataValue(new Variant(modifiedXo)));
    }

    public CompletableFuture<DataValue> read(
            final OpcUaClient client,
            final NodeId nodeId) {

        return client.readValue(0, TimestampsToReturn.Both, nodeId);
    }

    public void write(final OpcUaClient client, 
                    final NodeId nodeId, 
                    final DataValue writeValue) throws InterruptedException, ExecutionException{

                        CompletableFuture<StatusCode> statusFuture =  client.writeValue(nodeId, writeValue);
        StatusCode status = statusFuture.get();
        if (status.isGood()) {
            logger.info("Wrote '{}' to nodeId={}", writeValue, nodeId);
        }
    }
}
