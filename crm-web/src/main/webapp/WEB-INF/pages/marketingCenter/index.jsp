<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <meta charset=utf-8>
        <title>营销中心</title>
        <%@ include file="/common/common.jsp"%>

            <link href="./static/css/app.3a23a14b64ad2788b4a7bc32d9507503.css" rel=stylesheet>
    </head>

    <body>
        <%@ include file="/WEB-INF/header/top.jsp"%>
            <%@ include file="/WEB-INF/header/header.jsp"%>
                <div id=app></div>
                <script type=text/javascript src="./static/js/manifest.11ea515296d60104d6ac.js"></script>
                <script type=text/javascript src="./static/js/vendor.4909befcf7b03930e404.js"></script>
                <script type=text/javascript src="./static/js/app.3ac4f90844cfb4a3617d.js"></script>
                <input type="hidden" id="hreflink" value="${ctx}">
    </body>

    </html>