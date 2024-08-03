# 사용자 정의 단어장 서비스
사용자 정의 단어장 서비스는 단어 학습을 위한 사용자 정의 단어장 서비스로, 사용자가 단어의 정의, 어원, 예시, 관련 단어 등을 검색하고, 자신의 생각과 메모를 저장하여 학습에 활용할 수 있도록 합니다.

# 프로젝트 설계

**1. 단어 검색**
- **기능 설명**: 검색어를 입력하면 Wordnik API를 사용하여 해당 단어를 검색할 수 있습니다. 검색은 대소문자를 구분하지 않습니다.
- **기술**:
  - 단어 검색은 ElasticSearch를 활용하여 빠르게 처리하고, trie 데이터 구조를 활용합니다.
  - 사용자가 단어를 검색하면 ElasticSearch를 통해 빠르게 검색 결과를 제공하고, 필요한 경우 Wordnik API를 호출하여 추가 데이터를 가져옵니다.
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
- 자신의 생각에 대한 공개여부를 결정할 수 있습니다.
- user_think 테이블은 사용자별 생각 저장합니다.
  
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
  - 오늘의 방문자 수를 today_count로 저장하고, 실시간으로 업데이트합니다.
  - 스케쥴러로 매일 자정에 동기화 작업을 실행하여 데이터 일관성을 유지합니다.
- **MariaDB**
  - 방문자 수의 총(total_count) 합계를 저장합니다.
- **방문자 수 동기화 및 초기화**
  - 매일 자정에 Redis에 저장된 방문자 수를 MariaDB에 동기화합니다.
  - 동기화 작업 후 Redis 키는 자동으로 삭제되므로, 방문자 수는 초기화됩니다.

# ERD
![image](https://github.com/user-attachments/assets/94a3a61b-06f4-496b-8454-cdc33b9f5e00)




# ElasticSearch 문서구조

- **definitions**
```json
{
  "api_word_id": "string",
  "word": "string",
  "part_of_speech": "string",
  "attribution_text": "string",
  "source_dictionary": "string",
  "sequence": "integer",
  "score": "integer",
  "attribution_url": "string",
  "wordnik_url": "string"
}
```
- **examples**
```json
{
  "api_word_id": "string",
  "provider_id": "integer",
  "year": "integer",
  "rating": "integer",
  "url": "string",
  "text": "string",
  "document_id": "integer",
  "title": "string",
  "author": "string"
}
```
- **related_words**
```json
{
  "api_word_id": "string",
  "relationship_type": "string",
  "related_word": "string"
}
```
- **pronunciations**
```json
{
  "api_word_id": "string",
  "seq": "integer",
  "raw": "string",
  "raw_type": "string",
  "attribution_text": "string",
  "attribution_url": "string"
}
```
- **etymologies**
```json
{
  "api_word_id": "string",
  "etymology_text": "string",
  "use_canonical": "string"
}
```
# 기술 스택
<img src="https://img.shields.io/badge/java-007396?style=for-the-badge&logo=java&logoColor=white"><img src="https://img.shields.io/badge/spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white"><img src="https://img.shields.io/badge/mariaDB-003545?style=for-the-badge&logo=mariaDB&logoColor=white">![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![AWS](https://img.shields.io/badge/AWS-%23FF9900.svg?style=for-the-badge&logo=amazon-aws&logoColor=white)![ElasticSearch](https://img.shields.io/badge/-ElasticSearch-005571?style=for-the-badge&logo=elasticsearch)<img src="https://img.shields.io/badge/Wordnik-0066FF?style=for-the-badge&logo=wordnik&logoColor=white">
