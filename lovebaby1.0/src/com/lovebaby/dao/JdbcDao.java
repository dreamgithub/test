package com.lovebaby.dao;



import java.util.List;
import java.util.Map;
/**
 * @ClassName: AuthenticationServiceImpl 
 * @author likai
 * @date 2015年11月2日
 * @Description: 
 * 获取数据列表：getDatas
 * 获取单挑数据:getData
 * 无返回值批处理：batchData
 * 无返回值单条查询处理 ：audData
 * 返回整型数据：queryForInt
 */
public interface JdbcDao {
	@SuppressWarnings("rawtypes")
	List queryForList(String querySql,Object[] params);
	int queryForInt(String sql,Object[] params);
	void batchUpdate(String[] querySqlListh);
	void update(String sql,Object[] params);
	Map<String, Object> queryForMap(String sql,Object[] params);
	void batchBySimpleJdbcTemplate(String sql,List<Object[]> params);
}
