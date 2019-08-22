		$(function(){
			if ($("div").hasClass("bgc_check_blue")   ) {
					$("div.bgc_check_blue").next().find("input").val("1");
				} else{
					$("div.bgc_check_blue").next().find("input").val("");
				}
			$(".C_XG").click(function(){
					$(this).parent().parent().siblings(".die_SZ").hide();
					$(this).parent().parent().siblings(".Live_SZ").show();
					
			});
			
// $(".Save").click(function(){
// $(".Time").text()==$(".Time11").val();
// $(".Live_SZ").hide();
// $(".die_SZ").show();
// $(".Live_SZ22").hide();
//					
// })
//		
//			$(".Cancel").click(function(){
//					$(".Live_SZ").hide();
//					$(".die_SZ").show();
//					$(".Live_SZ22").hide();
//			
//			})
// 点击高级设置里面修改
			$('.Dx2').click(function(){
				$('.Live_SZ22').show();
				$(".die_SZ").hide();
			    $(".Live_SZ").show();
			    $(".WeiZhiDingShop").toggleClass("display_none");
				$('.QiTaWeiZhiDing').removeClass("bgc_check_blue");
				
			})
					$(".Dx").click(function(){
// $(this).parent().parent().siblings(".die_SZ").hide();
// $(this).parent().parent().siblings(".Live_SZ").show();
					$(".Live_SZ22").show();
			});
			
			
						/* 下拉框 */
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
				$(timeMSG).empty();
				var endTime = $("#endTime").val();
				var startTime = $("#startTime").val();
				if(startTime==undefined || endTime == undefined){
				}else{
					if(endTime!="" && startTime!=""){
						var endTimeFloat = parseFloat(endTime.substring(0,2));
						var startTimeFloat = parseFloat(startTime.substring(0,2));
						if(endTimeFloat<startTimeFloat){
							$('#endTime').val('');
							$('#startTime').val('');
							$(timeMSG).html("<font style='color: red;'>结束时间不能小于开始时间</font>");
						}else if(endTimeFloat==startTimeFloat){+
							$('#endTime').val('');
							$('#startTime').val('');
							$(timeMSG).html("<font style='color: red;'>结束时间不能等于开始时间</font>");
						}
					}
				}
//				console.log("正在修改  endTime ="+$(endTime).val());
//				console.log("正在修改  startTime = "+$(startTime).val());
				$('[name="nice-select"] ul').hide();
				e.stopPropagation();
			});
			$(document).click(function(){
				$('[name="nice-select"] ul').hide();
			});
			
// 点击下一步弹框
            $(".selectnx").click(function(){
              $(".selectnx1").show();	
            })
			
			
			$(".JiangZheHu").mouseover(function(){
				$(this).find("span").show();
			});
			$(".JiangZheHu").mouseout(function(){
				$(this).find("span").hide();
			});

        // 点击更多start
        $("#more").mouseover(function(){
            $(".bind_ol").show();
        });
        $("#more").mouseout(function(){
            $(".bind_ol").hide();
        });
        // 点击更多end
			
			
			
			
			
			
			
			/* 点击开关提醒 */
//			$(".green_check").click(function(){
//				if (   $(this).attr("src")=="images/green_check.png"   ) {
//					$(this).attr({"src":"images/green_uncheck.png"})
//				} else{
//					$(this).attr({"src":"images/green_check.png"})
//				}
//			});
//			
//			/* 点击顶部设置按钮显示右边框 */
//			$(".right_top_set_btn").click(function(){
//				$(".right_top_set").toggle()
//			});
//			
//			/* 顶部时间显示 */
//			setInterval(function() {
//		    	var now = (new Date()).toLocaleString();
//		   		$('.time').text(now);
//			}, 1000);
			
			
// 点击修改
            $(".change").click(function(){
            	$(".change_1").hide();
            	$(".change_2").show();
            });

  // 点击短信设置修改
           $(".f_change").click(function(){
           	$(".f_change1").hide();
           	$(".f_change2").show();
           })
// 点击取消
          $(".f_cancel").click(function(){
           	$(".f_change2").hide();
           	$(".f_change1").show();
           })
// // 点击保存
// $(".f_save").click(function(){
// $(".f_change2").hide();
// $(".f_change1").show();
// $(".f_area1").val($(".f_area2").val())
// });
// 点击确定以后弹出其他指定商品
       $(".Qita").click(function(){
           	$(".Qita1").show();
           })     			
       			
       			
            	
            	
            	/*
				 * if ( a<b ) { confirm("不符合规范") } else if ( b=0 ) {
				 * confirm("b不能为零") } else if ( a==b ) {
				 * $(".total_money").val("111"); } else{ $(".change_2").hide();
				 * $(".change_1").show(); }
				 */
            	
	
            	
            

// 点击添加指定宝贝
           $('.select').click(function(){
           	$('.select1').show();
           })
            $('.select3').click(function(){
            	$('.select1').hide();
            })
             // 点击确定
               $('.select2').click(function(){
            	$('.select1').hide();
            	('.selectnx1').show()
            })
               
               
               
// 点击保存
   	 $('.Saveb').click(function(){
    	$('.select1').hide();
           	$('.selectnx1').hide();
           	$(".bl1").hide();
           	$(".Yitianjia").show();
           })
// 点击取消
        $(".Savebb").click(function(){
        	$('.select1').hide();
           	$('.selectnx1').hide();
           	$(".bl1").hide();
           	$(".Yitianjia").show();
        })
		
		
		$(".C_XG").click(function(){
					$(this).parent().parent().siblings(".die_SZ").hide();
					$(this).parent().parent().siblings(".Live_SZ").show();
					
			});
			
			$(".Save").click(function(){
					$(".Time").text()==$(".Time11").val();
					$(".Live_SZ").hide();
					$(".die_SZ").show();
					
			})
			// 点击变量下拉框
         
       		$(".bl").click(function(){
            	$(".bl1").toggle();
            	
            })
		
			$(".Cancel").click(function(){
					$(".Cancel22").hide();
					$(".Cancel11").show();
					$(".bl1").hide()
			
			})






		$(".test_number").blur(function(){
			if (   $(this).val()!=""   ) {
				$(".test").bind("click",function(){
					confirm("这是给后台看的")
				})
			} else{
				$(".test").unbind()
			}
		});
		
//		// 点击保存
//            $(".save").click(function(){
//            	var a = parseFloat( $(".Money").val() );
//            	var b = parseFloat( $(".Money2").val() )
//       			$(".Time").text($(".Time11").val());
//       			$(".Time2").text($(".Time22").val());
//       			$(".Money").text($(".Money11").val()+"~");
//				$(".Money2").text($(".Money22").val());
//				$(".Time3").text($(".Time33").val());
//       			$(".Letter").val($(".Letter11").val());
//       			if (   a>b   ) {
//            		// confirm("a需小于b")
//       			} else
//       			
//       			if (   a<0   ) {
//            		// confirm("a需大于0")
//       			} else
//       			
//       			if (   b<0   ) {
//            		// confirm("b需大于0")
//       			} else{
//	            	$(".change_2").hide();
//	            	$(".change_1").show();
//       			}
//       			
//       				
//       			
//       			
//       			
//       			
//            	
//            	
//            	/*
//				 * if ( a<b ) { confirm("不符合规范") } else if ( b=0 ) {
//				 * confirm("b不能为零") } else if ( a==b ) {
//				 * $(".total_money").val("111"); } else{ $(".change_2").hide();
//				 * $(".change_1").show(); }
//				 */
//            });
			
			
			// <!-------------- 选项卡 ---------------->
			var  TABLE1=$(".TABLE1");
			var  TABLE2=$(".TABLE2");
			TABLE1.click(function(){
				 $(this).css('background','#fff').siblings(".TABLE1").css('background','#e3e7f0')
				var i=$(this).index();				
				 TABLE2.hide();
				 TABLE2.eq(i).show();
				 $(".Live_SZ22").hide()
				 
				 $("#HEADER").text($(this).text());
			});
			
			
			<!-------------    勾选框    ----------------------->
			$(".GXK").click(function(){
				$(this).addClass("bgc_check_blue")
				$(this).parent().siblings().children(".GXK").removeClass("bgc_check_blue")	
			})
			
			
			
			// <!------------- 商品弹框 ----------------------->
			
			$(".AddSpecified").click(function(){
				// 禁止滚动条
				$(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".ChoiceSpecified").show();
			});
			$(".XuanZePaiCheSpecified").click(function(){
				$(".ChoiceSpecified").show();
			});
			
			$(".SpecifiedOut").click(function(){
				// 启用滚动条
			 $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".ChoiceSpecified").hide();	
			});
			
			
			$(".AllShop").click(function(){
				$(this).parent().siblings(".AddSpecified").hide();	
				$(this).parent().siblings(".qitashop").hide();
				$(this).parent().siblings(".XuanZePaiCheSpecified").hide();	 
			});
			$(".PaiChuShop").click(function(){
				$(this).parent().siblings(".AddSpecified").hide();	
				$(this).parent().siblings(".qitashop").hide();
				$(this).parent().siblings(".WeiZhiDingShop").addClass("display_none");
				$(this).parent().siblings().find(".QiTaWeiZhiDing").removeClass("bgc_check_blue");
				$(this).parent().siblings(".XuanZePaiCheSpecified").show();
				
			});
			$(".ZhiDingShop").click(function(){
				$(this).parent().siblings(".AddSpecified").show();	
				$(this).parent().siblings(".qitashop").show();
				$(this).parent().siblings(".XuanZePaiCheSpecified").hide();	
			});
			
			$(".QiTaWeiZhiDing").click(function(){
				$(this).parent().siblings(".WeiZhiDingShop").toggleClass("display_none");
				$(this).toggleClass("bgc_check_blue");
			});
			
			$(".DeleteQuxiao").click(function(){
				$(".WeiZhiDingShop").addClass("display_none");
				$(".QiTaWeiZhiDing").removeClass("bgc_check_blue");
			});
			
			
			
			
			
			<!-------------    地区弹框    ----------------------->
			
			$(".CustomArea").click(function(){
				$(".ChoiceArea").show();
			});
			
			$(".ArerOut").click(function(){
				$(".ChoiceArea").hide();	
			});
			
			$(".QuanXuan").click(function(){
				$(".ChoiceArea").find("div.GXK").addClass("bgc_check_blue");
				$(".ChoiceArea").find("div.PaiChuBianYuanDiQu").removeClass("bgc_check_blue");
				$(".ChoiceArea").find("div.QuanBuXuan").removeClass("bgc_check_blue");	
			});
			$(".QuanBuXuan").click(function(){
				$(".ChoiceArea").find("div.GXK").removeClass("bgc_check_blue");	
				$(".ChoiceArea").find("div.QuanBuXuan").addClass("bgc_check_blue");	
			});
			$(".PaiChuBianYuanDiQu").click(function(){
				$(".ChoiceArea").find("div.GXK").addClass("bgc_check_blue");
				$(".ChoiceArea").find("div.XinJiang").removeClass("bgc_check_blue");
				$(".ChoiceArea").find("div.XiZang").removeClass("bgc_check_blue");
				$(".ChoiceArea").find("div.YunNan").removeClass("bgc_check_blue");
				$(".ChoiceArea").find("div.QingHai").removeClass("bgc_check_blue");	
				$(".ChoiceArea").find("div.QuanBuXuan").removeClass("bgc_check_blue");	
				$(".ChoiceArea").find("div.QuanXuan").removeClass("bgc_check_blue");
				
			});
			
			
			
			
			
			
			
			
			
			
			<!-------------    链接弹框    ----------------------->
			
			
			var  TaoBaoLianJie1=$(".TaoBaoLianJie1");
			var  TaoBaoLianJie2=$(".TaoBaoLianJie2");
			TaoBaoLianJie1.click(function(){
				 $(this).css('background','#fff').siblings(".TaoBaoLianJie1").css('background','#e3e7f0')
				var i=$(this).index();				
				 TaoBaoLianJie2.hide();
				 TaoBaoLianJie2.eq(i).show();
			});
			
			
			$(".ChangeLink").click(function(){
				//禁止滚动条
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".ChoiceLink").show();
			});
			
			$(".LinkOut").click(function(){
				//启用滚动条
				  $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".ChoiceLink").hide();	
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
			
			
			// <!------------- 短语库弹框 ----------------------->
			
			$(".ClickPhrase").click(function(){
				//禁止滚动条
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".ChoicePhrase").show();	
			});
			
			$(".PhraseOut").click(function(){
				//启用滚动条
				  $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".ChoicePhrase").hide();	
			});
			
			
			// <!------------- 手机模型输入同步 ---------------------------->
			
			$(".text_area").keyup(function(){  
//		        $(this).next().children(".text_count").text($(this).val().length);
				addLength();
				$("#textContent1").text($(this).val())
		        $(".text_area_copy").text($(this).val())
		   });
			$(".text_area").change(function(){  
				addLength();
				$("#textContent1").text($(this).val())
		        $(".text_area_copy").text($(this).val())
		   });
		   
		   <!-------------    引用短语库弹框    ----------------------->
		   	var  DYK1=$(".DYK1");
			var  DYK2=$(".DYK2");
			DYK1.click(function(){/*
				 $(this).css('background','#fff').siblings(".DYK1").css('background','#e3e7f0')*/
				 $(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
				 $(this).siblings(".DYK1").addClass("bgc_e3e7f0").removeClass("bgc_fff");
				var i=$(this).index();				
				 DYK2.hide();
				 DYK2.eq(i).show();
			});
		   
		   
		   
		   
		   	$(".Library").click(function(){
		   	//禁止滚动条 2
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
					
					
					
					
// //<!------------------- 插入付款链接------------------------------------>
		
			$(".ChaRuFuKuanLianJian").mouseover(function(){
				//禁止滚动条
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
             $(".ShouHuoTu").show();
            

         })
         $(".ChaRuFuKuanLianJian").mouseout(function(){
        	 //启用滚动条
        	  $(document.body).css({
        	  "overflow-x":"auto",
        	  "overflow-y":"auto"
        	  });
             $(".ShouHuoTu").hide();
            

      })
			
		
		    // <!------------------- 测试发送------------------------------------>
					var Phone=$(".Phone");
				   var phonez=/^1[3|4|5|7|8]\d{9}$/;
			$(".CheShiFaSong").click(function(){
				   
				   
				if (Phone.val()!='' && Phone.val().match(phonez)){
					confirm("发送成功");
				} else{
					confirm("手机号错误");
				}
			
			});
			
			
			
			// <!--------------------------- 手动订单提醒
			// ---------------------------------------->
			$(".PiLiangCuiFu").click(function(){
					$(".DingDanZhuangTai").show();
					$(".YuShouZhuangTai").hide();
					$(".DingDanZhuangTai").find("div.GXK").removeClass("bgc_check_blue");
					$(".DingDanZhuangTai").find("div.GXK").eq(1).addClass("bgc_check_blue");
			});
			$(".PiLiangCuiHaoPing").click(function(){
					$(".DingDanZhuangTai").show();
					$(".YuShouZhuangTai").hide();
					$(".DingDanZhuangTai").find("div.GXK").removeClass("bgc_check_blue");
					$(".DingDanZhuangTai").find("div.GXK").eq(5).addClass("bgc_check_blue");
			});
			$(".JiaoYiGuanBiGuanHuai").click(function(){
					$(".DingDanZhuangTai").show();
					$(".YuShouZhuangTai").hide();
					$(".DingDanZhuangTai").find("div.GXK").removeClass("bgc_check_blue");
					$(".DingDanZhuangTai").find("div.GXK").eq(7).addClass("bgc_check_blue");
			});
			$(".BiaoJiTiXing").click(function(){
					$(".DingDanZhuangTai").show();
					$(".YuShouZhuangTai").hide();
					$(".DingDanZhuangTai").find("div.GXK").removeClass("bgc_check_blue");
					$(".DingDanZhuangTai").find("div.GXK").eq(0).addClass("bgc_check_blue");
			});
			
			
			$(".YuShouCuiWeiKuan").click(function(){
				$(".DingDanZhuangTai").hide();
				$(".YuShouZhuangTai").show();	
				$(".YuShouZhuangTai").find("div.GXK").eq(2).addClass("bgc_check_blue");
			});
		
			$(".YanShiFaHuoTiXing").click(function(){
					$(".DingDanZhuangTai").show();
					$(".YuShouZhuangTai").hide();
					$(".DingDanZhuangTai").find("div.GXK").removeClass("bgc_check_blue");
					$(".DingDanZhuangTai").find("div.GXK").eq(0).addClass("bgc_check_blue");
			});
			
			$(".QiTa").click(function(){
					$(".DingDanZhuangTai").show();
					$(".YuShouZhuangTai").hide();
					$(".DingDanZhuangTai").find("div.GXK").removeClass("bgc_check_blue");
					$(".DingDanZhuangTai").find("div.GXK").eq(0).addClass("bgc_check_blue");
					$(".DingDanLeiXing").find("div.GXK").eq(0).addClass("bgc_check_blue");
					$(".PingJiaZhuangTai").find("div.GXK").eq(0).addClass("bgc_check_blue");
					$(".DiQuShaiXuan").find("div.GXK").eq(0).addClass("bgc_check_blue");
					$(".ShangPingXuanZe").find("div.GXK").eq(0).addClass("bgc_check_blue");
					$(".YiFaDuanXinGuoLv").find("div.GXK").eq(0).addClass("bgc_check_blue");
			});
					
					
					
					
					
					
					
					
					
					// 运单号start
					$(".ydh").mouseover(function(){
						$(this).find("span").show();
					});
					$(".ydh").mouseout(function(){
						$(this).find("span").hide();
					});
					// 运单号end
					
					
					
					
			// <!-------------- 卖家标记 ------------------>
			
					$(".PingBiMaiJiaBiaoJi").click(function(){
						$(".XiaoQiQi").show();
					});
					$(".YouMaiJiaBiaoJiDouBuPingBi").click(function(){
						$(".XiaoQiQi").hide();
					});
					
					
			<!-------------    卖家标记勾选框    ---------------------->
					$(".MaiJiaBiaoJiGXK").click(function(){
						$(this).toggleClass("bgc_check_blue");
					});
					
			<!-------------    VIP勾选框    ---------------------->
				/*
				 * $(".VIPGXK").click(function(){
				 * $(this).toggleClass("bgc_check_blue");
				 * $(this).parent().siblings().find(".ALLVIPGXK").removeClass("bgc_check_blue");
				 * });
				 */
					
					$(".ALLVIPGXK").click(function(){
						$(this).addClass("bgc_check_blue");
						$(this).parent().siblings().find(".VIPGXK").removeClass("bgc_check_blue");
					});	
	
					
					$(document).on("click", ".VIPGXK", function () {
						$(this).toggleClass("bgc_check_blue");
						$(this).parent().siblings().find(".ALLVIPGXK").removeClass("bgc_check_blue");
					});
		


				/*
				 * $(".hover_test").hover(function(){
				 * $(".hover_test_show").show(); },function(){
				 * $(".hover_test_show").hide(); });
				 */
				//  
				// 点击保存
//            $(".save").click(function(){
//            	var a = parseFloat( $(".limit-1").val() );
//            	var b = parseFloat( $(".limit-2").val() )
//       			$(".hour").text($(".save1").eq($(this).index()).val());
//       			$(".settime1").text($(".settime").eq($(this).index()).val());
//       			$(".settime2").text($(".settime_").eq($(this).index()).val());
//       			if (   a>b   ) {
//            		confirm("a需小于b")
//       			} else
//       			
//       			if (   a<0   ) {
//            		confirm("a需大于0")
//       			} else
//       			
//       			if (   b<0   ) {
//            		confirm("b需大于0")
//       			} else{
//       				$(".total_money").text($(".limit-1").val()+"元"+"~"+$(".limit-2").val()+"元")
//	            	$(".change_2").eq($(this).index()).hide();
//	            	$(".change_1").eq($(this).index()).show();
//       			}
//       			})


	// 宝贝关怀点击基本设置取消
             $('.cancel').click(function(){
            	$(".change_2").hide();
            	$(".change_1").show();
            })			
				
		// 点击高级设置保存
        $('.SaveG').click(function(){
        	$(".SaveG2").hide();
			$(".SaveG1").show();
        })
 // 点击高级设置取消
        $('.CanceG').click(function(){
        	$(".SaveG2").hide();
			$(".SaveG1").show();
        }) 
// 点击短信设置保存
        $('.SaveS').click(function(){
        	$(".SaveS1").show();
			$(".SaveS2").hide();
			$(".SaveS3").hide();
			$(".Letter").val($(".Letter11").val());
        })      
 // 点击短信设置取消
        $('.CancelS').click(function(){
        	$(".SaveS1").show();
			$(".SaveS2").hide();
			$(".SaveS3").hide();
        })        
            		
// 立即开启
$(".lijikaiqi").click(function(){
				
				if (   $(this).val()=="开启下单关怀"   ) {
					$(this).val("关闭下单关怀");
					$(".toggle_tishi_img").attr({"src":"${ctx}/crm/images/open_sucess.png"})
					$(".toggle_tishi").show();
					setTimeout(function(){
						$(".toggle_tishi").hide()
					},3000)
					
				} else
				{
					$(this).val("开启下单关怀");
					$(".toggle_tishi_img").attr({"src":"${ctx}/crm/images/close_sucess.png"})
					$(".toggle_tishi").show();
					setTimeout(function(){
						$(".toggle_tishi").hide()
					},3000)
				}
			});	
//     		11111111111111111111111111111111111	
     		$(".XGXGXG").click(function(){
     				$(".DuanXinSheZhi").find(".die_SZ").hide();
     				$(".DuanXinSheZhi").find(".Live_SZ").show();
     				$('.Live_SZ22').show();
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
	
				
				
				
			
				
				
				

})