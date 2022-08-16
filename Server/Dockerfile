FROM eclipse-temurin:8

RUN apt-get update -y
RUN apt-get install software-properties-common -y
RUN add-apt-repository ppa:deadsnakes/ppa -y
RUN apt-get install python3.9 -y
RUN ls /usr/bin/python3*
RUN update-alternatives --install /usr/bin/python python /usr/bin/python3.9 1
RUN python --version
RUN python -m site
RUN cd /usr/local/lib/python3.9/dist-packages
RUN DEBIAN_FRONTEND=noninteractive apt-get install curl -y
RUN curl -L https://github.com/IfcOpenShell/IfcOpenShell/releases/download/blenderbim-220711/blenderbim-220711-py39-linux.zip --output ifc.zip
RUN apt-get install unzip 
RUN unzip ifc.zip
RUN cp -r ./blenderbim/libs/site/packages/ifcopenshell /usr/local/lib/python3.9/dist-packages
RUN cp -r ./blenderbim/libs/site/packages/ifcpatch /usr/local/lib/python3.9/dist-packages
RUN python -c 'import ifcopenshell;'
RUN python -c 'import ifcpatch;'
         
         
         
WORKDIR /app
COPY ./serviceAccountKey.json ./serviceAccountKey.json
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
 
COPY src ./src
EXPOSE 3030
CMD ["./mvnw", "spring-boot:run"]
