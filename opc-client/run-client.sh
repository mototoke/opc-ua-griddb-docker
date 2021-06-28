#!/bin/bash
export CLASSPATH=${CLASSPATH}:/root/./opc-ua-client-milo.jar
javac MyOpcClient.java
java MyOpcClient