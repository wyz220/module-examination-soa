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
              <h3>用户详情</h3>
          </div>
          <div class="main">
            <div id="user_detail">
              <div class="info_box">
                <div class="l">
                  <img src="${ctx }images/user.png" alt="">
                  <div class="bbr">
                    <div class="t">${member.account } 
                    <small>
                      <c:choose>
                         <c:when test="${member.type == 1 }">
                                                                           民生宝会员
                         </c:when>
                         <c:otherwise>
                         IC卡用户
                         </c:otherwise>
                      </c:choose>
                    </small>
                    </div>
                    <div class="b">
                      <span class="left">充值余额:${member.payBalance }</span>
                      <span class="right">赠送余额:${member.giveBalance }</span>
                    </div>
                  </div>
                </div>
                <div class="r">
                  <div class="t">
                    <span class="left">近3个月消费次数：<b>
                    <c:choose>
                       <c:when test="${statReport.totalConsume == null}">
                        0
                       </c:when>
                       <c:otherwise>
                       ${statReport.totalConsume }
                       </c:otherwise>
                    </c:choose>
                    </b>次</span>
                    <span class="right">近3个月消费金额：<b>
                    <c:choose>
                       <c:when test="${statReport.consumeAmount == null}">
                        0.0
                       </c:when>
                       <c:otherwise>
                       ${statReport.consumeAmount }
                       </c:otherwise>
                    </c:choose>
                    </b></span>
                  </div>
                  <div class="bt">
                    <a href="javascript:;" onclick="refund(${member.id})">充值余额退款</a>
                  </div>
                </div>
              </div>
            </div>
            <table class="table1">
              <tr>
                <th>类型</th>
                <th>金额</th>
                <th>时间</th>
                <th>充值余额</th>
                <th>赠送余额</th>
                <th>相关订单</th>
              </tr>
              <c:forEach var="history" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td>
                 <c:forEach var="map" items="${ walletHistoryTypeMap }" varStatus="status">
                   <c:if test="${history.type == map.key }">${map.value }</c:if>
                 </c:forEach>
                </td>
                <td>${history.amount }</td>
                <td><fmt:formatDate value="${ history.createTime }" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                <td>${history.payBalance }</td>
                <td>${history.giveBalance }</td>
                <td>
                 <c:choose>
                   <c:when test="${history.orderNo != null }">
                   ${history.orderNo}
                   </c:when>
                   <c:otherwise>
                                                         无
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
        setMenu(2,32);
      });
      function refund(id){
        if(id){
        layer.confirm('确认要进行退款操作吗？', {
          btn: ['确认退款'] ,title:'退款提示'
        }, function(){
        	var jsonRequest = $.ajax({
   				type: "POST",
   				url: "refund",
   				data: {id:id},
   				dataType: "json",
   				asyn: false
   			});
   			
   			jsonRequest.done(function(data){
   				var result = data['result'];
   				var message = data['message'];
   				var json = data['data'];
   				if (result=='success') {
   					layer.msg('退款成功！', {icon: 1});
   					location.href="list";
   				} else {
   					layer.msg(message);
   					return;
   				}
   			});
          });
 
        }

      }
    </script>
  </body>
</html>