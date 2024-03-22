package com.acrolinx.api.request;

import com.acrolinx.api.OrderItem;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@XmlRootElement(name = "OrderRequest")
public class OrderRequest implements Serializable {

  private List<OrderItem> orderItems;
}
