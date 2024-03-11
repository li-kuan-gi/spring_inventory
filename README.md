# Spring Inventory

This is a [spring](https://spring.io/) implementation of naive inventory system.

Now, there are four services in this system:

- reserving products

    > A naive mimic of allocation; just do a "subtraction".

- restocking products

- getting quantities

- adding product

    > A [rabbitmq](https://www.rabbitmq.com/) consumer to add/create new product.


## Test

The dafault test environment is a container. To run tests,

```sh
make test
```

If want to run tests locally, 

- change the active profile defined in `src/test/resources/application.properties` from `main` to `local`; that is, from

    ```properties
    spring.profiles.active=main
    ```

    to

    ```properties
    spring.profiles.active=local
    ```
- run resources locally; for example,

    ```sh
    make local-resources
    ```
and remember to tear down resources after test,

```sh
make down
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