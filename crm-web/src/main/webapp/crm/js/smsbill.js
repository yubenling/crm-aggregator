
$(function(){
    var start={
        trigger:"", 
		isTime:true,
        format:'YYYY-MM-DD',
        choosefun: function(elem, val, date){

            end.minDate = val; //开始日选好后，重置结束日的最小日期
        }
    };	
    var end={
        trigger:"", 
		isTime:true,
        format:'YYYY-MM-DD', //最大日期
        choosefun: function(elem, val, date){

            start.maxDate = val; //将结束日的初始值设定为开始日的最大日期
        }	
    }
    /*下拉框*/
    $('[name="nice-select"]').click(function(e){
        if($(this).hasClass('disabled'))return;
        $('[name="nice-select"]').find('ul').hide();
        $(this).find('ul').show();
        e.stopPropagation();
    });
    $('[name="nice-select"] li').hover(function(e){
        
        $(this).toggleClass('on');
        e.stopPropagation();
    });
    $('[name="nice-select"] li').click(function(e){
        var thisTxt=$(e.target).text();
        var dataVal = $(this).attr("data-value");
        $(this).parents('[name="nice-select"]').find('p').text(thisTxt);
        $(this).parents('[name="nice-select"]').find('input').val(dataVal);
        $('#dataType').val(dataVal);
        if(dataVal=='day'){
            start={
                trigger:"", 
                isTime:true,
                format:'YYYY-MM-DD',
                choosefun: function(elem, val, date){
        
                    end.minDate = val; //开始日选好后，重置结束日的最小日期
                }
            };	
            end={
                trigger:"", 
                isTime:true,
                format:'YYYY-MM-DD', //最大日期
                choosefun: function(elem, val, date){
        
                    start.maxDate = val; //将结束日的初始值设定为开始日的最大日期
                }	
            }
        }else if(dataVal=='month'){
            start={
                trigger:"", 
                isTime:true,
                format:'YYYY-MM',
                choosefun: function(elem, val, date){
        
                    end.minDate = val; //开始日选好后，重置结束日的最小日期
                }
            };	
            end={
                trigger:"", 
                isTime:true,
                format:'YYYY-MM', //最大日期
                choosefun: function(elem, val, date){
        
                    start.maxDate = val; //将结束日的初始值设定为开始日的最大日期
                }	
            }
        }else if(dataVal=='year'){
            start={
                trigger:"", 
                isTime:true,
                format:'YYYY',
                choosefun: function(elem, val, date){
        
                    end.minDate = val; //开始日选好后，重置结束日的最小日期
                }
            };	
            end={
                trigger:"", 
                isTime:true,
                format:'YYYY', //最大日期
                choosefun: function(elem, val, date){
        
                    start.maxDate = val; //将结束日的初始值设定为开始日的最大日期
                }	
            }
        }
        $('[name="nice-select"] ul').hide();
        e.stopPropagation();
    });
    $(document).click(function(){
        $('[name="nice-select"] ul').hide();
    });
    laypage({
        cont: $('#smsbillTablePage'), //容器。值支持id名、原生dom对象，jquery对象,
        pages: 10, //总页数
        skip: true, //是否开启跳页
        curr:1,
        skin: '#2d8cf0',
        groups: 3, //连续显示分页数
        jump: function(obj, first){ //触发分页后的回调
        }
    });
    
    $('#tser01').on('click',function(){
        $('#tser01').jeDate(start);
    });
    $('#tser02').on('click',function(){
        $('#tser02').jeDate(end);
    }); 
    var URL=$('#src').val();
    var dataType=$('#dataType').val();   
    smsbillUpData(dataType,1);
});
function smsbillUpData(dataType,pageNo){
	if(dataType==''||dataType==undefined){
		$.ajax({
			url:URL+'/shopData/reportList',
			success:function(data){
				console.log(data);
			}
        })
    }else{
        if($('#tser01').val()!=''){
            $.ajax({
                type:'post',
                url:url+'/shopData/reportList',
                data:{
                    dateType:dateType,
                    pageNo:pageNo,
                    bTimeStr:$('#tser01').val(),
                    eTimeStr:$('#tser02').val()
                },
                success:function(data){
                    console.log(data);
                }
            })
        }
        
    }
}