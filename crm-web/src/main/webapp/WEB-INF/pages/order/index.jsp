<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
    <html>

    <head>
        <meta charset=utf-8>
        <title>订单中心</title>
        <%@ include file="/common/common.jsp"%>

            <link href="./static/css/app.06f318daf64557d512a897aa6626b3a9.css" rel=stylesheet>
    </head>

    <body>
        <%@ include file="/WEB-INF/header/top.jsp"%>
            <%@ include file="/WEB-INF/header/header.jsp"%>
                <div id=app></div>
                <script type=text/javascript src="./static/js/manifest.558faee05e59ecb27f8f.js"></script>
                <script type=text/javascript src="./static/js/vendor.b0dcf074c411cbd79dff.js"></script>
                <script type=text/javascript src="./static/js/app.81ae109a05ccb7d03230.js"></script>
                <input type="hidden" id="hreflink" value="${ctx}">
    </body>

    </html>