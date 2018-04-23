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
       <div id="main_content" class="bg1">
          <div class="main_title">
              <h3>设备详情</h3>
          </div>
          <div class="main">
            <div id="index_box" class="device_detail">
              <div class="jbxx">
                <div class="title">基本信息
                 <shiro:hasPermission name="equip:edit">
                <a href="javascript:;" onclick="open_edit()" class="btn">编辑/上传</a>
                </shiro:hasPermission>
                </div>
                <div class="cont">
                  <div class="linfo">
                    <div class="slider">
                    <c:choose>
                      <c:when test="${imgList != null and imgList != '[]'}">
                          <c:forEach var="img" items="${ imgList }" varStatus="status">
                               <a href="${img.imgUrl}" class="fancybox">
                                 <img src="${img.imgUrl}" alt="">
                               </a>
                          </c:forEach>
                      </c:when>
                      <c:otherwise>
                     <a href="${ctx}images/pic.png" class="fancybox">
                        <img src="${ctx }images/pic.png" alt="">
                      </a>
                      </c:otherwise>
                    </c:choose>
                    </div>
                    <div class="txt">点击查看大图</div>
                  </div>
                  <div class="rinfo">
                    <ul>
                      <li>
                        <span>设备型号</span>
                        <b>${equip.modelNumber }</b>
                      </li>
                      <li>
                        <span>设备ID</span>
                        <b>${equip.equipmentNo }</b>
                      </li>
                      <li>
                        <span>投放时间</span>
                        <b>
                       <c:if test="${equip.putTime != null }">
                  <fmt:formatDate value="${ equip.putTime }" pattern="yyyy-MM-dd HH:mm:ss" />
                </c:if>
                        </b>
                      </li>
                      <li>
                        <span>运行状态</span>
                        <b>
                  <c:forEach var="map" items="${ runStatusMap }" varStatus="status">
                <c:if test="${equip.runStatus == map.key }">${map.value }</c:if>
                </c:forEach>
                        </b>
                        <div class="bts">
                <shiro:hasPermission name="equip:oper">
                 <c:forEach var="map" items="${ runStatusMap }" varStatus="status">
                <c:if test="${equip.runStatus != map.key }"> <a href="javascript:;" onclick="kaiji(${equip.id},${map.key },'${map.value }')">${map.value }</a></c:if>
                </c:forEach>
                </shiro:hasPermission>
                          <shiro:hasPermission name="equip:maintain">
                          <a href="javascript:;" onclick="weihu(${equip.id })">维护</a>
                          </shiro:hasPermission>
                        </div>
                      </li>
                    </ul>
                    <div class="bt">
                      <div class="l">
                        <h3><span>上次维护记录</span><a href="maintainList?equipmentNo=${equip.equipmentNo }">查看全部 <i class="fa fa-angle-right"></i></a></h3>
                        <c:if test="${equip.lastMaintainTime != null }">
                        <h3>更换滤芯</h3>
                        <p><fmt:formatDate value="${ equip.lastMaintainTime }" pattern="yyyy-MM-dd HH:mm:ss" /></p>
                        </c:if>
                      </div>
                      <div class="r">
                        <p>投放小区：<c:out value="${equip.community.name }"></c:out> </p>
                        <p>地址：<c:out value="${equip.community.address }"></c:out> </p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <div class="gzzt">
                <div class="title">工作状态</div>
                <div class="cont">
                  <ul class="ul1">
                    <li>
                      <span>总销售额</span>
                      <b>${equip.saleAmount }</b>
                    </li>
                    <li>
                      <span>累计IC卡充值金额</span>
                      <b>${equip.icRechargeAmount }</b>
                    </li>
                    <li>
                      <span>售水量（升）</span>
                      <b>${equip.produceWaterQuantity }</b>
                    </li>
                    <li>
                      <span>制水量（升）</span>
                      <b>${equip.saleWaterQuantity }</b>
                    </li>
                  </ul>
                  <ul class="ul2">
                    <li>
                      <span>水位：
                      <c:if test="${equip.waterLevel != null }">
                  <c:forEach var="map" items="${ waterLevelMap }" varStatus="status">
                <c:if test="${equip.waterLevel == map.key }">${map.value }</c:if>
                </c:forEach>    
                      </c:if>
                      </span>
                    </li>
                    <li>
                                          <span>售水状态：
                        <c:if test="${equip.workStatus != null }">
                          <c:forEach var="map" items="${ workStatusMap }" varStatus="status">
                            <c:if test="${equip.workStatus == map.key }">${map.value }</c:if>
                          </c:forEach>  
                        </c:if>
                      </span>
                    </li>
                    <li>
                                          <span>制水状态：
                        <c:if test="${equip.produceStatus != null }">
                          <c:forEach var="map" items="${ produceStatusMap }" varStatus="status">
                            <c:if test="${equip.produceStatus == map.key }">${map.value }</c:if>
                          </c:forEach>  
                        </c:if>
                      </span>
                    </li>
                    <li><span>温度： <c:if test="${equip.temperature != null and equip.temperature != '' }">${equip.temperature}℃ </c:if> </span></li>
                    <li>
                      <span>湿度：<c:if test="${equip.humidity != null and equip.humidity != '' }">${equip.humidity}℃ </c:if></span>
                    </li>
                    <li>
                                          <span>臭氧工作时间：
                       <c:if test="${equip.ozoneWorkingTime != null}">
                        ${equip.ozoneWorkingTime}
                        </c:if>
                      </span>
                    </li>
                     <li>TDS脱盐率：${tdsDesalinationRate }%</li>
                    <li>
                      <span>原水TDS：
                        <c:if test="${equip.rawWaterTds != null and equip.rawWaterTds != '' }">
                        ${equip.rawWaterTds}
                        </c:if>
                      </span>

                    </li>
                    <li>
                      <span>纯水TDS：
                       <c:if test="${equip.purifyWaterTds != null and equip.purifyWaterTds != '' }">
                        ${equip.purifyWaterTds}
                        </c:if>
                      </span>

                    </li>
                    <li>
                     <span>原水状态：
                    <c:if test="${equip.rawWaterStatus != null }">
                  <c:forEach var="map" items="${ rawWaterStatusMap }" varStatus="status">
                <c:if test="${equip.rawWaterStatus == map.key }">${map.value }</c:if>
                </c:forEach>    
                      </c:if>
                      </span>
                   </li>
                  </ul>
                </div>
              </div>
              <div class="box2">
                <div class="content">
                   <div class="top">
                     <div class="tit">销售数据概览</div>
                     <ul>
                       <li>
                         <span>今日订单量</span>
                         <b>
                         <c:choose>
                           <c:when test="${dayStat.saleOrder == null}">
                             0
                           </c:when>
                           <c:otherwise>${dayStat.saleOrder }</c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                       <li>
                         <span>今日销售额</span>
                         <b>
                           <c:choose>
                           <c:when test="${dayStat.saleAmount == null}">
                             0.00
                           </c:when>
                           <c:otherwise>${dayStat.saleAmount }</c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                       <li>
                         <span>本月订单量</span>
                         <b>
                          <c:choose>
                           <c:when test="${dayStat.saleOrder == null}">
                             0
                           </c:when>
                           <c:otherwise>${dayStat.saleOrder }</c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                       <li>
                         <span>本月销售额</span>
                         <b>
                          <c:choose>
                           <c:when test="${dayStat.saleAmount == null}">
                             0.00
                           </c:when>
                           <c:otherwise>${dayStat.saleAmount }</c:otherwise>
                         </c:choose>
                         </b>
                       </li>
                       <li>
                         <span>下单用户数</span>
                         <b>${saleUserCount}</b>
                       </li>
                     </ul>
                   </div>
                   <div class="bottom">
                     <div class="time_tools">
                       <span class="text">时间：</span>
                       <div class="time_items">
                         <a href="javascript:;" data-type="week">本周</a>
                         <a href="javascript:;" data-type="month" class="active">最近1月</a>
                         <a href="javascript:;" data-type="year">最近一年</a>
                       </div>
                       <div class="ta_date" id="div_date_demo3">
                        <span class="date_title" id="date_demo3">点击选择时间段</span>
                        <a class="opt_sel" id="input_trigger_demo3" href="#">
                            <i class="i_orderd"></i>
                        </a>
                       </div>
                     </div>
                     <div id="echart_orders"></div>
                   </div>
                 </div>
              </div>
            </div>
          </div>
       </div>
     </div>

     <div id="device_edit">
        <div class="modal_1">
          <form action="saveOrUpdate" id="device_form" method="post" enctype="multipart/form-data">
          <input type="hidden" id="id" value="${ equip.id}" name="id"/>
            <ul class="upload_pic">
             <c:forEach var="img" items="${ tempImgList }" varStatus="status">
               <li>
                 <c:choose>
                    <c:when test="${img.imgUrl != null }">
                                     <a href="javascript:;">
                  <input type="file" name="files" value="${img.imgUrl}" <c:if test="${status.count==1 }">id="getLatAndLonPic"</c:if> >
                  <img src="${img.imgUrl}" alt="" style="display:block;">
                  <button type="button" style="display:block;" >删除</button>
                </a>
                    </c:when>
                    <c:otherwise>
                <a href="javascript:;">
                  <input type="file" name="files" value="" <c:if test="${status.count==1 }">id="getLatAndLonPic"</c:if>>
                   <img src="" alt="" style="display:none;">
                  <button type="button" style="display:none;" >删除</button>
                </a>
                    </c:otherwise>
                 </c:choose>

               </li>
              </c:forEach>
            </ul>
            <div class="fg">
              <label>设备型号</label>
              <select name="modelNumber" id="modelNumber" class="select">
                <c:forEach var="model" items="${ modelList }" varStatus="status">
                <option value="${model.modelNumber }" <c:if test="${model.modelNumber == equip.modelNumber}">selected="selected"</c:if>>${model.modelNumber }</option>
                </c:forEach>
              </select>
                <small> 说明：现阶段默认一种型号，不能选</small>
            </div>
            <div class="fg">
              <label>定位信息</label>
              <input type="text" class="text" name="address" id="address" placeholder="请输入详细地址" value="${equip.address }" maxlength="200">
            </div>
            <div class="fg">
              <label>投放小区</label>
              <input type="text" class="text" name="communityName" id="communityName" placeholder="请输入小区全名" value="${equip.community.name }" maxlength="100">
            </div>
            <div class="bt">
              <button type="button" onclick="save_device()">保存</button>
            </div>
          </form>
        </div>
     </div>
      <div id="l-map"></div>
     
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
    <script src="${ctx }js/script.js"></script>
    <script src="${ctx }js/exif.min.js"></script>
    <script src="${ctx }js/jquery.form.min.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=GkZ4LbMzGOL7qeHGS1LyK6Xr"></script>
	<script>
      $(function(){
        setMenu(3,17);
        var ctx = '${ctx}';
        var dateRange = new pickerDateRange('date_demo3', {
          isTodayValid : false,
          defaultText : ' ~ ',
          inputTrigger : 'input_trigger_demo3',
          theme : 'ta',
          success : function(obj) {
            $('.time_items a').removeClass('active');
            var start = obj.startDate;//开始时间
            var end = obj.endDate; //结束时间
            if(getTime2Time(end,start)>31){
                layer.msg("选择时间范围不能超过31天");
              }else{
            	  set_orderChart(5,start,end);
              }
          }
        });
        //默认调用最近1月数据
        set_orderChart(2,null,null);
        $('.time_items a').click(function(){
          if($(this).hasClass('active')) return;
          $(this).addClass('active').siblings().removeClass('active');
          var type = $(this).data('type');
          switch(type){
              case 'week':
            	  set_orderChart(3,null,null);
                 break;
              case 'month':
            	  set_orderChart(2,null,null);
                 break;
              case 'year':
            	  set_orderChart(1,null,null);
              default:
                break;
          }
        });

        $('#main_content').resize(function(){
          echart_orders.resize();
        });

        $('.slider').slick({
          arrows: false,
          dots: true
        });

        //图片添加
        $('body').on('change', '.upload_pic input', function() {
          var $file = $(this);
          var fileObj = $file[0];
          var windowURL = window.URL || window.webkitURL;
          var dataURL;
          var $img = $(this).siblings('img');
          var $button = $(this).siblings('button');
          if (fileObj && fileObj.files && fileObj.files[0]) {
            dataURL = windowURL.createObjectURL(fileObj.files[0]);
            $img.attr('src', dataURL);
            $img.show();
            $button.show();
          
          } else {
              dataURL = $file.val();
              var imgObj = $(this).siblings('img');
              $img.show();
              $button.show();
              // 两个坑:
              // 1、在设置filter属性时，元素必须已经存在在DOM树中，动态创建的Node，也需要在设置属性前加入到DOM中，先设置属性在加入，无效；
              // 2、src属性需要像下面的方式添加，上面的两种方式添加，无效；
              imgObj.style.filter = "progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)";
              imgObj.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = dataURL;
          
          }
        });
      
        $('.upload_pic button').click(function(){
          $(this).hide();
          $(this).siblings('img').attr('src','').hide();
          var file = $(this).siblings('input');
          file.after(file.clone().val(""));   
          file.remove();   
        });
        

        //第一张图获取经纬度
        var passFileType = /^(?:image\/bmp|image\/gif|image\/jpeg|image\/png)$/i;
        $('body').on('change',"#getLatAndLonPic", function(e){
          if (!e.target || !e.target.files.length || !e.target.files[0]) {return};
          var _file = e.target.files[0];
          if (!passFileType.test(_file.type)) {return};
          EXIF.getData(_file, function(){
            var Long = EXIF.getTag(_file, 'GPSLongitude');
            var lat = EXIF.getTag(_file, 'GPSLatitude');
            if(Long&&Long.length==3&&lat&&lat.length==3){
              var x = ChangeToDu(Long);
              var y = ChangeToDu(lat);
              var ggPoint = new BMap.Point(x,y);
              var convertor = new BMap.Convertor();
              var pointArr = [];
              pointArr.push(ggPoint);
              var geoc = new BMap.Geocoder(); 
              convertor.translate(pointArr, 1, 5, function(data){
                  if(data.status === 0) {
                      geoc.getLocation(new BMap.Point(data.points[0].lng,data.points[0].lat), function(rs){
                        $("#address").val(rs.address);
                      });
                    }
              });
            }else{
              // layer.msg("请上传带有坐标的图片!");
              // file = $(e.target);
              // file.siblings('img').attr('src','').hide();
              // file.siblings('button').hide();
              // file.after(file.clone().val(""));   
              // file.remove();   
              return;
            }
          });
        });
        
        var map = new BMap.Map("l-map");
        map.centerAndZoom("海口",12);  
        var ac = new BMap.Autocomplete(    //建立一个自动完成的对象
          {"input" : "address"
          ,"location" : map
        });
        ac.setInputValue($("#address").val());   
      
      });
  
      function save_device(){
    	  var index = layer.load(2)
          $("#device_form").ajaxSubmit({  
              url : "saveOrUpdate",  
              type : "post",  
              dataType : 'json',  
              success : function(data) {  
            	  var result = data['result'];
        			var message = data['message'];
        			layer.close(index); 
        			if (result=='success') {
        			       layer.msg('操作成功！', {icon: 1},function(){
        			            location.reload();
        			         });
        			} else {
        				layer.msg(message);
        				return;
        			}
              },  
              error : function(data) {  
                  alert("error:" + data);  
              }  
          });  
        }
      
    function ChangeToDu(arr){  
      var f = parseFloat(arr[1]) + parseFloat(arr[2]/60);
      var du = parseFloat(f/60) + parseFloat(arr[0]);
      return du;
    }

      function open_edit(){
        layer.open({
          type: 1,
          title: "编辑/上传",
          closeBtn: 1,
          area: '700px',
          skin: 'layui-layer-nobg', //没有背景色
          shadeClose: true,
          content: $('#device_edit')
        });
      }

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


      function kaiji(id,type,text){
        if(id){
        var tip = '确定' + text + '吗？';
        layer.confirm(tip, {
          btn: ['确认'] ,title:text
        }, function(){
        	
        	var jsonRequest = $.ajax({
    			type: "POST",
    			url: "operator",
    			data: {id:id,type:type},
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

      
      //订单图表
      var echart_orders = echarts.init(document.getElementById('echart_orders'));
      function set_orderChart(type, beginDate, endDate){
    	var equipmentNo = '${equip.equipmentNo}';
        $.ajax({
          type: 'get',
          url: ctx + 'report/orderCountStat',
          data:{equipmentNo:equipmentNo,type:type,beginDate:beginDate,endDate:endDate},
          success: function(result){
        	 var res = result.data;
        	 var values = null;
       	  if (res.rows != null){
    		  values = res.rows[0].values;
    	  }
             option = {
              title: {
                  text: res.title,
                  textStyle: {
                    fontSize: 14,
                    fontWeight: 'normal',
                    color: '#777'
                  }   
              },
              grid: {
                  left: '2%',
                  right: '2%',
                  bottom: '3%',
                  containLabel: true
              },
              tooltip : {
                  trigger: 'axis'
              },
              xAxis: {
                  type : 'category',
                  boundaryGap : false,
                  data: res.series
              },
              yAxis : [
                  {
                      type : 'value'
                  }
              ],
              series: [{
                  color: ['#0c97fb'],
                  name:'订单数',
                  type:'line',    
                  stack: '总量',
                  data: _.flatten(values)
              }]
            };
            echart_orders.resize();
            echart_orders.setOption(option);
          }
        });
      }
    </script>
</body>
</html>