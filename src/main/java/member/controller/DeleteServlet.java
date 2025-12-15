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

@WebServlet("/member/delete")
public class DeleteServlet extends HttpServlet {
    MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/member/delete.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter();
        Gson gson = new Gson();
        Map<String, Object> res = new HashMap<>();

        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (session != null) ? (MemberDTO) session.getAttribute("loginUser") : null;
        String password = request.getParameter("password");

        if(loginUser == null) {
            res.put("success", false);
            res.put("msg", "로그인이 필요합니다.");
            out.print(gson.toJson(res));
            return;
        }

        if(!password.equals(loginUser.getPassword())) {
            res.put("success", false);
            res.put("msg", "비밀번호가 틀렸습니다.");
            out.print(gson.toJson(res));
            return;
        }

        String msg = memberService.deleteService(loginUser.getUserId());
        session.invalidate();

        res.put("success", true);
        res.put("msg", "회원탈퇴가 완료되었습니다.");
        out.print(gson.toJson(res));
    }
}