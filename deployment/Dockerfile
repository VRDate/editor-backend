FROM asciidoctor/asciidoctorj-wildfly
MAINTAINER Maxime Gréau <greau.maxime@gmail.com>

#RUN curl -L https://github.com/adoc-editor/editor-backend/raw/master/solution/editor-backend-1.0-SNAPSHOT.war -o /opt/jboss/wildfly/standalone/deployments/editor-backend.war
ADD ../target/editor-backend.war ${JBOSS_HOME}/standalone/deployments/editor-backend.war

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
