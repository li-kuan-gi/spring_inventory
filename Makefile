down:
	docker compose down --remove-orphans -v --rmi local

test-without-clean-up:
	docker compose run test

test: test-without-clean-up down