
		$(function(){
			
			
			
			
			/*下拉框*/
			$('[name="nice-select"]').click(function(e){
				if($(this).hasClass('disabled'))return;
				$('[name="nice-select"]').find('ul').hide();
				$(this).find('ul').show();
				e.stopPropagation();
			});
			$('[name="nice-select"] li').hover(function(e){
				
				$(this).toggleClass('on');
				e.stopPropagation();
			});
			$('[name="nice-select"] li').click(function(e){

				if($(this).parents('.member-grouping-left').children('label').text()=='会员分组 :'){
					if($(this).text()!='所有分组'){

						$('.nice-select').each(function(index){
							if(index>0){
								if(index!=3){
									$('.nice-select').eq(index).addClass('disabled');
									$('.nice-select').eq(index).children('p').text($('.nice-select').eq(index).children('ul').children('li').eq(0).text())
								}
								
							}
						});
						$('.select-a').addClass('disabled');
						$('.cityalerta').addClass('disabled');
						$('.member-alerta').addClass('disabled');
						$('.serch-tj').addClass('disabled');
						$('#region').val('');
						$('#selectArea').text('自定义');
						$('#appoint_ItemId').text('自定义');
						$('#itemIds').val('');
						$('.memberAlli').addClass('on');
						$('.cityscreen').addClass('on');
						$('.serch-tj').removeClass('on');
						$('.serch-tj').siblings('#blackStatus').val('0');
						$('#customerSource').val('0');
						$('#orderStatus').val('');
						$('#lastestSend').val('');
						$('#tradeTimeByDay').val('');
						$('#tradeNumByTimes').val('');
						$('#minTradePrice').val('0');
						$('#maxTradePrice').val('0');
						$('#minAvgPrice').val('0');
						$('#maxAvgPrice').val('0');
					}else{
						$('.select-a').removeClass('disabled');
						$('.cityalerta').removeClass('disabled');
						$('.member-alerta').removeClass('disabled');
						$('.serch-tj').removeClass('disabled');
						$('.nice-select').removeClass('disabled');
						$('#groupIdNum').val('');
						
					}
				}
				
				var val = $(this).text();
				var dataVal = $(this).attr("data-value");
				$(this).parents('[name="nice-select"]').find('input').val(val);
				$('[name="nice-select"] ul').hide();
				e.stopPropagation();
			});
			$(document).click(function(){
				$('[name="nice-select"] ul').hide();
			});
			
			
			
			$(".AddSpecified").click(function(){
				// 禁止滚动条
				$(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".ChoiceSpecified").show();
			});
			$(".SpecifiedOut").click(function(){
				// 启用滚动条
			 $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".ChoiceSpecified").hide();	
			});
/*			$(document).on("click", ".VIPGXK", function () {
				$(this).toggleClass("bgc_check_blue");
				$(this).parent().siblings().find(".ALLVIPGXK").removeClass("bgc_check_blue");
			});*/
            //获取复选框
			var $bt=$(".fuxuankuang");
			
            //给按钮绑定点击事件
            $bt.click(function () {
            	var btContent=$(this).hasClass("bgc_check_blue");
            	if(btContent){
            		//所有的单选框的状态都要变成被选中
            		$(".VIPGXK").each(function () {
            			$(this).removeClass("bgc_check_blue");
            			$(".zhixianshishangjia").removeClass("bgc_check_blue");
            		});
                }else{
                	//所有的单选框的状态都要变成不被选中
                	$(".VIPGXK").each(function () {
                		$(this).addClass("bgc_check_blue");
                		$(".zhixianshishangjia").removeClass("bgc_check_blue");
                	});
                }
            });
            //单选
			$(document).on("click", ".item_check_div", function () {
			 	var a = $(".item_table").children().find(".bgc_check_blue").length;
			 	var b = $(".item_table").children().find("tr").length*1-1;
				if($(this).hasClass("bgc_check_blue")){
					$(this).removeClass("bgc_check_blue");
					$(".item_table").children().find(".fuxuankuang").removeClass("bgc_check_blue");
				}else{
					if(   a+1==b   ){
						$(this).addClass("bgc_check_blue");
						$(".item_table").children().find(".fuxuankuang").addClass("bgc_check_blue");
					}else{
						$(this).addClass("bgc_check_blue");
					}
				};
				
			});
			
		$(document).on("click", ".zhixianshishangjia", function () {
			if(!$(this).hasClass("bgc_check_blue")){
				$(".zhixianshishangjia").addClass("bgc_check_blue");
			}else{
				$(".zhixianshishangjia").removeClass("bgc_check_blue");
			}
		});
			
			
			
			
			/*店铺数据选项切换*/
			$(".out").click(function(){
				var i=$(this).index();				
				 	$(".in").hide();
				 	$(".in").eq(i).show();
				$(this).addClass("bgc_e3e7f0");
				$(this).siblings().removeClass("bgc_e3e7f0");
				$(".one_check_").hide();
				$(".one_check_only_").hide();
				$(".group_check_").hide();
				$("input").val("")
			});
			
			/*选项切换*/
			$(".short_out").click(function(){
				var i=$(this).index();				
				 	$(".short_in").hide();
				 	$(".short_in").eq(i).show();
			});
			
		
			
			
			/*table背景色*/
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/*table高度，行高*/
			$("tr").css("height","2.6vw");
			
		
			
			
			
			
			$(".all_check").click(function(){
				$(this).children(".one_check_").toggle();
				/*$("td div .one_check_").toggle();*/
				if($(this).children(".one_check_").css("display")=='inline'){
					$("td div .one_check_").css("display","inline")
				}else{
					$("td div .one_check_").css("display","none")
				}
				
			});
			
			$(".all_can_check").click(function(){
				$(this).children(".all_can_check_").toggle();
			});
			
			
			$(".set_time").click(function(){
				$(".set_time_").show();
			});
			$(".set_time_none").click(function(){
				$(".set_time_").hide();
			});
			
			$(".detail_btn").click(function(){
				$(".detail").toggle();
			});
			
			
			
			
		/*	$(".one_check").click(function(){
				$(this).children(".one_check_").toggle();
			});*/
			$(".one_check_only").click(function(){
				$(this).children(".one_check_").toggle();
				$(this).parent().siblings().children().children(".one_check_").hide();
			});
			$(".one_check_only_add").click(function(){
				$(this).children(".one_check_").show();
				$(this).parent().siblings().children().children(".one_check_").hide();
			});
			$(".group_check").click(function(){
				$(this).children(".group_check_").toggle();
				//$(this).parent().siblings().children().children(".group_check_").hide();
			});
			
			
			
			
			
			
			
			
			
			
			
	
			
			

		   /* $(".text_area").keyup(function(){  

		        $(this).next().children(".text_count").text($(this).val().length);
		    $(".text_area").on("change",function(){  
		    	var length = $(this).val().length;
		    	var unsubscribeMSG = $("#unsubscribeMSG").val();
//				var smsAutograph = $("#smsAutograph").val();  短信签名自动添加到了短信内容中，不需要重复计算
				var endLength = length+5+unsubscribeMSG.length;
		        $(this).next().children(".text_count").text(endLength);
		        $(".text_area_copy").text($(this).val())
		   });*/
    		
    		
    		$(".short_url_btn").click(function(){
    			$(".short_url").show();
    		});
    		$(".save_short_url").click(function(){
    			$(".short_url").hide();
    		});
    		
    		$(".text_model_btn").click(function(){
    			$(".text_model_window").show();
    		});
    		
    		$(".close").click(function(){
    			//启用滚动条
				$(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
    			$(".text_model_window").hide();
    			$(".text_test_window").hide();
    			$(".sifting").hide();
    		});
    		
    		
    		$(".text_test_btn").click(function(){
//    			var content = $("#textContent").val();//获取短信内容
//    			console.log("短信内容是：" + content);
    			$(".text_test_window").show();
    		});
    		
    		$(".sifting_btn").click(function(){
    			if($("#sendMember").val()==null||$("#sendMember").val()==""){

    				$(".jindu").show();
    			}else{

    				$(".jindu").hide();
    			}
    			
    			$('.markBg').show();
    			//禁止滚动条
    			  $(document.body).css({
    			    "overflow-x":"hidden",
    			    "overflow-y":"hidden"
    			  });
    			$("#memberSelectionBox").show();
    		});
    		
//    		$(".1check_box").click(function(){
//				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
//				$(this).parent().siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
//			});
    		
    		
    		
    		
    		
    		
    		
    		$(".qianming_none").click(function(){
    			$(".qianming_").attr("disabled","disabled");
    		});
    		
    		$(".qianming_done").click(function(){
    			$(".qianming_").removeAttr("disabled");
    		});
    		
    		
    		$(".text_model").click(function(){
    			$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
    			$(this).siblings(".text_model").addClass("bgc_e3e7f0").removeClass("bgc_fff");
    		});
    		
    		/*$(".more_detail_btn").click(function(){
    			$(".more_detail").toggle();
    		});*/
    		
    		
    		
    		
    		
    		
    		
    		
    		/*群发发送选项切换*/
			$(".text_designate_out").click(function(){
				var i=$(this).index();				
				 	$(".text_designate_in").hide();
				 	$(".text_designate_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					$("input").val("")
			});
    		
			$(".day_only").click(function(){
				$(this).children(".b_radius5").addClass("bgc_check_blue");
				$(this).siblings().children(".b_radius5").removeClass("bgc_check_blue");
				$(".date_only").children("input").val("");
			});
    		
			$(".date_only").click(function(){
				$(".day_only").children(".b_radius5").removeClass("bgc_check_blue");
			});
			
			
			
			
		/*				
			$(".类名").click(function(){
				
				if (   $(this).val()=="立即开启"   ) {
					$(this).val("立即关闭");
					$(".toggle_tishi_img").attr({"src":"images/open_sucess.png"})
					$(".toggle_tishi").show();
					setTimeout(function(){
						$(".toggle_tishi").hide()
					},1000)
					
				} else
				{
					$(this).val("立即开启");
					$(".toggle_tishi_img").attr({"src":"images/close_sucess.png"})
					$(".toggle_tishi").show();
					setTimeout(function(){
						$(".toggle_tishi").hide()
					},1000)
				}
			});*/
			
			
			
			
			//@uthor jackstraw_yu
			$(".close_template").click(function(){
    			//启用滚动条
				$(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
    			$(".text_model_window").hide();
    			//默认选中近期节日
    			/*$(".text_model").addClass("bgc_e3e7f0").removeClass("bgc_fff");
    			$(".jinqiFestival").removeClass("bgc_e3e7f0").addClass("bgc_fff");*/
    			
    			//默认选中推荐场景
    			$(".text_model").addClass("bgc_e3e7f0").removeClass("bgc_fff");
    			$(".tuijianchangjin").removeClass("bgc_e3e7f0").addClass("bgc_fff");
    			
    			//清空数据
    			//$('#table_jinqijieri_tr').siblings().remove();
    			$('#table_tuijianchangjin_tr').siblings().remove();
    			$('#table_lishifasong_tr').siblings().remove();
    			//$(".jqjr").show();
    			$(".tuijian").show();
    			$(".lishijilu").hide();
    			
    		});
			
			
			//lhy 2017-6-20 营销中心
			//为会员分组下拉框添加z-Index
			var nIndex=100;
			$('.member-grouping-left .nice-select').each(function(index){
				$('.member-grouping-left .nice-select').eq(index).css({
					zIndex:nIndex-index
				});
			});
			
			//点击筛选框li效果
			$('.member-grouping-nav li').on('click',function(){
				var thisText=$(this).text();
				var thisVal=$(this).val();
				
				$(this).parent().siblings('p').text(thisText);
				$(this).parents('.member-grouping-left').children('input').val(thisVal);
			});
			
			//点击指定商品自定义按钮弹框
			$('.member-all-box .member-alerta').on('click',function(){
				if($(this).hasClass('disabled'))return;
				$('#memberSelectionBox').hide();
				$('.memberAlli').removeClass('on');
//				$('#itemIds').val('');

				$('#memberAlart').show();
			});
			
			//指定商品全部商品按钮效果
			$('.member-all .serch-tj').on('click',function(){
				if($(this).hasClass('disabled'))return;
				if($(this).hasClass('on')){
					$(this).removeClass('on');
					$(this).siblings('input').val('0');
				}else{
					$(this).addClass('on');
					$(this).siblings('input').val('1');
					
				}
			});
			
			$('.member-grouping-rSpan .shield-select').on('click',function(){
				if($(this).hasClass('on')){
					$('.member-grouping-rSpan .shield-select').removeClass('on');
					$(this).siblings('input').val('');
					if($(this).attr('value')==1){
						$('.member-grouping-rSpan .shield-select').eq(0).parents('.member-grouping-rSpan').show();
						$(this).siblings('.color-signBox').removeClass('on');
						$(this).siblings('.color-signBox').find('i').removeClass('on');
					}
				}else{
					
					if($(this).attr('value')==1){
						$('.member-grouping-rSpan .shield-select').eq(0).parents('.member-grouping-rSpan').hide();
						$(this).siblings('.color-signBox').addClass('on');
						
						
					}
					$('.member-grouping-rSpan .shield-select').removeClass('on');
					$('.member-grouping-rSpan .shield-select').siblings('input').val('');
					$(this).addClass('on');
					$(this).siblings('input').val($(this).attr('value'));
					
				}
				
			});
			
			$('#memberCount').on('click',function(){
				$("#memberSelectionBox").show();
				$('.markBg').show();
			});
			
			//地区筛选自定义按钮弹窗
			$('.member-all-box .cityalerta').on('click',function(){
				if($(this).hasClass('disabled'))return;	
				$('#cityAlert').show();
				$('#memberSelectionBox').hide();
				$('.markBg').hide();
				$('.cityscreen').removeClass('on');
				$('.cityscreen').siblings('input').val('0');
			});
			
			//右侧筛选自定义按钮效果
			$('.select-a').on('click',function(){
				if($(this).hasClass('disabled'))return;
				if($(this).text()=='自定义'){
					$(this).siblings('.custom-box').addClass('on');
					$(this).siblings('.nice-select').addClass('on');
					$(this).parent('.member-grouping-right').children('input').val('');
					$(this).siblings('.nice-select').children('p').text('不限')
					$(this).text('取消');
					
				}else{
					if($(this).parent('.member-grouping-right').children('label').text()=='最后交易时间 :'){
						$(this).siblings('.custom-box').find('input').val('');
					}
					$(this).siblings('.nice-select').removeClass('on');
					$(this).siblings('.custom-box').removeClass('on');
					$(this).siblings('.custom-box').children('input').val('');
					$(this).text('自定义');
				}
			});
			$('.zdya').on('click',function(){
				$('#minTradePrice').val('0');
				$('#maxTradePrice').val('0');
			});
			$('.zdyb').on('click',function(){
				$('#minAvgPrice').val('0');
				$('#maxAvgPrice').val('0');
			});
			
			//屏蔽卖家标记选中效果
			$('.color-signDiv i').on('click',function(){
				if($(this).hasClass('on')){
					$(this).removeClass('on');
				}else{
					$(this).addClass('on');
				}
			});

			
			
			
			

			
			//选择商品弹窗 取消按钮 效果
			$('.selectaqx').on('click',function(){
				$('.memberAlli').addClass('on');
				$('.memberAlli').siblings('input').val('1');
				$('#itemIds').val('');
				$('.member-alerta').text('自定义');
			});
			
			//地区筛选自定义按钮效果
//			$('.cityalerta').on('click',function(){
//				$('.cityscreen').removeClass('on');
//				$('.cityscreen').siblings('input').val('0');
//			});
			
			//会员筛选弹框取消发送效果
			$('.closeInfo').on('click',function(){
				$('.sifting').hide();
				$('#memberCount').hide();
				$('#sendButton').show();
			});
			
			//累计金额和平均客单价value操作
			$('.TradePriceNav li').on('click',function(){
				var thisVal=$(this).attr('value');
				if(thisVal.indexOf('~')>0){
					$('#minTradePrice').val(thisVal.split('~')[0]);
					$('#maxTradePrice').val(thisVal.split('~')[1]);
				}else{
					$('#minTradePrice').val(thisVal);
					$('#maxTradePrice').val('');
				}
				
			});
			$('.AvgPriceNav li').on('click',function(){
				var thisVal=$(this).attr('value');
				if(thisVal.indexOf('~')>0){
					$('#minAvgPrice').val(thisVal.split('~')[0]);
					$('#maxAvgPrice').val(thisVal.split('~')[1]);
				}else{
					
					$('#minAvgPrice').val(thisVal);
					$('#maxAvgPrice').val('');
				}

			});
		

		});
		
	

