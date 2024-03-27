package com.acrolinx.api.request;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "OrderRequest")
public class OrderUpdateRequest implements Serializable {

  @NotNull
  @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Shipping date format should be yyyy-MM-dd")
  private String shipDate;

  @NotEmpty
  private List<OrderItem> orderItems;

  @NotNull
  private Status status;
}
