<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>民生宝企业管理系统</title>
<link href="/css/font-awesome.min.css" rel="stylesheet">
<link href="/plugins/layer/skin/layer.css" rel="stylesheet">
<link rel="stylesheet" href="/css/foundation-datepicker.css">
<link href="/plugins/fancybox/jquery.fancybox.css" rel="stylesheet">
<link href="/css/select2.min.css" rel="stylesheet">
<link href="/css/style.css" rel="stylesheet">
</head>
<body class="bodyhide">
	<div class="wrap">
    <jsp:include page="../common/menu.jsp">
    	<jsp:param value="manager:*" name="pageType"/>
    </jsp:include>
		<div class="page-wrapper">
			<div class="page_main">
				<div class="page_header">
					<div class="top">
						<div class="logo">
							<img src="../images/logo.png">民生宝企业派单管理系统
						</div>
					</div>
				</div>
				<div class="page_content">
					<div id="scroll_content" class="content">
						<div class="ibox">
							<div class="memberEdit">
								<div class="title">
									<i class="fa fa-edit"></i> 密码修改
								</div>
								<div class="passwordForm">
									<form action="" class="dFrom">
										<div class="block">
											<div class="fg">
												<label>原密码：</label> <input type="password" class="text"
													name="oldPwd" id="oldPwd" maxlength="20">
											</div>
										</div>
										<div class="block">
											<div class="fg">
												<label>新密码：</label> <input type="password" class="text"
													name="newPwd" id="newPwd" maxlength="20">
											</div>
										</div>
										<div class="block">
											<div class="fg">
												<label>确认密码：</label> <input type="password" class="text"
													name="confirmPwd" id="confirmPwd" maxlength="20">
											</div>
										</div>
										<button type="button" onclick="modifyPass()"
											class="btn btn-primary">修改</button>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="/js/jquery.min.js"></script>
	<script src="/plugins/fancybox/jquery.fancybox.js"></script>
	<script src="/js/jquery.slimscroll.min.js"></script>
	<script src="/js/collapse.js"></script>
	<script src="/js/foundation-datepicker.js"></script>
	<script src="/plugins/layer/layer.js"></script>
	<script src="/js/jquery.raty.min.js"></script>
	<script src="/js/select2.min.js"></script>
	<script src="/js/common.js"></script>
	<script>
		
		function modifyPass(){

	        var oldPwd = $("#oldPwd").val();
	        if (oldPwd == "") { 
	          layer.msg('旧密码不能为空！');
	          $("#oldPwd").focus(); 
	          return false; 
	        } 
	        
	        var newPwd = $("#newPwd").val();
	        if (newPwd == "") { 
	          layer.msg('新密码不能为空！');
	          $("#newPwd").focus(); 
	          return false; 
	        } 
	        if (newPwd.length < 6 || newPwd.length > 20) { 
		          layer.msg('新密码长度只能在6-20个字符之间！');
		          $("#newPwd").focus(); 
		          return false; 
		     } 
	        
	        var confirmPwd = $("#confirmPwd").val();
	        if (confirmPwd == "") { 
	          layer.msg('确认密码不能为空！');
	          $("#confirmPwd").focus(); 
	          return false; 
	        } 
	        
	        if (newPwd != confirmPwd) { 
		          layer.msg('确认密码不一致，请重新输入！');
		          $("#confirmPwd").focus(); 
		          return false; 
		    } 
	        
			var jsonRequest = $.ajax({
				type: "POST",
				url: "/admin/modifyPwd",
				data: {oldPwd:oldPwd,newPwd:newPwd,confirmPwd:confirmPwd},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				if (result=='success') {
					layer.msg("修改密码成功！");
				} else {
					layer.msg(message);
					return;
				}
			});
	        return true; 
		}
    </script>
</body>
</html>