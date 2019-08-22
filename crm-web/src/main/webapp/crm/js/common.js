$(function(){
    var timer=null;
    $('.userTxBox').on('mouseover',function(e){
        clearInterval(timer);
        $('.toggleAlertBox').addClass('on');
        e.stopPropagation();
    });
    $('.userTxBox').on('mouseout',function(e){
        var target=$(e.target);
        clearInterval(timer);
        timer=setTimeout(function(){
            $('.toggleAlertBox').removeClass('on');
        },300);
        
        
        e.stopPropagation();
    });
    $('.selsectBox *').on('mouseover',function(){
        clearInterval(timer);
        
    });
    
});