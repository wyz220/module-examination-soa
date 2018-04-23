<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
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
				<h3>添加角色</h3>
			</div>
			<div class="main">
				<div class="form_box">
					<div class="passwordForm">
						<form action="saveOrUpdate" class="dFrom">
							<input type="hidden" id="id" value="${role.id}" name="id" />
							<div class="block">
								<div class="fg">
									<label>权限组名：</label> <input type="text" class="text"
										name="name" id="name" maxlength="30" value="${role.name }">
								</div>
							</div>
							<div class="block">
								<div class="fg fgh">
									<label>权限设置：</label>
									<div class="quanxian">
										<div class="top">
											<a href="javascript:;" class="checkALL">[全选]</a> <a href="javascript:;"
												class="uncheckALL">[全部取消]</a>
										</div>
										<c:forEach var="menu" items="${ allMenus }" varStatus="status">
											<div class="line">
												<div class="parent">
													<label><input type="checkbox" name="menuIds"
														value="${menu.id }"
														<c:if test="${menu.checked }">checked=true</c:if>>${menu.name }</label>
												</div>
												<c:if test="${menu.hasChild }">
													<div class="childs">
														<ul>
															<c:forEach var="child" items="${ menu.childs }"
																varStatus="status">
																<li><label><input type="checkbox"
																		name="menuIds" class="c" value="${child.id }"
																		<c:if test="${child.checked }">checked=true</c:if>>${child.name}</label>

																	<c:if test="${child.hasChild }">
																		<dl>
																			<c:forEach var="button" items="${ child.childs }"
																				varStatus="status">
																				<dd>
																					<label><input type="checkbox"
																						name="menuIds" value="${button.id }"
																						<c:if test="${button.checked }">checked=true</c:if>>${button.name}</label>

																				</dd>
																			</c:forEach>
																		</dl>

																	</c:if></li>
															</c:forEach>

														</ul>
													</div>
												</c:if>
											</div>
										</c:forEach>
									</div>
								</div>
							</div>
							<button type="button" id="submit_btn" class="btn btn-primary">确认提交</button>
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
	<script src="${ctx }js/role.js"></script>
	<script>
    $.role_list.role_list();
	  $(function(){
	        setMenu(5,13);
	        $('.checkALL').click(function(){
	            $('.quanxian input').prop("checked", true);  
	          });
	          $('.uncheckALL').click(function(){
	            $('.quanxian input').prop("checked", false);  
	          });
	          $('.parent input').click(function(){
	            if(this.checked){    
	              $(this).parent().parent().siblings().find('input').prop("checked", true);   
	              }else{    
	              $(this).parent().parent().siblings().find('input').prop("checked", false);  
	             }   
	          });
	          $('.childs>ul>li>label>input').click(function(){
	            if(this.checked){    
	              $(this).parent().siblings().find('input').prop("checked", true);   
	              }else{    
	              $(this).parent().siblings().find('input').prop("checked", false);  
	            }
	            var t = $(this).parents('.childs').find('input.c:checked');
	            if(t.length<1){
	              $(this).parents('.childs').siblings().find('input').prop("checked",false);
	            }else{
	              $(this).parents('.childs').siblings().find('input').prop("checked",true);
	            }
	          });

	          $('.childs dd input').click(function(){
	            var t = $(this).parents('dl').find('input:checked');
	            if(t.length<1){
	              $(this).parents('dl').siblings().find('input').prop("checked",false);
	            }else{
	              $(this).parents('dl').siblings().find('input').prop("checked",true);
	            }
	            var c = $(this).parents('.childs').find('input.c:checked');
	            if(c.length<1){
	              $(this).parents('.childs').siblings().find('input').prop("checked",false);
	            }else{
	              $(this).parents('.childs').siblings().find('input').prop("checked",true);
	            }
	          });

	          });
    </script>
</body>
</html>