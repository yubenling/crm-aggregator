
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




			/*黑名单选项切换*/
			$(".hmd_out").click(function(){
				var i=$(this).index();				
				 	$(".hmd_in").hide();
				 	$(".hmd_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
			});
			
			
			
			$(".close").click(function(){
				$(".upload").hide();
				$(".hmd").hide();
			});
			
			
			$(".add").click(function(){
				$(".hmd").show()
			});
			
			$(".lots_upload").click(function(){
				$(".upload").show()
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
			
			
			
			
			
			
			
			
			$(".yichu").click(function(){
				
				$(".yichu_1").show();
			});
			
			$(".close").click(function(){
				$(".yichu_1").hide();
				
			});
			
			
			
			
			

			/*手机好*/
			var phone_=/^1[3|4|5|7|8]\d{9}$/
			
			$(".preserve_phone").click(function(){
				var mobile = $("#mobile").val();
				var remark = $("#remark").val();
				mobile =mobile.replace(/\s+/g, "");
				if(!(/^1[34578]\d{9}$/.test(mobile))){ 
					if(confirm("手机号码有误，请重填")){
					}
			        return false; 
			    } 
				if ($(".p2").val()!='') {
					$(".p1").hide();
					$(".hmd").hide();
					preserve(mobile,remark);
					
				} else{
					$(".p1").show();
					//$(".p1").text("手机号错误");
					//confirm("手机号错误！");
				}			
			});

		});
		
	

