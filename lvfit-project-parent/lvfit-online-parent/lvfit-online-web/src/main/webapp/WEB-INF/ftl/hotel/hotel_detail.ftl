<!-- hotelAlert 酒店弹窗 -->
<div class="resortOverlay"></div>
<div class="hotelAlert">
	<a href="javascript:;" class="ph_icon ph_icon_closehotel pa JS_hotelAlertClose"></a>
	<div class="hotelAlert-tit">
		<em id="hotel_detail_name" class="yh f20 c3"></em>
		<span id="hotel_detail_star" class="c9 pl10"></span>
	</div>
	
	<ul class="hotelAlertTab mt15 f14">
		<li class="active" >酒店简介</li>
		<li class="comment_hidden">点评<dfn class="cf60" id="hotel_comment_count2"></dfn></li>
		<li class="last">地图</li>
	</ul>
	<div class="hotelAlertCon">
		<!-- 酒店信息 -->
		<div class="hotelAlertInfo clearfix hotelAlertCon-li mt15" style="display:block">
			<!-- hotelInfo 酒店图示 -->
	        <div class="hotelInfo-Img fl pr">
	           <div id="hotel_detail_pic_big" class="hotelInfo-pic pr"></div>
	            <div class="hotelInfo-spic pr ml20 mt5">
	                <ul id="hotel_detail_pic_small" class="pa"></ul>
	            </div><!-- //hotelInfo-spic -->
	            <a href="javascript:;" class="l hotelImg-btn"><span class="ph_icon ph_icon_alertl"></span></a>
	            <a href="javascript:;" class="r hotelImg-btn"><span class="ph_icon ph_icon_alertr"></span></a>
	        </div><!-- //hotelInfo-Img -->
	
	        <!-- hotelInfo酒店信息 -->
	        <div class="hotelInfoBox fr pr20">
	            <ul class="hotelInfo">
	            	<li><em>酒店地址：</em><span id="hotel_detail_address"></span></li>
	            	<li>
	            		<em>设施服务：</em>
						<div class="list-icon" id="hotel_detail_fac"></div>
	            	</li>
	            	<li><em>酒店电话：</em><dfn id="hotel_detail_phone" class="cf60 f16"></dfn></li>
	            	<li><em>酒店政策：</em><span id="hotel_detail_policy"></span></li>
	            	<li><em>酒店简介：</em><span id="hotel_detail_desc"></span></li>
	            </ul>
				
				<!-- 地理位置 -->
				<div class="c9 mt15">地理位置：</div>
	            <div id="hotel_detail_traffic" class="hotelInfo-address mt10"></div><!-- //hotelInfo-address -->
	        </div><!-- //hotelInfo -->
		</div><!-- //hotelAlertInfo -->
		
		<!-- 点评 -->
		<div class="hotelAlertCom hotelAlertCon-li comment_hidden">
			<div class="alertComCount c9">
				<dfn class="f18 cf60" id="hotel_comment_per"></dfn>
				好评率&emsp;来自<dfn class="c6 f14" id="hotel_comment_count"></dfn>条点评</div>
			<div class="hotelAleCom mt5" id="hotel_comment_page">
			</div><!-- //alertCom -->
			<!-- 分页 -->
	        <div class="paging orangestyle">
				<div class="pagebox" id="pageComment">
		    	</div>
	        </div><!-- //paging -->
		</div><!-- //hotelAlertCom -->
		
		<!-- 地图 -->
		<div class="hotelAlertMap hotelAlertCon-li">
			<div class="traffic-map" id="traffic-map"></div>
		</div><!-- //hotelAlertMap -->
		
   </div><!-- //hotelAlertCon -->
  </div> <!-- //hotelAlert -->
  <!-- hotelAlert 酒店弹窗结束 -->
   <script>
	var body ;
	var comment ; 
	var sp_data = {};
	var sp_data2 = {};
	sp_data2.page = 1 ; //当前页数
	sp_data2.rows = 3 ; //当前页数
	var picBaseUrl = "http://pic.lvmama.com";
	//酒店信息弹层事件
	$('.list-t-img,.list-t-tit a').click(function(){
		sp_data.hotelId=$(this).attr("name");
		sp_data2.productId= $(this).attr("name") ;
		initBox(0);
	});
	//酒店点评弹层
	$('.JS_alertBox').click(function(){
		sp_data.hotelId=$(this).attr("name");
		sp_data2.productId= $(this).attr("name") ;
		initBox(1);
	});
	//酒店地图弹层
	$('.JS_maplink').click(function(){
		sp_data.hotelId=$(this).attr("name");
		sp_data2.productId= $(this).attr("name") ;
		initBox(2);
	});
	
	function initBox(i){
		//酒店详情初始化
		initHotelDetail();
		//点评实例化
		initHotelComment(1);
		if(body){
			showAlert(i,body.longitude,body.latitude,body.name);
		}
	}
	function initHotelComment(page){
		sp_data2.page = page;
		comment = $.getAjaxHtmlData("${request.contextPath}/hotel/querycmtpage",sp_data2);
		if(comment == null || comment.comments.results == null || comment.comments.results.length == 0){
			$(".comment_hidden").hide();
			return ;
		}else{
			$(".comment_hidden").show();
		}
		var results = comment.comments.results;
		var pagination = comment.pagination ;
		if(comment.commentStat){
			$("#hotel_comment_per").empty().append(comment.commentStat.formatAvgScore+"%");
		}else{
			$("#hotel_comment_per").empty().append("100%");
		}
		$("#hotel_comment_count").empty().append(pagination.records);
		$("#hotel_comment_count2").empty().append("（"+pagination.records+"）");
		var htmlAll ="";
		var html ;
		
		if(results && results.length >0){
			for(var i=0;i<results.length;i++ ){
				htmlAll += '<div class="comment-li"><div class="ufeed-info"><p class="ufeed-score">';
				var recmends = results[i].cmtLatitudes;
				if(recmends && recmends.length>0){
					html = "";
					html += '<span class="ufeed-level"><i data-level="'+ recmends[recmends.length-1].score+'" data-mark="" style="width: '+recmends[recmends.length-1].score * 20+'%;"></i></span>';
					for(var j=0; j<recmends.length-1;j++){
						html += '<span class="ufeed-item"><em>' + recmends[j].latitudeName + '</em><i>'+ recmends[j].score+"("+recmends[j].chScore+")" +'</i></span>';
					}
					$("#hotel_comment_recm").append(html);
				}
				htmlAll += html + '</p></div> <div class="ufeed-content" >' + results[i].content.replace(/<[^>]+>/g,"") ;
				if(results[i].content.replace(/[^x00-xFF]/g,'**').replace(/<[^>]+>/g,"").replace(/(^\s*)|(\s*$)/g, "").length>150){
					htmlAll +='<a href="javascript:;" class="ufeed-btn JS-ufeed-btn">查看详情</a>';
				}
				htmlAll += '</div><div class="com-userinfo">'+results[i].userName+" &emsp;发表点评于&emsp;" +new Date(results[i].createdTime).format("yyyy-MM-dd")+'</div></div>';
			}
		}
		
		var pageHtml = getPageHtml(pagination,"pagesel","initHotelComment");
		$("#pageComment").empty().append(pageHtml);
		
		$("#hotel_comment_page").empty().append(htmlAll);
	}
	
	function initHotelDetail(){
		body = $.getAjaxHtmlData("${request.contextPath}/hotel/showHotelDetail",sp_data);
		if(!body){
			return;
		}
		$("#hotel_detail_name").empty().append(body.name);
		$("#hotel_detail_star").empty().append(body.star);
		$("#hotel_detail_address").empty().append(body.address);
		if(body.earliestArriveTime && body.latestLeaveTime){
			$("#hotel_detail_policy").empty().append("最早到店时间:"+body.earliestArriveTime+"  最晚离店时间:"+body.latestLeaveTime);
		}
		$("#hotel_detail_phone").empty().html(body.telephone);
		$("#hotel_detail_desc").empty().append(body.description);
		$("#hotel_detail_traffic").empty().append(body.traffic);
		
		if(body.picList && body.picList.length>0){
			var html = "";
			var html2 = "";
			for(var i=0; i<body.picList.length;i++){
				if(i==0){
					if(body.picList[i].flag == 2){ //5:2的进行图片截取
						html += '<p class="active"><img class="bigImg" src="'+picBaseUrl+body.picList[i].photoUrl+'" width="455" height="303"></p>';
						html2 += '<li class="active"><img class="smallImg" src="'+picBaseUrl+body.picList[i].photoUrl+'" width="100" height="66" alt="'+body.picList[i].photoDesc+'"><span class="zhezhao"></span></li>';
					}else{
						html += '<p class="active"><img  src="'+picBaseUrl+body.picList[i].photoUrl+'" width="455" height="303"></p>';
						html2 += '<li class="active"><img  src="'+picBaseUrl+body.picList[i].photoUrl+'" width="100" height="66" alt="'+body.picList[i].photoDesc+'"><span class="zhezhao"></span></li>';
					}
				}else{
					if(body.picList[i].flag == 2){
	                	html += '<p><img class="bigImg" src="'+picBaseUrl+body.picList[i].photoUrl+'" width="455" height="303"></p>';
	                	html2 += '<li><img class="smallImg" src="'+picBaseUrl+body.picList[i].photoUrl+'" width="100" height="66" alt="'+body.picList[i].photoDesc+'"><span class="zhezhao"></span></li>';
					}else{
	                	html += '<p><img src="'+picBaseUrl+body.picList[i].photoUrl+'" width="455" height="303"></p>';
	                	html2 += '<li><img src="'+picBaseUrl+body.picList[i].photoUrl+'" width="100" height="66" alt="'+body.picList[i].photoDesc+'"><span class="zhezhao"></span></li>';
					}
				}
			}
			$("#hotel_detail_pic_big").empty().append(html);
			$("#hotel_detail_pic_small").empty().append(html2);
		}
		
		if(body.facilities && body.facilities.length>0){
			var html = "";
			for(var i in body.facilities){
				var $element = document.createElement("span");
				switch (body.facilities[i].id) {
				case 460:
					html += '<span class="ph_icon_hotel ph_icon_wifi_f" title="免费Wifi"></span>';
					break;
				case 462:
					html += '<span class="ph_icon_hotel ph_icon_internet_f" title="免费宽带"></span>';
					break;
				case 464:
					html += '<span class="ph_icon_hotel ph_icon_park_f" title="免费停车场"></span>';
					break;
				case 466:
					html += '<span class="ph_icon_hotel ph_icon_pick_f" title="免费接机"></span>';
					break;
				case 468:
					html += '<span class="ph_icon_hotel ph_icon_swim-in" title="室内游泳池"></span>';
					break;
				case 469: //室外游泳池  暂时没有
					html += '<span class="ph_icon_hotel ph_icon_swim-out" title="室外游泳池"></span>';
					break;
				case 470:
					html += '<span class="ph_icon_hotel ph_icon_gym" title="健身房"></span>';
					break;
				case 471:
					html += '<span class="ph_icon_hotel ph_icon_bc" title="商务中心"></span>';
					break;
				case 472:
					html += '<span class="ph_icon_hotel ph_icon_mr" title="会议室"></span>';
					break;
				case 473:
					html += '<span class="ph_icon_hotel ph_icon_restaurant" title="酒店餐厅"></span>';
					break;
					
				case 461:
					html += '<span class="ph_icon_hotel ph_icon_wifi_c" title="收费Wifi"></span>';
					break;
				case 463:
					html += '<span class="ph_icon_hotel ph_icon_internet_c" title="收费宽带"></span>';
					break;
				case 465:
					html += '<span class="ph_icon_hotel ph_icon_park_c" title="收费停车场"></span>';
					break;
				case 467:
					html += '<span class="ph_icon_hotel ph_icon_pick_c" title="收费接机"></span>';
					break;
				case 468:
					html += '<span class="ph_icon_hotel ph_icon_swim-in" title="室内游泳池"></span>';
					break;
				default:
					break;
				}
			}
			$("#hotel_detail_fac").empty().append(html);
		}

		
	}
	
	
		
	
</script>
   