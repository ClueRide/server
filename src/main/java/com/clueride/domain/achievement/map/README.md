# Achievement Map Package
This contains code that maps portions of a Badge Event to the Achievement to be awarded.

Different Events take different criteria, and thus build maps with different keys.

## Responsibilities
* Knows how to retrieve the relevant records.
  * Source Records by Badge Type.
  * Achievement Records by Badge.
* Builds a Map from an element in the Badge Event (Attraction Name, for example) to a list of specific achievement IDs.
* Cache these values per outing -- they need to remain unchanged throughout the course of an outing.

Some maps are one-to-one with a given event. For example, if we see a Course Completed event, we always 
award the achievement against a single ID in BadgeOS.

## Collaborations
* *Attraction ID* - For the Arrival at an Attraction event, the Attraction ID provides a unique identifier corresponding to the full details for a given record in the `location` table.
* *Location Type* - The `location` table carries an ID for the *primary* Location Type which corresponds closely with a Theme. There may not be an exact match for every Location. There may be more than one Location Type for a given Location, but first cut is to recognize a single location type per location arrival.
* *Themes* - correspond closely with Badges, either Close-Ended or Open-Ended. There may not be an official badge awarded for every location type. This map is the operational definition of which awards will be available since if we don't have an achievement in BadgeOS that we can award, there won't be a record in the map.
* *BadgeService* - A list of the available Badges will also have for each Badge the list of children steps.
* *AchievementMap* - A 'map' table that holds a record for each location and the achievement it corresponds to.
* *Missing Achievement Report* - A report listing the achievements mapped (or lack thereof) for a given set of locations.
* *Missing Location Report* - A report listing the locations (or lack thereof) for a given Badge with constituent steps.
* *Outing and Course* - list the attractions that we want to assure are covered (perhaps this is a criteria on the readiness of a location). 

## Life Cycle
* This can be verified at the time a Course is assembled.
* This can be verified at the time an Outing is assembled.
* Once an Outing has started, a cached copy of the map should be available in memory.
* Once an Outing has completed (signal from the Guide?), that cached copy can be released.

* There is probably utility in performing a verification across all Attractions (not all Locations which may be in various stages of readiness).

## References
Wiki Page for Methods of Awarding Badges: http://bikehighways.wikidot.com/methods-of-awarding-badges