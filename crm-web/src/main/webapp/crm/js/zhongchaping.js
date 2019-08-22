$(function(){
	$(".anfu_1").click(function(){
				var i=$(this).index();				
				 	$(".anfu_2").hide();
				 	$(".anfu_2").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
					$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
					/*$("input").val("");*/
			});
			
			
			
			
			
			
			
		$(".window_out li").click(function(){
								
				 	
				 	$(".window_in").show();
					$(this).addClass("bgc_fff");
					$(this).siblings().removeClass("bgc_fff");
			});	
			
			
			
			
			
			
			
			
	$(".moban1").click(function(){
				var i=$(this).index();				
				 	$(".moban2").hide();
				 	$(".moban2").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					/*$("input").val("");*/
			});		
			
	
	
	
	
	
	$(".wenti_1").click(function(){
				var i=$(this).index();				
				 	$(".wenti_2").hide();
				 	$(".wenti_2").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
					$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
					/*$("input").val("");*/
			});
		
		
		
		
		
		$(".xiugai_1").click(function(){
				var i=$(this).index();				
				 	$(".xiugai_2").hide();
				 	$(".xiugai_2").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
					$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
					/*$("input").val("");*/
			});
			
			
			
			
			
			
			
			$(".out").click(function(){
				var i=$(this).index();				
				 	$(".in").hide();
				 	$(".in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					/*$("input").val("");*/
			});
			
			
			
			
			$(".text_area").keyup(function(){  
		        $(this).next().children(".text_count").text($(this).val().length);
		        $(".text_area_copy").text($(this).val())
			});
		
		
		
		$(".all_check").click(function(){
				$(this).children(".one_check_").toggle();
				$("td div .one_check_").toggle();
			});
			
			$(".one_check").click(function(){
				$(this).children(".one_check_").toggle();
			});
			$(".one_check_only").click(function(){
				$(this).children(".one_check_").show();
				$(this).siblings().children(".one_check_").hide();
			});
			$(".one_check_only1").click(function(){
				$(this).children(".one_check_").toggle();
			});
			$(".group_check").click(function(){
				$(this).children(".group_check_").toggle();
				$(this).parent().siblings().children().children(".group_check_").hide();
			});
		
		
		
		
		
		
		
		
		
			
			
			
			$(".change_text_btn").click(function(){
				$(".change_text").show()
			});	
			
			$(".close").click(function(){
				$(".duanxin_2").hide()
			});	
			
			
			
			
			
			
			
			
	$(".jiankong_1").click(function(){
				var i=$(this).index();				
				 	$(".jiankong_2").hide();
				 	$(".jiankong_2").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
					$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
					/*$("input").val("");*/
			});	
			
			
			
			
			
			
			
			$(".tongji1").click(function(){
				var i=$(this).index();				
				 	$(".tongji2").hide();
				 	$(".tongji2").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
					$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
					/*$("input").val("");*/
			});
			
			
			
			
			
	$(".fenxi_1").click(function(){
				var i=$(this).index();				
				 	$(".fenxi_2").hide();
				 	$(".fenxi_2").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_8493a8");
					$(this).siblings().removeClass("bgc_00a0e9").addClass("c_8493a8");
					/*$("input").val("");*/
			});			
	
		/*table背景色*/
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/*table高度，行高*/
			$("tr").css("height","2.6vw");
			
			
			
			
			
		/*弹出框*/	
			$(".yuanyin_1").click(function(){
				$(".yuanyin_2").show();
        	});
        	
        	$(".close_yuanyin_2").click(function(){
				$(".yuanyin_2").hide();
        	});
        	$(".close_inside").click(function(){
				$(".yuanyin_2").hide();
        	});
        	
        
        
        
        
        $(".reason_1").click(function(){
				$(".reason_2").show();
        	});
        	
        	$(".close_reason_2").click(function(){
				$(".reason_2").hide();
        	});
        	
        $(".close_inside").click(function(){
				$(".reason_2").hide();
        	});
        
        
        
        
        
        
        
        $(".duanxin_1").click(function(){
        	$(".duanxin_2").show();
    	});
    	$(".close_duanxin_2").click(function(){
				$(".duanxin_2").hide();
        	});
    	
    	
    	
    	/*$(".fasong_1").click(function(){
				$(".fasong_2").show();
        	});*/
        	
        	$(".close_fasong_2").click(function(){
				$(".fasong_2").hide();
        	});
    	
    	$(".close_inside").click(function(){
				$(".fasong_2").hide();
        	});
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	$(".tishi_1").click(function(){
				$(".tishi_3").show();
        	});
        	
        	$(".close_tishi_2").click(function(){
				$(".tishi_3").hide();
        	});
    	
    	
    	
    	
    	
    	$(".close_inside").click(function(){
				$(".tishi_3").hide();
        	});
        	
        	
        	
        	
        	
        	
        	
        $(".sure_1").click(function(){
				$(".sure_2").show();
        	});
        	
        	$(".close_sure_2").click(function(){
				$(".sure_2").hide();
        	});
    	
    	
    	
    	
    	
    	$(".close_inside").click(function(){
				$(".sure_2").hide();
        	});	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
			
			$(".one_check").click(function(){
				$(this).children(".one_check_").toggle();
				$(this).parent().siblings().children().children(".one_check_").hide();
			});
    	
    	
    	
    	
    	
    	
    	
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
				var dataVal = $(this).attr("data-value");
				$(this).parents('[name="nice-select"]').find('input').val(val);
				$(timeMSG).empty();
				var endTime = $("#endTime").val();
				var startTime = $("#startTime").val();
				if(startTime==undefined || endTime == undefined){
				}else{
					if(endTime!="" && startTime!=""){
						var endTimeFloat = parseFloat(endTime.substring(0,2));
						var startTimeFloat = parseFloat(startTime.substring(0,2));
						if(endTimeFloat<startTimeFloat){
							$('#startTime').val('');
							$('#endTime').val('');
							$(timeMSG).html("<font style='color: red;'>结束时间不能小于开始时间</font>");
						}else if(endTimeFloat==startTimeFloat){+
							$('#startTime').val('');
							$('#endTime').val('');
							$(timeMSG).html("<font style='color: red;'>结束时间不能等于开始时间</font>");
						}
					}
				}
				$('[name="nice-select"] ul').hide();
				e.stopPropagation();
			});
			$(document).click(function(){
				$('[name="nice-select"] ul').hide();
			});
    	
    	$(".set_time").click(function(){
				$(".set_time_").toggle();
			});
			$(".set_time_none").click(function(){
				$(".set_time_").hide();
			});
			
			$(".detail_btn").click(function(){
				$(".detail").toggle();
			});
    	
    	
    	
    	$(".guize1").hover(function(){$(".guize2").show()},function(){$(".guize2").hide()})
    	
    	
		<!-------------    地区弹框    ----------------------->
		
		$(".1check_box_2").click(function(){
			$(this).children(".1check_box_1").addClass("bgc_check_blue");
			$(this).parent().siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
		});
		
		$(".1check_box_3").click(function(){
			$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
		});
		
		
		$(".all_check").click(function(){
			
			
			
			$(this).nextAll().children(".1check_box_1").removeClass("bgc_check_blue");
			
			if (   !$(this).children(".1check_box_1").hasClass("bgc_check_blue")   ) {
				
					
					$(this).children(".1check_box_1").addClass("bgc_check_blue");
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
			
				
			} else{
				/*	$(this).children(".1check_box_1").addClass("bgc_check_blue");
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
			*/
			}
			
			
			
			$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			
			$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
			
		});
		
		
		$(".all_notcheck").click(function(){
			
			$(this).siblings().children(".1check_box_1").removeClass("bgc_check_blue");
			
			$(this).children(".1check_box_1").addClass("bgc_check_blue");
			
			$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
			
			$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").removeClass("bgc_check_blue");
			
		})
		
		$(".far_notcheck").click(function(){
			
			$(this).prev().children(".1check_box_1").removeClass("bgc_check_blue");
			
			$(".all_check").children(".1check_box_1").addClass("bgc_check_blue");
			
			
			$(".place_check").find(".1check_box_1").addClass("bgc_check_blue");
			
			if (   $(this).children(".1check_box_1").hasClass("bgc_check_blue")   ) {
				$(this).children(".1check_box_1").removeClass("bgc_check_blue");
			$(this).parent().next(".place_check").children("ul").eq(5).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").addClass("bgc_check_blue");
			
			$(this).parent().next(".place_check").children("ul").eq(6).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".1check_box_1").addClass("bgc_check_blue");
			$(this).parent().next(".place_check").children("ul").eq(5).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").removeClass("bgc_check_blue");
			
			$(this).parent().next(".place_check").children("ul").eq(6).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").removeClass("bgc_check_blue");
			}
			
			
			
		});
		
		
		
		
		
	
		
		
		
		$(".place_check").click(function(){
			if (   $(".bgc_check_blue").length>43   ) {
				$(".all_check").children(".1check_box_1").addClass("bgc_check_blue");
				$(".all_check").siblings().children(".1check_box_1").removeClass("bgc_check_blue");
			} else
			
			if (   $(".bgc_check_blue").length<1   ) {
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_notcheck").children(".1check_box_1").addClass("bgc_check_blue");
			} else
			
			if (   $(".bgc_check_blue").length>1   ) {
				$(".all_notcheck").children(".1check_box_1").removeClass("bgc_check_blue");
			} else{
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
	
			
		});
		
		$(".gangaotai").click(function(){
		
			if (   $(this).children(".1check_box_1").hasClass("bgc_check_blue")   ) {
				$(this).children(".1check_box_1").removeClass("bgc_check_blue");
				$(this).nextAll(".li_").children(".1check_box_1").removeClass("bgc_check_blue");
			} else{
				$(this).children(".1check_box_1").addClass("bgc_check_blue");
				$(this).nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
			}
			
		});
		
		$(".gangaotai_ul").children(".li_").click(function(){
			$(this).prevAll(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
			$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
		});
		
		$(".gangaotai_ul").eq(0).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>4   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(1).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>5   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(2).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>2   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(3).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>3   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(4).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>2   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(5).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>4   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(6).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>4   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(7).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>2   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		$(".gangaotai_ul").eq(8).click(function(){
			if (   $(this).children(".li_").children(".bgc_check_blue").length>0   ) {
				$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
			} else{
				$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
			};
		});
		
		
		
		
		
		
			
		
		
		/*table背景色*/
		$("tr:odd").css("background-color","#FAFAFA");
		$("tr:even").css("background-color","#F4F4F4");
		/*table高度，行高*/
		$("tr").css("height","2.6vw");
		
	
		
		
	

		
		$(".check_part:eq(0),.check_part:eq(1),.check_part:eq(2),.check_part:eq(3)").find("li").click(function(){
			
			$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			$(this).siblings().children(".1check_box").children(".1check_box_1").removeClass("bgc_check_blue");
		});
		
		$(".check_part:eq(4),.check_part:eq(5)").find("li:eq(0)").click(function(){
			
			$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			$(this).siblings().children(".1check_box").children(".1check_box_1").removeClass("bgc_check_blue");
		});
		$(".check_part:eq(4)").find("li:eq(1),li:eq(2),li:eq(3),li:eq(4),li:eq(5)").click(function(){
			
			$(".check_part:eq(4)").find("li").eq(0).children().children(".1check_box_1").removeClass("bgc_check_blue");
			$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			
		});
		
		
		$(".check_part").eq(5).find("li").eq(0).click(function(){
			
			
			$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
			$(this).siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
			
			
		});
		
		$(".check_part").eq(5).find("li").eq(1).click(function(){
			$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
			$(".check_part").eq(5).find("li").eq(0).children().children(".1check_box_1").removeClass("bgc_check_blue");
			$(".check_part").eq(5).find("li").eq(3).children().children(".1check_box_1").removeClass("bgc_check_blue");
		});
		
		$(".check_part").eq(5).find("li").eq(2).click(function(){
			$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
			$(".check_part").eq(5).find("li").eq(0).children().children(".1check_box_1").removeClass("bgc_check_blue");
			$(".check_part").eq(5).find("li").eq(3).children().children(".1check_box_1").removeClass("bgc_check_blue");
		});
		
		$(".check_part").eq(5).find("li").eq(3).click(function(){
			
			
			$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
			$(this).siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
			
			
		});
		/*创建分组*/
		$(".add_group_btn").click(function(){
			$(".add_group").show();
			$(".group_check_").hide();
			$(".explain").val("");
			$(".group_name").val("");
		});
		
		$(".close").click(function(){
			$(".add_group").hide();
		});
		
		$(".close_1").click(function(){
			$(".area_check_window").hide();
		});
		
		
		
		
		$(".update_btn").click(function(){
			$(".update").show();
			$(".group_check_").hide();
			$(".explain").val("");
			$(".group_name").val("");
		});
		
		$(".close").click(function(){
			$(".update").hide();
		});
		
		
		
		
		
		
		$(".zdy").click(function(){
			$(this).next().show();
		});
		$(".zdy").prevAll().click(function(){
			$(this).nextAll(".zdy").next().hide();
		});
		
		$(".zdy_1").click(function(){
			$(this).prev().children().children(".1check_box_1").addClass("bgc_check_blue");
		});
		
		
		$(".zdy_").click(function(){
			$(".area_check_window").show();
		})
		
	
		$(".CustomArea").click(function(){
			
			
			if(   !$(this).children("img").hasClass("show_Area")   ){
				$(this).children("img").show();
				$(".area_check_window").show();
				$(this).children("img").addClass("show_Area")
			}else{
				$(this).children("img").hide();
				$(this).children("img").removeClass("show_Area")
			}
		});
		
		
		$('.areaSelect').attr("disabled",true)
    	
		
		$(".ZhanKai").click(function(){
	 			if($(".LiuCheng").css("display")=="none"){
	 				$(".LiuCheng").slideDown(200);
	 			}else if($(".LiuCheng").css("display")=="block") {$(".LiuCheng").slideUp(200)};
	 			$(".JTS").toggle();
	 			$(".JTX").toggle();
	 			if($(".JTX").css("display")=="none"){$(".LiuChengText").text("收回流程")};
	 			if($(".JTS").css("display")=="none"){$(".LiuChengText").text("展开流程")};
	 			
	 		})
})
