package com.zhangshuangshuang.cms.test;

import java.io.File;
import java.util.List;

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
public class GoodsTest {
	@Autowired
	RedisTemplate redisTemplate;
	@Test
	public void filetest() {
		String str = StreamUtil.readTextFile(new File("D:\\Eclipse-1705F-work-CMS\\zhangshuangshuang-senior2-week2\\src\\test\\resources\\新建文本文档.txt"));
//		然后去掉“¥”符号，再转成数字
		str = str.replace("¥", "");
		String[] split = str.split("//==");
		for (String string : split) {
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
				if(StringUtil.hasText(sp[3])) {
					if(sp[3]==null){
						sp[3]="0";
					}
					sp[3] = sp[3].replace("%", "");
					if(StringUtil.isNumber(sp[3])) {
						goods.setBaifen(sp[3].trim());
					}
				}
				for (int j = 0; j < sp.length; j++) {
					System.out.println(sp[i]);
				}
			}
//			redisTemplate.opsForList().leftPush("goods_list", goods);
		}
		
	}
	@Test
	public void listtest() {
		List<Goods> range = redisTemplate.opsForList().range("goods_list", 0, -1);
		for (Goods goods : range) {
			System.out.println(goods);
		}
	}
	
	

}
