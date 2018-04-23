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
              <h3>账号列表</h3>
          </div>
          <div class="main">
            <table class="table1">
              <tr>
                <th width="15%">账号</th>
                <th>权限</th>
                <th width="15%">操作</th>
              </tr>
              <c:forEach var="user" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td>${user.username }</td>
                <td><c:if test="${user.role != null }">${user.role.name}</c:if></td>
                <td>
                <shiro:hasPermission name="user:edit">
                <a href="${ctx }user/add?id=${user.id}" class="btn">编辑</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="user:delete">
                <a href="javascript:;" onclick="user_delete(${user.id})" class="btn">删除</a>
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
    <script src="${ctx }js/underscore-min.js"></script>
    <script src="${ctx }js/bootstrap-datepicker.js"></script>
    <script src="${ctx }js/bootstrap-datepicker.zh-CN.js"></script>
    <script src="${ctx }js/slick.min.js"></script>
    <script src="${ctx }plugins/fancybox/jquery.fancybox.js"></script>
    <script src="${ctx }js/doT.min.js"></script>
    <script src="${ctx }js/script.js"></script>
    <script src="${ctx }js/user.js"></script>
    <script>
    $.user_list.user_list();
      $(function(){
        setMenu(5,9);
      });

    </script>
  </body>
</html>