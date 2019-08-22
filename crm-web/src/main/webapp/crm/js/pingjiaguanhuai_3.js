
		$(function(){
			
			
			/*点击开关提醒*/
//			$(".green_check").click(function(){
//				if (   $(this).attr("src")=="images/green_check.png"   ) {
//					$(this).attr({"src":"images/green_uncheck.png"})
//				} else{
//					$(this).attr({"src":"images/green_check.png"})
//				}
//			});
//			
//			/*点击顶部设置按钮显示右边框*/
//			$(".right_top_set_btn").click(function(){
//				$(".right_top_set").toggle()
//			});
//			
//			/*顶部时间显示*/
//			setInterval(function() {
//		    	var now = (new Date()).toLocaleString();
//		   		$('.time').text(now);
//			}, 1000);
			
					/*关怀评价选项切换*/
			$(".ddzx_in_out").click(function(){
				var i=$(this).index();				
				 	$(".p_1").hide();
				 	$(".p_1").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
					/*$("input").val("")*/
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

			
			$('.kaiqi').click(function(){
				
					$('.guanbi').children().hide();
				
			})
			$('.guanbi').click(function(){
				
					$('.kaiqi').children().hide();
				
			})
			
		$(".moban1").click(function(){
				var i=$(this).index();				
				 	$(".moban2").hide();
				 	$(".moban2").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					/*$("input").val("");*/
			});		
			

		$("#more").mouseover(function(){
            $(".bind_ol").show();
       });
//      $("#more").mouseout(function(){
//          $(".bind_ol").hide();
//      });
        $(".bind_ol").mouseover(function(){
            $(".bind_ol").show();
       });
        $(".bind_ol").mouseout(function(){
            $(".bind_ol").hide();
        });
        
        $(".ZhanKai").click(function(){
 			if($(".LiuCheng").css("display")=="none"){
 				$(".LiuCheng").slideDown(200);
 			}else if($(".LiuCheng").css("display")=="block") {$(".LiuCheng").slideUp(200)};
 			$(".JTS").toggle();
 			$(".JTX").toggle();
 			if($(".JTX").css("display")=="none"){$(".LiuChengText").text("收回流程")};
 			if($(".JTS").css("display")=="none"){$(".LiuChengText").text("展开流程")};
 			
 		})
		});