package mototoke.opc.ua.client;

import java.util.Timer;
import java.util.TimerTask;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import mototoke.opc.ua.client.services.opcua.BrowseNodeClientService;
import mototoke.opc.ua.client.services.opcua.BrwoseClientService;
import mototoke.opc.ua.client.services.opcua.ReadClientService;
import mototoke.opc.ua.client.services.opcua.ReadNodeClientService;
import mototoke.opc.ua.client.services.opcua.ReadValueClientService;
import mototoke.opc.ua.client.services.opcua.ReadWriteIdClientService;
import mototoke.opc.ua.client.services.opcua.TransferBrowsePathClientService;
import mototoke.opc.ua.client.services.opcua.WriteClientService;

public class App {
    // 環境変数から設定値を取得
    private static final String ip = System.getenv("ACCESS_IP");
    private static final String port = System.getenv("ACCESS_PORT");
    private static final String nodeIdStr = System.getenv("NODE_ID");
    private static final String qualifiedNameStr = System.getenv("QUARLIFIED_NAME");
    private static final String executionCycleStr = System.getenv("EXECUTINON_CYCLE");

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
                    
                    // BrwoseClientService clientService = new BrwoseClientService();
                    // BrowseNodeClientService clientService2 = new BrowseNodeClientService();
                    // ReadClientService clientService3 = new ReadClientService();
                    // ReadNodeClientService clientService4 = new ReadNodeClientService();
                    WriteClientService clientService5 = new WriteClientService(new NodeId(nodeId, qualifiedName));
                    // TransferBrowsePathClientService clientService6 = new TransferBrowsePathClientService();
                    // ReadValueClientService clientService7 = new ReadValueClientService();
                    // ReadWriteIdClientService clientService8 = new ReadWriteIdClientService(new NodeId(2, 2));

                    try {
                        // clientService.run(client);
                        // clientService2.run(client);
                        // clientService3.run(client);
                        // clientService4.run(client);
                        clientService5.run(client);
                        // clientService6.run(client);
                        // clientService7.run(client);
                        // clientService8.run(client);
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
