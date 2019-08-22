
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




			
			
			/*店铺数据选项切换*/
			$(".ddzx_out").click(function(){
				var i=$(this).index();				
				 	$(".ddzx_in").hide();
				 	$(".ddzx_in").eq(i).show();
				$(this).addClass("bgc_e3e7f0");
				$(this).siblings().removeClass("bgc_e3e7f0");
				$(".one_check_").hide();
				$(".one_check_only_").hide();
				$(".group_check_").hide();
				$("input").val("")
			});
			
			/*黑名单选项切换*/
			$(".ddzx_in_out").click(function(){
				var i=$(this).index();				
				 	$(".ddzx_in_in").hide();
				 	$(".ddzx_in_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					$("input").val("")
			});
			
			/*黑名单选项切换*/
			$(".text_out").click(function(){
				var i=$(this).index();				
				 	$(".text_in").hide();
				 	$(".text_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					$("input").val("")
			});
			
		
			
			
			/*table背景色*/
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/*table高度，行高*/
			$("tr").css("height","2.6vw");
			
		
			
			
			
			
		
			$(".group_check").click(function(){
				$(this).children(".group_check_").toggle();
				$(this).parent().siblings().children().children(".group_check_").hide();
			});
			
			
			
			
			
			
			
			
			
			
			/*点击显示短信详情*/
			$(".text_detail").click(function(){
				$(this).toggleClass("one_line_only lh50 h50");
			});
			
			
			/*发送短信弹窗*/
			$(".history_list_btn").click(function(){
				$(".history_list").show()
			});
			
			$(".save_history_list").click(function(){
				$(".history_list").hide()
			});
			
			$(".cancel_history_list").click(function(){
				$(".history_list").hide()
			});
			
			
			
			$(".more_check").click(function(){
				$(".more_check_").toggle()
			});
			
			
			
		    $(".text_area").keyup(function(){  
		        $(this).next().children(".text_count").text($(this).val().length);
		        $(".text_area_copy").text($(this).val())
		   });
    		
    		
    		$(".short_url_btn").click(function(){
    			$(".short_url").show();
    		});
    		$(".save_short_url").click(function(){
    			$(".short_url").hide();
    		});
    		
    		$(".text_model_btn").click(function(){
    			$(".text_model_window").show();
    		});
    		
    		$(".text_test_btn").click(function(){
    			$(".text_test_window").show();
    		});
    		
    		
    		
    		$(".text_model").click(function(){
    			$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
    			$(this).siblings(".text_model").addClass("bgc_e3e7f0").removeClass("bgc_fff");
    		});
			
		});
		
	

