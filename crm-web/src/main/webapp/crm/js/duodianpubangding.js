
		$(function(){
			
			/*选项卡切换*/
			$(".binding_store_out").click(function(){
				$(".binding_store").show();
				$(".phone_num_set").hide();
			});

			$(".phone_num_set_out").click(function(){
				$(".phone_num_set").show();
				$(".binding_store").hide();
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
			})
			
			/*取消上传黑名单*/
			$(".cancel_upload").click(function(){
				$(".upload").hide();
			})
			
			
		});