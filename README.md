# Burp Annotate SOAP (Using the Montoya API)
__Author: Nick Coblentz__

MontoyaAnnotateSOAP is a Burp Suite plugin that adds a comment to requests in the proxy history tab showing the SOAP method called by the request for *in-scope* URLs. The logic it uses is:
- If the request URL is in scope
- If the request header contains `application/soap+xml`
- Try to find an `<a:Action ...>`methodhere`</Action>`
- And add "methodhere" to the Request's comment column in the proxy

## How to build this plugin

### Command-Line

```bash
$ ./gradlew jar
```

### InteliJ

1. Open the project in Intellij
2. Open the Gradle sidebar on the right hand side
3. Choose Tasks -> Build -> Jar

## How to add this plugin to Burp

1. Open Burp Suite
2. Go to Extensions -> Installed -> Add
   - Extension Type: Java
   - Extension file: build/libs/MontoyaAnnotateSoapMethod-1.0-SNAPSHOT.jar
