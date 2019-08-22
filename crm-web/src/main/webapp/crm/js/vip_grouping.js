
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
				var dataVal = $(this).attr("data-value");
				$(this).parents('[name="nice-select"]').find('input').val(val);
				$('[name="nice-select"] ul').hide();
				e.stopPropagation();
			});
			$(document).click(function(){
				$('[name="nice-select"] ul').hide();
			});


		
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
					$(this).children(".1check_box_1").removeClass("bgc_check_blue");
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
			
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").removeClass("bgc_check_blue");
			
					/*	$(this).children(".1check_box_1").addClass("bgc_check_blue");
						$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				
						$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
				*/
				}
				
				
				/*
				$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				
				$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
				*/
			});
			
			
			$(".all_notcheck").click(function(){
				
				$(this).siblings().children(".1check_box_1").removeClass("bgc_check_blue");
				
				$(this).children(".1check_box_1").addClass("bgc_check_blue");
				
				
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
			
			
			/*创建分组*/
			$(".add_group_btn").click(function(){
				$("#bombBox").show();
				$(".markBg").show();
				$(".group_check_").hide();
				$(".explain").val("");
				$(".group_name").val("");
			});
			
			$(".close").click(function(){
				$("#bombBox").hide();
				$(".markBg").hide();
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
			
			
			
			
			
			$(".JiangZheHu").mouseover(function(){
				$(this).find("span").show();
			});
			$(".JiangZheHu").mouseout(function(){
				$(this).find("span").hide();
			});
			
			
			
			
			
		});
		
		
	

