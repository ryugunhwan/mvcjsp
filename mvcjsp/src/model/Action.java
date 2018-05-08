package model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Action {
	ActionData execute(HttpServletRequest request, HttpServletResponse response);
}
