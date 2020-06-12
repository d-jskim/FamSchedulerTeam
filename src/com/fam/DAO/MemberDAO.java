package com.fam.DAO;

import oracle.jdbc.*;
import static common.JDBCTemplate.*;
import java.sql.*;
import java.util.*;

import com.fam.VO.Member;



public class MemberDAO implements MemberSql{
	private Connection conn;
	public MemberDAO(Connection conn) {
		this.conn=conn;
	} 
	
	public int getInsert(Member member) {
		int res = 0;
		CallableStatement Pstmt = null;
		try {
			Pstmt = conn.prepareCall(member_insert); // ����ȣ��
			
			Pstmt.setString(1, member.getMemberId());
			Pstmt.setString(2, member.getPassword());
			Pstmt.setInt(3, member.getFamilyId());
			Pstmt.setString(4, member.getNickname());
			Pstmt.execute();
			Commit(conn);
			
			//������ �����Ѵ�
			/*
			 * res = Pstmt.executeUpdate(); if (res>0) { System.out.println("�Է¼���");
			 * Commit(conn); }
			 */
		}catch (Exception e) {
			Rollback(conn);
		} finally {
			Close(Pstmt);
		}
		return res;
	}
	
	
	/**
	 * login
	 * @param member
	 * @return
	 */
	public int getLogIn(Member member) {
		System.out.println("dao���� �Ծ�???");
	int res = 0; //rs : resultSet  / res : result�� ����.
	
	CallableStatement cstmt =null;
	
	try {
		System.out.println("here 1");
		cstmt = conn.prepareCall(member_login);
		System.out.println("here 2");
        cstmt.setQueryTimeout(1800);
        System.out.println("here 3");
        cstmt.setString(2, member.getMemberId());
        System.out.println("here 4");
        cstmt.setString(3, member.getPassword());
        System.out.println("here 5");
        cstmt.registerOutParameter(1, java.sql.Types.INTEGER);
        System.out.println("here 6");
        cstmt.executeUpdate();
        System.out.println("here 7");
        res = cstmt.getInt(1);///////////////////////////////////????
        System.out.println("here 8");
        Commit(conn);
        System.out.println("���� �� �����߾�?");
        
	}catch (Exception e) {
		Rollback(conn);
	} finally {
		Close(cstmt);
	}
	return res;
	}	
	
	public Member member_selectVO(String memberId) {
		System.out.println("�̸��� ��� ����? db�� �־����");
		CallableStatement cstmt = null;
		Member m = null;
		ResultSet rs = null;
		
		try {
			System.out.println("here 1");
			cstmt = conn.prepareCall(member_selectVO);
			System.out.println("here 2");
			cstmt.setString(1, memberId);
			System.out.println("here 3");
			cstmt.registerOutParameter(2, OracleTypes.CURSOR);
			System.out.println("here 4");
			cstmt.execute();
			System.out.println("here 5");
			Commit(conn);
			System.out.println("here 6");
			rs =((OracleCallableStatement)cstmt).getCursor(2);
			System.out.println("here 7");
			
			while (rs.next()) {
				System.out.println("here 8");
				m = new Member(rs.getString(1), rs.getInt(2), rs.getString(3));
				System.out.println("here 9");	
			}
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			Close(rs);
			Close(cstmt);
		}
		System.out.println("���� ����");
		System.out.println(m.toString());
		return m;
	}
}//class end
