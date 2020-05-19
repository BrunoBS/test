# Requisitos:

- JDK (Java Development Kit) 1.8 ou superior.
- Apache Maven 3.2.3


#  Como baixar as dpendencias de sua aplicação?

- Abre o  terminal do seu sistema operacional
- Navegue até a pasta raiz do projeto e digite o seguinte comando:

```
   mvn clean install
   
```
#  Atenção
Caso *não* queira execeutar  os testes 

```
   mvn install -Dmaven.test.skip=true
   
```

Após baixar todas as dependencias executar  a aplicação:

```
  mvn spring-boot:run

```



