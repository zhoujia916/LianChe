package puzzle.lianche.intercept;

import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.executor.ErrorContext;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.ExecutorException;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.RoutingStatementHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.mapping.MappedStatement.Builder;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.scripting.defaults.DefaultParameterHandler;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.type.TypeHandler;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import puzzle.lianche.utils.Page;
import puzzle.lianche.utils.ReflectUtil;
import puzzle.lianche.utils.StringUtil;

import javax.xml.bind.PropertyException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//Executor、ParameterHandler、ResultSetHandler、StatementHandler
@Intercepts({
    @Signature(type=StatementHandler.class, method="prepare", args={Connection.class})
})
public class SqlInterceptor implements Interceptor {

    private static Logger logger = LoggerFactory.getLogger(SqlInterceptor.class);

    private static final String ITEM_PREFIX = "__frch_";

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        //对于StatementHandler其实只有两个实现类，一个是RoutingStatementHandler，另一个是抽象类BaseStatementHandler，
        //BaseStatementHandler有三个子类，分别是SimpleStatementHandler，PreparedStatementHandler和CallableStatementHandler，
        //SimpleStatementHandler是用于处理Statement的，PreparedStatementHandler是处理PreparedStatement的，而CallableStatementHandler是
        //处理CallableStatement的。Mybatis在进行Sql语句处理的时候都是建立的RoutingStatementHandler，而在RoutingStatementHandler里面拥有一个
        //StatementHandler类型的delegate属性，RoutingStatementHandler会依据Statement的不同建立对应的BaseStatementHandler，即SimpleStatementHandler、
        //PreparedStatementHandler或CallableStatementHandler，在RoutingStatementHandler里面所有StatementHandler接口方法的实现都是调用的delegate对应的方法。
        //我们在PageInterceptor类上已经用@Signature标记了该Interceptor只拦截StatementHandler接口的prepare方法，又因为Mybatis只有在建立RoutingStatementHandler的时候
        //是通过Interceptor的plugin方法进行包裹的，所以我们这里拦截到的目标对象肯定是RoutingStatementHandler对象。
        try {
            if(invocation.getTarget() instanceof RoutingStatementHandler){
                RoutingStatementHandler statementHandler = (RoutingStatementHandler)invocation.getTarget();

                StatementHandler delegate = (StatementHandler) ReflectUtil.getFieldValue(statementHandler, "delegate");
                BoundSql boundSql = delegate.getBoundSql();

                MappedStatement mappedStatement = (MappedStatement)ReflectUtil.getFieldValue(delegate, "mappedStatement");

                String originalSql = boundSql.getSql() == null || "".equals(boundSql.getSql()) ?
                                     boundSql.getSql() : boundSql.getSql().trim();
                Connection connection = mappedStatement.getConfiguration().getEnvironment().getDataSource().getConnection();
//                Connection connection = (Connection)invocation.getArgs()[0];

                PreparedStatement preparedStatement = connection.prepareStatement(originalSql);

                Object[] params = getParameters(preparedStatement, mappedStatement, boundSql, boundSql.getParameterObject());

                connection.close();

                StringBuffer buffer = new StringBuffer("[");
                for(int i = 0; i < params.length; i++){
                    if(i > 0){
                        buffer.append(", ");
                    }
                    buffer.append(params[i]);
                }
                buffer.append("]");

                System.out.println("prepare execute sql:" + originalSql.replaceAll("\\s+", " "));
                System.out.println("prepare execute parameters:" + buffer.toString());

                logger.debug("prepare execute sql:" + originalSql.replaceAll("\\s+", " "));
                logger.debug("prepare execute parameters:" + buffer.toString());



//                ParameterHandler parameterHandler = new DefaultParameterHandler(mappedStatement, boundSql.getParameterObject(), boundSql);
//
//                parameterHandler.setParameters(preparedStatement);


            }


        }
          catch (Exception e) {
            logger.error(e.getMessage());
        }
        return invocation.proceed();
    }

    @Override
    public Object plugin(Object arg0) {
        return Plugin.wrap(arg0, this);
    }

    @Override
	public void setProperties(Properties properties) {

	}

    /**
     * 获取SQL参数(?)设值,参考org.apache.ibatis.executor.parameter.
     * DefaultParameterHandler
     *
     * @param ps
     * @param mappedStatement
     * @param boundSql
     * @param parameterObject
     * @throws java.sql.SQLException
     */
    @SuppressWarnings("unchecked")
    private Object[] getParameters(PreparedStatement ps, MappedStatement mappedStatement, BoundSql boundSql, Object parameterObject) throws SQLException {

        ErrorContext.instance().activity("setting parameters").object(mappedStatement.getParameterMap().getId());
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            List<Object> params = new ArrayList<Object>();

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
                    params.add(value);
                }
            }

            return params.toArray();
        }
        return null;
    }
}
