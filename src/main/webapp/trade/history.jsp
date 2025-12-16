<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>거래 내역 조회</title>
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
            justify-content: flex-start;
            align-items: center;
            min-height: calc(100vh - 80px);
            padding: 40px 0;
        }

        .container {
            margin: 10px 0;
            background: white;
            width: 900px;
            max-width: 95%;
            padding: 40px 30px;
            border-radius: 12px;
            border: 1px solid #eee;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .page-title {
            font-size: 30px;
            color: #333;
            font-weight: 600;
            margin-bottom: 30px;
            display: block;
            text-align: center;
        }

        .history-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 30px;
        }

        .history-table th, .history-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
            text-align: left;
            font-size: 15px;
        }

        .history-table th {
            background-color: #f1f1f1;
            color: #333;
            font-weight: 600;
            border-top: 2px solid #555;
        }

        .history-table tr:hover {
            background-color: #f9f9f9;
        }

        .trade-type-buy {
            font-weight: 700;
            color: #cc0000;
        }
        .trade-type-sell {
            font-weight: 700;
            color: #0055ff;
        }

        .price-col {
            text-align: right;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<main>
    <div class="container">
        <p class="page-title">거래 내역 조회</p>
        <c:choose>
            <c:when test="${not empty historyList}">
                <table class="history-table">
                    <thead>
                    <tr>
                        <th style="width: 150px;">거래 일시</th>
                        <th>구분</th>
                        <th>코인</th>
                        <th>수량</th>
                        <th class="price-col">단가</th>
                        <th class="price-col">총액</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="trade" items="${historyList}">
                        <c:set var="isBuy" value="${trade.tradeType eq 'BUY '}" />
                        <tr>
                            <td><fmt:formatDate value="${trade.tradeDate}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                            <td class="${isBuy ? 'trade-type-buy' : 'trade-type-sell'}">
                                    ${isBuy ? '매수' : '매도'}
                            </td>
                            <td>${fn:substringAfter(trade.assetId, 'KRW-')}</td>
                            <td><fmt:formatNumber value="${trade.tradeQuantity}" type="number" maxFractionDigits="0" /> 개</td>
                            <td class="price-col">
                                <fmt:formatNumber value="${trade.tradePrice}" type="number" minFractionDigits="2" maxFractionDigits="4" /> 원
                            </td>
                            <td class="price-col">
                                <c:set var="total" value="${trade.tradePrice * trade.tradeQuantity}" />
                                <fmt:formatNumber value="${total}" type="number" minFractionDigits="2" maxFractionDigits="4" /> 원
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div style="text-align: center; padding: 50px; color: #888;">
                    아직 거래 기록이 없습니다. 매수를 먼저 진행해주세요.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</main>
</body>
</html>