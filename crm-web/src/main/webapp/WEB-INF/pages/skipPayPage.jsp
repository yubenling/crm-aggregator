<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/common/common.jsp"%>
<script type="text/javascript" src="${ctx}/js/jquery-2.2.3.min.js"></script>
</head>
<body>
	<input type="hidden" id="totalAmount" value="${totalAmount}">
	<input type="hidden" id="rechargeNum" value="${rechargeNum}">
</body>
<script>
$.ajax({
	url:'${ctx}/backstage/skipPay',
	type:'post',
	data:{
		totalAmount:$('#totalAmount').val(),
		rechargeNum:$('#rechargeNum').val()
	},
	success : function(data) {
		var json = $.parseJSON(data);
		if(json.code==100){
			$('body').append(json.result);
		}else{
			alert(json.msg)
			setTimeout(function(){
				window.close();
			},5000);
		}
	}
});
</script>
</html>
