package com.fam.BIZ;

import static common.JDBCTemplate.Close;
import static common.JDBCTemplate.getConnection;

import java.sql.Connection;
import java.util.List;

import com.fam.DAO.TaskDAO;
import com.fam.VO.Task;

public class TaskBIZ {

	public List<Task> task_selectAll() {

		Connection conn = getConnection();
		List<Task> all = new TaskDAO(conn).task_selectAll();
		Close(conn);

		return all;
	}

	public int getInsert(Task vo) {
		System.out.println("¿©±â biz");
		Connection conn = getConnection();

		int r = new TaskDAO(conn).getInsertVO(vo);
		// close
		Close(conn);
		return r;
	}

	public void getDelete(int delTaskNo) {
		Connection conn = getConnection();

		new TaskDAO(conn).getDelete(delTaskNo);
		// close
		Close(conn);
	}

	public int task_update(Task vo) {
		int res = 0;
		Connection conn = getConnection();

		res = new TaskDAO(conn).task_update(vo);
		// close
		Close(conn);
		return res;
	}

	public int task_updateLike(int likeNo, int taskNo) {
		int res = 0;
		Connection conn = getConnection();
		res = new TaskDAO(conn).task_updateLike(likeNo, taskNo);
		Close(conn);
		return res;
	}
}
