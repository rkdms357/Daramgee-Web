<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>마이페이지</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/coinmyresult.css">
    <style>
        body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background-color: #ffffff;
            color: #111;
        }

        main {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            min-height: calc(100vh - 80px);
            padding: 20px;
        }

        .page-wrapper {
            width: 800px;
            max-width: 100%;
            display: flex;
            flex-direction: column;
            gap: 30px;
        }

        .mypage-card {
            background: #fff;
            padding: 30px;
            border-radius: 14px;
            border: 1px solid #eee;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
            text-align: center;
        }

        .page-title {
            font-size: 28px;
            font-weight: 600;
            color: #1e8449;
            margin-bottom: 10px;
        }

        .mypage-card h2 {
            font-size: 26px;
            margin-bottom: 25px;
        }

        .asset-info {
            background: #fff9e6;
            border-radius: 10px;
            padding: 20px;
            margin-bottom: 25px;
        }

        .asset-info p {
            font-size: 18px;
            margin: 0;
        }

        .asset-info strong {
            display: block;
            margin-top: 8px;
            font-size: 24px;
            color: #ffb300;
        }

        .btn-area {
            display: block;
            margin-top: 10px;
        }

        .btn-secondary {
            padding: 12px 28px;
            border-radius: 8px;
            border: 1px solid #ddd;
            background: #f1f1f1;
            color: #555;
            font-weight: 600;
            text-decoration: none;
        }

        .btn-area a {
            display: block;
            width: 100%;
            box-sizing: border-box;
            padding: 14px 0;
            text-align: center;
            border-radius: 10px;
            font-weight: 600;
            font-size: 16px;
            text-decoration: none;
        }

        .btn-secondary:hover {
            background: #e0e0e0;
        }

        .portfolio-section {
            background: #fff;
            padding: 30px;
            border-radius: 14px;
            border: 1px solid #eee;
            box-shadow: 0 4px 12px rgba(0,0,0,0.05);
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <div class="page-wrapper">
        <section class="mypage-card">
            <p class="page-title">마이페이지</p>
            <h2>${member.userId} 님, 환영합니다!</h2>
            <div class="asset-info">
                <p>
                    현재 보유 자산 (투자금)
                    <strong>
                        <fmt:formatNumber value="${member.cash}" type="number"/> 원
                    </strong>
                </p>
            </div>
            <div class="btn-area">
                <a href="${pageContext.request.contextPath}/member/delete"
                   class="btn-secondary">회원탈퇴</a>
            </div>
        </section>
        <section class="portfolio-section">
            <%@ include file="/WEB-INF/views/common/coinmyresult.jsp" %>
        </section>
    </div>
</main>
</body>
</html>