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
	   <jsp:include page="common/header.jsp"></jsp:include>
	<div class="wrap">
		<jsp:include page="common/menu.jsp"></jsp:include>
       <div id="main_content">
          <div class="main_title">
            <h3>修改密码</h3>
          </div>
          <div class="main">
            <form action="" class="dFrom">
              <div class="block">
                <div class="fg">
                  <label>原密码：</label>
                  <input type="password" id="oldPwd" class="text" name="oldPwd" maxlength="20">
                </div>
              </div>
              <div class="block">
                <div class="fg">
                  <label>新密码：</label>
                  <input type="password" id="newPwd" class="text" name="newPwd"  maxlength="20">
                  <span class="tip">密码由6-18位字母、数字组成，字母需区分大小写</span>
                </div>
              </div>
              <div class="block">
                <div class="fg">
                  <label>确认密码：</label>
                  <input type="password" id="confirmPwd" class="text" name="confirmPwd"  maxlength="20">
                  <span class="tip">密码由6-18位字母、数字组成，字母需区分大小写</span>
                </div>
              </div>
              <button type="button" class="btn btn-primary" id="submit_btn">修改</button>
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
    <script>
      $(function(){
    	  
      var ctx = '${ctx}';
      $('#submit_btn').click(function(){
			var oldPwd = $.trim($('#oldPwd').val());
			var newPwd = $.trim($('#newPwd').val());
			var confirmPwd = $.trim($("#confirmPwd").val());
			
			 if(oldPwd.length<6||oldPwd.length>18){
		          layer.msg('密码必须6~18个字符!');
		          $('#oldPwd').focus();
		          return false;
		        }
		        if(newPwd.length<6||newPwd.length>18){
		          layer.msg('新密码必须6~18个字符!');
		          $('#newPwd').focus();
		          return false;
		        }
		        if(confirmPwd!=newPwd){
		          layer.msg('两次密码必须输入一致!');
		          $('#confirmPwd').focus();
		          return false;
		        }
		    	var reg=/^[a-z0-9A-Z]{6,18}$/;
		    	if (!reg.test(newPwd)){
					layer.msg('密码由6-18位字母、数字组成，字母需区分大小写！');
					return false;
		    	}
							
		
			var jsonRequest = $.ajax({
				type: "POST",
				url: ctx + "modifyPwd",
				data: {oldPwd:oldPwd, newPwd:newPwd,confirmPwd:confirmPwd},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				if (result=='success') {
			          layer.msg('修改密码成功！', {icon: 1},function(){
			              location.reload();
			            });
				} else {
					layer.msg(message);
				}
			});
		});
      
      });
    </script>

  </body>
</html>
