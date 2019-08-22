		//传递数组  手机号码
		function validatePhone(phoneNum){
			var result = "";
			for(var i=0;i<phoneNum.length;i++){
				if(!(/^1[3|4|5|7|8|9][0-9]\d{8}$/.test(phoneNum[i]))){ 
					result = result+","+phoneNum[i];
				}
			}
			return result;
		}
		
		$(function(){
			$("#phone_number_Content").on("keyup",function(){
				var phoneNums = $("#phone_number_Content").val();
				if(phoneNums!=null && phoneNums!=""){
					var length = phoneNums.length;
					$("#phone_Num").html(length);
				}
			});
			$("#phone_number_Content").on("change",function(){
//				addPhone();
				var phoneNums = $("#phone_number_Content").val();
				if(phoneNums!=null && phoneNums!=""){
					var phoneNum = phoneNums.split(",");
					var result = validatePhone(phoneNum);
					var length = phoneNums.length;
					$("#phone_Num").html(length);
					if(result!=null){
						if(result==""){
//									confirm("手机号码正确");
						}else{
							if(phoneNums.indexOf("，")>-1){
								$(".tishi_2").show();
					    		$(".tishi_2").children("p").text("多个号码使用英文逗号隔开！")
					    		setTimeout(function(){ 
					    			$(".tishi_2").hide()
					    		},3000)
							}else{
								$(".tishi_2").show();
								$(".tishi_2").children("p").text("您输入的手机号码不正确："+result)
								setTimeout(function(){ 
									$(".tishi_2").hide()
								},3000)
							}
						}
					}
				}
			});
			//查看发送记录 手机号正则验证
			$("#phone").on("keyup",function(){
				this.value=this.value.replace(/\D/g,'');
			});
			$("#phone").on("change",function(){
				var phoneNum = $("#phone").val();
				if(!(/^1[3|4|5|7|8|9][0-9]\d{8}$/.test(phoneNum))){
					$("#phone").val("");
					confirm("您输入的手机号码不正确： "+phoneNum);
				}
			});
			//发送时间
			$("#tser07").on("click",function(){
				$.jeDate('#tser07',{
					insTrigger:false,
					isTime:true,
					format:'YYYY-MM-DD',
					choosefun:function(elem, val) {
						var startTime = $(tser07).val();
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
						var startTime = $(tser07).val();
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
						var startTime = $(tser07).val();
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
						var startTime = $(tser07).val();
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
			
			
			
			
			
			$(".out").click(function(){
				var i=$(this).index();				
				 	$(".in").hide();
				 	$(".in").eq(i).show();
				$(this).addClass("bgc_e3e7f0");
				$(this).siblings().removeClass("bgc_e3e7f0");
				
				$(".one_check_only_").hide();
				$(".group_check_").hide();
				//$("input").val("")
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
				//$("input").val("")
			});
			
			/*指定号码发送选项切换*/
			$(".text_designate_out").click(function(){
				var i=$(this).index();				
				 	$(".text_designate_in").hide();
				 	$(".text_designate_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					//$("input").val("")
			});
			
			/*效果分析选项切换*/
			$(".detail_out").click(function(){
				var i=$(this).index();				
				 	$(".detail_in").hide();
				 	$(".detail_in").eq(i).show();
					$(this).addClass("bgc_00a0e9 c_fff").removeClass("c_cad3df");
					$(this).siblings().removeClass("bgc_00a0e9 c_fff").addClass("c_cad3df");
					$(".one_check_").hide()
					//$("input").val("")
			});
			
			/*效果分析选项切换*/
			$(".enter_outside").click(function(){			
				 	$(".outside").show();		
				 	$(".detail").hide();
			});
			
			/*效果分析选项切换*/
			$(".enter_detail").click(function(){			
				 	$(".detail").show();		
				 	$(".outside").hide();
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
			$(".one_check_only_555").click(function(){
				$(this).children(".one_check_").show();
				$(this).parent().siblings().children().children(".one_check_").hide();
			});
			$(".group_check").click(function(){
				$(this).children(".group_check_").toggle();
				$(this).parent().siblings().children().children(".group_check_").hide();
			});
			
			
			
			
			
			
			$(".short_url_btn").click(function(){
    			$(".short_url").show();
    		});
    		$(".save_short_url").click(function(){
    			$(".short_url").hide();
    		});
    		
    		
    		
    		
    		$(".text_model_btn").click(function(){
    			//禁止滚动条
    			  $(document.body).css({
    			    "overflow-x":"hidden",
    			    "overflow-y":"hidden"
    			  });
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
    			$(".upload_mail_list").hide();
    			$(".download").hide();
    			$(".analysed").hide();
    		});
    		
    		$(".closeError").click(function(){
    			//启用滚动条
    			  $(document.body).css({
    			  "overflow-x":"auto",
    			  "overflow-y":"auto"
    			  });
    			$(".showError").hide();
    		});
    		
    		
    		$(".download_btn").click(function(){
    			$(".download").show();
    		});
    		
    		
    		$(".analysed_btn").click(function(){
    			$(".analysed").show();
    		});
			
			
			
			
			
			
			$(".qianming_none").click(function(){
    			$(".qianming_").attr("disabled","disabled");
    		});
    		
    		$(".qianming_done").click(function(){
    			$(".qianming_").removeAttr("disabled");
    		});
			
			
			$(".manual_out").click(function(){
				$(".manual_in").show();
				$(".upload_in").hide();
				$(this).children(".o_check_only").children(".o_check_").show();
				$(this).siblings().children(".o_check_only").children(".o_check_").hide();
			});
			$(".upload_out").click(function(){
				$(".upload_in").show();
				$(".manual_in").hide();
				$(this).children(".o_check_only").children(".o_check_").show();
				$(this).siblings().children(".o_check_only").children(".o_check_").hide();
			});
			
			
			$(".upload_mail_list_btn").click(function(){
				$(".upload_mail_list").show()
			});
			
			
			/*点击显示短信详情*/
			$(".text_detail").click(function(){
				$(this).toggleClass("one_line_only lh50 h50");
			});
			
			
			
			$(".hover_example").hover(
				  function() {
				  	$(".hover_example_hide").show();
				  }, function() {
				  	$(".hover_example_hide").hide();
				  }
			);
			$(".hover_example_hide").hover(
					  function() {
					  	$(".hover_example_hide").show();
					  }, function() {
					  	$(".hover_example_hide").hide();
					  }
				);
			
			
			
			
			
			
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
			
			
			
		   /* $(".text_area").keyup(function(){  
//		        $(this).next().children(".text_count").text($(this).val().length);
		        $(".text_area_copy").text($(this).val())
			});*/
    		
    		
    		$(".short_url_btn").click(function(){
    			 //禁止滚动条
    			  $(document.body).css({
    			    "overflow-x":"hidden",
    			    "overflow-y":"hidden"
    			  });
    			$(".short_url").show();
    		});
    		$(".save_short_url").click(function(){
    			//启用滚动条
    			  $(document.body).css({
    			  "overflow-x":"auto",
    			  "overflow-y":"auto"
    			  });
    			$(".short_url").hide();
    		});
    		
    		
    		
    		/*选项切换*/
			$(".short_out").click(function(){
				var i=$(this).index();				
				 	$(".short_in").hide();
				 	$(".short_in").eq(i).show();
			});
    		
    		
    		
    		$(".set_time").click(function(){
				$(".set_time_").show();
			});
			$(".set_time_none").click(function(){
				$(this).children().find("img").show();
				$(".set_time_").hide();
				$(".set_time").children("img").hide();
			});
    		
    		$(".detail_btn").click(function(){
				$(".detail").toggle();
			});
    		
    		
    		$(".text_test_btn").click(function(){
    			$(".text_test_window").show();
    		});
    		
    		
    		
    		$(".text_model").click(function(){
    			$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
    			$(this).siblings(".text_model").addClass("bgc_e3e7f0").removeClass("bgc_fff");
    		});
			
		});
		
	

