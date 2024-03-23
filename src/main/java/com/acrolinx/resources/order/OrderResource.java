package com.acrolinx.resources.order;

import com.acrolinx.api.request.OrderRequest;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.core.SaveOrderUseCase;
import com.acrolinx.core.domain.Order;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Path("order")
@Tag(name = "order")
@RequiredArgsConstructor
public class OrderResource {

  private final SaveOrderUseCase saveOrderUseCase;

  @OPTIONS
  public Response options() {
    return Response.ok()
        .header(HttpHeaders.ALLOW, "OPTIONS,GET,POST,PUT,DELETE")
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderStatus.class))
      }),
      @ApiResponse(responseCode = "422", description = "Invalid order data")})
  public Response placeOrder(@RequestBody @Valid OrderRequest orderRequest) {

    var order = saveOrderUseCase.createOrder(OrderMapper.toOrder(orderRequest));

    var location = UriBuilder.fromMethod(OrderResource.class, "getOrderStatus")
        .build(order.getId())
        .normalize();

    return Response.created(location).entity(order).build();
  }

  @GET
  @Path("{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = OrderStatus.class))
      }),
      @ApiResponse(responseCode = "400", description = "Order ID is not valid"),
      @ApiResponse(responseCode = "404", description = "Order not found")})
  public Response getOrderStatus(@PathParam("orderId") @Min(1) Integer orderId) {

    return saveOrderUseCase.getOrder(orderId)
        .map(OrderMapper::toOrderStatus)
        .map(product -> Response.ok(product).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
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
      @ApiResponse(responseCode = "422", description = "Invalid order data"),
      @ApiResponse(responseCode = "404", description = "Order not found")})
  public Response updateOrder(@PathParam("orderId") @Min(1) Integer orderId,
                              @RequestBody @Valid OrderRequest orderRequest) {

    return saveOrderUseCase.updateOrder(orderId, OrderMapper.toOrder(orderRequest))
        .map(order -> Response.ok(order).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @DELETE
  @Path("{orderId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "204", description = "Successful operation"),
      @ApiResponse(responseCode = "400", description = "Order ID is not valid"),
      @ApiResponse(responseCode = "404", description = "Order not found")})
  public Response cancelOrder(@PathParam("orderId") @Min(1) Integer orderId) {

    return saveOrderUseCase.deleteOrder(orderId)
        .map(order -> Response.noContent().build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }
}
