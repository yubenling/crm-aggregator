;(function($){
    'use strict';
    $.fn.kpage = function(config, callback){
        var _this = this;
        _this.addClass('kpage');
        config = $.extend({
            "curPage":"1",
            "pageTotalNum":"1"
        },config);
        show(config, callback);
        function show(config, callback){
            _this.html('');
            if(config.curPage - 3 > 1){
                var $blk = $('<span class="kpage-block">1</span>');
                $blk.attr('onselectstart','return false');
                $blk.click(function(){
                    callcbk(1, config, callback);
                });
                _this.append($blk);
                if(config.curPage - 3 > 2){
                    var $omiss = $('<span class="kpage-omission">...</span>');
                    $omiss.attr('onselectstart','return false');
                    _this.append($omiss);
                }
            }
            for(var i = 1; i <= config.pageTotalNum; i++){
                if(canDrawBlack(i, config.pageTotalNum, config.curPage)){
                    var black = getPageBlack(i);
                    black.attr('onselectstart','return false');
                    if(i == config.curPage){
                        black.addClass('active');
                    }
                    _this.append(black);
                }
            }
            if(config.curPage - 0 + 3 < config.pageTotalNum){
                var $blk = $('<span class="kpage-block">'+config.pageTotalNum+'</span>');
                $blk.attr('onselectstart','return false');
                $blk.click(function(){
                    callcbk(config.pageTotalNum, config, callback);
                });
                if(config.curPage - 0 + 3 < config.pageTotalNum - 1){
                    var $omiss = $('<span class="kpage-omission">...</span>');
                    $omiss.attr('onselectstart','return false');
                    _this.append($omiss);
                }
                _this.append($blk);
            }
            _this.append('<input class="kpage-input" value="'+config.curPage+'">');
            var $go = $('<span class="kpage-block">确定</span>');
            $go.attr('onselectstart','return false');
            $go.click(function(){
                var $ipt = $(this).siblings().filter('input.kpage-input');
                var val = $ipt.val();
                //----添加校验@authorjackstraw_yu
                val=val.replace(/\D/g,'');
                //----添加校验@authorjackstraw_yu
                if(!val || val < 1){
                    val = 1;
                    $ipt.val(val);
                }
                if(val - 0 > config.pageTotalNum){
                    val = config.pageTotalNum;
                    $ipt.val(val);
                }
                callcbk(val, config, callback);
            });
            _this.append($go);

            function canDrawBlack(i, totalNum, curPage){
                if(i >= 1 && i <= totalNum){
                    if(i >= curPage - 3 && i <= curPage - 0 + 3){
                        return true;
                    } else {
                        return false;
                    }
                    return 1;
                } else {
                    return false;
                }
            }
            function getPageBlack(i){
                return $('<span class="kpage-block">'+i+'</span>').click(function(){
                    if(!$(this).hasClass('active')){
                        callcbk(i, config, callback);
                    }
                });
            }
        }
        function callcbk(i, config, callback){
        	_this.find('span').addClass('active');
            $.extend(config, {
                "curPage":i
            });
            //show(config, callback);
            callback(i);
        }
        return {"setPageTotalNum":function(obj){
            config = $.extend(config, obj, {"curPage":"1"});
            show(config, callback);
        }};
    };
})(jQuery);