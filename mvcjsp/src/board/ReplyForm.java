package board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;
import model.BoardVO;

public class ReplyForm implements Action{

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		BoardDAO dao = new BoardDAO();
		BoardVO vo = dao.detail(Integer.parseInt(request.getParameter("id")));
		dao.close();
		
		vo.setTitle("[답글]"+vo.getTitle());
		vo.setContent("[답글]"+vo.getContent());
		
		request.setAttribute("data", vo);
		request.setAttribute("main", "replyForm.jsp");
		
		return new ActionData();
	}
}
