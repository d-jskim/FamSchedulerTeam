--TaskNo�� ���� Sequence �����ϱ�
CREATE SEQUENCE task_seq
increment by 1
start with 1;

--Task ���̺� �����ϱ�
DROP TABLE task;

--Task ���̺� �����ϱ�
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

--TASK ���̺� ���� 
--DESC task; //DESC��ɹ��� sqlplus������ ���� ������
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
--Task ������ ����
String task_insert = "INSERT INTO task VALUES(?, ?, ?)";
CREATE OR REPLACE PROCEDURE task_insert(my_task in task.task%type, my_category in task.category%type, 
										my_status in task.status%type, my_member in task.memberId%type)
IS
BEGIN
	INSERT INTO Task(taskNo, task, category, status, memberId) 
				VALUES(task_seq.nextval, my_task, my_category, my_status, my_member);
END;
/

--Ȯ��
SQL> exec task_insert('���� �α�', 'û���ϱ�', '����ġ')
exec task_insert('�Ž� �ٴ� �۱�', 'û���ϱ�', '����')
SELECT * FROM task;

--Task ���� 
CREATE OR REPLACE PROCEDURE task_delete(my_taskno in task.taskNo%type)
IS
BEGIN
	DELETE FROM task WHERE taskNo = my_taskno;
END;
/

--SELECT ��� ������ -> ���̺�信 ǥ�� 
CREATE OR REPLACE PROCEDURE task_selectAll (myres out SYS_REFCURSOR)
IS
BEGIN
	OPEN myres FOR SELECT taskNo, task, category, status, memberId, substitute, startDate, endDate, memo, likeNo FROM task;
END;
/

-- ������ Ÿ�� Ȯ�� from Oracle: 2020-06-09 10:55:05.0 -- 
select sysdate from task;

--Task ����
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

--Like�� ����

CREATE OR REPLACE PROCEDURE task_updateLike(v_likeNo in task.likeNo%type, v_taskNo in task.taskNo%type)
IS
BEGIN
	UPDATE task SET likeNo = v_likeNo
	WHERE taskNo = v_taskNo;
END;
/





