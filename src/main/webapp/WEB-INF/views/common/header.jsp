<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<header class="main-header">
    <div class="logo">
        <a href="${pageContext.request.contextPath}/" class="logo-link">투자초보다!람쥐</a>
    </div>
    <nav class="main-nav">
        <c:choose>
            <c:when test="${not empty sessionScope.loginUser}">
                <div class="nav-item has-dropdown">
                    <a href="#" class="nav-link">거래</a>
                    <div class="dropdown-menu">
                        <a href="${pageContext.request.contextPath}/trade/buy" class="dropdown-item">매수 주문</a>
                        <a href="${pageContext.request.contextPath}/trade/sell" class="dropdown-item">매도 주문</a>
                        <a href="${pageContext.request.contextPath}/trade/history" class="dropdown-item">거래 내역</a>
                    </div>
                </div>
                <div class="nav-item">
                    <a href="${pageContext.request.contextPath}/member/mypage" class="nav-link">마이페이지</a>
                </div>
                <div class="nav-item">
                    <a href="${pageContext.request.contextPath}/member/logout" class="nav-link">로그아웃</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="nav-item">
                    <a href="${pageContext.request.contextPath}/member/login" class="nav-link">로그인</a>
                </div>
                <div class="nav-item">
                    <a href="${pageContext.request.contextPath}/member/join" class="nav-link">회원가입</a>
                </div>
            </c:otherwise>
        </c:choose>
    </nav>
</header>