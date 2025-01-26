# api do salve-food
para iniciar altere a variavel de ip LOCAL_IP no .env e rode o docker-compose-dockerized.yml
comando para rodar:
```
docker compose -f "docker-compose-dockerized.yml" up -d --build 

```

# caso esteja usando linux e já tenha rodado o projeto antes, e por algum motivo esteja observando um erro ao tentar iniciar o projeto via docker compose é mais rapido só usar o script que está na pasta inicial, para isso, va ao arquivo Dockerfile e comente com um # a linha 
```
RUN mvn clean package -DskipTests
```
ela deve ou ser apagada ou comentada.

```
#RUN mvn clean package -DskipTests
```
após isso rode o script.sh e irá compilar na sua maquina, depois irá iniciar o docker copiando tudo da sua maquina para a outra e irá rodar o projeto