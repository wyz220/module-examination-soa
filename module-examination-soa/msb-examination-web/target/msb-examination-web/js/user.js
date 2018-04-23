(function($){
	$.user_list = {
		user_list: function() {
			
			
			$('#submit_btn').click(function(){
				var id = $('#id').val();
				var username = $.trim($('#username').val());
				var roleId = $("#roleId").val();
				var password = $.trim($("#password").val());
				var confirmPassword = $.trim($("#confirmPassword").val());
				
				if (username==null || username=='') { 	 
					layer.msg('用户名不能为空！');
					return;
				}
				if (username.length < 2 || username.length > 20){
					layer.msg('用户名长度不正确！');
					return;
				}
				
				if (roleId == null || roleId == ''){
					layer.msg('请选择权限组！');
					return;
				}
				
				
				if (password == null || password == ''){
					layer.msg('请输入新密码！');
					return;
				}
				if (password.length < 6 || password.length > 18){
					layer.msg('新密码长度不正确！');
					return;
				}
				if (confirmPassword == null || confirmPassword == ''){
					layer.msg('请输入确认密码！');
					return;
				}
				if (password != confirmPassword){
					layer.msg('确认密码不一致，请重新输入！');
					return;
				}
								
			
				var jsonRequest = $.ajax({
					type: "POST",
					url: "saveOrUpdate",
					data: {id:id, username:username,roleId:roleId,password:password,confirmPassword:confirmPassword},
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

function user_delete(id){
	layer.confirm('确认删除该用户吗?', { 
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

//批量派单
function batch_delete(){
	var userIds;
	$('input[name="userIds"]:checked').each(function(i){
	      if(0==i){
	    	  userIds = $(this).val();
	      }else{
	    	  userIds += (","+$(this).val());
	      }
	});

   if(userIds != null){
  	layer.confirm('确认删除选中的管理员吗', {
		  btn: ['确认','取消'] //按钮
		}, function(){
			var jsonRequest = $.ajax({
				type: "POST",
				url: "batchDelete",
				data: {userIds:userIds},
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
		  layer.msg('已取消!');
		});
	}
	else{
		layer.msg('请选择管理员!');
	}

}
