package self.camel.demo;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.springframework.stereotype.Component;


@Component("CamelSqlConsumerRoute")
public class CamelSqlConsumerRoute extends RouteBuilder {

	    @Override
	    public void configure() throws Exception {

		  from("file://{{input_directory}}/?idempotent=true&move=backup/$simple{date:now:yyyyMMdd}/$simple{file:name}")
		    .routeId("sqlConsumerRoute")
		    .onCompletion().onCompleteOnly()
		        .to("bean:mySqlDBHandler?method=onSuccess")
		    	.setHeader(RabbitMQConstants.ROUTING_KEY, simple("${properties:camel.rabbitmq.routingKey}"))
		    	.setBody(constant("file upload complete"))
		    	.to("rabbitmqLog:{{demo.exchangeName}}?connectionFactory=#rabbitConnectionFactory&declare=false&autoDelete=false&arg.queue.x-message-ttl=20000")
		    	.end()
		    .onCompletion().onFailureOnly()
		    	.to("bean:mySqlDBHandler?method=onFailure")
		    	.setHeader(RabbitMQConstants.ROUTING_KEY, simple("${properties:camel.rabbitmq.routingKey}"))
		    	.setBody(constant("file upload failed"))
		    	.to("rabbitmqLog:{{demo.exchangeName}}?connectionFactory=#rabbitConnectionFactory&declare=false&autoDelete=false&arg.queue.x-message-ttl=20000")
		    	.end()
		 	.log("Processing ${header.CamelFileNameOnly} "+ Thread.currentThread().getName())
		 	.split(body()).to("direct:split").end()	;
		  
		 	from("direct:split")
			  .log("Split line ${body}")
			  .log("Insert ${header.CamelSplitIndex} of ${header.CamelSplitSize}")
			  .enrich("file:///{{template_director}}?fileName=${vm_file_name}&noop=true",new SqlAggregationStrategy())
			  .log("Finished Transformation:"+body())
			  .to("bean:mySqlDBHandler?method=execute")
		    .end();
	  
	    }

}
