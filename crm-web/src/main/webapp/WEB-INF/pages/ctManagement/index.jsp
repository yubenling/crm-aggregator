<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <meta charset=utf-8>
        <title>客户管理</title>
        <%@ include file="/common/common.jsp"%>

            <link href="./static/css/app.d00a76ec9ca3eed294ed5147473b9132.css" rel=stylesheet>
    </head>

    <body>
        <%@ include file="/WEB-INF/header/top.jsp"%>
            <%@ include file="/WEB-INF/header/header.jsp"%>
                <div id=app></div>
                <script type=text/javascript src="./static/js/manifest.f30bd46db5711d5bb990.js"></script>
                <script type=text/javascript src="./static/js/vendor.068d629a9c1caa961494.js"></script>
                <script type=text/javascript src="./static/js/app.24f912857d52bda0fa35.js"></script>
                <input type="hidden" id="hreflink" value="${ctx}">
    </body>

    </html>