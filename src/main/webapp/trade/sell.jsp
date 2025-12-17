<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>코인 매도 - 투자초보다!람쥐</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/coinmyresult.css">
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
            padding: 40px 0;
        }

        .container {
            margin: 10px 0;
            background: white;
            width: 800px;
            max-width: 90%;
            padding: 20px 30px;
            border-radius: 12px;
            border: 1px solid #eee;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .page-title {
            font-size: 30px;
            color: #1e8449;
            font-weight: 600;
            margin-bottom: 30px;
            display: block;
            text-align: center;
        }

        .message-box {
            padding: 15px;
            margin-bottom: 20px;
            border-radius: 6px;
            font-weight: 600;
            text-align: center;
            opacity: 0.9;
        }
        .message-box.success { background-color: #e6ffe6; color: #1e8449; border: 1px solid #b3ffb3; }
        .message-box.error { background-color: #ffe6e6; color: #cc0000; border: 1px solid #ffb3b3; }

        .table-title {
            font-size: 22px;
            font-weight: 600;
            color: #333;
            margin-bottom: 15px;
            padding-bottom: 10px;
            border-bottom: 2px solid #ddd;
        }

        .portfolio-table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 40px;
        }

        .portfolio-table th {
            background-color: #e6ffe6;
            color: #1e8449;
            font-weight: 600;
            border-top: 2px solid #1e8449;
            padding: 12px 15px;
            text-align: left;
        }

        .portfolio-table td {
            padding: 12px 15px;
            border-bottom: 1px solid #eee;
            text-align: left;
            font-size: 15px;
        }

        .portfolio-table tr:hover {
            background-color: #f9f9f9;
        }

        .trade-form {
            display: flex;
            flex-direction: column;
            gap: 20px;
            padding: 20px 0;
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

        .form-row input[type="text"], .form-row input[type="number"] {
            flex-grow: 1;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 6px;
            font-size: 16px;
        }

        .btn-submit {
            background: #1e8449;
            color: white;
            padding: 14px 30px;
            border: none;
            border-radius: 8px;
            font-weight: 600;
            font-size: 18px;
            cursor: pointer;
            transition: opacity 0.2s;
            margin-top: 10px;
        }

        .btn-submit:hover {
            opacity: 0.9;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<main>
    <div class="container">
        <c:if test="${not empty msg}">
            <div class="message-box ${fn:contains(msg, '성공') ? 'success' : 'error'}">
                    ${msg}
            </div>
        </c:if>
        <p class="page-title">코인 매도</p>
        <%@ include file="/WEB-INF/views/common/coinmyresult.jsp" %>
    </div>
    <div class="container">
        <p class="table-title" style="border-bottom: none;">매도 주문 실행</p>
        <form action="${pageContext.request.contextPath}/trade/sell" method="post" class="trade-form">
            <div class="form-row">
                <label for="symbol">매도할 코인 약어</label>
                <input type="text" id="symbol" name="symbol" placeholder="예: BTC 또는 ETH" required>
            </div>
            <div class="form-row">
                <label for="count">매도 수량</label>
                <input type="number" id="count" name="count" placeholder="보유 수량 내에서 입력" min="1" required>
            </div>
            <button type="submit" class="btn-submit">매도 실행</button>
        </form>
    </div>
</main>
</body>
</html>