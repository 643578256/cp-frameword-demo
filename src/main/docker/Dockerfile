FROM java:8
COPY demo.jar demo.jar
ENTRYPOINT exec java -Xms64m -Xmx128m -Xss128K -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=28887 -jar /demo.jar
#这里可以远程调试参数
#-Dfile.encoding=UTF-8 -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=28887