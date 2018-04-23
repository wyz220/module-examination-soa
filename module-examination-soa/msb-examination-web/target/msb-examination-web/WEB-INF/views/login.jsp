<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<%@include file="common/taglib.jsp"%>
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <title>登录</title>
    <link href="${ctx }css/font-awesome.min.css" rel="stylesheet">
    <link href="${ctx }plugins/layer/skin/layer.css" rel="stylesheet">
    <link href="${ctx }css/style.css" rel="stylesheet">
  </head>
  <body>
  	<div class="login_wrap">
  		<div id="login_header">
	  		<div class="cont">
	  			<img src="${ctx }images/logo.png" alt="">
	  			<h2>民生水宝 - </h2>
	  			<h4>后台管理</h4>
	  		</div>
	  	</div>
	  	<img src="images/bg1.png" alt="" class="bg2">
		<div id="login" class="login">
			<div class="login_box">
				<!--登录-->
				<div class="login_form">
					<form action="" id="login_form" method="post">
						<h3>登录管理系统</h3>
						<div class="fg">
							<i class="fa fa-user"></i>
							<input type="text" name="username" id="username" class="form-text" placeholder="用户名">
						</div>
						<div class="fg">
							<i class="fa fa-key"></i>
							<input type="text" name="password" class="form-text toggle_password" id="password" placeholder="密码">
						</div>
<!-- 						<div class="remember">
							<div class="l"><label><input type="checkbox" name="rememberMe"> 记住密码</label></div>
						</div> -->
						<button type="submit" class="btn login">登录</button>
					</form>
				</div>
			</div>
		</div>
		<div id="footer">
			<p>Copyright © 2017 - 2018 msbapp.cn All Rights Reserved.</p> 
		</div>
  	</div>
    <script src="${ctx }js/jquery.min.js"></script>
    <script src="${ctx }js/hideShowPassword.min.js"></script>
    <script src="${ctx }plugins/layer/layer.js"></script>
    <script>
		$(function(){
			$('.form-text').focus(function(){
				$(this).parents('.fg').addClass('fill');
			}).blur(function(){
				$(this).parents('.fg').removeClass('fill');
			});
      		$('.toggle_password').hidePassword(true);

      		$('#login_form').submit(function(){
		        //邮箱验证
		        if ($("#username").val() == "") { 
		          layer.msg('用户名不能为空！');
		          $("#username").focus(); 
		          return false; 
		        } 
		        //验证密码不能为空
		        if ($("#password").val() == "") { 
		          layer.msg('密码不能为空！');
		          $("#password").focus(); 
		          return false; 
		        } 
		        return true; 
		    });
		});
    </script>
  </body>
</html>