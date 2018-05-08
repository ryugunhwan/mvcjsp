package model;

import java.util.ArrayList;
import java.util.Date;

public class BoardVO {
	int id, gid, seq, lev, cnt;
	String title, pname, pw, upfile, content;
	Date regDate;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getGid() {
		return gid;
	}
	public void setGid(int gid) {
		this.gid = gid;
	}
	public int getSeq() {
		return seq;
	}
	public void setSeq(int seq) {
		this.seq = seq;
	}
	public int getLev() {
		return lev;
	}
	public void setLev(int lev) {
		this.lev = lev;
	}
	public int getCnt() {
		return cnt;
	}
	public void setCnt(int cnt) {
		this.cnt = cnt;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getPname() {
		return pname;
	}
	public void setPname(String pname) {
		this.pname = pname;
	}
	public String getPw() {
		return pw;
	}
	public void setPw(String pw) {
		this.pw = pw;
	}
	public String getUpfile() {
		if(upfile==null)
			upfile="";
		return upfile;
	}
	public void setUpfile(String upfile) {
		this.upfile = upfile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getRegDate() {
		return regDate;
	}
	public void setRegDate(Date date) {
		this.regDate = date;
	}
	public boolean isImg() {
		if(upfile==null) {
			return false;
		}
		ArrayList<String> res = new ArrayList<>();
		
		res.add("png");
		res.add("bmp");
		res.add("jpg");
		res.add("jpeg");
		res.add("gif");
		
		String exp = upfile.substring(upfile.indexOf(".")+1);
		System.out.println("VO_IMG_exp : "+exp);
		
		return res.contains(exp);
	}
}
