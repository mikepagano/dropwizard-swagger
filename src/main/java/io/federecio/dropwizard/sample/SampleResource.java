/*
 * Copyright © 2014 Federico Recio (N/A)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.federecio.dropwizard.sample;

import io.dropwizard.auth.Auth;
import io.dropwizard.auth.PrincipalImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.BasicAuthDefinition;
import io.swagger.annotations.OAuth2Definition;
import io.swagger.annotations.SecurityDefinition;
import io.swagger.annotations.SwaggerDefinition;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Api("/")
@Path("/")
@SwaggerDefinition(
    securityDefinition =
        @SecurityDefinition(
            basicAuthDefinitions = {@BasicAuthDefinition(key = "basic")},
            oAuth2Definitions = {
              @OAuth2Definition(
                  flow = OAuth2Definition.Flow.IMPLICIT,
                  key = "oauth2",
                  authorizationUrl = "/oauth2/auth")
            }))
public class SampleResource {

  @GET
  @Path("/hello")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Hello", notes = "Returns hello")
  @ApiResponses(value = {@ApiResponse(code = 200, message = "hello")})
  public Saying hello() {
    return new Saying("hello");
  }

  @GET
  @Path("/secret")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiOperation(
      value = "Secret",
      notes = "Returns secret",
      authorizations = {@Authorization("basic"), @Authorization("oauth2")})
  @ApiResponses(
      value = {
        @ApiResponse(
            code = 401,
            message = "Please enter basic credentials or use oauth2 authentication"),
        @ApiResponse(code = 200, message = "secret")
      })
  public Saying secret(@ApiParam(hidden = true) @Auth PrincipalImpl user) {
    return new Saying(String.format("Hi %s! It's a secret message...", user.getName()));
  }
}
