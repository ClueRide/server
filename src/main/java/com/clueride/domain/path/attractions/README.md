# Responsibilities

* CoursePathAttractions represent the flattened Attractions in order for a given Course.
* These records are backed by a View, so instances are read-only. Updating requires 
using the Course to Path table which links Courses and Paths with an order column.
* Only IDs are carried for the Attractions.
