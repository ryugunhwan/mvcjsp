package board;

import java.io.File;
import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class InsertReg implements Action{

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		ActionData data = new ActionData();
		BoardVO vo = new BoardVO();

		vo.setTitle(request.getParameter("title"));
		vo.setPname(request.getParameter("pname"));
		vo.setPw(request.getParameter("pw"));
		vo.setContent(request.getParameter("content"));

		vo.setUpfile(fileUpload(request));
		
		int id = new BoardDAO().insert(vo);
		
		data.setRedirect(true);
		data.setPath("Detail?id="+id);
		return data;
	}
	
	public String fileUpload(HttpServletRequest request) {
		try {
			Part pp = request.getPart("upfile");
			if(pp.getContentType()!=null) {
				String fileName="";

				for(String hh: pp.getHeader("Content-Disposition").split(";")) {
					if(hh.trim().startsWith("filename")) {
						fileName=hh.substring(hh.indexOf("=")+1).trim().replaceAll("\"", "");
					}
				}
				if(!fileName.equals("")) {
					return fileSave(pp, fileName, request);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	String fileSave(Part pp, String fileName, HttpServletRequest request) {
		int pos = fileName.lastIndexOf(".");

		String fileDo = fileName.substring(0, pos);
		String exp = fileName.substring(pos);
		String path = request.getRealPath("up");
		
		path = "C:\\Users\\JHTA\\eclipse-workspace\\mvcjsp\\WebContent\\up";
		path += "\\";
		
		int cnt =0;
		
		File ff = new File(path+fileName);
		
		while(ff.exists()) {
			fileName = fileDo+"_"+(cnt++)+exp;
			ff = new File(path+fileName);
		}
		
		try {
			pp.write(path+fileName);
			pp.delete();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return fileName;
	}
	
}
