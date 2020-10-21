# Responsibilities

* PathMeta is the meta data about a Path:
  * ID for the Path
  * ID for the CourseToPath record
  * Start/End Attraction IDs
  * Whether the path has edges defined yet.
* Does not contain GeoSpatial information; use PathGeometryEntity for 
placement on a map.
* Serves when building a course and the Attractions are being 
sorted out.
  
# Collaborators

* CourseToPath is the relationship between Path and Course and holds 
the order for the Course.
* PathGeometryEntity holds the GeoSpatial info for placement on the map.
