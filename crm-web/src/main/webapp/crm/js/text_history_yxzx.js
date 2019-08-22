
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
				var val = $(this).parent("ul").prev("input").html();
				var dataVal = $(this).attr("data-value");
				$(this).parents('[name="nice-select"]').val(val);
				$('[name="nice-select"] ul').hide();
				e.stopPropagation();
			});
			$(document).click(function(){
				$('[name="nice-select"] ul').hide();
			});


			

			
			
			$(".check_list1_btn").click(function(){
				$(".check_list1").show()
			});
			
			$(".check_list2_btn").click(function(){
				$(".check_list2").show()
			});
			
			$(".check_list3_btn").click(function(){
				$(".check_list3").show()
			});
			
			$(".close").click(function(){
				$(".check_list1").hide()
				$(".check_list2").hide()
				$(".check_list3").hide()
			});
			
			
			
			
	
			
			/*选项切换*/
		/*	$(".text_out").click(function(){
				var i=$(this).index();				
				 	$(".text_in").hide();
				 	$(".text_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
			});*/
		
			
			
			/*table背景色*/
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/*table高度，行高*/
			$("tr").css("height","2.6vw");
			
		

			
		});
		
	

