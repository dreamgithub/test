$(document).ready(function () {

	var theme_list_open = false;

	$("#theme_select").click( function () {
		if (theme_list_open == true) {
    		$("#theme_list ul").hide();
    		theme_list_open = false;
		} else {
			$("#theme_list ul").show();
			theme_list_open = true;
		}
		return false;
	});

	$("#theme_list ul li a").click(function () {
    	var theme_data = $(this).attr("rel").split(",");
    	$("li.purchase a").attr("href", theme_data[1]);
    	$("li.remove_frame a").attr("href", theme_data[0]);
    	$("#iframe").attr("src", theme_data[0]);
    	$("#theme_list a#theme_select").html('<span class="inner">'+$(this).html()+'</span>');
    	$("#theme_list ul").hide();
    	theme_list_open = false;
    	return false;
	});

	$('.viewport-preview-link').click(function() {

		var $el = $(this),
			$wrap = $('.device'),
			iframe = $wrap.find('#iframe').parent().html(),
			device = $(this).attr('title');

		if ( $el.hasClass('rotate') ) {
			return false;
		}

		$('.viewport-preview a').removeClass('selected');

		$el.addClass('selected');

		$wrap.closest('.viewport-wrap').removeClass('desktop tablet mobile').addClass(device);

		if ( device == 'tablet' ) {
			$wrap.removeClass('iphone6').addClass('marvel-device ipad');
			$el.closest('.viewport-preview').find('.rotate').animate({opacity: '1'}, 200);
		} else if ( device == 'mobile' ) {
			$wrap.removeClass('ipad').addClass('marvel-device iphone6');
			$el.closest('.viewport-preview').find('.rotate').animate({opacity: '1'}, 200);
		} else {
			$wrap.removeClass('marvel-device ipad iphone');
			$el.closest('.viewport-preview').find('.rotate').animate({opacity: '0'}, 200);
		}

		return false;
	});

	$('.viewport-preview .rotate').click(function(){

		$device = $('.viewport-wrap .device');

		if ( $device.hasClass('landscape') ) {
			$device.removeClass('landscape');
		} else {
			$device.addClass('landscape');
		}
		return false;
	});

	$(".screenshot-tool-tip").each(function(){
		$(this).poshytip({
			className: 'tip-twitter',
			content: '<img src="' + $(this).attr('title') + '" alt="Preview Screenshot..." />',
			className: 'tip-twitter',
			followCursor: true,
			slide: false,
			showTimeout: 1,
			fade: false,
			offsetX: 50,
			offsetY: 0,
		});
	});
});