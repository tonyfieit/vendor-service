package com.redhat.svc;

import org.springframework.stereotype.Component;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import javax.persistence.EntityNotFoundException;

@Component
public class RestRouteBuilder extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		restConfiguration().bindingMode(RestBindingMode.json);
                
		onException(EntityNotFoundException.class)
			.handled(true)      
			.setHeader(Exchange.HTTP_RESPONSE_CODE, constant(404))
			.setHeader(Exchange.CONTENT_TYPE, constant("text/plain"))
			.setBody(exceptionMessage());

		rest("/vendor").get("{id}").route().routeId("getVendor")
			.log("Requested vendor id = " + "${header.id}")
			.bean(VendorDAO.class, "findVendor(${header.id})");
	}

}
