// JavaScript Document


$(function(){
	var timer=null;
	$(".ChongZhi1").click(function(){
		var i=$(this).index();				
		 	$(".ChongZhi2").hide();
		 	$(".ChongZhi2").eq(i).show();
			$(this).addClass("bgc_e3e7f0").removeClass("bgc_fff");
			$(this).siblings().removeClass("bgc_e3e7f0").addClass("bgc_fff");
	});
	$('.contentDiv ul li').click(function(){
		$('.contentDiv ul li').removeClass('on');
		$(this).addClass('on');
		$('.btn .zf').removeClass('on');
		clearInterval(timer);
	});
	$('.btn .qx').click(function(){
		$('#customRechargeBox').hide();
		$('.markBg').hide();
		$('.zfberweimaBox').removeClass('active');
		clearInterval(timer);
		$('#customRechargeBox .zf').removeClass('on');
		$('.contentDivUl li').removeClass('on');
		$('.contentDivUl li').eq(0).addClass('on');
	});
	
	$('#czje').keyup(function(){
		
		this.value=(parseInt((this.value=this.value.replace(/\D/g,''))==''?'0':this.value,10))
		var num;
		if($(this).val()<=200){
			num=Math.ceil(parseFloat($(this).val()/0.055));
		}else if($(this).val()>200&&$(this).val()<=1000){
			num=Math.ceil(parseFloat($(this).val()/0.05));
		}else if($(this).val()>1000&&$(this).val()<=1500){
			num=Math.ceil(parseFloat($(this).val()/0.048));
		}else if($(this).val()>1500&&$(this).val()<=2500){
			num=Math.ceil(parseFloat($(this).val()/0.047));
		}else if($(this).val()>2500&&$(this).val()<=4500){
			num=Math.ceil(parseFloat($(this).val()/0.046));
		}else if($(this).val()>4500&&$(this).val()<=10000){
			num=Math.ceil(parseFloat($(this).val()/0.045));
		}else if($(this).val()>10000&&$(this).val()<=20000){
			num=Math.ceil(parseFloat($(this).val()/0.04));
		}else if($(this).val()>20000&&$(this).val()<=40000){
			num=Math.ceil(parseFloat($(this).val()/0.038));
		}else if($(this).val()>40000){
			num=Math.ceil(parseFloat($(this).val()/0.037));
		}
		

		$('#czts').text(num);
	});
	$('#czts').keyup(function(){
		this.value=this.value.replace(/\D/g,'');
		var num=Number(($(this).val()*5)/100);
		if($(this).val()<=3637){
			num=Number(($(this).val()*55)/1000);
		}else if($(this).val()>3637&&$(this).val()<=20000){
			num=Number(($(this).val()*50)/1000);
		}else if($(this).val()>20000&&$(this).val()<=31250){
			num=Number(($(this).val()*48)/1000);
		}else if($(this).val()>31250&&$(this).val()<=53192){
			num=Number(($(this).val()*47)/1000);
		}else if($(this).val()>53192&&$(this).val()<=97827){
			num=Number(($(this).val()*46)/1000);
		}else if($(this).val()>97827&&$(this).val()<=222223){
			num=Number(($(this).val()*45)/1000);
		}else if($(this).val()>222223&&$(this).val()<=500000){
			num=Number(($(this).val()*40)/1000);
		}else if($(this).val()>500000&&$(this).val()<=1052632){
			num=Number(($(this).val()*38)/1000);
		}else if($(this).val()>1052632){
			num=Number(($(this).val()*37)/1000);
		}
		var str;
		if(num.toString().indexOf('.')==-1){
			str=num;
		}else{
			str=num.toString().substring(0,num.toString().indexOf('.')+3)
		}
		
		$('#czje').val(str);
	});
	
	$('.customRechargeCz').click(function(){
		if($('#czje').val()==0||$('#czje').val()==''){
			$('#error').css({
				width:'320px',
				marginLeft:'-160px',
				'z-index':'1000'
			})
			$('#error').show();
			$('#error').text('自定义充值金额或充值条数不能为空！');
			setTimeout(function(){
				$('#error').hide();
			},2000);
		}else{
			$('#customRechargeBox').show();
			$('.markBg').show();
			$('#smsMoney').text($('#czje').val());

			$('.smsNum').text($("#czts").text());
		}
	});	
	
//	$('.zf').click(function(){
//		if($('.contentDivUl li').eq(0).hasClass('on')){
//			$('.zfberweimaBox').addClass('active');
//		}
//	});
	var url=$('#urlVal').val();
	
	$('#customRechargeBox .zf').click(function(){
		if($(this).hasClass('on'))return;
		$(this).addClass('on');
		if($('.contentDivUl li').eq(0).hasClass('on')){
			$.ajax({
				url:url+'/aliPay/qrCodePay',
				type:'post',
				data:{
					totalAmount:$('#smsMoney').text(),
					rechargeNum:$('.smsNum').text()
				},
				success : function(data) {

					if(data==''){
						if($('.contentDivUl li').eq(0).hasClass('on')){
							$('.zfberweimaBox').addClass('active');
							$('.ewmBox').html('二维码获取失败！');
						}
					}else{
						if($('.contentDivUl li').eq(0).hasClass('on')){
							var oImg=$('<img>');
							$('.zfberweimaBox').addClass('active');
							$('.ewmBox').html('');
							$('.ewmBox').append(oImg);
							$('.ewmBox img').prop({
								src:'/qrCodeImage/'+data
							});
						}
						var num=data.substring(data.indexOf('/')+1,data.length-4);
						var oldtime=new Date().getTime();
						clearInterval(timer);
						timer=setInterval(function(){
							var nowtime=new Date().getTime();
							$.ajax({
								url:url+'/aliPay/payStatus',
								type:'post',
								data:{
									payTrade:num
								},
								success : function(data) {
									console.log(data);
									
									if(data==1){
										clearInterval(timer);
										$('.ewmBox img').prop({
											src:url+'/crm/images/chenggongicon.png'
										});
										
									}else if(data==2){
										clearInterval(timer);
										$('.ewmBox img').prop({
											src:url+'/crm/images/shibaiicon.png'
										});
										
									}
								}
							})
							if(nowtime-oldtime>3600000){
								
								clearInterval(timer);
							}
						},2000)
					}
					
				}
			})
		}else if($('.contentDivUl li').eq(1).hasClass('on')){
			$('#customRechargeBox .zf').removeClass('on');
//			window.location.href =$('#urlVal').val()+"/aliPay/skipPayPage?totalAmount="+$('#smsMoney').text()+"&rechargeNum="+$('.smsNum').text();
			window.open($('#urlVal').val()+"/aliPay/skipPayPage?totalAmount="+$('#smsMoney').text()+"&rechargeNum="+$('.smsNum').text());  
//			sessionStorage.setItem('totalAmount', $('#smsMoney').text());
//			sessionStorage.setItem('rechargeNum', $('.smsNum').text());
//			$.ajax({
//				url:url+'/aliPay/skipPay',
//				type:'post',
//				data:{
//					totalAmount:$('#smsMoney').text(),
//					rechargeNum:$('.smsNum').text()
//				},
//				success : function(data) {
//					console.log(data.data);
//					if(data.data){
//						var str=data.data;
//						var str1=data.data.replace('<script>document.forms[0].submit();','<script id="formpost">document.forms[0].submit();document.forms[0].remove();document.getElementById("formpost").remove()');
//						$('body').append(str1);
//						$('#customRechargeBox .zf').removeClass('on');
//					}else{
//						$('#error').css({
//							width:'130px',
//							marginLeft:'-65px',
//							'z-index':'1000'
//						})
//						$('#error').show();
//						$('#error').text('充值失败!');
//						setTimeout(function(){
//							$('#error').hide();
//							$('#customRechargeBox .zf').removeClass('on');
//						},2000);
//					}
//					
//					
//				}
//			});
			
		}
		
	});
	$('.jfgzshow').mouseover(function(){
		$(this).siblings('.jfgzBox').show();
	});
	$('.jfgzshow').mouseout(function(){
		$(this).siblings('.jfgzBox').hide();
	});
})
			


/* $(function(){
            $("#change_inside div:eq(0)").show();
            $("#change_outside div").each(function(index){
                $(this).click(function(){
                        $("#page_content div").hide();
                     $("#page_content div:eq("+index+")").show();
             })              
            })
    });*/











































