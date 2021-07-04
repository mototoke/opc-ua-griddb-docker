package mototoke.opc.ua.griddb.register.services.opcua;

import static java.util.Arrays.asList;
import static java.util.Collections.nCopies;
import static java.util.Collections.singletonList;
import static org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn.Both;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.bouncycastle.crypto.tls.CipherType;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.AttributeId;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DataValue;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.builtin.Variant;
import org.eclipse.milo.opcua.stack.core.types.enumerated.TimestampsToReturn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadValueClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client) throws Exception {
        // pass
    }

    /**
     * この関数を使う
     * @param <T> 読み取り値の型
     * @param client 
     * @param nodeId 
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public <T> T readValue(final OpcUaClient client,
    final NodeId nodeId,
    final Class<T> clazz) throws InterruptedException, ExecutionException {
        DataValue r = this.read(client, nodeId).get();
        Variant value = r.getValue();

        // 値がどんな型か知っている前提
        Object readObject = value.getValue();

        // 指定した型で値を返す
        // 駄目だったときはnull
        return convertInstanceOfObject(readObject, clazz);
    }

    private CompletableFuture<DataValue> read(
            final OpcUaClient client,
            final NodeId nodeId) {

        return client.readValue(0, TimestampsToReturn.Both, nodeId);
    }

    /**
     * https://stackoverflow.com/questions/14524751/cast-object-to-generic-type-for-returning
     * @param <T>
     * @param o
     * @param clazz
     * @return
    */
    private <T> T convertInstanceOfObject(Object o, Class<T> clazz) {
        try {
            return clazz.cast(o);
        } catch(ClassCastException e) {
            return null;
        }
    }
}
