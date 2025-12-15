<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>회원가입</title>
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

        .join-box {
            background: white;
            width: 360px;
            padding: 40px 30px;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            text-align: center;
        }

        .join-box h1 {
            margin: 0 0 10px;
            font-size: 26px;
            font-weight: 800;
        }

        .input-group {
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

        .join-btn {
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

        .join-btn:hover {
            opacity: 0.9;
        }

        .msg {
            margin-top: 15px;
            color: #e53935;
            font-size: 13px;
        }
    </style>
</head>
<body>
<%@ include file="/WEB-INF/views/common/header.jsp"%>
<main>
    <div class="join-box">
        <h1>회원가입</h1>
        <form id="joinForm" action="${pageContext.request.contextPath}/member" method="post">
            <input type="hidden" name="action" value="join">
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
            <button type="submit" class="join-btn">회원가입</button>
        </form>
        <script>
            document.querySelector("#joinForm").addEventListener("submit", async (e) => {
                e.preventDefault();
                const userId = document.querySelector("#userId").value.trim();
                const password = document.querySelector("#password").value.trim();
                const msgBox = document.querySelector("#msg");

                if (!userId || !password) {
                    msgBox.innerText = "아이디와 비밀번호를 입력하세요.";
                    return;
                }

                try {
                    const res = await fetch("${pageContext.request.contextPath}/member/join", {
                        method: "POST",
                        headers: {"Content-Type": "application/x-www-form-urlencoded"},
                        body: "userId=" + encodeURIComponent(userId)
                            + "&password=" + encodeURIComponent(password)
                    });
                    const data = await res.json();

                    if (data.success) {
                        alert(data.msg);
                        location.href = "${pageContext.request.contextPath}/member/login";
                    }
                    else {
                        msgBox.innerText = data.msg;
                    }
                } catch (err) {
                    msgBox.innerText = "서버와 통신 중 오류가 발생했습니다.";
                    console.error(err);
                }
            });
        </script>
        <div id="msg" class="msg"></div>
    </div>
</main>
</body>
</html>