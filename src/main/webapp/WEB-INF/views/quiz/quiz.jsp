<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<div class="quiz-card ${isSolved ? 'solved-state' : ''}">
    <h3>🐿️ 오늘의 투자 퀴즈</h3>
    <c:if test="${not empty quizResult}">
        <div class="quiz-msg ${fn:contains(quizResult, '정답') ? 'success' : 'fail'}">
                ${quizResult}
        </div>
    </c:if>
    <c:choose>
        <c:when test="${not empty quiz}">
            <p><strong>Q.</strong> ${quiz.question}</p>
            <form action="${pageContext.request.contextPath}/quiz" method="post">
                <input type="hidden" name="quizId" value="${quiz.quizId}">
                <input type="hidden" name="realAnswer" value="${quiz.answer}">
                <div class="options-grid">
                    <c:forEach var="opt" items="${quiz.options}" varStatus="status">
                        <button type="submit" name="userAnswer" value="${status.count}" class="option-btn">
                                ${status.count}. ${opt}
                        </button>
                    </c:forEach>
                </div>
            </form>
        </c:when>
        <c:otherwise>
            <p class="quiz-msg info">
                    ${msg}
            </p>
        </c:otherwise>
    </c:choose>
</div>