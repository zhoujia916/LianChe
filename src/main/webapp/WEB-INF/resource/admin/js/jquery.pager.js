(function ($) {
    $.fn.pager = function (options, param) {
        if (typeof options == "string") {
			var method = $.fn.pager.methods[options];
			if($.isFunction(method)){
				method.call($(this), param)
			}
			return;
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'pager');
            if (state) {
                state.options = $.extend(state.options, options);
            } else {
                $.data(this, 'pager', {
                    options: $.extend($.fn.pager.defaults, options)
                });
            }
			if(options.recordCount > 0){
				setTotal($(this), options.recordCount);
			}
        });
    };

    function show(jq, opts) {
		if(opts.recordCount == 0){
			jq.empty();
			return;
		}
		var hasInput = false;
        var tags = [];
        tags.push("<ul class='pagination'>");
        tags.push("<li data-page='" + (opts.pageIndex - 1) + "' class='paginate_button previous" + (opts.pageIndex == 1 ? " disabled" : "") + "'><a>上一页</a></li>");
        if (opts.pageCount > opts.buttons) {
            var start = opts.pageIndex - opts.margin;
            start = start > 0 ? start : 1;
            var end = ((start + opts.buttons - 1) > opts.pageCount) ? opts.pageCount : (start + opts.buttons - 1);

            if (opts.pageIndex > (opts.margin + 1)) {
                start = start + 1;
                tags.push("<li data-page='1' class='paginate_button'><a>1...</a></li>");
            }
            if (opts.pageIndex < (opts.pageCount - 1)) {
                end = end - 1;
            }
            for (var i = start; i <= end; i++) {
                tags.push("<li data-page='" + i + "' class='paginate_button" + (opts.pageIndex == i ? " active" : "") + "'><a>" + i + "</a></li>");
            }
            if (opts.pageIndex < (opts.pageCount - 1)) {
                tags.push("<li data-page='" + opts.pageCount + "' class='paginate_button'><a>..." + opts.pageCount + "</a></li>");
            }
			hasInput = true;
        }
        else {
            for (var i = 1; i <= opts.pageCount; i++) {
                tags.push("<li data-page='" + i + "' class='paginate_button" + (opts.pageIndex == i ? " active" : "") + "'><a>" + i + "</a></li>");
            }
        }
        tags.push("<li data-page='" + (opts.pageIndex + 1) + "' class='paginate_button next" + (opts.pageIndex == opts.pageCount ? " disabled" : "") + "'><a>下一页</a></li>");
        tags.push("<li class='paginate_button total'><a>共" + opts.pageCount + "页</a></li>");
        if(hasInput){
			tags.push("<li class='paginate_button go'><a><input type='text' value='" + opts.pageIndex + "' /><span style=''>确定</span></a></li>");
        }
		tags.push("</ul>");

		jq.html(tags.join('')).find("li.paginate_button").on("click", function(){
			change(jq, this);
		}).find("input").on("click", function(event){
			event.stopPropagation();
		}).on("keydown", function(event){
			if(event.keyCode == 13){
				$(this.parentNode.parentNode).trigger("click");
			}
		});
    }

    function change(jq, o) {
		var target = $(o);
		if (target.hasClass("disabled") || target.hasClass("active")  || target.hasClass("total")) {
			return;
		}
		var opts = $.data(jq[0], 'pager').options;
		var page = opts.pageIndex;
		if(target.hasClass("go")){
			page = $.trim(target.find("input").val());
			var reg = /\d+/;
			if(page == "" || !reg.test(page)){
				target.find("input").val(opts.pageIndex);
				return;
			}
			page = parseInt(page);
			if(page < 1 || page > opts.pageCount || page == opts.pageIndex){
				target.find("input").val(opts.pageIndex);
				return;
			}
		}else{
			page = parseInt(target.attr("data-page"));
		}
		if(page != opts.pageIndex && $.isFunction(opts.changePage)){
			opts.changePage(page, opts.pageSize);
		}
    }

    function setTotal(jq, total) {
        var opts = $.data(jq[0], 'pager').options;
        opts.recordCount = total;
		
        opts.pageCount = opts.recordCount == 0 ? 0 :
        				 opts.pageSize >= opts.recordCount ? 1 :
                         Math.ceil(opts.recordCount / opts.pageSize);
        $.data(jq[0], 'pager', { options: opts });
		
        show(jq, opts);
    }
	
	function setOptions(jq, options){
		var opts = $.data(jq[0], 'pager').options;
		opts = $.extend(opts, options);
		if(options.recordCount > 0){
			opts.pageCount = opts.recordCount == 0 ? 0 :
							 opts.pageSize >= opts.recordCount ? 1 :
							 Math.ceil(opts.recordCount / opts.pageSize);
		}
		$.data(jq[0], 'pager', opts);
		show(jq, opts);
	}

    function getOptions(jq) {
        return $.data(jq[0], 'pager').options;
    }
	

    $.fn.pager.methods = {
        options: function (jq) {
            return getOptions(jq);
        },
        setTotal: function (jq, total) {
            setTotal(jq, total);
        }
    }

    $.fn.pager.defaults = {
        pageIndex: 1,
        pageSize: 15,
        pageCount: 0,
        recordCount: 0,
        buttons: 9,
        margin: 4,
        search: "",
        changePage: null
    };
})(jQuery);