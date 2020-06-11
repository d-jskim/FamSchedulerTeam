package com.fam.VO;


import java.sql.Date; //util.Date�� ����Ŭ�� ���� �Ұ�
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import com.fam.util.LocalDateAdapter;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;


public class Task {
	
	private IntegerProperty taskNo;
	private StringProperty category;
	private StringProperty task;
	private StringProperty status;
	private StringProperty member;
	private StringProperty substitute;
	private ObjectProperty<LocalDate> startDate;
	private ObjectProperty<LocalDate> endDate;
	private StringProperty memo;
	private IntegerProperty like;
	
	/**
	 * [NEW]��ư�� Ŭ������ �� �⺻�����ڷ� ������ Task��ü�� ���ο� �� �־�� �ϱ� ������ �ʿ��� �⺻������(args X & default value ����)
	 */
	public Task() {
		this.category = new SimpleStringProperty(""); 
		this.task = new SimpleStringProperty("");
		this.member = new SimpleStringProperty("");
	
		//default values
		this.taskNo = new SimpleIntegerProperty(0);
		this.status = new SimpleStringProperty("not started");
		this.substitute = new SimpleStringProperty("none");
		//TO DO: 2020.01.01 -> ���� ��¥�� �����ϱ�
		this.startDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(2020, 01, 01));
		this.endDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(2020, 01, 01));
		this.memo = new SimpleStringProperty("none");
		this.like = new SimpleIntegerProperty(0);

	}
	/**
	 * �ʼ� ����(category, task, member)�� �Է¹ް� ���� ����ϴ� �Ű����� ������
	 * @param category
	 * @param task
	 * @param member
	 */
	public Task(String category, String task, String member) {
		this.category = new SimpleStringProperty(category);
		this.task = new SimpleStringProperty(task);
		this.member = new SimpleStringProperty(member);
	
		//default values
		this.taskNo = new SimpleIntegerProperty(0);
		this.status = new SimpleStringProperty("not started");
		this.substitute = new SimpleStringProperty("none");
		this.startDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(2020, 01, 01));
		this.endDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(2020, 01, 01));
		this.memo = new SimpleStringProperty("none");
		this.like = new SimpleIntegerProperty(0);

	}
	
	
	/**
	 * SELECT ALL���� �� oracle cursor�� �޾� �� Ʃ�� �ϳ��� �Ű����� �����ڸ� ���� ������ �� all(java.util.List)�� add()��.
	 * DB�κ��� ���� ������ Ÿ���� Task(VO)���� ������ ������ ��ȯ�� �� ����.
	 * int -> IntegerProperty / String -> StringProperty / String(date[DB]) -> ObjectProperty<LocalDate>
	 * @param taskNo
	 * @param category
	 * @param task
	 * @param status
	 * @param memberId
	 * @param substitute
	 * @param sDate
	 * @param eDate
	 * @param memo
	 * @param like
	 */
	public Task(int taskNo, String category, String task, String status, String memberId, 
			String substitute, String sDate, String eDate, String memo, int like){
		
		this.taskNo = new SimpleIntegerProperty(taskNo);
		this.category = new SimpleStringProperty(category);
		this.task = new SimpleStringProperty(task);
		this.status = new SimpleStringProperty(status);
		this.member = new SimpleStringProperty(memberId);
		this.substitute = new SimpleStringProperty(substitute);	
		this.startDate =  new SimpleObjectProperty<LocalDate>(LocalDate.of(intDate(sDate)[0], intDate(sDate)[1], intDate(sDate)[2]));
		this.endDate = new SimpleObjectProperty<LocalDate>(LocalDate.of(intDate(eDate)[0], intDate(eDate)[1], intDate(eDate)[2]));
		this.memo = new SimpleStringProperty(memo);
		this.like = new SimpleIntegerProperty(like);
	}

	
	

	public int getTaskNo() {
		return taskNo.get();
	}

	public void setTaskNo(int taskNo) {
		this.taskNo.set(taskNo);
	}
	
	public IntegerProperty taskNoProperty() {
		return taskNo;
	}

	public String getCategory() {
		return category.get();	
	}

	public void setCategory(String category) {
		this.category.set(category);
	}
	
	public StringProperty categoryProperty() {
		return category;
	}

	public String getTask() {
		return task.get();
	}

	public void setTask(String task) {
		this.task.set(task);
	}
	
	public StringProperty taskProperty() {
		return task;
	}

	public String getStatus() {
		return status.get();
	}

	public void setStatus(String status) {
		this.status.set(status);
	}

	public StringProperty statusProperty() {
		return status;
	}
	
	public String getMember() {
		return member.get();
	}

	public void setMember(String member) {
		this.member.set(member);
	}
	
	public StringProperty memberProperty() {
		return member;
	}
	
	public String getSubstitute() {
		return substitute.get();
	}

	public void setSubstitute(String substitute) {
		this.substitute.set(substitute);
	}
	
	public StringProperty substituteProperty() {
		return substitute;
	}
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getStartDate() {
		return startDate.get();
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate.set(startDate);
	}
	
	public ObjectProperty<LocalDate> startDateProperty() {
		return startDate;
	}
	
	@XmlJavaTypeAdapter(LocalDateAdapter.class)
	public LocalDate getEndDate() {
		return endDate.get();
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate.set(endDate);
	}
	
	public ObjectProperty<LocalDate> endDateProperty() {
		return endDate;
	}

	public String getMemo() {
		return memo.get();
	}

	public void setMemo(String memo) {
		this.memo.set(memo);
	}	
	
	public StringProperty memoProperty() {
		return memo;
	}
	
	public int getLike() {
		return like.get();
	}

	public void setLike(int like) {
		this.like.set(like);
	}
	
	public IntegerProperty likeProperty() {
		return like;
	}
	
	/**
	 * ObjectProperty<LocalDate>�� �� ��ȯ �� String���� �޾� �� date(2020-06-20)�� year, month, day ����
	 * @param date
	 * @return
	 */
	public int[] intDate(String date) {
		//String startDate -> 2020-06-09 15:15:30 (����)
		
		int year = Integer.parseInt(date.substring(0, 4));
		int month = Integer.parseInt((date.substring(5, 7)).replace("0",""));
		int day = Integer.parseInt(date.substring(8, 10));	
		int[] intDate = {year, month, day};
		
		return intDate;
	}

	/**
	 * task ��ü Ȯ�� �� ��� �޼���
	 */
	@Override
	public String toString() {
		return String.format(
				"Task [taskNo=%s, category=%s, task=%s, status=%s, member=%s, substitute=%s, startDate=%s, endDate=%s, memo=%s, like=%s]",
				taskNo, category, task, status, member, substitute, startDate, endDate, memo, like);
	}

	
	
	
	
}