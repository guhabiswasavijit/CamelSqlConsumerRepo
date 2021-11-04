package self.camel.demo;

import org.apache.camel.ExchangePropertyKey;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.rabbitmq.RabbitMQConstants;
import org.springframework.stereotype.Component;


@Component("CamelSqlConsumerRoute")
public class CamelSqlConsumerRoute extends RouteBuilder {

	    @Override
	    public void configure() throws Exception {

		  from("file://{{input_directory}}/?noop=true")
		    .routeId("sqlConsumerRoute")
		    .onCompletion().onCompleteOnly()
		        .to("bean:mySqlDBHandler?method=onSuccess")
		    	.setHeader(RabbitMQConstants.ROUTING_KEY, simple("${properties:camel.rabbitmq.routingKey}"))
		    	.setBody(constant("file upload complete"))
		    	.to("rabbitmqLog:{{demo.exchangeName}}?connectionFactory=#rabbitConnectionFactory&declare=false&autoDelete=false&arg.queue.x-message-ttl=20000")
		    	.end()
		    .onException(Exception.class)
		    	.to("bean:mySqlDBHandler?method=onFailure")
		    	.setHeader(RabbitMQConstants.ROUTING_KEY, simple("${properties:camel.rabbitmq.routingKey}"))
		    	.setBody(constant("file upload failed"))
		    	.to("rabbitmqLog:{{demo.exchangeName}}?connectionFactory=#rabbitConnectionFactory&declare=false&autoDelete=false&arg.queue.x-message-ttl=20000")
		    	.end()
		 	.log("Processing ${header.CamelFileNameOnly} "+ Thread.currentThread().getName())
		 	.split(body().tokenize("\\n")).to("direct:split").end()	;
		  
		 	from("direct:split")
		 	  .routeId("sqlConsumeRecordRoute")
			  .log("Split line ${body}")
			  .log("Insert ${header.CamelSplitIndex} of ${header.CamelSplitSize}")
			  .setProperty(ExchangePropertyKey.SPLIT_INDEX.getName(),simple("${header.CamelSplitIndex}"))
			  .to("bean:sqlAggregationBean?method=aggregate")
			  .choice()
			   	.when(simple("${exchangeProperty.CamelSplitIndex} != 0"))
			   		.to("velocity:file://{{template_directory}}/{{vm_file_name}}?contentCache=true&allowTemplateFromHeader=true")			   	  
					.log("Finished Transformation:"+body())
					.to("bean:mySqlDBHandler?method=execute")
			   	.otherwise().log("Got body:"+body())
			  .endChoice()			  
		    .end();
  
	    }

}
