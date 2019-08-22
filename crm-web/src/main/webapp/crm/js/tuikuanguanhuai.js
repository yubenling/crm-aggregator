
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
							$('#startTime').val('');
							$('#endTime').val('');
							$(timeMSG).html("<font style='color: red;'>结束时间不能小于开始时间</font>");
						}else if(endTimeFloat==startTimeFloat){+
							$('#startTime').val('');
							$('#endTime').val('');
							$(timeMSG).html("<font style='color: red;'>结束时间不能等于开始时间</font>");
						}
					}
				}
// console.log("正在修改 endTime ="+$(endTime).val());
// console.log("正在修改 startTime = "+$(startTime).val());
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
// $(".green_check").click(function(){
// if ( $(this).attr("src")=="images/green_check.png" ) {
// $(this).attr({"src":"images/green_uncheck.png"})
// } else{
// $(this).attr({"src":"images/green_check.png"})
// }
// });
//			
// /*点击顶部设置按钮显示右边框*/
// $(".right_top_set_btn").click(function(){
// $(".right_top_set").toggle()
// });
//			
// /*顶部时间显示*/
// setInterval(function() {
// var now = (new Date()).toLocaleString();
// $('.time').text(now);
// }, 1000);
			
			
// 点击修改
            $(".change").click(function(){
            	$(".change_1").hide();
            	$(".change_2").show();
            });
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
		
		$(".C_XG").click(function(){
					$(this).parent().parent().siblings(".die_SZ").hide();
					$(this).parent().parent().siblings(".Live_SZ").show();
					
		});
		$(".test_number").blur(function(){
			if (   $(this).val()!=""   ) {
				$(".test").bind("click",function(){
					confirm("这是给后台看的")
				})
			} else{
				$(".test").unbind()
			}
		});
		
		// 点击保存
// $(".save").click(function(){
// var a = parseFloat( $(".Money").val() );
// var b = parseFloat( $(".Money2").val() );
// $(".Time").text($(".Time11").val());
// $(".Time2").text($(".Time22").val());
// $(".Money").text($(".Money11").val()+"~");
// $(".Money2").text($(".Money22").val());
// $(".Time3").text($(".Time33").val());
// $(".Letter").val($(".Letter11").val());
// if ( a>b ) {
// confirm("a需小于b")
// } else
//     			
// if ( a<0 ) {
// confirm("a需大于0")
// } else
//     			
// if ( b<0 ) {
// confirm("b需大于0")
// } else{
// $(".change_2").hide();
// $(".change_1").show();
// }
//     			
            // 1
            $('.tui1 .save').click(function(){
            	var a = parseFloat( $(".tui1 .Money").val() );
	          	var b = parseFloat( $(".tui1 .Money2").val() )
            	$(".tui1 .Time").text($(".tui1 .Time11").val());
    			$(".tui1 .Time2").text($(".tui1 .Time22").val());
    			$(".tui1 .Money").text($(".tui1 .Money11").val()+'元'+"~");
				$(".tui1 .Money2").text($(".tui1 .Money22").val()+'元');
				if (   a>b   ) {
          		confirm("a需小于b")
    			} else
       			
       			if (   a<0   ) {
            		confirm("a需大于0")
       			} else
       			
       			if (   b<0   ) {
            		confirm("b需大于0")
       			} else{
	            	$(".change_2").hide();
	            	$(".change_1").show();
       			}
           });
			
			
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
			
			
			// <!------------- 勾选框 ----------------------->
			$(".GXK").click(function(){
				$(this).addClass("bgc_check_blue")
				$(this).parent().siblings().children(".GXK").removeClass("bgc_check_blue")	
			})
			
			
			
			// <!------------- 商品弹框 ----------------------->
			
			$(".AddSpecified").click(function(){
				$(".ChoiceSpecified").show();
			});
			$(".XuanZePaiCheSpecified").click(function(){
				$(".ChoiceSpecified").show();
			});
			
			$(".SpecifiedOut").click(function(){
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
			
			
			
			
			
			// <!------------- 地区弹框 ----------------------->
			
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
			
			
			
			
			
			
			
			
			
			
			// <!------------- 链接弹框 ----------------------->
			
			
			var  TaoBaoLianJie1=$(".TaoBaoLianJie1");
			var  TaoBaoLianJie2=$(".TaoBaoLianJie2");
			TaoBaoLianJie1.click(function(){
				 $(this).css('background','#fff').siblings(".TaoBaoLianJie1").css('background','#e3e7f0')
				var i=$(this).index();				
				 TaoBaoLianJie2.hide();
				 TaoBaoLianJie2.eq(i).show();
			});
			
			
			$(".ChangeLink").click(function(){
				$(".ChoiceLink").show();
			});
			
			$(".LinkOut").click(function(){
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
				$(".ChoicePhrase").show();	
			});
			
			$(".PhraseOut").click(function(){
				$(".ChoicePhrase").hide();	
			});
			
			
			// <!------------- 手机模型输入同步 ---------------------------->
			
			/*$(".text_area").keyup(function(){  
		        $(this).next().children(".text_count").text($(this).val().length + 4);
		        $(".text_area_copy").text($(this).val())
		   });*/
		   
		   
		   // <!------------- 引用短语库弹框 ----------------------->
		   	var  DYK1=$(".DYK1");
			var  DYK2=$(".DYK2");
			DYK1.click(function(){
				 /* $(this).css('background','#fff').siblings(".DYK1").css('background','#e3e7f0') */
				$(this).addClass("bgc_fff").removeClass("bgc_e3e7f0");
				 $(this).siblings(".DYK1").addClass("bgc_e3e7f0").removeClass("bgc_fff");
				var i=$(this).index();				
				 DYK2.hide();
				 DYK2.eq(i).show();
			});
		   
		   
		   
		   
			$(".Library").click(function(){
				 // 禁止滚动条 15
				  $(document.body).css({
				    "overflow-x":"hidden",
				    "overflow-y":"hidden"
				  });
				$(".PhraseLibrary").show();
			});
					
			$(".LibraryOut").click(function(){
				// 启用滚动条
				  $(document.body).css({
				  "overflow-x":"auto",
				  "overflow-y":"auto"
				  });
				$(".PhraseLibrary").hide();
			});	
					
					
					
					
// //<!------------------- 插入付款链接------------------------------------>
		
			$(".ChaRuFuKuanLianJian").mouseover(function(){
             $(".ShouHuoTu").show();
            

         })
         $(".ChaRuFuKuanLianJian").mouseout(function(){
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
        $('.tui1 .SaveS').click(function(){
        	$(".SaveS1").show();
			$(".SaveS2").hide();
			$(".SaveS3").hide();
			$(".tui1 .Letter").val($(".tui1 .Letter11").val());
        }) 
 // 点击短信设置取消
        $('.CancelS').click(function(){
        	$(".SaveS1").show();
			$(".SaveS2").hide();
			$(".SaveS3").hide();
        })        
            		
				
// 买家申请退款 立即开启
$(".lijikaiqi").click(function(){
				
				if (   $(this).val()=="开启买家申请退款"   ) {
					$(this).val("关闭买家申请退款");
					$(".toggle_tishi_img").attr({"src":"images/open_sucess.png"})
					$(".toggle_tishi").show();
					setTimeout(function(){
						$(".toggle_tishi").hide()
					},3000)
					
				} else
				{      
					$(this).val("开启买家申请退款");
					$(".toggle_tishi_img").attr({"src":"images/close_sucess.png"})
					$(".toggle_tishi").show();
					setTimeout(function(){
						$(".toggle_tishi").hide()
					},3000)
				}
			});					

				$(".GaoJiSheZhiSave").click(function(){
			$(this).parent().parent(".Live_SZ").hide();
			$(this).parent().parent().siblings(".die_SZ").show();
			
			var lll=$(".DiQuShaiXuan").find(".bgc_check_blue").next("div").text();
			$(".DiQu").text(lll);
			var kkk=$(".DingDanLaiYuan").find(".bgc_check_blue").next("div").text();
			$(".LaiYuan").text(kkk);
			var jjj=$(".HuiYuanDengJi").find(".bgc_check_blue").next("div").text();
			$(".VipDengJi").text(jjj);
			var hhh=$(".ShangPinXuanZhe").find(".bgc_check_blue").next("div").text();
			$(".AllShangPin").text(hhh);
			
			
		})		
		// 11111111111111111111111111111111111
		$(".XGXGXG").click(function(){
				$(".DuanXinSheZhi").find(".die_SZ").hide();
				$(".DuanXinSheZhi").find(".Live_SZ").show();
				$('.Live_SZ22').show();
			});			
				
				
		$('.one_check_only').click(function(){
			if($(this).children().hasClass('display_none')){
				$(this).children().removeClass('display_none')
			}
			else{
				$(this).children().addClass('display_none')
			}
		})
	<!-------------    地区弹框    ----------------------->
			
			$(".1check_box_2").click(function(){
				$(this).children(".1check_box_1").addClass("bgc_check_blue");
				$(this).parent().siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
			});
			
			$(".1check_box_3").click(function(){
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			});
			
			
			$(".all_check").click(function(){
				$(this).nextAll().children(".1check_box_1").removeClass("bgc_check_blue");
				if (   !$(this).children(".1check_box_1").hasClass("bgc_check_blue")   ) {
						$(this).children(".1check_box_1").addClass("bgc_check_blue");
						$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
						$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".1check_box_1").removeClass("bgc_check_blue");
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").removeClass("bgc_check_blue");
					/*
					 * $(this).children(".1check_box_1").addClass("bgc_check_blue");
					 * $(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
					 * 
					 * $(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
					 */
				}
				/*
				 * $(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				 * 
				 * $(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
				 */
			});
			
			
			$(".all_notcheck").click(function(){
				
				$(this).siblings().children(".1check_box_1").removeClass("bgc_check_blue");
				
				$(this).children(".1check_box_1").addClass("bgc_check_blue");
				
				$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				
				$(this).parent().next(".place_check").children(".gangaotai_ul").children(".gangaotai").nextAll(".li_").children(".1check_box_1").removeClass("bgc_check_blue");
				
			})
			
			$(".far_notcheck").click(function(){
				
				$(this).prev().children(".1check_box_1").removeClass("bgc_check_blue");
				
				$(".all_check").children(".1check_box_1").addClass("bgc_check_blue");
				
				
				$(".place_check").find(".1check_box_1").addClass("bgc_check_blue");
				
				if (   $(this).children(".1check_box_1").hasClass("bgc_check_blue")   ) {
					$(this).children(".1check_box_1").removeClass("bgc_check_blue");
				$(this).parent().next(".place_check").children("ul").eq(5).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").addClass("bgc_check_blue");
				
				$(this).parent().next(".place_check").children("ul").eq(6).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".1check_box_1").addClass("bgc_check_blue");
				$(this).parent().next(".place_check").children("ul").eq(5).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").removeClass("bgc_check_blue");
				
				$(this).parent().next(".place_check").children("ul").eq(6).children("li:eq(0),li:eq(2),li:eq(3)").children(".1check_box_1").removeClass("bgc_check_blue");
				}
				
				
				
			});
			
			
			
			
			
		
			
			
			
			$(".place_check").click(function(){
				if (   $(".bgc_check_blue").length>43   ) {
					$(".all_check").children(".1check_box_1").addClass("bgc_check_blue");
					$(".all_check").siblings().children(".1check_box_1").removeClass("bgc_check_blue");
				} else
				
				if (   $(".bgc_check_blue").length<1   ) {
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_notcheck").children(".1check_box_1").addClass("bgc_check_blue");
				} else
				
				if (   $(".bgc_check_blue").length>1   ) {
					$(".all_notcheck").children(".1check_box_1").removeClass("bgc_check_blue");
				} else{
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
		
				
			});
			
			$(".gangaotai").click(function(){
			
				if (   $(this).children(".1check_box_1").hasClass("bgc_check_blue")   ) {
					$(this).children(".1check_box_1").removeClass("bgc_check_blue");
					$(this).nextAll(".li_").children(".1check_box_1").removeClass("bgc_check_blue");
				} else{
					$(this).children(".1check_box_1").addClass("bgc_check_blue");
					$(this).nextAll(".li_").children(".1check_box_1").addClass("bgc_check_blue");
				}
				
			});
			
			$(".gangaotai_ul").children(".li_").click(function(){
				$(this).prevAll(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
			});
			
			$(".gangaotai_ul").eq(0).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>4   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(1).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>5   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(2).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>2   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(3).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>3   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(4).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>2   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(5).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>4   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(6).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>4   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(7).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>2   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			$(".gangaotai_ul").eq(8).click(function(){
				if (   $(this).children(".li_").children(".bgc_check_blue").length>0   ) {
					$(this).children(".gangaotai").children(".1check_box_1").addClass("bgc_check_blue");
				} else{
					$(this).children(".gangaotai").children(".1check_box_1").removeClass("bgc_check_blue");
					$(".all_check").children(".1check_box_1").removeClass("bgc_check_blue");
				};
			});
			
			
			
			
			
			
				
			
			
			/* table背景色 */
			$("tr:odd").css("background-color","#FAFAFA");
			$("tr:even").css("background-color","#F4F4F4");
			/* table高度，行高 */
			$("tr").css("height","2.6vw");
			
		
			
			
		
	
			
			$(".check_part:eq(0),.check_part:eq(1),.check_part:eq(2),.check_part:eq(3)").find("li").click(function(){
				
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
				$(this).siblings().children(".1check_box").children(".1check_box_1").removeClass("bgc_check_blue");
			});
			
			$(".check_part:eq(4),.check_part:eq(5)").find("li:eq(0)").click(function(){
				
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
				$(this).siblings().children(".1check_box").children(".1check_box_1").removeClass("bgc_check_blue");
			});
			$(".check_part:eq(4)").find("li:eq(1),li:eq(2),li:eq(3),li:eq(4),li:eq(5)").click(function(){
				
				$(".check_part:eq(4)").find("li").eq(0).children().children(".1check_box_1").removeClass("bgc_check_blue");
				$(this).children(".1check_box_1").toggleClass("bgc_check_blue");
				
			});
			
			
			$(".check_part").eq(5).find("li").eq(0).click(function(){
				
				
				$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
				$(this).siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
				
				
			});
			
			$(".check_part").eq(5).find("li").eq(1).click(function(){
				$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
				$(".check_part").eq(5).find("li").eq(0).children().children(".1check_box_1").removeClass("bgc_check_blue");
				$(".check_part").eq(5).find("li").eq(3).children().children(".1check_box_1").removeClass("bgc_check_blue");
			});
			
			$(".check_part").eq(5).find("li").eq(2).click(function(){
				$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
				$(".check_part").eq(5).find("li").eq(0).children().children(".1check_box_1").removeClass("bgc_check_blue");
				$(".check_part").eq(5).find("li").eq(3).children().children(".1check_box_1").removeClass("bgc_check_blue");
			});
			
			$(".check_part").eq(5).find("li").eq(3).click(function(){
				
				
				$(this).children().children(".1check_box_1").addClass("bgc_check_blue");
				$(this).siblings().children().children(".1check_box_1").removeClass("bgc_check_blue");
				
				
			});
			/* 创建分组 */
			$(".add_group_btn").click(function(){
				$(".add_group").show();
				$(".group_check_").hide();
				$(".explain").val("");
				$(".group_name").val("");
			});
			
			$(".close").click(function(){
				$(".add_group").hide();
			});
			
			$(".close_1").click(function(){
				$(".area_check_window").hide();
			});
			
			
			
			
			$(".update_btn").click(function(){
				$(".update").show();
				$(".group_check_").hide();
				$(".explain").val("");
				$(".group_name").val("");
			});
			
			$(".close").click(function(){
				$(".update").hide();
			});
			
			
			
			
			
			
			$(".zdy").click(function(){
				$(this).next().show();
			});
			$(".zdy").prevAll().click(function(){
				$(this).nextAll(".zdy").next().hide();
			});
			
			$(".zdy_1").click(function(){
				$(this).prev().children().children(".1check_box_1").addClass("bgc_check_blue");
			});
			
			
			$(".zdy_").click(function(){
				$(".area_check_window").show();
			})
			
		
			$(".CustomArea").click(function(){
				$(".area_check_window").show();
			});

				
				
// 退款成功 立即开启
			$(".lijikaiqi_1").click(function(){
							
							if (   $(this).val()=="开启退款成功"   ) {
								$(this).val("关闭退款成功");
								$(".toggle_tishi_img").attr({"src":"images/open_sucess.png"})
								$(".toggle_tishi").show();
								setTimeout(function(){
									$(".toggle_tishi").hide()
								},3000)
								
							} else
							{
								$(this).val("开启退款成功");
								$(".toggle_tishi_img").attr({"src":"images/close_sucess.png"})
								$(".toggle_tishi").show();
								setTimeout(function(){
									$(".toggle_tishi").hide()
								},3000)
							}
						});	
			
			
// 等待退货 立即开启
			$(".lijikaiqi_2").click(function(){
							
							if (   $(this).val()=="开启同意退款，等待买家退货"   ) {
								$(this).val("关闭同意退款，等待买家退货");
								$(".toggle_tishi_img").attr({"src":"images/open_sucess.png"})
								$(".toggle_tishi").show();
								setTimeout(function(){
									$(".toggle_tishi").hide()
								},3000)
								
							} else
							{
								$(this).val("开启同意退款，等待买家退货");
								$(".toggle_tishi_img").attr({"src":"images/close_sucess.png"})
								$(".toggle_tishi").show();
								setTimeout(function(){
									$(".toggle_tishi").hide()
								},3000)
							}
						});	
			
			
		// 拒绝退货 立即开启
			$(".lijikaiqi_4").click(function(){
							
							if ($(this).val()=="开启拒绝退款，等待买家修改"   ) {
								$(this).val("关闭拒绝退款，等待买家修改");
								$(".toggle_tishi_img").attr({"src":"images/open_sucess.png"})
								$(".toggle_tishi").show();
								setTimeout(function(){
									$(".toggle_tishi").hide()
								},3000)
								
							} else
							{
								$(this).val("开启拒绝退款，等待买家修改");
								$(".toggle_tishi_img").attr({"src":"images/close_sucess.png"})
								$(".toggle_tishi").show();
								setTimeout(function(){
									$(".toggle_tishi").hide()
								},3000)
							}
						});
				
				
			/* 下拉框 */
			/*
			 * $('[name="nice-select"]').click(function(e){
			 * $('[name="nice-select"]').find('ul').hide();
			 * $(this).find('ul').show(); e.stopPropagation(); });
			 * $('[name="nice-select"] li').hover(function(e){
			 * $(this).toggleClass('on'); e.stopPropagation(); });
			 * $('[name="nice-select"] li').click(function(e){ var val =
			 * $(this).text(); var dataVal = $(this).attr("data-value");
			 * $(this).parents('[name="nice-select"]').find('input').val(val);
			 * $(timeMSG).empty(); var endTime = $("#endTime").val(); var
			 * startTime = $("#startTime").val(); if(startTime==undefined ||
			 * endTime == undefined){ }else{ if(endTime!="" && startTime!=""){
			 * var endTimeFloat = parseFloat(endTime.substring(0,2)); var
			 * startTimeFloat = parseFloat(startTime.substring(0,2));
			 * if(endTimeFloat<startTimeFloat){ $("#endTime").val("");
			 * $(timeMSG).html("<font style='color: red;'>结束时间不能小于开始时间</font>");
			 * }else if(endTimeFloat==startTimeFloat){+ $("#endTime").val("");
			 * $(timeMSG).html("<font style='color: red;'>结束时间不能等于开始时间</font>"); } } } //
			 * console.log("正在修改 endTime ="+$(endTime).val()); //
			 * console.log("正在修改 startTime = "+$(startTime).val());
			 * $('[name="nice-select"] ul').hide(); e.stopPropagation(); });
			 * $(document).click(function(){ $('[name="nice-select"]
			 * ul').hide(); });
			 */
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