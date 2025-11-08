<h1 align="center">Lista de Tarefas 06 - Correção de CORS</h1>

<p align="center">
  <strong>Projeto Full Stack para a disciplina de Projeto de Aplicação, demonstrando a identificação e correção de um erro de CORS em uma API Spring Boot.</strong>
</p>

<p align="center">
  <img alt="Java" src="https://img.shields.io/badge/Java-17%2B-orange?logo=java&style=for-the-badge">
  <img alt="Spring Boot" src="https://img.shields.io/badge/Spring_Boot-3.x-6DB33F?style=for-the-badge&logo=spring">
  <img alt="Status" src="https://img.shields.io/badge/status-corrigido-blue?style=for-the-badge">
</p>

---

## 📋 Sumário

* [Descrição do Problema (Erro de CORS)](#-descrição-do-problema)
* [Solução e Correção Aplicada](#-solução-e-correção-aplicada)
* [Passo a Passo para Executar o Projeto](#-passo-a-passo-para-executar)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)

---

## 🚨 Descrição do Problema

### O Erro Encontrado

Ao tentar executar a aplicação pela primeira vez, com o Frontend e o Backend rodando separadamente, o carregamento dos dados iniciais (seed) ou qualquer requisição do frontend (ex: `http://localhost:3000`) para a API (ex: `http://localhost:8080/tarefas`) falhava.

No console do navegador (F12), o seguinte erro era exibido:

```
Access to fetch at 'http://localhost:8080/tarefas' 
from origin 'http://localhost:3000' has been blocked by CORS policy: 
No 'Access-Control-Allow-Origin' header is present on the requested resource.
```

### Causa do Erro

Isso acontece por causa da política de segurança **CORS (Cross-Origin Resource Sharing)**, implementada pelos navegadores.

Por padrão, um navegador **bloqueia** requisições HTTP feitas por um script a um domínio (origem) diferente do seu. Em nosso caso:

* **Origem do Frontend:** `http://localhost:3000`
* **Origem do Backend:** `http://localhost:8080`

Como as origens são diferentes (a porta é diferente), o navegador bloqueia a comunicação. Para que funcione, o **servidor Backend** (`localhost:8080`) precisa explicitamente autorizar o **Frontend** (`localhost:3000`) a fazer requisições, enviando o cabeçalho `Access-Control-Allow-Origin` na resposta.

---

## 🛠️ Solução e Correção Aplicada

A correção foi implementada no **Backend**, configurando o Spring Boot para permitir requisições vindas da origem do nosso frontend.

Em vez de usar a anotação `@CrossOrigin` em cada método de cada Controller (o que pode ser repetitivo e propenso a erros), foi criada uma configuração global de CORS.

**Arquivo de Correção:** `src/main/java/com/example/listatarefas/config/WebConfig.java`

**Código Alterado:**
```java
package com.example.listatarefas.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configura as permissões de CORS globalmente para toda a aplicação.
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // Permite CORS para todas as rotas (/**)
            .allowedOrigins("http://localhost:3000") // Permite requisições SOMENTE desta origem
            .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Métodos HTTP permitidos
            .allowedHeaders("*") // Permite todos os cabeçalhos na requisição
            .allowCredentials(true); // Permite o envio de credenciais (como cookies)
    }
}
```

### Explicação da Correção:

Esta classe de configuração (`WebConfig`) informa ao Spring Boot que ele deve interceptar todas as rotas (`/**`) e adicionar os cabeçalhos CORS corretos.  
O mais importante é o `.allowedOrigins("http://localhost:3000")`, que diz ao navegador:  
"É seguro aceitar requisições vindas do `http://localhost:3000`."

Com essa configuração, o frontend passa a ter permissão para se comunicar com o backend, e a aplicação funciona como esperado.

---

## 🚀 Passo a Passo para Executar o Projeto

Para testar a solução completa, siga os passos para rodar o Backend e o Frontend simultaneamente.

### 1. Backend (API Spring Boot)

1.  Clone este repositório.
2.  Abra o projeto em sua IDE (IntelliJ, VS Code, etc.).
3.  Execute a classe principal `ListaTarefasApplication.java`.
4.  O servidor backend estará rodando em: `http://localhost:8080`.

### 2. Frontend (Aplicação Cliente)

1.  Navegue até a pasta do frontend (ex: `/frontend`).
2.  Instale as dependências (caso seja um projeto Node.js):
    ```bash
    npm install
    ```
3.  Inicie o servidor de desenvolvimento:
    ```bash
    npm start
    ```
4.  A aplicação frontend estará disponível em: `http://localhost:3000`.

*(Se o frontend for apenas um arquivo `index.html`, basta abri-lo usando a extensão "Live Server" do VS Code para que ele rode em uma porta diferente, simulando o cenário).*

Agora, acesse `http://localhost:3000` no seu navegador.  
O frontend conseguirá fazer as chamadas para o backend e carregar os dados sem o erro de CORS.

---

## 📦 Tecnologias Utilizadas

* **Backend:** Java 17, Spring Boot 3, Spring Data JPA, Maven  
* **Banco de Dados:** H2 Database (em memória)  
* **Frontend:** HTML, CSS, JavaScript (ou React/Angular/Vue, dependendo do tutorial base)  
* **Correção:** Spring WebMvcConfigurer (Global CORS)

---

## 👨‍💻 Autor

Desenvolvido por **Matheus de Almeida Vaz Rodrigues**  
📧 E-mail: [matheusdadonexd23@gmail.com](mailto:matheusdadonexd23@gmail.com)  
💼 GitHub: [Matheusxd43](https://github.com/matheus-rodrigues)

---
