function valiteTwoTime(){
	var startTime = $(tser01).val();
	var endTime = $(tser02).val();
//	console.log(startTime);
//	console.log(endTime);
//	console.log("转化时间 " + new Date(startTime));
	if(startTime!=undefined && endTime!=undefined && startTime!="" && endTime!=""){
		var startTimeDate = new Date(startTime);
		var endTimeDate = new Date(endTime);
		if(startTimeDate > endTimeDate){
			$(tser02).val("");
				$(".tishi_2").show();
				$(".tishi_2").children("p").text("开始时间不能大于结束时间！")
				setTimeout(function(){ 
					$(".tishi_2").hide()
				},3000)
			return false;
		}else if(startTimeDate == endTimeDate){
			$(tser02).val("");
			$(".tishi_2").show();
			$(".tishi_2").children("p").text("开始时间不能等于结束时间！")
			setTimeout(function(){ 
				$(".tishi_2").hide()
			},3000)
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}
$(function(){
	$("#PAGENO").on("keyup",function(){
//		var orderIdValue = $("#PAGENO").val();
//		var number = /^\d+$/;
//		if(number.test(orderIdValue)){
//		}else{
//			$("#PAGENO").val("");
//			confirm("页码必须为数字");
//		}
		var val = this.value;
		if(val !='' && val ==0){
			this.value = 1;
		}
		
		this.value=this.value.replace(/\D/g,'');
	});
	
});
