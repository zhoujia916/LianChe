package puzzle.lianche.intercept;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.StringUtil;

import javax.xml.bind.PropertyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


// 只拦截select部分
@Intercepts({
    @Signature(type = Executor.class, method = "query", args = { MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class })
})
public class PageInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(PageInterceptor.class);

    private static final String ITEM_PREFIX = "__frch_";
    private static final String DB_MYSQL = "mysql";
    private static final String DB_MSSQL = "mssql";
    private static final String DB_ORACLE = "oracle";
    private static final String DB_DB2 = "db2";



    private String dbType;// = DB_MYSQL;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        try {

            MappedStatement mappedStatement = (MappedStatement) invocation.getArgs()[0];
            Object parameter = invocation.getArgs()[1];

            BoundSql boundSql = mappedStatement.getBoundSql(parameter);
            String originalSql = boundSql.getSql() == null || "".equals(boundSql.getSql()) ? boundSql.getSql() : boundSql.getSql().trim();
            Object invocationObj = invocation.getArgs()[2];
            if (originalSql != null && invocationObj != null && invocationObj instanceof Page) {
                originalSql = originalSql.trim();
                Object parameterObject = boundSql.getParameterObject();
                if (boundSql == null || boundSql.getSql() == null || "".equals(boundSql.getSql()))
                    return null;
                int totalRecord = ((Page) invocationObj).getTotal();
                if (totalRecord == 0) {
                    StringBuffer countSql = new StringBuffer(originalSql.length() + 100);
                    countSql.append("select count(1) from (").append(originalSql).append(") total");
                    Connection connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
                    PreparedStatement countStmt = connection.prepareStatement(countSql.toString());
                    BoundSql countBS = new BoundSql(mappedStatement.getConfiguration(), countSql.toString(), boundSql.getParameterMappings(), parameterObject);

                    for (ParameterMapping mapping : boundSql.getParameterMappings()) {
                        String prop = mapping.getProperty();
                        if (boundSql.hasAdditionalParameter(prop)) {
                            countBS.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                        }
                    }

                    setParameters(countStmt, mappedStatement, countBS, parameterObject);
                    ResultSet rs = countStmt.executeQuery();
                    if (rs.next()) {
                        totalRecord = rs.getInt(1);
                    }
                    ((Page) invocationObj).setTotal(totalRecord);
                    rs.close();
                    countStmt.close();
                    connection.close();
                }
                String pageSql = generatePageSql(originalSql, ((Page) invocationObj));
                invocation.getArgs()[2] = new RowBounds(RowBounds.NO_ROW_OFFSET, RowBounds.NO_ROW_LIMIT);
                BoundSql newBoundSql = new BoundSql(mappedStatement.getConfiguration(), pageSql, boundSql.getParameterMappings(), boundSql.getParameterObject());
                MappedStatement newMs = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(newBoundSql));
                invocation.getArgs()[0] = newMs;
                return invocation.proceed();
            } else {
                return invocation.proceed();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            return invocation.proceed();
        }
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

	private String generatePageSql(String originalSql, Page page) throws PropertyException {
        String pageSql = "";
        if(dbType.equalsIgnoreCase(DB_MYSQL)){
            pageSql = originalSql + " limit " + (page.getPageIndex() - 1) * page.getPageSize() + "," + page.getPageSize();
        }
        else if(dbType.equalsIgnoreCase(DB_ORACLE)){
            pageSql = "select * from (select ROWNUM as ROWNO, t.*( " + originalSql + " ) as t) where ROWNO > "
                    + (page.getPageIndex() - 1) * page.getPageSize() + " and ROWNO < " + (page.getPageIndex() * page.getPageSize() + 1);
        }
        else if(dbType.equalsIgnoreCase(DB_DB2)){
            pageSql = "select * from (select ROW_NUMBER() OVER() AS ROWNO, t.*( " + originalSql + " ) as t) where ROWNO > "
                    + (page.getPageIndex() - 1) * page.getPageSize() + " and ROWNO < " + (page.getPageIndex() * page.getPageSize() + 1);
        }
        else if(dbType.equalsIgnoreCase(DB_MSSQL)){
            Pattern pattern = Pattern.compile("\\s+order\\s+by\\s+(\\w+)", Pattern.UNICODE_CASE);
            Matcher m = pattern.matcher(originalSql);
            String orderBy = "";
            while(m.find()){
                orderBy = m.group();
            }
            pageSql = "select * from (select ROW_NUMBER() OVER(" + orderBy + ") AS ROWNO, t.*( " + originalSql + " ) as t) where ROWNO > "
                    + (page.getPageIndex() - 1) * page.getPageSize() + " and ROWNO < " + (page.getPageIndex() * page.getPageSize() + 1);
        }
        if(logger.isDebugEnabled()){
            logger.debug(pageSql);
        }

        return pageSql;
	}

    @Override
	public void setProperties(Properties properties) {
        if(properties != null && properties.size() > 0) {
            String type = properties.getProperty("dbType");
            this.dbType = StringUtil.isNullOrEmpty(type) ? DB_MYSQL : type.toLowerCase();
        }
	}
	

    /**
     * 对SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.
     * DefaultParameterHandler
     * 
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("unchecked")
    private void setParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {
        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            Configuration configuration = mappedStatement.getConfiguration();
            TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
            MetaObject metaObject = parameterObject == null ? null : configuration.newMetaObject(parameterObject);
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    PropertyTokenizer prop = new PropertyTokenizer(propertyName);
                    if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (propertyName.startsWith(ITEM_PREFIX) && boundSql.hasAdditionalParameter(prop.getName())) {
                        value = boundSql.getAdditionalParameter(prop.getName());
                        if (value != null) {
                            value = configuration.newMetaObject(value).getValue(propertyName.substring(prop.getName().length()));
                        }
                    } else {
                        value = metaObject == null ? null : metaObject.getValue(propertyName);
                    }
                    TypeHandler<Object> typeHandler = (TypeHandler<Object>)parameterMapping.getTypeHandler();
                    if (typeHandler == null) {
                        throw new ExecutorException("There was no TypeHandler found for parameter " + propertyName + " of statement " + mappedStatement.getId());
                    }
                    typeHandler.setParameter(ps, i + 1, value, parameterMapping.getJdbcType());
                }
            }
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        Builder builder = new Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        // builder.keyProperty(ms.getKeyProperty());
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.cache(ms.getCache());
        MappedStatement newMs = builder.build();
        return newMs;
    }


    public void setDbType(String dbType){
        this.dbType = dbType;
    }

    public String getDbType(){
        return this.dbType;
    }
}
