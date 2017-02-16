package com.wong.spider.movie;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wong.spider.movie.config.ApplicationConfig;


@RunWith(SpringJUnit4ClassRunner.class)		//表示继承了SpringJUnit4ClassRunner类
@ContextConfiguration(classes={ApplicationConfig.class})
public class BaseTest {
	
	@Test
	public void base(){
		System.out.println("测试通过");
	}

}
