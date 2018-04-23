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
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />  
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>民生水宝后台管理系统</title>
    <link href="${ctx }css/font-awesome.min.css" rel="stylesheet">
    <link href="${ctx }plugins/layer/skin/layer.css" rel="stylesheet">
    <link href="${ctx }css/select2.min.css" rel="stylesheet">
    <link href="${ctx }css/style.css" rel="stylesheet">
    <!--[if lt IE 9]>
      <script src="http://cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
      <script src="http://cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    
    <style type="text/css">  
html{height:100%}  
body{height:100%;margin:0px;padding:0px}  
#container{height:100%}  
</style>  
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=DOSCvYNgZwwQKk7PMWdysTWGzncCYCnB">
//v2.0版本的引用方式：src="http://api.map.baidu.com/api?v=2.0&ak=您的密钥"
</script>
  </head>
  <body>
	   <jsp:include page="../common/header.jsp"></jsp:include>
     <div class="wrap">
<jsp:include page="../common/menu.jsp"></jsp:include>
       <div id="main_content">
         <div id="container"></div>
       </div>
     </div>
     
    <script src="${ctx }js/jquery.min.js"></script>
    <script src="${ctx }plugins/layer/layer.js"></script>
    <script src="${ctx }js/select2.min.js"></script>
    <script src="${ctx }js/script.js"></script>
    <script src="${ctx }js/user.js"></script>
    <script>
    $.user_list.user_list();
      $(function(){
        setMenu(3,18);
      });
   
  var map = new BMap.Map("container");
   // 创建地图实例  
   var point = new BMap.Point(116.404, 39.915);
   // 创建点坐标  
   map.centerAndZoom(point, 15);
   // 初始化地图，设置中心点坐标和地图级别  
   map.enableScrollWheelZoom(true);     //开启鼠标滚轮缩放

   var opts = {type: BMAP_NAVIGATION_CONTROL_SMALL};
   map.addControl(new BMap.NavigationControl(opts));    
   map.addControl(new BMap.ScaleControl());    
   map.addControl(new BMap.OverviewMapControl());    
   map.addControl(new BMap.MapTypeControl());    
   map.setCurrentCity("北京"); // 仅当设置城市信息时，MapTypeControl的切换功能才能可用   
   
   //个性化定制：选择模板
   //var mapStyle={style : "midnight"}  
   //map.setMapStyle(mapStyle);
   
   var marker = new BMap.Marker(point);        // 创建标注    
   map.addOverlay(marker);                     // 将标注添加到地图中 
   
/*    var ctx = '${ctx}';
   function addMarker(point, index){  // 创建图标对象   
	    var myIcon = new BMap.Icon(ctx + "images/logo.png", new BMap.Size(23, 25), {    
	        // 指定定位位置。   
	        // 当标注显示在地图上时，其所指向的地理位置距离图标左上    
	        // 角各偏移10像素和25像素。您可以看到在本例中该位置即是   
	        // 图标中央下端的尖角位置。    
	        anchor: new BMap.Size(10, 25),    
	        // 设置图片偏移。   
	        // 当您需要从一幅较大的图片中截取某部分作为标注图标时，您   
	        // 需要指定大图的偏移位置，此做法与css sprites技术类似。    
	        imageOffset: new BMap.Size(0, 0 - index * 25)   // 设置图片偏移    
	    });      
	    // 创建标注对象并添加到地图   
	    var marker = new BMap.Marker(point, {icon: myIcon});    
	    map.addOverlay(marker);    
	}    
	// 随机向地图添加10个标注    
 	var bounds = map.getBounds();    
	var lngSpan = bounds.maxX - bounds.minX;    
	var latSpan = bounds.maxY - bounds.minY;    
	for (var i = 0; i < 10; i ++) {    
	    var point = new BMap.Point(bounds.minX + lngSpan * (Math.random() * 0.7 + 0.15),    
	                                  bounds.minY + latSpan * (Math.random() * 0.7 + 0.15));    
	    addMarker(point, i);    
	}  */

	var opts = {    
		    width : 250,     // 信息窗口宽度    
		    height: 100,     // 信息窗口高度    
		    title : "Hello"  // 信息窗口标题   
		}    
	var infoWindow = new BMap.InfoWindow("World", opts);  // 创建信息窗口对象    
	map.openInfoWindow(infoWindow, map.getCenter());      // 打开信息窗口
	
/* 	var traffic = new BMap.TrafficLayer();        // 创建交通流量图层实例      
	map.addTileLayer(traffic);                    // 将图层添加到地图上 */
	
	//根据daboxId创建自定义图层，用户可用自己创建的geotableid替换30960  
	var customLayer=new BMap.CustomLayer({  
	    geotableId: 30960,   
	    q: '', //检索关键字  
	    tags: '', //空格分隔的多字符串  
	    filter: '' //过滤条件,参考http://developer.baidu.com/map/lbs-geosearch.htm#.search.nearby  
	});
	
	map.addTileLayer(customLayer);//添加自定义图层 
	
	customLayer.addEventListener('onhotspotclick',callback);//单击图层事件  
	function callback(e)//单击热点图层  
	{  
	        var customPoi = e.customPoi,  //获取poi对象  
	        str = [];  
	        str.push("address = " + customPoi.address);  
	        str.push("phoneNumber = " + customPoi.phoneNumber);  
	        var content = '<p style="width:280px;margin:0;line-height:20px;">地址：' + customPoi.address + '<br>电话：' + customPoi.phoneNumber + '</p>';  
	        var searchInfoWindow = new BMapLib.SearchInfoWindow(map, content, {  //带检索的信息窗口  
	            title: customPoi.title, //标题  
	            width: 290, //宽度  
	            height: 40, //高度  
	            panel : "panel", //检索结果面板  
	            enableAutoPan : true, //自动平移  
	            enableSendToPhone: true, //是否显示发送到手机按钮  
	            searchTypes :[  
	                BMAPLIB_TAB_SEARCH,   //周边检索  
	                BMAPLIB_TAB_TO_HERE,  //到这里去  
	                BMAPLIB_TAB_FROM_HERE //从这里出发  
	            ]  
	        });  
	        var point = new BMap.Point(customPoi.point.lng, customPoi.point.lat);  
	        searchInfoWindow.open(point);
	}
    </script>
  </body>
</html>