# Imagem base do Java 17
FROM eclipse-temurin:17-jdk

# Diretório de trabalho dentro do container
WORKDIR /app

# Copiar o JAR gerado para dentro do container
COPY target/mapa-viagem-0.0.1-SNAPSHOT.jar app.jar

# Comando para rodar a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]