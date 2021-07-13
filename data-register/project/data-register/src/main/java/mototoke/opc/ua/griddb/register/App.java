package mototoke.opc.ua.griddb.register;

import java.util.Timer;
import java.util.TimerTask;

import java.util.List;
import java.util.Properties;

import org.eclipse.milo.opcua.sdk.client.OpcUaClient;
import org.eclipse.milo.opcua.sdk.client.api.config.OpcUaClientConfigBuilder;
import org.eclipse.milo.opcua.stack.client.DiscoveryClient;
import org.eclipse.milo.opcua.stack.core.types.builtin.NodeId;
import org.eclipse.milo.opcua.stack.core.types.structured.EndpointDescription;
import org.eclipse.milo.opcua.stack.core.util.EndpointUtil;

import mototoke.opc.ua.griddb.register.services.griddb.nosql.RegisterService;
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
    private static final String containerName = "MyContainer";

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

                    Properties props = new Properties();
                    props.setProperty("notificationAddress", "239.0.0.1");
                    props.setProperty("notificationPort", "31999");
                    props.setProperty("clusterName", "dockerGridDB");
                    props.setProperty("user", "admin");
                    props.setProperty("password", "admin");

                    RegisterService registerService = new RegisterService();

                    // Init DB
                    CreateContainer cc = new CreateContainer(props, containerName);

                    try {
                        // OPC UAからデバイス2のセンサー値取得
                        Double dev1SensorValue = clientService.readValue(client, new NodeId(dev1NodeId, dev1QualifiedName), Double.class);

                        // // OPC UAからデバイス2のセンサー値取得
                        // Double dev2SensorValue = clientService.readValue(client, new NodeId(dev2NodeId, dev2QualifiedName), Double.class);

                        // // OPC UAからデバイス2のセンサー値取得
                        // Double dev3SensorValue = clientService.readValue(client, new NodeId(dev3NodeId, dev3QualifiedName), Double.class);

                        // デバイス1 データの登録
                        registerService.insertSensorValue(props, containerName, "panel001_voltage", dev1SensorValue);

                        // // デバイス2 データの登録
                        // registerService.insertSensorValue(props, containerName, "panel002_voltage", dev2SensorValue);

                        // // デバイス3 データの登録
                        // registerService.insertSensorValue(props, containerName, "panel003_voltage", dev3SensorValue);


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
