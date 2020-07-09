# Travis, coveralls 연동

[![Build Status](https://travis-ci.org/KimSeongGyu1/atdd-subway-favorite.svg?branch=master)](https://travis-ci.org/KimSeongGyu1/atdd-subway-favorite)
[![Coverage Status](https://coveralls.io/repos/github/KimSeongGyu1/atdd-subway-favorite/badge.svg?branch=master)](https://coveralls.io/github/KimSeongGyu1/atdd-subway-favorite?branch=master)


# 1단계 - 회원관리 기능

## 요구 사항

- 회원 정보를 관리하는 기능 구현
- 자신의 정보만 수정 가능하도록 해야하며 **로그인이 선행**되어야 함
- 토큰의 유효성 검사와 본인 여부를 판단하는 로직 추가
- side case에 대한 예외처리
- 인수 테스트와 단위 테스트 작성
- API 문서를 작성하고 문서화를 위한 테스트 작성
- 페이지 연동

## 기능 목록

### 1. 회원 가입

- [x]  회원가입을 할 수 있다.
    - [x]  모두 입력되어야 한다.
    - [x]  이메일 형식에 맞아야 한다.
    - [x]  비밀번호와 비밀번호 확인이 같아야 한다.
    - [x]  기존 회원으로 등록된 이메일은 사용할 수 없다.

### 2. 로그인

- [x]  로그인을 할 수 있다.
    - [x]  모두 입력되어야 한다.
    - [x]  입력한 정보가 기존 회원 DB에 존재해야 한다.

### 3. 로그인 후 회원정보 조회/수정/삭제

- [x]  로그인이 되어 있음을 확인해야 한다.
    - [x]  확인하는 방법으로 토큰을 이용한다.
    - [x]  회원정보를 조회할 수 있다.
    - [x]  회원정보를 수정할 수 있다.
    - [x]  회원정보를 삭제할 수 있다.
    
# 2단계 - 즐겨찾기 기능

# 즐겨찾기 기능

## 요구사항

- 즐겨찾기 기능을 추가(추가,삭제,조회)
- 자신의 정보만 수정 가능하도록 해야하며 **로그인이 선행**되어야 함
- 토큰의 유효성 검사와 본인 여부를 판단하는 로직 추가(interceptor, argument resolver)
- side case에 대한 예외처리 필수
- 인수 테스트와 단위 테스트 작성
- API 문서를 작성하고 문서화를 위한 테스트 작성
- 페이지 연동

## 기능목록

### 1. 즐겨찾기 기능을 구현한다.

- [x]  즐겨찾기 추가가 가능하다.
- [x]  즐겨찾기 조회가 가능하다.
- [x]  즐겨찾기 삭제가 가능하다.
