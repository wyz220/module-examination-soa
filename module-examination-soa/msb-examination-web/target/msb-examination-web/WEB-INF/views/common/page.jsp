<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div id="pagenation">
							<span>${pageObj.pageNo }/${pageObj.totalPage }，共${pageObj.count }条记录</span>
							<a id="prevPage" onclick="prev()" href="#">上一页</a>
							<a id="nextPage" onclick="next()" href="#">下一页</a>
							<span>跳转至</span>
							<form action="${pageObj.pageUrl }" method="post">
								<input type="text" class="text" name="pageNo" id="pageNo">
								<span>页</span>
								<button type="button" class="btn" onclick="go()">GO</button>
							</form>
						</div>
						
<script>


	function prev() {

		var pageNo = ${pageObj.pageNo};
		if (pageNo > 1){
			pageNo = pageNo - 1;
		}
		var form = $($("form").get(0));
		var pageNoElement = $("<input type='hidden' name='pageNo'/>");
		pageNoElement.attr('value', pageNo);
		form.append(pageNoElement);
		form.submit();
	}
	function next() {

		var pageNo = ${pageObj.pageNo};
		var totalPage = ${pageObj.totalPage};
		if (pageNo < totalPage){
			pageNo = pageNo + 1;
		}
		var form = $($("form").get(0));
		var pageNoElement = $("<input type='hidden' name='pageNo'/>");
		pageNoElement.attr('value', pageNo);
		form.append(pageNoElement);
		form.submit();
	}

	function go() {

		var pageNo = $("#pageNo").val();
		if (isNaN(pageNo)) {
			layer.msg("页码请输入正整数！");
			return;
		}
		if (parseInt(pageNo) <= 0) {
			layer.msg("页码请输入正整数！");
			return;
		}
		var form = $($("form").get(0));

		var pageNoElement = $("<input type='hidden' name='pageNo' />");
		pageNoElement.attr('value', pageNo);
		form.append(pageNoElement);
		form.submit();
	}
</script>