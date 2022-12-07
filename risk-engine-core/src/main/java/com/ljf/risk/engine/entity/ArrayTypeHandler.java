package com.ljf.risk.engine.entity;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lijinfeng
 */
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({String[].class})
public class ArrayTypeHandler extends BaseTypeHandler<String[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, String[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, StringUtils.join(parameter, ","));
    }

    @Override
    public String[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return StringUtils.split(rs.getString(columnName), ",");
    }

    @Override
    public String[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return StringUtils.split(rs.getString(columnIndex), ",");
    }

    @Override
    public String[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return StringUtils.split(cs.getString(columnIndex), ",");
    }

}
