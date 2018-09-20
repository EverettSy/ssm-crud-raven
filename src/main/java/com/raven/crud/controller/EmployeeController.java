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
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     * 员工保存
     * @param employee
     * @return
     */
    @RequestMapping(value = "emp",method = RequestMethod.POST)
    @ResponseBody
    public Msg saveEmp(Employee employee){
        employeeService.saveEmp(employee);
        return Msg.success();
    }

    /**
     * 导入Jackson包
     * @param pn
     * @return
     */
    @RequestMapping("/emps")
    @ResponseBody
    public Msg getEmpsWithJson(
            @RequestParam(value = "pn", defaultValue = "1") Integer pn){

        //这不是一个分页查询
        //引入PageHelper分页插件
        //只需要调用，传入页码，以及每页的大小
        PageHelper.startPage(pn, 5);
        //startPage后面紧跟的这个就是一个分页查询
        List<Employee> emps = employeeService.getAll();
        //使用PageInfo包装查询后的结果,只需要将PageInfo交给页面就行
        //封装了详细的分页信息，包括我们查询出来的数据,传入连续显示的页数
        PageInfo page = new PageInfo(emps,5);
        return Msg.success().add("pageInfo",page);
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