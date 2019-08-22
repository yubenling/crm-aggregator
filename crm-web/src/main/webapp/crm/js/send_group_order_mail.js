
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


			$(".more_detail_btn").click(function(){
    			$(".more_detail").toggle();
    		});
    		
    		$(".custom").click(function(){
    			$(".custom_hide").show();
    		});
    		
    		$(".custom_none").click(function(){
    			$(".custom_hide").hide();
    		});

			
    		//勾选框
    		/*$(".GXK").click(function(){
				$(this).toggleClass("bgc_check_blue")
				$(this).parent().siblings().children(".GXK").removeClass("bgc_check_blue")	
			});*/
    		$(document).on("click", ".GXK", function () {
				$(this).toggleClass("bgc_check_blue");
				$(this).parent().siblings().children(".GXK").removeClass("bgc_check_blue");
    		});
			
			/*选项切换*/
			$(".out").click(function(){
				var i=$(this).index();				
				 	$(".in").hide();
				 	$(".in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					$("input").val("")
			});
			
			/*选项切换*/
			$(".window_out li").click(function(){/*
				var i=$(this).index();				
				 	$(".window_in").hide();
				 	$(".window_in").eq(i).show();*/
					$(this).addClass("bgc_fff");
					$(this).siblings().removeClass("bgc_fff");
			});
			
			$(".order_mail_window_btn").click(function(){
				$(".order_mail_window").show();
			});
			
			
			$(".close_this").click(function(){
				//启用滚动条
				  $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".order_mail_window").hide();
				/*$("input").val("");
				$("textarea").val("");
				$(".group_check_").hide();*/
			});
			
		
			
			
			/*table背景色*/
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/*table高度，行高*/
			$("tr").css("height","2.6vw");
			
		
			
			
			
			
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
			$(".group_check").click(function(){
				$(this).children(".group_check_").toggle();
				/*$(this).parent().siblings().children().children(".group_check_").hide();*/
			});
			$(".group_check1").click(function(){
				$(this).parent().siblings().children().children(".group_check_").hide();
			});
			
			
			$(".time_show").click(function(){
				$(".time_hide").toggle();
			});
			
			$(".time_show_unable").click(function(){
				$(".time_hide").hide();
			});
			
			
			
			$(".resetting").click(function(){
				$("input").val("");
				$(".group_check_").hide();
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
			
			
			
			
			$(".1check_box").click(function(){
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			});
			
			
			//发送时间
			$("#tser11").on("click",function(){
				$.jeDate('#tser11',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD hh:mm:ss',
					choosefun:function(elem, val) {
						var startTime = $(tser11).val();
						var endTime = $(tser12).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser11).val('');
								$(tser12).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser11).val();
						var endTime = $(tser12).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser11).val("");
								$(tser12).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					}
				});
			});
			//结束时间
			$("#tser12").on("click",function(){
				$.jeDate('#tser12',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD hh:mm:ss',
					choosefun:function(elem, val) {
						var startTime = $(tser11).val();
						var endTime = $(tser12).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser11).val("");
								$(tser12).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser11).val();
						var endTime = $(tser12).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser11).val("");
								$(tser12).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					}
				});
			});
			
			//发送时间
			$("#tser13").on("click",function(){
				$.jeDate('#tser13',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD hh:mm:ss',
					choosefun:function(elem, val) {
						var startTime = $(tser13).val().getTime();
						var endTime = $(tser14).val().getTime();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime);
							var endTimeDate = new Date(endTime);
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser13).val("");
								$(tser14).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser13).val();
						var endTime = $(tser14).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser13).val("");
								$(tser14).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					}
				});
			});
			//结束时间
			$("#tser14").on("click",function(){
				$.jeDate('#tser14',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD hh:mm:ss',
					choosefun:function(elem, val) {
						var startTime = $(tser13).val();
						var endTime = $(tser14).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser13).val("");
								$(tser14).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser13).val();
						var endTime = $(tser14).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser13).val("");
								$(tser14).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					}
				});
			});
			
			
			
			
			//发送时间
			$("#tser15").on("click",function(){
				$.jeDate('#tser15',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD hh:mm:ss',
					choosefun:function(elem, val) {
						var startTime = $(tser15).val();
						var endTime = $(tser16).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser15).val("");
								$(tser16).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser15).val();
						var endTime = $(tser16).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser15).val("");
								$(tser16).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					}
				});
			});
			//结束时间
			$("#tser16").on("click",function(){
				$.jeDate('#tser16',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD hh:mm:ss',
					choosefun:function(elem, val) {
						var startTime = $(tser15).val();
						var endTime = $(tser16).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser15).val("");
								$(tser16).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					},
					okfun:function(elem, val) {
						var startTime = $(tser15).val();
						var endTime = $(tser16).val();
						if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
							var startTimeDate = new Date(startTime).getTime();
							var endTimeDate = new Date(endTime).getTime();
							console.log(startTimeDate>endTimeDate);
							if(startTimeDate > endTimeDate){
								$(tser15).val("");
								$(tser16).val("");
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000);
							}
						}
					}
				});
			});
			
			
			
			
			
			
			
			
			
			

			
			
			
			
			
			
			
			
			
			
			
			
		});
		
	

