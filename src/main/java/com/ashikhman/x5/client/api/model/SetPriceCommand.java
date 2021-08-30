/*
 * OpenAPI definition
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package com.ashikhman.x5.client.api.model;

import com.google.gson.annotations.SerializedName;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Objects;

/**
 * Команда для задания (или изменения) цены товара для покупателя.
 */
@Schema(description = "Команда для задания (или изменения) цены товара для покупателя.")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-08-29T20:10:20.018547+03:00[Europe/Moscow]")
public class SetPriceCommand {
  @SerializedName("productId")
  private Integer productId = null;

  @SerializedName("sellPrice")
  private Double sellPrice = null;

  public SetPriceCommand productId(Integer productId) {
    this.productId = productId;
    return this;
  }

   /**
   * Id товара
   * @return productId
  **/
  @Schema(required = true, description = "Id товара")
  public Integer getProductId() {
    return productId;
  }

  public void setProductId(Integer productId) {
    this.productId = productId;
  }

  public SetPriceCommand sellPrice(Double sellPrice) {
    this.sellPrice = sellPrice;
    return this;
  }

   /**
   * Цена, по которой продукт может купить покупатель.
   * @return sellPrice
  **/
  @Schema(required = true, description = "Цена, по которой продукт может купить покупатель.")
  public Double getSellPrice() {
    return sellPrice;
  }

  public void setSellPrice(Double sellPrice) {
    this.sellPrice = sellPrice;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SetPriceCommand setPriceCommand = (SetPriceCommand) o;
    return Objects.equals(this.productId, setPriceCommand.productId) &&
        Objects.equals(this.sellPrice, setPriceCommand.sellPrice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(productId, sellPrice);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SetPriceCommand {\n");

    sb.append("    productId: ").append(toIndentedString(productId)).append("\n");
    sb.append("    sellPrice: ").append(toIndentedString(sellPrice)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
