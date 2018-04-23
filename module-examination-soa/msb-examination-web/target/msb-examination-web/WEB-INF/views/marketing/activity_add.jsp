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
            <h3>创建活动</h3>
          </div>
          <div class="main">
            <form action="" class="dFrom">
            <input type="hidden" id="id" value="${activity.id}" name="id" />
              <div class="block">
                <div class="fg">
                  <label>活动名称：</label>
                  <input type="text" class="text" id="title" name="title" maxlength="50" value="${activity.title }">
                  <label>活动类型：</label>
                  <select class="select" id="type" name="type">
                    <option value="">请选择</option>
                    <c:forEach var="map" items="${ activityTypeMap }" varStatus="status">
                     <option value="${map.key }"  <c:if test="${activity.type == map.key }">selected="selected"</c:if>>${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
              <div class="block">
                <div class="fg">
                  <label>活动范围：</label>
                  <select class="select" id="scope" name="scope">
                    <option value="">请选择</option>
                    <c:forEach var="map" items="${ activityScopeMap }" varStatus="status">
                     <option value="${map.key }"  <c:if test="${activity.scope == map.key }">selected="selected"</c:if>>${map.value }</option>
                    </c:forEach>
                  </select>
                  <label>活动时间：</label>
                  <span id="input_trigger_demo"></span>
                  <input type="hidden" name="beginTime" id="beginTime" value="${activity.beginTime }">
                  <input type="hidden" name="endTime" id="endTime" value="${activity.beginTime }">
                </div>
              </div>
              <div class="block">
                <div class="fg">
                  <label>充值金额：</label>
                  <select name="packageId" class="select" id="packageId">
                    <option value="">请选择</option>
                      <c:forEach var="pack" items="${ packageList }" varStatus="status">
                    <option value="${pack.id }" <c:if test="${pack.id == activity.packageId}">selected="selected"</c:if>>${pack.amount }</option>
                    </c:forEach>
                  </select>
                </div>
              </div>
              <div class="block">
                <div class="fg">
                  <label>充赠送金额：</label>
                  <input type="text" class="text" id="giveFee" name="giveFee" maxlength="19" value="${activity.giveFee }">
                </div>
              </div>
              <div class="block">
                <div class="fg">
                  <label>活动赠送总金额：</label>
                  <input type="text" placeholder="选填" class="text" id="totalNum" name="totalNum" maxlength="19" value="${activity.totalNum }">
                </div>
              </div>

              <button type="button" class="btn btn-primary" id="submit_btn">保存</button>
            </form>
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
    <script src="${ctx }js/activity.js"></script>
    <script>
    $.activity_list.activity_list();
      $(function(){
        setMenu(4,25);
        var defaultText = '请选择活动起止时间';
        var beginTime = '${activity.beginTime }';
        var endTime = '${activity.endTime }';
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
      });

    </script>

  </body>
</html>