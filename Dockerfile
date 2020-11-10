FROM logr/oracle-jre8

MAINTAINER mjj

# 下载license
RUN wget -P . "https://github.com/6mb/Microsoft-365-Admin/releases/download/v1.3/microsoft-0.0.1-SNAPSHOT.jar"
RUN mv microsoft-0.0.1-SNAPSHOT.jar microsoft.jar
CMD java -jar microsoft.jar