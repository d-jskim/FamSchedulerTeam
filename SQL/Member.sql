--MemberId를 위한 Sequence 생성
CREATE SEQUENCE member_seq
 START WITH     1
 INCREMENT BY   1
 NOMAXVALUE
 NOCACHE
 NOCYCLE;
 
 --Member 테이블 삭제하기 
 DROP TABLE member;
 
 --Member 테이블 생성하기
 CREATE TABLE member(
 	memberNo number PRIMARY KEY,
 	memberId varchar2(20) NOT NULL,
 	password varchar2(20) NOT NULL,
 	familyId number, --family_seq
 	nickname varchar2(20) NOT NULL,
 	indate date DEFAULT sysdate
 );
 
--familyId, indate 제외한 컬럼 값 insert
INSERT INTO member(memberNo, memberId, password, nickname) VALUES 
(member_seq.nextVal,'abc@gmail.com','abc', '무지');

--familyId(테스트용) 
--TODO: [Family] 테이블 생성 시 table_seq.nextVal을 알려주고 user가 방 생성 OK 클릭하면 family데이터 삽입
INSERT INTO member(memberNo, memberId, password, familyId, nickname) VALUES 
(member_seq.nextVal,'aaa@gmail.com','aaa', 1, '라이언');

INSERT INTO member(memberNo, memberId, password, familyId, nickname) VALUES 
(member_seq.nextVal,'bbb@gmail.com','bbb', 1, '어피치');




--회원가입
CREATE OR REPLACE PROCEDURE member_insert(
	v_memberId IN member.memberId%TYPE,
	v_password IN member.password%TYPE,
	v_familyId IN member.familyId%TYPE,
	v_nickname IN member.nickname%TYPE)
IS
BEGIN
INSERT INTO member(memberNo, memberId, password, nickname) 
VALUES(member_seq.nextVal, v_memberId, v_password, v_nickname);
END;
/

--로그인 확인
CREATE OR REPLACE FUNCTION MEMBER_LOGIN(VID IN MEMBER.MEMBERID%TYPE,
VPW IN MEMBER.PASSWORD%TYPE) RETURN NUMBER
IS
DBPW MEMBER.PASSWORD%TYPE := NULL;
EXISTS_NUM NUMBER := 0;

BEGIN
 
SELECT CASE 
WHEN EXISTS (SELECT MEMBERID FROM MEMBER WHERE MEMBER.MEMBERID = VID)  THEN 1
ELSE 0 
END 
INTO EXISTS_NUM FROM DUAL;

IF EXISTS_NUM = 1 THEN  
SELECT PASSWORD INTO DBPW
FROM MEMBER
WHERE MEMBERID = VID;
IF DBPW = VPW THEN
RETURN 1;
ELSE
RETURN 0;
END IF;
ELSIF EXISTS_NUM = 0 THEN
RETURN 0;
END IF;
END;
/

-- 로그인 성공 시 해당 회원의 모든 정보 다 받아오기 (nickname 필요)

CREATE OR REPLACE PROCEDURE member_selectVO(v_memberId IN member.memberId%TYPE, res out SYS_REFCURSOR)
IS
BEGIN
	OPEN res FOR
			SELECT memberId, familyId, nickname FROM member WHERE memberId = v_memberId;
END;
/

SQL> var res refcursor;
SQL> exec member_selectVO('aaa@gmail.com', :res)

