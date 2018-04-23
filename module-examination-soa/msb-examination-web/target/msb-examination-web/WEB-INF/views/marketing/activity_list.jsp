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
              <h3>活动列表</h3>
             <div class="pright">
             <shiro:hasPermission name="activity:add">
                <a href="javascript:;" onclick="add_active()" class="btn">创建活动</a>
             </shiro:hasPermission>
              </div>
          </div>
          <div class="main_filter">
            <div class="form">
              <form action="">
                <div class="fg">
                  <select name="searchScope" id="searchScope">
                    <option value="">活动范围</option>
                     <c:forEach var="map" items="${ activityScopeMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.scope }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <select name="status" id="status">
                    <option value="">活动状态</option>
                     <c:forEach var="map" items="${ activityStatusMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.status }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <select name="searchType" id="searchType">
                    <option value="1" <c:if test="${1 == param.type }">selected="selected"</c:if>>活动名称</option>
                    <option value="2" <c:if test="${2 == param.type }">selected="selected"</c:if>>充值金额</option>
                    <option value="3" <c:if test="${3 == param.type }">selected="selected"</c:if>>赠送金额</option>
                  </select>
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="请输入关键词" id="keyword" name="keyword" maxlength="50" value="${param.keyword }">
                </div>
                <button class="btn" type="submit">搜索</button>
              </form>
            </div>
          </div>
          <div class="main">
            <table class="table1">
              <tr>
                <th>活动名称</th>
                <th>活动类型</th>
                <th>活动范围</th>
                <th>充值金额</th>
                <th>赠送金额</th>
                <th>有效期</th>
                <th>限制次数</th>
                <th>已使用次数</th>
                <th>活动状态</th>
                <th>操作</th>
              </tr>
               <c:forEach var="activity" items="${ pageObj.list }" varStatus="status">
              <tr>
                <td> <c:out value="${activity.title }"></c:out> </td>
                <td>
                <c:forEach var="map" items="${ activityTypeMap }" varStatus="status">
                <c:if test="${activity.type == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>
                <c:forEach var="map" items="${ activityScopeMap }" varStatus="status">
                <c:if test="${activity.scope == map.key }">${map.value }</c:if>
                </c:forEach>
                <c:if test="${activity.scope == 0 }">全部</c:if>
                </td>
                <td>${activity.pack.amount }</td>
                <td>${activity.giveFee }</td>
                <td>               
                  <fmt:formatDate value="${ activity.beginTime }" pattern="yyyy/MM/dd" />-<fmt:formatDate value="${ activity.endTime }" pattern="yyyy/MM/dd" />
                  </td>
                <td>
                  <c:choose>
                     <c:when test="${activity.totalNum == 0 }">
                                                                  无限制
                     </c:when>
                     <c:otherwise>
                       ${activity.totalNum }
                     </c:otherwise>
                  </c:choose>
                </td>
                <td>${activity.usedNum }</td>
                <td>
                <c:forEach var="map" items="${ activityStatusMap }" varStatus="status">
                <c:if test="${activity.status == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>
                <shiro:hasPermission name="activity:view">
                <c:if test="${activity.type == 2 or activity.type == 3}">
                 <a href="userList?activityId=${activity.id }" class="btn">查看</a>
                </c:if>
                </shiro:hasPermission>
               
                  <shiro:hasPermission name="activity:edit">
                  <c:if test="${ activity.status != 1}">
              <a href="javascript:;" onclick="edit_active(${activity.id })" class="btn">编辑</a>
                </c:if>
                </shiro:hasPermission>
                  <shiro:hasPermission name="activity:invalid">
                <a href="javascript:;" onclick="zuofei(${activity.id })" class="btn">作废</a>
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
       <form action="saveOrUpdate" class="dFrom">
        <input type="hidden" name="id" value="" id="id">
        <div class="block">
          <div class="fg">
            <label>活动名称：</label>
            <input type="text" class="text" id="title" name="title" maxlength="50">
          </div>
          <div class="fg">
            <label>活动类型：</label>
            <select class="" id="type" name="type">
              <option value="">请选择</option>
               <c:forEach var="map" items="${ activityTypeMap }" varStatus="status">
              <option value="${map.key }">${map.value }</option>
              </c:forEach>
            </select>
          </div>
          <div class="fg">
            <label>活动范围：</label>
            <select class="" id="scope" name="scope">
              <option value="">请选择</option>
              <c:forEach var="map" items="${ activityScopeMap }" varStatus="status">
              <option value="${map.key }">${map.value }</option>
              </c:forEach>
            </select>
          </div>
          <div class="fg">
            <label>活动时间：</label>
              <span id="input_trigger_demo"></span>
              <input type="hidden" name="beginTime" id="beginTime" >
              <input type="hidden" name="endTime" id="endTime">
          </div>
          <div class="fg" id="packageId_div">
            <label>充值金额：</label>
            <select name="packageId" class="" id="packageId">
              <option value="">请选择</option>
                      <c:forEach var="pack" items="${ packageList }" varStatus="status">
                    <option value="${pack.id }" >${pack.amount }</option>
                    </c:forEach>
            </select>
          </div>
          <div class="fg" id="giveFee_div">
            <label>充赠送金额：</label>
            <input type="text" class="text" id="giveFee" name="giveFee" maxlength="19">
          </div>
          <div class="fg">
            <label>限制次数：</label>
            <input type="text" placeholder="选填" class="text" id="totalNum" name="totalNum" maxlength="10">
          </div>
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
        setMenu(4,24);
        var ctx = '${ctx}';
        var dateRange = new pickerDateRange('input_trigger_demo', {
            startToday:true, 
            stopToday:false, 
            isTodayValid:true,
            defaultText : ' ~ ',
            inputTrigger : 'input_trigger_demo',
            theme : 'ta',
            success : function(obj) {
              var start = obj.startDate;//开始时间
              var end = obj.endDate; //结束时间
              $('input[name=beginTime]').val(start);
              $('input[name=endTime]').val(end);
            }
          });
          $("#input_trigger_demo").text('请选择活动起止时间');
          
          //活动范围触发
          $('body').on('change','select[name="scope"]',function(){
        	 
        	var type = this.value;
  			var jsonRequest = $.ajax({
				type: "POST",
				url: ctx + "pack/gets",
				data: {type:type},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				var json = data.data;
				if (result=='success') {
		            var _html = '<option value="">请选择</option>';
		            for(var i=0;i<json.length;i++){
		              _html += '<option value="'+json[i].id+'">'+json[i].amount+'</option>';
		            }
		            $('select[name="packageId"]').html(_html);
				} else {
					layer.msg(message);
					return;
				}
			});

          });
          
          $('#type').change(function(){ 
        	  var type=$(this).children('option:selected').val();
        	  if (type == 3){
        		  $("#packageId_div").hide();
        		  $("#giveFee_div").hide();
        		  
		           var _html = '<option value="0">全部</option>';
		           _html += '<c:forEach var="map" items="${ activityScopeMap }" varStatus="status"><option value="${map.key }">${map.value }</option></c:forEach>';
		           $('select[name="scope"]').html(_html);
		           //$('select[name="scope"]').trigger('change');
        	  } else if (type == 2){
        		  $("#packageId_div").show();
        		  $("#giveFee_div").show();
        		  var _html = '<option value="-1">请选择</option>';
		           _html += '<c:forEach var="map" items="${ activityScopeMap }" varStatus="status"><option value="${map.key }">${map.value }</option></c:forEach>';
		           $('select[name="scope"]').html(_html);
		           $('select[name="scope"]').trigger('change');
        	  }
              else{
        		  $("#packageId_div").show();
        		  $("#giveFee_div").show();

		           var _html = '<option value="">请选择</option>';
		           _html += '<c:forEach var="map" items="${ activityScopeMap }" varStatus="status"><option value="${map.key }">${map.value }</option></c:forEach>';
		           $('select[name="scope"]').html(_html);
        	  }
          });
      });
      function zuofei(id){
        if(id){
         layer.confirm('确认要进行取消活动操作吗？', {
          btn: ['确认','取消'], //按钮
          title:"取消活动"
        }, function(){
        	
			var jsonRequest = $.ajax({
				type: "POST",
				url: "invalid",
				data: {id:id},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				if (result=='success') {
			        layer.msg('作废成功！', {icon: 1});
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
          $('input[name="title"]').val('');
          $('select[name="type"]').val('');
          $('select[name="scope"]').val('');
          $('input[name="beginTime"]').val('');
          $('input[name="endTime"]').val('');
          $('select[name="packageId"]').html('');
          $('input[name="giveFee"]').val('');
          $('input[name="totalNum"]').val('');
          $('#input_trigger_demo').text('请选择活动起止时间');
          layer.open({
            type: 1,
            title: "创建活动",
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
					   var type = json.type;
	                   $('input[name="id"]').val(json.id);
	                   $('input[name="title"]').val(json.title);
	                   $('select[name="type"]').val(json.type);
	                  
	                   $('input[name="beginTime"]').val(json.beginTimeStr);
	                   $('input[name="endTime"]').val(json.endTimeStr);
	                   
	       	        	  if (type == 3){
	       	        		  $("#packageId_div").hide();
	       	        		  $("#giveFee_div").hide();
	       	        		  
	       			           var _html = '<option value="0">全部</option>';
	       			           _html += '<c:forEach var="map" items="${ activityScopeMap }" varStatus="status"><option value="${map.key }">${map.value }</option></c:forEach>';
	       			           $('select[name="scope"]').html(_html);
	       			        $('select[name="scope"]').val(json.scope);
	       			           //$('select[name="scope"]').trigger('change');
	       	        	  } else if (type == 2){
	       	        		  $("#packageId_div").show();
	       	        		  $("#giveFee_div").show();
	       	        		 var _html = '<option value="">请选择</option>';
	       			           _html += '<c:forEach var="map" items="${ activityScopeMap }" varStatus="status"><option value="${map.key }">${map.value }</option></c:forEach>';
	       			           $('select[name="scope"]').html(_html);
	       			        $('select[name="scope"]').val(json.scope);
	       			           //$('select[name="scope"]').trigger('change');
	       	        	  }
	       	              else{
	       	        		  $("#packageId_div").show();
	       	        		  $("#giveFee_div").show();

	       			           var _html = '<option value="">请选择</option>';
	       			           _html += '<c:forEach var="map" items="${ activityScopeMap }" varStatus="status"><option value="${map.key }">${map.value }</option></c:forEach>';
	       			           $('select[name="scope"]').html(_html);
	       			        $('select[name="scope"]').val(json.scope);
	       	        	  }
	       	        	  
	                   var jsonRequest = $.ajax({
	       				type: "POST",
	       				url: ctx + "pack/gets",
	       				data: {type:json.scope},
	       				dataType: "json",
	       				asyn: false
	       			});
	       			
	       			jsonRequest.done(function(data){
	       				var result = data['result'];
	       				var message = data['message'];
	       				var pack = data.data;
	       				if (result=='success') {
	       		            var _html = '<option value="">请选择</option>';
	       		            for(var i=0;i<pack.length;i++){
	       		               if (pack[i].id == json.packageId){
	       		            	_html += '<option value="'+pack[i].id + '" selected="selected">'+pack[i].amount+'</option>'; 
	       		               }else{
	       		            	_html += '<option value="'+pack[i].id+'">'+pack[i].amount+'</option>';
	       		               }
	       		            	
	       		            }
	       		            $('select[name="packageId"]').html(_html); 
	       		            
	       				} else {
	       					layer.msg(message);
	       					return;
	       				}
	       			});
	                   //$('select[name="packageId"]').val(json.packageId);
	                   $('input[name="giveFee"]').val(json.giveFee);
	                   $('input[name="totalNum"]').val(json.totalNum == 0 ? null : json.totalNum);
	                   $('#input_trigger_demo').text(json.beginTimeStr+'~'+json.endTimeStr);
	                   layer.open({
	                     type: 1,
	                     title: "创建活动",
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
	    	var title = $.trim($('#title').val());
	    	var type = $("#type").val();
	    	var scope = $("#scope").val();
	    	var beginTime = $("#beginTime").val();
	    	var endTime = $("#endTime").val();
	    	var packageId = $('#packageId').val();
	    	var giveFee = $.trim($('#giveFee').val());
	    	var totalNum = $.trim($('#totalNum').val());
	    	
	    	if (title == null || title == ''){
				layer.msg('活动名称不能为空，请输入活动名称！');
				return;
	    	}
	    	if (type == null || type == ''){
				layer.msg('活动类型不能为空，请选择活动类型！');
				return;
	    	}
	    	if (scope == null || scope == ''){
				layer.msg('活动范围不能为空，请选择活动范围！');
				return;
	    	}
	    	if (beginTime == null || beginTime == ''){
				layer.msg('活动开始时间不能为空，请选择活动开始时间！');
				return;
	    	}
	    	if (endTime == null || endTime == ''){
				layer.msg('活动结束时间不能为空，请选择活动结束时间！');
				return;
	    	}
	    	
	    	if (type != 3){
		    	if (packageId == null || packageId == ''){
					layer.msg('充值金额不能为空，请选择充值金额！');
					return;
		    	}
		    	if (giveFee == null || giveFee == ''){
					layer.msg('赠送金额不能为空，请输入赠送金额！');
					return;
		    	}
		    	
		    	var reg=/^[1-9]{1}\d*(\.\d{1,2})?$/;
		    	if (!reg.test(giveFee)){
					layer.msg('赠送金额格式不正确，请重新输入！');
					return;
		    	}
	    	}

	    	if (isNaN(totalNum)){
				layer.msg('限制次数格式不正确，请重新输入！');
				return;
	    	}
		
			var jsonRequest = $.ajax({
				type: "POST",
				url: "saveOrUpdate",
				data: {id:id, title:title,type:type,scope:scope,beginTime:beginTime,endTime:endTime,
					packageId:packageId,giveFee:giveFee,totalNum:totalNum
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