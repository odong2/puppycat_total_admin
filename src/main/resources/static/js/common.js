// csrf ajax 사용시 필요
/*
$.ajaxPrefilter(function (options, originalOptions) {
	if (!options.processData && !options.contentType) {
		options.data.append('csrf_fiverse_token', getCsrfCookie('csrf_fiverse_cookie'));
	} else if (options.type.toLowerCase() === 'post') {
		options.data = $.param($.extend({}, originalOptions.data, {
			csrf_fiverse_token: getCsrfCookie('csrf_fiverse_cookie')
		}))
	}
});

function getCsrfCookie(cname) {
	let name = cname + '=';
	let decodedCookie = decodeURIComponent(document.cookie);
	let ca = decodedCookie.split(';');

	for(let i = 0; i <ca.length; i++) {
		let c = ca[i];
		while (c.charAt(0) == ' ') {
			c = c.substring(1);
		}

		if (c.indexOf(name) == 0) {
			return c.substring(name.length, c.length);
		}
	}

	return '';
}
*/

// window.popup 함수
function js_popup(url, pop_name, w, h, z) {

    // 사이즈 처리
    if ( !w ) { w = 500; }
    if ( !h ) {	h = 400; }
    if ( !z ) {	z = 1; }

    let window_left	= (screen.width / 2) - (w / 2);
    if ( window_left < 0 ) {
        window_left = 0;
    }
    let window_top	= (screen.height / 2) - (h / 2);
    if ( window_top < 0 ) {
        window_top = 0;
    }

    let js_pop_form = window.open(url, pop_name,"top="+window_top+",left="+window_left+",toolbar=0,location=0,directories=0,status=0,menubar=0,scrollbars="+z+",resizable="+z+",width="+w+",height="+h);
    js_pop_form.resizeTo(w, h);
    js_pop_form.focus();
}

// bootstrap toast alert
let toast = {
	alert: function (message, options) {
		let settings = $.extend({
			title: "알림",
			bg_color: "white",
		}, options);

		let timestamp = $.now();
		let html_toast = '<div id="liveToast_' + timestamp + '" class="toast" role="alert" aria-live="assertive" aria-atomic="true" data-bs-autohide=true data-bs-delay="10000">';
		html_toast += '<div class="toast-header bg-danger">';
		html_toast += '<i class="fas fa-bell text-' + settings.bg_color + ' me-2"></i>';
		html_toast += '<strong class="me-auto text-white">' + settings.title + '</strong>';
		html_toast += '<button type="button" class="btn-close text-white" data-bs-dismiss="toast" aria-label="Close"></button>';
		html_toast += '</div>';
		html_toast += '<div class="toast-body">';
		html_toast += message;
		html_toast += '</div>';
		html_toast += '</div>';

		$("#box_toast").append(html_toast);

		let toastLive = $("#liveToast_" + timestamp);
		let toast = new bootstrap.Toast(toastLive);
		toast.show();
	}
}

// 페이지 로딩 후 처리
$(document).ready(function(){
	// Add active state to sidbar nav links
	let path = window.location.href; // because the 'href' property of the DOM element is the absolute path
	$("#layoutSidenav_nav .sb-sidenav a.nav-link").each(function() {
		if (this.href === path) {
			$(this).addClass("active");
		}
	});

	// Toggle the side navigation
	$("#sidebarToggle").on("click", function(e) {
		e.preventDefault();
		$("body").toggleClass("sb-sidenav-toggled");
	});

	// dropdown
	let dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'))
	dropdownElementList.map(function (dropdownToggleEl) {
		return new bootstrap.Dropdown(dropdownToggleEl)
	});

	//메뉴열때 다른메뉴 닫기
	$(".collapse").on("show.bs.collapse", function(e) {
		//$(".collapse").removeClass('show');
	});

	try{
		//마지막 선택 메뉴 활성화
		let myCollapse = $("input[name=sel_admin_leftmenu]").val();
		$("#"+myCollapse).collapse('show');
	}catch(e){

	}

	// 체크박스
	$("[type=checkbox][name='idx[]']").on("change", function () {
		let check = $(this).prop("checked");

		//전체 체크
		if ($(this).hasClass("allcheck")) {
			$("[type=checkbox][name='idx[]']").prop("checked", check);

			//단일 체크
		} else {
			let all = $("[type=checkbox][name='idx[]'].allcheck");
			let allcheck = all.prop("checked");

			if (check != allcheck) {
				let len = $("[type=checkbox][name='idx[]']").not(".allcheck").length;
				let ckLen = $("[type=checkbox][name='idx[]']:checked").not(".allcheck").length;

				if (len === ckLen) {
					all.prop("checked", true);
				} else {
					all.prop("checked", false);
				}
			}
		}
	});

	// 입력시 히스토리 이슈로 인해 autocomplete:off 추가
	$(".sel_date").attr("autocomplete","off");
	$(".sel_date").datepicker({
		changeMonth: true,
		changeYear: true,
		showMonthAfterYear: true,
		yearRange: 'c-5:c+5',
		dayNamesMin: ['일', '월', '화', '수', '목', '금', '토'], // 요일의 한글 형식.
		monthNamesShort: ['1월','2월','3월','4월','5월','6월','7월','8월','9월','10월','11월','12월'],
		dateFormat: "yy-mm-dd",
		altFormat: "yy-mm-dd",
		showButtonPanel: true,
		nextText: '다음 달',
		prevText: '이전 달',
		currentText: '오늘',
		closeText: '닫기'
	});
    
    // 날짜+시간
    $('.datetimepicker').datetimepicker();
});

// 페이징
function pagination (type, page, pagination) {
	// 시작 번호
	// let num = pagination.totalRecordCount - ((res.data.params.page - 1) * res.data.params.recordSize);

	if (!pagination) {
		document.querySelector('.pagination').innerHTML = '';
		return false;
	}

	let html = '';

	// 첫 페이지, 이전 페이지
	if (pagination.existPrevPage) {
		html += `
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(1);" aria-label="Previous">
					<span class="fas fa-angle-double-left" aria-hidden="true">&laquo;</span>
				</a>
			</li>
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${pagination.startPage - 1});" aria-label="Previous">
					<span class="fas fa-angle-left" aria-hidden="true">&lsaquo;</span>
				</a>
			</li>
		`;
	}

	// 페이지 번호
	for (let i = pagination.startPage; i <= pagination.endPage; i++) {
		const active = (i === page) ? 'active' : '';
		html += `<li class="page-item ${active}"><a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${i})">${i}</a></li>`;
	}

	// 다음 페이지, 마지막 페이지
	if (pagination.existNextPage) {
		html += `
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${pagination.endPage + 1});" aria-label="Next">
					<span class="fas fa-angle-right" aria-hidden="true">&rsaquo;</span>
				</a>
			</li>
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${pagination.totalPageCount});" aria-label="Next">
					<span class="fas fa-angle-double-right" aria-hidden="true">&raquo;</span>
				</a>
			</li>
		`;
	}

	document.querySelector('.pagination').innerHTML = html;
}

// paging 커스텀
function  paginationById (elementId, idx, type, page, pagination) {
	// 시작 번호
	// let num = pagination.totalRecordCount - ((res.data.params.page - 1) * res.data.params.recordSize);

	if (!pagination) {
		document.querySelector(elementId).innerHTML = '';
		return false;
	}

	let html = '';

	// 첫 페이지, 이전 페이지
	if (pagination.existPrevPage) {
		html += `
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(1, ${idx});" aria-label="Previous">
					<span class="fas fa-angle-double-left" aria-hidden="true">&laquo;</span>
				</a>
			</li>
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${pagination.startPage - 1}, ${idx});" aria-label="Previous">
					<span class="fas fa-angle-left" aria-hidden="true">&lsaquo;</span>
				</a>
			</li>
		`;
	}

	// 페이지 번호
	for (let i = pagination.startPage; i <= pagination.endPage; i++) {
		const active = (i === page) ? 'active' : '';
		html += `<li class="page-item ${active}"><a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${i}, ${idx})">${i}</a></li>`;
	}

	// 다음 페이지, 마지막 페이지
	if (pagination.existNextPage) {
		html += `
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${pagination.endPage + 1}, ${idx});" aria-label="Next">
					<span class="fas fa-angle-right" aria-hidden="true">&rsaquo;</span>
				</a>
			</li>
			<li class="page-item">
				<a href="javascript:void(0)" class="page-link rounded-circle mx-1" style="font-size:12px;" onclick="${type}.list(${pagination.totalPageCount}, ${idx});" aria-label="Next">
					<span class="fas fa-angle-double-right" aria-hidden="true">&raquo;</span>
				</a>
			</li>
		`;
	}

	document.querySelector(elementId).innerHTML = html;
}


/**
 * 천단위 콤마
 * @param num
 * @returns {string}
 */
function addComma(num) {
	return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}