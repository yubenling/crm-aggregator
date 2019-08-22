// JavaScript Document


$(function(){
				$(".ChongZhi1").click(function(){
					var i=$(this).index();				
					 	$(".ChongZhi2").hide();
					
					 	$(".ChongZhi2").eq(i).show();
				});
				
			})
			


/* $(function(){
            $("#change_inside div:eq(0)").show();
            $("#change_outside div").each(function(index){
                $(this).click(function(){
                        $("#page_content div").hide();
                     $("#page_content div:eq("+index+")").show();
             })              
            })
    });*/











































