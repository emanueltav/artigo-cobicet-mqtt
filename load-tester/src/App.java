import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "InjetorDeCargaJava";
        int messagesToSend = 10000;

        try (MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence())) {
            
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("admin");
            connOpts.setPassword("admin123".toCharArray());
            
            System.out.println("Conectando ao broker de forma segura: " + broker);
            client.connect(connOpts);
            System.out.println("Autenticado com sucesso! Iniciando injeção de carga...");

            long startTime = System.currentTimeMillis();

            for (int i = 1; i <= messagesToSend; i++) {
                String content = "{\"id_sensor\": 1, \"leitura\": " + i + "}";
                MqttMessage message = new MqttMessage(content.getBytes());
                message.setQos(0); 
                client.publish("telemetria/dados", message);
            }

            long endTime = System.currentTimeMillis();
            long totalTime = endTime - startTime;

            System.out.println("Teste concluído com sucesso!");
            System.out.println("Mensagens enviadas: " + messagesToSend);
            System.out.println("Tempo total de execução: " + totalTime + " ms");
            
            client.disconnect();
        } catch (MqttException me) {
            System.err.println("Erro na comunicação MQTT: " + me.getMessage());
            me.printStackTrace();
        }
    }
}