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
				<h3>新建账号</h3>
			</div>
			<div class="main">
				<div class="form_box">
					<div class="passwordForm">
						<form action="" class="dFrom">
							<div class="block">
								<div class="fg">
									<label>设置账号：</label> <input type="text" class="text"
										placeholder="6-12位数字或英文字母" name="username" id="username"
										value="${user.username }" maxlength="12">
								</div>
							</div>
							<div class="block">
								<div class="fg">
									<label>设置密码：</label> <input type="password"
										placeholder="6-18位数字或英文字母" class="text" name="password"
										id="password" maxlength="18">
								</div>
							</div>
							<div class="block">
								<div class="fg">
									<label>确认密码：</label> <input type="password"
										placeholder="6-18位数字或英文字母" class="text" name="confirmPassword"
										id="confirmPassword" maxlength="18">
								</div>
							</div>
							<div class="block">
								<div class="fg">
									<label>选择角色：</label> <select name="roleId" id="roleId"
										class="select">
										<c:forEach var="role" items="${ roleList}" varStatus="status">
											<c:choose>
												<c:when test="${ role.id==roleId }">
													<option value="${ role.id }" selected="selected">${ role.name }</option>
												</c:when>
												<c:otherwise>
													<option value="${ role.id }">${ role.name }</option>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</select>
								</div>
							</div>
							<button type="button" id="submit_btn" class=" btn-primary">确认提交</button>
						</form>
					</div>
				</div>
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
	<script src="${ctx }js/script.js"></script>
	<script src="${ctx }js/user.js"></script>
	<script>
    $.user_list.user_list();
      $(function(){
        setMenu(5,10);
      });
    </script>
</body>
</html>