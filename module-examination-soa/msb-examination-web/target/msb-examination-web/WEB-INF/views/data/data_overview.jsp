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
              <h3>统计概览</h3>
          </div>
          <div class="main">
            <div class="data_overview">
              <div class="qingbao">
                <h3><span>${currentDate}</span> 最新情报</h3>
                <ul>
                  <li>
                    <div class="l">
                      <b>销售金额</b>
                      <small>有效订单的总金额(元)</small>
                    </div>
                    <div class="r">
                      ${data.saleAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>下单量</b>
                      <small>有效订单的总数量</small>
                    </div>
                    <div class="r">
                      ${data.saleOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>用户总数</b>
                      <small>所有激活用户的数量</small>
                    </div>
                    <div class="r">
                      ${data.totalMember }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>设备总数</b>
                      <small>投放运营的设备总数</small>
                    </div>
                    <div class="r">
                       ${data.totalEquip }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>总售水量</b>
                      <small>有效订单的水流量(L)</small>
                    </div>
                    <div class="r">
                      ${data.saleWaterQuantity }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>平均购水量</b>
                      <small>平均每单的购买量(L)</small>
                    </div>
                    <div class="r">
                        ${data.avgSaleWaterQuantity }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>平均客单价</b>
                      <small>平均每单的金额</small>
                    </div>
                    <div class="r">
                      ${data.avgUnitPrice }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>设备维护次数</b>
                      <small>投放运营的设备维护总次数</small>
                    </div>
                    <div class="r">
                       ${data.totalEquipServicing }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>充值收入总额</b>
                      <small>全部用户的充值总额</small>
                    </div>
                    <div class="r">
                       ${data.rechargeAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>充值总笔数</b>
                      <small>全部用户的充值总笔数</small>
                    </div>
                    <div class="r">
                      ${data.rechargeOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>用户充值总余额</b>
                      <small>用户的全部余额</small>
                    </div>
                    <div class="r">
                      ${data.totalBalance}
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>营销赠送金额</b>
                      <small>赠送用户的全部金额</small>
                    </div>
                    <div class="r">
                      ${data.giveAmount }
                    </div>
                  </li>
                </ul>
              </div>
              <div id="sale_box" class="echart_box">
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
                 <div id="echart_sale" class="echart"></div>
              </div>
              <div id="deposit_box" class="echart_box">
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
                 <div id="echart_deposit" class="echart"></div>
              </div>
              <div class="sxph">
                <div class="l">
                  <div class="sx_box">
                    <h3>累计销售额排行Top10</h3>
                    <table class="table1">
                      <tr>
                        <th>序号</th>
                        <th>设备号</th>
                        <th>销售额</th>
                        <th>小区名称</th>
                        <th>投放地址</th>
                      </tr>
                      <c:forEach var="saleRankData" items="${ saleRankStatList}" varStatus="status">
                      <tr>
                        <td>${status.count }</td>
                        <td>${saleRankData.equipmentNo }</td>
                        <td>${saleRankData.saleAmount }</td>
                        <td>${saleRankData.communityName }</td>
                        <td>${saleRankData.address }</td>
                      </tr>
                      </c:forEach>
                    </table>
                  </div>
                </div>
                <div class="r">
                  <div class="sx_box">
                    <h3>累计充值排行Top10</h3>
                    <table class="table1">
                      <tr>
                        <th>序号</th>
                        <th>设备号</th>
                        <th>充值金额</th>
                        <th>小区名称</th>
                        <th>投放地址</th>
                      </tr>
                      <c:forEach var="rechargeRankData" items="${ rechargeRankStatList}" varStatus="status">
                      <tr>
                        <td>${status.count }</td>
                        <td>${rechargeRankData.equipmentNo }</td>
                        <td>${rechargeRankData.rechargeAmount }</td>
                        <td>${rechargeRankData.communityName }</td>
                        <td>${rechargeRankData.address }</td>
                      </tr>
                      </c:forEach>
                    </table>
                  </div>
                </div>
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
        setMenu(6,46);
        var dateRange = new pickerDateRange('date_demo3', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo3',
          theme : 'ta',
          success : function(obj) {
            $('#sale_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_orderChart(5,start,end);
            }
          }
        });
        //默认调用最近一周数据
        set_orderChart(3,null,null);
        $('#sale_box .time_items a').click(function(){
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


        var dateRange = new pickerDateRange('date_demo4', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo4',
          theme : 'ta',
          success : function(obj) {
            $('#deposit_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_depositChart(5,start,end);
            }
          }
        });

        //默认调用最近一周数据
        set_depositChart(3,null,null);
        $('#deposit_box .time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
                 set_depositChart(3,null,null);
                 break;
              case 'month':
                 set_depositChart(2,null,null);
                 break;
              case 'year':
                 set_depositChart(1,null,null); 
              default:
                break;
          }
        });
        $('#main_content').resize(function(){
          echart_sale.resize();
          echart_deposit.resize();
        });
      });
    
      //销售趋势图表
      var echart_sale = echarts.init(document.getElementById('echart_sale'));
      function set_orderChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/saleTrendStat',
            data:{type:type,beginDate:beginDate,endDate:endDate},
          success: function(result){
        	  var res = result.data;
         	 var values = null;
          	  if (res.rows != null){
       		  values = res.rows[0].values;
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
              grid: {
                  left: '2%',
                  right: '2%',
                  bottom: '3%',
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
                  color: ['#0c97fb'],
                  name:'销售额(元)',
                  type:'line',    
                  stack: '总量',
                  data: _.flatten(values)
              }]
            };
            echart_sale.resize();
            echart_sale.setOption(option);
          }
        });
      }

      //预存款统计图表
      var echart_deposit = echarts.init(document.getElementById('echart_deposit'));
      function set_depositChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/rechargeTrendStat',
            data:{type:type,beginDate:beginDate,endDate:endDate},
          success: function(result){
        	  var res = result.data;
          	 var values = null;
         	  if (res.rows != null){
      		  values = res.rows[0].values;
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
              grid: {
                  left: '2%',
                  right: '2%',
                  bottom: '3%',
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
                  color: ['#0c97fb'],
                  name:'预存款(元)',
                  type:'line',    
                  stack: '总量',
                  data: _.flatten(values)
              }]
            };
            echart_deposit.resize();
            echart_deposit.setOption(option);
          }
        });
      }
    </script>
  </body>
</html>