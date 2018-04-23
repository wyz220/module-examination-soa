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
              <h3>购水订单</h3>
              <div class="pright">
              <shiro:hasPermission name="recharge:export">
                <a href="javascript:;" class="btn" id="export_btn">导出Excel</a>
              </shiro:hasPermission>
              </div>
          </div>
          <div class="main_filter">
            <div class="form">
              <form action="">
                <div class="fg">
                  <span id="input_trigger_demo"></span>
                  <input type="hidden" id="beginTime" name="beginTime" value="${param.beginTime }">
                  <input type="hidden" id="endTime" name="endTime" value="${param.endTime }">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="充值账号" id="account" name="account" maxlength="30" value="${param.account }">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="订单号" id="orderNo" name="orderNo" maxlength="20" value="${param.orderNo }">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="设备ID" id="equipmentNo" name="equipmentNo" maxlength="10" value="${param.equipmentNo }">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="设备地址" id="address" name="address" maxlength="200" value="${param.address }">
                </div>
                <div class="fg">
                  <select name="payType" id="payType">
                    <option value="">付款方式</option>
                    <option value="1" <c:if test="${1 == param.payType }">selected="selected"</c:if>>微信</option>
                    <option value="2" <c:if test="${2 == param.payType }">selected="selected"</c:if>>支付宝</option>
                  </select>
                </div>
                <div class="fg">
                  <select name="source" id="source">
                    <option value="">订单类型</option>
                     <c:forEach var="map" items="${ orderSourceMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.source }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="付款手机号" id="payAccount" name="payAccount" maxlength="30">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="小区名称" id="communityName" name="communityName" maxlength="100">
                </div>
                <div class="fg">
                  <button class="btn" type="submit">搜索</button>
                </div>
              </form>
            </div>
          </div>
          <div class="main">
            <table class="table1">
              <tr>
                <th>订单类型</th>
                <th>订单号</th>
                <th>充值帐号</th>
                <th>充值金额</th>
                <th>赠送金额</th>
                <th>充值时间</th>
                <th>支付方式</th>
                <th>支付状态</th>
                <th>充值余额</th>
                <th>赠送余额</th>
                <th>付款帐号</th>
                <th>充值设备ID</th>
                <th>设备地址</th>
                <th>小区名称</th>
              </tr>
               <c:forEach var="order" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td>
                <c:forEach var="map" items="${ orderSourceMap }" varStatus="status">
                <c:if test="${order.source == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>${order.orderNo }</td>
                <td>${order.account }</td>
                <td>${order.payFee }</td>
                <td>${order.giveFee }</td>
                <td>
                <c:if test="${order.payTime != null }">
                 <fmt:formatDate value="${ order.payTime }" pattern="yyyy-MM-dd HH:mm:ss" />
                </c:if>
                </td>
                <td>
                <c:forEach var="map" items="${ payTypeMap }" varStatus="status">
                <c:if test="${order.payType == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>
                <c:forEach var="map" items="${ orderStatusMap }" varStatus="status">
                <c:if test="${order.status == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>${order.payBalance }</td>
                <td>${order.giveBalance }</td>
                <td>${order.payAccount }</td>
                <td>
                  <c:choose>
                     <c:when test="${order.equipmentNo == null }">
                                                                       无
                     </c:when>
                     <c:otherwise>
                     ${order.equipmentNo}
                     </c:otherwise>
                  </c:choose>
                                                 
                </td>
                <td title="${order.equipAddress }">
                  <c:choose>
                     <c:when test="${order.equipAddress == null }">
                                                                       无
                     </c:when>
                     <c:otherwise>
                     <c:out value="${order.equipAddress}"></c:out>
                     </c:otherwise>
                  </c:choose>
                </td>
                <td>
                   <c:choose>
                     <c:when test="${order.equipCommunityName == null }">
                                                                       无
                     </c:when>
                     <c:otherwise>
                     ${order.equipCommunityName}
                     </c:otherwise>
                  </c:choose>
                </td>
              </tr>
              </c:forEach>
            </table>
 <jsp:include page="../common/page.jsp"></jsp:include>
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
        setMenu(1,39);
        var defaultText = '请选择支付时间';
        var beginTime = '${param.beginTime }';
        var endTime = '${param.endTime }';
        if (beginTime != null && beginTime != ''){
        	defaultText = beginTime;
        }
        if (endTime != null && endTime != ''){
        	defaultText = defaultText + ' ~ ' + endTime;
        }
        var dateRange = new pickerDateRange('input_trigger_demo', {
            isTodayValid : true,
            defaultText : ' ~ ',
            inputTrigger : 'input_trigger_demo',
            theme : 'ta',
            success : function(obj) {
              var start = obj.startDate;//开始时间
              var end = obj.endDate; //结束时间
              $('input[name=beginTime]').val(start);
              $('input[name=endTime]').val(end);
            }
          });
          $("#input_trigger_demo").text(defaultText);
          
          $('#export_btn').click(function(){
      		var beginTime = $("#beginTime").val();
        		var endTime = $("#endTime").val();
    			var account = $('#account').val();
    			var orderNo = $('#orderNo').val();
    			var equipmentNo = $("#equipmentNo").val();
    			var address = $("#address").val();
    			var payType = $("#payType").val();
    			var source = $("#source").val();
    			var payAccount = $("#payAccount").val();
    			var communityName = $("#communityName").val();
    		
    			var url="rechargeExport?1=1";
    			if (beginTime!=null && beginTime!="") {
    				url += "&beginTime=" + beginTime;
    			}
    			if (endTime!=null && endTime!="") {
    				url += "&endTime=" + endTime;
    			}
    			if (account!=null && account!="") {
    				url += "&account=" + account;
    			}
    			if (orderNo!=null && orderNo!="") {
    				url += "&orderNo=" + orderNo;
    			}
    			if (equipmentNo!=null && equipmentNo!="") {
    				url += "&equipmentNo=" + equipmentNo;
    			}
    			if (address!=null && address!="") {
    				url += "&address=" + address;
    			}
      			if (payType!=null && payType!="") {
    				url += "&payType=" + payType;
    			}
    			if (source!=null && source!="") {
    				url += "&source=" + source;
    			}
    			if (payAccount!=null && payAccount!="") {
    				url += "&payAccount=" + payAccount;
    			}
    			if (communityName!=null && communityName!="") {
    				url += "&communityName=" + communityName;
    			}


    			window.location.href = url;				
    		});
      });

    </script>
  </body>
</html>