package mototoke.opc.ua.client.services.opcua;

import java.util.List;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.nodes.UaNode;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.UaException;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowseNodeClientService implements IClientBase {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client) throws Exception {
        // start browsing at root folder
        browseNode("", client, Identifiers.RootFolder);
    }

    private void browseNode(String indent, OpcUaClient client, NodeId browseRoot) {
        try {
            List<? extends UaNode> nodes = client.getAddressSpace().browseNodes(browseRoot);

            for (UaNode node : nodes) {
                logger.info("{} Node={}", indent, node.getBrowseName().getName());

                // recursively browse to children
                browseNode(indent + "  ", client, node.getNodeId());
            }
        } catch (UaException e) {
            logger.error("Browsing nodeId={} failed: {}", browseRoot, e.getMessage(), e);
        }
    }
}
