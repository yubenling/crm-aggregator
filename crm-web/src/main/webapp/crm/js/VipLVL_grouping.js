
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
			
			$(".huafen_btn").click(function(){
				$(".huafen_win").show()
			});
			
			/*保存打折*/
			$(".save_all_discount").click(function(){
				var amount1= $("#member1_0").val();
				var num1 = $("#member2_0").val();
				var amount2= $("#member1_1").val();
				var num2 = $("#member2_1").val();
				var amount3= $("#member1_2").val();
				var num3 = $("#member2_2").val();
				var amount4= $("#member1_3").val();
				var num4 = $("#member2_3").val();
				var zk1 = $("#member3_0").val();
				var zk2 = $("#member3_1").val();
				var zk3 = $("#member3_2").val();
				var zk4 = $("#member3_3").val();
				if(zk4==""||zk3==""||zk2==""||zk1==""){
					confirm("折扣不能为空！");
					return;
				}
				if(parseFloat(zk2)>=parseFloat(zk1)||parseFloat(zk3)>=parseFloat(zk2)||parseFloat(zk4)>=parseFloat(zk3)||parseFloat(zk1)<7||parseFloat(zk2)<7||parseFloat(zk3)<7||parseFloat(zk4)<7){
					confirm("请输入正确的折扣！");
					return;
				}
				if(Number(amount2)<=Number(amount1)||Number(amount3)<=Number(amount2)||Number(amount4)<=Number(amount3)){
					confirm("请输入正确的交易额！");
					return;
				}
				if(Number(num2)<=Number(num1)||Number(num3)<=Number(num2)||Number(num4)<=Number(num3)||Number(num1)<1||Number(num2)<1||Number(num3)<1||Number(num4)<1){
					confirm("请输入正确的交易数量！");
					return;
				}
				if(!(/(^[1-9]\d*$)/.test(num1))||!(/(^[1-9]\d*$)/.test(num2))||!(/(^[1-9]\d*$)/.test(num3))||!(/(^[1-9]\d*$)/.test(num4))){
					confirm("请输入正确的交易数量！");
					return;
				}
				$(".save_discount_window").show();
				/*if (   $(".edu_").eq(0).children("input").val()>$(".edu_").eq(1).children("input").val()  ||$(".edu_").eq(1).children("input").val()>$(".edu_").eq(2).children("input").val()  ||$(".edu_").eq(2).children("input").val()>$(".edu_").eq(3).children("input").val() ||$(".edu_").eq(0).children("input").val()=="" ||$(".edu_").eq(1).children("input").val()=="" ||$(".edu_").eq(2).children("input").val()=="" ||$(".edu_").eq(3).children("input").val()==""   ) {
					confirm("交易额度填写错误");
				} else
				if (   $(".del_num").eq(0).children("input").val()>$(".del_num").eq(1).children("input").val()  ||$(".del_num").eq(1).children("input").val()>$(".del_num").eq(2).children("input").val()  ||$(".del_num").eq(2).children("input").val()>$(".del_num").eq(3).children("input").val() ||$(".del_num").eq(0).children("input").val()=="" ||$(".del_num").eq(1).children("input").val()=="" ||$(".del_num").eq(2).children("input").val()=="" ||$(".del_num").eq(3).children("input").val()==""   ) {
					confirm("交易笔数填写错误");
				} else
				if (   parseInt($(".count_num").eq(0).children("input").val())<parseInt($(".count_num").eq(1).children("input").val())  ||parseInt($(".count_num").eq(1).children("input").val())<parseInt($(".count_num").eq(2).children("input").val())  ||parseInt($(".count_num").eq(2).children("input").val())<parseInt($(".count_num").eq(3).children("input").val())  ||	parseInt($(".count_num").eq(0).children("input").val()<5 ||$(".count_num").eq(1).children("input").val())<5 ||parseInt($(".count_num").eq(2).children("input").val())<5 ||parseInt($(".count_num").eq(3).children("input").val())<5  ||  parseInt($(".count_num").eq(0).children("input").val())>11 ||parseInt($(".count_num").eq(1).children("input").val())>11 ||parseInt($(".count_num").eq(2).children("input").val())>11 ||parseInt($(".count_num").eq(3).children("input").val())>11   ) {
					confirm("折扣填写错误");
				} else{
					
				}*/
				
				
				
			});
			
			$(".close").click(function(){
				$(".save_discount_window").hide()
				$(".add_group").hide();
				$(".cancel_discount_window").hide()
				$(".huafen_win").hide()
			});
			
			
			
			
			
			/*点击显示短信详情*/
			$(".text_detail").click(function(){
				$(this).toggleClass("one_line_only lh50 h50");
			});
			
			
			
			
			
			
			
			
			
			
			/*var reg = /^\+?[1-9]\d*$/;
			$(".del_num").eq(0).children("input").keyup(function(){
				
				if (   $(".del_num").eq(0).children("input").val()>$(".del_num").eq(1).children("input").val()   ) {
					$(".dif_lvl").eq(0).children("p").text("交易笔数必须小于高等级VIP");
					$(".dif_lvl").eq(0).children("p").show();
				}else{
					$(".dif_lvl").eq(0).children("p").hide();
				}
				
			});
			$(".del_num").eq(1).children("input").keyup(function(){
				
				if (   $(".del_num").eq(1).children("input").val()>$(".del_num").eq(0).children("input").val()   ) {
					$(".dif_lvl").eq(0).children("p").hide();
				} else
				
				if (   $(".del_num").eq(1).children("input").val()>$(".del_num").eq(2).children("input").val()   ) {
					$(".dif_lvl").eq(1).children("p").text("交易笔数必须小于高等级VIP");
					$(".dif_lvl").eq(1).children("p").show();
				}else
				
				{
					$(".dif_lvl").eq(1).children("p").hide();
				}
				
			});
			$(".del_num").eq(2).children("input").keyup(function(){
				
				if (   $(".del_num").eq(2).children("input").val()>$(".del_num").eq(1).children("input").val()   ) {
					$(".dif_lvl").eq(1).children("p").hide();
				} else
				
				if (   $(".del_num").eq(2).children("input").val()>$(".del_num").eq(3).children("input").val()   ) {
					$(".dif_lvl").eq(2).children("p").text("交易笔数必须小于高等级VIP");
					$(".dif_lvl").eq(2).children("p").show();
				}else{
					$(".dif_lvl").eq(2).children("p").hide();
				}
				
			});
			$(".del_num").eq(3).children("input").keyup(function(){
				
				if (   $(".del_num").eq(3).children("input").val()>$(".del_num").eq(2).children("input").val()   ) {
					$(".dif_lvl").eq(2).children("p").hide();
				} else
				
				if (   $(".del_num").eq(3).children("input").val()<$(".del_num").eq(2).children("input").val()   ) {
					$(".dif_lvl").eq(3).children("p").text("交易笔数必须大于低等级VIP");
					$(".dif_lvl").eq(3).children("p").show();
				}else{
					$(".dif_lvl").eq(3).children("p").hide();
				}
				
			});*/
			
			
			
			
			
			
			
			
			
		});
		
	

