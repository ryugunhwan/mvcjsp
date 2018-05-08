package board;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class FileDelete implements Action {
	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		BoardVO vo = new BoardVO();
		
		vo.setId(Integer.parseInt(request.getParameter("id"))  );
		vo.setSeq(Integer.parseInt(request.getParameter("seq"))  );
		vo.setPw(request.getParameter("pw"));
		vo.setPname(request.getParameter("pname"));
		vo.setTitle(request.getParameter("title"));
		vo.setContent(request.getParameter("content"));
		vo.setUpfile(request.getParameter("upfile"));
		
		String msg = "인증실패";
		BoardDAO dao = new BoardDAO();
		if(dao.sch(vo)!=null) {
			msg = "파일 삭제";
			vo.setUpfile("");
			String path = request.getRealPath("up");
			path = "C:\\Users\\JHTA\\eclipse-workspace\\mvcjsp\\WebContent\\up";
			
			new File(path+"\\"+request.getParameter("upfile")).delete();
			dao.fileDelete(vo.getId());	
		}
		request.setAttribute("msg", msg);
		request.setAttribute("main", "modifyForm.jsp");
		request.setAttribute("data", vo);
		dao.close();
		
		return new ActionData();
	}

}
