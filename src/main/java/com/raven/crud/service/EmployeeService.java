/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: EmployeeService
 * Author:   YuSong
 * Date:     2018/9/18 23:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.crud.service;

import com.raven.crud.bean.Employee;
import com.raven.crud.dao.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author Raven
 * @create 2018/9/18
 * @since 1.0.0
 */
@Service
public class EmployeeService {

    @Autowired
    EmployeeMapper employeeMapper;

    /**
     * 查询所有员工
     * @return
     */
    public List<Employee> getAll() {
        return  employeeMapper.selectByExampleWithDept(null);
    }

    /**
     * 员工保存
     * @param employee
     */
    public void saveEmp(Employee employee) {

        employeeMapper.insertSelective(employee);
    }
}