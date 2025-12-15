package member.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;
import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/login")
public class LoginServlet extends HttpServlet {
    MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/member/login.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();

        String userId = request.getParameter("userId");
        String password = request.getParameter("password");

        Map<String, Object> res = new HashMap<>();
        if(userId == null || userId.isBlank() || password == null || password.isBlank()) {
            res.put("success", false);
            res.put("msg", "아이디와 비밀번호를 입력하세요.");
            out.print(gson.toJson(res));
            return;
        }

        MemberDTO member = memberService.selectById(userId);
        if(member != null && password.equals(member.getPassword())) {
            request.getSession().setAttribute("loginUser", member);
            res.put("success", true);
        } else {
            res.put("success", false);
            res.put("msg", "아이디 또는 비밀번호가 틀렸습니다.");
        }
        out.print(gson.toJson(res));
    }
}