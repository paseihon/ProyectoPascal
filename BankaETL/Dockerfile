FROM openjdk:8-jdk

# Instalar dependencias
RUN apt-get update && apt-get install -y curl tar

# Descargar Spark 2.3.0 prebuild
RUN curl -O https://archive.apache.org/dist/spark/spark-2.3.0/spark-2.3.0-bin-hadoop2.7.tgz && \
    tar -xzf spark-2.3.0-bin-hadoop2.7.tgz -C /opt && \
    rm spark-2.3.0-bin-hadoop2.7.tgz

ENV SPARK_HOME=/opt/spark-2.3.0-bin-hadoop2.7
ENV PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin

# Copiar tu JAR
COPY target/BankaETL-1.0-SNAPSHOT.jar /app/app.jar
COPY bank.csv /app/bank.csv

WORKDIR /app

# Ejecutar Spark con tu JAR
CMD ["spark-submit", "--class", "org.example.BankETL", "app.jar"]
