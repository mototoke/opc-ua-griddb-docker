FROM java:8

# Install maven
ENV MAVEN_VERSION 3.5.2
ENV MAVEN_BASE=apache-maven-${MAVEN_VERSION}
RUN curl -OL https://archive.apache.org/dist/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz &&\
    tar zxvf ${MAVEN_BASE}-bin.tar.gz && mv $MAVEN_BASE /opt/maven
ENV PATH=$PATH:/opt/maven/bin

WORKDIR /root
# 必要ファイルのコピー
COPY MyOpcClient.java /root
COPY run-client.sh /root

# Set permission executable for script
RUN chmod a+x run-client.sh

# Get dependency for OPC UA SDK Client
RUN mvn dependency:get -Dartifact=org.eclipse.milo:sdk-client:0.6.1 -Dtest=./opc-ua-client-milo.jar

# Run Spring Boot Module
CMD ["/bin/bash", "run-client.sh"]