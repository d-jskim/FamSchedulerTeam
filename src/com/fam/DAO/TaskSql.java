package com.fam.DAO;

public interface TaskSql {
	
	/*SELECT ALL*/
	String task_selectAll = "{call task_selectAll(?)}";
	/*INSERT*/
	String task_insert = "{call task_insert(?, ?, ?, ?)}";
	/*DELETE*/
	String task_delete = "{call task_delete(?)}";
	/*UPDATE*/
	String task_update = "{call task_update(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
	//"UPDATE task SET task = ?, category = ?.......... WHERE taskNo = ?"; 마지막 ?는 taskNo
	String task_updateLike = "{call task_updateLike(?, ?)}"; //WHERE taskNo = ?"; 마지막 ?는 taskNo	
}
