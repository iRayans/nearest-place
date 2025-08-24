## Nearest-Place Project 

#### A Java + Quarkus API that searches nearby restaurants, coffee shops, and other places using the Google Places API. 
#### It adds simple filtering and ranking (e.g., removes chain restaurants and highlights top-rated results). Searches use a 5,000 m radius (5 km).

## Purpose
I wanted an easy way to find good nearby places, but also filter out certain types (like chain restaurants) and sort them in a more useful way.
This project is a practical example of using Java, Quarkus, and the Google Places API.

### Tech Stack
- Java 21
- Quarkus
- RESTEasy + MicroProfile Rest Client
- Google Places API
- Quarkus Caching
- Swagger UI for interactive API docs


### Features
- Search for places by latitude, longitude, and type (e.g., restaurant, coffee, hamburger).
- Excludes chain restaurants/coffee shops for more authentic results.
- Ranks results by rating + number of reviews for better quality.
- Returns top 10 results with:
  - Name
  - Rating & rating count
  - Google Maps link
  - Price level
- Caching to avoid repeated API calls.

### API Documentation
Swagger UI available at:
```
http://localhost:8080/q/openapi
```
Example search:
```
GET /v1/rest/nearbySearch?lat=24.6379&lng=46.5652&type=coffee
```

Example response:
```
{
  "results": [
    {
      "restaurantName": "Drip Coffee",
      "rating": 4.2,
      "ratingCount": 3447,
      "googleMapsUri": "https://maps.google.com/?cid=...",
      "priceLevel": "PRICE_LEVEL_MODERATE"
    }
  ]
}
```


### Running Locally
1- Clone the repo
2- Add your Google Places API key to application.properties:
```
api.key=YOUR_API_KEY
```
Run:
```
./mvnw quarkus:dev
```

### Running with Docker
#### Environment Variables
**Note**: Both API_KEY and FRONTEND_URL are required. If you don’t have a frontend URL, set FRONTEND_URL="*" to run the container.
```
docker run -p 8080:8080 \
  -e API_KEY=YOUR_API_KEY \
  -e FRONTEND_URL=YOUR_URL \
  irayan68/nearest-place:latest
```
