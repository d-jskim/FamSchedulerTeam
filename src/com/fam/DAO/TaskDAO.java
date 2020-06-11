package com.fam.DAO;

import static common.JDBCTemplate.Close;
import static common.JDBCTemplate.Commit;
import static common.JDBCTemplate.Rollback;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.fam.VO.Task;

import oracle.jdbc.OracleCallableStatement;
import oracle.jdbc.OracleTypes;

public class TaskDAO implements TaskSql {

	private Connection conn;

	public TaskDAO(Connection conn) {
		this.conn = conn;
	}

	// Select * FROM emp 완료
	public List<Task> task_selectAll() {
		// select결과를 한 줄씩 vo에 담아서 List객체에 추가한 후 리턴
		Task vo = null;
		List<Task> all = new ArrayList<>();

		// 명령을 수행하는 객체를 생성 CallableStatement는 Procedure를 호출한다.
		CallableStatement cstmt = null;
		// 결과 select 실행 후 데이터를 리턴받을 객체
		ResultSet rs = null;

		try {
			cstmt = conn.prepareCall(task_selectAll);
			System.out.println("here 1");
			cstmt.registerOutParameter(1, OracleTypes.CURSOR);
			System.out.println("here 2");
			cstmt.execute();

			rs = ((OracleCallableStatement) cstmt).getCursor(1); // 커서가 갖고 있는 것을 리턴받음
			System.out.println("here 3");
			while (rs.next()) {
				// 결과를 한 줄씩 읽어서 VO에 담는다.
				System.out.println("here4");
				System.out.println(rs.getDate(7)); // 2020-06-09
				// taskNo(1), task(2), category(3), status(4), memberId(5), substitute(6),
				// startDate(7), endDate(8), memo(9), likeNo(10)
				vo = new Task(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5),
						rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9), rs.getInt(10));

				// List
				System.out.println("here 5");
				all.add(vo);
				System.out.println("here 6");
			}

		} catch (Exception e) {
			System.out.println("task_selectAll" + e);
		} finally {
			Close(cstmt);
			Close(rs);
		} // end catch
		return all;
	}// end method

	/// INSERT
	public int getInsertVO(Task vo) {

		int res = 0; // 결과값리턴하는 변수 초기화
		CallableStatement cstmt = null;

		try {
			cstmt = conn.prepareCall(task_insert); // "{call task_insert(?, ?, ?)}";
			// task(String), category(String), member(String)
			cstmt.setString(1, vo.getTask());
			cstmt.setString(2, vo.getCategory());
			cstmt.setString(3, vo.getStatus());
			cstmt.setString(4, vo.getMember());

			// 쿼리를 실행한다.
			cstmt.execute();
			Commit(conn);

			res = 1;

		} catch (Exception e) {
			System.out.println(e);
			try {
				Rollback(conn);
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				Close(cstmt);
			}
		}
		return res;
	}

	public void getDelete(int taskNo) {
		CallableStatement cstmt = null;

		try {
			cstmt = conn.prepareCall(task_delete); // "{call task_delete(?)}";
			// ?: taskNo
			System.out.println("DAO.getDELETE()에 넘어 온 delTask ");
			System.out.println("taskNo는? " + taskNo);
			cstmt.setInt(1, taskNo);

			// 쿼리를 실행한다.
			cstmt.execute();
			Commit(conn);

		} catch (Exception e) {
			System.out.println(e);
			try {
				Rollback(conn);
			} catch (Exception e2) {
				e2.printStackTrace();
			} finally {
				Close(cstmt);
			}
		}

	}

	public int task_update(Task vo) {
		// String myemp_update = "UPDATE my_emp SET ename = ?, deptno = ? WHERE empno =
		// ?";
		int res = 0;
		CallableStatement cstmt = null;
		try {
			System.out.println("date 확인부터 해야징: " + vo.getStartDate().toString());
			// 쿼리
			cstmt = conn.prepareCall(task_update);
			// 파라미터? 값 대입
			cstmt.setString(1, vo.getTask()); // task
			cstmt.setString(2, vo.getCategory());
			cstmt.setString(3, vo.getStatus());
			cstmt.setString(4, vo.getMember());
			cstmt.setString(5, vo.getSubstitute());
			cstmt.setString(6, vo.getStartDate().toString());
			cstmt.setString(7, vo.getEndDate().toString());
			cstmt.setString(8, vo.getMemo());
			cstmt.setInt(9, vo.getLike());
			cstmt.setInt(10, vo.getTaskNo());

			// 쿼리 실행
			cstmt.execute();
			Commit(conn);
			res = 1;

		} catch (Exception e) {
			System.out.println(e);
			Rollback(conn);
		} // end outer try

		return res;
	}// end method

	public int task_updateLike(int likeNo, int taskNo) {
		int res = 0;
		CallableStatement cstmt = null;
		try {
			cstmt = conn.prepareCall(task_updateLike);
			cstmt.setInt(9, likeNo);
			cstmt.setInt(10, taskNo);

			// 쿼리 실행
			cstmt.execute();
			Commit(conn);
			res = 1;
		} catch (Exception e) {
			System.out.println(e);
			Rollback(conn);
		} // end outer try

		return res;
	}

}
