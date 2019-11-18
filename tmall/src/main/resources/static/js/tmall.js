var $table;	
var rows = 3;

function initMainTable() {	//初始化bootstrap-table的内容
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = 'portal/list';
    $table = $('#grid').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'POST',                      //请求方式（*）
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: true,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        pageSize: rows,                     //每页的记录行数（*）
        pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: false,                      //是否显示表格搜索
        strictSearch: true,
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        //得到查询的参数
        queryParams : function (params) {
            //这里的键的名字和控制器的变量名必须一直，这边改动，控制器也需要改成一样的
            var temp = {   
                rows: params.limit,                         //页面大小
                page: (params.offset / params.limit) + 1,   //页码
                sort: params.sort,      //排序列名  
                sortOrder: params.order //排位命令（desc，asc） 
            };
            return JSON.stringify(temp);
        },
        columns: [{
            field: 'id',
            title: '编号',
            visible: false,
            sortable: true
        }, {
            field: 'imageUrl',
            title: '实物图片',
            width: 120,
            formatter: imageFormatter
        }, {
            field: 'name',
            title: '商品名称',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
            sortable: true,      
            formatter: priceFormatter
        }, {
            field: 'stock',
            title: '库存',
        }, {
            field: 'quantity',
            title: '购买数量',
            formatter: quantityFormatter
        }, {
            field:'id',
            title: '操作',
            width: 150,
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter
        }, ],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            alert("数据加载失败！");
        },
        responseHandler: responseHandler,
    });
};

function actionFormatter(value, row, index) {	//操作栏的格式化
    var id = value;
    var result = "<button type=\"button\" class=\"btn btn-primary\" onclick=\"add("+id+",this);\"><span class=\"glyphicon glyphicon-shopping-cart\" aria-hidden=\"true\"></span> 加入购物车</button>";
    return result;
}

/**
 * 获取返回的数据的时候做相应处理，让bootstrap table认识我们的返回格式
 * @param {Object} res
 */
function responseHandler(res) {
	 return {
		 "rows": res.data.rows, // 具体每一个bean的列表
		 "total": res.data.total // 总共有多少条返回数据
	 }
}

function quantityFormatter(value, row, index) {	//
    var result = "<input type='text' size='3' value='1' />";
    return result;
}

function priceFormatter(value, row, index) {	//
    var result = '￥'+value.toFixed(2);
    return result;
}

function imageFormatter(value, row, index){
	var result = "<img src=\""+value+"\">";
	return result;
}

function add(id, e){
	var str = sessionStorage.getItem('cart');
	//alert(str);
	var cart;
	if(str == null||str == undefined){
		cart = [];
	} else {
		cart = JSON.parse(str);
	}
	$td = $(e).parent().prev();
	var nodes = $td.children();
	var n = parseInt(nodes[0].value);
	var row = $table.bootstrapTable('getRowByUniqueId', id);
	//alert(JSON.stringify(row));
	var item = row;
	item.quantity = n;
	cart.push(item);
	alert(JSON.stringify(cart));
	sessionStorage.setItem('cart',JSON.stringify(cart));
	$("#quantity").addClass("badge");
	$("#quantity").css("background-color","red");
	//$("#quantity").addClass("bg-info");
	$("#quantity").text(cart.length);
	//console.log('test.....');
	//var cart = sessionStorage.getItem('cart');
	//cart.push(row);
	//alert(cart[0]);
}

function logout(){
    $.ajax({
    	//几个参数需要注意一下
        type: "get",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "portal/logout" ,//url
        success: function (data) {
            //console.log(data);//打印服务端返回的数据(调试用)
            if (data.status == 200) {
            	document.location = "login.html";
            }
        },
        error : function() {
            alert("异常！");
        }
    });
}

/*
 * 获取到Url里面的参数
 */
function getUrlParam(name){
	   var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	   var r = window.location.search.substr(1).match(reg);
	   if (r != null) 
		   return unescape(r[2]); 
	   return null;
}