package com.billion.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.billion.dao.TestMapper;
import com.billion.model.entity.Test;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

/**
 * @author liqiang
 */
@Service
public class TestServiceImpl  extends ServiceImpl<TestMapper, Test> implements TestService {

}