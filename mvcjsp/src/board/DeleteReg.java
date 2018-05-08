package board;

import java.io.File;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class DeleteReg implements Action {

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		int page = Integer.parseInt(request.getParameter("page"));
		
		BoardVO vo = new BoardVO();
		vo.setId(Integer.parseInt(request.getParameter("id")));
		vo.setPw(request.getParameter("pw"));

		BoardDAO dao = new BoardDAO();
		BoardVO res = dao.sch(vo);

		String msg = "비번다름";
		String url = "DeleteForm?id=" + vo.getId() + "&page=" + page;
		if (res != null) {
			if (!res.getUpfile().equals("")) {
				String path = request.getRealPath("up");
				path = "C:\\Users\\JHTA\\eclipse-workspace\\mvcjsp\\WebContent\\up";
				path += "\\";
				new File(path + res.getUpfile()).delete();
			}
			dao.delete(vo.getId());
			msg = "글삭제";
			url = "List?page=" + page;
		}
		dao.close();

		request.setAttribute("main", "alert.jsp");
		request.setAttribute("msg", msg);
		request.setAttribute("url", url);

		return new ActionData();
	}

}
