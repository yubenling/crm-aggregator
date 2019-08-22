$(function(){
	//手机号 客户名称选中
	$('.blacklistDiv i').on('click',function(){
		$('.blacklistBtn p').removeClass('error');
		$('.blacklistDiv i').removeClass('on');
		$(this).addClass('on');
		$('.blacklistprompt span').text($(this).siblings('span').text());
		if($(this).siblings('span').text()=='客户昵称'){
			$('.blacklist-textarea textarea').attr({
				placeholder:'客云1，客云2'
			});
			$('.blacklist-textarea textarea').removeClass('phone')
		}else{
			$('.blacklist-textarea textarea').attr({
				placeholder:'13811111111，13811111112,13811111113'
			});
			$('.blacklist-textarea textarea').addClass('phone')
		}
	});
	
	//验证手机号是否正确
	$('.blacklistBtnBox .qd').on('click',function(){
		var phoneArr=$('.blacklist-textarea textarea').val().split(',');
		if($('.blacklist-textarea textarea').hasClass('phone')){
			checkMobile(phoneArr);
		}
	});
	
	//点击取消按钮效果
	$('.blacklistBtnBox .qx').on('click',function(){
		$('.blacklistBox').hide();
		$('.blacklist-textarea textarea').val('');
		$('.blacklistDiv i').removeClass('on');
		$('.blacklistDiv i').eq(0).addClass('on');
		$('.blacklistprompt span').text('手机号');
		$('.blacklist-textarea textarea').attr({
			placeholder:'13811111111，13811111112,13811111113'
		});
		$('.mark').removeClass('on');
		$(document.body).css({
		   "overflow-x":"",
		   "overflow-y":""
		 });
	});
	
	
	//手机号客户昵称选中
	$('.batchblacklistscreenCheck i').on('click',function(){
		$('.batchblacklistscreenCheck i').removeClass('on');
		$(this).addClass('on');

		if($(this).siblings('span').text()=='手机号'){

			$('.batchblacklistP div span').text('手机号');
			$('#batchblacklistType').val('1');
		}else{

			$('.batchblacklistP div span').text('客户昵称');
			$('#batchblacklistType').val('2');
		}
	});
	
	//批量上传模板移入移出效果
	$('.batchblacklistbtnScShow').mouseover(function(){
		$(this).siblings().addClass('on');
	});
	$('.batchblacklistbtnScShow').mouseout(function(){
		$(this).siblings().removeClass('on');
	});
	
	//点击关闭关闭弹窗
	$('.batchblacklistBtn a').on('click',function(){
		$('.batchblacklistBox').hide();
		$('.batchblacklistscreenCheck i').removeClass('on');
		$('.batchblacklistscreenCheck i').eq(0).addClass('on');
		$('.batchblacklistP div span').text('手机号');
		$('#type').val('1');
		$('.blacklistBox').hide();
		$('.mark').removeClass('on');
		$(document.body).css({
		   "overflow-x":"",
		   "overflow-y":""
		 });
	});
	
	//点击详情弹窗效果
	$('.details').on('click',function(){
		if($('#batchblacklistType').attr('value')=='1'){
			$('.phonedetails').addClass('on');
		}else{
			$('.namedetails').addClass('on');
		}
		
	});
	
	//点击详情弹窗关闭效果
	$('.detailsBtn a').on('click',function(){
		if($('#batchblacklistType').val()=='1'){
			$('.phonedetails').removeClass('on');
		}else{
			$('.namedetails').removeClass('on');
		}

	});
	//点击删除弹窗效果
	$('.delete').on('click',function(){
		$('#deleteBox').addClass('on');
	});
	//点击删除弹框取消效果
	$('#deleteBox .deleteBtn .qx').on('click',function(){
		$('#deleteBox').removeClass('on');
	});
	//点击删除弹框取消效果
	$('#reomveblacklistBox .deleteBtn .qx').on('click',function(){
		$('#reomveblacklistBox').hide();
		$('.mark').removeClass('on');
	});
	//点击删除弹框取消效果
	$('#emptyblacklistBox .deleteBtn .qx').on('click',function(){
		$('#emptyblacklistBox').hide();
		$('.mark').removeClass('on');
	});
	
	//点击短信黑名单全选效果
	$('.blacklistqx').on('click',function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$('.blacklistdx').removeClass('on');
		}else{
			$(this).addClass('on');
			$('.blacklistdx').addClass('on');
		}
	});
	
	//点击短信黑名单单选效果
	$('.blacklistIcon').on('click','.blacklistdx',function(){
		var num=0;
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$('.blacklistqx').removeClass('on');
		}else{
			$(this).addClass('on');
			$('.blacklistdx').each(function(index){
				if($(this).hasClass('on')){
					num++;
				}
				
			});
			if(num==$('.blacklistdx').length){
				$('.blacklistqx').addClass('on');
			}
		}
	});
	
	//点击添加黑名单
	$('.addblacklist').on('click',function(){
		$('.blacklistBox').show();
		$('.mark').addClass('on');
		$(document.body).css({
		   "overflow-x":"hidden",
		   "overflow-y":"hidden"
		 });
	});
	
	$('.batchblacklist').on('click',function(){
		$('.batchblacklistBox').show();
		$('.mark').addClass('on');
		$(document.body).css({
		   "overflow-x":"hidden",
		   "overflow-y":"hidden"
		 });
	});
	
	//点击移除黑名单效果
	$('.removeblacklist').on('click',function(){
		$('#reomveblacklistBox').show();
		$('.mark').addClass('on');
		$(document.body).css({
		   "overflow-x":"hidden",
		   "overflow-y":"hidden"
		 });
	});
	
	//点击清空黑名单效果
	$('.emptyblacklist').on('click',function(){
		$('#emptyblacklistBox').show();
		$('.mark').addClass('on');
		$(document.body).css({
		   "overflow-x":"hidden",
		   "overflow-y":"hidden"
		 });
	});
});


//验证手机号
function checkMobile(str){ 
	if(str.length>1){
		for(var i=0;i<str.length;i++){
			if(!(/^1[3|4|5|7|8][0-9]{9}$/.test(str[i]))){ 
		        $('.blacklistBtn p').addClass('error');
		        return false; 
		    }else{
		    	$('.blacklistBtn p').removeClass('error');
		    }
		}
		
	}else{
		if(!(/^1[3|4|5|7|8][0-9]{9}$/.test(str))){ 
	        $('.blacklistBtn p').addClass('error');
	        return false; 
	    }else{
	    	$('.blacklistBtn p').removeClass('error');
	    }
	}
    
} 