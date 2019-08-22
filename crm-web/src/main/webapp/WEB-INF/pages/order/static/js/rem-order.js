(function (doc, win) {
var docEl = doc.documentElement,
  resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize',
  recalc = function () {
    var clientWidth = docEl.clientWidth-18;
    
    if (!clientWidth) return;
    if(clientWidth<=1400){
    	clientWidth=1400;
    }

    docEl.style.fontSize = 100 * (clientWidth / 1920) + 'px';
    //640是设计图的宽度,100是一个基准宽度(html的font-size值)
  };

if (!doc.addEventListener) return;
win.addEventListener(resizeEvt, recalc, false);
doc.addEventListener('DOMContentLoaded', recalc, false);
})(document, window);