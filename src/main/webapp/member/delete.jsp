<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/common/head.jsp" %>
    <title>회원탈퇴</title>
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

        .delete-box {
            background: white;
            width: 360px;
            padding: 40px 30px;
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0,0,0,0.1);
            text-align: center;
        }

        .delete-box p {
            font-size: 15px;
            color: #555;
            margin-bottom: 25px;
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
            width: 100%;
            padding: 12px;
            border-radius: 10px;
            border: 1px solid #ddd;
            font-size: 14px;
            outline: none;
        }

        .input-group input:focus {
            border-color: #ffb300;
        }

        .delete-btn {
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

        .delete-btn:hover {
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
    <div class="delete-box">
        <h1>회원탈퇴</h1>
        <p>계정 보안을 위해 비밀번호를 입력해 주세요.</p>
        <form id="deleteForm" action="${pageContext.request.contextPath}/member/delete" method="post">
            <div class="input-group">
                <label>비밀번호
                    <input id="password" type="password" name="password">
                </label>
            </div>
            <button type="submit" class="delete-btn">회원탈퇴</button>
        </form>
        <script>
            document.querySelector("#deleteForm").addEventListener("submit", async (e) => {
                e.preventDefault();
                const password = document.querySelector("#password").value.trim();
                const msgBox = document.querySelector("#msg");

                if (!password) {
                    msgBox.innerText = "비밀번호를 입력하세요.";
                    return;
                }

                try {
                    const res = await fetch("${pageContext.request.contextPath}/member/delete", {
                        method: "POST",
                        headers: {"Content-Type": "application/x-www-form-urlencoded"},
                        body: "password=" + encodeURIComponent(password)
                    });
                    const data = await res.json();

                    if (data.success) {
                        alert(data.msg);
                        location.href = "${pageContext.request.contextPath}/";
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
