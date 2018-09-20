<%--
  Created by IntelliJ IDEA.
  User: YuSong
  Date: 2018/9/18
  Time: 23:31
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html" charset="UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
	<title>员工列表</title>
	<%
		pageContext.setAttribute("APP_PATH", request.getContextPath());
	%>
	<!-- web路径
	 不以/开始的相对路径，以当前资源的路径为基准，经常容易出错
	以/开始的相对路径，找资源，以服务器的路径为基准(http://localhost:3306/crud)需要加上项目名
	  http://localhost:3306/crud
	 -->
	<%-- 引入jQuery--%>
	<script type="text/javascript" src="${APP_PATH}/static/js/jquery-1.12.4.min.js"></script>
	<%--引入样式--%>
	<link href="${APP_PATH}/static/css/bootstrap.min.css" rel="stylesheet">
	<script src="${APP_PATH}/static/js/bootstrap.min.js"></script>
</head>
<body>
<!-- 员工新增Modal模态框 -->
<div class="modal fade" id="empAddModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
	<div class="modal-dialog" role="document">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span>
				</button>
				<h4 class="modal-title" id="myModalLabel">员工添加</h4>
			</div>
			<div class="modal-body">
				<form class="form-horizontal">
					<div class="form-group">
						<label for="empName_add_input" class="col-sm-2 control-label">姓名</label>
						<div class="col-sm-10">
							<input type="text" name="empName" class="form-control" id="empName_add_input"
							       placeholder="empName">
							<span class="help-block"></span>
						</div>
					</div>
					<div class="form-group">
						<label for="email_add_input" class="col-sm-2 control-label">email</label>
						<div class="col-sm-10">
							<input type="text" name="email" class="form-control" id="email_add_input"
							       placeholder="email@163.com">
							<%--校验状态--%>
							<span class="help-block"></span>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">gender</label>
						<div class="col-sm-10">
							<label class="radio-inline">
								<input type="radio" name="gender" id="gender1_add_input" value="M" checked="checked"> 男
							</label>
							<label class="radio-inline">
								<input type="radio" name="gender" id="gender2_add_input" value="F"> 女
							</label>
						</div>
					</div>
					<div class="form-group">
						<label for="email_add_input" class="col-sm-2 control-label">deptName</label>
						<div class="col-sm-4">
							<!-- 部门提交部门id即可-->
							<select class="form-control" name="dId" id="dept_add_select">
							
							</select>
						</div>
					</div>
				
				</form>
			</div>
			<div class="modal-footer">
				<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
				<button type="button" class="btn btn-primary" id="emp_save_btn">保存</button>
			</div>
		</div>
	</div>
</div>

<!-- 搭建显示页面-->
<div class="container">
	<!-- 标题 -->
	<div class="row">
		<div class="col-md-4 col-md-offset-4">
			<h1>客户信息管理系统</h1>
		</div>
	</div>
	<!-- 按钮 -->
	<div class="row">
		<div class="col-md-4 col-md-offset-8">
			<button class="btn btn-primary" id="emp_add_model_btn">新增</button>
			<button class="btn btn-danger">删除</button>
		</div>
	</div>
	<!-- 显示表格数据 -->
	<div class="row">
		<div class="col-md-12">
			<table class="table table-hover" id="emps_table">
				<thead>
				<tr>
					<th>编号</th>
					<th>姓名</th>
					<th>性别</th>
					<th>邮箱</th>
					<th>部门</th>
					<th>操作</th>
				</tr>
				</thead>
				<tbody>
				
				</tbody>
			</table>
		</div>
	</div>
	<!-- 显示分页信息 -->
	<div class="row">
		<!-- 分页信息-->
		<div class="col-md-6" id="page_info_area">
		
		</div>
		<!-- 分页条信息-->
		<div class="col-md-6" id="page_nav_area">
		
		</div>
	</div>
</div>
<script type="text/javascript">

    //全局定义一个总记录数变量
    var totalRecord;
    //1、页面加载完成以后，直接去发送一个ajax请求，要到分页数据
    $(function () {
        //去首页
        to_page(1);
    });

    function to_page(pn) {
        $.ajax({
            url: "${APP_PATH}/emps",
            data: "pn=" + pn,
            type: "GET",
            success: function (result) {
                //console.log(result);
                //1、解析并显示员工数据
                build_emps_table(result);
                //2、解析并显示分页信息
                build_page_info(result);
                //3、解析显示分页条数据
                build_page_nav(result);
            }
        });
    }

    function build_emps_table(result) {
        //清空表格
        $("#emps_table tbody").empty();
        var emps = result.extend.pageInfo.list;
        $.each(emps, function (index, item) {
            var empIdTd = $("<td></td>").append(item.empId);
            var empNameTd = $("<td></td>").append(item.empName);
            var genderTd = $("<td></td>").append(item.gender == "M" ? "男" : "女");
            var emailTd = $("<td></td>").append(item.email);
            var departmentTd = $("<td></td>").append(item.department.deptName);

            /**
             <button class="btn btn-primary btn-sm">
             <span class="glyphicon glyphicon-pencil" aria-hidden="true"></span>
             编辑
             </button>
             **/
                //编辑按钮
            var editBtn = $("<button></button>").addClass("btn btn-primary btn-sm")
                    .append($("<span></span>").addClass("glyphicon glyphicon-pencil")).append("编辑");
            //删除按钮
            var delBtn = $("<button></button>").addClass("btn btn-danger btn-sm")
                .append($("<span></span>").addClass("glyphicon glyphicon-trash")).append("删除");

            //将俩个按钮放到一个单元格中
            var btnTd = $("<td></td>").append(editBtn).append(" ").append(delBtn);
            //append方法执行完成以后还是返回原来的元素
            $("<tr></tr>").append(empIdTd)
                .append(empNameTd)
                .append(genderTd)
                .append(emailTd)
                .append(departmentTd)
                .append(btnTd)
                .appendTo("#emps_table tbody");
        });
    }

    //解析显示分页信息
    function build_page_info(result) {
        $("#page_info_area").empty();

        $("#page_info_area").append("当前第" +
            result.extend.pageInfo.pageNum +
            "页,总共" +
            result.extend.pageInfo.pages +
            "页,总共" +
            result.extend.pageInfo.total +
            "条记录");
        //全局总记录数
        totalRecord = result.extend.pageInfo.total;
    }

    //解析显示分页条，点击分页要能去下一页
    function build_page_nav(result) {
        $("#page_nav_area").empty();
        var ul = $("<ul></ul>").addClass("pagination");

        //构建元素
        var firstPageLi = $("<li></li>").append($("<a></a>").append("首页").attr("href", "#"));
        //前一页
        var perPageLi = $("<li></li>").append($("<a></a>").append("&laquo;"));
        if (result.extend.pageInfo.hasPreviousPage == false) {
            firstPageLi.addClass("disabled");
            perPageLi.addClass("disabled");
        } else {
            //为元素添加单击翻页的事件
            firstPageLi.click(function () {
                to_page(1);
            });
            perPageLi.click(function () {
                to_page(result.extend.pageInfo.pageNum - 1);
            });
        }

        //下一页
        var nextPageLi = $("<li></li>").append($("<a></a>").append("&raquo;"));
        var lastPageLi = $("<li></li>").append($("<a></a>").append("末页").attr("href", "#"));
        if (result.extend.pageInfo.hasNextPage == false) {
            nextPageLi.addClass("disabled");
            lastPageLi.addClass("disabled");
        } else {
            //为元素添加单击翻页的事件
            nextPageLi.click(function () {
                to_page(result.extend.pageInfo.pageNum + 1);
            });
            lastPageLi.click(function () {
                to_page(result.extend.pageInfo.pages);
            });
        }

        //添加首页和前一页的提示
        ul.append(firstPageLi).append(perPageLi);
        //遍历页码号 1,2,3,4,5,遍历给url中添加页码提示
        $.each(result.extend.pageInfo.navigatepageNums, function (index, item) {
            var numLi = $("<li></li>").append($("<a></a>").append(item));
            if (result.extend.pageInfo.pageNum == item) {
                numLi.addClass("active");
            }
            numLi.click(function () {
                to_page(item);
            });
            ul.append(numLi);
        });
        //添加下一页和末页的提示
        ul.append(nextPageLi).append(lastPageLi);

        //将ul加入到了nav
        var navEle = $("<nav></nav>").append(ul);
        navEle.appendTo("#page_nav_area");
    }

    //点击新增按钮弹出模态框
    $("#emp_add_model_btn").click(function () {
        //发送ajax请求，查出部门信息,显示到下拉列表

        getDepts();
        //弹出模态框
        $("#empAddModal").modal({
            backdrop: "static"
        });
    });

    //查出所有部门
    function getDepts() {
        $("#empAddModal select").empty();
        $.ajax({
            url: "${APP_PATH}/depts",
            type: "GET",
            success: function (result) {
                //{"code":100,"msg":"处理成功","extend":{"depts":[{"deptId":1,"deptName":"开发部"},{"deptId":2,"deptName":"测试部"},{"deptId":3,"deptName":"商务部"}]}}
                //console.log(result);
                //显示信息到下拉列表框
                //$("#dept_add_select").append("")
                var deptInfo = result.extend.depts;
                $.each(deptInfo, function (index, item) {
                    var optionEle = $("<option></option>").append(item.deptName)
                        .attr("value", item.deptId);
                    //optionEle.appendTo("#dept_add_select");
                    //查找modal的id
                    optionEle.appendTo("#empAddModal select");
                });

            }
        });
    }

    //校验表单数据
    function validate_add_form() {
        //1.拿到校验的数据，使用正则表达式校验
        var empName = $("#empName_add_input").val();
        var regName = /(^[A-Za-z0-9]{6,16}$)|(^[\u4e00-\u9fa5]{2,5})/;
        if (!regName.test(empName)) {
            //alert("用户名可以是2-5位中文或者6-16位英文和数字的组合");
            //调用校验方法
            show_validate_msg("#empName_add_input", "error", "用户名可以是2-5位中文或者6-16位英文和数字的组合");
            return false;
        } else {
            show_validate_msg("#empName_add_input", "success", "");
        }


        //2、校验邮箱
        var email = $("#email_add_input").val();
        var regEmail = /^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/;
        if (!regEmail.test(email)) {
            //alert("邮箱格式不正确");
            /*$("#email_add_input").parent().addClass("has-error");
            $("#email_add_input").next("span").text("邮箱格式不正确");*/
            show_validate_msg("#email_add_input", "error", "邮箱格式不正确");
            return false;
        } else {
            /* $("#email_add_input").parent().addClass("has-success");
			 $("#email_add_input").next("span").text("");*/
            show_validate_msg("#email_add_input", "success", "");
        }
        return true;
    }

    //显示校验的提示信息
    function show_validate_msg(ele, status, msg) {
        //清除当前元素的校验状态
	    $(ele).parent().removeClass("has-success has-error")
        $(ele).next("span").text("");
	    if ("success" == status) {
            $(ele).parent().addClass("has-success");
            $(ele).next("span").text(msg);
        } else if ("error" == status) {
            $(ele).parent().addClass("has-error");
            $(ele).next("span").text(msg);
        }
    }

    //保存员工
    $("#emp_save_btn").click(function () {
        //1、模态框中填写的表单数据提交给服务器进行保存
        if (!validate_add_form()) {
            return false;
        }
        ;
        //2、发送ajax请求保存员工
        $.ajax({
            url: "${APP_PATH}/emp",
            type: "POST",
            data: $("#empAddModal form").serialize(),
            success: function (result) {
                //alert(result.msg);
                //1、关闭模态框
                $("#empAddModal").modal('hide');
                //2、调转到最后一页,显示刚才保存的数据
                //发送ajax请求，显示最后一页数据
                to_page(totalRecord);
            }
        });

    });
</script>
</body>
</html>
