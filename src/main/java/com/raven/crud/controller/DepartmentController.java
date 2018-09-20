/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DepartmentController
 * Author:   YuSong
 * Date:     2018/9/20 16:35
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.crud.controller;

import com.raven.crud.bean.Department;
import com.raven.crud.bean.Msg;
import com.raven.crud.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈处理和部门有关的请求〉
 *
 * @author Raven
 * @create 2018/9/20
 * @since 1.0.0
 */
@Controller
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 返回所有的部门信息
     * @return
     */
    @RequestMapping("/depts")
    @ResponseBody
    public Msg getDepts(){
        List<Department> list = departmentService.getDepts();
        return  Msg.success().add("depts",list);

    }

}