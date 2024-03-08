# Spring Inventory

This is a [spring](https://spring.io/) implementation of naive inventory system.

Now, there is only one service in this system:

- reserving products: 

    It is a naive mimic of allocation. Just do a "subtraction".

## Test

To run tests in container,

```sh
make test
```

To run tests locally, 

- change the profile defined in `src/main/resources/application.properties` from `container` to `local`; that is, from

    ```properties
    spring.profiles.active=container
    ```

    to

    ```properties
    spring.profiles.active=local
    ```
- run database locally; for example,

    ```sh
    make local-db
    ```

## Run application

To run application in container,

```sh
make up
```

Remember to tear down after use,

```sh
make down
```

To run locally, refer to running [Test](#test) locally.