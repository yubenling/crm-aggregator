
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
			
			//发送时间
			$("#tser01").on("click",function(){
				$.jeDate('#tser01',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD',
					choosefun:function(elem, val) {
						var startTime = $(tser01).val();
						var endTime = $(tser02).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime);
							var endTimeDate = new Date(endTime);
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser07).val("");
								confirm("开始时间不能大于结束时间！");
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser01).val();
						var endTime = $(tser02).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime);
							var endTimeDate = new Date(endTime);
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser07).val("");
								confirm("开始时间不能大于结束时间！");
							}
						}
					}
				});
			});
			//结束时间
			$("#tser02").on("click",function(){
				$.jeDate('#tser02',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD',
					choosefun:function(elem, val) {
						var startTime = $(tser01).val();
						var endTime = $(tser02).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime);
							var endTimeDate = new Date(endTime);
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser01).val("");
								confirm("开始时间不能大于结束时间！");
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser01).val();
						var endTime = $(tser02).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime);
							var endTimeDate = new Date(endTime);
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser01).val("");
								confirm("开始时间不能大于结束时间！");
							}
						}
					}
				});
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
			
			$(".save_history_list").click(function(){
				$(".history_list").hide()
			});
			
			$(".cancel_history_list").click(function(){
				$(".history_list").hide()
			});
			
			
			
			$(".more_check").click(function(){
				$(".more_check_").toggle()
			});
			
			
		});
		
	

