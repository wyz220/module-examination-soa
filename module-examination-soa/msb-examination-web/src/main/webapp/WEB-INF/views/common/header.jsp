<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@include file="../common/taglib.jsp"%>
<div id="header">
	<div class="container">
		<a href="#" class="logo">民生和泰<small>后台考试管理系统</small></a>
		<div class="header_menu">
			<span><shiro:principal /><i class="fa fa-caret-down"></i></span>
			<ul>
				<li><a href="${ctx }admin_password">修改密码</a></li>
				<li><a href="${ctx }logout">退出</a></li>
			</ul>
		</div>
	</div>
</div>
