package self.camel.demo;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;
import org.apache.camel.ExchangePropertyKey;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SqlAggregationStrategy implements AggregationStrategy {
	private static final Logger LOG = LoggerFactory.getLogger(SqlAggregationStrategy.class);
	@Override
	public Exchange aggregate(Exchange original, Exchange resource) {
		String resourceBody = resource != null?resource.getIn().getBody(String.class):null;
		LOG.debug("Resource body:{}",resourceBody);
		String originalBody = original!= null?original.getIn().getBody(String.class):null;
		LOG.debug("Original body:{}",originalBody);
		
		int index = Integer.valueOf((String)original.getIn().getHeader(ExchangePropertyKey.SPLIT_INDEX.name()));
		
		if(index == 1) {
			String[] headers = resourceBody.split("\\,");
			Map<String,String> headerMap = new HashMap<String,String>();
			Arrays.stream(headers).forEach(str->headerMap.put(str, ""));
			original.setProperty(ExchangePropertyKey.AGGREGATED_CORRELATION_KEY,headerMap);
		}
		else {
			@SuppressWarnings("rawtypes")
			Map dataMap = (HashMap)original.getProperty(ExchangePropertyKey.AGGREGATED_CORRELATION_KEY);
			Template template = new Template();
	        template.setData(resourceBody);	          
	        VelocityContext vc = new VelocityContext();
	        vc.put("userDataMap",dataMap);	          
	        StringWriter writer = new StringWriter();
	        template.merge(vc, writer);
	        LOG.debug("SQL string at index {}:{}",index,writer.toString());
	        original.getIn().setBody(writer.toString());
		}

		return original;
	}

}
