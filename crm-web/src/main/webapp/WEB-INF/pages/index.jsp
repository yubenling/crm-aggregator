<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>首页</title>
<%@ include file="/common/common.jsp"%>


<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript" src="${ctx}/js/index.js"></script>
</head>
<body>
	<!--公共头部区域开始-->
	<%@ include file="/WEB-INF/header/header.jsp"%>
	<%@ include file="/WEB-INF/header/top.jsp"%>
	
	<!--公共头部区域结束-->
	<div id="app">
			
				
<!-- 			<div id="banner"> -->
<!-- 			</div> -->
			<div id="content" class="clearfix">
				<div class="contentLeft fr">
					<div class="contentLeftTop clearfix">
						<div class="storeData fl">
							<div class="shortcutEntranceTop clearfix">
								<div class="title fl">
									<i class="cIcon"></i>
									<span>数据概览</span>
								</div>
								
							</div>
							<div class="storeBottom">
								<ul class="ulTop clearfix">
									<li class="border-dee">
										<strong class="clearfix"><i id="reminderFee"></i><em>元</em></strong>
										<span title="获取固定时间段内（昨日0 - 24小时），通过催付提醒（常规、二次、聚划算）设置生效任务发送的短信支付的订单总金额">昨日催付回款金额</span>
									</li>
									<li class="border-dee">
										<strong class="clearfix"><i id="yesterMemberMoney"></i><em>元</em></strong>
										<span title="获取固定时间段内（昨日0 - 24小时），通过营销中心设置生效任务发送的短信支付订单总金额">昨日营销回款金额</span>
									</li>
									<li>
										<strong class="clearfix"><i id="yesterROI"></i></strong>
										<span title="昨日短信消费金额：昨日回款金额">昨日整体ROI</span>
									</li>
								</ul>
								<ul class="ulBottom clearfix">
									<li class="border-dee">
										<strong class="clearfix"><i id="yesterdayCreateMember"></i><em>个</em></strong>
										<span title="获取固定时间段内（昨日0 - 24小时），以不同会员昵称为判断条件，店铺内首次创建订单的总人数">昨日新增客户数</span>
									</li>
									<li class="border-dee">
										<strong class="clearfix"><i id="receiveCount"></i><em>条</em></strong>
										<span title="昨日短信发送回复总数（不含TD）">昨日客户短信回复数</span>
									</li>
									<li>
										<strong class="clearfix"><i id="badTradeCount"></i><em>笔</em></strong>
										<span title="昨日评价为中差评的订单总数">昨日中差评订单数</span>
									</li>
								</ul>
							</div>
						</div>
						<div class="shortcutEntrance fr">
							<div class="shortcutEntranceTop clearfix">
								<div class="title fl">
									<i class="cIcon"></i>
									<span>快捷入口</span>
								</div>
								<div class="shortcutEntranceLink fr">
									<i></i>
									<a href="javascript:;" class="shortcutEntranceLinkSetUp">设置</a>
									<div class="shortcutEntranceLinkBox">
										<span class="shortcutEntranceLinkClose"></span>
										<h5 class="shortcutEntranceLinkTitle">您可以根据需要选择快速入口，最多可以选择4个</h5>
										<div class="shortcutEntranceLinkCheckBox clearfix">
											<div class="shortcutEntranceLinkCheckBoxLeft fl">
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="1"></span>
													<label class="fl">下单关怀</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="9"></span>
													<label class="fl">签收提醒</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="15"></span>
													<label class="fl">退款关怀</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="37"></span>
													<label class="fl">好评提醒</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="2"></span>
													<label class="fl">常规催付</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="13"></span>
													<label class="fl">付款关怀</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="12"></span>
													<label class="fl">宝贝关怀</label>
												</div>
											</div>
											<div class="shortcutEntranceLinkCheckBoxCenter fl">
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="16"></span>
													<label class="fl">自动评价</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="38"></span>
													<label class="fl">短信账单</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="3"></span>
													<label class="fl">二次催付</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="6"></span>
													<label class="fl">发货提醒</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="8"></span>
													<label class="fl">派件提醒</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="14"></span>
													<label class="fl">回款提醒</label>
												</div>
												
											</div>
											<div class="shortcutEntranceLinkCheckBoxRight fl">
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="19"></span>
													<label class="fl">中差评管理</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="4"></span>
													<label class="fl">聚划算催付</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="11"></span>
													<label class="fl">延时发货提醒</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="35"></span>
													<label class="fl">订单短信群发</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="7"></span>
													<label class="fl">发货同城提醒</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="33"></span>
													<label class="fl">会员短信群发</label>
												</div>
												<div class="shortcutEntranceLinkCheckDiv clearfix">
													<span class="fl" data-num="34"></span>
													<label class="fl">指定号码群发</label>
												</div>
														
											</div>
										</div>
										<div class="shortcutEntranceLinkBtn">
											<a href="javascript:;" class="shortcutEntranceLinkBtnQd">确定</a>
										</div>
									</div>
								</div>
							</div>	
							<div class="shortcutEntranceBottom">
								<ul class="ulTop clearfix">
									<li class="border-dee" id="kj1">
										
									</li>
									<li id="kj2">
										
									</li>
									
								</ul>
								<ul class="ulBottom clearfix">
									<li class="border-dee" id="kj3">
										
									</li>
									<li id="kj4">
										
									</li>
									
								</ul>
							</div>
						</div>	
					</div>		
					<div class="contentLeftBottom">
						<div class="title clearfix">
							<i class="fl cIcon "></i>
							<h4 class="fl">客户下单时间表</h4>
						</div>	
						<div id="yesterdayOrderTimeB" class="higgchartHeight">
							
						</div>
					</div>
					<div class="contentLeftBottom2">
						<div class="title clearfix">
							<i class="fl cIcon "></i>
							<h4 class="fl">近7天短信消费金额和客户下单金额数据表</h4>
						</div>	
						<div id="yesterdayOrderTimeB2" class="higgchartHeight">
							
						</div>
					</div>
				</div>	
				<div class="contentRight fl">
					<div class="storeInformation">
						<div class="shortcutEntranceTop clearfix">
							<div class="title fl">
								<i class="cIcon"></i>
								<span>店铺信息</span>
							</div>
							
						</div>
						<h3 id="userId"></h3>
						<div class="showInformation">
							<div class="showInformationTop clearfix">
								<p class="fl">软件剩余时间：<span id="showInformationTime"></span><i id="showInformationTimeType">天</i></p>
								<a class="fr" href="https://fuwu.taobao.com/ser/detail.htm?service_code=FW_GOODS-1952286&selected_item_code=FW_GOODS-1952286-1&redirect=1" target="_blank">续费</a>
							</div>	
							<div class="progress"><span class="red"></span></span></div>
						</div>
						<div class="showInformation clearfix">
							<div class="showInformationTop">
								<p class="fl">短信剩余条数：<span id="userSms"></span>条</p>
								<a class="fr" href="${ctx}/shopData/index#/backstageManagement/rechargeRenewals">充值</a>
							</div>	
							<div class="progress"><span class="blue"></span></span></div>
						</div>
						<div class="storeTop clearfix">
							<div class="storeMembersBox fl">
								<span>目前店铺会员共：<strong id="memberCount"></strong>个</span>	
								
							</div>
							<div class="lookOrder fr">
	
								<a href="/shopData/index#/backstageManagement/historyOrderImport">人数不准？导入历史订单</a>
							</div>
						</div>	
					</div>
					<div class="SMSBox">
						<div class="smsMoney">
							<div class="shortcutEntranceTop clearfix">
								<div class="title fl">
									<i class="cIcon"></i>
									<span>消费明细</span>
								</div>
								<div class="shortcutEntranceLink fr">
									<i></i>
									<a href="${ctx}/shopData/index#/backstageManagement/smsbill">详情</a>
								</div>
							</div>
							<ul class="smsDetailed clearfix">
								<li class="border-dee">
									<strong class="clearfix"><i id="sendCustomerCount"></i><em>个</em></strong>
									<span title="今日短信已发送会员总人数">今日发送客户数</span>
								</li>
								<li>
									<strong class="clearfix"><i id="sendSmsCount"></i><em>条</em></strong>
									<span title="根据短信计费规则计算今日短信已发送总条数">今日扣除短信条数</span>
								</li>
							</ul>
						</div>
						<div class="smsPassageway">
							<div class="shortcutEntranceTop clearfix smsPassagewayTitleBox">
								<div class="title fl">
									<i class="cIcon"></i>
									<span>通道信息</span>
								</div>
								<div class="shortcutEntranceLink fr smsPassagewayTitle">
									<span id="nowTime"></span>
									
								</div>
							</div>
							<div class="smsPassagewayDiv marketingCenterBox">
								<h3 class="clearfix"><i class="fl"></i><span class="fl">营销中心</span></h3>	
								<ul class="clearfix">
									<li class="marginX">短信通道：<span>正常</span></li>
									<li class="marginX">到达率：<span class="daodalv">98%</span></li>
									<li>延迟<span>3</span>秒</li>
								</ul>
							</div>
							<div class="smsPassagewayDiv marketingCenterBox">
								<h3 class="clearfix"><i class="fl"></i><span class="fl">订单中心</span></h3>	
								<ul class="clearfix">
									<li class="marginX">短信通道：<span>正常</span></li>
									<li class="marginX">到达率：<span class="daodalv2">98%</span></li>
									<li>延迟<span>3</span>秒</li>
								</ul>
							</div>
						</div>
					</div>
					<div class="notice">
						<div class="shortcutEntranceTop clearfix">
							<div class="title fl">
								<i class="cIcon"></i>
								<span>公告</span>
							</div>
							<div class="shortcutEntranceLink fr">
								<i class="wenhao"></i>
								<a href="javascript:;" class="usehelp">使用帮助</a>
							</div>
						</div>
						<ul class="noticeShow">
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
							<li>
								<a href="javascript:;">
									<i class="fl"></i>
									<span class="fl">会员分组</span>
									<em class="fr">2016-06-28</em>
								</a>
							</li>
						</ul>
					</div>
					
				</div>	
			</div>
			
			<div id="helpBox">
			
			<h3 class="title">使用帮助<i class="helpBoxClose"></i></h3>
			<div class="helpBox">
				<div class="helpDiv">
					<a href="javascript:;"><i></i>客户交易额和交易量是累计的吗？</a>
					<p>是的，客户的交易额和交易量都是累计的。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>这些客户是开店以来的吗？</a>
					<p>购买软件后我们自动同步3个月的订单，这些客户是3个月内的，3个月之前的客户需要您手动导入历史订单。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>支持自定义客户分组吗？</a>
					<p>支持的，软件里有系统分组，也支持自定义客户分组。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>自定义创建好的分组可以直接发送短信吗？</a>
					<p>可以。直接在客户分组——会员营销操作，系统自动跳转会员短信群发页面。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>客户分组客户数量为什么一直不变？</a>
					<p>长时间未登录软件，系统有些数据不会自动更新。亲这边只需要点击分组信息当中的刷新按钮，客户数据就会自动更新过来。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>可以设置会员黑名单吗？</a>
					<p>可以。在【客户管理】--【黑名单管理】界面，可以查看到黑名单管理功能。设置黑名单以后，在卖家发送任何短信的时候，后台数据库自动过滤掉黑名单客户，不向黑名单中的手机号码发送短信。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>短信内容支持自定义吗？一条短信多少个字？</a>
					<p>可以。一条短信70个字，但是一条短信内容超过70个字每条短信按照67个字进行计费，例如 一条短信70个字 计费1条，一条短信71个字计费2条，一条短信140个字计费3条 一条短信134个字计费2条；其中汉字、英文字母、阿拉伯数字、标点符号均算作一个字。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>短信发送的号码是什么开头的？ 到达率如何？</a>
					<p>移动、电信、联通三网都是106号码开头，到达率98%以上，不到达也不计费。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>可以过滤中差评客户吗？</a>
					<p>可以，系统自动过滤近半年中差评客户。如果有不想发的客户，也可以添加到短信黑名单中，短信群发自动过滤不发。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>为什么筛选群发的客户那么少？</a>
					<p>新购买软件的客户，需要等待淘宝推送订单数据。此时数据未全面导入，还需要客户进行等待。因为淘宝不提供3个月前订单数据，所以在未导入历史订单的情况下，系统只显示近三个月的客户。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>营销短信发送注意事项</a>
					<p>短信内容中打折、促销、包邮、秒杀等词汇尽量少些，群发之前可以先测试下。注意短信发送时间，避免打扰买家休息。短信群发尽量在活动前2~3天发送，确保短信送达，做活动预热。如有不想发送短信的客户，可提前加入短信黑名单，并选择过滤黑名单。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>营销短信支持定时发送吗？</a>
					<p>可以。在【营销中心】--【会员短信群发】界面，可以查看到定时发送功能。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>可以添加自己号码做短信测试吗？</a>
					<p>可以。短信群发之前，可在【营销中心】--【指定号码发送】这边，输入号码，做短信测试。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>短信余额预警的手机号在哪里更改？</a>
					<p>可在【后台管理】--【手机号设置】这边，点击修改，输入号码，确定修改。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>如何开启短信剩余量提醒？</a>
					<p>当软件里面的短信余额为0，正常的营销短信发送会直接停止。进入【后台管理】--【手机号设置】可以实时提醒短信剩余数量。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>短信发送的号码是什么开头的？ 到达率如何？</a>
					<p>移动、电信、联通三网都是106号码开头，到达率98%以上，不到达也不计费。</p>
				</div>
				<div class="helpDiv">
					<a href="javascript:;"><i></i>订购软件后短信什么时候到账？</a>
					<p>购买之后短信是立即到账，若一次性充值短信量比较大，可以联系软件客服咨询价格。特别注意：软件服务一旦到期，即使有剩余短信在软件中，软件也是不可使用的！这种情况下，亲可以续费软件，剩余短信还存在于软件，可继续正常使用。</p>
				</div>
			</div>
		
		</div>
		<div id="hongbaoBox">
			<div class="hongbao">
				<div class="hongbaoClose"></div>
				<div class="hongbaoFormBox">
					<div class="hongbaoFormDiv clearfix">
						<label>手机号</label>
						<input type="text" name="" id="hongbaoPhone">
					</div>
					<div class="hongbaoFormDiv clearfix">
						<label>验证码</label>
						<input type="text" name="" id="hongbaoCode" style="width: 1.3rem;">
						<a href="javascript:;" class="getCodeClick">获取验证码</a>
					</div>
					<div class="hongbaoFormDiv clearfix">
						<label>QQ号</label>
						<input type="text" name="" id="hongbaoQQ">
					</div>
				</div>
				<div class="hongbaoBtn">
					<a href="javascript:;" class="hongbaoQd"></a>
				</div>
			</div>	
		</div>
		<div id="markBg" class=""></div>
		
		</div>
<!-- 		<!-- <div id="alertBannerBox"> -->
<!-- 			<i class="alertBannerClose"></i> -->
<!-- 			<a href="http://sms.idwee.cn/sms/ " target="_blank"> -->
			
<!-- 			</a> -->
<!-- 		</div> --> 
<!-- 		<div id="markBg2" class=""></div> -->
</body>
<script>
$(function(){
	
})

$('#request1').click(function(){
	var url = "/crm-web/effect/index";
	$.ajax({
		url : url,
		type : "post",
// 		params : "",
		success : function(data) {
			alert(11111);
			alert(data);
		},
		dataType : 'json'
	});
})

$('#request2').click(function(){
	var url = "/crm-web/home/sumData";
	$.ajax({
		url : url,
		type : "post",
		params : "",
		success : function(data) {
			alert(11111);
			alert(data);
		},
		dataType : 'json'
	});
})
function check() {  
     var excel_file = $("#excel_file").val();  
     if (excel_file == "" || excel_file.length == 0) {  
         alert("请选择文件路径！");  
         return false;  
     } else {  
        return true;  
     }  
} 
    
$(document).ready(function () {  
       var msg="";  
       if($("#importMsg").text()!=null){  
           msg=$("#importMsg").text();  
       }  
       if(msg!=""){  
           alert(msg);  
       }  
});
/* $(function(){
	var bOk=sessionStorage.getItem('alertBoK');
	if(bOk==1)return;
	
	$('#alertBannerBox').show();
	$('#markBg2').show();
	$('#alertBannerBox .alertBannerClose').on('click',function(){
		$('#alertBannerBox').hide();
		$('#markBg2').hide();
		sessionStorage.setItem('alertBoK','1');
	});
}) */
</script>
</html>