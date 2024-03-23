package com.acrolinx.resources;

import com.acrolinx.api.response.ProductInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Tag(name = "product")
@Path("product")
public class ProductResource {

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
    // TODO: Method stub
    return Response.ok().build();
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
  public Response filterProducts(@QueryParam("tags") List<String> tags) {
    // TODO: Method stub
    return Response.ok().build();
  }
}
