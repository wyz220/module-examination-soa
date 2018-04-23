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
              <h3>IC卡充值</h3>
              <div class="pright">
              <shiro:hasPermission name="icrecharge:add">
                <a href="javascript:;" onclick="add_active()" class="btn">充值</a>
              </shiro:hasPermission>
              <shiro:hasPermission name="icrecharge:export">
                <a href="javascript:;" class="btn" id="export_btn">导出</a>
              </shiro:hasPermission>
              </div>
          </div>
          <div class="main_filter">
            <div class="form">
              <form action="">
                <div class="fg">
                  <select name="status" id="status">
                    <option value="">活动状态</option>
                     <c:forEach var="map" items="${ syncWriteCardMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.status }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <select name="type" id="type">
                    <option value="1" <c:if test="${1 == param.type }">selected="selected"</c:if>>充值卡号</option>
                    <option value="2" <c:if test="${2 == param.type }">selected="selected"</c:if>>充值金额</option>
                    <option value="3" <c:if test="${3 == param.type }">selected="selected"</c:if>>赠送金额</option>
                  </select>
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="请输入关键词" id="keyword" name="keyword" maxlength="50" value="${param.keyword }">
                </div>
                <div class="fg">
                  <span id="input_trigger_demo"></span>
                  <input type="hidden" id="beginTime" name="beginTime" value="${param.beginTime }">
                  <input type="hidden" id="endTime" name="endTime" value="${param.endTime }">
                </div>
                <button class="btn" type="submit">搜索</button>
              </form>
            </div>
          </div>
          <div class="main">
            <table class="table1">
              <tr>
                <th>订单号</th>
                <th>充值卡号</th>
                <th>充值金额 </th>
                <th>赠送金额 </th>
                <th>充值时间 </th>
                <th>订单状态</th>
                <th>写卡时间 </th>
                <th>操作人</th>
              </tr>
              <c:forEach var="order" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td>${order.orderNo }</td>
                <td>${order.account }</td>
                <td>${order.payFee }</td>
                <td>${order.giveFee }</td>
                <td><fmt:formatDate value="${ order.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td>
                <c:forEach var="map" items="${ syncWriteCardMap }" varStatus="status">
                <c:if test="${map.key == order.syncWriteCard }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>
                <c:if test="${order.writeCardTime != null }">
                <fmt:formatDate value="${ order.writeCardTime }" pattern="yyyy-MM-dd HH:mm:ss" />
                </c:if>
                </td>
                <td>${order.createName }</td>
              </tr>
              </c:forEach>
            </table>
 <jsp:include page="../common/page.jsp"></jsp:include>
          </div>
       </div>
     </div>

     <div id="marketing_modal" class="marketing_modal">
       <form action="" class="dFrom" id="recharge_form">
        <input type="hidden" name="id" value="">
        <div class="block">
          <div class="fg">
            <label>充值卡号：</label>
            <input type="text" class="text" id="cardNumber" name="cardNumber" maxlength="10">
          </div>
          <div class="fg">
            <label>选择套餐：</label>
            <select id="packId" name="packId" class="" >
              <option value="">请选择</option>
              <c:forEach var="pack" items="${ packList }" varStatus="status">
              <option value="${pack.id }">${pack.amount }</option>
              </c:forEach>
            </select>
          </div>
        </div>
        <div class="bt">
          <button type="button" onclick="save()">保存</button>
        </div>
      </form>
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
    <script src="${ctx }js/jquery.form.min.js"></script>
    <script>
      $(function(){
        setMenu(4,50);
        var defaultText = '请选择充值时间';
        var beginTime = '${param.beginTime }';
        var endTime = '${param.endTime }';
        if (beginTime != null && beginTime != ''){
        	defaultText = beginTime;
        }
        if (endTime != null && endTime != ''){
        	defaultText = defaultText + ' ~ ' + endTime;
        }
        var dateRange = new pickerDateRange('input_trigger_demo', {
          startToday:true, 
          stopToday:false, 
          isTodayValid:true,
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
        	var status = $("#status").val();
        	var type = $("#type").val();
        	var keyword = $('#keyword').val();
    		var beginTime = $("#beginTime").val();
      		var endTime = $("#endTime").val();  		
  			var url="export?1=1";
  			if (status!=null && status!="") {
  				url += "&status=" + status;
  			}
  			if (type!=null && type!="") {
  				url += "&type=" + type;
  			}
  			if (keyword!=null && keyword!="") {
  				url += "&keyword=" + keyword;
  			}
  			if (beginTime!=null && beginTime!="") {
  				url += "&beginTime=" + beginTime;
  			}
  			if (endTime!=null && endTime!="") {
  				url += "&endTime=" + endTime;
  			}
  			window.location.href = url;				
  		});
      });

      function add_active(){
        layer.open({
          type: 1,
          title: "IC卡充值",
          closeBtn: 1,
          area: '400px',
          shadeClose: true,
          btn: false,
          content: $('#marketing_modal')
        });
      }

      function save(){
    	  var cardNumber = $('#cardNumber').val();
    	  var packId = $('#packId').val();
    	if (cardNumber == null || cardNumber == ''){
      		layer.msg("请输入卡号！");
      		return;
    	}
      	if (isNaN(cardNumber)){
      		layer.msg("卡号必须为数字！");
      		return;
      	}
      	if (packId == null || packId == ''){
      		layer.msg("请选择套餐！");
      		return;
      	}
          var index = layer.load(1);
          $("#recharge_form").ajaxSubmit({  
              url : "recharge",  
              type : "post",  
              dataType : 'json',  
              success : function(data) {  
      			var result = data['result'];
      			var message = data['message'];
      			if (result=='success') {
      			       layer.msg('操作成功！', {icon: 1},function(){
      			            location.reload();
      			          });
      			} else {
      				layer.msg(message);
      				layer.close(index);
      				return;
      			}
              },  
              error : function(data) {  
                  alert("error:" + data);  
              }  
          });  
      }
    </script>
  </body>
</html>