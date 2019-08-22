<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

	<link rel="stylesheet" type="text/css" href="${ctx}/css/index.css"/>
<%-- 	<%@ include file="/WEB-INF/header/top.jsp"%> --%>
	<!-- <div id="douSevenBannerBox">
		<a href="http://sms.idwee.cn/sms/" target="_blank">
			
		</a>
	</div> -->
	<div class="indexHead clearfix">
		<div class="logobox fl">
			<a href="${ctx}/home/index">
				<img src="${ctx}/images/logo.png"/>	
			</a>
		</div>
		<div class="headNav fl">
			<a class="on" href="${ctx}/home/index">首页</a>
			<a href="${ctx}/member/index">客户管理</a>
			<a href="${ctx}/tradeSetup/index">订单中心</a>
			<a href="${ctx}/msgSend/index">营销中心</a>
 			<a href="${ctx}/analysis/index">数据分析</a>

			<a href="${ctx}/shopData/index">后台管理</a>
			
<%-- 			<a href="${ctx}/shopData/index#/backstageManagement/smsSendRecord">短信发送记录</a> --%>

		</div>
<!-- 		<div class="userBox fl"> -->
			
<!-- 			<div class="userNameBox fl"> -->
<!-- 				<span>肉肉</span> -->
<!-- 			</div> -->
<!-- 			<i class="userTx"></i> -->
<!-- 		</div> -->
	</div>
			

<script>
	$(function(){
		var href=window.location.href;
		$('.headNav a').removeClass('on');
		if(href.indexOf('/home/')>0||href.indexOf('/login/testIndex')>0){
			$('.headNav a').eq(0).addClass('on');	
		}else if(href.indexOf('/member/')>0){
			$('.headNav a').eq(1).addClass('on');
		}else if(href.indexOf('/tradeSetup/')>0){
			$('.headNav a').eq(2).addClass('on');
		}else if(href.indexOf('/msgSend/')>0){
			$('.headNav a').eq(3).addClass('on');
		}else if(href.indexOf('/shopData/')>0){
			$('.headNav a').eq(4).addClass('on');
		}
	})
</script>
