package com.amplifyframework.datastore.generated.model;

import com.amplifyframework.core.model.temporal.Temporal;

import java.util.List;
import java.util.UUID;
import java.util.Objects;

import androidx.core.util.ObjectsCompat;

import com.amplifyframework.core.model.Model;
import com.amplifyframework.core.model.annotations.Index;
import com.amplifyframework.core.model.annotations.ModelConfig;
import com.amplifyframework.core.model.annotations.ModelField;
import com.amplifyframework.core.model.query.predicate.QueryField;

import static com.amplifyframework.core.model.query.predicate.QueryField.field;

/** This is an auto generated class representing the Task type in your schema. */
@SuppressWarnings("all")
@ModelConfig(pluralName = "Tasks")
public final class Task implements Model {
  public static final QueryField ID = field("Task", "id");
  public static final QueryField TITLE = field("Task", "title");
  public static final QueryField DESCRIPTION = field("Task", "description");
  public static final QueryField STATUS = field("Task", "status");
  public static final QueryField IMAGE = field("Task", "image");
  public static final QueryField LATITUDE = field("Task", "Latitude");
  public static final QueryField LONGITUDE = field("Task", "Longitude");
  public static final QueryField TEAM_TASKS_ID = field("Task", "teamTasksId");
  private final @ModelField(targetType="ID", isRequired = true) String id;
  private final @ModelField(targetType="String", isRequired = true) String title;
  private final @ModelField(targetType="String") String description;
  private final @ModelField(targetType="String") String status;
  private final @ModelField(targetType="String") String image;
  private final @ModelField(targetType="Float") Double Latitude;
  private final @ModelField(targetType="Float") Double Longitude;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime createdAt;
  private @ModelField(targetType="AWSDateTime", isReadOnly = true) Temporal.DateTime updatedAt;
  private final @ModelField(targetType="ID") String teamTasksId;
  public String getId() {
      return id;
  }
  
  public String getTitle() {
      return title;
  }
  
  public String getDescription() {
      return description;
  }
  
  public String getStatus() {
      return status;
  }
  
  public String getImage() {
      return image;
  }
  
  public Double getLatitude() {
      return Latitude;
  }
  
  public Double getLongitude() {
      return Longitude;
  }
  
  public Temporal.DateTime getCreatedAt() {
      return createdAt;
  }
  
  public Temporal.DateTime getUpdatedAt() {
      return updatedAt;
  }
  
  public String getTeamTasksId() {
      return teamTasksId;
  }
  
  private Task(String id, String title, String description, String status, String image, Double Latitude, Double Longitude, String teamTasksId) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.status = status;
    this.image = image;
    this.Latitude = Latitude;
    this.Longitude = Longitude;
    this.teamTasksId = teamTasksId;
  }
  
  @Override
   public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      } else if(obj == null || getClass() != obj.getClass()) {
        return false;
      } else {
      Task task = (Task) obj;
      return ObjectsCompat.equals(getId(), task.getId()) &&
              ObjectsCompat.equals(getTitle(), task.getTitle()) &&
              ObjectsCompat.equals(getDescription(), task.getDescription()) &&
              ObjectsCompat.equals(getStatus(), task.getStatus()) &&
              ObjectsCompat.equals(getImage(), task.getImage()) &&
              ObjectsCompat.equals(getLatitude(), task.getLatitude()) &&
              ObjectsCompat.equals(getLongitude(), task.getLongitude()) &&
              ObjectsCompat.equals(getCreatedAt(), task.getCreatedAt()) &&
              ObjectsCompat.equals(getUpdatedAt(), task.getUpdatedAt()) &&
              ObjectsCompat.equals(getTeamTasksId(), task.getTeamTasksId());
      }
  }
  
  @Override
   public int hashCode() {
    return new StringBuilder()
      .append(getId())
      .append(getTitle())
      .append(getDescription())
      .append(getStatus())
      .append(getImage())
      .append(getLatitude())
      .append(getLongitude())
      .append(getCreatedAt())
      .append(getUpdatedAt())
      .append(getTeamTasksId())
      .toString()
      .hashCode();
  }
  
  @Override
   public String toString() {
    return new StringBuilder()
      .append("Task {")
      .append("id=" + String.valueOf(getId()) + ", ")
      .append("title=" + String.valueOf(getTitle()) + ", ")
      .append("description=" + String.valueOf(getDescription()) + ", ")
      .append("status=" + String.valueOf(getStatus()) + ", ")
      .append("image=" + String.valueOf(getImage()) + ", ")
      .append("Latitude=" + String.valueOf(getLatitude()) + ", ")
      .append("Longitude=" + String.valueOf(getLongitude()) + ", ")
      .append("createdAt=" + String.valueOf(getCreatedAt()) + ", ")
      .append("updatedAt=" + String.valueOf(getUpdatedAt()) + ", ")
      .append("teamTasksId=" + String.valueOf(getTeamTasksId()))
      .append("}")
      .toString();
  }
  
  public static TitleStep builder() {
      return new Builder();
  }
  
  /** 
   * WARNING: This method should not be used to build an instance of this object for a CREATE mutation.
   * This is a convenience method to return an instance of the object with only its ID populated
   * to be used in the context of a parameter in a delete mutation or referencing a foreign key
   * in a relationship.
   * @param id the id of the existing item this instance will represent
   * @return an instance of this model with only ID populated
   */
  public static Task justId(String id) {
    return new Task(
      id,
      null,
      null,
      null,
      null,
      null,
      null,
      null
    );
  }
  
  public CopyOfBuilder copyOfBuilder() {
    return new CopyOfBuilder(id,
      title,
      description,
      status,
      image,
      Latitude,
      Longitude,
      teamTasksId);
  }
  public interface TitleStep {
    BuildStep title(String title);
  }
  

  public interface BuildStep {
    Task build();
    BuildStep id(String id);
    BuildStep description(String description);
    BuildStep status(String status);
    BuildStep image(String image);
    BuildStep latitude(Double latitude);
    BuildStep longitude(Double longitude);
    BuildStep teamTasksId(String teamTasksId);
  }
  

  public static class Builder implements TitleStep, BuildStep {
    private String id;
    private String title;
    private String description;
    private String status;
    private String image;
    private Double Latitude;
    private Double Longitude;
    private String teamTasksId;
    @Override
     public Task build() {
        String id = this.id != null ? this.id : UUID.randomUUID().toString();
        
        return new Task(
          id,
          title,
          description,
          status,
          image,
          Latitude,
          Longitude,
          teamTasksId);
    }
    
    @Override
     public BuildStep title(String title) {
        Objects.requireNonNull(title);
        this.title = title;
        return this;
    }
    
    @Override
     public BuildStep description(String description) {
        this.description = description;
        return this;
    }
    
    @Override
     public BuildStep status(String status) {
        this.status = status;
        return this;
    }
    
    @Override
     public BuildStep image(String image) {
        this.image = image;
        return this;
    }
    
    @Override
     public BuildStep latitude(Double latitude) {
        this.Latitude = latitude;
        return this;
    }
    
    @Override
     public BuildStep longitude(Double longitude) {
        this.Longitude = longitude;
        return this;
    }
    
    @Override
     public BuildStep teamTasksId(String teamTasksId) {
        this.teamTasksId = teamTasksId;
        return this;
    }
    
    /** 
     * @param id id
     * @return Current Builder instance, for fluent method chaining
     */
    public BuildStep id(String id) {
        this.id = id;
        return this;
    }
  }
  

  public final class CopyOfBuilder extends Builder {
    private CopyOfBuilder(String id, String title, String description, String status, String image, Double latitude, Double longitude, String teamTasksId) {
      super.id(id);
      super.title(title)
        .description(description)
        .status(status)
        .image(image)
        .latitude(latitude)
        .longitude(longitude)
        .teamTasksId(teamTasksId);
    }
    
    @Override
     public CopyOfBuilder title(String title) {
      return (CopyOfBuilder) super.title(title);
    }
    
    @Override
     public CopyOfBuilder description(String description) {
      return (CopyOfBuilder) super.description(description);
    }
    
    @Override
     public CopyOfBuilder status(String status) {
      return (CopyOfBuilder) super.status(status);
    }
    
    @Override
     public CopyOfBuilder image(String image) {
      return (CopyOfBuilder) super.image(image);
    }
    
    @Override
     public CopyOfBuilder latitude(Double latitude) {
      return (CopyOfBuilder) super.latitude(latitude);
    }
    
    @Override
     public CopyOfBuilder longitude(Double longitude) {
      return (CopyOfBuilder) super.longitude(longitude);
    }
    
    @Override
     public CopyOfBuilder teamTasksId(String teamTasksId) {
      return (CopyOfBuilder) super.teamTasksId(teamTasksId);
    }
  }
  
}
