package com.acrolinx.api.response;

import com.acrolinx.api.OrderItem;
import com.acrolinx.api.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement(name = "OrderStatus")
public class OrderStatus {

  private String id;

  private List<OrderItem> orderItems;

  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate shipDate;

  private Status status;
}
