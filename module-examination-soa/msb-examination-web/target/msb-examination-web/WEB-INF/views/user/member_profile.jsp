<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="zh-CN">
<%@include file="../common/taglib.jsp"%>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>民生宝企业管理系统</title>
<link href="${ctx }css/font-awesome.min.css" rel="stylesheet">
<link href="${ctx }plugins/layer/skin/layer.css" rel="stylesheet">
<link rel="${ctx }stylesheet" href="css/foundation-datepicker.css">
<link href="${ctx }plugins/fancybox/jquery.fancybox.css"
	rel="stylesheet">
<link href="${ctx }css/select2.min.css" rel="stylesheet">
<link href="${ctx}css/style.css" rel="stylesheet">
</head>
<body class="bodyhide">
<%-- 	<div class="changeAvatar">
		<form action="">
			<div class="form-group">
				<label>头像图片</label>
				<div class="r">
					<input type="file" name="thumb" id="imageInput1" class="thumb"
						onchange="loadImageFile();" style="margin-top: 5px;">
					<p class="btip">请上传jpg/png格式图片，图片宽高比例1:1,图片大小2M以下</p>
					<div class="pic_view" id="imagePreview1">
						<img src="${ctx }images/avt-9.jpg" class="thumb2" alt="">
					</div>
					<button type="submit" class="btn btn-primary">提交</button>
				</div>
			</div>
		</form>
	</div> --%>
	<div class="wrap">
		<jsp:include page="../common/menu.jsp">
			<jsp:param value="index:*" name="pageType" />
		</jsp:include>
		<div class="page-wrapper">
			<div class="page_main">
				<div class="page_header">
					<div class="top">
						<div class="logo">
							<img src="${ctx}images/logo.png">民生宝企业派单管理系统
						</div>
					</div>
				</div>
				<div class="page_content">
					<div id="scroll_content" class="content">
						<div class="ibox">
							<div class="memberEdit">
								<div class="title">
									<i class="fa fa-edit"></i> 资料修改
								</div>
								<c:if test="${user.auditStatus == 2 }">
									<div class="memeberEdit_tips">
										<div class="cont">
											<h3>审核提示</h3>
											<p>资料未通过审核!请重新填写</p>
										</div>
									</div>
								</c:if>
								<div class="passwordForm">
									<div class="reg_step3 insideStep3">
										<div class="form">
											<form action="modifyMemberProfile" method="post" name="member_profile_Form"
												id="member_profile_Form" enctype="multipart/form-data">
												<input type="hidden" id="id" value="${ user.id}" name="id"/>
												<div class="tit">
													<span>法人信息</span>
												</div>
												<div class="fg">
													<label><span>*</span>公司名称</label> <input type="text"
														class="text" id="companyName" name="companyName"
														maxlength="50" value="${user.companyName }">
												</div>
												<div class="fg">
													<label><span>*</span>法人姓名</label> <input type="text"
														placeholder="需跟营业执照信息一致" value="${user.legalName }"
														class="text" disabled="disabled">
												</div>
												<div class="fg">
													<label><span>*</span>法人性别</label> <select disabled>
														<option value="1"
															<c:if test="${user.legalSex==1 }">selected="selected"</c:if>>男</option>
														<option value="0"
															<c:if test="${user.legalSex==0 }">selected="selected"</c:if>>女</option>
													</select>
												</div>
												<div class="fg">
													<label><span>*</span>法人身份证</label> <input type="text"
														value="${user.legalIdCard }" disabled="disabled"
														class="text">
												</div>
												<div class="fg">
													<label><span>*</span>法人联系电话</label> <input type="text"
														value="${user.legalPhone }" class="text" id="legalPhone"
														name="legalPhone" maxlength="20">
												</div>
												<div class="fg">
													<label><span>*</span>法人常住地址</label> <input type="text"
														value="${user.legalAddress }" class="text"
														id="legalAddress" name="legalAddress" maxlength="150">
												</div>
												<div class="fg">
													<label><span>*</span>法人企业地址</label> <input type="text"
														value="${user.legalEnterpriseAddress }"
														disabled="disabled" class="text">
												</div>
												<div class="tit">
													<span>法人证件上传</span>
												</div>
												<div class="picarea">
													<div class="item">
														<h5>身份证正面</h5>
														<a href="#"> <input type="file" id="imageInput1"
															onchange="loadImageFile1();" name="legalIdCardFrontFile">
															<span id="imagePreview1"> 
															<c:choose>
															   <c:when test="${user.legalIdCardFrontImg != null }">
															   <img src="${user.legalIdCardFrontImg}" alt="">
															   </c:when>
															   <c:otherwise>
															   <img src="${ctx }images/upload_btn1.jpg" alt="">
															   </c:otherwise>
															</c:choose>
														</span>
														</a>
													</div>
													<div class="item">
														<h5>身份证反面</h5>
														<a href="#"> <input type="file" id="imageInput2"
															onchange="loadImageFile2();" name="legalIdCardBackFile">
															<span id="imagePreview2"> 
															<c:choose>
															   <c:when test="${user.legalIdCardBackImg != null }">
															   <img src="${user.legalIdCardBackImg}" alt="">
															   </c:when>
															   <c:otherwise>
															   <img src="${ctx }images/upload_btn1.jpg" alt="">
															   </c:otherwise>
															</c:choose>
														</span>
														</a>
													</div>
												</div>
												<div class="chr"></div>
												<div class="picarea">
													<div class="item">
														<h5>营业执照正面</h5>
														<a href="#"> <input type="file" id="imageInput3"
															onchange="loadImageFile3();"
															name="legalBusLicenseFrontFile"> <span
															id="imagePreview3"> 
														<c:choose>
															   <c:when test="${user.legalBusLicenseFrontImg != null }">
															   <img src="${user.legalBusLicenseFrontImg}" alt="">
															   </c:when>
															   <c:otherwise>
															   <img src="${ctx }images/upload_btn1.jpg" alt="">
															   </c:otherwise>
															</c:choose>
														</span>
														</a>
													</div>
													<div class="item">
														<h5>营业执照反面</h5>
														<a href="#"> <input type="file" id="imageInput4"
															onchange="loadImageFile4();"
															name="legalBusLicenseBackFile"> <span
															id="imagePreview4">
															<c:choose>
															   <c:when test="${user.legalBusLicenseBackImg != null }">
															   <img src="${user.legalBusLicenseBackImg}" alt="">
															   </c:when>
															   <c:otherwise>
															   <img src="${ctx }images/upload_btn1.jpg" alt="">
															   </c:otherwise>
															</c:choose>
														</span>
														</a>
													</div>
												</div>

												<div class="tit">
													<span>常用联系人</span>
												</div>
												<div class="fg">
													<label>联系人姓名</label> <input type="text"
														value="${user.linkmanName }" placeholder="所属公司员工"
														class="text" id="linkmanName" name="linkmanName"
														maxlength="20">
												</div>
												<div class="fg">
													<label>联系人性别</label> <select id="linkmanSex"
														name="linkmanSex">
														<option value="1"
															<c:if test="${user.linkmanSex==1 }">selected="selected"</c:if>>男</option>
														<option value="0"
															<c:if test="${user.linkmanSex==0 }">selected="selected"</c:if>>女</option>
													</select>
												</div>
												<div class="fg">
													<label>联系人电话</label> <input type="text"
														value="${user.linkmanPhone }" class="text"
														id="linkmanPhone" name="linkmanPhone" maxlength="20">
												</div>
												<div class="fg">
													<label>联系人身份证</label> <input type="text"
														value="${user.linkmanIdCard }" class="text"
														id="linkmanIdCard" name="linkmanIdCard" maxlength="20">
												</div>
												<div class="fg">
													<label>所属关系</label> <select id="linkmanRelation"
														name="linkmanRelation">
														<option value="1">下属</option>
													</select>
												</div>
												<div class="chr"></div>
												<div class="picarea">
													<div class="item">
														<h5>身份证正面</h5>
														<a href="#"> <input type="file" id="imageInput5"
															onchange="loadImageFile5();"
															name="linkmanIdCardFrontFile"> <span
															id="imagePreview5"> 
															<c:choose>
															   <c:when test="${user.linkmanIdCardFrontImg != null }">
															   <img src="${user.linkmanIdCardFrontImg}" alt="">
															   </c:when>
															   <c:otherwise>
															   <img src="${ctx }images/upload_btn1.jpg" alt="">
															   </c:otherwise>
															</c:choose>
														</span>
														</a>
													</div>
													<div class="item">
														<h5>身份证反面</h5>
														<a href="#"> <input type="file" id="imageInput6"
															onchange="loadImageFile6();" name="linkmanIdCardBackFile">
															<span id="imagePreview6"> 
															<c:choose>
															   <c:when test="${user.linkmanIdCardBackImg != null }">
															   <img src="${user.linkmanIdCardBackImg}" alt="">
															   </c:when>
															   <c:otherwise>
															   <img src="${ctx }images/upload_btn1.jpg" alt="">
															   </c:otherwise>
															</c:choose>
														</span>
														</a>
													</div>
												</div>
												<div class="btns">
													<button type="submit" class="btn">保存资料</button>
												</div>
											</form>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

	<script src="${ctx }js/jquery.min.js"></script>
	<script src="${ctx }plugins/fancybox/jquery.fancybox.js"></script>
	<script src="${ctx }js/jquery.slimscroll.min.js"></script>
	<script src="${ctx }js/collapse.js"></script>
	<script src="${ctx }js/foundation-datepicker.js"></script>
	<script src="${ctx }plugins/layer/layer.js"></script>
	<script src="${ctx }js/jquery.raty.min.js"></script>
	<script src="${ctx }js/select2.min.js"></script>
	<script src="${ctx }js/common.js"></script>

	<script>
		$(function() {
			$('#member_profile_Form').submit(function() {

				//公司名称
				if ($("#companyName").val() == "") {
					layer.msg('公司名称不能为空！');
					$("#companyName").focus();
					return false;
				}

				//法人姓名
				/*         if ($("#legalName").val() == "") { 
				 layer.msg('法人姓名不能为空！');
				 $("#legalName").focus(); 
				 return false; 
				 }  */

				var legalIdCard = $("#legalIdCard").val();
				if (legalIdCard == "") {
					layer.msg('法人身份证不能为空！');
					$("#legalIdCard").focus();
					return false;
				}

				//法人联系电话
				if ($("#legalPhone").val() == "") {
					layer.msg('法人联系电话不能为空！');
					$("#legalPhone").focus();
					return false;
				}

				if ($("#legalAddress").val() == "") {
					layer.msg('法人常住地址不能为空！');
					$("#legalAddress").focus();
					return false;
				}

				/*         if ($("#legalEnterpriseAddress").val() == "") { 
				 layer.msg('法人企业地址不能为空！');
				 $("#legalEnterpriseAddress").focus(); 
				 return false; 
				 }  */

				/* 	        var imageInput1 = $("#imageInput1").val();
				 alert(imageInput1);
				 return false; */
				return true;
			});
		});
		var loadImageFile1 = (function() {
			if (window.FileReader) {
				var oPreviewImg = null, oFReader = new window.FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

				oFReader.onload = function(oFREvent) {
					if (!oPreviewImg) {
						var newPreview = document
								.getElementById("imagePreview1");
						oPreviewImg = new Image();
						newPreview.innerHTML = '';
						newPreview.appendChild(oPreviewImg);
					}
					oPreviewImg.src = oFREvent.target.result;
				};

				return function() {
					var aFiles = document.getElementById("imageInput1").files;
					if (aFiles.length === 0) {
						return;
					}
					if (!rFilter.test(aFiles[0].type)) {
						alert("You must select a valid image file!");
						return;
					}
					oFReader.readAsDataURL(aFiles[0]);
				}

			}
			if (navigator.appName === "Microsoft Internet Explorer") {
				return function() {
					alert(document.getElementById("imageInput1").value);
					document.getElementById("imagePreview1").filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = document
							.getElementById("imageInput1").value;
				}
			}
		})();
		var loadImageFile2 = (function() {
			if (window.FileReader) {
				var oPreviewImg = null, oFReader = new window.FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

				oFReader.onload = function(oFREvent) {
					if (!oPreviewImg) {
						var newPreview = document
								.getElementById("imagePreview2");
						oPreviewImg = new Image();
						newPreview.innerHTML = '';
						newPreview.appendChild(oPreviewImg);
					}
					oPreviewImg.src = oFREvent.target.result;
				};

				return function() {
					var aFiles = document.getElementById("imageInput2").files;
					if (aFiles.length === 0) {
						return;
					}
					if (!rFilter.test(aFiles[0].type)) {
						alert("You must select a valid image file!");
						return;
					}
					oFReader.readAsDataURL(aFiles[0]);
				}

			}
			if (navigator.appName === "Microsoft Internet Explorer") {
				return function() {
					alert(document.getElementById("imageInput2").value);
					document.getElementById("imagePreview2").filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = document
							.getElementById("imageInput2").value;
				}
			}
		})();
		var loadImageFile3 = (function() {
			if (window.FileReader) {
				var oPreviewImg = null, oFReader = new window.FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

				oFReader.onload = function(oFREvent) {
					if (!oPreviewImg) {
						var newPreview = document
								.getElementById("imagePreview3");
						oPreviewImg = new Image();
						newPreview.innerHTML = '';
						newPreview.appendChild(oPreviewImg);
					}
					oPreviewImg.src = oFREvent.target.result;
				};

				return function() {
					var aFiles = document.getElementById("imageInput3").files;
					if (aFiles.length === 0) {
						return;
					}
					if (!rFilter.test(aFiles[0].type)) {
						alert("You must select a valid image file!");
						return;
					}
					oFReader.readAsDataURL(aFiles[0]);
				}

			}
			if (navigator.appName === "Microsoft Internet Explorer") {
				return function() {
					alert(document.getElementById("imageInput3").value);
					document.getElementById("imagePreview3").filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = document
							.getElementById("imageInput3").value;
				}
			}
		})();
		var loadImageFile4 = (function() {
			if (window.FileReader) {
				var oPreviewImg = null, oFReader = new window.FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

				oFReader.onload = function(oFREvent) {
					if (!oPreviewImg) {
						var newPreview = document
								.getElementById("imagePreview4");
						oPreviewImg = new Image();
						newPreview.innerHTML = '';
						newPreview.appendChild(oPreviewImg);
					}
					oPreviewImg.src = oFREvent.target.result;
				};

				return function() {
					var aFiles = document.getElementById("imageInput4").files;
					if (aFiles.length === 0) {
						return;
					}
					if (!rFilter.test(aFiles[0].type)) {
						alert("You must select a valid image file!");
						return;
					}
					oFReader.readAsDataURL(aFiles[0]);
				}

			}
			if (navigator.appName === "Microsoft Internet Explorer") {
				return function() {
					alert(document.getElementById("imageInput4").value);
					document.getElementById("imagePreview4").filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = document
							.getElementById("imageInput4").value;
				}
			}
		})();
		var loadImageFile5 = (function() {
			if (window.FileReader) {
				var oPreviewImg = null, oFReader = new window.FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

				oFReader.onload = function(oFREvent) {
					if (!oPreviewImg) {
						var newPreview = document
								.getElementById("imagePreview5");
						oPreviewImg = new Image();
						newPreview.innerHTML = '';
						newPreview.appendChild(oPreviewImg);
					}
					oPreviewImg.src = oFREvent.target.result;
				};

				return function() {
					var aFiles = document.getElementById("imageInput5").files;
					if (aFiles.length === 0) {
						return;
					}
					if (!rFilter.test(aFiles[0].type)) {
						alert("You must select a valid image file!");
						return;
					}
					oFReader.readAsDataURL(aFiles[0]);
				}

			}
			if (navigator.appName === "Microsoft Internet Explorer") {
				return function() {
					alert(document.getElementById("imageInput5").value);
					document.getElementById("imagePreview5").filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = document
							.getElementById("imageInput5").value;
				}
			}
		})();
		var loadImageFile6 = (function() {
			if (window.FileReader) {
				var oPreviewImg = null, oFReader = new window.FileReader(), rFilter = /^(?:image\/bmp|image\/cis\-cod|image\/gif|image\/ief|image\/jpeg|image\/jpeg|image\/jpeg|image\/pipeg|image\/png|image\/svg\+xml|image\/tiff|image\/x\-cmu\-raster|image\/x\-cmx|image\/x\-icon|image\/x\-portable\-anymap|image\/x\-portable\-bitmap|image\/x\-portable\-graymap|image\/x\-portable\-pixmap|image\/x\-rgb|image\/x\-xbitmap|image\/x\-xpixmap|image\/x\-xwindowdump)$/i;

				oFReader.onload = function(oFREvent) {
					if (!oPreviewImg) {
						var newPreview = document
								.getElementById("imagePreview6");
						oPreviewImg = new Image();
						newPreview.innerHTML = '';
						newPreview.appendChild(oPreviewImg);
					}
					oPreviewImg.src = oFREvent.target.result;
				};

				return function() {
					var aFiles = document.getElementById("imageInput6").files;
					if (aFiles.length === 0) {
						return;
					}
					if (!rFilter.test(aFiles[0].type)) {
						alert("You must select a valid image file!");
						return;
					}
					oFReader.readAsDataURL(aFiles[0]);
				}

			}
			if (navigator.appName === "Microsoft Internet Explorer") {
				return function() {
					alert(document.getElementById("imageInput6").value);
					document.getElementById("imagePreview6").filters
							.item("DXImageTransform.Microsoft.AlphaImageLoader").src = document
							.getElementById("imageInput6").value;
				}
			}
		})();
	</script>
</body>
</html>