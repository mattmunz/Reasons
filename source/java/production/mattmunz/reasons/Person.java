package mattmunz.reasons;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import mattmunz.property.PropertiedObject;
import mattmunz.property.Property;
import mattmunz.property.PropertyListBuilder;

public class Person implements PropertiedObject
{
  private final String name;
  private final String identifier; 
  
  public Person(String name) { this(name, null); }

  public Person(@JsonProperty("name") String name, 
                @JsonProperty("identifier") String identifier) 
  { 
    this.name = name; 
    this.identifier = identifier;  
  }

  public String getName() { return name; }

  // TODO Preferable to use the Optional type here
  public String getIdentifier() { return identifier; } 
  
  @Override
  public String toString() { return getToStringText(); }

  @Override
  public int hashCode() { return getHashCode(); }

  @Override
  public boolean equals(Object other) { return isEqualTo(other); }
  
  @JsonIgnore
  @Override
  public List<Property> getProperties()
  {
    return new PropertyListBuilder().add("name", name).add("identifier", identifier).build();
  }
}
