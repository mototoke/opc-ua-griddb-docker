package mototoke.opc.ua.client.services.opcua;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.stack.core.Identifiers;
import org.eclipse.milo.opcua.stack.core.types.builtin.QualifiedName;
import org.eclipse.milo.opcua.stack.core.types.builtin.StatusCode;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePath;
import org.eclipse.milo.opcua.stack.core.types.structured.BrowsePathResult;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePath;
import org.eclipse.milo.opcua.stack.core.types.structured.RelativePathElement;
import org.eclipse.milo.opcua.stack.core.types.structured.TranslateBrowsePathsToNodeIdsResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.collect.Lists.newArrayList;
import static org.eclipse.milo.opcua.stack.core.util.ConversionUtil.l;

public class TransferBrowsePathClientService implements IClientBase{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void run(OpcUaClient client) throws Exception {
        TranslateBrowsePathsToNodeIdsResponse response = client.translateBrowsePaths(newArrayList(new BrowsePath(
            Identifiers.ObjectsFolder,
            new RelativePath(new RelativePathElement[]{
                new RelativePathElement(
                    Identifiers.HierarchicalReferences,
                    false,
                    true,
                    new QualifiedName(2, "DataValue")
                )
            })
        ))).get();

        BrowsePathResult result = l(response.getResults()).get(0);
        StatusCode statusCode = result.getStatusCode();
        logger.info("Status={}", statusCode);

        l(result.getTargets()).forEach(target -> logger.info("TargetId={}", target.getTargetId()));
    }
}
