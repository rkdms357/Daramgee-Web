<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<main>
    <div class="asset-container">
        <p class="page-title">실시간 코인 시세</p>
        <table class="asset-table">
            <thead>
            <tr>
                <th>코인명</th>
                <th>약어</th>
                <th class="price-col">현재가 (KRW)</th>
                <th>전일 대비</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty assets}">
                    <c:forEach var="asset" items="${assets}">
                        <tr>
                            <td>${asset.name}</td>
                            <td>${asset.symbol}</td>
                            <td class="price-col">
                                <fmt:formatNumber value="${asset.currentPrice}" type="number" minFractionDigits="2" maxFractionDigits="4"/> 원
                            </td>
                            <td class="${asset.changeRate >= 0 ? 'rate-up' : 'rate-down'}">
                                    ${asset.changeRate}%
                            </td>
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
    </div>
</main>
