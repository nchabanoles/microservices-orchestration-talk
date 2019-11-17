FROM node

MAINTAINER Nicolas Chabanoles @ Bonitasoft

COPY . /payments-service
WORKDIR /payments-service

RUN npm install

CMD node /payments-service/app.js