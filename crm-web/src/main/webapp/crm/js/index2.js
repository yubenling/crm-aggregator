$(function(){
	
//	if(!sessionStorage.getItem('key_a')){
//		alert(1);
//	}
//	sessionStorage.setItem('key_a','1');
//	$.ajax({
//		url : '${ctx}/member/indexData',
//		type : "get",
//		success : function(data) {
//			console.log(data);
//		},
//		dataType : 'json'
//	});
//	//订单数
//	var totalTrade=parseInt(new Number($('#totalTrade').val()).valueOf());
//	//待付款订单数
//	var waitPayTrade=parseInt(new Number($('#waitPayTrade').val()).valueOf());
//	//已付款订单数
//	var succPayTrade=parseInt(new Number($('#succPayTrade').val()).valueOf());
//	//待发货订单数
//	var waitConsignTrade=parseInt(new Number($('#waitConsignTrade').val()).valueOf());
//	//退款中订单数
//	var refundTrade=parseInt(new Number($('#refundTrade').val()).valueOf());
//	//已付款金额
//	var succPayment=parseInt(new Number($('#succPayment').val()).valueOf());
//	//本月短信消费金额
//	var monthSmsMoney=new Number($('#monthSmsMoney').val()).valueOf();
//	//本月催付挽回金额
//	var monthReminderMoney=new Number($('#monthReminderMoney').val()).valueOf();
//	//本月营销金额
//	var monthMemberMoney=new Number($('#monthMemberMoney').val()).valueOf();
//	//今天短信消费金额
//	var todaySmsMoney=new Number($('#todaySmsMoney').val()).valueOf();
//	//今天催付挽回金额
//	var todayReminderMoney=new Number($('#todayReminderMoney').val()).valueOf();
//	//今天会员营销金额
//	var todayMemberMoney=new Number($('#todayMemberMoney').val()).valueOf();
//	//昨日短信消费金额
//	var yesterSmsMoney=new Number($('#yesterSmsMoney').val()).valueOf();
//	//昨日催付挽回金额
//	var yesterReminderMoney=new Number($('#yesterReminderMoney').val()).valueOf();
//	//昨日会员营销金额
//	var yesterMemberMoney=new Number($('#yesterMemberMoney').val()).valueOf();
	//本店铺会员数
	var memberCount=new Number($('#memberCount').val()).valueOf();

	
	
	
	
	
	
	
	
	
	
	
	
//	var settingType=$('#settingType').val();
//	var settingTypeArr=settingType.split(',');
//
//	for(var i=0;i<settingTypeArr.length;i++){
//		if(settingTypeArr[i]=='1'){
//			$('.xiadanguanhuai').addClass('on');
//		}
//		if(settingTypeArr[i]=='2'){
//			$('.changguicuifu').addClass('on');
//		}
//		if(settingTypeArr[i]=='13'){
//			$('.fukuanguanhuai').addClass('on');
//		}
//		if(settingTypeArr[i]=='9'){
//			$('.qianshoutixing').addClass('on');
//		}
//		if(settingTypeArr[i]=='12'){
//			$('.baobeiguanhuai').addClass('on');
//		}
//		if(settingTypeArr[i]=='14'){
//			$('.huikuantixing').addClass('on');
//		}
//		if(settingTypeArr[i]=='20'||settingTypeArr[i]=='21'){
//			$('.zhongchapingguanli').addClass('on');
//		}
//	}
	autoTab();
	wzgd();
	jQuery(".member-roll").slide({mainCell:"ul",autoPlay:true,effect:"topMarquee",vis:5,interTime:20,trigger:"click"});
	
});








function wzgd(){
	clearInterval(time1);
	var time1=setInterval("autoScroll('#system-announcement-roll')",2000);
	
}
function autoScroll(obj){  
	$(obj).find("ul").animate({  
		marginTop : "-39px"  
	},500,function(){  
		$(this).css({marginTop : "0px"}).find("li:first").appendTo(this);  
	})  
}  
function autoTab(){
	//系统公告图片轮播
	var iNow=0;
	clearInterval(time2);
	var time2=setInterval(function(){
		iNow++;
		if(iNow==2){
			iNow=0;
		}
		$('.system-announcementTab ul li').removeClass('on');
		$('.system-announcementTab ul li').eq(iNow).addClass('on');
	},3000);
}


