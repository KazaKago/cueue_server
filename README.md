# ktor_starter
"Ktor" Starter Kit with Deploying GAE.

## Run Locally

```sh
$ ./gradlew app:run
$ curl http://localhost:8080
```

## Deploy Google App Engine

1. Install gcloud CLI.
    - `$ brew cask install google-cloud-sdk`
1. Login account.
    - `$ gcloud auth login`

```sh
$ ./gradlew app:appengineDeploy
```
