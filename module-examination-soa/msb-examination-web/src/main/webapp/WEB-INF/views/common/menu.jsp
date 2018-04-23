<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<%@include file="../common/taglib.jsp"%>
<%-- <div id="header">
	<div class="container">
		<a href="#" class="logo">民生水宝<small>后台管理系统</small></a>
		<div class="header_menu">
			<span><shiro:principal /><i class="fa fa-caret-down"></i></span>
			<ul>
				<li><a href="${ctx }admin/passwordEdit">修改密码</a></li>
				<li><a href="${ctx }logout">退出</a></li>
			</ul>
		</div>
	</div>
</div> --%>

<div id="leftmenu">
	<div class="menu_p">
		<h3>后台操作</h3>
		<ul>
			<li data-id="0"><a href="${ctx }"><i class="fa fa-folder-o"></i>首页</a></li>
			<c:forEach var="menu" items="${ menus }" varStatus="status">
				<li data-id="${menu.id }"><a href="javascript:;"><i
						class="fa fa-folder-o"></i>${menu.name }</a></li>
			</c:forEach>

		</ul>
	</div>
	<div class="menu_s">
		<c:forEach var="menu" items="${ menus }" varStatus="status">
			<div data-pid="${menu.id }" class="menu_block">
				<h3>${menu.title }</h3>
				<c:if test="${menu.hasChild }">
					<ul>
						<c:forEach var="child" items="${ menu.childs }" varStatus="status">
							<li data-id="${child.id }"><a href="${ctx }${child.url }"><i
									class="fa fa-circle-o"></i>${child.name }</a></li>
						</c:forEach>
					</ul>
				</c:if>

			</div>
		</c:forEach>

	</div>
</div>
