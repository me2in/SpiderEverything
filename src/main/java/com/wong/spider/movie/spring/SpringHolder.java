package com.wong.spider.movie.spring;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.wong.spider.annotation.Processor;
import com.wong.spider.movie.config.ApplicationConfig;
import com.wong.spider.processor.PageProcessor;
import com.wong.spider.util.MyStringUtils;

/**
 * 
 * spring 工具类
 * @author wangjuntao
 * @date 2017-2-21
 * @since 0.1
 */
public class SpringHolder {
	
	private static final ApplicationContext context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
	
	public static <T> T getBean(Class<T> requiredType){
		return context.getBean(requiredType);
	}
	
	@SuppressWarnings("unchecked")
	public static <E> List<E>  getBeansWithAnnotation (Class<? extends Annotation> annotationType){
		
		String[] beanNames = context.getBeanNamesForAnnotation(annotationType);
		List<E> results = new ArrayList<>(beanNames.length);
		for(String beanName : beanNames){
			results.add((E)context.getBean(beanName));
		}
		return results;
	}
	
	public static List<PageProcessor> getProcessor4Site(String domain){
		List<PageProcessor> siteProcessor = new ArrayList<PageProcessor>();
		List<PageProcessor> allProcessors = getBeansWithAnnotation(Processor.class);
		for(PageProcessor processor : allProcessors){
			Processor pa = processor.getClass().getAnnotation(Processor.class);
			if(MyStringUtils.isEmpty(pa.domain()) || pa.domain().equals(domain)){
				siteProcessor.add(processor);
			}
		}
		return siteProcessor;
	}
	

}
