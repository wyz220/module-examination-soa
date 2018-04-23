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
              <h3>充值套餐设置</h3>
              <div class="pright">
               <shiro:hasPermission name="pack:add">
                <a href="javascript:;" onclick="add_active()" class="btn">新建套餐</a>
                </shiro:hasPermission>
              </div>
          </div>
          <div class="main">
            <table class="table1">
              <tr>
                <th>序号</th>
                <th>充值范围</th>
                <th>充值金额</th>
                <th>操作</th>
              </tr>
              <c:forEach var="pack" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td>${ (pageObj.pageNo-1) * pageObj.pageSize + status.count }</td>
                <td>
               <c:forEach var="map" items="${ activityScopeMap }" varStatus="status">
                <c:if test="${pack.type == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>${pack.amount }</td>
                <td>
                <shiro:hasPermission name="pack:edit">
                <a href="javascript:;" onclick="edit_active(${pack.id})" class="btn">编辑</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="pack:delete">
                <a href="javascript:;" onclick="del(${pack.id})" class="btn">删除</a>
                </shiro:hasPermission>
                </td>
              </tr>
              </c:forEach>
            </table>
             <jsp:include page="../common/page.jsp"></jsp:include>
          </div>
       </div>
     </div>

     <div id="marketing_modal" class="marketing_modal hide">
       <form action="" class="dFrom">
        <input type="hidden" id="id" name="id" value="">
        <div class="block">
          <div class="fg">
            <label>活动范围：</label>
            <select id="type" name="type" class="">
              <option value="">请选择</option>
              <c:forEach var="map" items="${ activityScopeMap }" varStatus="status">
              <option value="${map.key }">${map.value }</option>
              </c:forEach>
            </select>
          </div>
          <div class="fg">
            <label>充值金额：</label>
            <input type="text" class="text" name="amount" id="amount" maxlength="19">
          </div>
          <p class="text-center">说明：IC卡充值套餐限制最多5个。</p>
        </div>
        <div class="bt">
          <button type="button" onclick="save()">保存</button>
        </div>
      </form>
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
    <script src="${ctx }js/jquery.form.min.js"></script>
    <script>
      $(function(){
        setMenu(4,28);
      });
      function del(id){
        if(id){
         layer.confirm('确认要删除该套餐吗？', {
          btn: ['确认','取消'], //按钮
          title:"删除套餐"
        }, function(){
        	var jsonRequest = $.ajax({
				type: "POST",
				url: "delete",
				data: {id:id},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				var json = data['data'];
				if (result=='success') {
					layer.msg('删除成功！', {icon: 1});
					location.href="list";
				} else {
					layer.msg(message);
					return;
				}
			});
			
          
        }, function(){
          layer.msg('已取消!');
        });
        }
      }

      function add_active(){
          $('input[name="id"]').val('');
          $('select[name="type"]').val('');
          $('input[name="amount"]').val('');
        layer.open({
          type: 1,
          title: "新建套餐",
          closeBtn: 1,
          area: '400px',
          shadeClose: true,
          btn: false,
          content: $('#marketing_modal')
        });
      }

      function edit_active(id){
        if(id){
        	var jsonRequest = $.ajax({
				type: "POST",
				url: "get",
				data: {id:id},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				var json = data['data'];
				if (result=='success') {
	                   $('input[name="id"]').val(json.id);
	                   $('select[name="type"]').val(json.type);
	                   $('input[name="amount"]').val(json.amount);
	                   

	                   layer.open({
	                       type: 1,
	                       title: "编辑套餐",
	                       closeBtn: 1,
	                       area: '400px',
	                       shadeClose: true,
	                       btn: false,
	                       content: $('#marketing_modal')
	                     });
				} else {
					layer.msg(message);
					return;
				}
			});

        }else{
          layer.msg("操作错误!");
        }
      }

      function save(){
			var id = $('#id').val();
	    	var type = $('#type').val();
	    	var amount = $("#amount").val();
	    	
	    	if (type == null || type == ''){
				layer.msg('充值范围不能为空，请选择充值范围！');
				return;
	    	}
	    	if (amount == null || amount == ''){
				layer.msg('充值金额不能为空，请输入充值金额！');
				return;
	    	}
	    	
	    	var reg=/^[1-9]{1}\d*(\.\d{1,2})?$/;
	    	if (!reg.test(amount)){
				layer.msg('充值金额格式不正确，请重新输入！');
				return;
	    	}
		
			var jsonRequest = $.ajax({
				type: "POST",
				url: "saveOrUpdate",
				data: {id:id, type:type,amount:amount
				},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				if (result=='success') {
					layer.msg("操作成功");
					location.href="list";
				} else{
					layer.msg(message);
					return;
				}
			});
      }
    </script>
  </body>
</html>