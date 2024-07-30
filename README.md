# 사용자 정의 단어장 서비스
사용자 정의 단어장 서비스는 사용자가 단어를 검색하고 자신의 생각을 저장할 수 있는 시스템입니다. 이 서비스는 학습 및 정보 공유를 목적으로 하며, 사용자 간의 정보 교환이 효율적으로 이루어질 수 있도록 설계되었습니다.

# 프로젝트 설계

**1. 단어 검색**
- **기능 설명**: 검색어를 입력하면 Wordnik API를 사용하여 해당 단어를 검색할 수 있습니다. 검색은 대소문자를 구분하지 않습니다.
- **기술**: 단어 검색은 ElasticSearch를 활용하여 빠르게 처리하고, trie 데이터 구조를 활용합니다.
- **사용할 API:**
  - GET /word.json/{word}/definitions: 단어의 정의를 반환합니다.
  - GET /word.json/{word}/examples: 단어의 사용 예를 반환합니다.
  - GET /word.json/{word}/relatedWords: 단어와 관련된 단어들을 반환합니다.
  - GET /word.json/{word}/pronunciations: 단어의 발음 정보를 반환합니다.
  - GET /word.json/{word}/etymologies: 단어의 어원 정보를 반환합니다.

**2. 회원가입 및 로그인**
- JWT 방식을 통해 회원가입 및 로그인, 탈퇴 기능, 정보 수정을 구현합니다.
- 사용자는 아이디와 비밀번호를 입력하여 로그인할 수 있으며, 성공 시 JWT 토큰이 발급됩니다.
- 발급된 토큰은 이후 요청에서 사용자의 신원을 확인하는 데 사용됩니다.

**3. 자신의 생각 저장**
- 단어를 선택하고 자신의 생각을 작성하여 저장할 수 있습니다.

**4. 단어장 관리**
- 각 사용자는 자신의 단어장 단어를 저장할 수 있습니다.
- 단어장은 최대 100단어까지 저장 가능하며, 삭제할 수 있습니다.
- 저장된 단어는 생성된 순서대로 나열됩니다.
     
**5. 사용자 리스트 보기**
- 회원가입 한 사용자 리스트를 조회할 수 있습니다.
     
**6. 사용자 단어 목록 조회**
- 사용자 이름을 입력하면 해당 사용자가 저장한 단어 목록과 각 단어에 대해 메모한 내용 조회할 수 있습니다.

**7. 방문자 수 카운트**
   - 다른 사용자가 특정 사용자의 이름을 검색하면 해당 사용자의 방문자 수가 증가합니다.
   - 일일 방문자 수는 Redis에 저장됩니다.

**방문자 수 저장 구조**
- **Redis**
  - 스케쥴러로 매일 자정에 동기화 작업을 실행하여 데이터 일관성을 유지합니다.
- **MariaDB**
  - 방문자 수의 일별 기록과 총 합계를 저장합니다.
- **방문자 수 동기화 및 초기화**
  - 매일 자정에 Redis에 저장된 방문자 수를 MariaDB에 동기화합니다.
  - 동기화 작업 후 Redis 키는 자동으로 삭제되므로, 방문자 수는 초기화됩니다.

# ERD
![image](https://github.com/user-attachments/assets/f201a69f-87da-4919-baf1-45fae76c7189)


# 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)<img src="https://img.shields.io/badge/Wordnik-0066FF?style=for-the-badge&logo=wordnik&logoColor=white">
