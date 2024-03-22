package com.acrolinx.resources;

import com.acrolinx.api.request.OrderRequest;
import com.acrolinx.api.response.FilterResult;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.api.response.ProductInfo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Tag(name = "order")
@Path("order")
public class OrderResource {

  @POST
  @Path("order")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderStatus.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid input")})
  public Response placeOrder(@RequestBody @Valid OrderRequest order) {
    // TODO: Method stub
    return Response.ok().build();
  }

  @GET
  @Path("{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderStatus.class))
      }),
      @ApiResponse(responseCode = "404", description = "Order not found")})
  public Response getOrderStatus(@PathParam("orderId") Integer orderId) {
    // TODO: Method stub
    return Response.ok().build();
  }

  @PUT
  @Path("{orderId}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderStatus.class))
      }),
      @ApiResponse(responseCode = "400", description = "Invalid input"),
      @ApiResponse(responseCode = "404", description = "Order not found")})
  public Response updateOrder(@PathParam("orderId") Integer orderId, @RequestBody @Valid OrderRequest order) {
    // TODO: Method stub
    return Response.ok().build();
  }

  @DELETE
  @Path("{orderId}")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation"),
      @ApiResponse(responseCode = "404", description = "Order not found")})
  public Response cancelOrder(@PathParam("orderId") Integer orderId) {
    // TODO: Method stub
    return Response.ok().build();
  }
}
