package mototoke.opc.ua.griddb.register;

import java.util.Timer;
import java.util.TimerTask;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import mototoke.opc.ua.griddb.register.services.opcua.ReadValueClientService;


public class App {
    // 環境変数から設定値を取得
    private static final String ip = System.getenv("ACCESS_IP");
    private static final String port = System.getenv("ACCESS_PORT");
    private static final String nodeIdStr = System.getenv("DEVICE_1_NODE_ID");
    private static final String qualifiedNameStr = System.getenv("DEVICE_1_QUARLIFIED_NAME");
    private static final String executionCycleStr = System.getenv("EXECUTINON_CYCLE");
    private static final String containerName = System.getenv("CONTAINER_NAME");

    public static void main( String[] args )
    {
        Integer executionCycle = Integer.parseInt(executionCycleStr);
        Integer nodeId = Integer.parseInt(nodeIdStr);
        Integer qualifiedName = Integer.parseInt(qualifiedNameStr);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {

                try {
                    String endPoint = String.format("opc.tcp://%s:%s/opc-ua/griddb/server", ip, port);
                    List<EndpointDescription> endpoints = DiscoveryClient.getEndpoints(endPoint).get();
                    EndpointDescription configPoint = EndpointUtil.updateUrl(endpoints.get(0), System.getenv("ACCESS_IP"), Integer.parseInt(port));
        
                    OpcUaClientConfigBuilder cfg = new OpcUaClientConfigBuilder();
                    cfg.setEndpoint(configPoint);
        
                    OpcUaClient client = OpcUaClient.create(cfg.build());
                    client.connect().get();
                    
                    ReadValueClientService clientServiceDevice1 = new ReadValueClientService();

                    try {
                        // clientServiceDevice1.readValue(client, nodeId, clazz);

                    } catch (Exception e1) {
                        e1.printStackTrace();
                    } finally {
                        client.disconnect();
                    }
        
                } catch (Throwable ex) {
                    ex.printStackTrace();
                }
            }
        };
        // 1秒後にexecutionCycleの間隔でtaskを定期実行
        timer.scheduleAtFixedRate(task,1000, executionCycle);
    }
}
