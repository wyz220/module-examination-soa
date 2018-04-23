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
              <h3>设备地图</h3>
          </div>
          <div class="main_filter">
            <div class="form">
              <form action="${ctx }equip/map">
               <div class="fg">
                  <select name="runStatus" id="runStatus">
                    <option value="">运行状态</option>
                     <c:forEach var="map" items="${ runStatusMap }" varStatus="status">
                    <option value="${map.key }" <c:if test="${map.key == param.runStatus }">selected="selected"</c:if> >${map.value }</option>
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
          <div class="map">
            <div id="allmap"></div>
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
    <script src="${ctx }js/script.js"></script>;

    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=GkZ4LbMzGOL7qeHGS1LyK6Xr"></script>

    <script>
      $(function(){
        setMenu(3,18);
        var ctx = '${ctx}';
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
      });
      var map = new BMap.Map("allmap",{  
              minZoom : 9,  
              maxZoom : 20  
          });    // 创建Map实例
      map.centerAndZoom(new BMap.Point(110.344,20.059), 13); // 初始化地图,设置中心点坐标和地图级别
      map.addControl(new BMap.MapTypeControl());   //添加地图类型控件
      map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放
      var opts = {type: BMAP_NAVIGATION_CONTROL_SMALL};
      map.addControl(new BMap.NavigationControl(opts));
      //数据
/*        var data_info = [
        ["110.29793611111111","20.02281111111111","0016","2017/05/05","诚田大厦","开机"],
        ["110.3675","20.046783","0017","2017/07/05","诚田大厦","关机"]
      ];  */
      
      var data_info = ${dataInfo};
      var opts = {
          width : 200,     // 信息窗口宽度
          height: 80,     // 信息窗口高度
          enableMessage:false//设置允许信息窗发送短息
      };
      var aw = new BMap.Icon(ctx + "images/marker.png", new BMap.Size(27, 30), {
          anchor: new BMap.Size(13, 15)
      });
      var ar = new BMap.Icon(ctx + "images/markerover.png", new BMap.Size(29, 33), {
          anchor: new BMap.Size(14, 16)
      });

      setTimeout(doFor,1000);


      function doFor(){
          for(var i=0;i<data_info.length;i++){
              (function () {

                var x;
                var y;
                var num = data_info[i][2];
                var time = data_info[i][3];
                var addr = data_info[i][4];
                var status = data_info[i][5];
                var ggPoint = new BMap.Point(data_info[i][0],data_info[i][1]);
                var convertor = new BMap.Convertor();
                var pointArr = [];
                pointArr.push(ggPoint);
                convertor.translate(pointArr, 1, 5, function(data){
                  if(data.status===0){
                    x = data.points[0].lng;
                    y = data.points[0].lat;
                    var av = new BMap.Marker(new BMap.Point(x,y),{
                    icon: aw,
                    offset: new BMap.Size(3, -12)
                    });  
                    // 创建标注
                    var aD = new BMap.Point(x,y);
                    var ac = {
                            position: aD,
                            offset: new BMap.Size( - 15, 30)
                        };
                    var aC = new BMap.Label('<span>'+num+'</span>', ac);
                    aC.setStyle({
                        color: "#333",
                        fontSize: "12px",
                        height: "20px",
                        lineHeight: "20px",
                        fontFamily: "Microsoft Yahei",
                        borderRadius: "3px",
                        border: "1px solid #f60",
                        padding: "0 5px",
                        background: "#fff",
                        minWidth: "40px",
                        textAlign:"center"
                    });
                    aC.setZIndex(9999);
                    av.setZIndex(0);
                    map.addOverlay(av);               // 将标注添加到地图中
                    var ax = '<dl class="AJ_win"><dd><span>设备ID：</span>'+num+'</dd><dd><span>投放时间：</span>'+time+'</dd><dd><span>小区名：</span>'+addr+'</dd><dd><span>运行状态：</span>'+status+'</dd><dd><a href="detail?equipNo='+num+'" class="r">查看详情</a></dd></dl>';
                    var at = new BMap.InfoWindow(ax, {
                        opts
                    });
                    av.setLabel(aC);
                    at.setWidth(300);
                    av.addEventListener("mouseover",
                    function() {
                        this.setZIndex(50);
                        if (!at.isOpen()) {
                            this.setIcon(ar)
                        }
                    });
                    av.addEventListener("mouseout",
                    function() {
                        this.setZIndex(0);
                        if (!at.isOpen()) {
                            this.setIcon(aw)
                        }
                    });
                    av.addEventListener('click',function(){
                        this.setIcon(aw);
                        this.openInfoWindow(at);
                    });
                      }
                    });
                
                    
              })();
          }
      }


    </script>
  </body>
</html>