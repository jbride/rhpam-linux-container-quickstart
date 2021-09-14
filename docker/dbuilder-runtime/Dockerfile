# buildah bud -f docker/dbuilder-runtime/Dockerfile -t quay.io/redhat_naps_da/dashbuilder-runtime-psql:7.54.0.Final-6 .

# FROM quay.io/wsiqueir/dashbuilder-runtime:latest
FROM jboss/wildfly:19.1.0.Final

USER root

COPY ./docker/dbuilder-runtime/configuration /opt/jboss/wildfly/standalone/configuration/


COPY ./docker/dbuilder-runtime/deployments/*.war /opt/jboss/wildfly/standalone/deployments/

RUN chown -R jboss:root /opt/jboss/wildfly/standalone/
USER jboss:root
