# Badge Progress
This class holds the details about Progress toward an _incomplete_ Badge:
* Badge ID - unique identifier which matches the Post ID for the Badge.
* Badge Features - Details for displaying the badge.
* Achievements - List of Achievement instances (completed Steps).
* Steps - Complete list of required steps to earn the Badge.
* Progress - Numerical summary of the achievements toward full set of steps.

## Collaboration
As shown in the diagram below, there is a one-to-one correspondence between `BadgeProgress` and
`BadgeFeatures` which carries the details of the Badge for presentation.

There is also a one-to-many relationship with both 
a) the Set of constituent `Step` instances required to earn the badge and 
b) the List of currently earned `Achievement` instances.

## Diagram
![Badge Progress Class Diagram][badge-progress-diagram]

[badge-progress-diagram]: https://raw.githubusercontent.com/jettmarks/clueRide-angular/master/cluerideDocs/badge-progress-class-relationships.png "Badge Progress Class Diagram"
