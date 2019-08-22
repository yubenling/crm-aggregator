
		$(function(){
			
			
			
		


	
		
			
			
			/*table背景色*/
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/*table高度，行高*/
			$("tr").css("height","2.6vw");
			
		
			
			
			
			
			$(".all_check").click(function(){
				$(this).children(".one_check_").toggle();
				$("td div .one_check_").toggle();
			});
			$(".one_check").click(function(){
				$(this).children(".one_check_").toggle();
			});
			$(".one_check_only").click(function(){
				$(this).children(".one_check_").toggle();
				$(this).parent().siblings().children().children(".one_check_").hide();
			});
			$(".group_check").click(function(){
				$(this).children(".group_check_").toggle();
				$(this).parent().siblings().children().children(".group_check_").hide();
			});
			
			
			
			
			
			
			
			
			$(".1check_box").click(function(){
				$(this).children(".1check_box_1").addClass("bgc_check_blue");
				$(this).siblings(".1check_box").children(".1check_box_1").removeClass("bgc_check_blue");
				var ss = $(this).children("[name='key']").val();
				$("#type_s").val(ss);
			});
			
			
			$(".hmd_window_btn").click(function(){
				//禁止滚动条
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".hmd_window").show();
			});
			
			
			$(".add_hmd_window_btn").click(function(){
				//禁止滚动条
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".add_hmd_window").show();
			});
			
			
			
			$(".close").click(function(){
				//启用滚动条
				  $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".hmd_window").hide();
				$(".add_hmd_window").hide();
//				$("input").val("");
			});
			
			
			
			
			$(".queding_").click(function(){
				queding()
				
				
				if (   $(".bgc_check_blue").length == 1  &&  $("#content").val()!=''   ) {
					
					queding()
					
				} else{
					$(".p3").show();
				}
				
			});
		
			
			
    
		});
		
	

