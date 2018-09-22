/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: EmployeeController
 * Author:   YuSong
 * Date:     2018/9/18 23:30
 * Description: 处理员工CRUD请求
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.crud.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.raven.crud.bean.Employee;
import com.raven.crud.bean.Msg;
import com.raven.crud.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈处理员工CRUD请求〉
 *
 * @author Raven
 * @create 2018/9/18
 * @since 1.0.0
 */
@Controller
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    /**
     * 根据员工id删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public Msg deleteEmpById(@PathVariable("id") Integer id) {
        employeeService.deleteEmp(id);
        return Msg.success();
    }

    /**
     * 如果直接发送put请求
     * 封装的数据
     * <p>
     * 问题：
     * 请求体中有数据，但是对象封装不上，
     * update tbl_emp where emp_id = 1022
     * <p>
     * 原因：
     * tomcat：
     * 1、请求体重的数据，封装了一个map。
     * 2、request.getParameter("empName"),就会从这个map中取值了
     * 3、SpringMvc封装POJO对象的时候。
     * 会把POJO中每个属性的值，request.getParameter("email")
     * <p>
     * AJAX发送put请求引发的血案
     * PUT请求，请求体中的数据，request.("empName")拿不到
     * Tomcat一看是PUT不会封装请求体中的数据为map，只有post形式的请求才会封装请求体为map
     * org.apache.catalina.connector.Request--getParameter()()
     * <p>
     * 解决方案：
     * 我们要能直接发送PUT之类的请求还要封装请求体中的数据
     * 1、配置上HttpPutFormContentFilter过滤器
     * 2、这个过滤器的作用，将请求体重的的数据解析包装成一个map.
     * 3、request被重新包装，request。getparameter（）被重写，就会从自己封装的map中取数据。
     * <p>
     * 根据员工id更新方法
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "/emp/{empId}", method = RequestMethod.PUT)
    @ResponseBody
    public Msg saveEmp(Employee employee) {
        System.out.println("将要更新的员工数据：" + employee);
        employeeService.updateEmp(employee);
        return Msg.success();
    }

    /**
     * 根据id查询员工
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/emp/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Msg getEmp(@PathVariable("id") Integer id) {
        Employee employee = employeeService.getEmp(id);

        return Msg.success().add("emp", employee);
    }


    /**
     * 检查用户名是否可用
     *
     * @param empName
     * @return
     */
    @RequestMapping("/checkuser")
    @ResponseBody
    public Msg checkuse(@RequestParam("empName") String empName) {
        //判断用户名是否合法的表达式；
        String regx = "(^[A-Za-z0-9]{6,16}$)|(^[\u4e00-\u9fa5]{2,5})";
        if (!empName.matches(regx)) {
            return Msg.fail().add("vs_msg", "用户名必须是6-16位数字或者2-5位中文");
        }
        boolean b = employeeService.checkUser(empName);
        if (b) {
            return Msg.success();
        } else {
            return Msg.fail().add("vs_msg", "用户名不可用");
        }
    }

    /**
     * 员工保存
     * 1、支持JSR303校验
     * 2、导入Hibernate-Validator
     *
     * @param employee
     * @return
     */
    @RequestMapping(value = "emp", method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(@Valid Employee employee, BindingResult result) {
        if (result.hasErrors()) {
            //校验失败，应该返回失败，在模态框中显示校验失败的信息
            Map<String, Object> map = new HashMap<>();
            List<FieldError> errors = result.getFieldErrors();
            for (FieldError fieldError : errors) {
                System.out.println("错误的字段名" + fieldError.getField());
                System.out.println("错误信息：" + fieldError.getDefaultMessage());
                map.put(fieldError.getField(), fieldError.getDefaultMessage());
            }
            return Msg.fail().add("errorFields", map);
        } else {
            employeeService.saveEmp(employee);
            return Msg.success();
        }
    }

    /**
     * 导入Jackson包
     *
     * @param pn
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(
            @RequestParam(value = "pn", defaultValue = "1") Integer pn) {

        //这不是一个分页查询
        //引入PageHelper分页插件
        //只需要调用，传入页码，以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的这个就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果,只需要将PageInfo交给页面就行
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps, 5);
        return Msg.success().add("pageInfo", page);
    }

    /**
     * 查询员工数据(分页查询)
     *
     * @return
     */
    /*@GetMapping("/emps")*/
    public String getEmps(@RequestParam(value = "pn", defaultValue = "1") Integer pn,
                          Model model) {

        //这不是一个分页查询
        //引入PageHelper分页插件
        //只需要调用，传入页码，以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的这个就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果,只需要将PageInfo交给页面就行
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps, 5);
        model.addAttribute("pageInfo", page);

        return "list";
    }

}