package com.example.config;

import java.util.Collection;

import org.beetl.sql.clazz.SQLType;
import org.beetl.sql.core.ExecuteContext;
import org.beetl.sql.core.InterceptorContext;
import org.beetl.sql.core.SqlId;
import org.beetl.sql.ext.DebugInterceptor;

public class PrintSqlInterceptor extends DebugInterceptor {

	@Override
	public void after(InterceptorContext ctx) {
		ExecuteContext executeContext = ctx.getExecuteContext();
		SqlId sqlId = executeContext.sqlId;
		if (this.isSimple(sqlId)) {
			this.simpleOut(ctx);
			return;
		}
		long time = System.currentTimeMillis();
		long start = (Long) ctx.get("debug.time");
		String lineSeparator = System.getProperty("line.separator", "\n");
		StringBuilder sb = (StringBuilder) ctx.get("logs");
		sb.append("┣ 时间：\t " + (time - start) + "ms").append(lineSeparator);
		SQLType sqlType = ctx.getExecuteContext().sqlSource.sqlType;
		Object result = ctx.getExecuteContext().executeResult;
		if (sqlType.isUpdate()) {
			sb.append("┣ 更新：\t [");

			if (result.getClass().isArray()) {
				int[] ret = (int[]) result;
				for (int i = 0; i < ret.length; i++) {
					if (i > 0) {
						sb.append(",");
					}
					sb.append(ret[i]);
				}
			} else {
				sb.append(result);
			}
			sb.append("]").append(lineSeparator);
		} else {
			if (result instanceof Collection) {
				sb.append("┣ 结果：\t [").append(((Collection) result).size()).append("]").append(lineSeparator);
			} else {
				sb.append("┣ 结果：\t [").append(result).append("]").append(lineSeparator);
			}
		}
		sb.append(getMetaData(ctx));
		sb.append("┗━━━━━ Debug [").append(formatSqlId(executeContext)).append("] ━━━").append(lineSeparator);
		println(sb.toString());
	}

	private String getMetaData(InterceptorContext ctx) {
		StringBuilder sb = new StringBuilder();
		String lineSeparator = System.getProperty("line.separator", "\n");
		try {
			ExecuteContext executeContext = ctx.getExecuteContext();
			return sb
					.append("┣ 数据源：\t [").append(executeContext.sqlManager.getDs()
							.getConn(executeContext, executeContext.isUpdate).getMetaData().getURL())
					.append("]").append(lineSeparator).toString();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
