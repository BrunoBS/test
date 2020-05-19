**Requisitos:**

- JDK (Java Development Kit) 1.8 ou superior.
- Apache Maven 3.2.3


**Para baixar as dependencias da aplicação**

- Abre o  terminal do seu sistema operacional
- Navegue até a pasta raiz do projeto e digite o seguinte comando:

```
   mvn clean install
   
```


**Como Executar os testes da aplicação**

```
   mvn install -Dmaven.test.skip=true
   
```

**Para executar a a aplicação execuatar o comando abaixo** 

```
  mvn spring-boot:run
```

Após executar a  aplicação acessar o link: http://localhost:8080/swagger-ui.html  para conhecer a documentação da API.



**Observações**

Foi desenvolvido uma  criptografia assimétrica funciona da seguinte forma:
O servidor e o cliente geram as suas chaves públicas e privadas. O servidor envia para o cliente sua chave pública e o cliente envia para o servidor sua chave pública, para testar a execução do  projeto foi criado um endpoint que gera as chaves privada e publica do lado do servidor e um endpoint que criptografa os dados solicitados utilizando a chave publica do servidor (para enviar para o servidor).

http://localhost:8080/swagger-ui.html#!/app45controller/configUsingOPTIONS

http://localhost:8080/swagger-ui.html#!/app45controller/cifraUsingOPTIONS





