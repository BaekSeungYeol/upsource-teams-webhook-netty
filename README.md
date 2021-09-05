# Teams Webhook Proxy Server

<img src="https://user-images.githubusercontent.com/47316511/132132092-a1ad19a8-1ce0-45ee-a5b7-d821f793e548.png" width="70%" height="70%"/>

팀즈로 노티가 필요한 경우에 사용한다.


## 1. Upsource Usage

업소스에서 팀즈로 노티가 필요한 경우

1. 업소스를 설치한다.


2. Webhook 설정을 추가한다.

```
curl --location --request POST 'https://your.domain.com/~rpc/setProjectWebhooks' \
--header 'Content-Type: application/json' \
--header 'Authorization: Basic your_token=' \
--data-raw '{
    "projectId" : "yourProjectId",
    "triggers" : [
        {
            "events" : 16,
            "urls" : "https://your.domain.com/webhook/teams/label"
        },![Uploading upsource-teams.jpg…]()

        {
            "events" : 6,
            "urls" : "https://your.domain.com/webhook/teams/revision"
        },
        {
            "events" : 3,
            "urls" : "https://your.domain.com/webhook/teams/accept"
        }
    ]
}'
```




#### 1.1 teams -> cloud native -> upsource 채널에 리뷰 생성 Notification 추가

Teams에 리뷰 생성 메세지를 보낸다.

- URL: POST /webhook/teams/label

- Request Body
``` json
{
  "majorVersion": 2020,
  "minorVersion": 1,
  "projectId": "your-project",
  "dataType": "ReviewLabelChangedEventBean",
  "data": {
    "reviewId": "CB-CR-36",
    "labelId": "ready",
    "labelName": "ready for review",
    "wasAdded": true,
    "actor": {
      "userId": "51a6d3ff-ab69-4953-90bc-5a29df222745",
      "userName": "admin"
    }
  }
}

```

- Response Body

```json
{
  "success": true,
  "response": "1",
  "error": null
}
```






#### 1.2 teams -> cloud native -> upsource 채널에 커밋 추가 Notification 

Teams에 커밋 추가 메세지를 보낸다.

- URL: POST /webhook/teams/revision

- Request Body
``` json
{
  "majorVersion": 2020,
  "minorVersion": 1,
  "projectId": "your-project",
  "dataType": "NewRevisionEventBean",
  "data": {
    "revisionId": "de3dfaba9225a79a5d31026dc9c4845c0c9ef91a",
    "branches": [
      "PR 19",
      "feature/DOE-241"
    ],
    "author": "백승열",
    "message": "[TEST 2] Update DspServerGCClient.java",
    "date": 1626537436000
  }
}

```

- Response Body

```json
{
  "success": true,
  "response": "1",
  "error": null
}
```


#### 1.3 teams -> cloud native -> upsource 채널에 Accept Notification 추가

Teams에 Accept 메세지를 보낸다.

- URL: POST /webhook/teams/accept

- Request Body
``` json
{
  "majorVersion": 2020,
  "minorVersion": 1,
  "projectId": "your-project",
  "dataType": "ParticipantStateChangedFeedEventBean",
  "data": {
    "base": {
      "userIds": [
        {
          "userId": "9e3bc279-2221-488a-8e68-6b5dfff1face",
          "userName": "백승열",
          "userEmail": "seungyeol.baek@seungyeol.com"
        },
        {
          "userId": "51a6d3ff-ab69-4953-90bc-5a29df222745",
          "userName": "admin"
        }
      ],
      "reviewNumber": 60,
      "reviewId": "CB-CR-60",
      "date": 1626587313108,
      "actor": {
        "userId": "51a6d3ff-ab69-4953-90bc-5a29df222745",
        "userName": "admin"
      },
      "feedEventId": "1626587313108#ccsp20-behavioralpatternanalysis#61ac5725-118d-4d00-baf4-0faf2d079ce8"
    },
    "participant": {
      "userId": "51a6d3ff-ab69-4953-90bc-5a29df222745",
      "userName": "admin"
    },
    "oldState": 1,
    "newState": 2
  }
}

```

- Response Body
```json
{
  "success": true,
  "response": "1",
  "error": null
}
```


## Version

|  Client  |  Upsource   |
|----------|:-----------:|
|  0.0.1   | 2020.1.1883 |


## References

* [Reactor](https://projectreactor.io/docs/core/release/reference/)
