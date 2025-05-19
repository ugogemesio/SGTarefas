# Etapa 1: Build da aplicação Angular
FROM node:20 AS builder

WORKDIR /app

COPY . .
RUN npm install

RUN npm run build 

# Etapa 2: Nginx para servir o build
FROM nginx:alpine

# Copia o build gerado para o nginx
COPY --from=builder /app/dist/frontend/browser /usr/share/nginx/html


COPY nginx.conf /etc/nginx/nginx.conf
COPY mime.types /etc/nginx/mime.types

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

