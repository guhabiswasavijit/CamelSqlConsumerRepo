package self.camel.demo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangePropertyKey;
import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Component("sqlAggregationBean")
public class SqlAggregationBean {
	private static final Logger LOG = LoggerFactory.getLogger(SqlAggregationBean.class);
	
	
	public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
	    AtomicInteger counter = new AtomicInteger(0);
	    return item -> consumer.accept(counter.getAndIncrement(), item);
	}
	public void aggregate(Exchange original){
		String originalBody = original != null ? original.getIn().getBody(String.class) : null;		
		LOG.debug("Original body:{}", originalBody);
		LOG.debug("Exchange Property Key:{} value: {}", ExchangePropertyKey.SPLIT_INDEX.getName(),
			original.getProperty(ExchangePropertyKey.SPLIT_INDEX));
		String[] dataArray = originalBody.split("\\,");
		Map<String,Object> dataMap = new HashMap<String,Object>();
		Arrays.stream(dataArray).forEach(withCounter((dIdx, str) -> {
			StringBuffer key = new StringBuffer("clm");
			key = key.append(dIdx);
		    dataMap.put(key.toString(),str != null?str.trim():"");
		}));
		dataMap.entrySet().forEach(entry -> {
			Map.Entry<String,Object> keyVal = (Map.Entry<String,Object>) entry;
			LOG.debug("Data Entry:{},{}",keyVal.getKey(),keyVal.getValue());
		});
		VelocityContext velocityContext = new VelocityContext(dataMap);
		original.getIn().setHeader("CamelVelocityContext", velocityContext);
		
	}

}
