--TaskNo를 위한 Sequence 생성하기
CREATE SEQUENCE task_seq
increment by 1
start with 1;

--Task 테이블 삭제하기
DROP TABLE task;

--Task 테이블 생성하기
DROP TABLE TASK;
CREATE TABLE TASK(
	taskNo number primary key,
	task varchar2(50) Not Null,
	category varchar2(30),
	status varchar2(30),
	memberId varchar2(20) Not Null,
	substitute varchar2(20),
	startDate date default sysdate,
	endDate date default sysdate,
	memo varchar2(100),
	LikeNo number
);

--TASK 테이블 구조 
--DESC task; //DESC명령문은 sqlplus에서만 실행 가능함
SQL> desc task;
/*
 Name                                      Null?    Type
 ----------------------------------------- -------- ----------------------------
 TASKNO                                    NOT NULL NUMBER                     	-> int
 TASK                                      NOT NULL VARCHAR2(50)				-> String
 CATEGORY                                           VARCHAR2(30)				-> String
 STATUS                                             VARCHAR2(30)				-> String
 MEMBERID                                  NOT NULL VARCHAR2(20)				-> String
 SUBSTITUTE                                         VARCHAR2(20)				-> String
 STARTDATE                                          DATE						-> String
 ENDDATE                                            DATE						-> String
 MEMO                                               VARCHAR2(100)				-> String
 LIKENO                                             NUMBER						-> int
*/

--/////////////PROCEDURE[TO DO LIST(CRUD)]////////////////////
--Task 데이터 삽입
String task_insert = "INSERT INTO task VALUES(?, ?, ?)";
CREATE OR REPLACE PROCEDURE task_insert(my_task in task.task%type, my_category in task.category%type, 
										my_status in task.status%type, my_member in task.memberId%type)
IS
BEGIN
	INSERT INTO Task(taskNo, task, category, status, memberId) 
				VALUES(task_seq.nextval, my_task, my_category, my_status, my_member);
END;
/

--확인
SQL> exec task_insert('빨래 널기', '청소하기', '어피치')
exec task_insert('거실 바닥 닦기', '청소하기', '무지')
SELECT * FROM task;

--Task 삭제 
CREATE OR REPLACE PROCEDURE task_delete(my_taskno in task.taskNo%type)
IS
BEGIN
	DELETE FROM task WHERE taskNo = my_taskno;
END;
/

--SELECT 모든 데이터 -> 테이블뷰에 표시 
CREATE OR REPLACE PROCEDURE task_selectAll (myres out SYS_REFCURSOR)
IS
BEGIN
	OPEN myres FOR SELECT taskNo, task, category, status, memberId, substitute, startDate, endDate, memo, likeNo FROM task;
END;
/

-- 데이터 타입 확인 from Oracle: 2020-06-09 10:55:05.0 -- 
select sysdate from task;

--Task 수정
CREATE OR REPLACE PROCEDURE task_update(v_task in task.task%type, 
										v_category in task.category%type,
										v_status in task.status%type,
										v_memberId in task.memberId%type,
										v_substitute in task.substitute%type,
										v_startDate in task.startDate%type,
										v_endDate in task.endDate%type,
										v_memo in task.memo%type,
										v_likeNo in task.likeNo%type,
										v_taskNo in task.taskNo%type
										)
IS
BEGIN
	UPDATE task SET task = v_task, category = v_category, status = v_status, memberId = v_memberId,
					substitute = v_substitute, startDate = v_startDate, endDate = v_endDate,
					memo = v_memo, likeNo = v_likeNo
	WHERE taskNo = v_taskNo;
END;
/

--Like수 증가

CREATE OR REPLACE PROCEDURE task_updateLike(v_likeNo in task.likeNo%type, v_taskNo in task.taskNo%type)
IS
BEGIN
	UPDATE task SET likeNo = v_likeNo
	WHERE taskNo = v_taskNo;
END;
/





