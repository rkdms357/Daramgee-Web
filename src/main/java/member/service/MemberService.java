package member.service;

import member.dao.MemberDAO;
import member.dto.MemberDTO;

public class MemberService {

    MemberDAO memberDAO = new MemberDAO();

    public MemberDTO selectById(String userId) {
        return memberDAO.selectById(userId);
    }

    public String deleteService(String userId) {
        return memberDAO.delete(userId);
    }

    public String insertService(MemberDTO member) {
        return memberDAO.insert(member);
    }
}
