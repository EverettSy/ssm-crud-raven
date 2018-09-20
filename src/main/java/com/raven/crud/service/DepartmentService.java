/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: DepartmentService
 * Author:   YuSong
 * Date:     2018/9/20 16:38
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.crud.service;

import com.raven.crud.bean.Department;
import com.raven.crud.bean.Msg;
import com.raven.crud.dao.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author Raven
 * @create 2018/9/20
 * @since 1.0.0
 */
@Service
public class DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;


    public List<Department> getDepts() {
        //查出的所有部门信息
        List<Department> list = departmentMapper.selectByExample(null);
        return list;
    }
}