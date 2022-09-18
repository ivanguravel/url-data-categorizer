# Simple web-crawler

## Assumptions:
1) user can add the link with massive content inside. it may be parsed to megabytes of text. 
   that's why app helps us to store output data to disk
2) I assume that there is heavy load is present in the system and app should react for each user call. 
   That's why app uses Akka Http under the hood. This actor model framework could be more efficient 
   for this situation than other java servers (based on standard thread models).
3) There are ids present in the responses from app side. These ids are equal to file names which 
   will be used for further categorization. 

### Usage example:
1) run `./gradlew clean run` from command line
2) execute the following curl command: 
```
curl --header "Content-Type: application/json" \
  --request POST \
  --data '{"links": ["https://www.bbc.com", "https://www.msn.com/"]}' \
  http://localhost:9091/links
```
3) for running tests you may use `./gradlew test` 

### Required improvements:
1) add more tests
2) do async execution of the actors which are used for parsing pages (http calls via network are slow 
and huge amount of content may be generated)
3) add CRON for updating the content in parsed url files by some schedule
4) add logic for handling duplicate links (store new info in the existing file)
