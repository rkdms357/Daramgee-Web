<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>코인 시세 조회</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <style>
        body {
            margin: 0;
            font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", sans-serif;
            background-color: #ffffff;
        }

        main {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            min-height: calc(100vh - 80px);
        }

        .asset-container {
            margin: 10px 0px;
            background: white;
            width: 800px;
            max-width: 90%;
            padding: 40px 30px;
            border-radius: 12px;
            border: 1px solid #eee;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
            text-align: center;
        }

        .page-title {
            font-size: 30px;
            color: #ffb300;
            font-weight: 600;
            margin-bottom: 30px;
            display: block;
        }

        .asset-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        .asset-table th, .asset-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
            text-align: left;
            font-size: 15px;
        }

        .asset-table th {
            background-color: #fff9e6;
            color: #333;
            font-weight: 600;
            border-top: 2px solid #ffb300;
        }

        .asset-table tr:hover {
            background-color: #f9f9f9;
        }

        .price-col {
            text-align: right;
            font-weight: bold;
            color: #111;
        }

        .asset-table td[colspan="4"] {
            text-align: center;
            color: #888;
            padding: 40px;
        }

        .trade-group {
            margin-top: 30px;
            display: flex;
            justify-content: center;
            gap: 15px;
        }

        .btn-primary, .btn-secondary {
            display: inline-block;
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
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<main>
    <div class="asset-container">
        <p class="page-title">코인 실시간 시세</p>
        <table class="asset-table">
            <thead>
            <tr>
                <th>코인명</th>
                <th>현재가</th>
                <th>전일 대비</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty assets}">
                    <c:forEach var="asset" items="${assets}">
                        <tr>
                            <td>${asset.name}</td>
                            <td class="price-col">
                                <fmt:formatNumber value="${asset.currentPrice}" type="number" /> 원
                            </td>
                            <td>${asset.changeRate}%</td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="4">현재 시세 정보를 불러올 수 없습니다.</td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
        <div class="trade-group">
            <a href="${pageContext.request.contextPath}/trade/" class="btn-primary">매수하기</a>
            <a href="${pageContext.request.contextPath}/trade/" class="btn-secondary">매도하기</a>
        </div>
    </div>
</main>
</body>
</html>
