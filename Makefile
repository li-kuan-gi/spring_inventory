up:
	docker compose up -d app

down:
	docker compose down --remove-orphans -v --rmi local

test-without-clean-up:
	docker compose run test

test: test-without-clean-up down

local-db:
	docker compose run --rm -d -p 3306:3306 db