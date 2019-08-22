
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







		/*拖拽框*/
    var clicked = "Nope.";
    var mausx = "0";
    var mausy = "0";
    var winx = "0";
    var winy = "0";
    var difx = mausx - winx;
    var dify = mausy - winy;

    $("html").mousemove(function (event) {
        mausx = event.pageX;
        mausy = event.pageY;
        winx = $(".chat_window").offset().left;
        winy = $(".chat_window").offset().top;
        if (clicked == "Nope.") {
            difx = mausx - winx;
            dify = mausy - winy;
        }

        var newx = event.pageX - difx - $(".chat_window").css("marginLeft").replace('px', '');
        var newy = event.pageY - dify - $(".chat_window").css("marginTop").replace('px', '');
        $(".chat_window").css({ top: newy, left: newx });

        $(".container").html("Mouse Cords: " + mausx + " , " + mausy + "<br />" + "Window Cords:" + winx + " , " + winy + "<br />Draggin'?: " + clicked + "<br />Difference: " + difx + " , " + dify + "");
    });

    $(".pew").mousedown(function (event) {
        clicked = "Yeah.";
    });

    $("html").mouseup(function (event) {

        clicked = "Nope.";
    });
			
			
			
			
			
			
			
			
			
			/*客户管理选项切换*/
			$(".khgl_out").click(function(){
				var i=$(this).index();				
				 	$(".khgl_in").hide();
				 	$(".khgl_in").eq(i).show();
				$(this).addClass("bgc_e3e7f0");
				$(this).siblings().removeClass("bgc_e3e7f0");
				$(".one_check_").hide();
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
			
			/*黑名单选项切换*/
			$(".viphd_out").click(function(){
				var i=$(this).index();				
				 	$(".viphd_in").hide();
				 	$(".viphd_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
			});
			
			/*黑名单选项切换*/
			$(".discount_out").click(function(){
				var i=$(this).index();				
				 	$(".discount_in").hide();
				 	$(".discount_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
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
			
			
			
			
			
			
			/*取消打折*/
			$(".cancel_all_discount").click(function(){
				$(".cancel_discount_window").show()
			});
			$(".cancel_discount_chat_windowbtn").click(function(){
				$(".cancel_discount_window").hide()
			});
			$(".cancel_discount_cancel").click(function(){
				$(".cancel_discount_window").hide()
			});
			$(".cancel_discount_save").click(function(){
				$(".cancel_discount_window").hide()
			});
			
			
			/*保存打折*/
			$(".save_all_discount").click(function(){
				$(".save_discount_window").show()
			});
			$(".cancel_discount_chat_windowbtn").click(function(){
				$(".save_discount_window").hide()
			});
			$(".save_discount_save").click(function(){
				$(".save_discount_window").hide()
			});
			$(".save_discount_cancel").click(function(){
				$(".save_discount_window").hide()
			});
			
			
			
			/*创建分组*/
			$(".add_group_btn").click(function(){
				$(".add_group").show();
				$(".group_check_").hide();
				$(".explain").val("");
				$(".group_name").val("");
			});
			
			$(".save_group").click(function(){
				$(".add_group").hide();
			});
			
			$(".cancel_group").click(function(){
				$(".add_group").hide();
			});
			
			
			/*点击显示短信详情*/
			$(".text_detail").click(function(){
				$(this).toggleClass("one_line_only lh50 h50");
			});
			
			
			/*发送短信弹窗*/
			$(".send_text_btn").click(function(){
				$(".send_text_window").show()
				$("input").val("")
				$("textarea").val("")
			});
			
			$(".save_text").click(function(){
				$(".send_text_window").hide()
			});
			
			$(".cancel_text").click(function(){
				$(".send_text_window").hide()
			});
			
			
			
			
			/*聊天弹窗*/
			
			$(".chat_btn").click(function(){
				
				$(".chat_window").show();
				$("textarea").val("");
				$("input").val("");
			});
			
			$(".close_chat_window").click(function(){
				$(".chat_window").hide();
			});
			
			$(".save_chat").click(function(){
				$(".chat_window").hide();
			});
			
			$(".cancel_chat").click(function(){
				$(".chat_window").hide();
			});
			
			
			
			$(".text_area").keyup(function(){
			        $(this).parent().prev().children().children(".text_count").text($(this).val().length);
			});
			
		});
		
	

