$().ready(function() {
	var $search_url = $("#search_url").val();
	$("#baidu_button").click(function() {
		var $input = $("#main_search").val();
		var href= $search_url+"/"+"baidu"+"/"+$input+".html";
		window.location.href =href;
	});
	$("#haosou_button").click(function() {
		var $input = $("#main_search").val();
		var href= $search_url+"/"+"haosou"+"/"+$input+".html";
		window.location.href =href;
	});
	$("#bing_button").click(function() {
		var $input = $("#main_search").val();
		var href= $search_url+"/"+"bing"+"/"+$input+".html";
		window.location.href =href;
	});
	$("#google_button").click(function() {
		var $input = $("#main_search").val();
		var href= $search_url+"/"+"google"+"/"+$input+".html";
		window.location.href =href;
	});
	
	$('#baidu_button').mouseover(function(){
		$('#baidu_button img').attr({src:"/pic/baidu2.jpg"});
		
	});
	$('#baidu_button').mouseout(function(){
		$('#baidu_button img').attr({src:"/pic/baidu.jpg"});
	});
	
	$('#haosou_button').mouseover(function(){
		$('#haosou_button img').attr({src:"/pic/haosou2.jpg"});
		
	});
	$('#haosou_button').mouseout(function(){
		$('#haosou_button img').attr({src:"/pic/haosou.jpg"});
	});
	
	$('#bing_button').mouseover(function(){
		$('#bing_button img').attr({src:"/pic/bing2.jpg"});
		
	});
	$('#bing_button').mouseout(function(){
		$('#bing_button img').attr({src:"/pic/bing.jpg"});
	});
	
	$('#google_button').mouseover(function(){
		$('#google_button img').attr({src:"/pic/google2.jpg"});
		
	});
	
	
	var json = {
			searchId:"5ce99686-4d19-4d3c-9e47-7df56206b734",
			engineName:"haosou",
			page:2
	};
	
	
	$('#google_button').mouseout(function(){
		$('#google_button img').attr({src:"/pic/google.jpg"});
		$.post("http://localhost/s/search/page",json,function(data){
			if(data.status==200){
				alert("yes");
			}
		});
	});
});



/*function blur(){
	alert("xxxxx");
}
*/


/*var json = {
		searchContent:$input,
		engineName:'baidu'
};*/