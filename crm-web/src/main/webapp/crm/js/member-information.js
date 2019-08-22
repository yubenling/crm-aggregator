$(function(){
	
	/*下拉框*/
	$('[name="nice-select"]').click(function(e){
		$('[name="nice-select"]').find('ul').hide();
		$(this).find('ul').show();
		e.stopPropagation();
	});
	$('[name="nice-select"] li').hover(function(e){
		$(this).toggleClass('on');
		e.stopPropagation();
	});
	$('[name="nice-select"] li').click(function(e){

		var val = $(this).text();
		var dataVal = $(this).attr("value");
		$(this).parents('[name="nice-select"]').find('p').text(val);
		$(this).parents('[name="nice-select"]').find('input').val(dataVal);
		$('[name="nice-select"] ul').hide();
		e.stopPropagation();
	});
	$(document).click(function(){
		$('[name="nice-select"] ul').hide();
	});	
	$('[name="nice-select"]').each(function(index){
		$(this).css({
			zIndex:100-index
		})
	});
	
	//会员信息开始
	//累计消费金额 对比大小
	$('#minTradePrice,#maxTradePrice').on('blur',function(){
		zhengze('minTradePrice','maxTradePrice');
	});
	
	//交易次数对比大小
	$('#minTradeNum,#maxTradeNum').on('blur',function(){
		zhengze('minTradeNum','maxTradeNum');
	});
	//平均客单价对比大小
	$('#minAvgPrice,#maxAvgPrice').on('blur',function(){
		zhengze('minAvgPrice','maxAvgPrice');
	});
	//会员信息结束
	
	//客户信息详情开始
	//客户信息详情客户信息修改
	$('.customer-detailsDiv .xg').on('click',function(){

		var thisTDheight=$('.modify').eq(0).height();
		$('.customer-detailsDiv .modify').addClass('on');
		$('.customer-detailsDiv .modify div input[type="text"]').css({
			width:'100%',
			height:thisTDheight
		});
		$('.customer-detailsDiv .modify>div').css({
			width:'100%',
			height:thisTDheight
		});
		
		$(this).addClass('on');
		$(this).siblings('.bc').addClass('on');
		
	});
	
	//客户信息详情客户信息保存
	$('.customer-detailsDiv .bc').on('click',function(){
		
	});
	
	//点击其他地址显示弹框
	$('.other-address').on('click',function(){
		$('#other-addressBox').show();
		$('.mask').show();
		$(document.body).css({
			'overflow-x':'hidden',
			'overflow-y':'hidden'
		});
	});
	
	//点击其他地址弹框关闭效果
	$('#other-addressBox .close').on('click',function(){
		$('#other-addressBox').hide();
		$('.mask').hide();
		$(document.body).css({
			'overflow-x':'',
			'overflow-y':''
		});
	});
	
	//添加备注弹框输入文字后计算
	var textNum=120;
	$('.addRemarksTxt textarea').keyup(function(){
		var thisNum=$(this).val().length;
		var thisVal=$(this).val();
		if(thisNum>=textNum){
			$('.addRemarksTxt p span').text('0');
			$('.addRemarksTxt p em').text('120');
			$(this).val(thisVal.substring(0,120));
		}else{
			$('.addRemarksTxt p span').text(textNum-thisNum);
			$('.addRemarksTxt p em').text(thisNum);
			
		}
		
	});
	
	//点击备注弹框确定后效果
	
	$('.addRemarksBtn .qd').on('click',function(){
		if($('.customer-detailRemarks table tr').length>1){
			var trIndex=$('.customer-detailRemarks table tr').eq($('.customer-detailRemarks table tr').length-1).attr('value');
			
		}else{
			var trIndex=0;
		}
		trIndex++;
		var textVal=$('.addRemarksTxt textarea').val();
		
		if(textVal==''){
			$('.tishi_2').show();
			setTimeout(function(){
				$('.tishi_2').hide();
			},2000);
		}else{
			var str=$('<tr value="'+trIndex+'"><td class="copsell"><p>'+textVal+'</p></td><td>'+timeDate()+'</td><td class="remarks"><a class="modify" href="javscript:;">修改</a><a  class="remove" href="javscript:;"> 删除</a></td></tr>');
			$('#addRemarksBox').hide();
			$('.customer-detailRemarks table').append(str);
			$('.addRemarksTxt p span').text('120');
			$('.addRemarksTxt p em').text('0');
			$('.addRemarksTxt textarea').val('');
			$('.mask').hide();	
			$(document.body).css({
				'overflow-x':'',
				'overflow-y':''
			});
		}
		
	});
	//点击备注弹框取消后效果
	$('.addRemarksBtn .qx').on('click',function(){
		$('.addRemarksTxt p span').text('120');
		$('.addRemarksTxt p em').text('0');
		$('.addRemarksTxt textarea').val('');
		$('#addRemarksBox').hide();
		$('.mask').hide();
		$(document.body).css({
			'overflow-x':'',
			'overflow-y':''
		});
	});
	
	//点击添加备注弹出弹框
	$('.addRemarks').on('click',function(){
		$('#addRemarksBox').show();
		$('.mask').show();	
		$(document.body).css({
			'overflow-x':'hidden',
			'overflow-y':'hidden'
		});
	});
	
	//点击备注 删除 弹框
	$('.customer-detailRemarks').on('click','.remove',function(){
		var thisVal=$(this).parents('tr').attr('value');
		$('#removeRemarks').show();	
		$('#removeRemarks input').val(thisVal);
		$('.mask').show();	
		$(document.body).css({
			'overflow-x':'hidden',
			'overflow-y':'hidden'
		});
	});
	
	//点击备注删除弹框 确定效果
	$('#removeRemarks .qd').on('click',function(){
		var removeVal=$('#removeRemarks input').attr('value');
		$('#removeRemarks').hide();
		$('.mask').hide();
		$(document.body).css({
			'overflow-x':'',
			'overflow-y':''
		});
		$('.customer-detailRemarks table tr[value="'+removeVal+'"]').remove();
	});
	
	//点击备注删除 取消效果
	$('#removeRemarks .qx').on('click',function(){

		$('#removeRemarks').hide();
		$('.mask').hide();
		$(document.body).css({
			'overflow-x':'',
			'overflow-y':''
		});

	});
	
	//备注 点击修改弹窗
	$('.customer-detailRemarks').on('click','.modify',function(){
		var thisVal=$(this).parents('tr').attr('value');
		var thisTxt=$(this).parents('tr').children('td').eq(0).text();
		var thisLen=$.trim($(this).parents('tr').children('td').eq(0).text()).length;
		$('#modifyRemarksBox').show();	
		$('#modifyRemarksBox input').val(thisVal);
		$('.modifyRemarksTxt textarea').val($.trim(thisTxt));
		$('.modifyRemarksTxt p span').text(120-thisLen);
		$('.modifyRemarksTxt p em').text(thisLen);
		$('.mask').show();	
		$(document.body).css({
			'overflow-x':'hidden',
			'overflow-y':'hidden'
		});
	});
	
	//备注 修改弹窗 文本框输入 计算
	$('.modifyRemarksTxt textarea').keyup(function(){
		var thisNum=$(this).val().length;
		var thisVal=$(this).val();
		if(thisNum>=textNum){
			$('.modifyRemarksTxt p span').text('0');
			$('.modifyRemarksTxt p em').text('120');
			$(this).val(thisVal.substring(0,120));
		}else{
			$('.modifyRemarksTxt p span').text(textNum-thisNum);
			$('.modifyRemarksTxt p em').text(thisNum);
			
		}
		
	});
	
	//备注 修改备注弹窗 确定效果
	$('.modifyRemarksBtn .qd').on('click',function(){
		
		var textVal=$('.modifyRemarksTxt textarea').val();
		var modifyRemarksVal=$('#modifyRemarksBox input').attr('value');
		if(textVal==''){
			$('.tishi_2').show();
			setTimeout(function(){
				$('.tishi_2').hide();
			},2000);
		}else{
			$('.customer-detailRemarks table tr[value="'+modifyRemarksVal+'"] td:eq(0) p').text(textVal);
			$('.customer-detailRemarks table tr[value="'+modifyRemarksVal+'"] td:eq(1)').text(timeDate());
			
			$('.modifyRemarksTxt textarea').val('');
			$('.modifyRemarksTxt p span').text('120');
			$('.modifyRemarksTxt p em').text('0');
			$('.mask').hide();	
			$('#modifyRemarksBox').hide();
			$(document.body).css({
				'overflow-x':'',
				'overflow-y':''
			});
		}
		
	});
	
	//备注 修改备注弹窗 取消效果
	$('.modifyRemarksBtn .qx').on('click',function(){
		$('.modifyRemarksTxt textarea').val('');
		$('.modifyRemarksTxt p span').text('120');
		$('.modifyRemarksTxt p em').text('0');
		$('.mask').hide();	
		$('#modifyRemarksBox').hide();
		$(document.body).css({
			'overflow-x':'',
			'overflow-y':''
		});
		
		
	});
	
	$('.customer-detailRemarks').on('click','.copsell p',function(){
		if($(this).parent().hasClass('on')){
			$(this).parent().removeClass('on');
		}else{
			$(this).parent().addClass('on');
		}
		
	});
	
	$('.tdnine p').on('click',function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on');
		}else{
			$(this).addClass('on');
		}
		
	});
	//客户信息详情结束
	
	//订单信息详情开始
	//点击商家发送内容展开收起
	$('.customer-responseBox table tr td p').on('click',function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on');
		}else{
			$(this).addClass('on');
		}
	});
	//点击删除弹出弹出框
	$('.customer-responseTable tr td .close').on('click',function(){
		var thisParentsVal=$(this).parents('tr').attr('value');
		$('#removecustomer-responseBox input').val(thisParentsVal);
		$('#removecustomer-responseBox').show();
		$('.mask').show();
	});
	//点击删除弹出框确定效果
	$('#removecustomer-responseBox .qd').on('click',function(){
		var thisInputVal=$('#removecustomer-responseBox input').val();
		$('#removecustomer-responseBox').hide();
		$('.mask').hide();
		$('.customer-responseTable tr[value="'+thisInputVal+'"]').remove();
	});
	
	//点击删除弹出框取消效果
	$('#removecustomer-responseBox .qx').on('click',function(){
		
		$('#removecustomer-responseBox').hide();
		$('.mask').hide();

	});
	//订单信息详情结束
	
	
	
	$('.transaction-timeInput-zdy').on('click',function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$(this).text('自定义');
			$(this).siblings('.nice-select').show();
			$(this).siblings('.transaction-timeInputBox-v').hide();
		}else{
			$(this).siblings('.transaction-timeInputBox-v').find('input').val('');
			$(this).addClass('on');
			$(this).text('取消');
			$(this).siblings('.nice-select').hide();
			$(this).siblings('.transaction-timeInputBox-v').show();
		}
	});
	
	//生日点击效果
	$('#text01').on('click',function(){
		$.jeDate(
			'#text01',
			{
				insTrigger:false,
				isTime:true,
				format:'YYYY-MM-DD',
				maxDate:$.nowDate({DD:0})
			}
		);
	});
	
});



//正则表达式
function zhengze(value1, value2) {
	var val1 = $("#" + value1).val();
	var val2 = $("#" + value2).val();
	val1 = Number(val1);
	val2 = Number(val2);
	if (val2 != null && val2 != "" && val1 > val2) {
		
		$(".tishi_2").show();
		$(".tishi_2").children("p").text("请输入正确的数据！")
		setTimeout(function() {
			$(".tishi_2").hide()
		}, 1500)

		// 		alert("请输入正确的数据！");
		$("#" + value1).val(null);
		$("#" + value2).val(null);
	}

}

//保存验证邮箱性别生日年龄微信QQ职业
function verificationsome(){
	var bOk=true;
	//邮箱
	var mailBox=$('#email').val();
	//生日
	var BirthdayVal=$('#text01').val();
	//年龄
	var ageVal=$('#age').val();
	//微信
	var weixinVal=$('#wechat').val();
	//qq
	var qqVal=$('#qq').val();
	//职业
	var OccupationVal=$('#occupation').val();
	//邮箱验证
	var mailBoxReg=/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/;
	//职业验证
	var occupationReg=/^[\u2E80-\u9FFF]+$/;
	if(mailBox!=''){
		if(!mailBoxReg.test(mailBox)){
			bOk=false;
			$(".tishi_2").children("p").text("邮箱格式错误");
		}
	}else{
		bOk=true;
	}
	/*if(mailBox==''||!mailBoxReg.test(mailBox)){
		bOk=false;
		$(".tishi_2").children("p").text("邮箱格式错误");
	}else if(!occupationReg.test(OccupationVal)){
		bOk=false;
		$(".tishi_2").children("p").text("职业格式错误");
	}else{
		bOk=true;
	}*/
	return bOk;
}

//计算时间
function timeDate(){
	var oDate=new Date();
	var oYear=oDate.getFullYear();
	var oMon=oDate.getMonth()+1;
	var oDateDay=oDate.getDate();
	var oH=oDate.getHours();
	var oM=oDate.getMinutes();
	var oS=oDate.getSeconds();
	return oYear+'-'+oMon+'-'+oDateDay+'  '+toDube(oH)+':'+toDube(oM)+':'+toDube(oS);
}

function toDube(str){
	
	return str>=10?''+str:'0'+str;
}
