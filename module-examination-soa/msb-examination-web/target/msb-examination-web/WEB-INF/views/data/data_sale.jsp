<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<%@include file="../common/taglib.jsp"%>
   <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>民生水宝后台管理系统</title>
    <link href="${ctx }css/font-awesome.min.css" rel="stylesheet">
    <link href="${ctx }plugins/layer/skin/layer.css" rel="stylesheet">
    <link href="${ctx }css/select2.min.css" rel="stylesheet">
    <link href="${ctx }plugins/daterange/dateRange.css" rel="stylesheet">
    <link href="${ctx }css/datepicker3.css" rel="stylesheet">
    <link href="${ctx }css/slick.css" rel="stylesheet">
    <link href="${ctx }plugins/fancybox/jquery.fancybox.css" rel="stylesheet">
    <link href="${ctx }css/style.css" rel="stylesheet">
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
	<jsp:include page="../common/header.jsp"></jsp:include>
     <div class="wrap">
      <jsp:include page="../common/menu.jsp"></jsp:include>
       <div id="main_content">
          <div class="main_title">
              <h3>销售订单一览</h3>
          </div>
          <div class="main">
            <div class="data_overview">
              <div class="qingbao">
                <ul>
                  <li>
                    <div class="l">
                      <b>总销售金额</b>
                      <small>有效订单的总金额(元)</small>
                    </div>
                    <div class="r">
                     ${data.saleAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡销售额</b>
                      <small>IC卡订单的销售额</small>
                    </div>
                    <div class="r">
                     ${data.icSaleAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝销售额</b>
                      <small>民生宝订单的销售额</small>
                    </div>
                    <div class="r">
                      ${data.msbSaleAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>配送销售额</b>
                      <small>配送订单的销售额</small>
                    </div>
                    <div class="r">
                      ${data.deliverySaleAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>下单量</b>
                      <small>有效订单的总量(笔)</small>
                    </div>
                    <div class="r">
                      ${data.saleOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡订单数</b>
                      <small>IC卡用户下单量</small>
                    </div>
                    <div class="r">
                      ${data.icSaleOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝订单数</b>
                      <small>民生宝用户下单量</small>
                    </div>
                    <div class="r">
                      ${data.msbSaleOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>配送订单数</b>
                      <small>配送订单下单量</small>
                    </div>
                    <div class="r">
                      ${data.deliverySaleOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>总销售水量</b>
                      <small>有效订单的水流量(L)</small>
                    </div>
                    <div class="r">
                      ${data.saleWaterQuantity }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡订单水量</b>
                      <small>IC卡订单销售水流量</small>
                    </div>
                    <div class="r">
                      ${data.icSaleWaterQuantity }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝订单水量</b>
                      <small>民生宝订单销售水流量</small>
                    </div>
                    <div class="r">
                      ${data.msbSaleWaterQuantity }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>配送订单水量</b>
                      <small>配送订单销售水流量</small>
                    </div>
                    <div class="r">
                      ${data.deliverySaleWaterQuantity }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>配套水桶销量</b>
                      <small>累计线上销售的水桶数量</small>
                    </div>
                    <div class="r">
                      ${data.bucketCount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>配套水桶销售额</b>
                      <small>累计线上销售的水桶销售额</small>
                    </div>
                    <div class="r">
                      ${data.bucketFee }
                    </div>
                  </li>
                </ul>
              </div>
              <div class="echart_tab">
                <ul>
                  <li class="active" data-type="salee">销售额</li>
                  <li data-type="order">订单量</li>
                  <li data-type="water">销售水量</li>
                </ul>
              </div>
              <div id="salee_box" class="echart_box">
                 <div class="time_tools">
                   <span class="text">时间：</span>
                   <div class="time_items">
                     <a href="javascript:;" data-type="week" class="active">本周</a>
                     <a href="javascript:;" data-type="month">最近1月</a>
                     <a href="javascript:;" data-type="year">最近一年</a>
                   </div>
                   <div class="ta_date" id="div_date_demo3">
                    <span class="date_title" id="date_demo3">点击选择时间段</span>
                    <a class="opt_sel" id="input_trigger_demo3" href="#">
                        <i class="i_orderd"></i>
                    </a>
                   </div>
                 </div>
                 <div id="echart_salee" class="echart"></div>
              </div>
              <div id="order_box" class="echart_box hide">
                 <div class="time_tools">
                   <span class="text">时间：</span>
                   <div class="time_items">
                     <a href="javascript:;" data-type="week" class="active">本周</a>
                     <a href="javascript:;" data-type="month">最近1月</a>
                     <a href="javascript:;" data-type="year">最近一年</a>
                   </div>
                   <div class="ta_date" id="div_date_demo4">
                    <span class="date_title" id="date_demo4">点击选择时间段</span>
                    <a class="opt_sel" id="input_trigger_demo4" href="#">
                        <i class="i_orderd"></i>
                    </a>
                   </div>
                 </div>
                 <div id="echart_order" class="echart"></div>
              </div>
              <div id="water_box" class="echart_box hide">
                 <div class="time_tools">
                   <span class="text">时间：</span>
                   <div class="time_items">
                     <a href="javascript:;" data-type="week" class="active">本周</a>
                     <a href="javascript:;" data-type="month">最近1月</a>
                     <a href="javascript:;" data-type="year">最近一年</a>
                   </div>
                   <div class="ta_date" id="div_date_demo5">
                    <span class="date_title" id="date_demo5">点击选择时间段</span>
                    <a class="opt_sel" id="input_trigger_demo5" href="#">
                        <i class="i_orderd"></i>
                    </a>
                   </div>
                 </div>
                 <div id="echart_water" class="echart"></div>
              </div>
            </div>
          </div>
       </div>
     </div>
     
    <script src="${ctx }js/jquery.min.js"></script>
    <script src="${ctx }plugins/layer/layer.js"></script>
    <script src="${ctx }js/select2.min.js"></script>
    <script src="${ctx }plugins/daterange/dateRange.js"></script>
    <script src="${ctx }js/echarts.min.js"></script>
    <script src="${ctx }js/underscore-min.js"></script>
    <script src="${ctx }js/bootstrap-datepicker.js"></script>
    <script src="${ctx }js/bootstrap-datepicker.zh-CN.js"></script>
    <script src="${ctx }js/slick.min.js"></script>
    <script src="${ctx }plugins/fancybox/jquery.fancybox.js"></script>
    <script src="${ctx }js/doT.min.js"></script>
    <script src="${ctx }js/script.js"></script>
    <script>
      $(function(){
        setMenu(6,47);

        //销售额
        var dateRange = new pickerDateRange('date_demo3', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo3',
          theme : 'ta',
          success : function(obj) {
            $('#salee_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_saleChart(5,start,end);
            }
          }
        });

        //订单量
        var dateRange = new pickerDateRange('date_demo4', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo4',
          theme : 'ta',
          success : function(obj) {
            $('#order_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_orderChart(5,start,end);
            }
          }
        });


        //销售水量
        var dateRange = new pickerDateRange('date_demo5', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo5',
          theme : 'ta',
          success : function(obj) {
            $('#water_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_waterChart(5,start,end);
            }
          }
        });
        //默认调用最近一周数据 销售额
        set_saleChart(3,null,null);
        $('#salee_box .time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
                 set_saleChart(3,null,null);
                 break;
              case 'month':
                 set_saleChart(2,null,null);
                 break;
              case 'year':
                 set_saleChart(1,null,null);
              default:
                break;
          }
        });

        //默认调用最近一周数据 订单量
        set_orderChart(3,null,null);
        $('#order_box .time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
                 set_orderChart(3,null,null);
                 break;
              case 'month':
                 set_orderChart(2,null,null);
                 break;
              case 'year':
                 set_orderChart(1,null,null);
              default:
                break;
          }
        });


        //默认调用最近一周数据 水量
        set_waterChart(3,null,null);
        $('#water_box .time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
                 set_waterChart(3,null,null);
                 break;
              case 'month':
                 set_waterChart(2,null,null);
                 break;
              case 'year':
                 set_waterChart(1,null,null);
              default:
                break;
          }
        });

        $('#main_content').resize(function(){
          echart_sale.resize();
          echart_order.resize();
          echart_water.resize();
        });

        //图表切换
        $(".echart_tab li").click(function(){
          if($(this).hasClass('active')){
            return;
          }else{
            $(this).addClass('active').siblings().removeClass('active');
            var i = $(this).index();
            $('.echart_box').hide().eq(i).show();
            var type = $(this).data('type');
            if(type=="salee"){
              echart_sale.resize();
            }else if(type=="order"){
              echart_order.resize();
            }else if(type=="water"){
              echart_water.resize();
            }
          }

        });

      });
      //销售额图表
      var echart_sale = echarts.init(document.getElementById('echart_salee'));
      function set_saleChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/saleTrendStat2',
            data:{type:type,beginDate:beginDate,endDate:endDate},
          success: function(result){
        	  var res = result.data;
          	 var msb_values = null;
          	 var ic_values = null;
         	  if (res.rows != null){
         		 msb_values = res.rows[0].values;
         		 ic_values = res.rows[1].values;
      	      }
             option = {
              title: {
                  text: res.title,
                  textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#777'
                  }   
              },
              legend: {
                  data:['民生宝','IC卡'],
                  y: "bottom"
              },
              grid: {
                  left: '2%',
                  right: '2%',
                  bottom: '10%',
                  containLabel: true
              },
              tooltip : {
                  trigger: 'axis'
              },
              xAxis: {
                  type : 'category',
                  boundaryGap : false,
                  data: res.series
              },
              yAxis : [
                  {
                      type : 'value'
                  }
              ],
              series: [{
                  color: ['#ff6600'],
                  name:'民生宝',
                  type:'line',    
                  data: _.flatten(msb_values)
              },{
                  color: ['#0c97fb'],
                  name:'IC卡',
                  type:'line',    
                  data: _.flatten(ic_values)
              }
              ]
            };
            echart_sale.resize();
            echart_sale.setOption(option);
          }
        });
      }

      //订单量图表
      var echart_order = echarts.init(document.getElementById('echart_order'));
      function set_orderChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/orderTrendStat',
            data:{type:type,beginDate:beginDate,endDate:endDate},
          success: function(result){
        	  var res = result.data;
           	 var msb_values = null;
          	 var ic_values = null;
         	  if (res.rows != null){
         		 msb_values = res.rows[0].values;
         		 ic_values = res.rows[1].values;
      	      }
             option = {
              title: {
                  text: res.title,
                  textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#777'
                  }   
              },
              legend: {
                  data:['民生宝','IC卡'],
                  y: "bottom"
              },
              grid: {
                  left: '2%',
                  right: '2%',
                  bottom: '10%',
                  containLabel: true
              },
              tooltip : {
                  trigger: 'axis'
              },
              xAxis: {
                  type : 'category',
                  boundaryGap : false,
                  data: res.series
              },
              yAxis : [
                  {
                      type : 'value'
                  }
              ],
              series: [{
                  color: ['#ff6600'],
                  name:'民生宝',
                  type:'line',    
                  data: _.flatten(msb_values)
              },{
                  color: ['#0c97fb'],
                  name:'IC卡',
                  type:'line',    
                  data: _.flatten(ic_values)
              }
              ]
            };
            echart_order.resize();
            echart_order.setOption(option);
          }
        });
      }

      //销售水量图表
      var echart_water = echarts.init(document.getElementById('echart_water'));
      function set_waterChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/waterTrendStat',
            data:{type:type,beginDate:beginDate,endDate:endDate},
          success: function(result){
        	  var res = result.data;
            	 var msb_values = null;
              	 var ic_values = null;
             	  if (res.rows != null){
             		 msb_values = res.rows[0].values;
             		 ic_values = res.rows[1].values;
          	      }
             option = {
              title: {
                  text: res.title,
                  textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#777'
                  }   
              },
              legend: {
                  data:['民生宝','IC卡'],
                  y: "bottom"
              },
              grid: {
                  left: '2%',
                  right: '2%',
                  bottom: '10%',
                  containLabel: true
              },
              tooltip : {
                  trigger: 'axis'
              },
              xAxis: {
                  type : 'category',
                  boundaryGap : false,
                  data: res.series
              },
              yAxis : [
                  {
                      type : 'value'
                  }
              ],
              series: [{
                  color: ['#ff6600'],
                  name:'民生宝',
                  type:'line',    
                  data: _.flatten(msb_values)
              },{
                  color: ['#0c97fb'],
                  name:'IC卡',
                  type:'line',    
                  data: _.flatten(ic_values)
              }
              ]
            };
            echart_water.resize();
            echart_water.setOption(option);
          }
        });
      }
    </script>
  </body>
</html>