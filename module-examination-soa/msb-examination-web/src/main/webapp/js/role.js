(function($){
	$.role_list = {
			role_list: function() {
			
			
			$('#submit_btn').click(function(){
				var id = $.trim($('#id').val());
				var name = $.trim($('#name').val());
				
				if (name==null || name=='') { 	 
					layer.msg('权限组名不能为空！');
					return;
				}
				
				if (name.length > 30){
					layer.msg('权限组名不能超过30个字符！');
					return;
				}
				
				var menuIds;
				$('input:checkbox[name=menuIds]:checked').each(function(i){
				       if(0==i){
				    	   menuIds = $(this).val();
				       }else{
				    	   menuIds += (","+$(this).val());
				       }
				});
								
			
				var jsonRequest = $.ajax({
					type: "POST",
					url: "saveOrUpdate",
					data: {id:id, name:name,menuIds:menuIds},
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

function role_delete(id){
	layer.confirm('确认删除该权限组吗?', { 
		btn: ['确认','取消'] //按钮
	}, function(){
		if (id==null || id=='' || id=='0') {
			layer.msg("参数无效，无法删除！", {time: 2000});
			return;
		}

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
			if (result=='success') {
				layer.msg("删除成功!", {icon: 1});
				window.location.reload(true);
			} else {
				layer.msg(message);
				return;
			}
		});
	}, function(){
		layer.msg('已取消！');
	});
}
