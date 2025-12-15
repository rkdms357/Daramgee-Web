<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header>
    <div class="logo">
        <a href="/Daramgee-Web/">투자초보다!람쥐</a>
    </div>
    <nav class="nav">
        <c:choose>
            <c:when test="${not empty sessionScope.loginUser}">
                <a href="/Daramgee-Web/member/logout">로그아웃</a>
                <a href="/Daramgee-Web/member/mypage">마이페이지</a>
            </c:when>
            <c:otherwise>
                <a href="/Daramgee-Web/member/login">로그인</a>
                <a href="/Daramgee-Web/member/join">회원가입</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
