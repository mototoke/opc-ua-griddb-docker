package mototoke.opc.ua.client;

import java.util.Timer;
import java.util.TimerTask;

import java.util.List;
import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import mototoke.opc.ua.client.services.BrwoseClientService;
import mototoke.opc.ua.client.services.BrowseNodeClientService;
import mototoke.opc.ua.client.services.ReadClientService;
import mototoke.opc.ua.client.services.ReadNodeClientService;
import mototoke.opc.ua.client.services.ReadValueClientService;
import mototoke.opc.ua.client.services.TransferBrowsePathClientService;
import mototoke.opc.ua.client.services.WriteClientService;

public class App {
    // 環境変数から設定値を取得
    private static final String ip = System.getenv("ACCESS_IP");
    private static final String port = System.getenv("ACCESS_PORT");
    private static final String tagName = System.getenv("TAG_NAME");
    private static final String executionCycleStr = System.getenv("EXECUTINON_CYCLE");

    public static void main( String[] args )
    {
        Integer executionCycle = Integer.getInteger(executionCycleStr, 10);

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
                    WriteClientService clientService5 = new WriteClientService();
                    // TransferBrowsePathClientService clientService6 = new TransferBrowsePathClientService();
                    // ReadValueClientService clientService7 = new ReadValueClientService();

                    try {
                        // clientService.run(client);
                        // clientService2.run(client);
                        // clientService3.run(client);
                        // clientService4.run(client);
                        clientService5.run(client);
                        // clientService6.run(client);
                        // clientService7.run(client);
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
