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
    <link href="${ctx}css/slick.css" rel="stylesheet">
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
                     <div class="tit">欢迎进入后台考试管理系统！</div>
                   </div>
                 </div>
               </div>
          </div>
       </div>
     </div>
     
    <script src="${ctx}js/jquery.min.js"></script>
    <script src="${ctx}js/slick.min.js"></script>
    <script src="${ctx}js/script.js"></script>
    
  </body>
</html>
