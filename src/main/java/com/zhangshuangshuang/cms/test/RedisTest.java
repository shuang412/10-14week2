package com.zhangshuangshuang.cms.test;

import java.io.File;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhangshuangshuang.cms.domain.Goods;
import com.zhangshuangshuang.common.utils.StreamUtil;
import com.zhangshuangshuang.common.utils.StringUtil;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:redis.xml")
public class RedisTest {
	@Autowired
	RedisTemplate redisTemplate;
	@Test
	public void filetest() {
		String str = StreamUtil.readTextFile(new File("D:\\Eclipse-1705F-work-CMS\\zhangshuangshuang-senior2-week2\\src\\test\\resources\\新建文本文档.txt"));
//		然后去掉“¥”符号，再转成数字
//		str = str.replace("¥", "");
		String[] split = str.split("//,");
		for (String string : split) {
			System.out.println(string);
			Goods goods = new Goods();
			String[] sp = string.split("==");
			for (int i = 0; i < sp.length; i++) {
//				a)ID值要使用isNumber()工具方法判断是不是数字。（2分）
				if(StringUtil.isNumber(sp[0])) {
					goods.setId(sp[0].trim());
					
				}
//				b)商品名称要使用hasText()方法判断有没有值。（2分）
				if(StringUtil.hasText(sp[1])) {
					goods.setName(sp[1].trim());
//					System.out.println(sp[1]);
				}
//				价格要使用hasText()方法判断有没有值，并使用isNumber()判断是不是数字
				sp[2] = sp[2].replace("¥", "");
				if(StringUtil.hasText(sp[2]) && StringUtil.isNumber(sp[2])) {
					goods.setPrice(sp[2].trim());
				}
//				百分比使用hasText()方法判断有没有值，如果没值则默认为0，并使用isNumber()判断是不是数字。然后去掉“%”符号，再转成数字。
				sp[3] = sp[3].replace("%", "");
				if(StringUtil.hasText(sp[3])) {
					System.out.println(sp[3]);
					if(sp[3]==null){
						sp[3]="0";
					}
					if(StringUtil.isNumber(sp[3])) {
						goods.setBaifen(sp[3].trim());
					}
				}
//				然后将解析出来的106个Goods对象使用Java编程依次添加到list类型的value中
				redisTemplate.opsForList().leftPush("goods_list", goods);
			}
		}
	}
//	再在工程编写Java代码读取Redis数据，将读取结果显示在页面中（6分）
	@Test
	public void listtest() {
//		a)页面显示结果要分页，每页10条。（4分）
//		页面结果按倒序显示，
		List<Goods> range = redisTemplate.opsForList().range("goods_list", 150, 160);
		for (Goods goods : range) {
			System.out.println(goods);
		}
	}
	@Test
	public void file() {
		String str = StreamUtil.readTextFile(new File("D:\\Eclipse-1705F-work-CMS\\zhangshuangshuang-senior2-week2\\src\\test\\resources\\新建文本文档.txt"));
//		然后去掉“¥”符号，再转成数字
		str = str.replace("¥", "");
		String[] split = str.split("//==");
		for (String string : split) {
//			System.out.println(string);
			Goods goods = new Goods();
			String[] sp = string.split("==");
			for (int i = 0; i < sp.length; i++) {
//				a)ID值要使用isNumber()工具方法判断是不是数字。（2分）
				if(StringUtil.isNumber(sp[0])) {
					goods.setId(sp[0].trim());
				}
//				b)商品名称要使用hasText()方法判断有没有值。（2分）
				if(StringUtil.hasText(sp[1])) {
					goods.setName(sp[1].trim());
				}
//				价格要使用hasText()方法判断有没有值，并使用isNumber()判断是不是数字
				if(StringUtil.hasText(sp[2]) && StringUtil.isNumber(sp[2])) {
					goods.setPrice(sp[2].trim());
				}
//				百分比使用hasText()方法判断有没有值，如果没值则默认为0，并使用isNumber()判断是不是数字。然后去掉“%”符号，再转成数字。
				sp[3] = sp[3].replace("%", "");
				if(StringUtil.hasText(sp[3])) {
//					System.out.println(sp[3]);
					if(sp[3]==null){
						sp[3]="0";
					}
					if(StringUtil.isNumber(sp[3])) {
						goods.setBaifen(sp[3].trim());
					}
				}
//				然后将解析出来的106个Goods对象使用Java编程依次添加到zset类型的value中。
//				Zset每个元素的权重值为商品的已售百分比。
			
				redisTemplate.opsForZSet().add("zset", goods,i);
			}
//			redisTemplate.opsForZSet().add("goods_list", goods);
		}
	}
	@Test
	public void zsettest() {
		Set range = redisTemplate.opsForZSet().reverseRange("zset",0, -1);
		for (Object object : range) {
			System.out.println(object);
		}
	}
}
