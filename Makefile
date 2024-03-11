up:
	docker compose up -d app

down:
	docker compose down --remove-orphans -v --rmi local

test-without-clean-up:
	docker compose run test

test: test-without-clean-up down

local-db:
	docker compose run --rm -d -p 3306:3306 db

local-mq:
	docker compose run --rm -d -p 5672:5672 -p 15672:15672 mq

local-resources: local-db local-mq