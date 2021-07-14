FROM openjdk:18

# Install maven
ENV MAVEN_VERSION 3.5.2
ENV MAVEN_BASE=apache-maven-${MAVEN_VERSION}
RUN curl -OL https://archive.apache.org/dist/maven/maven-3/3.5.2/binaries/apache-maven-3.5.2-bin.tar.gz &&\
    tar zxvf ${MAVEN_BASE}-bin.tar.gz && mv $MAVEN_BASE /opt/maven
ENV PATH=$PATH:/opt/maven/bin

# Run
RUN mkdir /root/project
WORKDIR /root/project

CMD cd data-register && mvn install && mvn exec:java -Dexec.mainClass=mototoke.opc.ua.griddb.register.App
