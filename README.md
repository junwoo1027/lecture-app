# 강연 신청 플랫폼 시스템
강연 신청 플랫폼의 설계 및 API 개발

---

## 📚 사용 스택
- Spring Boot 3.2.2
- Java (JDK17)
- Gradle
- Junit5
- H2 Database
- JPA
- QueryDSL
- Redis (Redisson)
- Mockit

--- 

## 📁 Project Structure
멀티모듈 구조를 사용했습니다.
```bash
├── core  
│       └── core-api  
│           └── controller
│               └── request # request 객체
│               └── response # response 객체
│           └── support
│               └── error # core-api 에러 코드, 에러 타입 등
│               └── response # response 타입 정의
│       └── core-domain
│           └── <도메인> # 각 도메인 ex : lecture
│               └── domain # 각 도메인 객체 ex : lecture
│           └── error # core 에러 코드, 에러 타입 등
├── storage  
│       └── db-core     
│           └── <Entity> # 각 Entity ex : lecture, lectureRegs
└── support
        └── redisson # redis
            └── aop # aop
```

<details>
<summary>테이블</summary>
<div markdown="1">

#### 1. Lecture (강연)
| 컬럼명         | 데이터타입         | 설명          
|-------------|---------------|-------------|
| id          | pk            | primary key |
| lecturer    | String        | 강연자         |
| hall        | String        | 강연장         |
| seats       | int           | 신청 인원       |
| startAt     | LocalDateTime | 강연 시간       |
| description | String        | 강연 내용       |
| createdAt   | LocalDateTime | 생성일시        |
| updatedAt   | LocalDateTime | 최종 수정일시     |

#### 2. LectureRegs (강연 신청)
- 향후 운영 편의성을 위해 현재 프로젝트에는 lectureId에 대한 연관관계를 적용하지 않았습니다.

| 컬럼명            | 데이터타입         | 설명          
|----------------|---------------|-------------|
| id             | pk            | primary key |
| employeeNumber | int           | 사번          |
| lectureId      | Long          | 강연 id       |
| createdAt      | LocalDateTime | 생성일시        |
| updatedAt      | LocalDateTime | 최종 수정일시     |

</div>
</details>

<details>
<summary>API</summary>
<div markdown="1">

#### 1. 강연 등록
##### 정보
- 강연자, 강연장, 신청 인원, 강연 시간, 강연 내용을 정보를 받아서 강연을 등록한다.
##### 요청
```json
POST /api/v1/lectures
{
  "lecturer" : "김준우",
  "hall" : "1강연장",
  "seats" : 10,
  "startAt" : "2024-03-30T23:31:00",
  "description" : "스프링 강연"
}
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": {
    "id": 1
  },
  "error": null
}
```

<br>

#### 2. 전체 강연 목록 조회
##### 정보
- 전체 강연 목록을 조회한다.
##### 요청
```json
GET /api/v1/lectures
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "lecturer" : "웹툰",
      "hall" : "1강연장",
      "seats" : 100,
      "startAt" : "2024-03-29T10:30:00",
      "description" : "웹툰 강연"
    },
    {
      "lecturer": "토비",
      "hall": "2강연장",
      "seats": 100,
      "startAt": "2024-03-30T10:30:00",
      "description": "스프링 강연"
    },
    {
      "lecturer": "김영한",
      "hall": "3강연장",
      "seats": 100,
      "startAt": "2024-03-31T10:30:00",
      "description": "JPA 강연"
    }
  ],
  "error": null
}
```

<br>

#### 3. 강연 신청
##### 정보
- 강연 id, 사번(5자리)을 정보를 받아서 강연 강연을 신청한다.
- 같은 강연 중복 신청은 불가능하다.
##### 요청
```json
POST /api/v1/lectures/{lectureId}/apply
{
  "employeeNumber" : 12345
}
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": {
    "id": 1
  },
  "error": null
}
```

<br>

#### 4. 강연 신청 취소
##### 정보
- 강연 id, 사번(5자리)을 정보를 받아서 신청한 강연을 취소한다.
##### 요청
```json
POST /api/v1/lectures/{lectureId}/cancel
{
  "employeeNumber" : 12345
}
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": null,
  "error": null
}
```

<br>

#### 5. 강연 신청자 목록 조회
##### 정보
- 강연 id 정보를 받아서 해당 강연에 신청한 사번 목록을 조회한다.
##### 요청
```json
GET /api/v1/lectures/{lectureId}/employee
{
  "employeeNumber" : 12345
}
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "employeeNumber": 11111
    },
    {
      "employeeNumber": 22222
    }
  ],
  "error": null
}
```

<br>

#### 6. 신청한 강연 목록 조회
##### 정보
- 사번 정보를 받아서 신청한 강연 목록을 조회한다.
##### 요청
```json
GET /api/v1/employee/{employeeNumber}/lectures
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "lecturer" : "웹툰",
      "hall" : "1강연장",
      "seats" : 100,
      "startAt" : "2024-03-29T10:30:00",
      "description" : "웹툰 강연"
    },
    {
      "lecturer": "토비",
      "hall": "2강연장",
      "seats": 100,
      "startAt": "2024-03-30T10:30:00",
      "description": "스프링 강연"
    },
    {
      "lecturer": "김영한",
      "hall": "3강연장",
      "seats": 100,
      "startAt": "2024-03-31T10:30:00",
      "description": "JPA 강연"
    }
  ],
  "error": null
}
```

<br>

#### 7. 실시간 인기 강연 목록 조회
##### 정보
- 최근 3일간 가장 신청이 많은 순으로 조회한다.
##### 요청
```json
GET /api/v1/lectures/recent-popular
```

##### 응답
```json
{
  "result": "SUCCESS",
  "data": [
    {
      "lecturer" : "웹툰",
      "hall" : "1강연장",
      "seats" : 100,
      "startAt" : "2024-03-29T10:30:00",
      "description" : "웹툰 강연"
    },
    {
      "lecturer": "토비",
      "hall": "2강연장",
      "seats": 100,
      "startAt": "2024-03-30T10:30:00",
      "description": "스프링 강연"
    },
    {
      "lecturer": "김영한",
      "hall": "3강연장",
      "seats": 100,
      "startAt": "2024-03-31T10:30:00",
      "description": "JPA 강연"
    }
  ],
  "error": null
}
```

<br>

</div>
</details>

<details>
<summary>예외처리</summary>
<div markdown="1">

##### 예외 응답 형식

```json
{
  "result": "ERROR",
  "data": null,
  "error": {
    "code": "E1000",
    "message": "Not found data.",
    "data": null
  }
}
```

##### 예외 코드
| Type                    | Code         | Message                           |
|-------------------------|--------------|-----------------------------------|
| INTERNAL_SERVER_ERROR   | E500         | An unexpected error has occurred. |
| NOT_FOUND_DATA          | E1000  | Not found data.                   |
| LECTURE_EXCEEDED          | E1001   | Lecture has been exceeded.        |
| ALREADY_APPLIED_LECTURE       | E1002   | Already applied for a lecture.    |
| NOT_VALID_EMPLOYEE_NUMBER | E1003 | Not valid employee number.        |

</div>
</details>


### 동시성 이슈
1. 이슈
- 강연 신청이 동시에 들어오는 경우 동시성 이슈가 발생해 신청 인원보다 더 많은 신청이 되는 문제가 있습니다.

<br>

2. 해결방안
- 해당 이슈를 해결하기 위해 분산락을 고민하게 되었고 락을 사용하기 위해 별도의 커넥션 풀을 관리해야 하고 락에 관련된 부하를 RDS에서 받는 Named Lock보다는 
Redis의 Redission을 사용해 분산락을 적용했습니다.
- 분산락을 쉽게 사용하기 위해 어노테이션 기반으로 AOP로 구성해 ```@RedissonLock```을 구현했습니다.
- 해당 기능을 테스트하기 위헤 ```LectureServiceConCurrentcyTest```로 통합테스트를 진행했습니다.
