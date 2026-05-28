# Avaliação de Desempenho do Protocolo MQTT

Este projeto contém a infraestrutura e a aplicação de injeção de carga desenvolvidas para avaliar empiricamente o desempenho, a resiliência e o consumo de recursos do protocolo MQTT. O ambiente foi construído como parte da componente prática de um artigo científico submetido ao Congresso Brasileiro Interdisciplinar em Ciência e Tecnologia (CoBICET 2026).

## Tecnologias Utilizadas
- Java 21
- Maven (Gerenciador de dependências e automação de build)
- Eclipse Paho (Cliente MQTT nativo)
- Eclipse Mosquitto (Broker MQTT)
- Ferramentas nativas Linux (Monitoramento via htop)

## Estrutura da Arquitetura
O repositório adota a separação rigorosa de responsabilidades entre servidor e cliente:
 1. Camada de Infraestrutura: O arquivo mosquitto.conf localizado na raiz atua como a configuração primária do servidor, expondo o serviço localmente na porta 1883.
 2. Camada de Aplicação: O diretório load-tester/ contém o script injetor construído em Java, responsável por instanciar múltiplas conexões e publicar 10.000 mensagens síncronas de telemetria para testes de estresse (throughput).

## Como rodar o experimento localmente?
Para reproduzir o ambiente de testes, é necessário abrir três instâncias de terminal para isolar a execução dos serviços.

Terminal 1 (Broker MQTT):
```bash
mosquitto -c mosquitto.conf
```

Terminal 2 (Monitoramento de Hardware):
```bash
htop
```

Terminal 3 (Injeção de Carga):
```bash
cd load-tester
mvn clean compile exec:java -Dexec.mainClass="App"
```
