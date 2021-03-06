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
 * Команда снять товар с полки
 */
@Schema(description = "Команда снять товар с полки")
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2021-08-29T20:10:20.018547+03:00[Europe/Moscow]")
public class PutOffRackCellCommand {
  @SerializedName("rackCellId")
  private Integer rackCellId = null;

  public PutOffRackCellCommand rackCellId(Integer rackCellId) {
    this.rackCellId = rackCellId;
    return this;
  }

   /**
   * Id полки
   * @return rackCellId
  **/
  @Schema(required = true, description = "Id полки")
  public Integer getRackCellId() {
    return rackCellId;
  }

  public void setRackCellId(Integer rackCellId) {
    this.rackCellId = rackCellId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PutOffRackCellCommand putOffRackCellCommand = (PutOffRackCellCommand) o;
    return Objects.equals(this.rackCellId, putOffRackCellCommand.rackCellId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(rackCellId);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PutOffRackCellCommand {\n");

    sb.append("    rackCellId: ").append(toIndentedString(rackCellId)).append("\n");
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
