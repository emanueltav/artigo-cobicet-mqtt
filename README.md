# Avaliação de Desempenho do Protocolo MQTT

Este projeto contém a infraestrutura e a aplicação de injeção de carga desenvolvidas para avaliar empiricamente o desempenho, a resiliência e o consumo de recursos do protocolo MQTT. O ambiente foi construído como parte da componente prática de um artigo científico submetido ao Congresso Brasileiro Interdisciplinar em Ciência e Tecnologia (CoBICET 2026).

## Tecnologias Utilizadas
- Java 21
- Maven (Gerenciador de dependências e automação de build)
- Eclipse Paho (Cliente MQTT nativo)
- Eclipse Mosquitto (Broker MQTT)
- Ferramentas nativas Linux (Monitoramento via htop e criptografia via mosquitto_passwd)

## Estrutura da Arquitetura
O repositório adota a separação rigorosa de responsabilidades entre servidor e cliente, implementando práticas de segurança e otimização de memória:

 1. Camada de Infraestrutura: O arquivo `mosquitto.conf` localizado na raiz atua como a configuração primária do servidor, expondo o serviço localmente na porta 1883 com bloqueio severo a conexões anônimas. A autenticação é exigida e validada através do arquivo `mosquitto.pwd` (criptografia PBKDF2 com SHA-512).
 2. Camada de Aplicação: O diretório `load-tester/` contém o motor de injeção construído em Java. O script atua de forma automatizada, autenticando-se no broker e executando baterias sequenciais de testes de estresse (throughput). O motor publica lotes de 10.000 mensagens de telemetria transitando pelos três níveis de garantia de entrega do MQTT (QoS 0, 1 e 2). A janela de retenção em memória (`MaxInflight`) da aplicação foi customizada para suportar a alta concorrência de *handshakes* sem causar estouro de pilha.

## Credenciais de Acesso (Ambiente de Laboratório)
Para garantir a reprodutibilidade do experimento de forma direta, o arquivo de credenciais simuladas foi intencionalmente mantido no controle de versão:
- **Usuário:** `admin`
- **Senha:** `admin123`

## Como rodar o experimento localmente?
Para reproduzir a bateria de testes, é necessário abrir três instâncias de terminal para isolar a execução dos serviços.

Terminal 1 (Broker MQTT Seguro):
```bash
mosquitto -c mosquitto.conf
```

Terminal 2 (Monitoramento de Hardware):
```bash
htop
```

Terminal 3 (Injeção de Carga e Análise de QoS):
```bash
cd load-tester
mvn clean compile exec:java -Dexec.mainClass="App"
```

*Nota: Ao acionar o motor de injeção no Terminal 3, a aplicação Java executará automaticamente o loop pelos níveis QoS 0, QoS 1 e QoS 2, aguardando a liberação de rede entre cada ciclo e imprimindo o tempo total (em milissegundos) de cada nível no console.*