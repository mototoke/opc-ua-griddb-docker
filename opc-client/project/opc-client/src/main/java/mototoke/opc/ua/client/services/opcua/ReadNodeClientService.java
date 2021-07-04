package mototoke.opc.ua.client.services.opcua;

import java.util.Arrays;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.model.nodes.objects.ServerTypeNode;
import org.eclipse.milo.opcua.sdk.client.model.nodes.variables.ServerStatusTypeNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.DateTime;
import org.eclipse.milo.opcua.stack.core.types.enumerated.ServerState;
import org.eclipse.milo.opcua.stack.core.types.structured.BuildInfo;
import org.eclipse.milo.opcua.stack.core.types.structured.ServerStatusDataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadNodeClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client) throws Exception {
        // Get a typed reference to the Server object: ServerNode
        ServerTypeNode serverNode = (ServerTypeNode) client.getAddressSpace().getObjectNode(
            Identifiers.Server,
            Identifiers.ServerType
        );

        // Read properties of the Server object...
        String[] serverArray = serverNode.getServerArray();
        String[] namespaceArray = serverNode.getNamespaceArray();

        logger.info("ServerArray={}", Arrays.toString(serverArray));
        logger.info("NamespaceArray={}", Arrays.toString(namespaceArray));

        // Read the value of attribute the ServerStatus variable component
        ServerStatusDataType serverStatus = serverNode.getServerStatus();

        logger.info("ServerStatus={}", serverStatus);

        // Get a typed reference to the ServerStatus variable
        // component and read value attributes individually
        ServerStatusTypeNode serverStatusNode = serverNode.getServerStatusNode();
        BuildInfo buildInfo = serverStatusNode.getBuildInfo();
        DateTime startTime = serverStatusNode.getStartTime();
        DateTime currentTime = serverStatusNode.getCurrentTime();
        ServerState state = serverStatusNode.getState();

        logger.info("ServerStatus.BuildInfo={}", buildInfo);
        logger.info("ServerStatus.StartTime={}", startTime);
        logger.info("ServerStatus.CurrentTime={}", currentTime);
        logger.info("ServerStatus.State={}", state);
    }
}
