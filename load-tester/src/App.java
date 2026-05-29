import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class App {
    public static void main(String[] args) {
        String broker = "tcp://localhost:1883";
        String clientId = "InjetorDeCargaQoS";
        int messagesToSend = 10000;
        
        int[] qosLevels = {0, 1, 2};

        try (MqttClient client = new MqttClient(broker, clientId, new MemoryPersistence())) {
            
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            connOpts.setUserName("admin");
            connOpts.setPassword("admin123".toCharArray());
            
            connOpts.setMaxInflight(messagesToSend);
            
            System.out.println("Conectando ao broker de forma segura: " + broker);
            client.connect(connOpts);
            System.out.println("Autenticado! Iniciando bateria de testes de estresse (QoS 0, 1 e 2)...\n");

            for (int qos : qosLevels) {
                System.out.println(">>> Iniciando injeção de " + messagesToSend + " mensagens com QoS " + qos + " <<<");
                
                long startTime = System.currentTimeMillis();

                for (int i = 1; i <= messagesToSend; i++) {
                    String content = "{\"id_sensor\": 1, \"leitura\": " + i + "}";
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(qos);
                    client.publish("telemetria/dados", message);
                }

                long endTime = System.currentTimeMillis();
                long totalTime = endTime - startTime;

                System.out.println("[RESULTADO] QoS " + qos + " concluído em: " + totalTime + " ms\n");
                
                Thread.sleep(2000); 
            }

            System.out.println("Bateria de testes finalizada com sucesso!");
            client.disconnect();

        } catch (MqttException | InterruptedException e) {
            System.err.println("Erro na execução dos testes: " + e.getMessage());
            e.printStackTrace();
        }
    }
}