package com.springmvc.util;

import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

 

public class ResultSetTool {

	
//	@SuppressWarnings("static-access")
//	public static String xxxx(ResultSet rs)
//	{
//		
//		net.sf.json.JSONArray array = new net.sf.json.JSONArray();
//		String string = "西西西西2 ";
//		try
//		{
////			string = array.fromObject(bindDataToDTO(rs,)).toString();
//		}
//		catch (SQLException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		// 
//		
//		return string;
//		
//	}
    public static Map<String, Object> getResultMap(ResultSet rs)  
            throws SQLException {  
        Map<String, Object> hm = new HashMap<String, Object>();  
        ResultSetMetaData rsmd = rs.getMetaData();  
        int count = rsmd.getColumnCount();  
        System.out.println(" to map 111:"+count);
        try
		{
        	for (int i = 1; i <= count; i++) {  
        		String key = rsmd.getColumnLabel(i);  
        		Object value = rs.getObject(i);  
        		System.out.println(" to map 111:"+count+"  key="+key+"  values="+value);
        		hm.put(key, value);  
        	}  
			
		}
		catch (Exception e)
		{
			// TODO: handle exception
			System.out.println(" to map exception="+e.toString());
		}
        return hm;  
}  
	
	public static List resultSetToList(ResultSet rs) throws java.sql.SQLException {   
        if (rs == null)   
            return Collections.EMPTY_LIST;   
        ResultSetMetaData md = rs.getMetaData(); //得到结果集(rs)的结构信息，比如字段数、字段名等   
        int columnCount = md.getColumnCount(); //返回此 ResultSet 对象中的列数   
        List list = new ArrayList();   
        Map rowData = new HashMap();   
        while (rs.next()) {   
         rowData = new HashMap(columnCount);   
         for (int i = 1; i <= columnCount; i++) {   
                 rowData.put(md.getColumnName(i), rs.getObject(i));   
         }   
         list.add(rowData);   
         System.out.println("list:" + list.toString());   
        }   
        return list;   
}  
	
//	{
////		表1字段a:1,
//		表1字段b:2,
//		表1字段c:3,
//		
//		表2
//		[
//		 	{
//		 		表2字段a:1,
//		 		表2字段b:1,
//		 	},
//		 	{
//		 		表2字段a:1,
//		 		表2字段b:1,
//		 	}
//		 ]
//	}
	
//	{
//		表1字段a:1,
//		表1字段b:2,
//		表1字段c:3,
//		表2字段d:1,
//		表2字段e:2,
//		表2字段f:3,
//	}

	
    /**

     * 将resultSet转化为实体类（实体字段全为String类型）

     * @param rs

     * @param dto

     * @return

     * @throws Exception

     */

    public static <T> T bindDataToDTO(ResultSet rs, T dto) throws Exception { 

        //取得Method方法  

        Method[] methods = dto.getClass().getMethods(); 

        //取得ResultSet的列名  

        ResultSetMetaData rsmd = rs.getMetaData();  

        int columnsCount = rsmd.getColumnCount();  

        String[] columnNames = new String[columnsCount];  

        for (int i = 0; i < columnsCount; i++) {  

            columnNames[i] = rsmd.getColumnLabel(i + 1);  

        } 

        //遍历ResultSet  

        while (rs.next()) {  

            //反射, 从ResultSet绑定到JavaBean  

            for (int i = 0; i < columnNames.length; i++) {  

                //取得Set方法  

                String setMethodName = "set" + columnNames[i];  

                //遍历Method  

                for (int j = 0; j < methods.length; j++) {  

                    if (methods[j].getName().equalsIgnoreCase(setMethodName)) {  

                        setMethodName = methods[j].getName();  

                        Object value = rs.getObject(columnNames[i]); 

   

                        //实行Set方法  

                        try {  

                            //JavaBean内部属性和ResultSet中一致时候  

                            if(value != null) {

                                Method setMethod = dto.getClass().getMethod(  

                                        setMethodName, value.getClass());  

                                setMethod.invoke(dto, value);  

                            }

                        } catch (Exception e) {  

                            //JavaBean内部属性和ResultSet中不一致时候，使用String来输入值。  

                            Method setMethod = dto.getClass().getMethod(  

                                    setMethodName, String.class);  

                            setMethod.invoke(dto, value.toString());  

                        }  

                    }  

                }  

            }  

        } 

        return dto;  

    }

     

    /**

     * 将resultSet转化为JSON数组

     * @param rs

     * @return

     * @throws SQLException

     * @throws JSONException

     */

    public static JSONArray resultSetToJsonArry(ResultSet rs) throws SQLException,JSONException 

    { 

       // json数组 

       JSONArray array = new JSONArray(); 


       // 获取列数 

       ResultSetMetaData metaData = rs.getMetaData(); 

       int columnCount = metaData.getColumnCount(); 

         

       // 遍历ResultSet中的每条数据 

        while (rs.next()) { 

            JSONObject jsonObj = new JSONObject(); 
              

            // 遍历每一列 

            for (int i = 1; i <= columnCount; i++) { 

                String columnName =metaData.getColumnLabel(i); 

                String value = rs.getString(columnName); 

                jsonObj.put(columnName, value); 

            }  

            array.put(jsonObj);  

        } 

         

       return array; 

    }

     

    /**

     * 将resultSet转化为JSONObject

     * @param rs

     * @return

     * @throws SQLException

     * @throws JSONException

     */

    public static JSONObject resultSetToJsonObject(ResultSet rs) throws SQLException,JSONException 

    { 

       // json对象

        JSONObject jsonObj = new JSONObject();     

       // 获取列数 

       ResultSetMetaData metaData = rs.getMetaData(); 

       int columnCount = metaData.getColumnCount(); 

       // 遍历ResultSet中的每条数据 

        if (rs.next()) { 

            // 遍历每一列 

            for (int i = 1; i <= columnCount; i++) { 

                String columnName =metaData.getColumnLabel(i); 

                String value = rs.getString(columnName); 

                jsonObj.put(columnName, value); 

            }   

        }
        System.out.println("原始数据"+jsonObj.toString());
       return jsonObj; 

    }

}
