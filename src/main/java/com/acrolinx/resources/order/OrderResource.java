package com.acrolinx.resources.order;

import com.acrolinx.api.request.NewOrderRequest;
import com.acrolinx.api.request.OrderPartialUpdateRequest;
import com.acrolinx.api.request.OrderUpdateRequest;
import com.acrolinx.api.response.OrderStatus;
import com.acrolinx.core.SaveOrderUseCase;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.PATCH;
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

@Path(OrderResource.PATH)
@Tag(name = OrderResource.PATH)
@RequiredArgsConstructor
public class OrderResource {

  static final String PATH = "order";

  private final SaveOrderUseCase saveOrderUseCase;

  @OPTIONS
  public Response options() {
    return Response.ok()
        .header(HttpHeaders.ALLOW, "OPTIONS,GET,POST,PUT,PATCH,DELETE")
        .build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Successful operation"),
      @ApiResponse(responseCode = "422", description = "Invalid order data")})
  public Response placeOrder(@RequestBody @Valid NewOrderRequest newOrderRequest) {

    return saveOrderUseCase.createOrder(OrderMapper.toOrder(newOrderRequest))
        .map(orderId -> {
          var location = UriBuilder
              .fromResource(OrderResource.class)
              .path(OrderResource.class, "getOrderStatus")
              .build(orderId)
              .normalize();

          return Response.created(location).build();
        })
        .orElse(Response.accepted().build());
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
  public Response getOrderStatus(
      @PathParam("orderId") @Size(min = 24, max = 24, message = "Order id format is invalid") String orderId) {

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
  public Response updateOrder(
      @PathParam("orderId") @Size(min = 24, max = 24, message = "Order id format is invalid") String orderId,
      @RequestBody @Valid OrderUpdateRequest orderUpdateRequest) {

    return saveOrderUseCase.updateOrder(orderId, OrderMapper.toOrder(orderUpdateRequest))
        .map(order -> Response.ok(order).build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @PATCH
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
  public Response partialUpdateOrder(
      @PathParam("orderId") @Size(min = 24, max = 24, message = "Order id format is invalid") String orderId,
      @RequestBody @Valid OrderPartialUpdateRequest orderPartialUpdateRequest) {

    return saveOrderUseCase.partialUpdateOrder(orderId, OrderMapper.toOrder(orderPartialUpdateRequest))
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
  public Response cancelOrder(
      @PathParam("orderId") @Size(min = 24, max = 24, message = "Order id format is invalid") String orderId) {

    return saveOrderUseCase.deleteOrder(orderId)
        .map(order -> Response.noContent().build())
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }
}
