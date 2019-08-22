
		$(function(){
			//模板示例图片显示
			$(".hover_example").hover(
					  function() {
					  	$(".hover_example_hide").show();
					  }, function() {
					  	$(".hover_example_hide").hide();
					  }
				);
			$("#uploadFile").on("click",function(){
				$("#historyShow").show();
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
				$('[name="nice-select"] ul').hide();
				e.stopPropagation();
			});
			$(document).click(function(){
				$('[name="nice-select"] ul').hide();
			});




			
			
			/*店铺数据选项切换*/
			$(".dpsj_out").click(function(){
				var i=$(this).index();				
				 	$(".dpsj_in").hide();
				 	$(".dpsj_in").eq(i).show();
				$(this).addClass("bgc_e3e7f0");
				$(this).siblings().removeClass("bgc_e3e7f0");
				$(".one_check_").hide();
				$(".one_check_only_").hide();
				$(".group_check_").hide();
				$("input").val("")
			});
			
			/*选项切换*/
			$(".text_out").click(function(){
				var i=$(this).index();				
				 	$(".text_in").hide();
				 	$(".text_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					$("input").val("")
			});
			
			/*选项切换*/
			$(".out").click(function(){
				var i=$(this).index();				
				 	$(".in").hide();
				 	$(".in").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_cad3df");
					$(this).siblings(".out").removeClass("bgc_00a0e9 c_fff").addClass("c_cad3df");
					$(".one_check_").hide()
					$("input").val("")
			});
			
		
			
			
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
			
			
			
			
			
			
			
			
			
			
			/*点击显示短信详情*/
			$(".text_detail").click(function(){
				$(this).toggleClass("one_line_only lh50 h50");
			});
			
			
			/*发送短信弹窗*/
			$(".history_list_btn").click(function(){
				$(".history_list").show()
			});
			
			$(".close").click(function(){
				$(".history_list").hide()
			});
			
			
			$(".more_check").click(function(){
				$(".more_check_").toggle()
			});
			
			
		});
		
	

