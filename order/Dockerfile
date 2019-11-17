FROM node

MAINTAINER Nicolas Chabanoles @ Bonitasoft

COPY . /orders-service
WORKDIR /orders-service

RUN npm install

CMD node /orders-service/app.js