FROM openjdk:21

ENV JAR_FILE="application.jar"
ENV JAVA_OPTS="-XX:+UseStringDeduplication -Xmx1024m"
ENV JMX_PORT=9016
ENV JMX_OPTS="-Dcom.sun.management.jmxremote \
-Dcom.sun.management.jmxremote.port=$JMX_PORT \
-Dcom.sun.management.jmxremote.rmi.port=$JMX_PORT \
-Dcom.sun.management.jmxremote.ssl=false \
-Dcom.sun.management.jmxremote.local.only=false \
-Dcom.sun.management.jmxremote.authenticate=false"

WORKDIR /app
ADD build/libs/chat-crawler-*.jar ./$JAR_FILE

EXPOSE 4141

ENTRYPOINT exec java $JAVA_OPTS $JMX_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/$JAR_FILE

