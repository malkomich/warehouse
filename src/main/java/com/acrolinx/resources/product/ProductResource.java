package com.acrolinx.resources.product;

import com.acrolinx.api.response.ProductInfo;
import com.acrolinx.core.FilterProductsUseCase;
import com.acrolinx.core.GetProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Path("product")
@Tag(name = "product")
@RequiredArgsConstructor
public class ProductResource {

  private final GetProductUseCase getProductUseCase;

  private final FilterProductsUseCase filterProductsUseCase;

  @GET
  @Path("{productId}")
  @Produces(MediaType.APPLICATION_JSON)
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Successful operation", content = {
          @Content(mediaType = "application/json", schema = @Schema(implementation = ProductInfo.class))
      }),
      @ApiResponse(responseCode = "400", description = "Product ID is not valid"),
      @ApiResponse(responseCode = "404", description = "Product not found")})
  public Response getProductById(@PathParam("productId") @Min(1) Integer productId) {

    return getProductUseCase.getProductById(productId)
        .map(ProductMapper::toProductInfo)
        .map(product -> Response.ok(product).build())
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
