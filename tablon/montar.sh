~/tomcat/webapps/axis2-binario/bin/java2wsdl.sh -cn Tablon Tablon.class
~/tomcat/webapps/axis2-binario/bin/wsdl2java.sh -o . -sn Tablon -ss -sd -ssi -uri Tablon.wsdl 
/home/cubo/tomcat/apache-ant-1.10.1/bin/ant jar.server
cp build/lib/*.aar /home/cubo/tomcat/webapps/axis2/WEB-INF/services



