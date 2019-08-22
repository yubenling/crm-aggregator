$(function(){
	$('.xuanzeshangpinSearchRightDiv i').click(function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$(this).siblings('input').val('0');
		}else{
			$(this).addClass('on');
			$(this).siblings('input').val('1');
		}
	});
	
	//左侧单选基本操作
	$('.tableLeftBody').on('click','.dx',function(){
		var thisParent=$(this).parent();
		var str='<tr class="trtwo">'
				+'<td class="tdone"><i data-id="'+$(this).attr('data-id')+'" class="dx"></i></td>'
				+'<td class="tdtwo">'+thisParent.siblings('td').eq(0).html()+'</td>'
				+'<td class="tdtdree">'+thisParent.siblings('td').eq(1).html()+'</td>'
				+'<td class="tdfour">'+thisParent.siblings('td').eq(2).html()+'</td>'
				+'<td class="tdfive">'+thisParent.siblings('td').eq(3).html()+'</td>'
				+'</tr>';
		var thisId=$(this).attr('data-id');
		var rightTableDxLength=$('.rightTableBody .dx').length;
		var rightTableDx=$('.rightTableBody .dx');
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$('.tableLeftdead').find('.qx').removeClass('on');
			for(var i=0;i<rightTableDxLength;i++){
				if(thisId==rightTableDx.eq(i).attr('data-id')){
					rightTableDx.eq(i).parents('tr').remove();
				}
			}
			$('.yxDiv span').text($('.rightTableBody .dx').length);
		}else{
			if(parseInt($('.rightTableBody .dx').length+1)>20){
				$(".tishi_2").show();
				$(".tishi_2").children("p").text("最多可添加20个商品!")
				setTimeout(function() {
					$(".tishi_2").hide()
					$(".text_test_window").hide();
				}, 3000)
				return;
			}
			$(this).addClass('on');
			if(regDxNum('.tableLeftBody')){
				$('.tableLeftdead').find('.qx').addClass('on');
			}
			
			$('.rightTableBody table').append(str);
			$('.yxDiv span').text($('.rightTableBody .dx').length);
		}
		
	});
	//左侧全选基本操作
	$('.tableLeftdead').on('click','.qx',function(){
		var thisDx=$('.tableLeftBody .dx');
		var rightDx=$('.rightTableBody .dx');
		var arrLeftId=[];
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$('.tableLeftBody').find('.dx').removeClass('on');
			for(var i=0;i<thisDx.length;i++){
				arrLeftId.push(thisDx.eq(i).attr('data-id'));
			}
			for(var i=0;i<arrLeftId.length;i++){
				for(var j=0;j<rightDx.length;j++){
					if(arrLeftId[i]==rightDx.eq(j).attr('data-id')){
						rightDx.eq(j).parents('tr').remove();
					}
				}
			}
			$('.yxDiv span').text($('.rightTableBody .dx').length);
		}else{
			if(parseInt($('.rightTableBody .dx').length+$('.tableLeftBody .dx').length)>20){
				$(".tishi_2").show();
				$(".tishi_2").children("p").text("最多可添加20个商品!")
				setTimeout(function() {
					$(".tishi_2").hide()
					$(".text_test_window").hide();
				}, 3000)
				return;
			}
			$(this).addClass('on');
			
			for(var i=0;i<$('.tableLeftBody .dx').length;i++){
				if(!$('.tableLeftBody .dx').eq(i).hasClass('on')){
					var str='<tr class="trtwo">'
						+'<td class="tdone"><i data-id="'+thisDx.eq(i).attr('data-id')+'" class="dx"></i></td>'
						+'<td class="tdtwo">'+thisDx.eq(i).parent('td').siblings('td').eq(0).html()+'</td>'
						+'<td class="tdtdree">'+thisDx.eq(i).parent('td').siblings('td').eq(1).html()+'</td>'
						+'<td class="tdfour">'+thisDx.eq(i).parent('td').siblings('td').eq(2).html()+'</td>'
						+'<td class="tdfive">'+thisDx.eq(i).parent('td').siblings('td').eq(3).html()+'</td>'
						+'</tr>';
					$('.rightTableBody table').append(str);
				}
				
			}
			$('.tableLeftBody').find('.dx').addClass('on');
			$('.yxDiv span').text($('.rightTableBody .dx').length);
			
		}
		
	});
	//删除选中商品效果
	$('#removesp').on('click',function(){
		var rightarrId=[];
		var rightDxon=$('.rightTableBody .dx.on');
		var leftDx=$('.tableLeftBody .dx');
		for(var i=0;i<rightDxon.length;i++){
			rightarrId.push(rightDxon.eq(i).attr('data-id'));
			rightDxon.eq(i).parents('tr').remove();
		}
		for(var i=0;i<rightarrId.length;i++){
			for(var j=0;j<leftDx.length;j++){
				if(rightarrId[i]==leftDx.eq(j).attr('data-id')){
					leftDx.eq(j).removeClass('on');
				}
			}
		}
		if(!regDxNum('.leftTable')){
			$('.tableLeftdead').find('.qx').removeClass('on');
		}
		if(!regDxNum('.rightTableBody')){
			$('.rightTableHead').find('.qx').removeClass('on');
		}
		$('.yxDiv span').text($('.rightTableBody .dx').length);
	});
	//右侧单选效果
	$('.rightTableBody').on('click','.dx',function(){
		if($(this).hasClass('on')){
			$(this).removeClass('on');
			$('.rightTableHead').find('.qx').removeClass('on');
		}else{
			$(this).addClass('on');
			if(regDxNum('.rightTableBody')){
				$('.rightTableHead').find('.qx').addClass('on');
			}
		}
	});
	//右侧全选效果
	$('.rightTableHead .qx').on('click',function(){
		if($(this).hasClass('on')){
			$('.rightTableBody .dx').removeClass('on');
			$(this).removeClass('on');
		}else{
			$('.rightTableBody .dx').addClass('on');
			$(this).addClass('on');
		}
	});
	//点击搜索按钮
	$('#xuanzeshangpinSearch').on('click',function(){
		if($(this).hasClass('on'))return;
		$(this).addClass('on');
		$('#SPID').val($('#numId').val());
		$('#GJZ').val($('#gjztitle').val());
		$('#SHOWSJ').val($('#shelves').val());
		$('#SHOWDSJ').val($('#waitgrounding').val());
		$('#GLSP').val($('#relation').val());
		var numIid=$('#SPID').val();
		var onsaleStatus=0;
		var importStatus=0;
		var instockStatus=0;
		var title=$('#GJZ').val();
		if($('#SHOWSJ').val()==1){
			onsaleStatus=1;
		}else{
			onsaleStatus=0;
		}
		if($('#SHOWDSJ').val()==1){
			instockStatus=1;
		}else{
			instockStatus=0;
		}
		if($('#GLSP').val()==1){
			importStatus=1;
		}else{
			importStatus=0;
		}
		
		$('.leftTable').html('');
		var aTr=$('<tr class="trtwo active"><td colspan="5"><img src="'+$('#urlVal').val()+'/crm/images/yu-jiazai.gif"></td></tr>');
		$('.leftTable').append(aTr);
		var that=$(this);
		$.ajax({
			url:$('#urlVal').val()+'/item/findItemData',
			type:'post',
			data:{
				pageNo:1,
				numIid:numIid,
				onsaleStatus:onsaleStatus,
				importStatus:importStatus,
				instockStatus:instockStatus,
				title:title
			},
			success:function(data){
				
				that.removeClass('on')
				console.log(data);
				showData(data);
			}
		})
	});
	//点击自定义按钮效果
	$('#appoint_ItemId').on('click',function(){
		if($(this).hasClass('disabled'))return;
		$('#xuanzeshangpinBox').show();
		$('.markBg').show();
		
		$('.leftTable').html('');
		$('#xuanzeshangpinpage').html('');
		$('.tableLeftdead .qx').removeClass('on');
		var aTr=$('<tr class="trtwo active"><td colspan="5"><img src="'+$('#urlVal').val()+'/crm/images/yu-jiazai.gif"></td></tr>');
		$('.leftTable').append(aTr);
		$('.rightTableBody table').html('');
		$('.yxDiv span').text('0');
		$.ajax({
			url:$('#urlVal').val()+'/item/findItemData',
			type:'post',
			data:{
				itemId:$('#itemIds').val()
			},
			success:function(data){
				
				console.log(data);
				showData(data);
			}
		})
	});
	
	//点击取消效果
	$('#xuanzeshangpinBox .btn .qx').on('click',function(){
		$('#xuanzeshangpinBox').hide();
		$('.markBg').hide();
		$(document.body).css({
			'overflow-y':'auto'
		});
		$('#numId').val('');
		$('#gjztitle').val('');
		$('#shelves').val('');
		$('#waitgrounding').val('');
		$('#relation').val('');
		$('#SPID').val('');
		$('#GJZ').val('');
		$('#SHOWSJ').val('');
		$('#SHOWDSJ').val('');
		$('#GLSP').val('');
		$('.xuanzeshangpinSearchRightDiv i').removeClass('on');
		if($('#memberSelectionBox').length){
			
			$('#memberSelectionBox').show();
			$('.markBg').show();
			$(document.body).css({
				'overflow-y':'hidden'
			});
		}
		
	});
	
	//点击确定效果
	$('#xuanzeshangpinBox .btn .qd').on('click',function(){
		$('#numId').val('');
		$('#gjztitle').val('');
		$('#shelves').val('');
		$('#waitgrounding').val('');
		$('#relation').val('');
		$('#SPID').val('');
		$('#GJZ').val('');
		$('#SHOWSJ').val('');
		$('#SHOWDSJ').val('');
		$('#GLSP').val('');
		$('.xuanzeshangpinSearchRightDiv i').removeClass('on');
		var rightDx=$('.rightTableBody .dx');
		var aItemId=[];
		for(var i=0;i<rightDx.length;i++){
			aItemId.push($('.rightTableBody .dx').eq(i).attr('data-id'));
		}
		$('#itemIds').val(aItemId.join(','));
		$('#xuanzeshangpinBox').hide();
		$('.markBg').hide();
		if(aItemId.length){
			$('.member-alerta').text('已选择'+aItemId.length+'件商品');
		}else{
			$('.member-alerta').text('自定义');
		}
		$(document.body).css({
			'overflow-y':'hidden'
		});
		$('#memberSelectionBox').show();
		$('.markBg').show();
	});
	
	
	//订单短信群发updata
	//点击自定义按钮效果
	$('#xzspBtn').on('click',function(){
		if($(this).hasClass('disabled'))return;
		$('#xuanzeshangpinBox').show();
		$('.markBg').show();
		$(document.body).css({
			'overflow-y':'hidden'
		});
		$('.leftTable').html('');
		$('#xuanzeshangpinpage').html('');
		$('.tableLeftdead .qx').removeClass('on');
		var aTr=$('<tr class="trtwo active"><td colspan="5"><img src="'+$('#urlVal').val()+'/crm/images/yu-jiazai.gif"></td></tr>');
		$('.leftTable').append(aTr);
		$('.rightTableBody table').html('');
		$('.yxDiv span').text('0');
		$.ajax({
			url:$('#urlVal').val()+'/item/findItemData',
			type:'post',
			data:{
				itemId:$('#numIid').val()
			},
			success:function(data){
				
				console.log(data);
				showData(data);
			}
		})
	});
	
	//点击确定效果
	$('#xuanzeshangpinBox.ordersmsqf .btn .qd').on('click',function(){
		$('#numId').val('');
		$('#gjztitle').val('');
		$('#shelves').val('');
		$('#waitgrounding').val('');
		$('#relation').val('');
		$('#SPID').val('');
		$('#GJZ').val('');
		$('#SHOWSJ').val('');
		$('#SHOWDSJ').val('');
		$('#GLSP').val('');
		$('.xuanzeshangpinSearchRightDiv i').removeClass('on');
		var rightDx=$('.rightTableBody .dx');
		var aItemId=[];
		for(var i=0;i<rightDx.length;i++){
			aItemId.push($('.rightTableBody .dx').eq(i).attr('data-id'));
		}
		$('#numIid').val(aItemId.join(','));
		$('#xuanzeshangpinBox').hide();
		$('.markBg').hide();
		if(aItemId.length){
			$('#xzspBtn').text('已选择'+aItemId.length+'件商品');
			$('#xzspImg img').hide();
			$('#xzspImg input').val('');
		}else{
			$('#xzspBtn').text('自定义');
			$('#xzspImg img').show();
			$('#xzspImg input').val('1');
		}
		$(document.body).css({
			'overflow-y':'auto'
		});
	});
	
	$('#xzspImg').on('click',function(){
		if($(this).children('input').val()){
			$(this).children('input').val('1');
		}else{
			$(this).children('input').val('');
		}
		$('#numIid').val('');
		$(this).siblings('.serchBtn').text('自定义');
	});
	$('#ssdqImg').on('click',function(){
		if($(this).children('input').val()){
			$(this).children('input').val('1');
			
		}else{
			$(this).children('input').val('');
		}
		$('#stateId').val('');
		$(this).siblings('.serchBtn').text('自定义');
	});
	$('#ssdqBtn').on('click',function(){
		$('.ChoiceArea').show();
		if($('#stateId').val()){
			var locality = $("#stateId").val(); 

			var localityList = locality.split(",");
			var getprovince = $(".place_check").children().find('.li_').children().next().children('input');
			for(var i=0;i<localityList.length;i++){
				for(var j=0;j<getprovince.length;j++){
					if(getprovince.eq(j).val()==localityList[i]){
						getprovince.eq(j).parent().prev().addClass("bgc_check_blue");
					}
				}
			}
		}else{
			$('.1check_box_2').removeClass('bgc_check_blue');
		}
	});
});
//展示数据
function showData(data){
	if(data.datas){
		$('.rightTableBody table').html('');
		$('.yxDiv span').text(data.datas.length);
		for(var i=0;i<data.datas.length;i++){
			var str='<tr class="trtwo">'
				+'<td class="tdone"><i data-id="'+data.datas[i].itemId+'" class="dx"></i></td>'
				+'<td class="tdtwo">'+data.datas[i].itemId+'</td>'
				+'<td class="tdtdree"><img src="'+data.datas[i].url+'"></td>'
				+'<td class="tdfour">'+data.datas[i].title+'</td>'
				+'<td class="tdfive">'+data.datas[i].price+'</td>'
				+'</tr>';
			$('.rightTableBody table').append(str);
		}
	}
	$('.leftTable').find('.active').remove();
	for(var i=0;i<data.pageData.list.length;i++){
		var str='<tr class="trtwo">'
			+'<td class="tdone"><i data-id="'+data.pageData.list[i].itemId+'" class="dx"></i></td>'
			+'<td class="tdtwo">'+data.pageData.list[i].itemId+'</td>'
			+'<td class="tdtdree"><img src="'+data.pageData.list[i].url+'"></td>'
			+'<td class="tdfour">'+data.pageData.list[i].title+'</td>'
			+'<td class="tdfive">'+data.pageData.list[i].price+'</td>'
			+'</tr>';
		$('.leftTable').append(str);
	}
	var rightIdArr=[];
	for(var i=0;i<$('.rightTableBody .dx').length;i++){
		rightIdArr.push($('.rightTableBody .dx').eq(i).attr('data-id'));
	}
	for(var j=0;j<rightIdArr.length;j++){
		for(var i=0;i<$('.leftTable .dx').length;i++){
			if(rightIdArr[j]==$('.leftTable .dx').eq(i).attr('data-id')){
				$('.leftTable .dx').eq(i).addClass('on');
			}
		}
	}
	if(regDxNum('.leftTable')){
		$('.tableLeftdead').find('.qx').addClass('on');
	}else{
		$('.tableLeftdead').find('.qx').removeClass('on');
	}
	if(data.pageData.totalPage>1){
		laypage({
		  	cont: $('#xuanzeshangpinpage'),  //容器。值支持id名、原生dom对象，jquery对象,
		  	pages: data.pageData.totalPage, //总页数
		  	skip: true, //是否开启跳页
		  	curr:1,
		  	skin: '#2d8cf0',
		  	groups: 3, //连续显示分页数
		  	jump: function(obj, first){
		  		if(!first){
		  			$('.leftTable').html('');
		  			var aTr=$('<tr class="trtwo active"><td colspan="5"><img src="'+$('#urlVal').val()+'/crm/images/yu-jiazai.gif"></td></tr>');
		  			$('.leftTable').append(aTr);
		  			var numIid=$('#SPID').val();
		  			var onsaleStatus=0;
		  			var importStatus=0;
		  			var instockStatus=0;
		  			var title=$('#GJZ').val();
		  			if($('#SHOWSJ').val()==1){
		  				onsaleStatus=1;
		  			}else{
		  				onsaleStatus=0;
		  			}
		  			if($('#SHOWDSJ').val()==1){
		  				instockStatus=1;
		  			}else{
		  				instockStatus=0;
		  			}
		  			if($('#GLSP').val()==1){
		  				importStatus=1;
		  			}else{
		  				importStatus=0;
		  			}
		  			$.ajax({
		  				url:$('#urlVal').val()+'/item/findItemData',
		  				type:'post',
		  				data:{
		  					pageNo:obj.curr,
		  					numIid:numIid,
		  					onsaleStatus:onsaleStatus,
		  					importStatus:importStatus,
		  					instockStatus:instockStatus,
		  					title:title
		  				},
		  				success:function(data){
		  					
		  					$('.leftTable').find('.active').remove();
		  					$('.tableLeftdead .qx').removeClass('on');
		  					
		  					for(var i=0;i<data.pageData.list.length;i++){
		  						var str='<tr class="trtwo">'
		  							+'<td class="tdone"><i data-id="'+data.pageData.list[i].itemId+'" class="dx"></i></td>'
		  							+'<td class="tdtwo">'+data.pageData.list[i].itemId+'</td>'
		  							+'<td class="tdtdree"><img src="'+data.pageData.list[i].url+'"></td>'
		  							+'<td class="tdfour">'+data.pageData.list[i].title+'</td>'
		  							+'<td class="tdfive">'+data.pageData.list[i].price+'</td>'
		  							+'</tr>';
		  						$('.leftTable').append(str);
		  						
		  					}
		  					var rightIdArr=[];
		  					for(var i=0;i<$('.rightTableBody .dx').length;i++){
		  						rightIdArr.push($('.rightTableBody .dx').eq(i).attr('data-id'));
		  					}
		  					for(var j=0;j<rightIdArr.length;j++){
	  							for(var i=0;i<$('.leftTable .dx').length;i++){
	  								if(rightIdArr[j]==$('.leftTable .dx').eq(i).attr('data-id')){
	  									$('.leftTable .dx').eq(i).addClass('on');
	  								}
	  							}
	  						}
		  					if(regDxNum('.leftTable')){
		  						$('.tableLeftdead').find('.qx').addClass('on');
		  					}else{
		  						$('.tableLeftdead').find('.qx').removeClass('on');
		  					}
		  				}
		  			})
		  		}
		  	}
		});
	}else{
		$('#xuanzeshangpinpage').html('');
	}
}
//单选判断选中个数激活全选
function regDxNum(sClass){
	var num=$(sClass).find('.dx.on').length;
	var allNum=$(sClass).find('.dx').length
	var bOk;
	if(allNum==0){
		bOk=false;
	}else if(num==allNum){
		bOk=true;
	}else{
		bOk=false;
	}
	return bOk;
}
