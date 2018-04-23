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
              <h3>用户列表</h3>
              <div class="pright">
              <shiro:hasPermission name="member:export">
                <a href="javascript:;" class="btn" id="export_btn">导出Excel</a>
              </shiro:hasPermission>
              </div>
          </div>
          <div class="main_filter">
            <div class="form">
              <form action="list">
                <div class="fg">
                  <span id="input_trigger_demo"></span>
                  <input type="hidden" name="beginTime" id="beginTime" value="${param.beginTime }">
                  <input type="hidden" name="endTime" id="endTime" value="${param.endTime }">
                </div>
                <div class="fg">
                  <select name="type" id="type">
                    <option value="">用户类型</option>
                     <c:forEach var="map" items="${ memberTypeMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.type }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="用户账号" name="account" id="account" value="${param.account }" maxlength="30">
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
                <th>用户类型</th>
                <th>用户帐号</th>
                <th>总余额</th>
                <th>充值余额</th>
                <th>赠送余额</th>
                <th>激活时间</th>
                <th>累计消费</th>
                <th>累计充值</th>
                <th>操作</th>
              </tr>
              <c:forEach var="member" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td>
                 <c:forEach var="map" items="${ memberTypeMap }" varStatus="status">
                   <c:if test="${member.type == map.key }">${map.value }</c:if>
                 </c:forEach>
                </td>
                <td>${member.account }</td>
                <td>${member.payBalance + member.giveBalance }</td>
                <td>${member.payBalance}</td>
                <td>${member.giveBalance }</td>
                <td><fmt:formatDate value="${ member.activationTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td>${member.consumeAmount }</td>
                <td>${member.rechargeAmount }</td>
                <td>
                <shiro:hasPermission name="member:view">
                <a href="detail?id=${member.id }" class="btn" >查看</a>
                </shiro:hasPermission>
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
        setMenu(2,32);
        var defaultText = '请选择激活时间';
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
    			var type = $('#type').val();
    			var account = $('#account').val();
    			var beginTime = $("#beginTime").val();
    			var endTime = $("#endTime").val();
    		
    			var url="export?1=1";
    			if (type!=null && type!="") {
    				url += "&type=" + type;
    			}
    			if (account!=null && account!="") {
    				url += "&account=" + account;
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
      function tuikuan(id){
        if(id){
        layer.confirm('确认要进行退款操作吗？', {
          btn: ['确认退款'] ,title:'退款提示'
        }, function(){
          layer.msg('退款成功！', {icon: 1},function(){
            location.reload();
          });
        });
        }
      }
    </script>
  </body>
</html>