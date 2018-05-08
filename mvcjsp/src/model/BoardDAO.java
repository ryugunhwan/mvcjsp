package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {

	Connection con;
	PreparedStatement ptmt;
	ResultSet rs;
	String sql;
	
	public BoardDAO() {
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource)init.lookup("java:comp/env/oooo");
			con = ds.getConnection();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//토탈카운트. 게시글 몇갠지 센다.
	public int totalCount()
	{
		int res=0;
		try {
			sql = "select count(*) from board";
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			
			rs.next();
			res = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return res;
	}

	//리스트. 한페이지만큼의 리스트 받아옴. close
	public ArrayList<BoardVO> list(int start, int end){
		ArrayList<BoardVO> res = new ArrayList<>();
		try {
			sql = "select * from "
					+ "(select rownum rnum, tt.* from "
					+ "(select * from board order by gid desc, seq)tt) "
					+ "where rnum >= ? and rnum <= ?";
			
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, start);
			ptmt.setInt(2, end);
			rs = ptmt.executeQuery();
			
			while(rs.next()) {
				BoardVO vo = new BoardVO();
				
				vo.setLev(rs.getInt("lev"));
				vo.setId(rs.getInt("id"));
				vo.setTitle(rs.getString("title"));
				vo.setPname(rs.getString("pname"));
				vo.setRegDate(rs.getTimestamp("regDate"));
				
				res.add(vo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return res;
	}
	
	//인서트. vo받아서 디비에 넣음. close
	public int insert(BoardVO vo) {
		int res=0;
		try {
			sql = "insert into board "
					+ "(id, gid, seq, lev, cnt, regdate, pname, pw, title, content, upfile) values "
					+ "(board_Seq.nextval,board_Seq.nextval,0,0,-1, sysdate, ?,?,?,?,?)";
			ptmt = con.prepareStatement(sql);
			
			ptmt.setString(1, vo.getPname());
			ptmt.setString(2, vo.getPw());
			ptmt.setString(3, vo.getTitle());
			ptmt.setString(4, vo.getContent());
			ptmt.setString(5, vo.getUpfile());
			
			ptmt.executeUpdate();
			
			sql="select max(id) from board";
			ptmt = con.prepareStatement(sql);
			
			rs= ptmt.executeQuery();
			rs.next();
			res = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return res;
	}
	
	//에드카운트. 조회수++
	public void addCount(int id) {
		try {
			sql = "update board set cnt = cnt+1 where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);

			ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	//디테일. 아이디 받고 게시글에 뿌려질 정보를 받아 게시글로 리턴. pw빼고 전부.
	public BoardVO detail(int id){
		try {
			sql ="select * from board where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			rs = ptmt.executeQuery();
			
			if(rs.next()) {
				BoardVO vo = new BoardVO();
				vo.setId(id);
				vo.setGid(rs.getInt("gid"));
				vo.setLev(rs.getInt("lev"));
				vo.setSeq(rs.getInt("seq"));
				vo.setCnt(rs.getInt("cnt"));
				vo.setRegDate(rs.getTimestamp("regDate"));
				vo.setTitle(rs.getString("title"));
				vo.setPname(rs.getString("pname"));
				vo.setContent(rs.getString("content"));
				vo.setUpfile(rs.getString("upfile"));
				
				return vo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	//서치. 아이디,패스워드에 해당되는 게시물을 리턴.
	public BoardVO sch(BoardVO vo) {
		try {
			sql = "select * from board where id =? and pw =?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, vo.getId());
			ptmt.setString(2, vo.getPw());
			
			rs = ptmt.executeQuery();
			
			if(rs.next()) {
				BoardVO res = new BoardVO();
				res.setUpfile(rs.getString("upfile"));
				return res;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void delete(int id) {
		try {
			sql = "delete from board where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			
			ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void modify(BoardVO vo) {
		try {
			sql = "update board set title=?, pname=?, content=?, upfile=? where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setString(1, vo.getTitle());
			ptmt.setString(2, vo.getPname());
			ptmt.setString(3, vo.getContent());
			ptmt.setString(4, vo.getUpfile());
			ptmt.setInt(5, vo.getId());
			
			ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void fileDelete(int id) {
		try {
			sql = "update board set upfile = null where id = ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, id);
			
			ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int reply(BoardVO vo) {
		int res =0;
		try {
			BoardVO oo = detail(vo.id);
			
			sql = "update board set seq = seq+1 where gid = ? and seq > ?";
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, oo.getGid());
			ptmt.setInt(2, oo.getSeq());
			ptmt.executeUpdate();
			
			sql = "insert into board (id, gid, seq, lev, regdate, cnt, pname, pw, title, content) "
					+ "values (board_Seq.nextval, ?,?, ?, sysdate, -1, ? ,?,?,?)";
			
			ptmt = con.prepareStatement(sql);
			ptmt.setInt(1, oo.getGid());
			ptmt.setInt(2, oo.getSeq()+1);
			ptmt.setInt(3, oo.getLev()+1);
			ptmt.setString(4,vo.getPname());
			ptmt.setString(5,vo.getPw());
			ptmt.setString(6,vo.getTitle());
			ptmt.setString(7,vo.getContent());
			
			ptmt.executeUpdate();
			
			sql = "select max(id) from board";
			ptmt = con.prepareStatement(sql);
			rs = ptmt.executeQuery();
			rs.next();
			res = rs.getInt(1);
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			close();
		}
		return res;
	}
	
	public void close() {
		if(rs!=null) try {rs.close();} catch (SQLException e) {e.printStackTrace();}
		if(ptmt!=null) try {ptmt.close();} catch (SQLException e) {e.printStackTrace();}
		if(con!=null) try {con.close();} catch (SQLException e) {e.printStackTrace();}
	}
}
