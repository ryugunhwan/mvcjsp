package control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.Action;
import model.ActionData;
@WebServlet("/board/*")
@MultipartConfig(
		location = "C:\\tomcat\\temp",
		maxFileSize = 1024 * 5000,
		maxRequestSize = 1024 * 1024 * 100, 
		fileSizeThreshold = 1024 * 1024 * 10
		)
public class F_Control extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public F_Control() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String service = request.getRequestURI().substring((request.getContextPath() + "/board/").length());
		System.out.println("서비스:"+service);

		try {
			Action action = (Action) Class.forName("board." + service).newInstance();
			ActionData data = action.execute(request, response);

			if (data != null) {
				if (data.isRedirect()) {
					response.sendRedirect(data.getPath());
				} else {
					request.getRequestDispatcher("../view/template.jsp").forward(request, response);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
