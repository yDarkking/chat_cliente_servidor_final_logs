✅ Sistema de Chat TCP – Cliente/Servidor com Salas e Mensagens Privadas

Este projeto implementa um sistema de chat multiusuário utilizando sockets TCP, com suporte a múltiplas salas, mensagens privadas, concorrência via threads e registro de logs por sala.

O objetivo é aproximar o conteúdo teórico da disciplina de Redes de Computadores à prática, simulando comunicação real em rede por meio do modelo Cliente/Servidor.

🎯 Funcionalidades Atendidas
Requisito	Status
Comunicação TCP entre Cliente e Servidor	✅
Multiplos clientes conectados simultaneamente	✅
Multiples salas dinâmicas	✅
Troca de mensagens em broadcast dentro da sala	✅
Entrada/Saída de salas	✅
Comandos para listar salas e usuários	✅
Encerramento seguro da conexão	✅
Logs por sala (histórico de mensagens)	✅
Timestamp nos logs	✅
Mensagens privadas entre usuários	✅
Menu de comandos com /help	✅
Interface do cliente mais amigável	✅
🖥️ Tecnologias Utilizadas

Linguagem: Java (JDK 8+)

Protocolo de transporte: TCP

Threads para controlar múltiplas conexões simultâneas

Estrutura de dados: ConcurrentHashMap para gerenciar salas

Sistema operacional sugerido: Debian no VirtualBox

🗂️ Estrutura do Projeto
/chat
 ├── ChatServer.java
 ├── ChatClient.java
 ├── /logs
 │    ├── #geral.txt
 │    ├── #sala1.txt
 │    └── ...
 └── README.md


A pasta /logs/ é criada automaticamente e armazena o histórico de cada sala separadamente.

⚙️ Como Compilar

No diretório onde estão os arquivos:

javac ChatServer.java ChatClient.java

🚀 Como Executar

📌 Primeiro inicie o servidor:

java ChatServer


Ele irá perguntar:

IP do servidor:
Porta:


📌 Depois inicie os clientes (cada terminal é um usuário):

java ChatClient


Ele irá perguntar:

IP do servidor:
Porta:
Digite seu nome:

💬 Comandos do Chat
Comando	Ação
/join #sala	Entra em uma sala (cria se não existir)
/leave	Volta para a sala padrão #geral
/list	Lista salas disponíveis
/users	Lista usuários da sala atual
/pm nome msg	Envia mensagem privada para um usuário
/help	Mostra o menu de ajuda
/exit	Desconecta do servidor
📝 Log das Salas (Opcional Extra da Atividade ✅)

Todas mensagens são gravadas em arquivos individuais por sala

Entradas e saídas de usuários registradas com timestamp

Mensagens privadas também são registradas, indicando remetente e destinatário

Exemplo de log gravado:

[05/11/2025 01:23:10] João entrou na sala #geral
[05/11/2025 01:23:18] [PM] João -> Maria: oi!

🌐 Observação sobre Topologia em Rede

Para cumprir o requisito acadêmico:

✅ Servidor em uma máquina virtual
✅ Clientes em outras VMs na mesma rede interna (NAT ou Host-Only)
✅ Todos usando o IP da VM do servidor

🎓 Objetivos Educacionais Atendidos

✔ Entendimento do modelo Cliente/Servidor
✔ Manipulação de Sockets TCP
✔ Concorrência (threads)
✔ Comunicação e broadcast em rede
✔ Gerenciamento de estado de usuários e salas
✔ Criação simples de protocolo de comunicação

📌 Possíveis Melhorias Futuras

Histórico da sala sendo exibido ao entrar

Comando /rename para trocar nome do usuário

Autenticação com cadastro

Cores e formatação no terminal

Interface gráfica (JavaFX ou Swing)

🙌 Desenvolvido por:

Aluno(a): Seu Nome Aqui
Disciplina: Redes de Computadores
Instituição: IFBA
Ano: 2025