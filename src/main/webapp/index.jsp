<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>투자초보다!람쥐</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/quiz.css">
    <style>
        body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background-color: #ffffff;
            color: #111;
        }

        .mascot {
            margin-top: 80px;
            margin: 80px 0px 20px 0px;
            width: 220px;
        }

        main {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            height: calc(100vh - 80px);
            text-align: center;
            margin-bottom: 80px;
        }

        main h1 {
            font-size: 48px;
            margin-bottom: 16px;
        }

        main p {
            font-size: 18px;
            color: #555;
            margin-bottom: 40px;
        }

        .btn-area a {
            display: inline-block;
            padding: 14px 32px;
            margin: 0 10px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 16px;
        }

        .btn-primary {
            background: #ffb300;
            color: white;
        }

        .btn-primary:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<%@ include file="/WEB-INF/views/quiz/quiz.jsp"%>
<main>
    <img src="${pageContext.request.contextPath}/images/Daramgee.png" class="mascot" alt="다람쥐 캐릭터">
    <h1>투자, 어렵지 않게</h1>
    <p>내 돈 잃지 않고 배우는 안전한 가상화폐 투자 연습장</p>
    <div class="btn-area">
        <a href="${pageContext.request.contextPath}/member/login" class="btn-primary">지금 시작하기</a>
    </div>
</main>
</body>
</html>