package com.cn;

import com.cn.concurrency.example.threadLocal.HttpFilter;
import com.cn.concurrency.example.threadLocal.HttpInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
public class ConcurrencyApplication extends WebMvcConfigurerAdapter {

	public static void main(String[] args) {
		SpringApplication.run(ConcurrencyApplication.class, args);
	}


	/**
	 * 实例化注册的filter
	 * @return
	 */
	@Bean
	public FilterRegistrationBean httpFilter(){

		FilterRegistrationBean registrationBean = new FilterRegistrationBean();

		registrationBean.setFilter(new HttpFilter());
		registrationBean.addUrlPatterns("threadLocal/*");//拦截的url

		return registrationBean;

	}


	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new HttpInterceptor()).addPathPatterns("/**");
	}


}

