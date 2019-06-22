A Step is one part of earning a Badge. The Step may have its own child steps.

This is similar to a Badge in that it carries
the same BadgeFeatures.

# Responsibilities
Defines the criteria to accomplish a given Step. 

If there are no children, this step can be accomplished (earned) on its own.

# Collaboration
* Holds a BadgeFeatures instance that gives the details
of this step.
* Holds a list of Child Steps if this step is completed by
completing the children. 
* An Achievement is a given user earning this Step on
a given date -- the Achievement instance becomes an instance of the Step.