package com.acrolinx.api.response;

import com.acrolinx.api.OrderItem;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "OrderStatus")
public class OrderStatus {

  private Integer id;

  private List<OrderItem> orderItems;

  private LocalDateTime shipDate;

  private Status status;

  enum Status {
    PLACED,
    APPROVED,
    DELIVERED
  }
}
