<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>마이페이지</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <style>
        body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background-color: #ffffff;
            color: #111;
        }

        main {
            margin-top: 80px;
            display: flex;
            justify-content: center;
            align-items: flex-start;
            min-height: calc(100vh - 80px);
            padding: 20px;
        }

        .page-title {
            font-size: 30px;
            color: #ffb300;
            font-weight: 600;
            margin-bottom: 5px;
            text-transform: uppercase;
        }

        .mypage-container {
            width: 100%;
            max-width: 600px;
            padding: 40px;
            text-align: center;
            border: 1px solid #eee;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            background-color: #fff;
        }

        .mypage-container h2 {
            font-size: 32px;
            color: #111;
            margin-bottom: 30px;
            border-bottom: 2px solid #ffb300;
            padding-bottom: 10px;
            display: inline-block;
        }

        .asset-info {
            background-color: #fff9e6;
            padding: 20px;
            border-radius: 8px;
            margin-bottom: 30px;
            text-align: left;
        }

        .asset-info p {
            font-size: 20px;
            font-weight: 500;
            margin: 10px 0;
            color: #333;
        }

        .asset-info strong {
            font-size: 24px;
            color: #ffb300;
            font-weight: 700;
            margin-left: 10px;
        }

        .btn-area {
            display: flex;
            flex-direction: column;
            gap: 15px;
        }

        .btn-area a {
            display: block;
            padding: 14px 32px;
            border-radius: 8px;
            text-decoration: none;
            font-weight: 600;
            font-size: 16px;
            transition: background-color 0.2s, opacity 0.2s;
        }

        .btn-primary {
            background: #ffb300;
            color: white;
        }

        .btn-primary:hover {
            opacity: 0.9;
        }

        .btn-secondary {
            background: #f1f1f1;
            color: #555;
            border: 1px solid #ddd;
        }

        .btn-secondary:hover {
            background: #e0e0e0;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <div class="mypage-container">
        <p class="page-title">마이페이지</p>
        <h2>${member.userId} 님, 환영합니다!</h2>
        <div class="asset-info">
            <p>현재 보유 자산 (투자금):
                <strong>
                    <fmt:formatNumber value="${member.cash}" type="number" /> 원
                </strong>
            </p>
        </div>
        <div class="btn-area">
            <a href="${pageContext.request.contextPath}/member/logout" class="btn-primary">로그아웃</a>
            <a href="${pageContext.request.contextPath}/member/delete" class="btn-secondary">회원탈퇴</a>
        </div>
    </div>
</main>
</body>
</html>