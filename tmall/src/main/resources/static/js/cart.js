var $cartTable;

function initCartTable() {	//初始化bootstrap-table的内容

	var str = sessionStorage.getItem('cart');
	var cart;
	if(str != null){
		cart = JSON.parse(str);
	}else{
		cart = [];
	}

	$cartTable = $('#cart_grid').bootstrapTable({
        //method: 'POST',              //请求方式（*）
        striped: true,                      //是否显示行间隔色
        cache: false,                       //是否使用缓存，默认为true，所以一般情况下需要设置一下这个属性（*）
        pagination: false,                   //是否显示分页（*）
        sortable: true,                     //是否启用排序
        sortOrder: "asc",                   //排序方式
        //sidePagination: "server",           //分页方式：client客户端分页，server服务端分页（*）
        //pageNumber: 1,                      //初始化加载第一页，默认第一页,并记录
        //pageSize: rows,                     //每页的记录行数（*）
        //pageList: [10, 25, 50, 100],        //可供选择的每页的行数（*）
        search: false,                      //是否显示表格搜索
        strictSearch: true,
        showColumns: true,                  //是否显示所有的列（选择显示的列）
        showRefresh: true,                  //是否显示刷新按钮
        minimumCountColumns: 2,             //最少允许的列数
        clickToSelect: true,                //是否启用点击选中行
        //height: 500,                      //行高，如果没有设置height属性，表格自动根据记录条数觉得表格高度
        //uniqueId: "id",                     //每一行的唯一标识，一般为主键列
        showToggle: true,                   //是否显示详细视图和列表视图的切换按钮
        cardView: false,                    //是否显示详细视图
        detailView: false,                  //是否显示父子表
        data: cart,
        columns: [ {
        	field: 'Num',
            title: '序号',//标题  可不加
            align: 'center',
            valign: 'middle',
            formatter: function (value, row, index) {
            	row.Num = index + 1;
                return index + 1;
            }
        },{
            field: 'name',
            title: '商品名称',
            sortable: true
        }, {
            field: 'price',
            title: '单价',
            sortable: true,      
            formatter: cartPriceFormatter
        }, {
            field: 'quantity',
            title: '购买数量',
            formatter: cartQuantityFormatter
        }, {
            field:'Num',
            title: '操作',
            width: 150,
            align: 'center',
            valign: 'middle',
            formatter: cartActionFormatter
        }, ],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            alert("数据加载失败！");
        }
    });
};

function cartActionFormatter(value, row, index) {	//操作栏的格式化
    var id = index+1;
    var result = "<a href=\"javascript:;\" class=\"btn btn-xs red\" onclick=\"del("+ id + ", this);\" title=\"删除\"><span class=\"glyphicon glyphicon-remove\"></span></a>";
    return result;
}

function cartQuantityFormatter(value, row, index) {	//
    var result = "<input type='text' size='3' value='"+value+"' />";
    return result;
}

function cartPriceFormatter(value, row, index) {	//
    var result = '￥'+value.toFixed(2);
    return result;
}

function del(id, e){
	$tr = $(e).parent().parent();
	var nodes = $tr.children();
	var index = parseInt(nodes[0].innerText);
	if(!confirm('您确定要删除第'+index+'行么？')){
		return;
	};
	var str = sessionStorage.getItem('cart');
	//alert(str);
	var cart;
	if(str == null||str == undefined){
		cart = [];
	} else {
		cart = JSON.parse(str);
	}

	//var ids = [];
	//ids.push(n);
	$cartTable.bootstrapTable('remove', {field:'Num',values:[index]});
	cart = $cartTable.bootstrapTable('getData');
	if(cart.length==0){
		$("#quantity").removeClass("badge");
	}
	//alert(JSON.stringify(row));
	//var item = row;
	//item.quantity = n;
	//cart.push(item);
	//alert(JSON.stringify(cart));
	sessionStorage.setItem('cart',JSON.stringify(cart));
	//$("#quantity").addClass("badge");
	//$("#quantity").css("background-color","red");
	$("#quantity").text(cart.length);
	//console.log('test.....');
	//var cart = sessionStorage.getItem('cart');
	//cart.push(row);
	//alert(cart[0]);
	var count = 0;
	var total = 0.00;
	$.each(cart, function(index,obj){
		count += obj.quantity;
		total += obj.price*obj.quantity;
	});
	$("#itemCount").text(cart.length);	
	$("#qtyCount").text(count);
	$("#priceTotal").text(total.toFixed(2));
}

function createOrder(){
    $.ajax({
         //几个参数需要注意一下
            type: "post",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            contentType: 'application/json',
            url: "order/new" ,//url
            data: sessionStorage.getItem('cart'),
            success: function (data) {
                //console.log(data);//打印服务端返回的数据(调试用)
                if (data.status == 200) {
                 sessionStorage.setItem('cart',"[]");	
                 $cartTable.bootstrapTable('removeAll');
                 $("#quantity").removeClass("badge");
                 $("#quantity").text('0');
             	$("#itemCount").text('0');	
            	$("#qtyCount").text('0');
            	$("#priceTotal").text('0.00');
                 document.location = "orderDetail.html?orderId="+data.data.id;
                }
            },
            error : function() {
                alert("异常！");
            }
        });
}
