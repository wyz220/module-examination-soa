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
              <h3>设备列表</h3>
              <div class="pright">
              <shiro:hasPermission name="equip:export">
                <a href="javascript:;" class="btn" id="export_btn">导出Excel</a>
               </shiro:hasPermission>
              </div>
          </div>
          <div class="main_filter">
            <div class="form">
              <form action="${ctx }equip/list">
                <div class="fg">
                  <select name="runStatus" id="runStatus">
                    <option value="">运行状态</option>
                     <c:forEach var="map" items="${ runStatusMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.runStatus }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <select name="paramStatus" id="paramStatus">
                    <option value="">参数状态</option>
                     <c:forEach var="map" items="${ paramStatusMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.paramStatus }">selected="selected"</c:if> >${map.value }</option>
                    </c:forEach>
                  </select>
                </div>
                <div class="fg">
                  <span id="input_trigger_demo"></span>
                  <input type="hidden" id="beginTime" name="beginTime" value="${param.beginTime }">
                  <input type="hidden" id="endTime" name="endTime" value="${param.endTime }">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="地址" id="address" name="address" maxlength="200" value="${param.address }">
                </div>
                <div class="fg">
                  <input type="text" class="text" placeholder="设备ID" id="equipmentNo" name="equipmentNo" maxlength="10" value="${param.equipmentNo }">
                </div>
                <button class="btn" type="submit">搜索</button>
              </form>
            </div>
          </div>
          <div class="main">
            <table class="table1">
              <tr>
                <th>设备ID</th>
                <th>型号</th>
                <th>投放时间</th>
                <th>地址</th>
                <th>投放小区</th>
                <th>销售额(元)</th>
                <th>售水量(L)</th>
                <th>充值金额</th>
                <th>上次维护距今(天)</th>
                <th>参数状态</th>
                <th>开机状态</th>
                <th>流速调节(5L)</th>
                <th>操作</th>
              </tr>
               <c:forEach var="equip" items="${ pageObj.list }" varStatus="status">
               <tr>
                <td>${equip.equipmentNo }</td>
                <td>${equip.modelNumber }</td>
                <td>
                <c:if test="${equip.putTime != null }">
                  <fmt:formatDate value="${ equip.putTime }" pattern="yyyy-MM-dd HH:mm:ss" />
                </c:if>
                </td>
                <td title="${equip.address }">${equip.address }</td>
                <td>${equip.community.name }</td>
                <td>${equip.saleAmount }</td>
                <td>${equip.saleWaterQuantity }L</td>
                <td>${equip.icRechargeAmount }</td>
                <td>
                <c:choose>
                  <c:when test="${equip.lastMaintainTime != null }">
                  ${equip.days }
                  </c:when>
                  <c:otherwise>
                                                      无维护记录
                  </c:otherwise>
                </c:choose>
                </td>
                <td>
                 <c:forEach var="map" items="${ paramStatusMap }" varStatus="status">
                <c:if test="${equip.paramStatus == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>
                <c:forEach var="map" items="${ runStatusMap }" varStatus="status">
                <c:if test="${equip.runStatus == map.key }">${map.value }</c:if>
                </c:forEach>
                </td>
                <td>${equip.outWaterTime }秒</td>
                <td>
                <shiro:hasPermission name="equip:maintain">
                <a href="javascript:;" class="btn" onclick="weihu(${equip.id })">维护</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="equip:view">
                <a href="detail?equipNo=${equip.equipmentNo }"  class="btn">查看</a>
                </shiro:hasPermission>
                <shiro:hasPermission name="equip:adjust">
                <a href="javascript:;" class="btn" onclick="setfloat('${equip.equipmentNo }',${equip.outWaterTime },${equip.preDeductAmount })">设置</a>
                </shiro:hasPermission>
                </td>
              </tr>
               </c:forEach>
            </table>
             <jsp:include page="../common/page.jsp"></jsp:include>
          </div>
       </div>
     </div>
     
      <div id="device_edit">
        <div class="modal_1">
          <form action="adjust" id="device_form" method="post" >
          <input type="hidden" id="equipNo" name="equipNo" />
            <div class="fg">
              <label>设备ID</label>
              <span id="device_id"></span>
            </div>
            <div class="fg">
              <label>流速调节(5L)</label>
              <input type="text" name="outWaterTime" id="outWaterTime" class="text" placeholder="请输入秒数" value="" style="width:60px;min-width:60px;margin-right:10px;"> <span> 秒</span> 
            </div>
            <div class="fg">
              <label>预扣金额</label>
              <input type="text" name="preDeductAmount" id="preDeductAmount" class="text" placeholder="请输入预扣金额" value="" style="width:60px;min-width:60px;margin-right:10px;"> <span> 元</span> 
            </div>
            <div class="bt">
              <button type="button" onclick="save_device()">保存</button>
            </div>
          </form>
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
    <script src="${ctx }js/jquery.form.min.js"></script>
    <script>
      $(function(){
        setMenu(3,17);
        var defaultText = '请选择投放时间';
        var beginTime = '${param.beginTime }';
        var endTime = '${param.endTime }';
        if (beginTime != null && beginTime != ''){
        	defaultText = beginTime;
        }
        if (endTime != null && endTime != ''){
        	defaultText = defaultText + ' ~ ' + endTime;
        }
        var dateRange = new pickerDateRange('input_trigger_demo', {
            isTodayValid : true,
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
          $("#input_trigger_demo").text(defaultText);
          
          
      	$('#export_btn').click(function(){
			var runStatus = $('#runStatus').val();
			var paramStatus = $('#paramStatus').val();
			var beginTime = $("#beginTime").val();
			var endTime = $("#endTime").val();
			var address = $('#address').val();
			var equipmentNo = $('#equipmentNo').val();
		
			var url="export?1=1";
			if (runStatus!=null && runStatus!="") {
				url += "&runStatus=" + runStatus;
			}
			if (paramStatus!=null && paramStatus!="") {
				url += "&paramStatus=" + paramStatus;
			}
			if (beginTime!=null && beginTime!="") {
				url += "&beginTime=" + beginTime;
			}
			if (endTime!=null && endTime!="") {
				url += "&endTime=" + endTime;
			}
			if (address!=null && address!="") {
				url += "&address=" + address;
			}
			if (equipmentNo!=null && equipmentNo!="") {
				url += "&equipmentNo=" + equipmentNo;
			}
			window.location.href = url;				
		});
      });
      function weihu(id){
        if(id){
        layer.confirm('确认已经完成更换滤芯等操作了吗？', {
          btn: ['确认维护'] ,title:'维护提示'
        }, function(){
        	var jsonRequest = $.ajax({
    			type: "POST",
    			url: "maintain",
    			data: {id:id},
    			dataType: "json",
    			asyn: false
    		});
    		
    		jsonRequest.done(function(data){
    			var result = data['result'];
    			var message = data['message'];
    			if (result=='success') {
    			       layer.msg('操作成功！', {icon: 1},function(){
    			            location.reload();
    			          });
    			} else {
    				layer.msg(message);
    				return;
    			}
    		});
   
        });
        }
      }
      
      function setfloat(equipmentNo,outWaterTime,preDeductAmount){
          if(equipmentNo&&outWaterTime){
            $('#device_id').text(equipmentNo);
            $('#outWaterTime').val(outWaterTime);
            $("#equipNo").val(equipmentNo);
            $("#preDeductAmount").val(preDeductAmount);
            layer.open({
              type: 1,
              title: "设置流速调节(5L)",
              closeBtn: 1,
              area: '250px',
              skin: 'layui-layer-nobg', //没有背景色
              shadeClose: true,
              content: $('#device_edit')
            });
          }
      }

      function save_device(){
    	var outWaterTime = $('#outWaterTime').val();
    	var preDeductAmount = $('#preDeductAmount').val();
    	if (outWaterTime == null || outWaterTime == ''){
    		layer.msg("请输入流速！");
    		return;
    	}
    	if (isNaN(outWaterTime)){
    		layer.msg("流速必须为数字！");
    		return;
    	}
    	if (preDeductAmount == null || preDeductAmount == ''){
    		layer.msg("请输入预扣金额！");
    		return;
    	}
    	if (isNaN(preDeductAmount)){
    		layer.msg("预扣金额格式不正确，请重新输入！");
    		return;
    	}
        var index = layer.load(1);
        $("#device_form").ajaxSubmit({  
            url : "adjust",  
            type : "post",  
            dataType : 'json',  
            success : function(data) {  
    			var result = data['result'];
    			var message = data['message'];
    			if (result=='success') {
    			       layer.msg('操作成功！', {icon: 1},function(){
    			            location.reload();
    			          });
    			} else {
    				layer.msg(message);
    				layer.close(index);
    				return;
    			}
            },  
            error : function(data) {  
                alert("error:" + data);  
            }  
        });  
      }

    </script>
  </body>
</html>