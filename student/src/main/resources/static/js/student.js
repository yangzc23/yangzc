var $table;
var rows = 5;
function initMainTable() {	//初始化bootstrap-table的内容
    //记录页面bootstrap-table全局变量$table，方便应用
    var queryUrl = 'welcome';
    $table = $('#grid').bootstrapTable({
        url: queryUrl,                      //请求后台的URL（*）
        method: 'POST',                     //请求方式（*）
        toolbar: '.toolbar',              	//工具按钮用哪个容器
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
        uniqueId: "sno",                     //每一行的唯一标识，一般为主键列
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
            checkbox: true,
            visible: true                  //是否显示复选框  
        }, {
            field: 'sno',
            title: '学号',
            sortable: true
        }, {
            field: 'sname',
            title: '姓名',
            sortable: true
        }, {
            field: 'male',
            title: '性别',
            sortable: true,
            formatter: genderFormatter
        }, {
            field: 'birth',
            title: '生日'
        }, {
            field:'sno',
            title: '操作',
            width: 120,
            align: 'center',
            valign: 'middle',
            formatter: actionFormatter
        }, ],
        onLoadSuccess: function () {
        },
        onLoadError: function () {
            alert("数据加载失败！");
        },
        onDblClickRow: function (row, $element) {
            var id = row.sno;
            edit(id);
        },
        responseHandler: responseHandler,
    });
};


function actionFormatter(value, row, index) {	//操作栏的格式化
    var id = value;
    var result = "";
    result += "<a href='javascript:;' class='btn btn-xs green' onclick=\"view(" + id + ")\" title='查看'><span class='glyphicon glyphicon-search'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs blue' onclick=\"edit(" + id + ")\" title='编辑'><span class='glyphicon glyphicon-pencil'></span></a>";
    result += "<a href='javascript:;' class='btn btn-xs red' onclick=\"del(" + id + ")\" title='删除'><span class='glyphicon glyphicon-remove'></span></a>";
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

//性别字段格式化
function genderFormatter(value) {
    var gender;
    if (value == false) {
        color = 'Red';
        gender = '小姐姐';
    }
    else if (value == true) {
        color = 'Blue';
        gender = '大帅哥';
    }
    else { color = 'Yellow'; }

    return '<div  style="color: ' + color + '">' + gender + '</div>';
}

function add(){
    $("#subject").text("注册学生信息");
    $("#first").hide();
    $("#register")[0].reset();
    $("#photo2").attr("src","images/default.png");
    //$("#edit").css("display","block");
    $('.form_date').datetimepicker("setDate", new Date());
    $('#edit').modal('show');
}

function edit(sid){
    $("#register")[0].reset();
    $("#subject").text("修改学生信息");
    $("#first").show();
    $("#photo2").attr("src","images/default.png");
    //var sid = $(e).parent().siblings().first().text();
    //alert(sid);
    $.ajax({
        //几个参数需要注意一下
        type: "GET",//方法类型
        cache: false,
        dataType: "json",//预期服务器返回的数据类型
        url: "edit/"+sid ,//url
        success: function (data) {
            //console.log(data);//打印服务端返回的数据(调试用)
            if (data.status == 200) {
                var stu = data.data;
                $("#sid").val(stu.sno);
                $("#sname").val(stu.sname);
                if(stu.male){
                    $("#male").prop("checked",true);
                }else{
                    $("#female").prop("checked",true);
                }
                $("#birth").val(stu.birth);
                $("#filePath").val(stu.imageUrl);
                $("#photo2").attr("src",stu.imageUrl);
                $('#edit').modal('show');
            }
        },
        error : function() {
            alert("异常！");
        }
    });
}

function save(){
    $.ajax({
        //几个参数需要注意一下
        type: "post",//方法类型
        dataType: "json",//预期服务器返回的数据类型
        url: "save" ,//url
        data: $("#register").serialize(),
        success: function (data) {
            //console.log(data);//打印服务端返回的数据(调试用)
            if (data.status == 200) {
                $('#edit').modal('hide');
                //$("#edit").css("display","none");
                //$("#register")[0].reset();
                //$("#photo2").attr("src","images/default.png");
                //alert(data.message);
                $table.bootstrapTable('refresh');
            }
        },
        error : function() {
            alert("异常！");
        }
    });
}

function upload(){
    var file = $("#photo").get(0).files[0];
    var formData = new FormData();
    formData.append("source",file);
    $.ajax({
        url:"upload/file",
        type:"post",
        dataType:"json",
        cache:false,
        data:formData,
        contentType:false,
        processData:false,
        success:function(data){
            if(data.status==200){
                $("#photo2").attr("src",data.data);
                $("#filePath").val(data.data);
            }
            console.log("hello test");
        }
    });
}

function del(sid){
    //var sid = $(e).parent().siblings().first().text();
    var yesOrNo = confirm("确定要删除该学生么？");
    if(yesOrNo){
        $.ajax({
            //几个参数需要注意一下
            type: "DELETE",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "delete/"+sid ,//url
            success: function (data) {
                console.log(data.msg);//打印服务端返回的数据(调试用)
                if (data.status == 200) {
                    $table.bootstrapTable('refresh');
                }
            },
            error : function() {
                alert("异常！");
            }
        });
    }
}

function exportData(){
    //window.open("export" ,"_blank");
    var xhr ;
    if(window.XMLHttpRequest){//code for IE7+,Firefox,Chrome,Opera,Safari
        xhr = new XMLHttpRequest();
    }else{//code for IE6,IE5
        xhr = new ActiveXObject("Microsoft.XMLHTTP");
    }
    var url = 'export';
    //设置响应类型为blob类型
    xhr.responseType = "blob";
    xhr.open("post", url, true);
    xhr.onload = function () {
        if (this.status == "200") {
            var name = xhr.getResponseHeader("Content-disposition");
            var filename = name.substring(20, name.length);
            //获取响应文件流　　
            var blob = this.response;
            var reader = new FileReader();
            reader.readAsDataURL(blob);    // 转换为base64，可以直接放入a表情href
            reader.onload = function (e) {
                // 转换完成，创建一个a标签用于下载
                var a = document.createElement('a');
                a.download = filename;
                a.href = e.target.result;
                $("body").append(a);    // 修复firefox中无法触发click
                a.click();
                $(a).remove();
            }
        }
    }

    xhr.send();

}
