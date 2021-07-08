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
    private static final String dev1NodeIdStr = System.getenv("DEVICE_1_NODE_ID");
    private static final String dev1QualifiedNameStr = System.getenv("DEVICE_1_QUARLIFIED_NAME");
    private static final String dev2NodeIdStr = System.getenv("DEVICE_2_NODE_ID");
    private static final String dev2QualifiedNameStr = System.getenv("DEVICE_2_QUARLIFIED_NAME");
    private static final String dev3NodeIdStr = System.getenv("DEVICE_3_NODE_ID");
    private static final String dev3QualifiedNameStr = System.getenv("DEVICE_3_QUARLIFIED_NAME");
    private static final String executionCycleStr = System.getenv("EXECUTINON_CYCLE");
    private static final String containerName = System.getenv("CONTAINER_NAME");

    public static void main( String[] args )
    {
        Integer executionCycle = Integer.parseInt(executionCycleStr);
        Integer dev1NodeId = Integer.parseInt(dev1NodeIdStr);
        Integer dev1QualifiedName = Integer.parseInt(dev1QualifiedNameStr);
        Integer dev2NodeId = Integer.parseInt(dev2NodeIdStr);
        Integer dev2QualifiedName = Integer.parseInt(dev2QualifiedNameStr);
        Integer dev3NodeId = Integer.parseInt(dev3NodeIdStr);
        Integer dev3QualifiedName = Integer.parseInt(dev3QualifiedNameStr);

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
                    
                    ReadValueClientService clientService = new ReadValueClientService();

                    RegisterService registerService = new RegisterService();

                    try {
                        // OPC UAからデバイス2のセンサー値取得
                        double dev1SensorValue = clientService.readValue(client, new NodeId(dev1NodeId, dev1QualifiedName), double.class);

                        // OPC UAからデバイス2のセンサー値取得
                        double dev2SensorValue = clientService.readValue(client, new NodeId(dev2NodeId, dev2QualifiedName), double.class);

                        // OPC UAからデバイス2のセンサー値取得
                        double dev3SensorValue = clientService.readValue(client, new NodeId(dev3NodeId, dev3QualifiedName), double.class);




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
