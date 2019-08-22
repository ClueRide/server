# Badge Features
This class holds the details about a Badge for presentation -- either 
a Completed Badge or an In Progress Badge.
* Title / Display Name
* Level - Aware, Adept, Advocate, Angel
* Badge ID - same as the Post ID.
* Criteria URL - WP page describing the Badge and what it takes to earn the badge
* Image URL - the visual representation of the Badge, hosted by WordPress.

## Collaboration

As shown in the diagram below, there is a one-to-one correspondence between `BadgeFeatures` and the following classes:
* `BadgeProgress` -- which is a big motivation for separating the "Features" out from a Badge.
* `BadgeStepsEntity` (_not a good name_)

Not Shown in the diagram

## Diagram
![Badge Progress Class Diagram][badge-progress-diagram]

[badge-progress-diagram]: https://raw.githubusercontent.com/jettmarks/clueRide-angular/master/cluerideDocs/badge-progress-class-relationships.png "Badge Progress Class Diagram"
