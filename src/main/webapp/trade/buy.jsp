<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>코인 매수 - 투자초보다!람쥐</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/coinresult.css">
    <style>
        .trade-section {
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-bottom: 50px;
            background-color: #ffffff;
        }

        .trade-form-container {
            margin: 20px auto;
            background: white;
            width: 800px;
            max-width: 90%;
            padding: 40px 30px;
            border-radius: 12px;
            border: 1px solid #eee;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .page-title {
            margin-top: 0;
        }

        .cash-info {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 20px;
            padding-bottom: 15px;
            border-bottom: 1px solid #eee;
            color: #333;
        }

        .cash-amount {
            color: #ffb300;
            font-size: 20px;
            margin-left: 5px;
        }

        .trade-form {
            display: flex;
            flex-direction: column;
            gap: 20px;
            padding: 10px 0;
        }

        .form-row {
            display: flex;
            align-items: center;
            gap: 20px;
        }

        .form-row label {
            width: 150px;
            font-weight: 600;
            text-align: right;
            color: #555;
        }

        .form-row input {
            flex-grow: 1;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
            outline: none;
        }

        .form-row input:focus {
            border-color: #ffb300;
        }

        .btn-submit {
            background: #ffb300;
            color: white;
            padding: 15px 30px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            font-size: 18px;
            cursor: pointer;
            transition: background 0.2s;
            margin-top: 10px;
        }

        .btn-submit:hover {
            background: #e6a100;
        }

        .message-box {
            width: 800px;
            max-width: 90%;
            margin: 20px auto 0;
            padding: 15px;
            border-radius: 8px;
            font-weight: 600;
            text-align: center;
        }

        .message-box.success {
            background-color: #e6ffe6;
            color: #1e8449;
            border: 1px solid #b3ffb3;
        }

        .message-box.error {
            background-color: #ffe6e6;
            color: #cc0000;
            border: 1px solid #ffb3b3;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<c:if test="${not empty msg}">
    <div class="message-box ${fn:contains(msg, '성공') ? 'success' : 'error'}">
            ${msg}
    </div>
</c:if>
<%@ include file="/WEB-INF/views/common/coinresult.jsp" %>
<section class="trade-section">
    <div class="trade-form-container">
        <p class="page-title">
            코인 매수 주문
        </p>
        <div class="cash-info">
            보유 투자금:
            <span class="cash-amount">
                    <fmt:formatNumber value="${cash}" type="number" maxFractionDigits="4"/>
            </span> 원
        </div>
        <form action="${pageContext.request.contextPath}/trade/buy" method="post" class="trade-form">
            <div class="form-row">
                <label for="symbol">매수할 코인 약어</label>
                <input type="text" id="symbol" name="symbol" placeholder="예: BTC 또는 ETH" required>
            </div>
            <div class="form-row">
                <label for="count">매수 수량</label>
                <input type="number" id="count" name="count" placeholder="정수만 입력하세요" required>
            </div>
            <button type="submit" class="btn-submit">매수 실행</button>
        </form>
    </div>
</section>
</body>
</html>