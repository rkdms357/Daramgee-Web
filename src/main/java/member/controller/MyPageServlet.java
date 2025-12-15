package member.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import member.dto.MemberDTO;
import member.service.MemberService;

@WebServlet("/member/mypage")
public class MyPageServlet extends HttpServlet {
    MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        MemberDTO loginUser = (session != null) ? (MemberDTO) session.getAttribute("loginUser") : null;

        if(loginUser == null) {
            response.sendRedirect(request.getContextPath() + "/member/login.jsp");
            return;
        }

        MemberDTO freshMember = memberService.selectById(loginUser.getUserId());
        request.setAttribute("member", freshMember);
        request.getRequestDispatcher("/member/mypage.jsp").forward(request, response);
    }
}