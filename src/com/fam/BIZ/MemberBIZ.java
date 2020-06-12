package com.fam.BIZ;

import java.sql.*;
import java.util.*;

import com.fam.DAO.*;
import com.fam.VO.Member;

import static common.JDBCTemplate.*;

public class MemberBIZ {

	public int getLogIn(Member member) {
		System.out.println("멤버 들고 biz까지 왔니?");
		Connection conn = getConnection();

		int rs = new MemberDAO(conn).getLogIn(member);
		Close(conn);
		return rs;
	}

	public Member member_selectVO(String memberId) {
		System.out.println("이메일 들고 왔어?");
		Member member = null;
		Connection conn = getConnection();

		member = new MemberDAO(conn).member_selectVO(memberId);
		Close(conn);
		return member;
	}

}
