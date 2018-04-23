<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<%@include file="common/taglib.jsp"%>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>民生水宝后台管理系统</title>
    <link href="${ctx}css/font-awesome.min.css" rel="stylesheet">
    <link href="${ctx}plugins/layer/skin/layer.css" rel="stylesheet">
    <link href="${ctx}css/select2.min.css" rel="stylesheet">
    <link href="${ctx}plugins/daterange/dateRange.css" rel="stylesheet">
    <link href="${ctx}css/datepicker3.css" rel="stylesheet">
    <link href="${ctx}css/slick.css" rel="stylesheet">
    <link href="${ctx}plugins/fancybox/jquery.fancybox.css" rel="stylesheet">
    <link href="${ctx}css/style.css" rel="stylesheet">
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
	<jsp:include page="common/header.jsp"></jsp:include>
     <div class="wrap">
      <jsp:include page="common/menu.jsp"></jsp:include>
       <div id="main_content" class="bg1">
          <div class="main">
             <div id="index_box">
               <div class="box2">
                 <div class="content">
                   <div class="top">
                     <div class="tit">销售数据概览</div>
                     <ul>
                       <li>
                         <span>今日订单量</span>
                         <b>
                         <c:choose>
                           <c:when test="${dayStat.saleOrder == null}">
                           0
                           </c:when>
                           <c:otherwise>
                            ${dayStat.saleOrder }
                           </c:otherwise>
                         </c:choose>
                        
                         </b>
                       </li>
                       <li>
                         <span>今日销售额</span>
                         <b>
                         <c:choose>
                           <c:when test="${dayStat.saleAmount == null}">
                           0.0
                           </c:when>
                           <c:otherwise>
                            ${dayStat.saleAmount }
                           </c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                       <li>
                         <span>本月订单量</span>
                         <b>
                       <c:choose>
                           <c:when test="${monthStat.saleOrder == null}">
                           0
                           </c:when>
                           <c:otherwise>
                            ${monthStat.saleOrder }
                           </c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                       <li>
                         <span>本月销售额</span>
                         <b>
                          <c:choose>
                           <c:when test="${monthStat.saleAmount == null}">
                           0.0
                           </c:when>
                           <c:otherwise>
                            ${monthStat.saleAmount }
                           </c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                     </ul>
                   </div>
                   <div class="bottom">
                     <div class="time_tools">
                       <span class="text">时间：</span>
                       <div class="time_items">
                         <a href="javascript:;" data-type="week">本周</a>
                         <a href="javascript:;" data-type="month" class="active">最近1月</a>
                         <a href="javascript:;" data-type="year">最近一年</a>
                       </div>
                       <div class="ta_date" id="div_date_demo3">
                        <span class="date_title" id="date_demo3">点击选择时间段</span>
                        <a class="opt_sel" id="input_trigger_demo3" href="#">
                            <i class="i_orderd"></i>
                        </a>
                       </div>
                     </div>
                     <div id="echart_orders"></div>
                   </div>
                 </div>
               </div>
               <div class="box3">
                 <div class="l">
                   <div class="cbox">
                     <div class="tit">销售数据概览</div>
                     <div class="top">
                       <ul>
                         <li>
                           <span>民生宝</span>
                           <b>${data.msbRechargeAmountPercent }%</b>
                         </li>
                         <li>
                           <span>IC卡</span>
                           <b>${data.icRechargeAmountPercent }%</b>
                         </li>
                       </ul>
                     </div>
                     <div class="bottom">
                      <ul>
                        <li>
                          <span>充值金额</span>
                          <b>${data.msbRechargeAmount }</b>
                          <b>${data.icRechargeAmount }</b>
                        </li>
                        <li>
                          <span>赠送金额</span>
                          <b>${data.msbGiveAmount }</b>
                          <b>${data.icGiveAmount }</b>
                        </li>
                        <li>
                          <span>充值笔数</span>
                          <b>${data.msbRechargeOrder }</b>
                          <b>${data.icRechargeOrder }</b>
                        </li>
                      </ul>
                     </div>
                   </div>
                 </div>
                 <div class="r">
                   <div class="cbox">
                     <div class="tit">用户概览</div>
                     <div class="top">
                       <ul>
                         <li>
                           <span>民生宝</span>
                           <b>${data.msbUserCountPercent }%</b>
                         </li>
                         <li>
                           <span>IC卡</span>
                           <b>${data.icUserCountPercent }%</b>
                         </li>
                       </ul>
                     </div>
                     <div class="bottom">
                      <ul>
                        <li>
                          <span>用户总数</span>
                          <b>${data.msbUserCount }</b>
                          <b>${data.icUserCount }</b>
                        </li>
                        <li>
                          <span>今日新增用户数</span>
                          <b>${data.todayMsbAddUserCount }</b>
                          <b>${data.todayICAddUserCount }</b>
                        </li>
                        <li>
                          <span>今日购水用户数</span>
                          <b>${data.todayMsbTradeUserCount }</b>
                          <b>${data.todayICTradeUserCount }</b>
                        </li>
                      </ul>
                     </div>

                   </div>
                 </div>
               </div>
               <div class="box4">
                 <div class="l">
<!--                    <div class="cbox">
                     <div class="tit">配送数据概览</div>
                     <div class="top">
                       <div class="t">
                         <span>开通配送服务小区数</span>
                         <b>8800</b>
                       </div>
                       <div class="t">
                         <span>未开通配送服务小区数</span>
                         <b>8800</b>
                       </div>
                     </div>
                     <div class="bottom">
                       <div class="left">
                         <div class="mtit"><img src="images/icon_1.png" alt="">配送订单(个)<span>118</span></div>
                         <ul>
                           <li><a href="#">已完成<b>0</b></a></li>
                           <li><a href="#">待完成<b>1</b></a></li>
                           <li><a href="#">已关闭<b>1</b></a></li>
                         </ul>
                       </div>
                       <div class="left">
                         <div class="mtit"><img src="images/icon_2.png" alt="">配送人数(人)<span>118</span></div>
                         <ul>
                           <li><a href="#">已通过<b>0</b></a></li>
                           <li><a href="#">待审核<b>1</b></a></li>
                           <li><a href="#">工作中<b>1</b></a></li>
                         </ul>
                       </div>
                     </div>
                   </div> -->
                 </div>
                 <div class="r">
                   <div class="cbox">
                     <div class="tit">设备数据概览</div>
                     <div class="top">
                       <ul>
                         <li>
                           <span>设备总数</span>
                           <b>${data.totalEquip }</b>
                         </li>
                         <li>
                           <span>异常告警</span>
                           <b>${data.warnCountEquip }</b>
                         </li>
                       </ul>
                     </div>
                    <div id="expenses" class="chart"></div>
                   </div>
                 </div>
               </div>
<!--                <div class="box5">
                 <div class="l">
                   <div class="cbox">
                     <div class="tit">配送人员收入排行TOP10</div>
                     <table class="table1">
                       <tr>
                         <th>排名</th>
                         <th>姓名</th>
                         <th>配送收入</th>
                         <th>代充提成</th>
                         <th>总收入</th>
                       </tr>
                       <tr>
                         <td>Top1</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top2</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top3</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top4</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top5</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                     </table>
                   </div>
                 </div>
                 <div class="l">
                   <div class="cbox">
                     <div class="tit">配送人员收入排行TOP10</div>
                     <table class="table1">
                       <tr>
                         <th>排名</th>
                         <th>姓名</th>
                         <th>配送收入</th>
                         <th>代充提成</th>
                         <th>总收入</th>
                       </tr>
                       <tr>
                         <td>Top1</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top2</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top3</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top4</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                       <tr>
                         <td>Top5</td>
                         <td>小年轻</td>
                         <td>132456.4</td>
                         <td>132456.4</td>
                         <td>132456.</td>
                       </tr>
                     </table>
                   </div>
                 </div>
               </div>
             </div> -->
          </div>
       </div>
     </div>
     
    <script src="${ctx}js/jquery.min.js"></script>
    <script src="${ctx}plugins/layer/layer.js"></script>
    <script src="${ctx}js/select2.min.js"></script>
    <script src="${ctx}plugins/daterange/dateRange.js"></script>
    <script src="${ctx}js/echarts.min.js"></script>
    <script src="${ctx}js/underscore-min.js"></script>
    <script src="${ctx}js/bootstrap-datepicker.js"></script>
    <script src="${ctx}js/bootstrap-datepicker.zh-CN.js"></script>
    <script src="${ctx}js/slick.min.js"></script>
    <script src="${ctx}plugins/fancybox/jquery.fancybox.js"></script>
    <script src="${ctx}js/doT.min.js"></script>
    <script src="${ctx}js/script.js"></script>
    <script>
      $(function(){
        setMenu(0);
        
        var ctx = '${ctx}';
        var dateRange = new pickerDateRange('date_demo3', {
          isTodayValid : true,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo3',
          theme : 'ta',
          success : function(obj) {
            $('.time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
              layer.msg("选择时间范围不能超过31天");
            }else{
              set_orderChart(5,start,end);
            }
          }
        });
        //默认调用最近1月数据
        set_orderChart(2,null,null);
        $('.time_items a').click(function(){
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

        $('#main_content').resize(function(){
          echart_orders.resize();
        });

      });
      //订单图表
      var echart_orders = echarts.init(document.getElementById('echart_orders'));
      function set_orderChart(type, beginDate, endDate){
        $.ajax({
            type: 'get',
            url: ctx + 'data/saleOrderTrendStat',
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
                  name:'订单数',
                  type:'line',    
                  stack: '总量',
                  data: _.flatten(values)
              }]
            };
            echart_orders.setOption(option);
          }
        });
      }


      //获取设备状况数据
      // $.ajax({
      //   type: 'get',
      //   url: url,
      //   success: function(res){
          var data = [
            {value:130, name:'正常'},
            {value:200, name:'告警'},
            {value:100, name:'损坏'}
            ];
      //   }
      // });
      var expenses = echarts.init(document.getElementById('expenses'));
      expenses.setOption({
        title : {
            text: '',
        },
        tooltip: {
            trigger: 'item',
            formatter: "{c}"
        },
        legend: {
            orient: 'vertical',
            right: '20',
            bottom: '20',
            data: ['正常','告警','损坏'],
            padding: 5,
            itemGap: 8,
            itemWidth: 5,
            itemHeight: 5,
        },
        series : [
            {
                name: '设备状况',
                type: 'pie',
                radius : '80%',
                center: ['50%', '50%'],
                data: ${equipData},
                itemStyle: {
                    emphasis: {
                        shadowBlur: 10,
                        shadowOffsetX: 0,
                        shadowColor: 'rgba(0, 0, 0, 0.5)'
                    }
                }
            }
        ],
        color: ['#34bfce','#ff9100', '#38cf9a']
      });

    </script>
  </body>
</html>
