<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <meta charset=utf-8>
        <title>后台管理</title>
        <%@ include file="/common/common.jsp"%>

            <link href="./static/css/app.63d3b18470615b4c8dfb1725c986f8de.css" rel=stylesheet>
    </head>

    <body>
        <%@ include file="/WEB-INF/header/top.jsp"%>
            <%@ include file="/WEB-INF/header/header.jsp"%>
                <div id=app></div>
                <script type=text/javascript src="./static/js/manifest.e0dd113bd6ca83e8502a.js"></script>
                <script type=text/javascript src="./static/js/vendor.b0e0662b01c9d944ae97.js"></script>
                <script type=text/javascript src="./static/js/app.5fd3d9306f4fa7dd4cab.js"></script>
                <input type="hidden" id="hreflink" value="${ctx}">
    </body>

    </html>