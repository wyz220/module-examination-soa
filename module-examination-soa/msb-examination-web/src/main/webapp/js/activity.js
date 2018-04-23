(function($){
	$.activity_list = {
			activity_list: function() {
			
			
			$('#submit_btn').click(function(){
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
		    	if (packageId == null || packageId == ''){
					layer.msg('充值金额不能为空，请选择充值金额！');
					return;
		    	}
		    	if (giveFee == null || giveFee == ''){
					layer.msg('赠送金额不能为空，请输入赠送金额！');
					return;
		    	}
		    	if (totalNum == null || totalNum == ''){
					layer.msg('活动赠送总金额不能为空，请输入活动赠送总金额！');
					return;
		    	}
		    	
		    	var reg=/^[1-9]{1}\d*(\.\d{1,2})?$/;
		    	if (!reg.test(giveFee)){
					layer.msg('赠送金额格式不正确，请重新输入！');
					return;
		    	}
		    	if (!reg.test(totalNum)){
					layer.msg('活动赠送总金额格式不正确，请重新输入！');
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
					} else {
						layer.msg(message);
						return;
					}
				});
			});

		},
	}
})(jQuery);

