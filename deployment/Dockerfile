FROM asciidoctor/asciidoctorj-wildfly
MAINTAINER Maxime Gréau <greau.maxime@gmail.com>

#RUN curl -L https://github.com/adoc-editor/editor-backend/raw/master/solution/editor-backend-1.0-SNAPSHOT.war -o /opt/jboss/wildfly/standalone/deployments/editor-backend.war
#ADD editor-backend.war $JBOSS_HOME/standalone/deployments/editor-backend.war

ENV JAVA_OPTS "-server -Xms1024m -Xmx2056m -XX:MaxPermSize=512m -XX:+TieredCompilation -XX:TieredStopAtLevel=1 -Djava.net.preferIPv4Stack=true -Djboss.modules.system.pkgs=org.jboss.byteman -Djava.awt.headless=true"
ADD standalone-editor.xml $JBOSS_HOME/standalone/configuration/

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-c", "standalone-editor.xml", "-b", "0.0.0.0", "-bmanagement", "0.0.0.0"]
