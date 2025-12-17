<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<p class="table-title">보유 코인 현황</p>
<table class="portfolio-table">
    <thead>
    <tr>
        <th>코인명</th>
        <th>약어</th>
        <th>보유 수량</th>
        <th class="price-col">평균 단가</th>
        <th class="price-col">현재 시세</th>
        <th class="price-col">평가 손익</th>
    </tr>
    </thead>
    <tbody>
    <c:choose>
        <c:when test="${not empty myList}">
            <c:forEach var="item" items="${myList}">
                <tr>
                    <td>${item.name}</td>
                    <td>${fn:substringAfter(item.assetId, 'KRW-')}</td>
                    <td>
                        <fmt:formatNumber value="${item.quantity}" type="number" maxFractionDigits="0"/> 개
                    </td>
                    <td class="price-col">
                        <fmt:formatNumber value="${item.avgPrice}" type="number" maxFractionDigits="4"/> 원
                    </td>
                    <td class="price-col">
                        <fmt:formatNumber value="${item.currentPrice}" type="number" maxFractionDigits="4"/> 원
                    </td>
                    <td class="price-col
                        ${item.profit >= 0 ? 'profit' : 'loss'}">
                        <fmt:formatNumber value="${item.profit}" type="number" maxFractionDigits="4"/> 원
                    </td>
                </tr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <tr>
                <td colspan="6">보유 중인 코인이 없습니다. 매수(구매)를 먼저 진행해주세요.</td>
            </tr>
        </c:otherwise>
    </c:choose>
    </tbody>
</table>
