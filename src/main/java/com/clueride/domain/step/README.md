A Step is one part of earning a Badge. It is an instance of a "Class". 
The "Class" may have its own child steps.

# Responsibilities
Identifies a specific criteria to accomplish a given Rollup Badge. 

If there are no children, this step can be accomplished (earned) on its own.

# Collaboration
* A BadgeProgress instance will hold a list of Steps.
* An Achievement is a given user earning this Step on
a given date -- the Achievement instance refers to a Step.

# Examples

* Participation Points is a "Sub-Achievement" with a "Badge" ID of 3402.
3403, 3404, 3405, and 3406 need to be awarded to achieve 3402.
* Badge 3631 (Adept Seeker) requires 3402. It is listed as a "sub-achievement_post_id" for that record
However, the *stepId* for Participation Points against the 3631 badge is 3633. (This may be a short cut to award that "instance")
* Where this falls down, we need to include the "link" to the class of child instead of the instance of child.
* Using the term Rollup might be better than Badge.
* Using the terms Class vs Instance might be better.
* Note that two badges include "Participation Points". The class is 3402 in both cases, but the instance is different in each case. Also note that the "Label" is different for the two and may be useful for carrying information about that instance. (We're leaning in that direction with the Tiny Doors, for example).