function doChart(categoriesData,seriesData){

	 $('#container').highcharts({
	        chart: {
	            type: 'line',
	            width:$('#hTable').width()
	        },
	        title: {
	            text: '交易成功金额'
	        },
	        subtitle: {
	            text: '数据来源: 客云CRM'
	        },
	        xAxis: {
	            categories: categoriesData
	        },
	        yAxis: {
	            title: {
	                text: '金额 (元)'
	            }
	        },
	        plotOptions: {
	            line: {
	                dataLabels: {
	                    enabled: true          // 开启数据标签
	                },
	                enableMouseTracking: false // 关闭鼠标跟踪，对应的提示框、点击事件会失效
	            }
	        },
	        series: [{
	            name: '成交金额',
	            data: seriesData
	        }]
	    });
}
