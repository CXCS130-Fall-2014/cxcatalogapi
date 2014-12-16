#Quickstart

<pre>

    ### SETTING UP
    Project was developed with a local MySQL database.
    - Server configurations are located in com.shopzilla.service.shoppingcart.resource.SQLAccess.java at the top
    - catalog_site.sql should be a MySQL dump that the site uses

    - for a version of the site that doesn't use a live database for tags, checkout commit
    7d12864eaf15428e68c0814f44e1de316fca4f4f
    "git checkout 7d12864eaf15428e68c0814f44e1de316fca4f4f"



    ### TO COMPILE AND RUN
    "mvn clean install"
    "cd service"
    "java -jar target/shoppingcart-service-1.0-SNAPSHOT.jar server service.yaml" OR "./run.sh"
    open browser to http://localhost:7500/services/shoppingcart/v1/

    ## TROUBLESHOOT
    If the site doesn't load or stalls on loading, "CTR+C" the terminal to kill the server. Then rerun:
        "java -jar target/shoppingcart-service-1.0-SNAPSHOT.jar server service.yaml"
    OR
        "./run.sh"

</pre>

