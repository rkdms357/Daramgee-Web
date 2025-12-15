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

@WebServlet("/member/join")
public class JoinServlet extends HttpServlet {
    MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/member/join.jsp");
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

        if(userId == null || userId.trim().isEmpty() || password == null || password.trim().isEmpty()) {
            res.put("success", false);
            res.put("msg", "아이디와 비밀번호를 입력하세요.");
            out.print(gson.toJson(res));
            return;
        }

        MemberDTO check = memberService.selectById(userId);
        if(check != null) {
            res.put("success", false);
            res.put("msg", "이미 존재하는 아이디입니다.");
            out.print(gson.toJson(res));
            return;
        }

        MemberDTO member = new MemberDTO();
        member.setUserId(userId);
        member.setPassword(password);
        member.setCash(1000000);
        String msg = memberService.insertService(member);

        res.put("success", true);
        res.put("msg", msg + " (가입축하금으로 투자금 100만원 지급되었습니다.)");
        out.print(gson.toJson(res));
    }
}