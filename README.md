# 사용자 정의 단어장 서비스
사용자 정의 단어장 서비스는 사용자가 단어를 검색하고 자신의 생각을 저장하며, 잘못된 정보를 관리자가 수정할 수 있는 시스템입니다. 이 서비스는 학습 및 정보 공유를 목적으로 하며, 사용자 간의 정보 교환이 효율적으로 이루어질 수 있도록 설계되었습니다.

# 프로젝트 설계
### 공통 기능 (매니저, 사용자, 로그인하지 않은 사용자 모두 사용 가능)
**1. 단어 검색**
- 검색어를 입력하면 해당 단어를 검색할 수 있습니다.
- 단어 검색은 대소문자를 구분하지 않습니다.
- 로그인하지 않더라도 검색은 가능합니다.
- 단어 검색 결과는 ElasticSearch와 Redis를 활용하여 캐싱합니다.
### 사용자 기능
**2. 회원가입 및 로그인**
- JWT 방식을 통해 회원가입 및 로그인, 탈퇴 기능, 정보 수정을 구현합니다.
- 사용자는 아이디와 비밀번호를 입력하여 로그인할 수 있으며, 성공 시 JWT 토큰이 발급됩니다.
- 발급된 토큰은 이후 요청에서 사용자의 신원을 확인하는 데 사용됩니다.

**3. 자신의 생각 저장**
- 단어를 선택하고 자신의 생각을 작성하여 저장할 수 있습니다.
- 다음에 해당 단어를 검색하면 저장한 내용이 함께 나타납니다.

**4. 단어장 관리**
- 각 사용자는 자신의 단어장 목록에 단어를 저장할 수 있습니다.
- 단어장은 최대 100단어까지 저장 가능하며, 삭제할 수 있습니다.
- 자주 조회하는 단어장 목록을 Redis에 캐싱하여 빠른 응답 속도를 제공합니다.
- 저장된 단어는 생성된 순서대로 나열됩니다.
     
**5. 사용자 리스트 보기**
- 회원가입 한 사용자 리스트를 조회할 수 있습니다.
     
**6. 사용자 단어 목록 조회**
- 사용자 이름을 입력하면 해당 사용자가 저장한 단어 목록과 각 단어에 대해 메모한 내용 조회할 수 있습니다.

**7. 방문자 수 카운트**
   - 다른 사용자가 특정 사용자의 이름을 검색하면 해당 사용자의 방문자 수가 증가합니다.
   - 방문자 수는 Redis에 날짜별로 저장됩니다.
   - 예: `visitor_count:2023-07-25:사용자 닉네임`

 방문자 수 저장 구조
- **Redis**
  - 키 형식: `visitor_count:YYYY-MM-DD:userID`
  - 값: 방문자 수
  - TTL 설정: 24시간
- **MariaDB**
  - 방문자 수의 일별 기록과 총 합계를 저장합니다.
- **방문자 수 동기화 및 초기화**
  - 매일 자정에 Redis에 저장된 방문자 수를 MariaDB에 동기화합니다.
  - 동기화 작업 후 Redis 키는 자동으로 삭제되므로, 방문자 수는 초기화됩니다.

  **8. 메시지 답변**
  - 사용자와 메세지를 주고 받음으로서 소통을 할 수 있습니다.


### 매니저 기능
**메시지 답변**
  - 사용자와 메세지를 주고 받음으로서 소통을 할 수 있습니다.

**단어 내용 수정**
- 검색된 단어의 내용과 사용자의 단어장을 수정할 수 있습니다.

**사용자 탈퇴**
- 사용자를 탈퇴시킬 수 있습니다.

# ERD

# 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)
