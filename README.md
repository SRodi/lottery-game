![REST interface Lottery System](https://github.com/srodi/lottery-game/actions/workflows/main.yaml/badge.svg)

## Lottery Game

This project is a `RestAPI` developed with `SpringBoot` which implements a lottery game according to the following specifications.

## Specification

### Problem

We are looking for a REST interface to a simple lottery system. The rules of the game are described below.

### Lottery Rules

You have a series of lines on a ticket with 3 numbers, each of which has a value of 0, 1, or 2. For each ticket if the sum of the values on a line is 2, the result for that line is 10. Otherwise if they are all the same, the result is 5. Otherwise so long as both 2nd and 3rd numbers are different from the 1st, the result is 1. Otherwise the result is 0.

### Implementation

Implement a REST interface to generate a ticket with n lines. Additionally we will need to have the ability to check the status of lines on a ticket. We would like the lines sorted into outcomes before being returned. It should be possible for a ticket to be amended with n additional lines. Once the status of a ticket has been checked it should not be possible to amend. We would like tested, clean code to be returned.

## Solution

The following `HTTP Requests` are available through this `RestAPI`:

* **Create a ticket**
```http
POST     /ticket
```
  Body
```json
{
    "numberOfLines": $NUM_LINES
}
```

* **Return list of tickets**
```http
GET     /ticket
```

* **Get individual ticket**
```http
GET     /ticket/{id}
```

* **Amend ticket lines**
```http
PUT     /ticket/{id}
```
  Body
```json
{
    "numberOfLines": $NUM_LINES
}
```

* **Retrieve status of ticket**
```http
PUT     /status/{id}
```

### Run with Docker

To build and run this application on a `Docker` container, run the following.

```bash
# name image
IMG_TAG=my-img

# tag image and build using Dockerfile in current dir
docker build -t $IMG_TAG .

# run interactively and remove container after run
# expose app on port 8081 locally
docker run -it --rm -p 8081:8080 $IMG_TAG
```

Alternatively you can use the image on `Dockerhub` which is pushed via `CI`. 

```bash
# run interactively and remove container after run
# expose app on port 8081 locally
docker run -it --rm -p 8081:8080 srodi/rest-lottery:latest
```

### Requests example

If you are running locally and the app is exposed on port `8081` (for example using `Docker` image as described above), you can test using `cURL` and `jq` for response processing from the terminal.

```bash
# create ticket
curl -X POST -H "Content-Type: application/json" \
    -d '{"numberOfLines": 3}' \
    http://localhost:8081/ticket -sS

# get tickets
curl http://localhost:8081/ticket -sS > response

# manipulate the response to extract id of first
id="$(cat response | jq '.[0].id')"
id=$(sed -e 's/^"//' -e 's/"$//' <<< "$id")
url=http://localhost:8081/ticket/"$id"

# get ticket by id
curl $url -sS

# add lines to ticket
curl -X PUT -H "Content-Type: application/json" \
    -d '{"numberOfLines": 3}' \
    $url -sS

# check ticket status
url=http://localhost:8081/status/"$id"
curl -X PUT $url -sS
```

