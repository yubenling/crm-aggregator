
$(function(){
    $('.selsectBox').click(function(e){
        $('.nice-select').show();
        e.stopPropagation();
    });
    $('.nice-select li').hover(function(e){
        $('.nice-select li').removeClass('on');
        $(this).addClass('on');
        e.stopPropagation();
    });
    $('.nice-select li').click(function(e){
        $('.nice-select').hide();
        $('.txtVal').text($(this).text());
        e.stopPropagation();
    });
	$(document).click(function(){
		$('.nice-select').hide();
	});	
	
	//店铺信息时间变更
	var oDate=new Date();
	var time=moment(oDate).format('YYYY-MM-DD HH:mm:ss'); 

	$('#nowTime').text(time)
	var url=$('#url').val();
	
	//首页基本数据
	$.ajax({
		url:url+'/home/indexConstant',
		type:'post',
		success:function(data){
			console.log(data);
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('首页基本数据加载失败');
	        	var msgAlertH=$('.msgAlert').height();
	        	var msgAlertW=$('.msgAlert').width();
	        	$('.msgAlert').css({
	        		'margin-left':-msgAlertW/2,
	        		'margin-top':-msgAlertH/2,
	        	});
	        	$('.msgAlert').show();
	        	setTimeout(function(){
	        		$('.msgAlert').hide();
	        	},2000);
			}else{
				
				//店铺会员数
				$('#memberCount').text(res.memberCount);
				//催付汇款金额
				$('#reminderFee').text(res.reminderFee);
				//昨日营销汇款金额
				$('#yesterMemberMoney').text(res.yesterMemberMoney);
				//昨日新增会员数
				$('#yesterdayCreateMember').text(res.yesterdayCreateMember);
				//买家回复数
				$('#receiveCount').text(res.receiveCount);
				//中差评订单数
				$('#badTradeCount').text(res.badTradeCount);
				//昨日整体ROI
				$('#yesterROI').text(res.yesterROI);
				
				//昨日发送短信数
				$('#yesterSmsNum').text(res.yesterSmsNum);
				$('#userId').text(res.userId);
			}
		}
	});
	//消费明细
	$.ajax({
		url:url+'/home/indexTodayConsume',
		type:'post',
		success:function(data){
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('消费明细数据加载失败');
	        	var msgAlertH=$('.msgAlert').height();
	        	var msgAlertW=$('.msgAlert').width();
	        	$('.msgAlert').css({
	        		'margin-left':-msgAlertW/2,
	        		'margin-top':-msgAlertH/2,
	        	});
	        	$('.msgAlert').show();
	        	setTimeout(function(){
	        		$('.msgAlert').hide();
	        	},2000);
			}else{
				//今日发送客户数
				$('#sendCustomerCount').text(res.sendCustomerCount);
				//今日扣除短信条数
				$('#sendSmsCount').text(res.sendSmsCount);
			}
		}
	})
	//首页图表数据
	$.ajax({
		url:url+'/home/indexChart',
		type:'post',
		success:function(data){
			console.log(data);
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('首页图表数据加载失败');
	        	var msgAlertH=$('.msgAlert').height();
	        	var msgAlertW=$('.msgAlert').width();
	        	$('.msgAlert').css({
	        		'margin-left':-msgAlertW/2,
	        		'margin-top':-msgAlertH/2,
	        	});
	        	$('.msgAlert').show();
	        	setTimeout(function(){
	        		$('.msgAlert').hide();
	        	},2000);
			}else{
				var json={
					id:"#yesterdayOrderTimeB",
					hourData:res.hourData,
					tradeNum:res.tradeNum,
					weekTradeNum:res.weekTradeNum,
					monthTradeNum:res.monthTradeNum
				};
				var json2={
					id:"#yesterdayOrderTimeB2",
					dateList:res.dateList,
					weekMoneyList:res.weekMoneyList,
					weekPaymentList:res.weekPaymentList,
				}
				higFn(json);
				higFn2(json2);
			}
		}
	});
	
	//查询软件到期时间和短信剩余条数
	$.ajax({
		url:url+'/home/indexExpirationTime',
		type:'post',
		success:function(data){
			console.log(data);
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('查询软件到期时间和短信剩余条数数据加载失败');
	        	var msgAlertH=$('.msgAlert').height();
	        	var msgAlertW=$('.msgAlert').width();
	        	$('.msgAlert').css({
	        		'margin-left':-msgAlertW/2,
	        		'margin-top':-msgAlertH/2,
	        	});
	        	$('.msgAlert').show();
	        	setTimeout(function(){
	        		$('.msgAlert').hide();
	        	},2000);
			}else{
				$('#userSms').text(res.userSms?res.userSms:0);
				$('.progress span.blue').css({
					width:parseFloat($('#userSms').text()/100000)*100+'%'
				});
				if(res.days!=undefined){
					$('#showInformationTime').text(res.days);
					$('#showInformationTimeType').text('天');
					$('.progress span.red').css({
						width:parseFloat(res.days/365)*100+'%'
					});
				}else if(res.hours!=undefined){
					$('#showInformationTime').text(res.hours);
					$('#showInformationTimeType').text('小时');
					$('.progress span.red').css({
						width:'0%'
					});
				}
				
			}
		}
	});
	//查询快捷入口
	queryConvenientEntrance(url);
	
	//点击快捷入口设置
	$('.shortcutEntranceLinkSetUp').click(function(){
		$('.shortcutEntranceLinkBox').show();
		queryConvenientEntrance(url);
	})
	//点击快捷入口弹框关闭
	$('.shortcutEntranceLinkClose').click(function(){
		$('.shortcutEntranceLinkCheckDiv span').removeClass('on');
		$('.shortcutEntranceLinkBox').hide();
	})
	//快捷入口 多选 效果
    $('.shortcutEntranceLinkCheckDiv span').on('click',function(){
        var xzOnLen=$('.shortcutEntranceLinkCheckDiv span.on').length;
        if($(this).hasClass('on')){
            $(this).removeClass('on');
        }else{
            $(this).addClass('on');
        }
        
    });
    //快捷入口 多选 效果-确定
    $('.shortcutEntranceLinkBtnQd').on('click',function(){
        var xzOnLen=$('.shortcutEntranceLinkCheckDiv span.on').length;
        if(xzOnLen>4){
        	$('.msgAlert').text('最多只能添加4个,请重新添加！');
        	var msgAlertH=$('.msgAlert').height();
        	var msgAlertW=$('.msgAlert').width();
        	$('.msgAlert').css({
        		'margin-left':-msgAlertW/2,
        		'margin-top':-msgAlertH/2,
        	});
        	$('.msgAlert').show();
        	setTimeout(function(){
        		$('.msgAlert').hide();
        	},2000);
        }else{
        	var linkStrArr=[];
        	for(var i=0;i<xzOnLen;i++){
        		linkStrArr.push($('.shortcutEntranceLinkCheckDiv span.on').eq(i).attr('data-num'));
        	}

        	//更新快捷入口
        	$.ajax({
        		url:url+'/home/updateLink',
        		data:{
        			linkStr:linkStrArr.join(',')
        		},
        		type:'post',
        		success:function(data){
        			$('.shortcutEntranceLinkCheckDiv span').removeClass('on');
        			$('.shortcutEntranceLinkBox').hide();
        			console.log(data);
        			var res=$.parseJSON(data);
        			if(res.code!=100){
        				$('.msgAlert').text('更新快捷数据失败！');
        	        	var msgAlertH=$('.msgAlert').height();
        	        	var msgAlertW=$('.msgAlert').width();
        	        	$('.msgAlert').css({
        	        		'margin-left':-msgAlertW/2,
        	        		'margin-top':-msgAlertH/2,
        	        	});
        	        	$('.msgAlert').show();
        	        	setTimeout(function(){
        	        		$('.msgAlert').hide();
        	        	},2000);
        			}else{
        				queryConvenientEntrance(url);
        			
        				
        			}
        		}
        	});
        }
        
    });
    //点击使用帮助效果
    $('.usehelp').click(function(){
        $('#helpBox').show();
        $('#markBg').addClass('on');
    });
    //点击使用帮助关闭效果
    $('.helpBoxClose').click(function(){
        $('#helpBox').hide();
        $('#markBg').removeClass('on');
    });
    //点击使用帮助条例显示隐藏解释
    $('.helpDiv a').click(function(){
        
        if($(this).hasClass('on')){
            $(this).siblings('p').hide();
            $(this).removeClass('on'); 
        }else{
            $(this).siblings('p').show();
            $(this).addClass('on'); 
        }
    })
    //进入首页获取值判断红包是否展示
    if(!sessionStorage.getItem('hongbao')){
    	$.ajax({
    		url:url+'/systemManage/showIndexFrame',
    		type:'post',
    		
    		success:function(data){
    			console.log(data);
    			var res=$.parseJSON(data);
    			if(res.code==100){
    				$('#hongbaoBox').show();
    		    	$('#markBg').addClass('on');
    		    	sessionStorage.setItem('hongbao',1);
    			}else{
    				sessionStorage.setItem('hongbao',1);
    			}
    		}
    	});
    }
    
   
   
    //关闭红包弹框
    $('.hongbaoClose').click(function(){
    	$('#hongbaoBox').hide();
    	$('#markBg').removeClass('on');
    	sessionStorage.setItem('hongbao',1);
    });
    
    //获取验证码
    $('.getCodeClick').click(function(){
    	getCode(url);
    });
    //点击红包弹框确定
    $('.hongbaoQd').click(function(){
    	hongbaoQd(url);
    });
    $('.noticeShow').html('');
    //获取公告列表
    getshowNoticeTitleListData(url);
    
    $('.noticeShow').on('click','a',function(e){
    	var id=$(e.target).attr('data-id');

    	getnoticeTitleData(url,id,e);
    })
    
    //到达率随机变化
    var timer=null;
    timer=setInterval(function(){
    	$('.daodalv').text(sum(98.5,100)+'%');
    	$('.daodalv2').text(sum(98.5,100)+'%');
    },3000);
})

//点击某个公告获取内容
function getnoticeTitleData(url,id,e){
	
	$.ajax({
    	url:url+'/home/showNoticeContent',
    	type:'post',
    	data:{
    		id:id
    	},
    	success:function(data){
    		var res=$.parseJSON(data);
			console.log(res);
			$(e.target).parents('li').siblings('li').find('.noticeTitleBox').hide();
			$(e.target).parents('li').find('.noticeTitleBox').text(res.result);

			if($(e.target).parents('li').find('.noticeTitleBox').is(':hidden')){
				$(e.target).parents('li').find('.noticeTitleBox').show();
				$(e.target).parents('li').find('.noticeTitleBox').text(res.result);
			}else{
				$(e.target).parents('li').find('.noticeTitleBox').hide();
				
			}
    	}
	});
}

//获取公告列表
function getshowNoticeTitleListData(url){
	$.ajax({
    	url:url+'/home/showNoticeTitle',
    	type:'post',
    	success:function(data){
    		var res=$.parseJSON(data);
			console.log(res);
			
    		if(res.code!=100){
    			$('.msgAlert').text('更新公告失败！');
	        	var msgAlertH=$('.msgAlert').height();
	        	var msgAlertW=$('.msgAlert').width();
	        	$('.msgAlert').css({
	        		'margin-left':-msgAlertW/2,
	        		'margin-top':-msgAlertH/2,
	        	});
	        	$('.msgAlert').show();
	        	setTimeout(function(){
	        		$('.msgAlert').hide();
	        	},2000);
    		}else{
    			var len=res.result.length;
    			var list=res.result;

    			for(var i=0;i<len;i++){
    				var aLi=$('<li data-id="'+list[i].id+'"></li>');
    				var aDiv=$('<div class="noticeTitleBox"></div>');
    				aLi.html(
    						'<a data-id="'+list[i].id+'" href="javascript:;" data-id="'+list[i].id+'"><i data-id="'+list[i].id+'" class="fl"></i><span data-id="'+list[i].id+'" class="fl">'+list[i].title+'</span><em data-id="'+list[i].id+'" class="fr">'+list[i].createdDate+'</em></a>'
    						+'<div class="noticeTitleBox"></div>'
    				);
    				$('.noticeShow').append(aLi);
    				 
    			}
    		}
			
		}
    })
}

//获取验证码
function getCode(url){
	var phone=$('#hongbaoPhone').val();
	//判断手机号必需匹配正则
	if (phone.length<11) {
		$('.msgAlert').text('手机号错误！');
    	var msgAlertH=$('.msgAlert').height();
    	var msgAlertW=$('.msgAlert').width();
    	$('.msgAlert').css({
    		'margin-left':-msgAlertW/2,
    		'margin-top':-msgAlertH/2,
    	});
    	$('.msgAlert').show();
    	setTimeout(function(){
    		$('.msgAlert').hide();
    	},2000);
		return;
	}
	var num=60;
	var timer=null;
	if($('.getCodeClick').hasClass('active'))return;
	$('.getCodeClick').addClass('active');
	clearInterval(timer);
	timer=setInterval(function(){
		num--;
		$('.getCodeClick').text(num+'S后重新获取');
		if(num==0){
			clearInterval(timer);
			$('.getCodeClick').text('获取验证码');
			$('.getCodeClick').removeClass('active');
		}
	},1000)
	$.ajax({
		url:url+'/systemManage/indexSecurityCode',
		type:'post',
		data:{
			mobile:phone
		},
		success:function(data){
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('验证码获取失败！');
		    	var msgAlertH=$('.msgAlert').height();
		    	var msgAlertW=$('.msgAlert').width();
		    	$('.msgAlert').css({
		    		'margin-left':-msgAlertW/2,
		    		'margin-top':-msgAlertH/2,
		    	});
		    	$('.msgAlert').show();
		    	setTimeout(function(){
		    		$('.msgAlert').hide();
		    	},2000);
			}
		}
	});
	
}

//红包确定效果
function hongbaoQd(url){
	var code=$('#hongbaoCode').val();
	var phone=$('#hongbaoPhone').val();
	var QQ=$('#hongbaoQQ').val();
	if (phone == null || phone == '') {
		$('.msgAlert').text('手机号码错误！');
    	var msgAlertH=$('.msgAlert').height();
    	var msgAlertW=$('.msgAlert').width();
    	$('.msgAlert').css({
    		'margin-left':-msgAlertW/2,
    		'margin-top':-msgAlertH/2,
    	});
    	$('.msgAlert').show();
    	setTimeout(function(){
    		$('.msgAlert').hide();
    	},2000);
		return false;
	}
	if (isNaN(QQ)) {
		$('.msgAlert').text('QQ号码错误！');
    	var msgAlertH=$('.msgAlert').height();
    	var msgAlertW=$('.msgAlert').width();
    	$('.msgAlert').css({
    		'margin-left':-msgAlertW/2,
    		'margin-top':-msgAlertH/2,
    	});
    	$('.msgAlert').show();
    	setTimeout(function(){
    		$('.msgAlert').hide();
    	},2000);
		return false;
	}
	if (code == null || code == '') {
		$('.msgAlert').text('验证码错误！');
    	var msgAlertH=$('.msgAlert').height();
    	var msgAlertW=$('.msgAlert').width();
    	$('.msgAlert').css({
    		'margin-left':-msgAlertW/2,
    		'margin-top':-msgAlertH/2,
    	});
    	$('.msgAlert').show();
    	setTimeout(function(){
    		$('.msgAlert').hide();
    	},2000);
		return false;
	}
	$.ajax({
		url:url+'/systemManage/saveUserInformation',
		type:'post',
		data:{
			mobile:phone,
			code:code,
			qqNum:QQ
		},
		success:function(data){
			console.log(data);
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('红包添加失败！');
		    	var msgAlertH=$('.msgAlert').height();
		    	var msgAlertW=$('.msgAlert').width();
		    	$('.msgAlert').css({
		    		'margin-left':-msgAlertW/2,
		    		'margin-top':-msgAlertH/2,
		    	});
		    	$('.msgAlert').show();
		    	setTimeout(function(){
		    		$('.msgAlert').hide();
		    	},2000);
			}else{
				$('#hongbaoBox').hide();
		    	$('#markBg').removeClass('on');
			}
		}
	});
}
//查询便捷入口
function queryConvenientEntrance(url){
	
	$.ajax({
		url:url+'/home/listLink',
		type:'post',
		success:function(data){
			console.log(data);
			var res=$.parseJSON(data);
			if(res.code!=100){
				$('.msgAlert').text('查询便捷入口数据失败！');
		    	var msgAlertH=$('.msgAlert').height();
		    	var msgAlertW=$('.msgAlert').width();
		    	$('.msgAlert').css({
		    		'margin-left':-msgAlertW/2,
		    		'margin-top':-msgAlertH/2,
		    	});
		    	$('.msgAlert').show();
		    	setTimeout(function(){
		    		$('.msgAlert').hide();
		    	},2000);
			}else{
				if(res.data){
					var arr=res.data.split(',');
					var len=arr.length;
					var str='';
					var href='';
					var rukou=$('.shortcutEntranceLinkCheckDiv span');
					for(var i=0;i<rukou.length;i++){
						for(var j=0;j<len;j++){
							if(rukou.eq(i).attr('data-num')==arr[j]){
								rukou.eq(i).addClass('on');
							}
						}
					}
					$('.shortcutEntranceBottom .ulTop li').html('');
					$('.shortcutEntranceBottom .ulBottom li').html('');
					for(var i=0;i<len;i++){
						
						switch(Number(arr[i]))
						{
							case 1:
								str='下单关怀';
								href='/tradeSetup/index#/orderCare/orderCareIndex'
							break;
							case 2:
								str='常规催付'
								href='/tradeSetup/index#/promptCare/promptCareIndex'
							break;
							case 3:
								str='二次催付'
								href='/tradeSetup/index#/promptCare/TwoPayment?type=3'
							break;
							case 4:
								str='聚划算催付'
								href='/tradeSetup/index#/promptCare/JuhuasuanReminder?type=4'
							break;
							case 6:
								str='发货提醒'
								href='/tradeSetup/index#/logisticsReminder/shippingReminderIndex'
							break;
							case 13:
								str='付款关怀'
								href='/tradeSetup/index#/paymentConcern/paymentConcernIndex'
							break;
							case 11:
								str='延时发货提醒'
								href='/tradeSetup/index#/logisticsReminder/delayedShipmentReminderIndex'
							break;
							case 7:
								str='到达同城提醒'
								href='/tradeSetup/index#/logisticsReminder/arrivalCityReminderIndex'
							break;
							case 8:
								str='派件提醒'
								href='/tradeSetup/index#/logisticsReminder/sendRemindersIndex'
							break;
							case 9:
								str='签收提醒'
								href='/tradeSetup/index#/logisticsReminder/signReminderIndex'
							break;
							case 12:
								str='宝贝关怀'
								href='/tradeSetup/index#/babyCare/babyCareIndex'
							break;
							case 14:
								str='回款提醒'
							    href='/tradeSetup/index#/payment/paymentRemindIndex'
							break;
							case 15:
								str='退款关怀'
								href='/tradeSetup/index#/refundCare/applyForRefundReminderSetUp?type=29'
							break;
							case 16:
								str='自动评价'
								href='/tradeSetup/index#/evaluationManagement/automaticEvaluation?type=16'
							break;
							case 19:
								str='中差评管理'
								href='/tradeSetup/index#/evaluationManagement/middleSchoolReviewMonitor?type=20'
							break;
							case 33:
								str='会员短信群发'
								href='/msgSend/index#/marketingCenter/memberSMSGroup'
							break;
							case 34:
								str='指定号码群发'
								href='/msgSend/index#/marketingCenter/specifiedNumber'
							break;
							case 35:
								str='订单短信群发'
								href='/msgSend/index#/marketingCenter/orderSMSGroup'
							break;
							case 37:
								str='好评提醒'
								href='/tradeSetup/index#/praiseRemind/praiseRemindSetUp?type=37'
							break;
							case 38:
								str='短信账单'
								href='/shopData/index#/backstageManagement/smsbill'
							break;
						}
						if(i<=1){
							$('.shortcutEntranceBottom .ulTop li').eq(i).html(
								'<a href="'+url+href+'">'	
								+'<div><i class="icon'+arr[i]+'"></i></div>'
								+'<p>'+str+'</p>'
								+'</a>'
							)
						}else{
							
							$('.shortcutEntranceBottom .ulBottom li').eq(i-2).html(
								'<a href="'+url+href+'">'
								+'<div><i class="icon'+arr[i]+'"></i></div>'
								+'<p>'+str+'</p>'
								+'</a>'
							)
						}
						
					}
				}
			}
		}
	});
}

function higFn(json){
	var thisWidth=$(json.id).width();
	var thisHeight=$(json.id).height();
	var tradeNum=json.tradeNum;
	var hourData=json.hourData;
	var weekTradeNum=json.weekTradeNum?json.weekTradeNum:[];
	var monthTradeNum=json.monthTradeNum?json.monthTradeNum:[];
	if(!tradeNum.length){
		for(var i=0;i<24;i++){
			tradeNum.push(sum(20,50));
			weekTradeNum.push(sum(50,100));
			monthTradeNum.push(sum(100,150));
			hourData.push(i<10?('0'+i+':00'):(i+':00'))
		}
	}
	$(json.id).highcharts({
        chart: {
            type: 'area',
        	width:thisWidth-20,
            height:thisHeight
        },
        title: {
            text: ''
        },			       
        xAxis: {
            categories:hourData,
                
            crosshair: true
        },
        legend:{
            align:'right',
            verticalAlign: 'top'
        },
        credits: {
            enabled:false
        },
        yAxis: {
            min: 0,
            title: {
                text: ''
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y} 个</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.1,
                borderWidth: 0
            },
            area:{
            	fillOpacity: 0.1,
            }
            
        
        },
        series: [{
            name: '昨日下单数',
            color: '#00cf94',
            data: tradeNum
        },
        {
            name: '近七天下单数',
            color: '#1cc6ff',
            data: weekTradeNum
        },
        {
            name: '近30天下单数',
            color: '#9d67fd',
            data: monthTradeNum
        },]
    });
}


function higFn2(json){
	var thisWidth=$(json.id).width();
	var thisHeight=$(json.id).height();
	var dateList=json.dateList;
	var weekMoneyList=json.weekMoneyList;
	var weekPaymentList=json.weekPaymentList;
	if(!weekMoneyList.length&&!weekPaymentList.length){
		for(var i=0;i<7;i++){
			tradeNum.push(100);
			hourData.push(i<10?('0'+i+':00'):(i+':00'))
		}
	}
	$(json.id).highcharts({
        chart: {
        	type: 'area',
        	width:thisWidth-20,
            height:thisHeight
        },
        title: {
            text: ''
        },			       
        xAxis: {
            categories:json.dateList,
                
            crosshair: true
        },
        legend:{
            align:'right',
            verticalAlign: 'top'
        },
        credits: {
            enabled:false
        },
        yAxis: {
            min: 0,
            title: {
                text: ''
            }
        },
        tooltip: {
            headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
            pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
            '<td style="padding:0"><b>{point.y:.2f} 元</b></td></tr>',
            footerFormat: '</table>',
            shared: true,
            useHTML: true
        },
        plotOptions: {
            column: {
                pointPadding: 0.1,
                borderWidth: 0
            },
            area:{
            	fillOpacity: 0.1,
            }
        },
        series: [{
            name: '短信消费金额',
            color: '#e9816d',
            data: json.weekMoneyList
        },{
            name: '客户下单金额',
            color: '#49a6f8',
            data: json.weekPaymentList
        }]
    });
}

//随机数生成
function sum (m,n){
    var num = Math.random()*(m - n) + n;
    var reg=/.*\..*/;
    if(reg.test(num)){
        num=num.toFixed(1);
    }   
    return num;
}