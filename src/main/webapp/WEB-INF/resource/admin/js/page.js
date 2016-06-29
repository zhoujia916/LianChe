/**
 * 在顶部显示提示消息
 * @param content  提示消息内容
 * @param delay    提示消息时长
 */
function showTip(content, delay){
	delay = delay || 3000;
    var tip = $(".page-content #tip-box");
    if(tip.length == 0){
        tip = $('<div id="tip-box" class="alert alert-info alert-tip">' + content + '</div>').appendTo(".page-content");
    }else{
        tip.html(content);
    }
    if(!tip.hasClass("disabled")){
        tip.addClass("disabled").slideDown(200, function(){
            setTimeout(function(){
                tip.slideUp(200, function(){
                    tip.removeClass("disabled");
                });
            }, delay);
        });
    }
}

/**
* 页面显示confirm对话框
* @param content  	提示消息内容
* @param onConfirm  确定按钮单击事件
*/
function showConfirm(content, onConfirm){
    showDialog("消息提示", "<i class='ace-icon fa fa-question-circle bigger-150 orange'></i>" + content, [
        {
            html: "<i class='ace-icon fa fa-close' ></i>取消",
            "class" : "btn btn-sm",
            click: function() {
                $(this).dialog( "close" );
            }
        },
        {
            html: "<i class='ace-icon fa fa-check'></i>确定",
            "class" : "btn btn-sm btn-primary",
            click: function() {
                $(this).dialog( "close" );
                if($.isFunction(onConfirm)){
                    onConfirm.call(this);
                }
            }
        }
    ], "dialog-confirm");
}



function showAlert(content, onConfirm){
    showDialog("消息提示", "<i class='ace-icon fa fa-exclamation-circle bigger-150 orange'></i>" + content, [
        {
            html: "<i class='ace-icon fa fa-check'></i>&nbsp;确定",
            "class" : "btn btn-sm btn-primary",
            click: function() {
                $(this).dialog( "close" );
                if($.isFunction(onConfirm)){
                    onConfirm.call(this);
                }
            }
        }
    ], "dialog-alert");
	
	
}

function showDialog(title, content, buttons, selector){
    if(!selector)
        selector = "dialog_" + parseInt(Math.random(101,1000)).toString();

    var dialog = $("#" + selector);
    if(dialog.length == 0){
        dialog = $('<div id="' + selector + '"></div>').appendTo("body");
    }
    dialog.html('<div class="main">' + content + '</div>');

    dialog.dialog({
        resizable: false,
        modal: true,
        title: "<div class='widget-header'><h5 class='smaller'>" + (title || "消息提示") + "</h5></div>",
        title_html: true,
        buttons: buttons
    });
}

function getQuery(name){
    var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);
    if(r!=null)return  unescape(r[2]); return null;
}


function changeHash(hash){
    if(!!hash){
        if(hash[0] == "#")
            hash = "#" + hash;
        hash = hash.replace("admin/", "page/");
        window.location.hash = hash;
    }
}

function showBootAlert(msg, callback){
    var buttons = {
        confirm: {
            label: "<i class='ace-icon fa fa-check'></i>确定",
            className: "btn-primary btn-sm",
            callback: callback
        }
    };
    showBootDialog(msg, buttons);
}

function showBootConfirm(msg, callback){
    var buttons = {
        confirm: {
            label: "<i class='ace-icon fa fa-check'></i>确定",
            className: "btn-primary btn-sm",
            callback: callback
        },
        cancel: {
            label: "<i class='ace-icon fa fa-close'></i>取消",
            className: "btn-sm",
            callback: function(){

            }
        }
    };
    showBootDialog(msg, buttons);
}


function showBootDialog(msg, handler){
    bootbox.dialog({
        message: msg,
        buttons: handler
    });
}