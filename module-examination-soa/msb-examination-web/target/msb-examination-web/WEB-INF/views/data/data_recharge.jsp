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
              <h3>充值统计</h3>
          </div>
          <div class="main">
            <div class="data_overview">
              <div class="qingbao_tab">
                <ul>
                  <li class="active">IC卡数据</li>
                  <li>民生宝数据</li>
                </ul>
              </div>
              <div class="qingbao">
                <ul>
                  <li>
                    <div class="l">
                      <b>IC卡充值金额</b>
                      <small>IC卡充值的总金额(元)</small>
                    </div>
                    <div class="r">
                      ${data.icRechargeAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡充值总余额</b>
                      <small>IC卡充值的总余额</small>
                    </div>
                    <div class="r">
                     ${data.icRechargeBalance }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡已消费充值余额</b>
                      <small>已使用的总余额</small>
                    </div>
                    <div class="r">
                      ${data.icConsumedRechargeAmount}
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡赠送总额</b>
                      <small>IC卡充值总赠送余额(元)</small>
                    </div>
                    <div class="r">
                      ${data.icGiveAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡总赠送余额</b>
                      <small>IC卡用户未使用的赠送余额</small>
                    </div>
                    <div class="r">
                      ${data.icGiveBalance }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡已消费赠送余额</b>
                      <small>已使用的赠送余额</small>
                    </div>
                    <div class="r">
                     ${data.icConsumedGiveAmount}
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡充值笔数</b>
                      <small>IC卡用户充值的总次数(笔)</small>
                    </div>
                    <div class="r">
                     ${data.icRechargeOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>IC卡充值用户数</b>
                      <small>IC充值的总用户数</small>
                    </div>
                    <div class="r">
                      ${data.icRechargeUserCount }
                    </div>
                  </li>
                </ul>
                <ul class="hide">
                  <li>
                    <div class="l">
                      <b>民生宝充值金额</b>
                      <small>民生宝充值的总金额(元)</small>
                    </div>
                    <div class="r">
                      ${data.msbRechargeAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝充值总余额</b>
                      <small>民生宝充值的总余额</small>
                    </div>
                    <div class="r">
                     ${data.msbRechargeBalance }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝已消费充值余额</b>
                      <small>民生宝已使用的总余额</small>
                    </div>
                    <div class="r">
                     ${data.msbConsumedRechargeAmount}
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝赠送总额</b>
                      <small>民生宝充值总赠送余额(元)</small>
                    </div>
                    <div class="r">
                      ${data.msbGiveAmount }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝总赠送余额</b>
                      <small>民生宝用户未使用的赠送余额</small>
                    </div>
                    <div class="r">
                     ${data.msbGiveBalance }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝已消费赠送余额</b>
                      <small>民生宝已使用的赠送余额</small>
                    </div>
                    <div class="r">
                     ${data.msbConsumedGiveAmount}
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝充值笔数</b>
                      <small>民生宝用户充值的总次数(笔)</small>
                    </div>
                    <div class="r">
                     ${data.msbRechargeOrder }
                    </div>
                  </li>
                  <li>
                    <div class="l">
                      <b>民生宝充值用户数</b>
                      <small>民生宝充值的总用户数</small>
                    </div>
                    <div class="r">
                     ${data.msbRechargeUserCount }
                    </div>
                  </li>
                </ul>
              </div>
              <div class="echart_tab">
                <ul>
                  <li class="active" data-type="amount">充值金额</li>
                  <li data-type="account">充值笔数</li>
                </ul>
              </div>
              <div id="amount_box" class="echart_box">
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
                 <div id="echart_amount" class="echart"></div>
              </div>
              <div id="account_box" class="echart_box hide">
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
                 <div id="echart_account" class="echart"></div>
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
        setMenu(6,48);
        $(".qingbao_tab li").click(function(){
          if($(this).hasClass('active')){
            return;
          }else{
            $(this).addClass('active').siblings().removeClass('active');
            var i = $(this).index();
            $('.qingbao ul').hide().eq(i).show();
          }
        });

        //充值金额
        new pickerDateRange('date_demo3', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo3',
          theme : 'ta',
          success : function(obj) {
            $('#amount_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_amountChart(3,start,end);
            }
          }
        });

        //充值笔数
        new pickerDateRange('date_demo4', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo4',
          theme : 'ta',
          success : function(obj) {
            $('#account_box .time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_accountChart(3,start,end);
            }
          }
        });

        //默认调用最近一周数据 充值金额
        set_amountChart(3,null,null);
        $('#amount_box .time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
                 set_amountChart(3,null,null);
                 break;
              case 'month':
                 set_amountChart(2,null,null);
                 break;
              case 'year':
                 set_amountChart(1,null,null);
              default:
                break;
          }
        });

        //默认调用最近一周数据 充值笔数
        set_accountChart(3,null,null);
        $('#account_box .time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
                 set_accountChart(3,null,null);
                 break;
              case 'month':
                 set_accountChart(2,null,null);
                 break;
              case 'year':
                 set_accountChart(1,null,null);
              default:
                break;
          }
        });


        $('#main_content').resize(function(){
          // echart_amount.resize();
          // echart_account.resize();
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
            if(type=="account"){
              echart_account.resize();
            }else if(type=="amount"){
              echart_amount.resize();
            }
          }

        });

      });
      //充值金额图表
      var echart_amount = echarts.init(document.getElementById('echart_amount'));
      function set_amountChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/rechargeTrendStat2',
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
            echart_amount.resize();
            echart_amount.setOption(option);
          }
        });
      }

      //充值笔数图表
      var echart_account = echarts.init(document.getElementById('echart_account'));
      function set_accountChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/rechargeOrderTrendStat',
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
            echart_account.resize();
            echart_account.setOption(option);
          }
        });
      }
  
    </script>
  </body>
</html>