/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.quarkus.component.jaxb.it;

import java.net.URI;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.camel.ProducerTemplate;
import org.apache.camel.quarkus.component.jaxb.it.model.Person;
import org.jboss.logging.Logger;

@Path("/jaxb")
@ApplicationScoped
public class JaxbResource {

    private static final Logger LOG = Logger.getLogger(JaxbResource.class);

    @Inject
    ProducerTemplate producerTemplate;

    @Path("/unmarshal-lastname")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response unmarshalLastNameFromXml(String message) throws Exception {
        LOG.infof("Sending to jaxb: %s", message);
        final Person response = producerTemplate.requestBody("direct:unmarshal", message, Person.class);
        LOG.infof("Got response from jaxb: %s", response);
        return Response
                .created(new URI("https://camel.apache.org/"))
                .entity(response.getLastName())
                .build();
    }

    @Path("/unmarshal-firstname")
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.TEXT_PLAIN)
    public Response unmarshalFirstNameFromXml(String message) throws Exception {
        LOG.infof("Sending to jaxb: %s", message);
        final Person response = producerTemplate.requestBody("direct:unmarshal-2", message, Person.class);
        LOG.infof("Got response from jaxb: %s", response);
        return Response
                .created(new URI("https://camel.apache.org/"))
                .entity(response.getFirstName())
                .build();
    }

    @Path("/marshal-firstname")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public Response marshallFirstName(String name) throws Exception {
        Person p = new Person();
        p.setFirstName(name);

        String response = producerTemplate.requestBody("direct:marshal", p, String.class);
        LOG.infof("Got response from jaxb=>: %s", response);
        return Response
                .created(new URI("https://camel.apache.org/"))
                .entity(response)
                .build();
    }

    @Path("/marshal-lastname")
    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    public Response marshallLastName(String name) throws Exception {
        Person p = new Person();
        p.setLastName(name);

        String response = producerTemplate.requestBody("direct:marshal-2", p, String.class);
        LOG.infof("Got response from jaxb=>: %s", response);
        return Response
                .created(new URI("https://camel.apache.org/"))
                .entity(response)
                .build();
    }
}
