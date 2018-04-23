//设置当前栏目高亮
function setMenu(id,sid){
	var that;
	$('.menu_p li').each(function(){
		if($(this).data('id')==id){
			$(this).addClass('active');
		}
	});
	$('.menu_s .menu_block').each(function(){
		if($(this).data('pid')==id){
			that = $(this);
			$(this).addClass('active');
		}
		$("#main_content").resize();
	});
	if(sid){
		if(that){
			that.find('li').each(function(){
				if($(this).data('id')==sid){
					$(this).addClass('active');
				}
			});
		}
	}
}
$(function(){
	$('.menu_p li').click(function(){
		if($(this).hasClass('active')){
			var id = $(this).data('id');
			$('.menu_s .menu_block').each(function(){
				if($(this).data('pid')==id){
					$(this).removeClass('active');
				}
			});
			$(this).removeClass('active');
		}else{
			var id = $(this).data('id');
			$(this).addClass('active').siblings().removeClass('active');
			$('.menu_s .menu_block').removeClass('active');
			$('.menu_s .menu_block').each(function(){
				if($(this).data('pid')==id){
					$(this).addClass('active');
				}
			});
		}
		$("#main_content").resize();
	});

	//select2
	$('.select').select2({
		minimumResultsForSearch: -1
	});

	$('.datepicker').datepicker({
      autoclose: true,
      format: 'yyyy-mm-dd',
      language:'zh-CN',
      pickTime: true
    });

	$('.fancybox').fancybox();
});

Date.prototype.Format = function(formatStr)   
{   
    var str = formatStr;   
    var Week = ['日','一','二','三','四','五','六'];  
  
    str=str.replace(/yyyy|YYYY/,this.getFullYear());   
    str=str.replace(/yy|YY/,(this.getYear() % 100)>9?(this.getYear() % 100).toString():'0' + (this.getYear() % 100));   
  
    str=str.replace(/MM/,this.getMonth()>9?this.getMonth().toString():'0' + this.getMonth());   
    str=str.replace(/M/g,this.getMonth());   
  
    str=str.replace(/w|W/g,Week[this.getDay()]);   
  
    str=str.replace(/dd|DD/,this.getDate()>9?this.getDate().toString():'0' + this.getDate());   
    str=str.replace(/d|D/g,this.getDate());   
  
    str=str.replace(/hh|HH/,this.getHours()>9?this.getHours().toString():'0' + this.getHours());   
    str=str.replace(/h|H/g,this.getHours());   
    str=str.replace(/mm/,this.getMinutes()>9?this.getMinutes().toString():'0' + this.getMinutes());   
    str=str.replace(/m/g,this.getMinutes());   
  
    str=str.replace(/ss|SS/,this.getSeconds()>9?this.getSeconds().toString():'0' + this.getSeconds());   
    str=str.replace(/s|S/g,this.getSeconds());   
  
    return str;   
}   
function getTime2Time($time1, $time2)
{
    var time1 = arguments[0], time2 = arguments[1];
    time1 = Date.parse(time1)/1000;
    time2 = Date.parse(time2)/1000;
    var time_ = time1 - time2;
    return (time_/(3600*24));
}