open docker desktop
docker compose up -d
docker logs -f commerce-backend-commerce-backend-1

kafka topic:
docker exec -it commerce-backend-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic order.created --from-beginning

redis:
 docker exec -it commerce-backend-redis-1 redis-cli
 keys *

