mvn clean package
docker build -f src/main/docker/Dockerfile.jvm -t quarkus/websocket-auction .
docker tag quarkus/websocket-auction registry.heroku.com/websocket-auction/web
heroku container:login
docker push registry.heroku.com/websocket-auction/web
heroku container:release web -a websocket-auction
