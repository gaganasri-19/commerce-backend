open docker desktop
docker compose up -d
docker logs -f commerce-backend-commerce-backend-1

kafka topic:
docker exec -it commerce-backend-kafka-1 kafka-console-consumer --bootstrap-server localhost:9092 --topic order.created --from-beginning

redis:
 docker exec -it commerce-backend-redis-1 redis-cli
 keys *

to approve the payment from paypal/send paypal webhook:
postman request POST 'http://localhost:8082/api/paypal/webhook' \
  --header 'Content-Type: application/json' \
  --header 'Cookie: JSESSIONID=FC7293A53F92A0CAAC12609FBE151C27' \
  --body '{
  "event_type": "CHECKOUT.ORDER.APPROVED",
  "resource": {
    "id": "{paypal_orderid}"
  }
}'


(take paypal order from mysql payments table)



if mysql data is erased:
docker compose down -v
enable init in compose and run once with create in commerce app.
next time run without it
