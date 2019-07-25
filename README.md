# clarity-challenge
Clarity IA code challenge

## Main class:
```yozanshin.org.ClarityChallengeMain```

## Script arguments:
    
    action(int):
    1 = Parse single file:
      - filePath(string) = abolute path to the file to be parsed
      - host(string) = host to be used for getting connected hosts to
      - initDatetime(timestamp) = time range init
      - endDatetime(timestamp) = time range end
      
      example: java -jar 1 <filePath> <host> <initDatetime> <endDatetime> 
      
    2 = Parse files in folder:
      - folderPath(string) = abolute path to the folder which contains all files to be parsed
      - host(string) = host to be used for getting the metrics of connected hosts
      
      example: java -jar 2 <folderPath> <host>
      
    3 = Parse unlimited file:
      - filePath(string) = abolute path to the unlimited file to be parsed
      - host(string) = host to be used for getting the metrics of connected hosts
      
      example: java -jar 3 <filePath> <host>
      
## TODO Tasks:

- Refactor to make testing easier (IoC and components injection)
- Increase test coverage
- Use `Flowable` instead of `Observable` to add back pressure strategies
- Add `Schedulers` to improve general application performance
- Improve exception handling 