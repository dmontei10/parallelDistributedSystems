FROM postgres 
ENV POSTGRES_PASSWORD admin
ENV POSTGRES_DB armazem 
COPY init.sql /docker-entrypoint-initdb.d/
