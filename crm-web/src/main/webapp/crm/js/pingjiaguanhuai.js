
	$(function(){
		/* 关怀评价选项切换 */
		$(".ddzx_in_out").click(function(){
			var i=$(this).index();				
		 	$(".p_1").hide();
		 	$(".p_1").eq(i).show();
			$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
			$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
			$(".one_check_").hide()
			$("input").val("")
		});
		/* 好评中评差评选项切换 */
		$(".pingjia").click(function(){
			var i=$(this).parent().index();				
		 	$(".pingjia1").hide();
		 	$(".pingjia1").eq(i).show();
			$(".one_check_").hide();
		});
			
		/* 下拉框 */
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
			var dataVal = $(this).attr("data-value");
			$(this).parents('[name="nice-select"]').find('input').val(val);
			$('[name="nice-select"] ul').hide();
			e.stopPropagation();
		});
		$(document).click(function(){
			$('[name="nice-select"] ul').hide();
		});
			
		$(".pingjia_1").click(function(){
			var i=$(this).index();				
			$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
			$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
		});	
			
		$(".all_check").click(function(){
			$(".check_").toggle();
		});
		$(".check").click(function(){
			$(this).children(".check_").toggle();
		});
		$(".one_check_only").click(function(){
			$(this).children(".one_check_").toggle();
			$(this).parent().siblings().children().children(".one_check_").hide();
		});
		$(".group_check_only").click(function(){
			$(this).children(".group_check_").show();
			$(this).parent().siblings().children().children(".group_check_").hide();
		});
		$(".group_check").click(function(){
			$(this).children(".group_check_").show();
			$(this).parent().siblings().children().find(".group_check_").hide();
		});
		
		// 点击更多start
	    $('.tianjia_1').click(function(){
		    // 禁止滚动条
		    $(document.body).css({
		    "overflow-x":"hidden",
		    "overflow-y":"hidden"
		    });
		    $(".tianjia_2").show();
		});
	    
	    $('.tianjia_good').click(function(){
	    	$('#resultType').val('good');
		    // 禁止滚动条
		    $(document.body).css({
			    "overflow-x":"hidden",
			    "overflow-y":"hidden"
		    });
		    $(".tianjia_2").show();
		});
	    $('.tianjia_neutral').click(function(){
	    	$('#resultType').val('neutral');
		    // 禁止滚动条
		    $(document.body).css({
			    "overflow-x":"hidden",
			    "overflow-y":"hidden"
		    });
		    $(".tianjia_2").show();
		});
	    $('.tianjia_bad').click(function(){
	    	$('#resultType').val('bad');
		    // 禁止滚动条
		    $(document.body).css({
			    "overflow-x":"hidden",
			    "overflow-y":"hidden"
		    });
		    $(".tianjia_2").show();
		});
		
		$(".close_tianjia_2").click(function(){
			// 启用滚动条
		    $(document.body).css({
		    "overflow-x":"auto",
		    "overflow-y":"auto"
		    });
			$(".tianjia_2").hide();
		});
		
	    $(".close_inside").click(function(){
	    	// 启用滚动条
	    	$(document.body).css({
	    		"overflow-x":"auto",
	    		"overflow-y":"auto"
	    	});
			$(".tianjia_2").hide();
		});	
	 
	    $(".text_model").click(function(){
			$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
			$(this).siblings(".text_model").addClass("bgc_e3e7f0").removeClass("bgc_fff");
		});
		
		$('.kaiqi').click(function(){
			$('.guanbi').children().hide();
		})
		
		$('.guanbi').click(function(){
			$('.kaiqi').children().hide();
		})
		
	    $(".moban1").click(function(){
			var i=$(this).index();				
		 	$(".moban2").hide();
		 	$(".moban2").eq(i).show();
			$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
			$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
		});		
			
	    // 点击抢评出现弹框
	    $('.pingjia_check').click(function(){
	   		$(".pingjia_check_1").hide();
	   		$(".pingjia_check_2").hide();
	   		$(this).children().children(".group_check_").show();
	   		$(this).next(".pingjia_check_1").show();
	   		$(this).next(".pingjia_check_1").next(".pingjia_check_2").show();
	   		$(this).parent("ul").parent("div").siblings().children("ul").children(".pingjia_check").children(".group_check").children().hide();
		    })
	   
		    // 批量评价全部
		$('.quanbu').click(function(){
			$('.quanbu1').children().toggle();
		})
		$('.quanbu2').click(function(){
			$('quanbu2').children().hide();
			$(this).children().show();
		})
		$(".XGXGXG").click(function(){
			$(this).parents(".GaoJiSheZhi").next(".DuanXinSheZhi").find(".die_SZ").hide();
			$(this).parents(".GaoJiSheZhi").next(".DuanXinSheZhi").find(".Live_SZ").show();
		});	
		$('.ZP').click(function(){
			$(this).children().show()
		})
		$('.CP').click(function(){
			$(this).children().show()
		})
		$("#more").mouseover(function(){
	        $(".bind_ol").show();
	    });
		
	    $(".bind_ol").mouseover(function(){
	        $(".bind_ol").show();
	    });
	    $(".bind_ol").mouseout(function(){
	        $(".bind_ol").hide();
	    });
	    
	    <!-------------    卖家标记勾选框    ---------------------->
		$(".MaiJiaBiaoJiGXK").click(function(){
			$(this).toggleClass("bgc_check_blue");
		});
	
		$(".ZhanKai").click(function(){
			if($(".LiuCheng").css("display")=="none"){
				$(".LiuCheng").slideDown(200);
			}else if($(".LiuCheng").css("display")=="block") {$(".LiuCheng").slideUp(200)};
			$(".JTS").toggle();
			$(".JTX").toggle();
			if($(".JTX").css("display")=="none"){$(".LiuChengText").text("收回流程")};
			if($(".JTS").css("display")=="none"){$(".LiuChengText").text("展开流程")};
			
		})
});