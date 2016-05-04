package com.lovebaby.dao;


import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Repository;
@Repository(value="jdbcDao")
public class JdbcDaoImpl implements JdbcDao {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@SuppressWarnings("rawtypes")
	@Override
	public List queryForList(String querySql, Object[] params) {
		jdbcTemplate.execute("set names utf8mb4");
		List dataList=(List) jdbcTemplate.queryForList(querySql, params);
		return dataList;
	}

	@Override
	public int queryForInt(String sql, Object[] params) {
		jdbcTemplate.execute("set names utf8mb4");
		int data=jdbcTemplate.queryForInt(sql,params);
		return data;
	}

	@Override
	public void batchUpdate(String[] ids) {
		jdbcTemplate.execute("set names utf8mb4");
		//批处理，只能处理没有返回值的sql
		jdbcTemplate.batchUpdate(ids);
	}

	

	
	    /* 
	    * @Description: 
	    * 
	    * @param id
	    * @return
	    * @see com.lovebaby.app.dao.JdbcDao#getDataById(java.lang.String)
	    */
	    
	@Override
	public Map<String, Object> queryForMap(String sql,Object[] params) {
		jdbcTemplate.execute("set names utf8mb4");
		Map<String, Object> map=jdbcTemplate.queryForMap(sql,params);
		return map;
	}

	
	    /* 
	    * @Description: 
	    * 
	    * @param sql
	    * @param params
	    * @return
	    * @see com.lovebaby.app.dao.JdbcDao#audData(java.lang.String, java.lang.Object[])
	    */
	    
	@Override
	public void update(String sql, Object[] params) {
		jdbcTemplate.execute("set names utf8mb4");
		jdbcTemplate.update(sql, params);

	}

	
	    /* 
	    * @Description: 批处理
	    * 
	    * @param list
	    * @return
	    * @see com.lovebaby.dao.JdbcDao#batchBySimpleJdbcTemplate(java.util.List)
	    */  
	@Override
	public void batchBySimpleJdbcTemplate(String sql,List<Object[]> params) {
		jdbcTemplate.execute("set names utf8mb4");
		SimpleJdbcTemplate simpleJdbcTemplate = new SimpleJdbcTemplate(jdbcTemplate);
		simpleJdbcTemplate.batchUpdate(sql, params);

	}


	

	
	
}
