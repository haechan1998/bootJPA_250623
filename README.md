JPA 스프링 프로젝트 (25.06.23 ~ 25.07.03)
=========================================
### Security, toast ui 를 사용.

개발환경
--------
* Version : Java 17
* IDE : IntelliJ
* Framwork : SpringBoot 3.3.13
* ORM : JPA

**1. User(Login) security**
 - dependency
 - ----------
 - implementation 'org.thymeleaf.extras:thymeleaf-extras-springsecurity6'
 - implementation 'org.springframework.boot:spring-boot-starter-security'

**2. toast ui**
 - editor 및 viewer 적용
 - addimageblobhook 을 사용한 image 파일을 별도 폴더에 저장
 - editor 언어 변경
 - DB 에 HTML 형식으로 editor 내용 저장
 - 수정 시 HTML 형식 -> markdown 형식으로 변환하여 일관성 있는 내용 유지.


