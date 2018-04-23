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
              <h3>参数配置</h3>
          </div>
          <div class="main">
            <div class="form_box">
              <div class="device_setting">
                <div class="tip">
                  <h5>说明提示</h5>
                  <p>数据仅供参考，根据当地实际使用环境来调整，<span>环境温度</span>4-40℃，<span>湿度</span><90%, TDS<span>脱盐率</span>≥85%。</p>
                </div>
                <div class="form">
                  <form action="">
                    <div class="fg">
                      <label>环境温度区间</label>
                      <div class="r">
                        <input type="text" class="text w2" required id="temperatureStart" 
                        name="temperatureStart" value="${paramMap['temperature_start'].paramValue }" maxlength="30"><sub>℃</sub>
                        <span>—</span>
                        <input type="text" class="text w2" required id="temperatureEnd" 
                        name="temperatureEnd" value="${paramMap['temperature_end'].paramValue }" maxlength="30"><sub>℃</sub>
                      </div>
                    </div>
                    <div class="fg">
                      <label>最大湿度</label>
                      <div class="r">
                        <input type="text" class="text" required id="maxHumidity" 
                        name="maxHumidity" value="${paramMap['max_humidity'].paramValue }" maxlength="30"><sub>℃</sub>
                      </div>
                    </div>
                    <div class="fg">
                      <label>最小TDS脱盐率</label>
                      <div class="r">
                        <input type="text" class="text" required id="tdsDesalinationRate" 
                        name="tdsDesalinationRate" value="${paramMap['tds_desalination_rate'].paramValue }" maxlength="30"><sub>℃</sub>
                        <p>脱盐率计算：(原水TDS-纯水TDS)/原水TDS x100%</p>
                      </div>
                    </div>
                    <div class="bt">
                      <button type="button" class="btn btn-primary" id="submit_btn">保存</button>
                    </div>
                  </form>
                </div>
              </div>
            </div>
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
        setMenu(3,45);
        
        var ctx = '${ctx}';
        $('#submit_btn').click(function(){
			var temperatureStart = $.trim($('#temperatureStart').val());
			var temperatureEnd = $.trim($('#temperatureEnd').val());
			var maxHumidity = $.trim($("#maxHumidity").val());
			var tdsDesalinationRate = $.trim($("#tdsDesalinationRate").val());
			
			var reg=/^(([1-9][0-9]*)|(([1-9][0-9]*\.\d{1,2})))$/;
			 if(temperatureStart == null || temperatureStart == ''){
		          layer.msg('环境温度区间不能为空!');
		          $('#temperatureStart').focus();
		          return false;
		        }
		        if(temperatureEnd == null || temperatureEnd == ''){
		        	 layer.msg('环境温度区间不能为空!');
			          $('#temperatureEnd').focus();
		          return false;
		        }
		    	if (!reg.test(temperatureStart)){
					layer.msg('环境温度区间格式不正确，必须是数字，保留两位小数！');
					return false;
		    	}
		    	if (!reg.test(temperatureEnd)){
					layer.msg('环境温度区间格式不正确，必须是数字，保留两位小数！');
					return false;
		    	}
		        if(maxHumidity == null || maxHumidity == ''){
		          layer.msg('最大湿度不能为空!');
		          $('#maxHumidity').focus();
		          return false;
		        }
		    	if (!reg.test(maxHumidity)){
					layer.msg('最大湿度格式不正确，必须是数字，保留两位小数！');
					return false;
		    	}
		    	
		        if(tdsDesalinationRate == null || tdsDesalinationRate == ''){
			          layer.msg('最小TDS脱盐率不能为空!');
			          $('#tdsDesalinationRate').focus();
			          return false;
			        }
		        
		    	if (!reg.test(tdsDesalinationRate)){
					layer.msg('最小TDS脱盐率格式不正确，必须是数字，保留两位小数！');
					return false;
		    	}

							
		
			var jsonRequest = $.ajax({
				type: "POST",
				url: "saveSetting",
				data: {temperatureStart:temperatureStart, temperatureEnd:temperatureEnd,maxHumidity:maxHumidity,
					tdsDesalinationRate:tdsDesalinationRate},
				dataType: "json",
				asyn: false
			});
			
			jsonRequest.done(function(data){
				var result = data['result'];
				var message = data['message'];
				if (result=='success') {
			          layer.msg('保存参数配置成功！');
				} else {
					layer.msg(message);
				}
			});
		});
      });
    </script>
  </body>
</html>