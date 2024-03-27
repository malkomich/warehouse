package com.acrolinx.resources.product;

import com.acrolinx.api.response.ProductInfo;
import com.acrolinx.core.FilterProductsUseCase;
import com.acrolinx.core.GetProductUseCase;
import io.dropwizard.jersey.caching.CacheControl;
import io.lettuce.core.api.StatefulRedisConnection;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.OPTIONS;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Path("product")
@Tag(name = "product")
@RequiredArgsConstructor
public class ProductResource {

  private final GetProductUseCase getProductUseCase;

  private final FilterProductsUseCase filterProductsUseCase;

  private final StatefulRedisConnection<String, String> redisConnection;

  @OPTIONS
  public Response options() {
    return Response.ok()
        .header(HttpHeaders.ALLOW, "OPTIONS,GET")
        .build();
  }

  @GET
  @Path("{productId}")
  @Produces(MediaType.APPLICATION_JSON)
  @CacheControl(maxAge = 1, maxAgeUnit = TimeUnit.HOURS)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ProductInfo.class))
      }),
      @ApiResponse(responseCode = "400", description = "Product ID is not valid"),
      @ApiResponse(responseCode = "404", description = "Product not found")})
  public Response getProductById(
      @PathParam("productId") @Size(min = 24, max = 24, message = "Product id format is invalid") String productId) {

    var syncCommands = redisConnection.sync();
    var cachedProductInfo = syncCommands.get(productId);
    if (Objects.nonNull(cachedProductInfo)) {
      return Response.ok(ProductInfoCacheSerializer.deserialize(cachedProductInfo)).build();
    }

    return getProductUseCase.getProductById(productId)
        .map(ProductMapper::toProductInfo)
        .map(product -> {
          syncCommands.setex(productId, 3600, ProductInfoCacheSerializer.serialize(product));

          return Response.ok(product).build();
        })
        .orElse(Response.status(Response.Status.NOT_FOUND).build());
  }

  @GET
  @Path("filter")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Filter products in stock by different tags",
      description = "Filter by multiple tags, provided with comma separated strings")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ProductInfo.class))
      }),
      @ApiResponse(responseCode = "204", description = "No products found with given filters"),
      @ApiResponse(responseCode = "400", description = "No tags provided")})
  public Response filterProducts(@QueryParam("tags") @NotEmpty List<String> tags) {

    var results = filterProductsUseCase.filterProductsByTags(tags)
        .stream()
        .map(ProductMapper::toProductInfo)
        .collect(Collectors.toList());

    return results.isEmpty()
        ? Response.noContent().build()
        : Response.ok(results).build();
  }
}
