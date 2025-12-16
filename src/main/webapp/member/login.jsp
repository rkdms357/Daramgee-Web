<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>로그인</title>
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

        .login-box {
            background: white;
            width: 360px;
            padding: 40px 30px;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            text-align: center;
        }

        .login-box h1 {
            margin: 0 0 10px;
            font-size: 26px;
            font-weight: 800;
        }

        .input-group {
            width: 100%;
            text-align: left;
            margin-bottom: 15px;
        }

        .input-group label {
            display: block;
            font-size: 13px;
            color: #555;
            margin-bottom: 5px;
        }

        .input-group input {
            width: 93%;
            padding: 12px;
            border-radius: 10px;
            border: 1px solid #ddd;
            font-size: 14px;
            outline: none;
        }

        .input-group input:focus {
            border-color: #ffb300;
        }

        .login-btn {
            width: 100%;
            padding: 14px;
            border: none;
            border-radius: 12px;
            background: #ffb300;
            color: white;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            margin-top: 10px;
        }

        .login-btn:hover {
            opacity: 0.9;
        }

        .msg {
            margin-top: 15px;
            color: #e53935;
            font-size: 13px;
        }

        .join-link {
            margin-top: 25px;
            font-size: 14px;
        }

        .join-link a {
            color: #ff9800;
            font-weight: bold;
            text-decoration: none;
        }

        .join-link a:hover {
            text-decoration: underline;
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
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp" %>
<main>
    <div class="login-box">
        <h1>로그인</h1>
        <form id="loginForm" action="${pageContext.request.contextPath}/member/login" method="post">
            <div class="input-group">
                <label>아이디
                    <input id="userId" type="text" name="userId" value="${userId}">
                </label>
            </div>
            <div class="input-group">
                <label>비밀번호
                    <input id="password" type="password" name="password">
                </label>
            </div>
            <button type="submit" class="login-btn">로그인</button>
        </form>
        <script>
            document.querySelector("#loginForm").addEventListener("submit", async (e) => {
                e.preventDefault();
                const userId = document.querySelector("#userId").value.trim();
                const password = document.querySelector("#password").value.trim();
                const msgBox = document.querySelector("#msg");

                if (!userId || !password) {
                    msgBox.innerText = "아이디와 비밀번호를 입력하세요.";
                    return;
                }

                try {
                    const res = await fetch("${pageContext.request.contextPath}/member/login", {
                        method: "POST",
                        headers: {"Content-Type": "application/x-www-form-urlencoded"},
                        body: "userId=" + encodeURIComponent(userId)
                            + "&password=" + encodeURIComponent(password)
                    });
                    const data = await res.json();

                    if (data.success) location.href = "${pageContext.request.contextPath}";
                    else msgBox.innerText = data.msg;
                } catch (err) {
                    msgBox.innerText = "서버와 통신 중 오류가 발생했습니다.";
                    console.error(err);
                }
            });
        </script>
        <div id="msg" class="msg"></div>
        <div class="join-link">
            아직 회원이 아니신가요?
            <a href="${pageContext.request.contextPath}/member/join">회원가입</a>
        </div>
    </div>
</main>
</body>
</html>