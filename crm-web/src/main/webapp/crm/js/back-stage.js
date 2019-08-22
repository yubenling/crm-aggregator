
		$(function(){
			
			/*选项卡切换*/
			$(".binding_store_out").click(function(){
				$(".binding_store").show();
				$(".phone_num_set").hide();
				$(".binding_store_out").addClass("bgc_f1f3f7");
				$(".phone_num_set_out").removeClass("bgc_f1f3f7");
			});

			$(".phone_num_set_out").click(function(){
				$(".phone_num_set").show();
				$(".binding_store").hide();
				$(".phone_num_set_out").addClass("bgc_f1f3f7");
				$(".binding_store_out").removeClass("bgc_f1f3f7");
			});
			
			
			/*添加黑名单选中状态*/
			$(".black_uncheck").click(function(){
				$(this).children().children(".black_check").toggle();
			});
			
			
			/*添加黑名单选中状态*/
			$(".phone_num_check").click(function(){
				$(this).children().children().toggle();
			});
			
			/*取消添加黑名单*/
			$(".cancel_hmd").click(function(){
				$(".hmd").hide();
				$(".hmd_text").val("");
			});
			
			/*取消上传黑名单*/
			$(".cancel_upload").click(function(){
				$(".upload").hide();
			});
			
			/*$(".change_number").click(function(){
				$(".all_change").removeClass("no_click");
				$(".save_cancel").show();
				$(".HQ").show();
				$(".phone_num").attr("disabled",false);
			});*/
//			点击获取验证码出现倒计时
        /*    $('.HQ').click(function(){
            	var phone=$(".SJ").val();
            	if(phone==null || phone==''){
            		$('.TS').show();
  						$('.TS').html('手机号不能为空');
					setTimeout(function(){
					$('.TS').hide();	
					},1000);
					return;
  					}
           	var re = /^1\d{10}$/;
           	if(re.test(phone)){
           		$('.HQ').hide();
  				$('.JS').show();
  				$('.TS').hide();
            	waitTime();
  					}
           	else{
           		$(".TS").show();
					setTimeout(function(){
						$(".TS").hide()
					},1000)
           		$('.JS').hide();
           	}
            	
            })		
            //60秒倒计时函数
			function waitTime(){
				var T=60;
				timer=null;
				timer=setInterval(function(){
					T--;
					document.getElementById('timer').innerHTML=T;
					if(T==0){
						clearInterval(timer);
						document.getElementById('timer').innerHTML=60;
						$('.JS').hide();
						$('.HQ').show();
					}	
				},1000)
			}*/
			
			$(".save").click(function(){
				//$(".save_cancel").hide();//点击保存时隐藏编辑框
				//$(".HQ").hide();
				$('.JS').hide();
				//$(".phone_num").attr("disabled",true);//取消点击保存后不能修改
				//$(".all_change").addClass("no_click");//取消点击保存后不能修改
			});
			
			$(".cancel").click(function(){
				$(".save_cancel").hide();
				$(".HQ").hide();
				$('.JS').hide();
				$('.SJ').val('');
			$(".phone_num").attr("disabled",true);
			$(".all_change").addClass("no_click");
			});
			
			$('.BC').click(function(){
				if($('.SJ').val()==null||$('.SJ').val()==''){
					$('.TS').show();
					$('.TS').html('手机号不能为空');
					setTimeout(function(){
					$('.TS').hide();	
					},3000);
					return;
				}
				else if($('.YZM').val()==null||$('.YZM').val()==''){
					$('.TS_1').show();
					$('.TS_1').html('验证码不能为空');
					setTimeout(function(){
					$('.TS_1').hide();	
					},3000);
					return;
				}
				else{
					$(".save_cancel").hide();
				$(".HQ").hide();
				$('.JS').hide();
				$(".phone_num").attr("disabled",true);
				$(".all_change").addClass("no_click");
				}
			})
			
			
			
			
			
			
			$(".1check_box").click(function(){
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			});
			
			
			
			
		});