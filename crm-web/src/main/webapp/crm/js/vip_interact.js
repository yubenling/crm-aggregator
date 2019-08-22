
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
			
			
			
			$(".GXK").click(function(){
				$(this).addClass("bgc_check_blue")
				$(this).parent().siblings().children(".GXK").removeClass("bgc_check_blue")	
			})
			
			var  DYK1=$(".DYK1");
			var  DYK2=$(".DYK2");
			DYK1.click(function(){
				 /*$(this).css('background','#fff').siblings(".DYK1").css('background','#e3e7f0')*/
				$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
				 $(this).siblings(".DYK1").addClass("bgc_e3e7f0").removeClass("bgc_fff");
				var i=$(this).index();				
				 DYK2.hide();
				 DYK2.eq(i).show();
			});
			
			
			
			$(".ShopLianJie").click(function(){
				$(".ShopId").show();	
				$(".HuoDongWangZhi").hide();
			});
			$(".DianPuShouYe").click(function(){
				$(".ShopId").hide();
				$(".HuoDongWangZhi").hide();
			});
			$(".HuoDongLianJie").click(function(){
				$(".ShopId").hide();
				$(".HuoDongWangZhi").show();
			})
			
			
			
			
			
			
			
			


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
		
		
			/*点击显示短信详情*/
			$(".text_detail").click(function(){
				$(this).toggleClass("one_line_only lh50 h50");
			});
					
		
			

			
			


			
			/*发送短信弹窗*/
			$(".send_text_btn").click(function(){
				//关闭滚动条
				closeOverFlow();
				$(".text_window").show()
//				$("input").val("")
//				$("textarea").val("")
			});
			
			
			
			$(".close").click(function(){
				//开启滚动条
				autoOverFlow();
				$(".text_window").hide()
				$(".chat_window").hide();
			});
			
			
			
			/*聊天弹窗*/
			
			$(".chat_btn").click(function(){
				
				$(".chat_window").show();
//				$("textarea").val("");
//				$("input").val("");
			});
			
		
			
			
			$(".text_area").keyup(function(){
			        $(this).parent().prev().children().children(".text_count").text($(this).val().length);
			});
					
	
		
			/*会员互动选项切换*/
			/*$(".viphd_out").click(function(){
				var i=$(this).index();				
				 	$(".viphd_in").hide();
				 	$(".viphd_in").eq(i).show();
					$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
					$(this).siblings().removeClass("bgc_fff").addClass("bgc_e3e7f0");
					$(".one_check_").hide()
			});*/
			
			
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
			
			
			
			
			
			
			
			
			$(".cdf_1").click(function(){
				confirm(   $(".cdf").eq(0).text() + "," + $(".cdf").eq(1).text() + "," + $(".cdf").eq(2).text() + "," + $(".cdf").eq(3).text() + "," + $(".cdf").eq(4).text() + "," + $(".cdf").eq(5).text()  )
			})
			
			
			$(".ChangeLink").click(function(){
				$(".ChoiceLink").show();
			});
			
			$(".LinkOut").click(function(){
				$(".ChoiceLink").hide();	
			});
			
			var  TaoBaoLianJie1=$(".TaoBaoLianJie1");
			var  TaoBaoLianJie2=$(".TaoBaoLianJie2");
			TaoBaoLianJie1.click(function(){
				 $(this).css('background','#fff').siblings(".TaoBaoLianJie1").css('background','#e3e7f0')
				var i=$(this).index();				
				 TaoBaoLianJie2.hide();
				 TaoBaoLianJie2.eq(i).show();
			});
			
			$(".Library").click(function(){
				 //禁止滚动条  16
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".PhraseLibrary").show();
			});
					
			$(".LibraryOut").click(function(){
				//启用滚动条
				  $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".PhraseLibrary").hide();
			});	
			
			$(".ClickPhrase").click(function(){
				$(".ChoicePhrase").show();	
			});
			
			$(".PhraseOut").click(function(){
				$(".ChoicePhrase").hide();	
			});
			
			var url=$('#src').val();
			
			$('.replyBtn').on('click',function(){
				var phone=$(this).attr('data-phone');
				$('.replyBOX').attr('data-phone',$(this).attr('data-phone'));
				$.ajax({
					url:url+'/smsReceiveInfo/records',
					type:'post',
					data:{
						"phone": phone
					},
					success:function(data){
						var data=$.parseJSON(data)
						$('#markBg2').addClass('on');
						$('.replyBOX').addClass('on');
						$('.showReplyListBox').html('');
						if(data.rc==100){
								
							if(data.data.length>0){
								for(var i=0;i<data.data.length;i++){
									var str='';
									if(data.data[i].role=='seller'){
										str='<div class="clearfix">'
											+'<i class="fr"></i>'
											+'<p class="fr frP"><i></i>'+data.data[i].content+'</p>'
											+'</div>'
										
										$('.showReplyListBox').append(str);
									}else{
										str='<div class="clearfix">'
											+'<i class="fl"></i>'
											+'<p class="fl flP"><i></i>'+data.data[i].content+'</p>'
											+'</div>'
										
										$('.showReplyListBox').append(str);
									}
								}
							}
						}
						
						
					}
				});
			});
			
			$('.closeReplyBOX').on('click',function(){
				$('#markBg2').removeClass('on');
				$('.replyBOX').removeClass('on');
				$('.showReplyListBox').scrollTop(0);
			});
			$('.replyTxtBox textarea').on('keyup',function(){
				var num=$(this).val().length;
				var tiao=Math.ceil(num/70);
				$('.replyNum').text(num);
				$('.replyTiao').text(tiao);
			});
			$('.jfgz a').on('mouseover',function(){
				$(this).siblings('p').show();
			});
			$('.jfgz a').on('mouseout',function(){
				$(this).siblings('p').hide();
			});
			$('.replyBOX .send').on('click',function(){
				
				
				var reg=/(\【)([\S\s\t]*?)(\】)/g;
				
				var str=$('<p id="error" style="width: 320px; margin-left: -160px; z-index: 1000; display: none;">自定义充值金额或充值条数不能为空！</p>');
				if(!reg.test($('.replyTxtBox textarea').val())){
					$('body').append(str);
					$('#error').css({
						width:'320px',
						marginLeft:'-160px',
						'z-index':'1000'
					})
					$('#error').show();
					$('#error').text('必须填写签名！');
					setTimeout(function(){
						$('#error').hide();
					},2000);
					
					return;
				}else if($('.replyTxtBox textarea').val()==''){
					$('body').append(str);
					$('#error').css({
						width:'320px',
						marginLeft:'-160px',
						'z-index':'1000'
					})
					$('#error').show();
					$('#error').text('必须发送内容！');
					setTimeout(function(){
						$('#error').hide();
					},2000);
					
					return;
				}
				$('#markBg2').addClass('on');
				$('.alertBox').addClass('on');
				$('#markBg2').css({
					'z-index':55
				});
				
			});
			$('.alertBox .qr').on('click',function(){
				var that=$(this);
				if($(this).hasClass('on'))return;
				$(this).addClass('on');
				$.ajax({
					url:url+"/smsReceiveInfo/receiveSendSms",
					type: "post",
					data:{
						phone:$('.replyBOX').attr('data-phone'),
						signVal:$('#smsqianming').val(),
						content:$('.replyTxtBox textarea').val()
					},
					success:function(data){
						
						$('#markBg2').removeClass('on');
						$('.replyBOX').removeClass('on');
						$('.alertBox').removeClass('on');

						$('.replyTxtBox textarea').val('【'+$('#smsqianming').val()+'】');
						console.log(data);
						$('#markBg2').css({
							'z-index':50
						});
						setTimeout(function(){
							that.removeClass('on');
						},1500)
						
					}
				});
				
				
			});
			$('.alertBox .qx').on('click',function(){
				$('#markBg2').css({
					'z-index':50
				});
				$('.alertBox').removeClass('on');
			});
			$.ajax({
				url:url+"/systemManage/showShopName",
				type: "post",
				success:function(data){
					var json=$.parseJSON(data);
					$('#smsqianming').val(json.shopName);
					$('.replyTxtBox textarea').val('【'+json.shopName+'】');
					$('.replyNum').text($('.replyTxtBox textarea').val().length);
				}
			});
		});
		function closeOverFlow(){
			//禁止滚动条
			  $(document.body).css({
			    "overflow-x":"hidden",
			    "overflow-y":"hidden"
			  });
		}
		function autoOverFlow(){
			 //启用滚动条
			  $(document.body).css({
			  "overflow-x":"auto",
			  "overflow-y":"auto"
			  });
		}
	

