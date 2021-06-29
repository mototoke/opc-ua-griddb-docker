package mototoke.opc.ua.client;

import java.util.Timer;
import java.util.TimerTask;

import mototoke.opc.ua.client.services.ClientRunner;
import mototoke.opc.ua.client.services.MyBrowseNodeService;
import mototoke.opc.ua.client.services.MyBrowseService;
import mototoke.opc.ua.client.services.MyClientService;

public class App 
{

    // 環境変数から設定値を取得
    private static final String tagName = System.getenv("TAG_NAME");
    private static final String executionCycleStr = System.getenv("EXECUTINON_CYCLE");

    public static void main( String[] args )
    {
        Integer executionCycle = Integer.getInteger(executionCycleStr, 10);

        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    // MyClientService service = new MyClientService(tagName);
                    MyBrowseNodeService service = new MyBrowseNodeService();
                    // MyBrowseService service = new MyBrowseService();
                    new ClientRunner(service).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        // 1秒後にexecutionCycleの間隔でtaskを定期実行
        timer.scheduleAtFixedRate(task,1000, executionCycle);
    }
}
