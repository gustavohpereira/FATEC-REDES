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

## Início do Projeto

### **Configuração dos Servidores Frontend com Apache**

#### 1. Criação e Configuração das Instâncias EC2 e do Banco de Dados RDS

- **Criação da primeira instância EC2** para servir como frontend.
- Configurar o grupo de segurança padrão com acesso geral `0.0.0.0/0` por questões práticas.
- **Criação da instância RDS** para servir como banco de dados PostgreSQL. Lembrar de configurar o grupo de segurança igual ao da instância EC2.

#### 2. Instalação do Apache2 para Servir o Conteúdo do Frontend
```bash
sudo apt-get install apache2

### Passo 1: Criar uma Instância EC2 para o Load Balancer

#### **1. Acesso ao AWS Management Console**
- Entre no **AWS Management Console** e navegue para o serviço **EC2**.

#### **2. Criar uma Nova Instância EC2**
- Clique em **Launch Instance** e configure conforme as etapas abaixo:
  - **AMI**: Escolha uma AMI baseada no Ubuntu Server (ex.: `Ubuntu 20.04`).
  - **Tipo de Instância**: Selecione um tipo como `t2.micro` (grátis para o nível gratuito da AWS).
  - **Armazenamento**: Use o padrão (8 GB ou mais, dependendo das necessidades).
  - **Segurança**:
    - Crie ou configure um **Grupo de Segurança** com as seguintes regras:
      - **HTTP (porta 80)**: Permitir acesso de qualquer lugar (`0.0.0.0/0`).
      - **HTTPS (porta 443)**: Permitir acesso de qualquer lugar (`0.0.0.0/0`).
      - **SSH (porta 22)**: Restrinja ao seu IP público atual.

#### **3. Acessar a Instância**
- Após o lançamento, conecte-se à instância via SSH:
```bash
ssh -i "seu-arquivo.pem" ubuntu@<seu-endereço-ip>


```

2. Instalar o Nginx
Instale o Nginx na instância:
bash
Copiar código
sudo apt install nginx -y


3. Configurar o Nginx como Load Balancer
Edite o arquivo de configuração do Nginx:
bash
Copiar código
sudo nano /etc/nginx/sites-available/default


Substitua o conteúdo pelo seguinte:
nginx
Copiar código
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

4. Testar e Reiniciar o Nginx
Verifique a configuração:
bash
Copiar código
sudo nginx -t



Reinicie o Nginx para aplicar as mudanças:
bash
Copiar código
sudo systemctl restart nginx
