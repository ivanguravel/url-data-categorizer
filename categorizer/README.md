# Simple Categorizer App

this app helps user to split text which is created by crawler into different categories which should be created by user 
before.

## Assumptions
1) app is reading links text from files which were parsed by crawler
2) It's really hard to find a good approach to build great categorization without NLP, but I've tried to do that
via `aho-corasick` algorithm. I found the exising library which is created using Java.

### Usage:
1) fill in the `filesDirectory` which is also used by crawler app inside `configuration.yml` and run
   `./gradlew clean run`
2) configure category with help the following curl command: 
```
curl --header "Content-Type: application/json" \
   --request POST \
   --data '{"name": "Star Wars", "parts": ["star wars", "starwar"]}' \
   http://localhost:8080/categories
```

3) `curl --header "Content-Type: application/json" http://localhost:8080/categories/${fileId}`
where fileId variable is `id` from crawler output.
   Example: `[{"id":"9984b2a7-0","link":"https://www.starwars.com/news/everything-we-know-about-the-mandalorian"}]`
   
4) tests may be executed via `./gradlew test` or via IDE

