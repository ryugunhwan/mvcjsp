package board;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
import model.BoardDAO;

public class ModifyForm implements Action {

	@Override
	public ActionData execute(HttpServletRequest request, HttpServletResponse response) {
		BoardDAO dao = new BoardDAO();
		request.setAttribute("data", dao.detail(Integer.parseInt(request.getParameter("id"))));

		dao.close();
		request.setAttribute("main", "modifyForm.jsp");

		return new ActionData();
	}

}
