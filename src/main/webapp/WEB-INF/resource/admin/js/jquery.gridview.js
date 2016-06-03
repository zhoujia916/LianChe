(function ($) {
    $.fn.gridview = function (options, param) {
        if (typeof options == "string") {
			var method = $.fn.gridview.methods[options];
			if(method != undefined && method != null && $.isFunction(method)){
				return method.call($(this), param);
			}
        }
        options = options || {};
        return this.each(function () {
            var state = $.data(this, 'gridview');
            if (!state){
				state = $.extend(true, {}, $.fn.gridview.defaults, options);
                $.data(this, 'gridview', state);
				initialize($(this), state);
            }
        });
    };
	
	function debugMsg(mode, msg){
		if(mode){
			if(console.log){
				console.log(msg);
			}
		}
	}
	
	function initialize(jq, opts){
		if(opts.columns == null || opts.columns.length == 0){
			debugMsg(opts.debug, "columns can't be null or empty");
			return;
		}
		
		loadData(jq);

        attachEvent(jq);
	}
	
	function attachEvent(jq){
		var opts = getOption(jq);

		jq.on("click", opts.gridfilter + " .btn-action", function(){
			if($(this).attr("data-action") == "search"){
				var data = {};
				jq.find(opts.gridfilter).find("input, select").each(function(){
					data[$(this).attr("name")] = $.trim($(this).val());
				});
				opts.filter = data;
                opts.pagination.pageIndex=1;
				setOption(jq, { filter: data });
				loadData(jq);
			}
		});
		
		jq.on("click", opts.gridaction + " .btn-action", function(){
			var action = $(this).attr("data-action");
			if(action == "add"){
				showForm(jq, action);
			}
			else if(action == "edit" || action == "view"){
				var rows = getRows(jq, 1);
				if(rows.length == 0){
					opts.onUnselect.call(jq);
					return;
				}
				showForm(jq, action, rows[0]);
			}
			else if(action == "delete"){
				var keys = getKeys(jq);
				if(keys.length == 0){
					opts.onUnselect.call(jq);
					return;
				}
				opts.onDelete(function(){
					$.post(opts.form.url, { action: action, ids: keys.join(',')}, function(response){
						if(response[opts.jsonReader.statu_field] == 0){
							loadData(jq);
							if(opts.afterDeleteSuccess && $.isFunction(opts.afterDeleteSuccess)){
								opts.afterDeleteSuccess.call(jq);
							}
						}else{
							if(opts.afterDeleteError && $.isFunction(opts.afterDeleteError)){
								opts.afterDeleteError.call(jq);
							}
						}
					});
				});
			}
			else if(action == "search"){
				if(opts.search.show){
					$(opts.search.selector).show();
				}
			}
			else if(action == "refresh"){
				loadData(jq);
			}
		});
		
		jq.on("click", opts.gridtable + " .btn-action", function(){
			var action = $(this).attr("data-action").toLowerCase();
			var key = $(this).attr("data-key");
			
			
			if(action == "edit" || action == "view"){
				showForm(jq, action, getRowData(jq, key));
			}
			else if(action == "delete"){
				opts.onDelete(function(){
					$.post(opts.form.url, { action: action, id: key}, function(response){
						if(response[opts.jsonReader.statu_field] == 0){
							loadData(jq);
							if(opts.afterDeleteSuccess && $.isFunction(opts.afterDeleteSuccess)){
								opts.afterDeleteSuccess.call(jq, key);
							}
						}else{
							if(opts.afterDeleteError && $.isFunction(opts.afterDeleteError)){
								opts.afterDeleteError.call(jq, key);
							}
						}
					});
				});
			}
		});
		
		jq.on("change", opts.gridtable + " thead " + opts.gridallcb, function(){
			var checked = this.checked;
			jq.find(opts.gridtable + " tbody " + opts.gridrowcb).each(function(){
				this.checked = checked;
			});
		});
			
		jq.on("click", opts.gridform + " .btn-action", function(){
			var action = $(this).attr("data-action");
			var form = false;
			var forms = $(opts.gridform);
			for(var i = 0; i < forms.length; i++){
				if($.contains(forms[i], this)){
					form = $(forms[i]);
                    break;
				}
			}
			if(action == "save"){
				var result = opts.form.check();
				if(result != true){
					form.find(".alert-info").html(result).slideDown();
					return;
				}
				var data = $.extend(true, {
                    action: opts.form.actions[form.attr("data-action")]
                }, fetchForm(form));
				$.post(opts.form.url, data, function (response) {
					if (response.code == 0) {
                        form.hide();
						opts.afterSaveSuccess.call(jq);
						loadData(jq);
					} else {
						opts.afterSaveError.call(jq);
					}
				});
			}
			else if(action == "hide"){
				form.find(".alert-info").hide();
				form.hide();
			}
			else if(action == "reset"){
				form.find(".alert-info").slideUp();
				fillForm(form);
			}
		});
			
		
		if(opts.treeReader.show){
			jq.on("click", opts.gridtable + " tbody " + opts.treeReader.branch_class, function(event){
				var key = $(this.parentNode).attr("data-key");
				var children = jq.find(opts.gridtable + " tbody tr[data-parent='" + key + "']");
				if($(this.parentNode).attr("data-status") == "true"){
					children.hide();
					$(this.parentNode).attr("data-status", "false");
                    $(this).find(".ace-icon").attr("class", opts.treeReader.icons.close);
				}else{
					children.show();
					$(this.parentNode).attr("data-status", "true");
                    $(this).find(".ace-icon").attr("class", opts.treeReader.icons.open);
				}

                event.stopPropagation();
			});
		}

		jq.find(opts.gridform + " .widget-box").draggable({ handle:".widget-header", cancel: ".widget-toolbar" });
	}
	
	function getKeys(jq, length){
		var keys = [];
		var opts = $.data(jq[0], "gridview");
		if(opts.status == "loaded.success" && opts.data.length > 0){
			$(opts.gridtable + " tbody tr").each(function(){
				var cb = $(this).find("input" + opts.gridrowcb);
				if(cb.length > 0 && cb[0].checked){
					keys.push(cb.val());
					if(length && length > 0 && keys.length >= length){
						return false;
					}
				}
			});
		}
		return keys;
	}
	
	function getRows(jq, length){
		var rows = [];
		var opts = $.data(jq[0], "gridview");
		if(opts.status == "loaded.success" && opts.data.length > 0){
			$(opts.gridtable + " tbody tr").each(function(){
				var cb = $(this).find("input" + opts.gridrowcb);
				if(cb.length > 0 && cb[0].checked){
					var rowData = getRowData(jq, cb.val());
					if(rowData != null){
						rows.push(rowData);
						if(length && length > 0 && rows.length >= length){
							return false;
						}
					}
				}
			});
		}
		return rows;
	}
	
	function getRowData(jq, key){
		var rowData = null;
		var opts = $.data(jq[0], "gridview");
		if(key != undefined && key != null && opts.status == "loaded.success" && opts.data.length > 0){
			$.each(opts.data, function(){
				if(this[opts.jsonReader.key_field] == key){
					rowData = this;
					return false;
				}
			});
		}
		return rowData;
	}
	
	function showForm(jq, action, data){
		var opts = $.data(jq[0], "gridview");
		var form = $(opts.form.selector);
		form.find(".widget-title").html(opts.form.captions[action]);
		if(action == "add"){
			fillForm(form);
			form.find(".form-actions").show();
			form.removeClass("read-form");
			form.find("input, select, textarea").removeAttr("disabled");
            form.attr("data-action", "add");
		}
		else if(action == "edit"){
			fillForm(form, data);
			form.find(".form-actions").show();
			form.removeClass("read-form");
			form.find("input, select, textarea").removeAttr("disabled");
            form.attr("data-action", "edit");
		}
		else if(action == "view"){
			fillForm(form, data);
			form.find(".form-actions").hide();
			form.addClass("read-form");
			
			form.find("input, select, textarea").attr("disabled", "disabled");
		}

        form.show();
	}
	
	function hideForm(jq, action){
		var opts = $.data(jq[0], "gridview");
		var form = $(opts.form.selector);
		form.hide();
	}
	
	function fillForm(form, data){
		var fields = [];
		form.find("input, select, textarea").each(function(){
			var name = $(this).attr("name");
			if(!!name) {
				if($.inArray(name, fields) == -1){
					var tagName = this.tagName.toLowerCase();
					var value = (!!data && data[name] != undefined && data[name] != null) ? data[name].toString() : ($(this).attr("data-init") || "");
					if (tagName == "input") {
						var type = $(this).attr("type").toLowerCase();
						if(type == "text" || type == "hidden"){
							$(this).val(value);
						}
						else if(type == "checkbox" || type == "radio"){
							if($(this).hasClass("ace-switch")){
								this.checked = $(this).val() == value;
							}
							else{
								var values = value.split(",");
								$("input[name='" + name + "']:" + type).each(function(){
                                    this.checked = $.inArray($(this).val(), values) > -1;
								});
							}
						}
					} else if (tagName == "select") {
                        var values = value.split(",");

                        if($(this).hasClass("chosen-select") && $.fn.chosen){

                            $(".chosen-select").chosen({ allow_single_deselect:true });
                        }else{
                            $(this).val(value);
                        }
					} else if (tagName == "textarea") {
						$(this).val(value);
					}
					fields.push(name);
				}
			}
		});
	}

    function isUndefinedOrNull(object){
        return object == undefined || object == null;
    }


    function fetchForm(form){
        var result = {};
        var fields = [];
        form.find("input, select, textarea").each(function(){
            var name = $(this).attr("name");
            if(!!name) {
                if($.inArray(name, fields) == -1){
                    var tagName = this.tagName.toLowerCase();
                    if (tagName == "input") {
                        var type = $(this).attr("type").toLowerCase();
                        if(type == "text" || type == "hidden" || type == "password"){
                            result[name] = $.trim($(this).val());
                        }
                        else if(type == "checkbox" || type == "radio"){
                            if($(this).hasClass("ace-switch")){
                                result[name] = this.checked ? $(this).val() : $(this).attr("offval");
                            }
                            else{
                                var values = [];
                                $("input[name='" + name + "']:" + type).each(function(){
                                    if(this.checked){
                                        values.push($(this).val());
                                    }
                                });
                                result[name] = values.join(",");
                            }
                        }
                    } else if (tagName == "select" || tagName == "textarea") {
                        result[name] = $.trim($(this).val());
                    }
                    fields.push(name);
                }
            }
        });
        return result;
    }
	
	function showLoading(jq){
		var opts = $.data(jq[0], "gridview");
		jq.find(opts.gridtable + " tbody").html("<tr><td colspan='" + opts.columns.length + "'><div class='loading'><i class='ace-icon fa fa-spinner bigger-200 orange'></i><span class='text'>正在加载</span></div></td></tr>");
	}
	
	function hideLoading(jq){
		var opts = $.data(jq[0], "gridview");
		jq.find(opts.gridtable + " tbody tr").each(function(){
			if($.contains(this, ".loading")){
				$(this).remove();
			}
		});
	}
	
	function showStatus(jq, status){
		var opts = $.data(jq[0], "gridview");
		if(status == "empty"){
			jq.find(opts.gridtable + " tbody").html("<tr><td colspan='" + opts.columns.length + "'><div class='empty'>暂无数据</div></td></tr>");
		}
		else if(status == "error" || status == "timeout" || status == "abort"){
			var html = "<tr><td colspan='" + opts.columns.length + "'>";
			html += "<div class='error'><i class='ace-icon fa fa-meh-o orange'>&nbsp;&nbsp;加载数据失败！</i><a class='btn btn-xs btn-white btn-info'><i class='ace-icon fa fa-repeat blue'></i>重新加载</a></div>";
			html += "</td></tr>";
			jq.find(opts.gridtable + " tbody").html(html).find(".btn-info").on("click", function(){
				loadData(jq);
			});
		}
	}
	
	function loadData(jq, param){
		var opts = $.data(jq[0], "gridview");
		if(opts.status === "loading"){
			opts.onLoading();
			return;
		}
		changeStatus(jq, "loading");
		
		param = $.extend(true, opts.param, opts.filter, opts.search.data, param);

        if(opts.pagination.show){
            param = $.extend(param, {pageIndex: opts.pagination.pageIndex, pageSize: opts.pagination.pageSize});
        }
		
		debugMsg(opts.debug, "Gridview request data:");
		debugMsg(opts.debug, param);
		$.ajax({
			url: opts.url,
			method: opts.method,
			data: param,
			timeout: opts.timeout,
			dataType: opts.dataType,
			context: jq,
			success: function(response, status, xhr){
				if(status == "success"){
					var opts = $.data(this[0], "gridview");
					debugMsg(opts.debug, "Gridview load data success:");
					debugMsg(opts.debug, response);
					
					if(response[opts.jsonReader.statu_field] == 0){
						if(response[opts.jsonReader.data_field] && response[opts.jsonReader.data_field].length > 0){
							var total = response[opts.jsonReader.total_field] || response[opts.jsonReader.data_field].length;
                            opts.data = response[opts.jsonReader.data_field];
                            opts.pagination.total = total;
                            changeStatus(this, "loaded.success");
						}else{
							changeStatus(this, "loaded.empty");
						}
					}else{
						
						changeStatus(this, "loaded.error");
					}
				}
			},
			error: function(xhr, status, error){
				var opts = getOption(this);
				
				debugMsg(opts.debug, "Gridview load data error:" + status);
				
				if(status == "timeout"){
					changeStatus(this, "loaded.timeout");
				}
				else if(status == "error"){
					changeStatus(this, "loaded.error");
				}
				else if(status == "abort"){
					changeStatus(this, "loaded.abort");
				}
			}
		});
	}
	
	function changeStatus(jq, status){
		var opts = $.data(jq[0], "gridview");
		opts.status = status;
		$.data(jq[0], "gridview", opts);
		if(status == "loading"){
			showLoading(jq);
			hidePagination(jq);
		}else if(status == "loaded.success"){
			hideLoading(jq);
			showData(jq);
			if(opts.onLoadSuccess && $.isFunction(opts.onLoadSuccess)){
				opts.onLoadSuccess.call(jq, opts.data);
			}
			if(opts.pagination.show){
				showPagination(jq, opts.pagination.total);
			}else{
				hidePagination(jq);
			}
            if(opts.onLoadSuccess && $.isFunction(opts.onLoadSuccess)){
                opts.onLoadSuccess.call(jq, opts.data);
            }
		}else if(status == "loaded.empty"){
			hideLoading(jq);
            showStatus(jq, "empty");
            if(opts.onLoadSuccess && $.isFunction(opts.onLoadSuccess)){
                opts.onLoadSuccess.call(jq, opts.data);
            }
		}else if(status == "loaded.error"){
			hideLoading(jq);
			showStatus(jq, "error");
		}else if(status == "loaded.timeout"){
			hideLoading(jq);
			showStatus(jq, "timeout");
		}else if(status == "loaded.abort"){
			hideLoading(jq);
			showStatus(jq, "abort");
		}
	}
	
	function changePage(jq, pageIndex, pageSize){
		setOption(jq, { pagination:{ pageIndex: pageIndex, pageSize: pageSize } });
		loadData(jq);
	}

    function showData(jq, parentId) {
		var opts = $.data(jq[0], "gridview");
		if(opts.status == "loaded.success" && opts.data.length > 0){
            var html = "";
            $.each(opts.data, function () {
                if(opts.treeReader.show) {
                    if (this[opts.treeReader.parent_field] == opts.treeReader.parent_root){
                        html += showRow(opts, this, true);
                    }
                }else{
                    html += showRow(opts, this, false);
                }
            });

			jq.find(opts.gridtable + " tbody").html(html);
		};
    }

    function showRow(opts, row, children){
        var html = "";
        var attr = " data-key='" + row[opts.jsonReader.key_field] + "'";
        if(opts.treeReader.show) {
            attr += " data-parent='" + row[opts.treeReader.parent_field] + "'";
            attr += " data-status='" + (row[opts.treeReader.expaned_field] ? "true" : "false") + "'";
        }
        html += "<tr" + attr + ">";
        $.each(opts.columns, function () {
            var format = this.format || formatColumn;
            html += format(opts, this, row);
        });
        html += "</tr>";
        if(opts.treeReader.show && children) {
            $.each(opts.data, function () {
                if(this[opts.treeReader.parent_field] == row[opts.jsonReader.key_field]){
                    html += showRow(opts, this, children);
                }
            });
        }
        return html;
    }
	
	function getLevel(opts, row){
		if(opts.treeReader.show == false || opts.treeReader.parent_field == null)
			return undefined;
		if(row[opts.treeReader.parent_field] == opts.treeReader.parent_root)
			return 0;
		
		var level = 0;
		var hasParent = true;
		while(hasParent){
			hasParent = false;
			$.each(opts.data, function(){
				if(this[opts.jsonReader.key_field] == row[opts.treeReader.parent_field]){
					hasParent = true;
					level++;
					row = this;
					return false;
				}
			});
		}
		return level;
	}
	
	function formatColumn(opts, column, row){
		var html = '';
		if(column.name == "checkbox"){
			var cbClazz = $.trim(opts.gridrowcb);
			if(cbClazz.length > 0 && cbClazz[0] == ".")
				cbClazz = cbClazz.substr(1);
			html += '<td>';
			html += '	<label class="position-relative"><input type="checkbox" class="ace ' + cbClazz + '" value="' + row[opts.jsonReader.key_field] + '" /><span class="lbl"></span></label>';
			html += '</td>';
		}
		else if(column.name == "actions"){
			html += '<td>';
			if(column.list && column.list.length > 0){
				html += '<div class="hidden-sm hidden-xs btn-group">';
				$.each(column.list, function(){
					var action = opts.actions[this];
					html += '<a data-key="' + row[opts.jsonReader.key_field] + '" data-action="' + this + '" class="btn-action btn btn-xs ' + action.clazz + ' tooltip-info" data-rel="tooltip"  title="' + action.tooltip + '">';
					html += '	<i class="ace-icon ' + action.icon + ' bigger-120"></i>';
					html += '</a>';
				});
				
				html += '</div>';
				
				html += '<div class="hidden-md hidden-lg">';
				html += '	<div class="inline position-relative">';
				html += '		<button class="btn btn-minier btn-primary dropdown-toggle" data-toggle="dropdown" data-position="auto">';
				html += '			<i class="ace-icon fa fa-cog icon-only bigger-110"></i>';
				html += '		</button>';
				html += '		<ul class="dropdown-menu dropdown-only-icon dropdown-yellow dropdown-menu-right dropdown-caret dropdown-close">';
				$.each(column.list, function(){
					var action = opts.actions[this];
					html += '		<li>';
					html += '			<a data-key="' + row[opts.jsonReader.key_field] + '" data-action="' + this + '" class="btn-action btn btn-xs ' + action.clazz + ' tooltip-info" data-rel="tooltip"  title="' + action.tooltip + '">';
					html += '				<i class="ace-icon ' + action.icon + ' bigger-120"></i>';
					html += '			</a>';
					html += '		</li>';
				});
				html += '		</ul>';
				html += '	</div>';
				html += '</div>';
			}
			html += '</td>';
		}
		else{
			if(opts.treeReader.show && opts.treeReader.expand_column == column.name){
                var isLeaf = false;
                if($.type(opts.treeReader.leaf_field) === "string" && row[opts.treeReader.leaf_field]){
                    isLeaf = true;
                }
                else if($.type(opts.treeReader.leaf_field) === "function"){
                    isLeaf = opts.treeReader.leaf_field(row);
                }
                var clazz = isLeaf ? opts.treeReader.icons.leaf :
                    !row[opts.treeReader.expanded_field] ? opts.treeReader.icons.open : opts.treeReader.icons.close;
                row[opts.treeReader.level_field] = getLevel(opts, row);
				var prefix = '';
					prefix += '<div class="tree-wrap tree-wrap-ltr level' + row[opts.treeReader.level_field] + '">';
					prefix += '		<div class="' + clazz + '"></div>';
					prefix += '</div>';
				clazz = (column.clazz ? column.clazz : "");
				if(!row[opts.treeReader.leaf_field]){
					clazz = clazz.length == 0 ? opts.treeReader.branch_class.substr(1) : (clazz + " " + opts.treeReader.branch_class.substr(1));
				}
				var style = (column.style ? $.trim(column.style) : "");
				if(style.length > 0 && style[style.length - 1] != ';')
					style += ";";
				style += "text-align:left;";
				
				html += '<td class="' + clazz + '"' + ' style="' + style + '">';
				html += prefix + (!column.value ? row[column.name] : (row[column.value] || ""));
				html += '</td>';
			}else{
				html += '<td' + (column.clazz ? (' class="' + column.clazz + '"') : "") + (column.style ? (' style="' + column.style + '"') : "") + '>';
				html += (column.prefix ? column.prefix : "") + 
						(column.value ? (isUndefinedOrNull(row[column.value]) ? "" : row[column.value]) :
                                        (isUndefinedOrNull(row[column.name]) ? "" : row[column.name])) +
						(column.suffix ? column.suffix : "");
				html += '</td>';
			}
		}
		debugMsg(opts.debug, "show row column:" + column.name);
		debugMsg(opts.debug, (!column.value ? row[column.name] : (row[column.value] || "")));
		debugMsg(opts.debug, html);
		return html;
	}
	
	function showPagination(jq, total){
		var opts = $.data(jq[0], "gridview");
		if(opts.pagination.show && total > 0 && $.fn.pager){
			$(opts.pagination.selector).pager({
				recordCount: total, 
				pageIndex: opts.pagination.pageIndex, 
				pageSize: opts.pagination.pageSize,
				changePage: function(pageIndex, pageSize){
					changePage(jq, pageIndex, pageSize);
				}
			});
			jq.find(opts.gridtable + " " + opts.pagination.selector).show();
		}else{
			jq.find(opts.gridtable + " " + opts.pagination.selector).hide();
		}
	}
	
	function hidePagination(jq){
		var opts = $.data(jq[0], "gridview");
		jq.find(opts.gridtable + " " + opts.pagination.selector).hide();
	}

    function getOption(jq) {
        return $.data(jq[0], 'gridview');
    }
	
	function setOption(jq, opts) {
        var now = $.data(jq[0], 'gridview');
        $.extend(true, now, opts);
    }
	
    $.fn.gridview.methods = {
        getOption: function () {
            return getOption($(this));
        },
		setOption: function(opts){
			return setOption($(this), opts);
		},
		loadData: function(param){
			loadData($(this), param);
		},
        getRows: function(){
            return getRows($(this));
        },
        getRow: function(){
            var rows = getRows($(this), 1);
            if(rows.length > 0){
                return rows[0];
            }
        },
        getKeys: function(){
            return getKeys($(this));
        },
        getKey: function(){
            var keys = getKeys($(this), 1);
            if(keys.length > 0){
                return keys[0];
            }
        }
    }

    $.fn.gridview.defaults = {
		debug: false,
		url: null,
		timeout: 15000,
		method: 'post',
		dataType: 'json',
		jsonReader: {
			key_field: 'id',
			statu_field: 'code',
			data_field: 'data',
			total_field: 'total'
		},
		treeReader:{
			show: false,
			expaned_field: 'expanded',
			leaf_field: 'isLeaf',
			parent_field: 'parentId',
			level_field: 'level',
			parent_root: 0,
			branch_class: '.tree-node',
			icons:{
				open: 'icon-folder ace-icon fa fa-folder-open blue',
				close: 'icon-folder ace-icon fa fa-folder blue',
				leaf: 'ace-icon fa fa-book blue'
			},
			expand_column: null
		},
		actions:{
			"add":{
				tooltip: "新增",
				clazz: "btn-info btn-xs white",
				icon: "fa fa-plus-circle"
			},
			"edit":{
				tooltip: "编辑",
				clazz: "btn-info btn-xs white",
				icon: "fa fa-pencil"
			},
			"view":{
				tooltip: "查看",
				clazz: "btn-warning btn-xs white",
				icon: "fa fa-search-plus"
			},
			"delete":{
				tooltip: "删除",
				clazz: "btn-danger btn-xs white",
				icon: "fa fa-trash-o"
			},
			"search":{
				tooltip: "搜索",
				clazz: "btn-primary btn-xs white",
				icon: "fa fa-search"
			},
			"refresh":{
				tooltip: "刷新",
				clazz: "btn-primary btn-xs white",
				icon: "fa fa-refresh"
			},
            "setting":{
                tooltip: "设置",
                clazz: "btn-success btn-xs white",
                icon: "fa fa-cog"
            }
		},
		columnSize: 0,
		columns:null,
		param: null,
		filter: null,
		showData: null,
		gridfilter: '.grid-filter',
		gridaction: '.grid-action',
		gridtable: '.grid-table',
		gridform: '.grid-form',
		gridallcb: '.grid-cb-all',
		gridrowcb: '.grid-cb-row',
		pagination: {
			show: true,
			selector: ".pagination-box",
			pageIndex: 1,
			pageSize: 8,
			total: 0
		},
		form:{
			url: "action.do",
			selector: ".save-form",
			captions:{
				add: "新增记录",
				edit: "编辑记录",
				view: "查看记录"
			},
			actions:{
				add: "create",
				edit: "update"
			},
			check: function(){
				return true;
			}
		},
		search: {
			show: true,
			selector: ".search-form",
			data: null
		},
		onLoading: function(){
			showTip("正在加载，请稍后再尝试！");
		},
		onUnselect: function(){
			showAlert("请选择记录！");
		},
		onDelete: function(handler){
			showConfirm("您确定删除指定项？", handler);
		},
		onLoadSuccess: function(){
			
		},
		afterSaveSuccess: function(){
			showTip("保存成功！");
		},
		afterSaveError: function(){
			showAlert("保存失败！");
		},
		afterDeleteSuccess: function(){
			
		},
		afterDeleteError: function(){
			showAlert("删除失败！");
		},
		onEdit: function(){
			
		},
		onView: function(){
			
		}
    };
})(jQuery);