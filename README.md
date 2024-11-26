# FATEC-REDES
 # ATIVIDADE REDES DE COMPUTADORES

O objetivo da atividade é desenvolver uma infraestrutura de rede para uma empresa que cumpra os seguintes requisitos:

## Requisitos do Projeto

### 1. Load Balancer
- Implementar um sistema de balanceamento de carga utilizando no mínimo 3 máquinas para distribuir o tráfego.

### 2. Proxy Reverso
- Configurar uma máquina para atuar como proxy reverso.

### 3. Banco de Dados
- Implementar uma máquina dedicada para o banco de dados.

### 4. VPN
- Configurar o acesso à rede através de uma VPN.

### 5. Docker
- Utilizar o Docker para criar os servidores web e o banco de dados, garantindo a portabilidade e fácil gestão dos serviços.

---

## Requisitos do Trabalho

### **1. Arquitetura da Rede**
- Desenhar a topologia da rede, identificando as máquinas, conexões e fluxos de dados.
- Detalhar o uso do load balancer, proxy reverso e banco de dados.

### **2. Configuração do Load Balancer**
- Implementar um load balancer utilizando **Nginx**.
- Configurar para balancear o tráfego entre, no mínimo, 3 máquinas que servirão conteúdo web.

### **3. Proxy Reverso**
- Configurar uma máquina com **Nginx** para atuar como proxy reverso.
- Detalhar como o proxy reverso irá gerenciar as requisições e redirecioná-las para as máquinas corretas.

### **4. Banco de Dados**
- Criar uma máquina para o banco de dados utilizando o **Docker** ou **AWS RDS**.
- Implementar e configurar um banco de dados relacional ou não relacional (MySQL, PostgreSQL, MongoDB etc.) dentro de um container Docker ou AWS RDS.
- Detalhar as medidas de segurança adotadas para proteger os dados armazenados.

### **5. VPN**
- Configurar uma VPN (como **OpenVPN**) para que o acesso à rede da empresa XPTO seja seguro.
- Demonstrar o processo de configuração da VPN e como ela se integra com o restante da infraestrutura.

### **6. Docker**
- Utilizar o Docker para criar e gerenciar os containers dos servidores web e do banco de dados.
- Explicar a configuração dos containers e como eles se comunicam entre si.
- Demonstrar como os containers podem ser escalados ou migrados para outros ambientes.

---

## Entrega

### **1. Relatório Técnico**
- Documento detalhado com toda a configuração, justificativas para as escolhas feitas, desafios encontrados e soluções implementadas.
- Incluir diagramas da rede, comandos utilizados, arquivos de configuração e capturas de tela.

### **2. Demonstração**
- Apresentação prática, onde o(a) aluno(a)/dupla/trio deverá demonstrar o funcionamento da infraestrutura montada.
- Simular o acesso à rede via VPN, o funcionamento do load balancer e do proxy reverso, e a operação dos containers Docker.

### **3. Código e Configurações**
- Todos os arquivos de configuração e scripts utilizados devem ser entregues junto com o relatório.
- O projeto deverá ser entregue via **GitHub**, com documentação clara de como replicar o ambiente.

---


## INTEGRANTES

- [Gustavo Henrique Pereira de Abreu](https://github.com/gustavohpereira)
- [Pedro Henrique Pucci](https://github.com/pedro11pucci)
- [Luis Cardoso](https://github.com/LuisSCardoso)


![image](https://github.com/user-attachments/assets/0115937f-9e65-4ad2-b1eb-cd89a6e64876)


## Início do Projeto

### **Configuração dos Servidores Frontend com Apache**

#### 1. Criação e Configuração das Instâncias EC2 para Nginx e Load Balancer

- **Criação da primeira instância EC2** para servir como frontend.
- Configurar o grupo de segurança padrão com acesso geral `0.0.0.0/0` por questões práticas.


Após acessar a maquina ec2:

- 2. Instalar o Nginx
Instale o Nginx na instância:
```
sudo apt install nginx -y
```


- 3. Configurar o Nginx como Load Balancer
Edite o arquivo de configuração do Nginx:
```
sudo nano /etc/nginx/sites-available/default
```


Substitua o conteúdo pelo seguinte:
```
 upstream backend_servers {
     server <IP_do_servidor_web_1>;
     server <IP_do_servidor_web_2>;
     server <IP_do_servidor_web_3>;
 }
 
 server {
     listen 80;
     
     location / {
         proxy_pass http://backend_servers;
         proxy_set_header Host $host;
         proxy_set_header X-Real-IP $remote_addr;
         proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
     }
 }
```
4. Testar e Reiniciar o Nginx
Verifique a configuração:
```
sudo nginx -t
```



Reinicie o Nginx para aplicar as mudanças:
```
sudo systemctl restart nginx
```



## BACK-END

### criar uma ec2
crie uma ec2 para servir como servidor back-end para nossa aplicação

### clonar back-end

```
   git clone https://github.com/gustavohpereira/FATEC-REDES.git
```

### Rode o docker 

#### dentro do arquivo há um docker file e um docker compose, nos quais rodam a aplicação junto de um container de mysql, rode o docker compose

```
docker compose up
```



## SERVERS WEB

### para servir nossas aplicações front-end, criamos uma ec2 para cada front-end ( para uso do load balancer )


agora dentro de cada uma, você vai instalar o apache

```
 sudo apt-get install apache2
```
agora renicie o apache
```
 sudo systemctl restart apache2
```

e entre nas configurações do apache e coloque o html do repositório nele (/var/www/html)

```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Mensagens</title>
    <script>
const apiUrl = 'http://23.20.134.85:8080/api/mensagem';

        async function fetchMessages() {
            try {
    const response = await fetch(apiUrl, {
    method: 'GET',
    headers: {
    'Content-Type': 'application/json'
    },
    credentials: 'same-origin'
    });
                if (!response.ok) {
                    throw new Error('Erro ao buscar mensagens');
                }
                const messages = await response.json();
                displayMessages(messages);
            } catch (error) {
                console.error(error);
            }
        }

       
        function displayMessages(messages) {
            const messageList = document.getElementById('messageList');
            messageList.innerHTML = '';

            messages.forEach(message => {
                const listItem = document.createElement('li');
                listItem.textContent = `${message.data}: ${message.mensagem}`;
                messageList.appendChild(listItem);
            });
        }

        
        async function sendMessage() {
            const messageInput = document.getElementById('messageInput');
            const mensagem = messageInput.value;
            const data = new Date().toISOString().split('T')[0];

            try {
                const response = await fetch(apiUrl, {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({ mensagem, data })
                });

                if (!response.ok) {
                    throw new Error('Erro ao enviar mensagem');
                }

                messageInput.value = ''; 
                fetchMessages();
            } catch (error) {
                console.error(error);
            }
        }
        
        window.onload = fetchMessages;
    </script>
</head>
<body>
    <main>
        <div>
            <h1>Aplicação 1</h1>
            <ul id="messageList"></ul> 
            <input type="text" id="messageInput" placeholder="Digite sua mensagem" />
            <button onclick="sendMessage()">Enviar</button>
        </div>
    </main>
</body>
</html>
```

entre no html e atualize o endpoint do back-end

## VPN

### instale openvpn em sua ec2 do loadbalancer por meio de um script

```
wget https://git.io/vpn -O openvpn-install.sh

```

Torne o script executável:

```
sudo chmod +x openvpn-install.sh
```

Execute o script para instalar o OpenVPN:

```
sudo bash openvpn-install.sh
```

Copie o arquivo de configuração do cliente para o diretório do usuário padrão:

```
sudo cp /root/client1.ovpn ~
```

Baixe o arquivo .ovpn para a máquina local:

```
scp -i /caminho/para/chave.pem ubuntu@<IP-SERVIDOR>:/home/ubuntu/client1.ovpn .
```

instale o aplicativo da openvpn

ja instalado, coloque a chave que você conseguiu no aplicativo da openvpn e rode

acesse o endereço da vpn no seu navegador







