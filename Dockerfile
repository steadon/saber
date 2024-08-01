FROM openjdk:17-jdk-alpine

ADD target/saber-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 38210
ENTRYPOINT java \
           -Dspring.config.import=nacos:saber.yml?group=DEFAULT_GROUP \
           -Dspring.cloud.nacos.config.server-addr=192.168.115.23:8848 \
           -Dspring.cloud.nacos.config.namespace=87aaed90-bb47-4c18-8564-61bb36cb2539 \
           -XX:+UseZGC \
           -jar app.jar
