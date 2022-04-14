ssh -t root@207.244.248.194 " cd /opt/projects/auction/websocket-auction ;
                              git pull ;
                              bash mvnw clean package "

#ssh -t root@207.244.248.194  "docker stop ''$(docker ps -a -q --filter ancestor=quarkus/websocket-auction --format="{{.ID}}") ;
#docker run  -d --rm -i -p 9090:8080 quarkus/websocket-auction"
