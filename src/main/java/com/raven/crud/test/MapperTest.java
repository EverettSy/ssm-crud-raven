/**
 * Copyright (C), 2015-2018, XXX有限公司
 * FileName: MapperTest
 * Author:   YuSong
 * Date:     2018/9/18 21:26
 * Description: 测试dao层工作
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.raven.crud.test;

import com.raven.crud.bean.Department;
import com.raven.crud.bean.Employee;
import com.raven.crud.dao.DepartmentMapper;
import com.raven.crud.dao.EmployeeMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * 〈一句话功能简述〉<br>
 * 〈测试dao层的工作〉
 * 推荐Spring项目可以使用Spring的单元测试，可以自动注入我们需要的组件
 * 1.导入spring-test模块
 * 2.@ContextConfiguration指定Spring配置文件的位置
 * 3.直接autowired要使用的组件
 * @author Raven
 * @create 2018/9/18
 * @since 1.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring.xml"})
public class MapperTest {

    @Autowired
    DepartmentMapper departmentMapper;

    @Autowired
    EmployeeMapper employeeMapper;

    @Autowired
    SqlSessionTemplate sqlSessionTemplate;
    /**
     * 测试DepartmentMapper
     */
    @Test
    public void testCRUD() {

        /*//创建SpringIOC容器
        ApplicationContext act = new ClassPathXmlApplicationContext("spring.xml");
        //从容器中获取mapper
        DepartmentMapper bean = act.getBean(DepartmentMapper.class);*/
        System.out.println(departmentMapper);

      /*  //1、插入几个部门
        departmentMapper .insertSelective(new Department(null,"开发部"));
        departmentMapper .insertSelective(new Department(null,"测试部"));
        departmentMapper .insertSelective(new Department(null,"商务部"));
*/
       /* //2、插入员工信息
        employeeMapper.insertSelective(new Employee(null,"jREEY","M","sy759770423@163.com",1));*/


       //3、批量插入多个员工 ,使用可以执行批量操作的sqlSession

        /*for (){
            employeeMapper.insertSelective(new Employee(null,"jREEY","M","sy759770423@163.com",1))
        }*/

       EmployeeMapper mapper  = sqlSessionTemplate.getMapper(EmployeeMapper.class);
        for (int i = 0; i <1000 ; i++) {
            String uid = UUID.randomUUID().toString().substring(0,5)+i;
            mapper.insertSelective(new Employee(null,uid,"M","@163.com",1));
        }
        System.out.println("批量完成");
    }
}