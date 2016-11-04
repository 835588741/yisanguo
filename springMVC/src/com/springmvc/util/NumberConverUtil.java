package com.springmvc.util;

public class NumberConverUtil
{

	public static String conver(String temp)
	{
	       // 单位数组  
     String[] units = new String[] {"十", "百", "千", "万", "十", "百", "千", "亿"};  
       
     // 中文大写数字数组  
     String[] numeric = new String[] {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九"};  
       
     String res = "";  
       
         // 遍历一行中所有数字  
         for (int k = -1; temp.length() > 0; k++)  
         {  
             // 解析最后一位  
             int j = Integer.parseInt(temp.substring(temp.length() - 1, temp.length()));  
             String rtemp = numeric[j];  
               
             // 数值不是0且不是个位 或者是万位或者是亿位 则去取单位  
             if (j != 0 && k != -1 || k % 8 == 3 || k % 8 == 7)  
             {  
                 rtemp += units[k % 8];  
             }  
               
             // 拼在之前的前面  
             res = rtemp + res;  
               
             // 去除最后一位  
             temp = temp.substring(0, temp.length() - 1);  
         }  
           
         // 去除后面连续的零零..  
         while (res.endsWith(numeric[0]))  
         {  
             res = res.substring(0, res.lastIndexOf(numeric[0]));  
         }  
           
         // 将零零替换成零  
         while (res.indexOf(numeric[0] + numeric[0]) != -1)  
         {  
             res = res.replaceAll(numeric[0] + numeric[0], numeric[0]);  
         }  
           
         // 将 零+某个单位 这样的窜替换成 该单位 去掉单位前面的零  
         for (int m = 1; m < units.length; m++)  
         {  
             res = res.replaceAll(numeric[0] + units[m], units[m]);  
         }  
         
         return res;
	}
	
	public static  String getNameByMoney(long money)
	{
		if(money>=1000)
		{
			long surplusTongBan = money % 1000;
			long baiying = money / 1000;
			
			if(baiying >= 1000)
			{
				long surplusBaiYin = baiying % 1000;
				long huangjin = baiying / 1000;
				
				return ""+(huangjin > 0 ?huangjin+"两黄金":(surplusBaiYin>0? surplusBaiYin+"两白银":"")+(surplusTongBan > 0? surplusTongBan+"个铜板":""));
			}
			else
			{
				//System.out.println("baiyin="+baiying+"  tongban="+surplusTongBan);
				return ""+baiying+"两白银"+(surplusTongBan > 0 ? ""+surplusTongBan+""+"个铜板":"");
			}
		}
		else if(money > 0 && money < 1000)
		{
			return money+"个铜板";
		}
		return null;
	}
	
	/** 大写数字 */
//	public static  String getNameByMoney(long money)
//	{
//		if(money>=1000)
//		{
//			long surplusTongBan = money % 1000;
//			long baiying = money / 1000;
//			
//			if(baiying >= 1000)
//			{
//				long surplusBaiYin = baiying % 1000;
//				long huangjin = baiying / 1000;
//				
//				return ""+(huangjin > 0 ?NumberConverUtil.conver(huangjin+"")+"两黄金":"")+(surplusBaiYin>0? NumberConverUtil.conver(surplusBaiYin+"")+"两白银":"")+(surplusTongBan > 0? NumberConverUtil.conver(surplusTongBan+"")+"个铜板":"");
//			}
//			else
//			{
//				//System.out.println("baiyin="+baiying+"  tongban="+surplusTongBan);
//				return ""+baiying+"两白银"+(surplusTongBan > 0 ? ""+NumberConverUtil.conver(surplusTongBan+"")+"个铜板":"");
//			}
//		}
//		else if(money > 0 && money < 1000)
//		{
//			return ""+NumberConverUtil.conver(money+"")+"个铜板";
//		}
//		return null;
//	}

}
